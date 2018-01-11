package com.wrisx.wrisxdapp.expert.controller;

import com.wrisx.wrisxdapp.data.request.ExpertRequest;
import com.wrisx.wrisxdapp.data.response.ExpertData;
import com.wrisx.wrisxdapp.expert.service.ExpertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.MessageFormat;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class ExpertController {
    private final Logger logger = LoggerFactory.getLogger(ExpertController.class);

    private final ExpertService expertService;

    @Autowired
    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @RequestMapping(value = "/expert", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createExpert(@RequestBody ExpertRequest expertRequest) {
        logger.debug(MessageFormat.format("Creating expert {0}", expertRequest.getAddress()));

        expertService.createExpert(expertRequest);

        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/expert/{expertAddress}", method = GET)
    public ResponseEntity<ExpertData> getExpert(
            @PathVariable String expertAddress) {
        logger.debug(MessageFormat.format("Getting expert {0}", expertAddress));

        ExpertData expertData = expertService.getExpert(expertAddress);

        return new ResponseEntity<>(expertData, OK);
    }

    @RequestMapping(value = "/expert/{expertAddress}", method = DELETE)
    public ResponseEntity<?> deleteExpert(@PathVariable String expertAddress) {
        logger.debug(MessageFormat.format("Deleting expert {0}", expertAddress));

        expertService.deleteExpert(expertAddress);

        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/expert/{expertAddress}/confirm", method = PUT)
    public ResponseEntity<Void> confirmExpertCreation(
            @PathVariable String expertAddress,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format(
                "Confirming expert creation {0}", expertAddress));

        expertService.confirmExpertCreation(expertAddress, transactionHash);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/expert/{expertAddress}/commit", method = PUT)
    public ResponseEntity<Void> commitExpertCreation(
            @PathVariable String expertAddress,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format(
                "Committing expert creation {0} {1}", expertAddress, transactionHash));

        expertService.commitExpertCreation(expertAddress, transactionHash);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/expert/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ExpertData>> findExperts(
            @PathVariable String keywords,
            Pageable pageable) {
        logger.debug(MessageFormat.format("Searching experts {0}", keywords));

        return getExpertsByKeywords(keywords, pageable);
    }

    @RequestMapping(value = "/expert/keywords", method = GET)
    public ResponseEntity<List<ExpertData>> findAllExperts(Pageable pageable) {
        logger.debug("Searching experts");

        return getExpertsByKeywords("", pageable);
    }

    private ResponseEntity<List<ExpertData>> getExpertsByKeywords(
            @PathVariable String keywords,
            Pageable pageable) {
        List<ExpertData> experts =
                expertService.findExperts(keywords).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return new ResponseEntity<>(experts, OK);
    }
}
