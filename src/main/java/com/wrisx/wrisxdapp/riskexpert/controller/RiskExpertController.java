package com.wrisx.wrisxdapp.riskexpert.controller;

import com.wrisx.wrisxdapp.data.RiskExpertData;
import com.wrisx.wrisxdapp.riskexpert.service.RiskExpertService;
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
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RiskExpertController {
    private final Logger logger = LoggerFactory.getLogger(RiskExpertController.class);

    private final RiskExpertService riskExpertService;

    @Autowired
    public RiskExpertController(RiskExpertService riskExpertService) {
        this.riskExpertService = riskExpertService;
    }

    @RequestMapping(value = "/riskExpert", method = POST)
    public ResponseEntity<?> createRiskExpert(
            @RequestParam("address") String address,
            @RequestParam("name") String name) {
        logger.debug(MessageFormat.format("Creating risk expert {0}", address));

        riskExpertService.saveRiskExpert(address, name);

        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/riskExpert/{address}", method = GET)
    public ResponseEntity<RiskExpertData> getRiskExpert(
            @PathVariable("address") String riskExpertAddress) {
        logger.debug(MessageFormat.format("Getting risk expert {0}", riskExpertAddress));

        RiskExpertData riskExpert = riskExpertService.getRiskExpert(riskExpertAddress);

        if (riskExpert == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        return new ResponseEntity<>(riskExpert, OK);
    }

    @RequestMapping(value = "/riskExpert", method = GET)
    public ResponseEntity<List<RiskExpertData>> getRiskExperts() {
        logger.debug("Getting risk experts");

        List<RiskExpertData> riskExperts = riskExpertService.getRiskExperts();

        return new ResponseEntity<>(riskExperts, OK);
    }
}
