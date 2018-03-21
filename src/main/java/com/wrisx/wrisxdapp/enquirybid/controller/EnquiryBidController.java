package com.wrisx.wrisxdapp.enquirybid.controller;

import com.wrisx.wrisxdapp.data.request.TransactionHashRequest;
import com.wrisx.wrisxdapp.data.response.EnquiryBidData;
import com.wrisx.wrisxdapp.enquirybid.service.EnquiryBidService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
public class EnquiryBidController {
    private final Logger logger = LoggerFactory.getLogger(EnquiryBidController.class);

    private final EnquiryBidService enquiryBidService;
    private final AuthenticationService authenticationService;

    @Autowired
    public EnquiryBidController(EnquiryBidService enquiryBidService,
                                AuthenticationService authenticationService) {
        this.enquiryBidService = enquiryBidService;
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/enquiry/{enquiryId}/bid", method = POST)
    public ResponseEntity<EnquiryBidData> placeEnquiryBid(
            @PathVariable long enquiryId,
            @RequestParam("address") String expertAddress,
            @RequestParam("bid") int bid,
            @RequestParam("comment") String comment,
            HttpServletRequest request) {
        String emailAddress = authenticationService.authenticateRequest(request);

        logger.debug(MessageFormat.format(
                "Placing bid {0} for research enquiry {1}", bid, enquiryId));

        EnquiryBidData enquiryBid =
                enquiryBidService.placeEnquiryBid(enquiryId, expertAddress, bid, comment);

        return ResponseEntity.ok(enquiryBid);
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/select", method = PUT)
    public ResponseEntity<EnquiryBidData> selectEnquiryBid(
            @PathVariable long enquiryBidId,
            HttpServletRequest request) {
        String emailAddress = authenticationService.authenticateRequest(request);

        logger.debug(MessageFormat.format(
                "Selecting bid {0} for research enquiry", enquiryBidId));

        EnquiryBidData enquiryBid =
                enquiryBidService.setEnquiryBidSelection(enquiryBidId, true);

        return ResponseEntity.ok(enquiryBid);
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/unselect", method = PUT)
    public ResponseEntity<EnquiryBidData> unselectEnquiryBid(
            @PathVariable long enquiryBidId,
            HttpServletRequest request) {
        String emailAddress = authenticationService.authenticateRequest(request);

        logger.debug(MessageFormat.format(
                "Unselecting bid {0} for research enquiry", enquiryBidId));

        EnquiryBidData enquiryBid =
                enquiryBidService.setEnquiryBidSelection(enquiryBidId, false);

        return ResponseEntity.ok(enquiryBid);
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}", method = DELETE)
    public ResponseEntity<Void> deleteEnquiryBid(
            @PathVariable long enquiryBidId,
            HttpServletRequest request) {
        String emailAddress = authenticationService.authenticateRequest(request);

        logger.debug(MessageFormat.format(
                "Deleting bid {0} for research enquiry", enquiryBidId));

        enquiryBidService.deleteEnquiryBid(enquiryBidId);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/confirm", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmEnquiryBidCreation(
            @PathVariable long enquiryBidId,
            @RequestBody TransactionHashRequest transactionHashRequest,
            HttpServletRequest request) {
        String emailAddress = authenticationService.authenticateRequest(request);

        logger.debug(MessageFormat.format(
                "Confirming bid creation {0} for research enquiry", enquiryBidId));

        enquiryBidService.confirmEnquiryBidCreation(enquiryBidId,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/enquiry/bid/{enquiryBidId}/commit", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> commitEnquiryBidCreation(
            @PathVariable long enquiryBidId,
            @RequestBody TransactionHashRequest transactionHashRequest,
            HttpServletRequest request) {
        String emailAddress = authenticationService.authenticateRequest(request);

        logger.debug(MessageFormat.format(
                "Committing enquiry bid creation {0} {1}",
                enquiryBidId, transactionHashRequest.getTransactionHash()));

        enquiryBidService.commitEnquiryBidCreation(enquiryBidId,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/enquiry/bid/expert/{expertAddress}", method = GET)
    public ResponseEntity<List<EnquiryBidData>> getExpertEnquiryBids(
            @PathVariable String expertAddress,
            Pageable pageable,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, expertAddress);

        logger.debug(MessageFormat.format(
                "Getting expert enquiry bids {0}", expertAddress));

        List<EnquiryBidData> researchItems =
                enquiryBidService.getEnquiryBidsByExpert(expertAddress).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(researchItems);
    }

    @RequestMapping(value = "/enquiry/{enquiryId}/bid", method = GET)
    public ResponseEntity<List<EnquiryBidData>> getEnquiryBids(
            @PathVariable long enquiryId,
            Pageable pageable,
            HttpServletRequest request) {
        String emailAddress = authenticationService.authenticateRequest(request);

        logger.debug(MessageFormat.format(
                "Getting bids for research enquiry {0}", enquiryId));

        List<EnquiryBidData> enquiryBids =
                enquiryBidService.getEnquiryBidsByEnquiry(enquiryId).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(enquiryBids);
    }
}
