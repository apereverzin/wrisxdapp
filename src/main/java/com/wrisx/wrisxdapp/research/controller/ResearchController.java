package com.wrisx.wrisxdapp.research.controller;

import com.wrisx.wrisxdapp.data.request.ResearchRequest;
import com.wrisx.wrisxdapp.data.request.TransactionHashRequest;
import com.wrisx.wrisxdapp.data.response.ResearchData;
import com.wrisx.wrisxdapp.research.data.ResearchFile;
import com.wrisx.wrisxdapp.research.service.ResearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.MessageFormat;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@CrossOrigin
@RestController
public class ResearchController {
    private final Logger logger = LoggerFactory.getLogger(ResearchController.class);

    private final ResearchService researchService;

    @Autowired
    public ResearchController(ResearchService researchService) {
        this.researchService = researchService;
    }

    @RequestMapping(value = "/uploadFile", method = POST)
    public ResponseEntity<ResearchFile> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) {
        logger.debug(MessageFormat.format("Uploading file {0}", uploadfile.getName()));

        ResearchFile researchFile =
                researchService.saveUploadedFile(uploadfile);

        return ResponseEntity.ok(researchFile);
    }

    @RequestMapping(value = "/research", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setFileAttributes(
            @RequestBody ResearchRequest researchRequest) {
        logger.debug(MessageFormat.format("Setting file attributes {0}", researchRequest.getTitle()));

        ResearchData research = researchService.saveResearch(researchRequest);

        return ResponseEntity.ok(research);
    }

    @RequestMapping(value = "/research/{uuid}", method = DELETE)
    public ResponseEntity<Void> deleteResearch(@PathVariable("uuid") String uuid) {
        logger.debug(MessageFormat.format("Deleting research {0}", uuid));

        researchService.deleteResearch(uuid);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/research/{uuid}/confirm", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmResearchCreation(
            @PathVariable("uuid") String uuid,
            @RequestBody TransactionHashRequest transactionHashRequest) {
        logger.debug(MessageFormat.format("Confirming research creation {0}", uuid));

        researchService.confirmResearchCreation(uuid,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/research/{uuid}/commit", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> commitResearchCreation(
            @PathVariable("uuid") String uuid,
            @RequestBody TransactionHashRequest transactionHashRequest) {
        logger.debug(MessageFormat.format(
                "Committing research creation {0} {1}",
                uuid, transactionHashRequest.getTransactionHash()));

        researchService.commitResearchCreation(uuid,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/research/expert/{address}", method = GET)
    public ResponseEntity<List<ResearchData>> getExpertResearchItems(
            @PathVariable("address") String expertAddress,
            Pageable pageable, HttpServletRequest request) {
        logger.debug(MessageFormat.format(
                "Getting expert research items {0}", expertAddress));

        List<ResearchData> researchItems =
                researchService.getExpertResearchItems(expertAddress).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(researchItems);
    }

    @RequestMapping(value = "/research/client/{address}/password/{uuid}", method = GET)
    public ResponseEntity<String> getResearchPassword(
            @PathVariable("address") String clientAddress,
            @PathVariable String uuid) {
        logger.debug(MessageFormat.format("Getting research {0}", uuid));

        String researchPassword = researchService.getResearchPassword(clientAddress, uuid);

        return ResponseEntity.ok(researchPassword);
    }

    @RequestMapping(value = "/research/{uuid}", method = GET)
    public ResponseEntity<ResearchData> getResearch(@PathVariable String uuid) {
        logger.debug(MessageFormat.format("Getting research {0}", uuid));

        ResearchData research = researchService.getResearch(uuid);

        return ResponseEntity.ok(research);
    }

    @RequestMapping(value = "/research/client/{address}/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ResearchData>> findClientResearchItems(
            @PathVariable String keywords,
            @PathVariable("address") String clientAddress,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Searching research items for client {0} {1}",
                clientAddress, keywords));

        return getResearchItemsByKeywords(clientAddress, keywords, pageable);
    }

    @RequestMapping(value = "/research/client/{address}/keywords", method = GET)
    public ResponseEntity<List<ResearchData>> findAllClientResearchItems(
            @PathVariable("address") String clientAddress,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Searching research items for client {0}",
                clientAddress));

        return getResearchItemsByKeywords(clientAddress, "", pageable);
    }

    @RequestMapping(value = "/downloadFile/{uuid}", produces = "application/zip", method = GET)
    public ResponseEntity<Resource> downloadResearch(
            @PathVariable("uuid") String uuid) throws FileNotFoundException {
        File file = researchService.getResearchFile(uuid);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header("Content-Disposition", "attachment; filename=\"" + uuid + ".zip\"")
                .body(resource);
    }

    @RequestMapping(value = "/research/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ResearchData>> findResearchItems(
            @PathVariable String keywords, Pageable pageable) {
        logger.debug(MessageFormat.format("Searching research {0}", keywords));

        List<ResearchData> researchItems =
                researchService.findResearchItemsByKeywords(keywords).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(researchItems);
    }

    @RequestMapping(value = "/research/keywords", method = GET)
    public ResponseEntity<List<ResearchData>> findAllResearchItems(Pageable pageable) {
        logger.debug("Searching research");

        List<ResearchData> researchItems =
                researchService.findResearchItemsByKeywords("").stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(researchItems);
    }

    private ResponseEntity<List<ResearchData>> getResearchItemsByKeywords(
            String clientAddress, String keywords, Pageable pageable) {
        List<ResearchData> researchItems =
                researchService.findResearchItems(clientAddress, keywords).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(researchItems);
    }
}
