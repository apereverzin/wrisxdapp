package com.wrisx.wrisxdapp.user.service;

import com.wrisx.wrisxdapp.data.UserData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.RiskExpert;
import com.wrisx.wrisxdapp.domain.RiskExpertDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final ClientDao clientDao;
    private final RiskExpertDao riskExpertDao;

    @Autowired
    public UserService(ClientDao clientDao, RiskExpertDao riskExpertDao) {
        this.clientDao = clientDao;
        this.riskExpertDao = riskExpertDao;
    }

    public UserData getUser(String userAddress) {
        Client client = clientDao.findByAddress(userAddress);
        if (client == null) {
            return getRiskExpert(userAddress);
        }
        return new UserData(client);
    }

    private UserData getRiskExpert(String userAddress) {
        RiskExpert riskExpert = riskExpertDao.findByAddress(userAddress);
        if (riskExpert == null) {
            return null;
        }
        return new UserData(riskExpert);
    }
}
