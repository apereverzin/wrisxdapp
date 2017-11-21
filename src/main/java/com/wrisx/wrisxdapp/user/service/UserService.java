package com.wrisx.wrisxdapp.user.service;

import com.wrisx.wrisxdapp.data.UserData;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.ClientDao;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final ClientDao clientDao;
    private final ExpertDao expertDao;

    @Autowired
    public UserService(ClientDao clientDao, ExpertDao expertDao) {
        this.clientDao = clientDao;
        this.expertDao = expertDao;
    }

    public UserData getUser(String userAddress) {
        Client client = clientDao.findByAddress(userAddress);
        if (client == null) {
            return getRiskExpert(userAddress);
        }
        return new UserData(client);
    }

    private UserData getRiskExpert(String userAddress) {
        Expert expert = expertDao.findByAddress(userAddress);
        if (expert == null) {
            return null;
        }
        return new UserData(expert);
    }
}
