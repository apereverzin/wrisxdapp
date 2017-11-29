package com.wrisx.wrisxdapp.enquiry.controller;

import com.wrisx.wrisxdapp.data.ResearchEnquiryData;
import com.wrisx.wrisxdapp.enquiry.service.ResearchEnquiryService;
import com.wrisx.wrisxdapp.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.MessageFormat;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class ResearchEnquiryController {
    private final Logger logger = LoggerFactory.getLogger(ResearchEnquiryController.class);

    private final ResearchEnquiryService researchEnquiryService;

    @Autowired
    public ResearchEnquiryController(ResearchEnquiryService researchEnquiryService) {
        this.researchEnquiryService = researchEnquiryService;
    }

    @RequestMapping(value = "/enquiry", method = POST)
    public ResponseEntity<ResearchEnquiryData> createEnquiry(
            @RequestParam("address") String address,
            @RequestParam("keywords") String keywords,
            @RequestParam("description") String description) {
        logger.debug(MessageFormat.format("Creating research enquiry {0}", keywords));

        try {
            ResearchEnquiryData researchEnquiry =
                    researchEnquiryService.saveEnquiry(address, keywords, description);
            return new ResponseEntity<>(researchEnquiry, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/enquiry/client/{address}", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> getClientEnquiries(
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format(
                "Getting research enquiries of client {0}", clientAddress));

        try {
            List<ResearchEnquiryData> researchEnquiries =
                    researchEnquiryService.getClientEnquiries(clientAddress);
            return new ResponseEntity<>(researchEnquiries, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/enquiry/{id}", method = GET)
    public ResponseEntity<ResearchEnquiryData> getEnquiry(@PathVariable long id) {
        logger.debug(MessageFormat.format("Getting research enquiry {0}", id));

        try {
            ResearchEnquiryData researchEnquiry = researchEnquiryService.getEnquiry(id);
            return new ResponseEntity<>(researchEnquiry, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/enquiry/expert/{address}/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> findExpertEnquiries(
            @PathVariable String keywords,
            @PathVariable(("address")) String expertAddress) {
        logger.debug(MessageFormat.format(
                "Searching research enquiries for expert {0} {1}",
                expertAddress, keywords));

        return getExpertEnquiries(expertAddress, keywords);
    }

    @RequestMapping(value = "/enquiry/expert/{address}/keywords", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> findAllExpertEnquiries(
            @PathVariable("address") String expertAddress) {
        logger.debug(MessageFormat.format(
                "Searching research enquiries for expert {0}",
                expertAddress));

        return getExpertEnquiries(expertAddress, "");
    }

    private ResponseEntity<List<ResearchEnquiryData>> getExpertEnquiries(
            String expertAddress, String keywords) {
        try {
            List<ResearchEnquiryData> researchEnquiries =
                    researchEnquiryService.findExpertEnquiries(expertAddress, keywords);
            return new ResponseEntity<>(researchEnquiries, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}
