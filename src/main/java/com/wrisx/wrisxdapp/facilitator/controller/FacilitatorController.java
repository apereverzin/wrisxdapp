package com.wrisx.wrisxdapp.facilitator.controller;

import com.wrisx.wrisxdapp.domain.Facilitator;
import com.wrisx.wrisxdapp.facilitator.service.FacilitatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.MessageFormat;
import java.util.List;

import static com.wrisx.wrisxdapp.init.controller.InitController.USER_ADDRESS;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class FacilitatorController {
    private final Logger logger = LoggerFactory.getLogger(FacilitatorController.class);

    private final FacilitatorService facilitatorService;

    @Autowired
    public FacilitatorController(FacilitatorService facilitatorService) {
        this.facilitatorService = facilitatorService;
    }

    @RequestMapping(value = "/facilitator", method = POST)
    public ResponseEntity<?> createFacilitator(
            @SessionAttribute(USER_ADDRESS) String address,
            @RequestParam("name") String name,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("description") String description) {
        logger.debug(MessageFormat.format("Creating facilitator {0}", address));

        facilitatorService.saveFacilitator(address, name, emailAddress, description);

        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/facilitator", method = GET)
    public ResponseEntity<List<Facilitator>> getFacilitators(Pageable pageable) {
        logger.debug("Getting facilitators");

        List<Facilitator> facilitators =
                facilitatorService.getFacilitators().stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return new ResponseEntity<>(facilitators, OK);
    }
}
