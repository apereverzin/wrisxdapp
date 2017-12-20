package com.wrisx.wrisxdapp.client.controller;

import com.wrisx.wrisxdapp.client.service.ClientService;
import com.wrisx.wrisxdapp.data.ClientData;
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

import static com.wrisx.wrisxdapp.user.controller.UserController.USER_ADDRESS;
import static com.wrisx.wrisxdapp.user.controller.UserController.USER_AUTHORISED;
import static com.wrisx.wrisxdapp.util.WrisxUtil.verifyUserAuthorisation;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class ClientController {
    private final Logger logger = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @RequestMapping(value = "/client", method = POST)
    public ResponseEntity<Void> createClient(
            @SessionAttribute(USER_ADDRESS) String address,
            @RequestParam("name") String name,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("description") String description,
            @RequestParam("secret") String secret) {
        logger.debug(MessageFormat.format("Creating client {0}", address));

        clientService.createClient(address, name, emailAddress, description, secret);

        return new ResponseEntity<>(CREATED);
    }

    @RequestMapping(value = "/client", method = GET)
    public ResponseEntity<ClientData> getClient(
            @SessionAttribute(USER_ADDRESS) String clientAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised) {
        logger.debug(MessageFormat.format("Getting client {0}", clientAddress));

        verifyUserAuthorisation(clientAddress, userAuthorised);

        ClientData client = clientService.getClient(clientAddress);

        return new ResponseEntity<>(client, OK);
    }

    @RequestMapping(value = "/client", method = DELETE)
    public ResponseEntity<Void> deleteClient(
            @SessionAttribute(USER_ADDRESS) String clientAddress) {
        logger.debug(MessageFormat.format("Deleting client {0}", clientAddress));

        clientService.deleteClient(clientAddress);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/client/confirm", method = PUT)
    public ResponseEntity<Void> confirmClientCreation(
            @SessionAttribute(USER_ADDRESS) String clientAddress,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format(
                "Confirming client creation {0}", clientAddress));

        clientService.confirmClientCreation(clientAddress, transactionHash);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/client/commit", method = PUT)
    public ResponseEntity<Void> commitClientCreation(
            @SessionAttribute(USER_ADDRESS) String clientAddress,
            @RequestParam("transactionHash") String transactionHash) {
        logger.debug(MessageFormat.format(
                "Committing client creation {0} {1}", clientAddress, transactionHash));

        clientService.commitClientCreation(clientAddress, transactionHash);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/clients", method = GET)
    public ResponseEntity<List<ClientData>> getClients(
            @SessionAttribute(USER_ADDRESS) String clientAddress,
            @SessionAttribute(name = USER_AUTHORISED, required = false) String userAuthorised,
            Pageable pageable) {
        logger.debug("Getting clients");

        verifyUserAuthorisation(clientAddress, userAuthorised);

        List<ClientData> clients =
                clientService.getClients().stream().
                        skip(pageable.getPageNumber() * pageable.getPageSize()).
                        limit(pageable.getPageSize()).
                        collect(toList());

        return new ResponseEntity<>(clients, OK);
    }
}
