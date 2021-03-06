package com.wrisx.wrisxdapp.enquiry.controller;

import com.wrisx.wrisxdapp.data.request.ResearchEnquiryRequest;
import com.wrisx.wrisxdapp.data.response.ResearchEnquiryData;
import com.wrisx.wrisxdapp.enquiry.service.ResearchEnquiryService;
import com.wrisx.wrisxdapp.security.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin
@RestController
public class ResearchEnquiryController {
    private final Logger logger = LoggerFactory.getLogger(ResearchEnquiryController.class);

    private final ResearchEnquiryService researchEnquiryService;
    private final AuthenticationService authenticationService;

    @Autowired
    public ResearchEnquiryController(ResearchEnquiryService researchEnquiryService,
                                     AuthenticationService authenticationService) {
        this.researchEnquiryService = researchEnquiryService;
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/enquiry", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ResearchEnquiryData> createEnquiry(
            @RequestBody ResearchEnquiryRequest researchEnquiryRequest,
            HttpServletRequest request) {
        String emailAddress = authenticationService.authenticateRequest(request);

        logger.debug(MessageFormat.format("Creating research enquiry {0}",
                researchEnquiryRequest.getKeywords()));

        ResearchEnquiryData researchEnquiry =
                researchEnquiryService.saveEnquiry(researchEnquiryRequest);

        return ResponseEntity.ok(researchEnquiry);
    }

    @RequestMapping(value = "/enquiry/client/{address}", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> getClientEnquiries(
            @PathVariable("address") String clientAddress,
            Pageable pageable,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, clientAddress);

        logger.debug(MessageFormat.format(
                "Getting research enquiries of client {0}", clientAddress));

        List<ResearchEnquiryData> researchEnquiries =
                researchEnquiryService.getClientEnquiries(clientAddress).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(researchEnquiries);
    }

    @RequestMapping(value = "/enquiry/expert/{address}/{id}", method = GET)
    public ResponseEntity<ResearchEnquiryData> getEnquiry(
            @PathVariable("address") String expertAddress,
            @PathVariable long id,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, expertAddress);

        logger.debug(MessageFormat.format("Getting research enquiry {0}", id));

        ResearchEnquiryData researchEnquiry =
                researchEnquiryService.getExpertEnquiry(expertAddress, id);

        return ResponseEntity.ok(researchEnquiry);
    }

    @RequestMapping(value = "/enquiry/expert/{address}/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> findExpertEnquiries(
            @PathVariable("address") String expertAddress,
            @PathVariable String keywords,
            Pageable pageable,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, expertAddress);

        logger.debug(MessageFormat.format(
                "Searching research enquiries for expert {0} {1}",
                expertAddress, keywords));

        return getExpertEnquiries(expertAddress, keywords, pageable);
    }

    @RequestMapping(value = "/enquiry/expert/{address}/keywords", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> findAllExpertEnquiries(
            @PathVariable("address") String expertAddress,
            Pageable pageable,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, expertAddress);

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

        return ResponseEntity.ok(researchEnquiries);
    }
}
