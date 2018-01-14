package com.wrisx.wrisxdapp.purchase.controler;

import com.wrisx.wrisxdapp.data.request.PurchaseRequest;
import com.wrisx.wrisxdapp.data.request.TransactionHashRequest;
import com.wrisx.wrisxdapp.data.response.PurchaseData;
import com.wrisx.wrisxdapp.purchase.service.PurchaseService;
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
public class PurchaseController {
    private final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    private final PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @RequestMapping(value = "/purchase", method = POST,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<PurchaseData> payForResearch(
            @RequestBody PurchaseRequest purchaseRequest) {
        logger.debug(MessageFormat.format("Client {0} is paying for {1}",
                purchaseRequest.getAddress(), purchaseRequest.getUuid()));

        PurchaseData purchase = purchaseService.createPurchase(purchaseRequest);

        return ResponseEntity.ok(purchase);
    }

    @RequestMapping(value = "/purchase/{id}", method = DELETE)
    public ResponseEntity<Void> deleteResearch(
            @PathVariable("id") long purchaseId) {
        logger.debug(MessageFormat.format("Deleting purchase {0}", purchaseId));

        purchaseService.deletePurchase(purchaseId);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/purchase/{id}/confirm", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmResearch(
            @PathVariable("id") long purchaseId,
            @RequestBody TransactionHashRequest transactionHashRequest) {
        logger.debug(MessageFormat.format("Confirming purchase {0}", purchaseId));

        purchaseService.confirmPurchaseCreation(purchaseId,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/purchase/{id}/commit", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> commitPurchase(
            @PathVariable("id") long purchaseId,
            @RequestBody TransactionHashRequest transactionHashRequest) {
        logger.debug(MessageFormat.format(
                "Committing purchase {0} {1}",
                purchaseId, transactionHashRequest.getTransactionHash()));

        purchaseService.commitPurchaseCreation(purchaseId,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/purchase/client/{address}", method = GET)
    public ResponseEntity<List<PurchaseData>> getClientPurchases(
            @PathVariable("address") String clientAddress,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Getting client purchases {0}", clientAddress));

        List<PurchaseData> purchases =
                purchaseService.getClientPurchases(clientAddress).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(purchases);
    }

    @RequestMapping(value = "/purchase/expert/{address}", method = GET)
    public ResponseEntity<List<PurchaseData>> getExpertPurchases(
            @PathVariable("address") String expertAddress,
            Pageable pageable) {
        logger.debug(MessageFormat.format(
                "Getting expert purchases {0}", expertAddress));

        List<PurchaseData> purchases =
                purchaseService.getExpertPurchases(expertAddress).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(purchases);
    }

    @RequestMapping(value = "/purchase/research/{uuid}", method = GET)
    public ResponseEntity<List<PurchaseData>> getResearchPurchases(
            @PathVariable("uuid") String uuid,
            Pageable pageable) {
        logger.debug(MessageFormat.format("Client research purchases {0}", uuid));

        List<PurchaseData> purchases =
                purchaseService.getResearchPurchases(uuid).stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(purchases);
    }
}
