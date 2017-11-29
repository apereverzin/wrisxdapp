package com.wrisx.wrisxdapp.enquirybid.controller;

import com.wrisx.wrisxdapp.data.EnquiryBidData;
import com.wrisx.wrisxdapp.enquirybid.service.EnquiryBidService;
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
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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
        logger.debug(MessageFormat.format(
                "Placing bid {0} for research enquiry {1}", bid, enquiryId));

        try {
            EnquiryBidData enquiryBid =
                    enquiryBidService.placeEnquiryBid(enquiryId, expertAddress, bid, comment);
            return new ResponseEntity<>(enquiryBid, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }

    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/select", method = PUT)
    public ResponseEntity<EnquiryBidData> selectEnquiryBid(
            @PathVariable long enquiryBidId) {
        logger.debug(MessageFormat.format(
                "Selecting bid {0} for research enquiry", enquiryBidId));

        try {
            EnquiryBidData enquiryBid =
                    enquiryBidService.setEnquiryBidSelection(enquiryBidId, true);
            return new ResponseEntity<>(enquiryBid, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/unselect", method = PUT)
    public ResponseEntity<EnquiryBidData> unselectEnquiryBid(
            @PathVariable long enquiryBidId) {
        logger.debug(MessageFormat.format(
                "Unselecting bid {0} for research enquiry", enquiryBidId));

        try {
            EnquiryBidData enquiryBid =
                    enquiryBidService.setEnquiryBidSelection(enquiryBidId, false);
            return new ResponseEntity<>(enquiryBid, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}", method = DELETE)
    public ResponseEntity<Void> deleteEnquiryBid(
            @PathVariable long enquiryBidId) {
        logger.debug(MessageFormat.format(
                "Deleting bid {0} for research enquiry", enquiryBidId));

        try {
            enquiryBidService.deleteEnquiryBid(enquiryBidId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/confirm", method = PUT)
    public ResponseEntity<Void> confirmEnquiryBidCreation(
            @PathVariable long enquiryBidId,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format(
                "Confirming bid creation {0} for research enquiry", enquiryBidId));

        try {
            enquiryBidService.confirmEnquiryBidCreation(enquiryBidId, transactionHash);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/enquiry/bid/expert/{expertAddress}", method = GET)
    public ResponseEntity<List<EnquiryBidData>> getExpertEnquiryBids(
            @PathVariable String expertAddress) {
        logger.debug(MessageFormat.format(
                "Getting expert enquiry bids {0}", expertAddress));

        try {
            List<EnquiryBidData> researchItems =
                    enquiryBidService.getEnquiryBidsByExpert(expertAddress);
            return new ResponseEntity<>(researchItems, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/enquiry/{enquiryId}/bid", method = GET)
    public ResponseEntity<List<EnquiryBidData>> getEnquiryBids(@PathVariable long enquiryId) {
        logger.debug(MessageFormat.format(
                "Getting bids for research enquiry {0}", enquiryId));

        try {
            List<EnquiryBidData> enquiryBids =
                    enquiryBidService.getEnquiryBidsByEnquiry(enquiryId);
            return new ResponseEntity<>(enquiryBids, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}
