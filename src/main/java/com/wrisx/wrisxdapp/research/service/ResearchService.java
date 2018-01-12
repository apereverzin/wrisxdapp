package com.wrisx.wrisxdapp.research.service;

import com.wrisx.wrisxdapp.common.DigestProvider;
import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.common.RandomStringProvider;
import com.wrisx.wrisxdapp.data.request.ResearchRequest;
import com.wrisx.wrisxdapp.data.response.ResearchData;
import com.wrisx.wrisxdapp.domain.*;
import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import com.wrisx.wrisxdapp.errorhandling.ResourceNotFoundException;
import com.wrisx.wrisxdapp.purchase.service.PurchaseService;
import com.wrisx.wrisxdapp.research.data.ResearchFile;
import net.lingala.zip4j.core.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

import static com.wrisx.wrisxdapp.domain.State.COMMITTED;
import static com.wrisx.wrisxdapp.domain.State.CONFIRMED;
import static com.wrisx.wrisxdapp.user.service.UserService.MD5_ALGORITHM;
import static com.wrisx.wrisxdapp.util.WrisxUtil.getKeywordList;
import static com.wrisx.wrisxdapp.util.WrisxUtil.getListFromIterable;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class ResearchService {
    private final Logger logger = LoggerFactory.getLogger(ResearchService.class);

    public static final String ZIP_EXTENSION = ".zip";
    private static final int PASSWORD_LENGTH = 10;

    @Autowired
    private Environment env;

    private final FileUploadProvider fileUploadProvider;
    private final RandomStringProvider randomStringProvider;
    private final ZipFileProvider zipFileProvider;
    private final DigestProvider digestProvider;
    private final ResearchDao researchDao;
    private final EntityProvider entityProvider;
    private final ResearchEnquiryDao researchEnquiryDao;
    private final EnquiryBidDao enquiryBidDao;
    private final PurchaseService purchaseService;
    private final PurchaseDao purchaseDao;

    @Autowired
    public ResearchService(FileUploadProvider fileUploadProvider,
                           RandomStringProvider randomStringProvider,
                           ZipFileProvider zipFileProvider,
                           DigestProvider digestProvider,
                           ResearchDao researchDao,
                           EntityProvider entityProvider,
                           ResearchEnquiryDao researchEnquiryDao,
                           EnquiryBidDao enquiryBidDao,
                           PurchaseService purchaseService,
                           PurchaseDao purchaseDao) {
        this.fileUploadProvider = fileUploadProvider;
        this.randomStringProvider = randomStringProvider;
        this.zipFileProvider = zipFileProvider;
        this.digestProvider = digestProvider;
        this.researchDao = researchDao;
        this.entityProvider = entityProvider;
        this.researchEnquiryDao = researchEnquiryDao;
        this.enquiryBidDao = enquiryBidDao;
        this.purchaseService = purchaseService;
        this.purchaseDao = purchaseDao;
    }

    public ResearchFile saveUploadedFile(MultipartFile file) {
        String directory = env.getProperty("wrisx.paths.uploadedFiles");
        File researchFile = fileUploadProvider.uploadFile(file, directory);

        String password = randomStringProvider.getRandomString(PASSWORD_LENGTH);
        ZipFile zipFile =
                zipFileProvider.zipAndProtectFile(researchFile, directory, password);

        String zipFileChecksumMD5 =
                digestProvider.getFileDigest(zipFile.getFile(), MD5_ALGORITHM);

        researchFile.delete();

        String zipFileName = zipFile.getFile().getName();
        return new ResearchFile(
                zipFileName.substring(0, zipFileName.indexOf(ZIP_EXTENSION)),
                password, zipFileChecksumMD5);
    }

    @Transactional
    public ResearchData saveResearch(ResearchRequest researchRequest)
            throws ResourceNotFoundException {
        Research research = createResearch(researchRequest);

        if (researchRequest.getBidId() > 0) {
            EnquiryBid enquiryBid = enquiryBidDao.findOne(researchRequest.getBidId());

            if (enquiryBid == null) {
                throw new ResourceNotFoundException(MessageFormat.format(
                        "Bid not found {0}", researchRequest.getBidId()));
            }

            updateEnquiryBid(researchRequest.getEnquiryId(), enquiryBid, research);

            Client client = entityProvider.getClientByAddress(researchRequest.getClientAddress());

            purchaseService.createPurchase(client, research, enquiryBid.getPrice());
        }

        return new ResearchData(research);
    }

    @Transactional
    public void deleteResearch(String uuid) throws ResourceNotFoundException {
        Research research = entityProvider.getResearchByUuid(uuid);

        purchaseDao.findByResearch(research).forEach(
                purchase -> purchaseDao.delete(purchase));
        enquiryBidDao.findByResearch(research).forEach(
                enquiryBid -> {
                    enquiryBid.setResearch(null);
                    enquiryBidDao.save(enquiryBid);
                });
        researchDao.delete(research);
    }

    @Transactional
    public void confirmResearchCreation(String uuid, String transactionHash)
            throws ResourceNotFoundException {
        Research research = entityProvider.getResearchByUuid(uuid);

        purchaseDao.findByResearch(research).forEach(
                purchase -> {
                    purchase.setState(CONFIRMED);
                    purchaseDao.save(purchase);
                });
        research.setPassword(null);
        research.setState(CONFIRMED);
        research.setTransactionHash(transactionHash);

        researchDao.save(research);
    }

    @Transactional
    public void commitResearchCreation(String uuid, String transactionHash)
            throws ResourceNotFoundException {
        Research research =
                entityProvider.getResearchByUuidAndTransactionHash(uuid, transactionHash);

        if (research.getState() != CONFIRMED) {
            throw new BadRequestException(MessageFormat.format(
                    "Illegal state of research {0}", uuid));
        }

        purchaseDao.findByResearch(research).forEach(
                purchase -> {
                    if (purchase.getState() != CONFIRMED) {
                        throw new BadRequestException(MessageFormat.format(
                                "Illegal state of purchase {0}", purchase.getId()));
                    }
                    purchase.setState(COMMITTED);
                    purchaseDao.save(purchase);
                });
        research.setState(COMMITTED);

        researchDao.save(research);
    }

    public List<ResearchData> getExpertResearchItems(String expertAddress)
            throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(expertAddress);

        return getListFromIterable(researchDao.findByExpert(expert)).stream().
                sorted(comparing(Research::getTimestamp).reversed()).
                map(research -> new ResearchData(research)).
                collect(toList());
    }

    public ResearchData getResearch(String uuid) throws ResourceNotFoundException {
        Research research = entityProvider.getResearchByUuid(uuid);

        return new ResearchData(research);
    }

    public List<ResearchData> findResearchItems(String clientAddress, String keywords)
            throws ResourceNotFoundException {
        Client client = entityProvider.getClientByAddress(clientAddress);

        List<String> keywordList = getKeywordList(keywords);

        List<Research> researchItems =
                getListFromIterable(researchDao.findAll());
        return (keywordList.isEmpty() ? researchItems.stream() :
                researchItems.stream().
                        filter(item -> {
                            List<String> itemKeywordList = getKeywordList(item.getKeywords());
                            return itemKeywordList.containsAll(keywordList);
                        })).
                sorted(comparing(Research::getTimestamp)).
                map(item -> {
                    List<Purchase> purchases =
                            purchaseDao.findByClientAndResearch(client, item);
                    if (purchases.isEmpty()) {
                        return new ResearchData(item);
                    }
                    return new ResearchData(item, purchases.get(0));
                }).
                collect(toList());
    }

    public List<ResearchData> findResearchItemsByKeywords(String keywords) {
        List<String> keywordList = getKeywordList(keywords);

        List<Research> researchItems =
                getListFromIterable(researchDao.findAll());
        return (keywordList.isEmpty() ? researchItems.stream() :
                researchItems.stream().
                        filter(item -> {
                            List<String> itemKeywordList = getKeywordList(item.getKeywords());
                            return itemKeywordList.containsAll(keywordList);
                        })).
                sorted(comparing(Research::getTimestamp)).
                map(item -> new ResearchData(item)).
                collect(toList());
    }

    public File getResearchFile(String fileName) {
        String directory = env.getProperty("wrisx.paths.uploadedFiles");
        String filepath = Paths.get(directory, fileName + ZIP_EXTENSION).toString();
        return new File(filepath);
    }

    private Research createResearch(ResearchRequest researchRequest)
            throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(researchRequest.getExpertAddress());

        Research research = new Research(researchRequest.getUuid(), researchRequest.getPrice(), researchRequest.getTitle(), researchRequest.getDescription(),
                researchRequest.getKeywords(), researchRequest.getChecksum(), researchRequest.getPassword(), expert);
        research = researchDao.save(research);

        return research;
    }

    private void updateEnquiryBid(long enquiryId, EnquiryBid enquiryBid,
                                  Research research) throws ResourceNotFoundException {
        ResearchEnquiry researchEnquiry = researchEnquiryDao.findOne(enquiryId);

        if (researchEnquiry == null) {
            throw new ResourceNotFoundException(MessageFormat.format(
                    "Research enquiry not found {0}", enquiryId));
        }

        enquiryBid.setResearch(research);
        enquiryBidDao.save(enquiryBid);
    }
}
