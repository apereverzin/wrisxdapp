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
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.MessageFormat;
import java.util.List;

import static com.wrisx.wrisxdapp.user.controller.UserController.USER_ADDRESS;
import static com.wrisx.wrisxdapp.user.controller.UserController.USER_AUTHORISED;
import static com.wrisx.wrisxdapp.util.WrisxUtil.verifyUserAuthorisation;
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
            @SessionAttribute(USER_ADDRESS) String address,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @RequestParam("keywords") String keywords,
            @RequestParam("description") String description) {
        logger.debug(MessageFormat.format("Creating research enquiry {0}", keywords));

        verifyUserAuthorisation(address, userAuthorised);

        ResearchEnquiryData researchEnquiry =
                researchEnquiryService.saveEnquiry(address, keywords, description);

        return new ResponseEntity<>(researchEnquiry, OK);
    }

    @RequestMapping(value = "/enquiry/client", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> getClientEnquiries(
            @SessionAttribute(USER_ADDRESS) String clientAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Getting research enquiries of client {0}", clientAddress));

        verifyUserAuthorisation(clientAddress, userAuthorised);

        List<ResearchEnquiryData> researchEnquiries =
                researchEnquiryService.getClientEnquiries(clientAddress).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return new ResponseEntity<>(researchEnquiries, OK);
    }

    @RequestMapping(value = "/enquiry/{id}", method = GET)
    public ResponseEntity<ResearchEnquiryData> getEnquiry(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @PathVariable long id) {
        logger.debug(MessageFormat.format("Getting research enquiry {0}", id));

        verifyUserAuthorisation(expertAddress, userAuthorised);

        ResearchEnquiryData researchEnquiry = researchEnquiryService.getEnquiry(id);

        return new ResponseEntity<>(researchEnquiry, OK);
    }

    @RequestMapping(value = "/enquiry/expert/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> findExpertEnquiries(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @PathVariable String keywords,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Searching research enquiries for expert {0} {1}",
                expertAddress, keywords));

        verifyUserAuthorisation(expertAddress, userAuthorised);

        return getExpertEnquiries(expertAddress, keywords, pageable);
    }

    @RequestMapping(value = "/enquiry/expert/keywords", method = GET)
    public ResponseEntity<List<ResearchEnquiryData>> findAllExpertEnquiries(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Searching research enquiries for expert {0}",
                expertAddress));

        verifyUserAuthorisation(expertAddress, userAuthorised);

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
