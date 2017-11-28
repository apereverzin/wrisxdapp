package com.wrisx.wrisxdapp.client.controller;

import com.wrisx.wrisxdapp.client.service.ClientService;
import com.wrisx.wrisxdapp.data.ClientData;
import com.wrisx.wrisxdapp.exception.NotFoundException;
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

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
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
    public ResponseEntity<?> createClient(
            @RequestParam("address") String address,
            @RequestParam("name") String name,
            @RequestParam("emailAddress") String emailAddress,
            @RequestParam("description") String description) {
        logger.debug(MessageFormat.format("Creating client {0}", address));

        clientService.saveClient(address, name, emailAddress, description);

        return new ResponseEntity<>(CREATED);
    }

    @RequestMapping(value = "/client/{address}", method = GET)
    public ResponseEntity<ClientData> getClient(
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format("Getting client {0}", clientAddress));

        try {
            ClientData client = clientService.getClient(clientAddress);
            return new ResponseEntity<>(client, OK);
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return new ResponseEntity<>(NOT_FOUND);
        }
    }

    @RequestMapping(value = "/client/{address}", method = DELETE)
    public ResponseEntity<Void> deleteClient(
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format("Deleting client {0}", clientAddress));

        try {
            clientService.deleteClient(clientAddress);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/client/{address}/confirm", method = PUT)
    public ResponseEntity<Void> confirmClientCreation(
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format(
                "Confirming client creation {0}", clientAddress));

        try {
            clientService.confirmClientCreation(clientAddress);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage(), ex);
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/client", method = GET)
    public ResponseEntity<List<ClientData>> getClients() {
        logger.debug("Getting clients");

        List<ClientData> clients = clientService.getClients();

        return new ResponseEntity<>(clients, OK);
    }
}
