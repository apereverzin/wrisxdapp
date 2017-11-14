package com.wrisx.wrisxdapp.riskexpert.service;

import com.wrisx.wrisxdapp.data.RiskExpertData;
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
public class RiskExpertService {
    private final RiskExpertDao riskExpertDao;
    private final ClientDao clientDao;

    @Autowired
    public RiskExpertService(RiskExpertDao riskExpertDao, ClientDao clientDao) {
        this.riskExpertDao = riskExpertDao;
        this.clientDao = clientDao;
    }

    public RiskExpertData saveRiskExpert(String expertAddress, String name,
                                         String emailAddress, String comment) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(expertAddress);
        if (riskExpert == null) {
            String riskExpertName;
            String riskExpertEmailAddress;
            String riskExpertComment;

            Client client = clientDao.findByAddress(expertAddress);
            if (client != null) {
                riskExpertName = client.getName();
                riskExpertEmailAddress = client.getEmailAddress();
                riskExpertComment = client.getComment();
            } else {
                riskExpertName = name;
                riskExpertEmailAddress = emailAddress;
                riskExpertComment = comment;
            }

            riskExpert = new RiskExpert(expertAddress, riskExpertName,
                    riskExpertEmailAddress, riskExpertComment);
            riskExpert = riskExpertDao.save(riskExpert);
        }
        return new RiskExpertData(riskExpert);
    }

    public RiskExpertData getRiskExpert(String riskExpertAddress) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(riskExpertAddress);
        if (riskExpert == null) {
            return null;
        }
        return new RiskExpertData(riskExpert);
    }

    public List<RiskExpertData> getRiskExperts() {
        List<RiskExpert> riskExperts = new ArrayList<>();
        riskExpertDao.findAll().forEach(riskExperts::add);
        return riskExperts.stream().map(expert -> new RiskExpertData(expert)).collect(toList());
    }
}
