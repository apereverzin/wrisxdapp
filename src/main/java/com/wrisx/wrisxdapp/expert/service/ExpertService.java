package com.wrisx.wrisxdapp.expert.service;

import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.data.ExpertData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import com.wrisx.wrisxdapp.errorhandling.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.wrisx.wrisxdapp.domain.State.COMMITTED;
import static com.wrisx.wrisxdapp.domain.State.CONFIRMED;
import static com.wrisx.wrisxdapp.util.WrisxUtil.getKeywordList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class ExpertService {
    private final ExpertDao expertDao;
    private final ClientDao clientDao;
    private final EntityProvider entityProvider;

    @Autowired
    public ExpertService(ExpertDao expertDao, ClientDao clientDao,
                         EntityProvider entityProvider) {
        this.expertDao = expertDao;
        this.clientDao = clientDao;
        this.entityProvider = entityProvider;
    }

    public ExpertData saveExpert(String expertAddress, String name,
                                 String emailAddress, String expertKeywords,
                                 String description) {
        Expert expert = expertDao.findByAddress(expertAddress);

        if (expert == null) {
            String expertName;
            String expertEmailAddress;
            String expertDescription;

            Client client = clientDao.findByAddress(expertAddress);
            if (client != null) {
                expertName = client.getName();
                expertEmailAddress = client.getEmailAddress();
            } else {
                expertName = name;
                expertEmailAddress = emailAddress;
            }
            expertDescription = description;

            expert = new Expert(expertAddress, expertName,
                    expertEmailAddress, expertKeywords, expertDescription);
            expert = expertDao.save(expert);
        }

        return new ExpertData(expert);
    }

    public ExpertData getExpert(String expertAddress) throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(expertAddress);

        return new ExpertData(expert);
    }

    public void deleteExpert(String expertAddress) throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(expertAddress);

        expertDao.delete(expert);
    }

    public void confirmExpertCreation(String clientAddress, String transactionHash)
            throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(clientAddress);

        expert.setState(CONFIRMED);
        expert.setTransactionHash(transactionHash);

        expertDao.save(expert);
    }

    public void commitExpertCreation(String expertAddress, String transactionHash)
            throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddressAndTransactionHash(
                expertAddress, transactionHash);

        if (expert.getState() != CONFIRMED) {
            throw new BadRequestException(MessageFormat.format(
                    "Illegal state of expert {0}", expertAddress));
        }
        expert.setState(COMMITTED);

        expertDao.save(expert);
    }

    public List<ExpertData> findExperts(String keywords) {
        List<String> keywordList = getKeywordList(keywords);

        List<Expert> experts = new ArrayList<>();
        expertDao.findAll().forEach(experts::add);

        return (keywordList.isEmpty() ? experts.stream() :
                experts.stream().
                        filter(item -> {
                            List<String> itemKeywordList = getKeywordList(item.getKeywords());
                            return itemKeywordList.containsAll(keywordList);
                        })).
                sorted(comparing(Expert::getName)).
                map(item -> new ExpertData(item)).
                collect(toList());
    }
}
