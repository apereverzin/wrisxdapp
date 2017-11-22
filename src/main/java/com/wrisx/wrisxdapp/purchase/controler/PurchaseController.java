package com.wrisx.wrisxdapp.purchase.controler;

import com.wrisx.wrisxdapp.data.PurchaseData;
import com.wrisx.wrisxdapp.purchase.service.PurchaseService;
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
public class PurchaseController {
    private final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @RequestMapping(value = "/purchase", method = POST)
    public ResponseEntity<PurchaseData> payForResearch(
            @RequestParam("address")String clientAddress,
            @RequestParam("uuid")String uuid) {
        logger.debug(MessageFormat.format(
                "Client {0} is paying for {1}", clientAddress, uuid));

        PurchaseData purchaseData = purchaseService.createPurchase(clientAddress, uuid);

        return new ResponseEntity<>(purchaseData, OK);
    }

    @RequestMapping(value = "/purchase/client/{address}", method = GET)
    public ResponseEntity<List<PurchaseData>> getClientPurchases(
            @PathVariable("address")String clientAddress) {
        logger.debug(MessageFormat.format(
                "Getting client purchases {0}", clientAddress));

        List<PurchaseData> purchases = purchaseService.getClientPurchases(clientAddress);

        return new ResponseEntity<>(purchases, OK);
    }

    @RequestMapping(value = "/purchase/expert/{address}", method = GET)
    public ResponseEntity<List<PurchaseData>> getExpertPurchases(
            @PathVariable("address")String expertAddress) {
        logger.debug(MessageFormat.format(
                "Getting expert purchases {0}", expertAddress));

        List<PurchaseData> purchases = purchaseService.getExpertPurchases(expertAddress);

        return new ResponseEntity<>(purchases, OK);
    }

    @RequestMapping(value = "/purchase/research/{uuid}", method = GET)
    public ResponseEntity<List<PurchaseData>> getResearchPurchases(
            @PathVariable("uuid")String uuid) {
        logger.debug(MessageFormat.format("Client research purchases {0}", uuid));

        List<PurchaseData> purchases = purchaseService.getResearchPurchases(uuid);

        return new ResponseEntity<>(purchases, OK);
    }
}
