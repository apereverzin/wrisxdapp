package com.wrisx.wrisxdapp.client.controller;

import com.wrisx.wrisxdapp.client.service.ClientService;
import com.wrisx.wrisxdapp.data.request.ClientRequest;
import com.wrisx.wrisxdapp.data.request.TransactionHashRequest;
import com.wrisx.wrisxdapp.data.response.ClientData;
import com.wrisx.wrisxdapp.domain.Client;
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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@CrossOrigin
@RestController
public class ClientController {
    private final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;
    private final AuthenticationService authenticationService;

    @Autowired
    public ClientController(ClientService clientService,
                            AuthenticationService authenticationService) {
        this.clientService = clientService;
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/client", method = POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> createClient(
            @RequestBody ClientRequest clientRequest,
            HttpServletRequest request) {
        logger.debug(MessageFormat.format("Creating client {0}", clientRequest.getAddress()));

        Client client  = clientService.createClient(clientRequest);
        authenticationService.storeUserInSession(request, client.getUser().getEmailAddress());

        return new ResponseEntity<>(CREATED);
    }

    @RequestMapping(value = "/client/{address}", method = GET)
    public ResponseEntity<ClientData> getClient(
            @PathVariable("address") String clientAddress,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, clientAddress);

        logger.debug(MessageFormat.format("Getting client {0}", clientAddress));

        ClientData client = clientService.getClient(clientAddress);

        return ResponseEntity.ok(client);
    }

    @RequestMapping(value = "/client/{address}", method = DELETE)
    public ResponseEntity<Void> deleteClient(
            @PathVariable("address") String clientAddress,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, clientAddress);

        logger.debug(MessageFormat.format("Deleting client {0}", clientAddress));

        clientService.deleteClient(clientAddress);

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/client/{address}/confirm", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> confirmClientCreation(
            @PathVariable("address") String clientAddress,
            @RequestBody TransactionHashRequest transactionHashRequest,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, clientAddress);

        logger.debug(MessageFormat.format(
                "Confirming client creation {0}", clientAddress));

        clientService.confirmClientCreation(clientAddress,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/client/{address}/commit", method = PUT,
            consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> commitClientCreation(
            @PathVariable("address") String clientAddress,
            @RequestBody TransactionHashRequest transactionHashRequest,
            HttpServletRequest request) {
        authenticationService.authenticateRequest(request, clientAddress);

        logger.debug(MessageFormat.format(
                "Committing client creation {0} {1}",
                clientAddress, transactionHashRequest.getTransactionHash()));

        clientService.commitClientCreation(clientAddress,
                transactionHashRequest.getTransactionHash());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/clients", method = GET)
    public ResponseEntity<List<ClientData>> getClients(Pageable pageable) {
        logger.debug("Getting clients");

        List<ClientData> clients =
                clientService.getClients().stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return ResponseEntity.ok(clients);
    }
}
