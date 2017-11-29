package com.wrisx.wrisxdapp.purchase.controler;

import com.wrisx.wrisxdapp.data.PurchaseData;
import com.wrisx.wrisxdapp.exception.NotFoundException;
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

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

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

        try {
            PurchaseData purchaseData =
                    purchaseService.createPurchase(clientAddress, uuid);
            return new ResponseEntity<>(purchaseData, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/purchase/{id}", method = DELETE)
    public ResponseEntity<Void> deleteResearch(@PathVariable("id") long purchaseId) {
        logger.debug(MessageFormat.format("Deleting purchase {0}", purchaseId));

        try {
            purchaseService.deletePurchase(purchaseId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/purchase/{id}/confirm", method = PUT)
    public ResponseEntity<Void> confirmResearch(
            @PathVariable("id") long purchaseId,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format("Confirming purchase {0}", purchaseId));

        try {
            purchaseService.confirmPurchaseCreation(purchaseId, transactionHash);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/purchase/client/{address}", method = GET)
    public ResponseEntity<List<PurchaseData>> getClientPurchases(
            @PathVariable("address")String clientAddress) {
        logger.debug(MessageFormat.format(
                "Getting client purchases {0}", clientAddress));

        try {
            List<PurchaseData> purchases =
                    purchaseService.getClientPurchases(clientAddress);
            return new ResponseEntity<>(purchases, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }

    }

    @RequestMapping(value = "/purchase/expert/{address}", method = GET)
    public ResponseEntity<List<PurchaseData>> getExpertPurchases(
            @PathVariable("address")String expertAddress) {
        logger.debug(MessageFormat.format(
                "Getting expert purchases {0}", expertAddress));

        try {
            List<PurchaseData> purchases =
                    purchaseService.getExpertPurchases(expertAddress);
            return new ResponseEntity<>(purchases, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/purchase/research/{uuid}", method = GET)
    public ResponseEntity<List<PurchaseData>> getResearchPurchases(
            @PathVariable("uuid")String uuid) {
        logger.debug(MessageFormat.format("Client research purchases {0}", uuid));

        try {
            List<PurchaseData>purchases = purchaseService.getResearchPurchases(uuid);
            return new ResponseEntity<>(purchases, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }
}
