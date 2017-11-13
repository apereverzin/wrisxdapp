package com.wrisx.wrisxdapp.client.service;

import com.wrisx.wrisxdapp.data.ClientData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientService {
    private final ClientDao clientDao;

    @Autowired
    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public ClientData saveClient(String address,
                                 String name) {
        Client client = clientDao.findByAddress(address);
        if (client == null) {
            client = new Client(address, name);
            client = clientDao.save(client);
        }
        return new ClientData(client);
    }

    public ClientData getClient(String clientAddress) {
        Client riskExpert = clientDao.findByAddress(clientAddress);
        if (riskExpert == null) {
            return null;
        }
        return new ClientData(riskExpert);
    }

    public List<ClientData> getClients() {
        List<Client> clients = new ArrayList<>();
        clientDao.findAll().forEach(clients::add);
        return clients.stream().map(client -> new ClientData(client)).collect(toList());
    }
}
