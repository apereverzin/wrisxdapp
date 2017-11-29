package com.wrisx.wrisxdapp.research.controller;

import com.wrisx.wrisxdapp.data.ResearchData;
import com.wrisx.wrisxdapp.errorhandling.ErrorData;
import com.wrisx.wrisxdapp.exception.NotFoundException;
import com.wrisx.wrisxdapp.research.data.ResearchFile;
import com.wrisx.wrisxdapp.research.service.ResearchService;
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

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class ResearchController {
    private final Logger logger = LoggerFactory.getLogger(ResearchController.class);

    private final ResearchService researchService;

    @Autowired
    public ResearchController(ResearchService researchService) {
        this.researchService = researchService;
    }

    @RequestMapping("/a")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/uploadFile", method = POST)
    public ResponseEntity<ResearchFile> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) {
        logger.debug(MessageFormat.format("Uploading file {0}", uploadfile.getName()));

        ResearchFile researchFile =
                researchService.saveUploadedFile(uploadfile);

        return new ResponseEntity<>(researchFile, OK);
    }

    @RequestMapping(value = "/research", method = POST)
    public ResponseEntity<?> setFileAttributes(
            @RequestParam("address") String expertAddress,
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

        try {
            ResearchData research =
                    researchService.saveResearch(expertAddress, uuid, price, title,
                            description, keywords, checksum, password,
                            clientAddress, enquiryId, bidId);
            return new ResponseEntity<>(research, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/research/{uuid}", method = DELETE)
    public ResponseEntity<Void> deleteResearch(
            @PathVariable("uuid") String uuid) {
        logger.debug(MessageFormat.format("Deleting research {0}", uuid));

        try {
            researchService.deleteResearch(uuid);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/research/{uuid}/confirm", method = PUT)
    public ResponseEntity<Void> confirmResearchCreation(
            @PathVariable("uuid") String uuid) {
        logger.debug(MessageFormat.format("Confirming research creation {0}", uuid));

        try {
            researchService.confirmResearchCreation(uuid);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/research/expert/{address}", method = GET)
    public ResponseEntity<List<ResearchData>> getExpertResearchItems(
            @PathVariable String address) {
        logger.debug(MessageFormat.format(
                "Getting expert research items {0}", address));

        try {
            List<ResearchData> researchItems =
                    researchService.getExpertResearchItems(address);
            return new ResponseEntity<>(researchItems, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/research/{uuid}", method = GET)
    public ResponseEntity<ResearchData> getResearch(@PathVariable String uuid) {
        logger.debug(MessageFormat.format("Getting research {0}", uuid));

        try {
            ResearchData research = researchService.getResearch(uuid);
            return new ResponseEntity<>(research, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/research/client/{address}/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ResearchData>> findClientResearchItems(
            @PathVariable String keywords,
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format(
                "Searching research items for client {0} {1}",
                clientAddress, keywords));

        return getResearchItems(clientAddress, keywords);
    }

    @RequestMapping(value = "/research/client/{address}/keywords", method = GET)
    public ResponseEntity<List<ResearchData>> findAllClientResearchItems(
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format(
                "Searching research items for client {0}",
                clientAddress));

        return getResearchItems(clientAddress, "");
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
            @PathVariable String keywords) {
        logger.debug(MessageFormat.format("Searching research {0}", keywords));

        List<ResearchData> researchItems =
                researchService.findResearchItemsByKeywords(keywords);

        return new ResponseEntity<>(researchItems, OK);
    }

    @RequestMapping(value = "/research/keywords", method = GET)
    public ResponseEntity<List<ResearchData>> findAllResearchItems() {
        logger.debug("Searching research");

        List<ResearchData> researchItems = researchService.findResearchItemsByKeywords("");

        return new ResponseEntity<>(researchItems, OK);
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ErrorData handleException(Exception ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ErrorData(ex.getMessage(), 1001);
    }

    private ResponseEntity<List<ResearchData>> getResearchItems(
            String clientAddress, String keywords) {
        try {
            List<ResearchData> researchItems =
                    researchService.findResearchItems(clientAddress, keywords);
            return new ResponseEntity<>(researchItems, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}
