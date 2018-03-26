package com.wrisx.wrisxdapp.facilitator.controller;

import com.wrisx.wrisxdapp.data.request.FacilitatorRequest;
import com.wrisx.wrisxdapp.data.request.TransactionHashRequest;
import com.wrisx.wrisxdapp.data.response.FacilitatorData;
import com.wrisx.wrisxdapp.domain.Facilitator;
import com.wrisx.wrisxdapp.facilitator.service.FacilitatorService;
import com.wrisx.wrisxdapp.security.service.AuthenticationService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
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
public class FacilitatorController {
    private final Logger logger = LoggerFactory.getLogger(FacilitatorController.class);

    private final FacilitatorService facilitatorService;
    private final AuthenticationService authenticationService;

    @Autowired
    public FacilitatorController(FacilitatorService facilitatorService,
                                 AuthenticationService authenticationService) {
        this.facilitatorService = facilitatorService;
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/facilitator", method = POST)
    public ResponseEntity<?> createFacilitator(
            @RequestBody FacilitatorRequest facilitatorRequest,
            HttpServletRequest request) {
        logger.debug(MessageFormat.format("Creating facilitator {0}",
                facilitatorRequest.getAddress()));

        Facilitator facilitator = facilitatorService.createFacilitator(facilitatorRequest);
        authenticationService.storeUserInSession(request, facilitator.getUser().getEmailAddress());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/facilitator/{address}", method = GET)
    public ResponseEntity<FacilitatorData> getFacilitator(
            @PathVariable("address") String facilitatorAddress) {
        logger.debug(MessageFormat.format(
                "Getting facilitator {0}", facilitatorAddress));

        FacilitatorData facilitator = facilitatorService.getFacilitator(facilitatorAddress);

        return ResponseEntity.ok(facilitator);
    }

    @RequestMapping(value = "/facilitator/{address}", method = DELETE)
    public ResponseEntity<Void> deleteFacilitator(
            @PathVariable("address") String facilitatorAddress,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, facilitatorAddress);

        logger.debug(MessageFormat.format(
                "Deleting facilitator {0}", facilitatorAddress));

        facilitatorService.deleteFacilitator(facilitatorAddress);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/facilitator/{address}/confirm", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmFacilitatorCreation(
            @PathVariable("address") String facilitatorAddress,
            @RequestBody TransactionHashRequest transactionHashRequest,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, facilitatorAddress);

        logger.debug(MessageFormat.format(
                "Confirming facilitator creation {0}", facilitatorAddress));

        facilitatorService.confirmFacilitatorCreation(facilitatorAddress,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/facilitator/{address}/commit", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> commitFacilitatorCreation(
            @PathVariable("address") String facilitatorAddress,
            @RequestBody TransactionHashRequest transactionHashRequest,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, facilitatorAddress);

        logger.debug(MessageFormat.format(
                "Committing facilitator creation {0} {1}",
                facilitatorAddress, transactionHashRequest.getTransactionHash()));

        facilitatorService.commitFacilitatorCreation(facilitatorAddress,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/facilitator", method = GET)
    public ResponseEntity<List<Facilitator>> getFacilitators(Pageable pageable) {
        logger.debug("Getting facilitators");

        List<Facilitator> facilitators =
                facilitatorService.getFacilitators().stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(facilitators);
    }

    @RequestMapping(value = "/facilitator/{address}/approve/{uuid}", method = POST,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> approveResearch(
            @PathVariable("address") String facilitatorAddress,
            @PathVariable("uuid") String uuid,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, facilitatorAddress);

        logger.debug(MessageFormat.format(
                "Confirming facilitator creation {0}", facilitatorAddress));

        facilitatorService.approveResearch(facilitatorAddress, uuid);

        return ResponseEntity.ok().build();
    }
}
