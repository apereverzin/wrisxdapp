package com.wrisx.wrisxdapp.riskknowledge.service;

import com.wrisx.wrisxdapp.data.RiskKnowledgeData;
import com.wrisx.wrisxdapp.domain.*;
import com.wrisx.wrisxdapp.purchase.service.PurchaseService;
import com.wrisx.wrisxdapp.riskknowledge.data.RiskKnowledgeFile;
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
public class RiskKnowledgeService {
    private final Logger logger = LoggerFactory.getLogger(RiskKnowledgeService.class);

    public static final String ZIP_EXTENSION = ".zip";
    private static final int PASSWORD_LENGTH = 10;
    private static final String MD5_ALGORITHM = "MD5";

    @Autowired
    private Environment env;

    private final FileUploadProvider fileUploadProvider;
    private final PasswordProvider passwordProvider;
    private final ZipFileProvider zipFileProvider;
    private final ChecksumProvider checksumProvider;
    private final RiskKnowledgeDao riskKnowledgeDao;
    private final RiskExpertDao riskExpertDao;
    private final ClientDao clientDao;
    private final RiskKnowledgeEnquiryDao riskKnowledgeEnquiryDao;
    private final EnquiryBidDao enquiryBidDao;
    private final PurchaseService purchaseService;
    private final PurchaseDao purchaseDao;

    @Autowired
    public RiskKnowledgeService(FileUploadProvider fileUploadProvider,
                                PasswordProvider passwordProvider,
                                ZipFileProvider zipFileProvider,
                                ChecksumProvider checksumProvider,
                                RiskKnowledgeDao riskKnowledgeDao,
                                RiskExpertDao riskExpertDao,
                                ClientDao clientDao,
                                RiskKnowledgeEnquiryDao riskKnowledgeEnquiryDao,
                                EnquiryBidDao enquiryBidDao,
                                PurchaseService purchaseService,
                                PurchaseDao purchaseDao) {
        this.fileUploadProvider = fileUploadProvider;
        this.passwordProvider = passwordProvider;
        this.zipFileProvider = zipFileProvider;
        this.checksumProvider = checksumProvider;
        this.riskKnowledgeDao = riskKnowledgeDao;
        this.riskExpertDao = riskExpertDao;
        this.clientDao = clientDao;
        this.riskKnowledgeEnquiryDao = riskKnowledgeEnquiryDao;
        this.enquiryBidDao = enquiryBidDao;
        this.purchaseService = purchaseService;
        this.purchaseDao = purchaseDao;
    }

    public RiskKnowledgeFile saveUploadedFile(MultipartFile file) {
        try {
            String directory = env.getProperty("wrisx.paths.uploadedFiles");
            File riskKnowledgeFile = fileUploadProvider.uploadFile(file, directory);

            String password = passwordProvider.getRandomPassword(PASSWORD_LENGTH);
            ZipFile zipFile = zipFileProvider.zipAndProtectFile(riskKnowledgeFile, directory, password);

            String zipFileChecksumMD5 = checksumProvider.getFileChecksum(zipFile.getFile(), MD5_ALGORITHM);

            riskKnowledgeFile.delete();

            String zipFileName = zipFile.getFile().getName();
            return new RiskKnowledgeFile(
                    zipFileName.substring(0, zipFileName.indexOf(ZIP_EXTENSION)),
                    password, zipFileChecksumMD5);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new RiskKnowledgeException(ex);
        }
    }

    @Transactional
    public RiskKnowledgeData saveRiskKnowledge(String riskExpertAddress,
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
        RiskKnowledge riskKnowledge =
                saveRiskKnowledge(riskExpertAddress, uuid, price, title,
                        description, keywords, checksum, password);

        if (bidId > 0) {
            EnquiryBid enquiryBid = enquiryBidDao.findOne(bidId);
            if (enquiryBid == null) {
                throw new RuntimeException(MessageFormat.format("Bid not found {0}", bidId));
            }
            updateEnquiryBid(enquiryId, enquiryBid, riskKnowledge);
            Client client = clientDao.findByAddress(clientAddress);
            if (client == null) {
                throw new RuntimeException(MessageFormat.format("Client not found {0}", clientAddress));
            }
            purchaseService.createPurchase(client, riskKnowledge, enquiryBid.getPrice());
        }

        return new RiskKnowledgeData(riskKnowledge);
    }

    public List<RiskKnowledgeData> getExpertRiskKnowledgeItems(String riskExpertAddress) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(riskExpertAddress);
        if (riskExpert == null) {
            throw new RuntimeException(MessageFormat.format("Risk expert not found {0}", riskExpertAddress));
        }
        return getListFromIterable(riskKnowledgeDao.findByRiskExpert(riskExpert)).stream().
                sorted(comparing(RiskKnowledge::getTimestamp).reversed()).
                map(riskKnowledge -> new RiskKnowledgeData(riskKnowledge)).
                collect(toList());
    }

    public RiskKnowledgeData getRiskKnowledge(String uuid) {
        return new RiskKnowledgeData(riskKnowledgeDao.findByUuid(uuid));
    }

    public List<RiskKnowledgeData> findRiskKnowledge(String clientAddress, String keywords) {
        List<String> keywordList = getKeywordList(keywords);
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new RuntimeException(MessageFormat.format("Client not found {0}", clientAddress));
        }

        List<RiskKnowledge> riskKnowledgeItems =
                getListFromIterable(riskKnowledgeDao.findAll());
        return (keywordList.isEmpty() ? riskKnowledgeItems.stream() :
                riskKnowledgeItems.stream().
                        filter(item -> {
                            List<String> itemKeywordList = getKeywordList(item.getKeywords());
                            return itemKeywordList.containsAll(keywordList);
                        })).
                sorted(comparing(RiskKnowledge::getTimestamp)).
                map(item -> {
                    List<Purchase> purchases =
                            purchaseDao.findByClientAndRiskKnowledge(client, item);
                    if (purchases.isEmpty()) {
                        return new RiskKnowledgeData(item);
                    }
                    return new RiskKnowledgeData(item, purchases.get(0));
                }).
                collect(toList());
    }

    public File getRiskKnowledgeFile(String fileName) {
        try {
            String directory = env.getProperty("wrisx.paths.uploadedFiles");
            String filepath = Paths.get(directory, fileName + ZIP_EXTENSION).toString();
            return new File(filepath);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new RiskKnowledgeException(ex);
        }
    }

    private RiskKnowledge saveRiskKnowledge(String riskExpertAddress, String uuid, int price, String title, String description, String keywords, String checksum, String password) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(riskExpertAddress);
        if (riskExpert == null) {
            throw new RuntimeException(MessageFormat.format("Risk expert not found {0}", riskExpertAddress));
        }
        RiskKnowledge riskKnowledge = new RiskKnowledge(uuid, price, title, description,
                keywords, checksum, password, riskExpert);
        riskKnowledge = riskKnowledgeDao.save(riskKnowledge);
        return riskKnowledge;
    }

    private void updateEnquiryBid(long enquiryId, EnquiryBid enquiryBid,
                                  RiskKnowledge riskKnowledge) {
        RiskKnowledgeEnquiry riskKnowledgeEnquiry = riskKnowledgeEnquiryDao.findOne(enquiryId);
        if (riskKnowledgeEnquiry == null) {
            throw new RuntimeException(MessageFormat.format("Risk knowledge enquiry not found {0}", enquiryId));
        }
        enquiryBid.setRiskKnowledge(riskKnowledge);
        enquiryBidDao.save(enquiryBid);
    }
}
