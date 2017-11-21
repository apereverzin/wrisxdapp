package com.wrisx.wrisxdapp.research.service;

import com.wrisx.wrisxdapp.data.ResearchData;
import com.wrisx.wrisxdapp.domain.*;
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

import static com.wrisx.wrisxdapp.util.WrisxUtil.getKeywordList;
import static com.wrisx.wrisxdapp.util.WrisxUtil.getListFromIterable;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class ResearchService {
    private final Logger logger = LoggerFactory.getLogger(ResearchService.class);

    public static final String ZIP_EXTENSION = ".zip";
    private static final int PASSWORD_LENGTH = 10;
    private static final String MD5_ALGORITHM = "MD5";

    @Autowired
    private Environment env;

    private final FileUploadProvider fileUploadProvider;
    private final PasswordProvider passwordProvider;
    private final ZipFileProvider zipFileProvider;
    private final ChecksumProvider checksumProvider;
    private final ResearchDao researchDao;
    private final ExpertDao expertDao;
    private final ClientDao clientDao;
    private final ResearchEnquiryDao researchEnquiryDao;
    private final EnquiryBidDao enquiryBidDao;
    private final PurchaseService purchaseService;
    private final PurchaseDao purchaseDao;

    @Autowired
    public ResearchService(FileUploadProvider fileUploadProvider,
                           PasswordProvider passwordProvider,
                           ZipFileProvider zipFileProvider,
                           ChecksumProvider checksumProvider,
                           ResearchDao researchDao,
                           ExpertDao expertDao,
                           ClientDao clientDao,
                           ResearchEnquiryDao researchEnquiryDao,
                           EnquiryBidDao enquiryBidDao,
                           PurchaseService purchaseService,
                           PurchaseDao purchaseDao) {
        this.fileUploadProvider = fileUploadProvider;
        this.passwordProvider = passwordProvider;
        this.zipFileProvider = zipFileProvider;
        this.checksumProvider = checksumProvider;
        this.researchDao = researchDao;
        this.expertDao = expertDao;
        this.clientDao = clientDao;
        this.researchEnquiryDao = researchEnquiryDao;
        this.enquiryBidDao = enquiryBidDao;
        this.purchaseService = purchaseService;
        this.purchaseDao = purchaseDao;
    }

    public ResearchFile saveUploadedFile(MultipartFile file) {
        try {
            String directory = env.getProperty("wrisx.paths.uploadedFiles");
            File researchFile = fileUploadProvider.uploadFile(file, directory);

            String password = passwordProvider.getRandomPassword(PASSWORD_LENGTH);
            ZipFile zipFile =
                    zipFileProvider.zipAndProtectFile(researchFile, directory, password);

            String zipFileChecksumMD5 =
                    checksumProvider.getFileChecksum(zipFile.getFile(), MD5_ALGORITHM);

            researchFile.delete();

            String zipFileName = zipFile.getFile().getName();
            return new ResearchFile(
                    zipFileName.substring(0, zipFileName.indexOf(ZIP_EXTENSION)),
                    password, zipFileChecksumMD5);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new ResearchException(ex);
        }
    }

    @Transactional
    public ResearchData saveResearch(String riskExpertAddress,
                                     String uuid,
                                     int price,
                                     String title,
                                     String description,
                                     String keywords,
                                     String checksum,
                                     String password,
                                     String clientAddress,
                                     long enquiryId,
                                     long bidId) {
        Research research =
                saveResearch(riskExpertAddress, uuid, price, title,
                        description, keywords, checksum, password);

        if (bidId > 0) {
            EnquiryBid enquiryBid = enquiryBidDao.findOne(bidId);
            if (enquiryBid == null) {
                throw new RuntimeException(MessageFormat.format(
                        "Bid not found {0}", bidId));
            }
            updateEnquiryBid(enquiryId, enquiryBid, research);
            Client client = clientDao.findByAddress(clientAddress);
            if (client == null) {
                throw new RuntimeException(MessageFormat.format(
                        "Client not found {0}", clientAddress));
            }
            purchaseService.createPurchase(client, research, enquiryBid.getPrice());
        }

        return new ResearchData(research);
    }

    public List<ResearchData> getExpertResearchItems(String riskExpertAddress) {
        Expert expert = expertDao.findByAddress(riskExpertAddress);
        if (expert == null) {
            throw new RuntimeException(MessageFormat.format(
                    "Expert not found {0}", riskExpertAddress));
        }
        return getListFromIterable(researchDao.findByExpert(expert)).stream().
                sorted(comparing(Research::getTimestamp).reversed()).
                map(research -> new ResearchData(research)).
                collect(toList());
    }

    public ResearchData getResearch(String uuid) {
        return new ResearchData(researchDao.findByUuid(uuid));
    }

    public List<ResearchData> findResearch(String clientAddress, String keywords) {
        List<String> keywordList = getKeywordList(keywords);
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(MessageFormat.format(
                    "Client not found {0}", clientAddress));
        }

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

    public File getResearchFile(String fileName) {
        try {
            String directory = env.getProperty("wrisx.paths.uploadedFiles");
            String filepath = Paths.get(directory, fileName + ZIP_EXTENSION).toString();
            return new File(filepath);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new ResearchException(ex);
        }
    }

    private Research saveResearch(String expertAddress, String uuid, int price,
                                  String title, String description, String keywords,
                                  String checksum, String password) {
        Expert expert = expertDao.findByAddress(expertAddress);
        if (expert == null) {
            throw new RuntimeException(MessageFormat.format(
                    "Expert not found {0}", expertAddress));
        }
        Research research = new Research(uuid, price, title, description,
                keywords, checksum, password, expert);
        research = researchDao.save(research);
        return research;
    }

    private void updateEnquiryBid(long enquiryId, EnquiryBid enquiryBid,
                                  Research research) {
        ResearchEnquiry researchEnquiry = researchEnquiryDao.findOne(enquiryId);
        if (researchEnquiry == null) {
            throw new RuntimeException(MessageFormat.format(
                    "Research enquiry not found {0}", enquiryId));
        }
        enquiryBid.setResearch(research);
        enquiryBidDao.save(enquiryBid);
    }
}
