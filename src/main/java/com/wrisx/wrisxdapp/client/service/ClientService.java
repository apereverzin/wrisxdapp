package com.wrisx.wrisxdapp.client.service;

import com.wrisx.wrisxdapp.data.ClientData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import com.wrisx.wrisxdapp.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientService {
    private final ClientDao clientDao;
    private final ExpertDao expertDao;

    @Autowired
    public ClientService(ClientDao clientDao, ExpertDao expertDao) {
        this.clientDao = clientDao;
        this.expertDao = expertDao;
    }

    public ClientData saveClient(String clientAddress, String name,
                                 String emailAddress, String description) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            String clientName;
            String clientEmailAddress;
            String clientDescription;

            Expert expert = expertDao.findByAddress(clientAddress);
            if (expert != null) {
                clientName = expert.getName();
                clientEmailAddress = expert.getEmailAddress();
            } else {
                clientName = name;
                clientEmailAddress = emailAddress;
            }
            clientDescription = description;

            client = new Client(clientAddress, clientName,
                    clientEmailAddress, clientDescription);
            client = clientDao.save(client);
        }
        return new ClientData(client);
    }

    public ClientData getClient(String clientAddress) throws NotFoundException {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new NotFoundException(MessageFormat.format("Client not found {0}", clientAddress));
        }
        return new ClientData(client);
    }

    public void deleteClient(String clientAddress) throws NotFoundException {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            throw new NotFoundException(MessageFormat.format("Client not found {0}", clientAddress));
        }
        clientDao.delete(client);
    }

    public List<ClientData> getClients() {
        List<Client> clients = new ArrayList<>();
        clientDao.findAll().forEach(clients::add);
        return clients.stream().map(client -> new ClientData(client)).collect(toList());
    }
}
