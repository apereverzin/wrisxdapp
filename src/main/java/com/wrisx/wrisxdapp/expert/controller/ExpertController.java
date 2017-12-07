package com.wrisx.wrisxdapp.expert.controller;

import com.wrisx.wrisxdapp.data.ExpertData;
import com.wrisx.wrisxdapp.expert.service.ExpertService;
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

import static org.springframework.http.HttpStatus.OK;
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

    @RequestMapping(value = "/expert", method = POST)
    public ResponseEntity<?> createExpert(
            @RequestParam("address") String address,
            @RequestParam("name") String name,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("keyWords") String keyWords,
            @RequestParam("description") String description) {
        logger.debug(MessageFormat.format("Creating expert {0}", address));

        expertService.saveExpert(address, name, emailAddress, keyWords, description);

        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/expert/{address}", method = GET)
    public ResponseEntity<ExpertData> getExpert(
            @PathVariable("address") String expertAddress) {
        logger.debug(MessageFormat.format("Getting expert {0}", expertAddress));

        ExpertData expert = expertService.getExpert(expertAddress);
        return new ResponseEntity<>(expert, OK);
    }

    @RequestMapping(value = "/expert/{address}", method = DELETE)
    public ResponseEntity<?> deleteExpert(
            @PathVariable("address") String expertAddress) {
        logger.debug(MessageFormat.format("Deleting expert {0}", expertAddress));

        expertService.deleteExpert(expertAddress);
        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/expert/{address}/confirm", method = PUT)
    public ResponseEntity<Void> confirmExpertCreation(
            @PathVariable("address") String expertAddress,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format(
                "Confirming expert creation {0}", expertAddress));

        expertService.confirmExpertCreation(expertAddress, transactionHash);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/expert/{address}/commit", method = PUT)
    public ResponseEntity<Void> commitExpertCreation(
            @PathVariable("address") String expertAddress,
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
        List<ExpertData> experts = expertService.findExperts(keywords);

        return new ResponseEntity<>(experts, OK);
    }
}
