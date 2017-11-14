package com.wrisx.wrisxdapp.client.service;

import com.wrisx.wrisxdapp.data.ClientData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.RiskExpert;
import com.wrisx.wrisxdapp.domain.RiskExpertDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientService {
    private final ClientDao clientDao;
    private final RiskExpertDao riskExpertDao;

    @Autowired
    public ClientService(ClientDao clientDao, RiskExpertDao riskExpertDao) {
        this.clientDao = clientDao;
        this.riskExpertDao = riskExpertDao;
    }

    public ClientData saveClient(String clientAddress, String name,
                                 String emailAddress, String comment) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            String clientName;
            String clientEmailAddress;
            String clientComment;

            RiskExpert riskExpert = riskExpertDao.findByAddress(clientAddress);
            if (riskExpert != null) {
                clientName = riskExpert.getName();
                clientEmailAddress = riskExpert.getEmailAddress();
                clientComment = riskExpert.getComment();
            } else {
                clientName = name;
                clientEmailAddress = emailAddress;
                clientComment = comment;
            }

            client = new Client(clientAddress, clientName,
                    clientEmailAddress, clientComment);
            client = clientDao.save(client);
        }
        return new ClientData(client);
    }

    public ClientData getClient(String clientAddress) {
        Client client = clientDao.findByAddress(clientAddress);
        if (client == null) {
            return null;
        }
        return new ClientData(client);
    }

    public List<ClientData> getClients() {
        List<Client> clients = new ArrayList<>();
        clientDao.findAll().forEach(clients::add);
        return clients.stream().map(client -> new ClientData(client)).collect(toList());
    }
}
