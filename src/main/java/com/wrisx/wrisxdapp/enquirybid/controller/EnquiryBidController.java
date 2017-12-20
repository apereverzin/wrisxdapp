package com.wrisx.wrisxdapp.enquirybid.controller;

import com.wrisx.wrisxdapp.data.EnquiryBidData;
import com.wrisx.wrisxdapp.enquirybid.service.EnquiryBidService;
import com.wrisx.wrisxdapp.errorhandling.UnauthorizedException;
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
import static java.util.stream.Collectors.toList;
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
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @RequestParam("bid") int bid,
            @RequestParam("comment") String comment) {
        logger.debug(MessageFormat.format(
                "Placing bid {0} for research enquiry {1}", bid, enquiryId));

        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", expertAddress));
        }

        EnquiryBidData enquiryBid =
                enquiryBidService.placeEnquiryBid(enquiryId, expertAddress, bid, comment);
        return new ResponseEntity<>(enquiryBid, OK);

    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/select", method = PUT)
    public ResponseEntity<EnquiryBidData> selectEnquiryBid(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @PathVariable long enquiryBidId) {
        logger.debug(MessageFormat.format(
                "Selecting bid {0} for research enquiry", enquiryBidId));

        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", expertAddress));
        }

        EnquiryBidData enquiryBid =
                enquiryBidService.setEnquiryBidSelection(enquiryBidId, true);

        return new ResponseEntity<>(enquiryBid, OK);
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/unselect", method = PUT)
    public ResponseEntity<EnquiryBidData> unselectEnquiryBid(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @PathVariable long enquiryBidId) {
        logger.debug(MessageFormat.format(
                "Unselecting bid {0} for research enquiry", enquiryBidId));

        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", expertAddress));
        }

        EnquiryBidData enquiryBid =
                enquiryBidService.setEnquiryBidSelection(enquiryBidId, false);

        return new ResponseEntity<>(enquiryBid, OK);
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}", method = DELETE)
    public ResponseEntity<Void> deleteEnquiryBid(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @PathVariable long enquiryBidId) {
        logger.debug(MessageFormat.format(
                "Deleting bid {0} for research enquiry", enquiryBidId));

        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", expertAddress));
        }

        enquiryBidService.deleteEnquiryBid(enquiryBidId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/confirm", method = PUT)
    public ResponseEntity<Void> confirmEnquiryBidCreation(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @PathVariable long enquiryBidId,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format(
                "Confirming bid creation {0} for research enquiry", enquiryBidId));

        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", expertAddress));
        }

        enquiryBidService.confirmEnquiryBidCreation(enquiryBidId, transactionHash);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/commit", method = PUT)
    public ResponseEntity<Void> commitEnquiryBidCreation(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @PathVariable long enquiryBidId,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format(
                "Committing enquiry bid creation {0} {1}",
                enquiryBidId, transactionHash));

        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", expertAddress));
        }

        enquiryBidService.commitEnquiryBidCreation(enquiryBidId, transactionHash);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/enquiry/bid/expert", method = GET)
    public ResponseEntity<List<EnquiryBidData>> getExpertEnquiryBids(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Getting expert enquiry bids {0}", expertAddress));

        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", expertAddress));
        }

        List<EnquiryBidData> researchItems =
                enquiryBidService.getEnquiryBidsByExpert(expertAddress).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return new ResponseEntity<>(researchItems, OK);
    }

    @RequestMapping(value = "/enquiry/{enquiryId}/bid", method = GET)
    public ResponseEntity<List<EnquiryBidData>> getEnquiryBids(
            @SessionAttribute(USER_ADDRESS) String expertAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            @PathVariable long enquiryId,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Getting bids for research enquiry {0}", enquiryId));

        if (!Boolean.valueOf(userAuthorised)) {
            throw new UnauthorizedException(MessageFormat.format(
                    "Not authorised {0}", expertAddress));
        }

        List<EnquiryBidData> enquiryBids =
                enquiryBidService.getEnquiryBidsByEnquiry(enquiryId).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return new ResponseEntity<>(enquiryBids, OK);
    }
}
