package com.wrisx.wrisxdapp.common;

import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import com.wrisx.wrisxdapp.domain.Research;
import com.wrisx.wrisxdapp.domain.ResearchDao;
import com.wrisx.wrisxdapp.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class EntityProvider {
    private final ExpertDao expertDao;
    private final ClientDao clientDao;
    private final ResearchDao researchDao;

    public EntityProvider(ExpertDao expertDao, ClientDao clientDao,
                          ResearchDao researchDao) {
        this.expertDao = expertDao;
        this.clientDao = clientDao;
        this.researchDao = researchDao;
    }

    public Expert getExpertByAddress(String expertAddress) throws NotFoundException {
        Expert expert = expertDao.findByAddress(expertAddress);

        if (expert == null) {
            throw new NotFoundException(
                    MessageFormat.format("Expert not found {0}", expertAddress));
        }
        return expert;
    }

    public Client getClientByAddress(String clientAddress) throws NotFoundException {
        Client client = clientDao.findByAddress(clientAddress);

        if (client == null) {
            throw new NotFoundException(
                    MessageFormat.format("Client not found {0}", clientAddress));
        }
        return client;
    }

    public Research getResearchByUuid(String uuid) throws NotFoundException {
        Research research = researchDao.findByUuid(uuid);

        if (research == null) {
            throw new NotFoundException(
                    MessageFormat.format("Research not found {0}", uuid));
        }
        return research;
    }
}
