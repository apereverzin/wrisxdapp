package com.wrisx.wrisxdapp.enquiry.controller;

import com.wrisx.wrisxdapp.data.ResearchEnquiryData;
import com.wrisx.wrisxdapp.enquiry.service.ResearchEnquiryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.MessageFormat;
import java.util.List;

import static java.util.stream.Collectors.toList;
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

        ResearchEnquiryData researchEnquiry =
                researchEnquiryService.saveEnquiry(address, keywords, description);

        return new ResponseEntity<>(researchEnquiry, OK);
    }

    @RequestMapping(value = "/enquiry/client", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> getClientEnquiries(
            @PathVariable("address") String clientAddress,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Getting research enquiries of client {0}", clientAddress));

        List<ResearchEnquiryData> researchEnquiries =
                researchEnquiryService.getClientEnquiries(clientAddress).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return new ResponseEntity<>(researchEnquiries, OK);
    }

    @RequestMapping(value = "/enquiry/{id}", method = GET)
    public ResponseEntity<ResearchEnquiryData> getEnquiry(
            @PathVariable long id) {
        logger.debug(MessageFormat.format("Getting research enquiry {0}", id));

        ResearchEnquiryData researchEnquiry = researchEnquiryService.getEnquiry(id);

        return new ResponseEntity<>(researchEnquiry, OK);
    }

    @RequestMapping(value = "/enquiry/expert/{address}/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> findExpertEnquiries(
            @PathVariable("address") String expertAddress,
            @PathVariable String keywords,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Searching research enquiries for expert {0} {1}",
                expertAddress, keywords));

        return getExpertEnquiries(expertAddress, keywords, pageable);
    }

    @RequestMapping(value = "/enquiry/expert/{address}/keywords", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> findAllExpertEnquiries(
            @PathVariable("address") String expertAddress,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Searching research enquiries for expert {0}",
                expertAddress));

        return getExpertEnquiries(expertAddress, "", pageable);
    }

    private ResponseEntity<List<ResearchEnquiryData>> getExpertEnquiries(
            String expertAddress, String keywords, Pageable pageable) {
        List<ResearchEnquiryData> researchEnquiries =
                researchEnquiryService.findExpertEnquiries(expertAddress, keywords).
                        stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return new ResponseEntity<>(researchEnquiries, OK);
    }
}
