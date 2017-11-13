package com.wrisx.wrisxdapp.enquirybid.controller;

import com.wrisx.wrisxdapp.data.EnquiryBidData;
import com.wrisx.wrisxdapp.enquirybid.service.EnquiryBidService;
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
public class EnquiryBidController {
    private final Logger logger = LoggerFactory.getLogger(EnquiryBidController.class);

    private final EnquiryBidService enquiryBidService;

    @Autowired
    public EnquiryBidController(EnquiryBidService enquiryBidService) {
        this.enquiryBidService = enquiryBidService;
    }

    @RequestMapping(value = "/enquiry/{enquiryId}/bid", method = POST)
    public ResponseEntity<EnquiryBidData> placeEnquiryBid(
            @PathVariable long enquiryId,
            @RequestParam("address") String expertAddress,
            @RequestParam("bid") int bid,
            @RequestParam("comment") String comment) {
        logger.debug(MessageFormat.format("Placing bid {0} for risk knowledge enquiry {1}", bid, enquiryId));

        EnquiryBidData enquiryBid = enquiryBidService.placeEnquiryBid(enquiryId, expertAddress, bid, comment);

        return new ResponseEntity<>(enquiryBid, OK);
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}", method = POST)
    public ResponseEntity<EnquiryBidData> selectEnquiryBid(
            @PathVariable long enquiryBidId) {
        logger.debug(MessageFormat.format("Selecting bid {0} for risk knowledge enquiry", enquiryBidId));

        EnquiryBidData enquiryBid = enquiryBidService.selectEnquiryBid(enquiryBidId);

        return new ResponseEntity<>(enquiryBid, OK);
    }

    @RequestMapping(value = "/enquiry/bid/expert/{expertAddress}", method = GET)
    public ResponseEntity<List<EnquiryBidData>> getExpertEnquiryBids(@PathVariable String expertAddress) {
        logger.debug(MessageFormat.format("Getting expert enquiry bids {0}", expertAddress));

        List<EnquiryBidData> riskKnowledgeItems = enquiryBidService.getEnquiryBidsByExpert(expertAddress);

        return new ResponseEntity<>(riskKnowledgeItems, OK);
    }

    @RequestMapping(value = "/enquiry/{enquiryId}/bid", method = GET)
    public ResponseEntity<List<EnquiryBidData>> getEnquiryBids(@PathVariable long enquiryId) {
        logger.debug(MessageFormat.format("Getting bids for risk knowledge enquiry {0}", enquiryId));

        List<EnquiryBidData> enquiryBids = enquiryBidService.getEnquiryBidsByEnquiry(enquiryId);

        return new ResponseEntity<>(enquiryBids, OK);
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}", method = GET)
    public ResponseEntity<EnquiryBidData> selectEnquiryBids(@PathVariable long enquiryBidId) {
        logger.debug(MessageFormat.format("Selecting bid {0} for risk knowledge enquiry", enquiryBidId));

        EnquiryBidData enquiryBid = enquiryBidService.selectEnquiryBid(enquiryBidId);

        return new ResponseEntity<>(enquiryBid, OK);
    }
}
