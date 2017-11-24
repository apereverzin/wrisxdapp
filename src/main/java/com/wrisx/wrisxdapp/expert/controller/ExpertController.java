package com.wrisx.wrisxdapp.expert.controller;

import com.wrisx.wrisxdapp.data.ClientData;
import com.wrisx.wrisxdapp.data.ExpertData;
import com.wrisx.wrisxdapp.exception.NotFoundException;
import com.wrisx.wrisxdapp.expert.service.ExpertService;
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

        try {
            ExpertData expert = expertService.getExpert(expertAddress);
            return new ResponseEntity<>(expert, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/expert/{address}", method = DELETE)
    public ResponseEntity<?> deleteExpert(
            @PathVariable("address") String expertAddress) {
        logger.debug(MessageFormat.format("Getting expert {0}", expertAddress));

        try {
            expertService.deleteExpert(expertAddress);
            return new ResponseEntity<>(OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/expert/keywords/{keywords}", method = GET)
    public ResponseEntity<List<ExpertData>> findExperts(
            @PathVariable String keywords) {
        logger.debug(MessageFormat.format("Searching experts {0}", keywords));

        List<ExpertData> experts = expertService.findExperts(keywords);

        return new ResponseEntity<>(experts, OK);
    }

    @RequestMapping(value = "/expert/keywords", method = GET)
    public ResponseEntity<List<ExpertData>> findAllExperts() {
        logger.debug("Searching experts");

        List<ExpertData> experts = expertService.findExperts("");

        return new ResponseEntity<>(experts, OK);
    }
}
