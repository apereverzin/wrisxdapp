package com.wrisx.wrisxdapp.client.controller;

import com.wrisx.wrisxdapp.client.service.ClientService;
import com.wrisx.wrisxdapp.data.ClientData;
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
            @RequestParam("name") String name) {
        logger.debug(MessageFormat.format("Creating client {0}", address));

        clientService.saveClient(address, name);

        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/client/{address}", method = GET)
    public ResponseEntity<ClientData> getClient(
            @PathVariable("address") String clientAddress) {
        logger.debug(MessageFormat.format("Getting client {0}", clientAddress));

        ClientData client = clientService.getClient(clientAddress);

        if (client == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }

        return new ResponseEntity<>(client, OK);
    }

    @RequestMapping(value = "/client", method = GET)
    public ResponseEntity<List<ClientData>> getClients() {
        logger.debug("Getting clients");

        List<ClientData> clients = clientService.getClients();

        return new ResponseEntity<>(clients, OK);
    }
}
