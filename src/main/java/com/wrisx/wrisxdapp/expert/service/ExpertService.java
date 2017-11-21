package com.wrisx.wrisxdapp.expert.service;

import com.wrisx.wrisxdapp.data.ExpertData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ExpertService {
    private final ExpertDao expertDao;
    private final ClientDao clientDao;

    @Autowired
    public ExpertService(ExpertDao expertDao, ClientDao clientDao) {
        this.expertDao = expertDao;
        this.clientDao = clientDao;
    }

    public ExpertData saveExpert(String expertAddress, String name,
                                 String emailAddress, String riskExpertKeywords,
                                 String description) {
        Expert expert = expertDao.findByAddress(expertAddress);
        if (expert == null) {
            String riskExpertName;
            String riskExpertEmailAddress;
            String riskExpertDescription;

            Client client = clientDao.findByAddress(expertAddress);
            if (client != null) {
                riskExpertName = client.getName();
                riskExpertEmailAddress = client.getEmailAddress();
            } else {
                riskExpertName = name;
                riskExpertEmailAddress = emailAddress;
            }
            riskExpertDescription = description;

            expert = new Expert(expertAddress, riskExpertName,
                    riskExpertEmailAddress, riskExpertKeywords, riskExpertDescription);
            expert = expertDao.save(expert);
        }
        return new ExpertData(expert);
    }

    public ExpertData getExpert(String riskExpertAddress) {
        Expert expert = expertDao.findByAddress(riskExpertAddress);
        if (expert == null) {
            return null;
        }
        return new ExpertData(expert);
    }

    public List<ExpertData> getExperts() {
        List<Expert> experts = new ArrayList<>();
        expertDao.findAll().forEach(experts::add);
        return experts.stream().map(expert -> new ExpertData(expert)).collect(toList());
    }
}
