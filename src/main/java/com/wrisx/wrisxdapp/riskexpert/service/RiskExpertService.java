package com.wrisx.wrisxdapp.riskexpert.service;

import com.wrisx.wrisxdapp.data.RiskExpertData;
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

    @Autowired
    public RiskExpertService(RiskExpertDao riskExpertDao) {
        this.riskExpertDao = riskExpertDao;
    }

    public RiskExpertData saveRiskExpert(String address,
                                         String name) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(address);
        if (riskExpert == null) {
            riskExpert = new RiskExpert(address, name);
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
