package com.wrisx.wrisxdapp.client.service;

import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.data.request.ClientRequest;
import com.wrisx.wrisxdapp.data.response.ClientData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.domain.UserDao;
import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import com.wrisx.wrisxdapp.errorhandling.ResourceNotFoundException;
import com.wrisx.wrisxdapp.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.wrisx.wrisxdapp.domain.State.COMMITTED;
import static com.wrisx.wrisxdapp.domain.State.CONFIRMED;
import static com.wrisx.wrisxdapp.domain.State.CREATED;
import static com.wrisx.wrisxdapp.util.WrisxUtil.validateStringArgument;
import static com.wrisx.wrisxdapp.util.WrisxUtil.validateStringArguments;
import static java.util.stream.Collectors.toList;

@Service
public class ClientService {
    private final Logger logger = LoggerFactory.getLogger(ClientService.class);

    private final ClientDao clientDao;
    private final UserService userService;
    private final UserDao userDao;
    private final EntityProvider entityProvider;

    @Autowired
    public ClientService(ClientDao clientDao, UserService userService, UserDao userDao,
                         EntityProvider entityProvider) {
        this.clientDao = clientDao;
        this.userService = userService;
        this.userDao = userDao;
        this.entityProvider = entityProvider;
    }

    public Client createClient(ClientRequest clientRequest) {
        validateStringArgument(clientRequest.getAddress(), "Address cannot be empty");
        validateStringArguments(clientRequest.getName(),
                clientRequest.getEmailAddress(),
                clientRequest.getDescription(),
                clientRequest.getPassword());

        Client client = clientDao.findByAddress(clientRequest.getAddress());

        if (client == null) {
            User user = userDao.findByAddress(clientRequest.getAddress());
            if (user == null) {
                user = new User(clientRequest.getAddress(),
                        clientRequest.getName(),
                        clientRequest.getEmailAddress(),
                        clientRequest.getPassword(),
                        clientRequest.getProfileLink(),
                        clientRequest.getWebsiteLink());
                user = userDao.save(user);
            }

            client = new Client(clientRequest.getAddress(),
                    clientRequest.getDescription(),
                    user);
            client = clientDao.save(client);

            return client;
        }

        String msg = MessageFormat.format("Client already exists {0}", clientRequest.getAddress());
        logger.error(msg);
        throw new BadRequestException(msg);
    }

    public ClientData getClient(String clientAddress) throws ResourceNotFoundException {
        Client client = entityProvider.getClientByAddress(clientAddress);

        return new ClientData(client);
    }

    public void deleteClient(String clientAddress) throws ResourceNotFoundException {
        Client client = entityProvider.getClientByAddress(clientAddress);

        clientDao.delete(client);

        userService.deleteUserIfPossible(clientAddress);
    }

    public void confirmClientCreation(String clientAddress, String transactionHash)
            throws ResourceNotFoundException {
        Client client = entityProvider.getClientByAddress(clientAddress);

        client.setState(CONFIRMED);
        client.setTransactionHash(transactionHash);

        User user = entityProvider.getUserByAddress(clientAddress);
        if (user.getState() == CREATED) {
            user.setState(CONFIRMED);
            user.setTransactionHash(transactionHash);
            userDao.save(user);
        }

        clientDao.save(client);
    }

    public void commitClientCreation(String clientAddress, String transactionHash)
            throws ResourceNotFoundException {
        Client client = entityProvider.getClientByAddressAndTransactionHash(
                clientAddress, transactionHash);

        if (client.getState() != CONFIRMED) {
            throw new BadRequestException(MessageFormat.format(
                    "Illegal state of client {0}", clientAddress));
        }
        client.setState(COMMITTED);

        User user = entityProvider.getUserByAddress(clientAddress);
        if (user.getState() == CONFIRMED) {
            user.setState(COMMITTED);
            user.setTransactionHash(transactionHash);
            userDao.save(user);
        }

        clientDao.save(client);
    }

    public List<ClientData> getClients() {
        List<Client> clients = new ArrayList<>();
        clientDao.findAll().forEach(clients::add);
        return clients.stream().map(client -> new ClientData(client)).collect(toList());
    }
}
