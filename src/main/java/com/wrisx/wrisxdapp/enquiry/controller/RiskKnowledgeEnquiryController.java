package com.wrisx.wrisxdapp.enquiry.controller;

import com.wrisx.wrisxdapp.data.RiskKnowledgeEnquiryData;
import com.wrisx.wrisxdapp.enquiry.service.RiskKnowledgeEnquiryService;
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

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RiskKnowledgeEnquiryController {
    private final Logger logger = LoggerFactory.getLogger(RiskKnowledgeEnquiryController.class);

    private final RiskKnowledgeEnquiryService riskKnowledgeEnquiryService;

    @Autowired
    public RiskKnowledgeEnquiryController(RiskKnowledgeEnquiryService riskKnowledgeEnquiryService) {
        this.riskKnowledgeEnquiryService = riskKnowledgeEnquiryService;
    }

    @RequestMapping(value = "/enquiry", method = POST)
    public ResponseEntity<RiskKnowledgeEnquiryData> createEnquiry(
            @RequestParam("address") String address,
            @RequestParam("keywords") String keywords,
            @RequestParam("description") String description) {
        logger.debug(MessageFormat.format("Creating risk knowledge enquiry {0}", keywords));

        RiskKnowledgeEnquiryData riskKnowledgeEnquiry =
                riskKnowledgeEnquiryService.saveEnquiry(address, keywords, description);

        return new ResponseEntity<>(riskKnowledgeEnquiry, OK);
    }

    @RequestMapping(value = "/enquiry/client/{address}", method = GET)
    public ResponseEntity<List<RiskKnowledgeEnquiryData>> getClientEnquiries(
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format("Getting risk knowledge enquiries of client {0}", clientAddress));

        List<RiskKnowledgeEnquiryData> riskKnowledgeEnquiries =
                riskKnowledgeEnquiryService.getClientEnquiries(clientAddress);

        return new ResponseEntity<>(riskKnowledgeEnquiries, OK);
    }

    @RequestMapping(value = "/enquiry/{id}", method = GET)
    public ResponseEntity<RiskKnowledgeEnquiryData> getEnquiry(@PathVariable long id) {
        logger.debug(MessageFormat.format("Getting risk knowledge enquiry {0}", id));

        RiskKnowledgeEnquiryData riskKnowledgeEnquiry =
                riskKnowledgeEnquiryService.getEnquiry(id);

        return new ResponseEntity<>(riskKnowledgeEnquiry, OK);
    }

    @RequestMapping(value = "/enquiry/expert/{address}/keywords/{keywords}", method = GET)
    public ResponseEntity<List<RiskKnowledgeEnquiryData>> findExpertEnquiries(
            @PathVariable String keywords,
            @PathVariable(("address")) String expertAddress) {
        logger.debug(MessageFormat.format(
                "Searching risk knowledge enquiries for expert {0} {1}",
                expertAddress, keywords));

        List<RiskKnowledgeEnquiryData> riskKnowledgeEnquiries =
                riskKnowledgeEnquiryService.findExpertEnquiries(expertAddress, keywords);

        return new ResponseEntity<>(riskKnowledgeEnquiries, OK);
    }

    @RequestMapping(value = "/enquiry/expert/{address}/keywords", method = GET)
    public ResponseEntity<List<RiskKnowledgeEnquiryData>> findAllExpertEnquiries(
            @PathVariable("address") String expertAddress) {
        logger.debug(MessageFormat.format(
                "Searching risk knowledge enquiries for expert {0}",
                expertAddress));

        List<RiskKnowledgeEnquiryData> riskKnowledgeEnquiries =
                riskKnowledgeEnquiryService.findExpertEnquiries(expertAddress, "");

        return new ResponseEntity<>(riskKnowledgeEnquiries, OK);
    }
}
