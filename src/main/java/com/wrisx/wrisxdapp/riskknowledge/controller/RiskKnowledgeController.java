package com.wrisx.wrisxdapp.riskknowledge.controller;

import com.wrisx.wrisxdapp.data.RiskKnowledgeData;
import com.wrisx.wrisxdapp.errorhandling.ErrorData;
import com.wrisx.wrisxdapp.riskknowledge.data.RiskKnowledgeFile;
import com.wrisx.wrisxdapp.riskknowledge.service.RiskKnowledgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RiskKnowledgeController {
    private final Logger logger = LoggerFactory.getLogger(RiskKnowledgeController.class);

    private final RiskKnowledgeService riskKnowledgeService;

    @Autowired
    public RiskKnowledgeController(RiskKnowledgeService riskKnowledgeService) {
        this.riskKnowledgeService = riskKnowledgeService;
    }

    @RequestMapping("/a")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/uploadFile", method = POST)
    public ResponseEntity<RiskKnowledgeFile> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) {
        logger.debug(MessageFormat.format("Uploading file {0}", uploadfile.getName()));

        RiskKnowledgeFile riskKnowledgeFile =
                riskKnowledgeService.saveUploadedFile(uploadfile);

        return new ResponseEntity<>(riskKnowledgeFile, OK);
    }

    @RequestMapping(value = "/riskKnowledge", method = POST)
    public ResponseEntity<?> setFileAttributes(
            @RequestParam("address") String riskExpertAddress,
            @RequestParam("uuid") String uuid,
            @RequestParam("price") int price,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("keywords") String keywords,
            @RequestParam("checksum") String checksum,
            @RequestParam("password") String password,
            @RequestParam("clientAddress") String clientAddress,
            @RequestParam("enquiryId") long enquiryId,
            @RequestParam("bidId") long bidId) {
        logger.debug(MessageFormat.format("Setting file attributes {0}", title));

        RiskKnowledgeData riskKnowledge =
                riskKnowledgeService.saveRiskKnowledge(riskExpertAddress, uuid, price, title,
                        description, keywords, checksum, password,
                        clientAddress, enquiryId, bidId);

        return new ResponseEntity<>(riskKnowledge, OK);
    }

    @RequestMapping(value = "/riskKnowledge/expert/{address}", method = GET)
    public ResponseEntity<List<RiskKnowledgeData>> getExpertRiskKnowledgeItems(
            @PathVariable String address) {
        logger.debug(MessageFormat.format("Getting expert risk knowledge items {0}", address));

        List<RiskKnowledgeData> riskKnowledgeItems =
                riskKnowledgeService.getExpertRiskKnowledgeItems(address);

        return new ResponseEntity<>(riskKnowledgeItems, OK);
    }

    @RequestMapping(value = "/riskKnowledge/{uuid}", method = GET)
    public ResponseEntity<RiskKnowledgeData> getRiskKnowledge(@PathVariable String uuid) {
        logger.debug(MessageFormat.format("Getting risk knowledge {0}", uuid));

        RiskKnowledgeData riskKnowledge = riskKnowledgeService.getRiskKnowledge(uuid);

        return new ResponseEntity<>(riskKnowledge, OK);
    }

    @RequestMapping(value = "/riskKnowledge/client/{address}/keywords/{keywords}", method = GET)
    public ResponseEntity<List<RiskKnowledgeData>> findClientRiskKnowledgeItems(
            @PathVariable String keywords,
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format(
                "Searching risk knowledge items for client {0} {1}",
                clientAddress, keywords));

        List<RiskKnowledgeData> riskKnowledgeItems =
                riskKnowledgeService.findRiskKnowledge(clientAddress, keywords);

        return new ResponseEntity<>(riskKnowledgeItems, OK);
    }

    @RequestMapping(value = "/riskKnowledge/client/{address}/keywords", method = GET)
    public ResponseEntity<List<RiskKnowledgeData>> findAllClientRiskKnowledgeItems(
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format(
                "Searching risk knowledge items for client {0}",
                clientAddress));

        List<RiskKnowledgeData> riskKnowledgeItems =
                riskKnowledgeService.findRiskKnowledge(clientAddress,"");

        return new ResponseEntity<>(riskKnowledgeItems, OK);
    }

    @RequestMapping(value = "/downloadFile/{uuid}", produces = "application/zip", method = GET)
    public ResponseEntity<Resource> downloadRiskKnowledge(
            @PathVariable("uuid") String uuid) throws FileNotFoundException {
        File file = riskKnowledgeService.getRiskKnowledgeFile(uuid);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header("Content-Disposition", "attachment; filename=\"" + uuid + ".zip\"")
                .body(resource);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ErrorData handleException(Exception ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ErrorData(ex.getMessage(), 1001);
    }
}
