package com.wrisx.wrisxdapp.facilitator.service;

import com.wrisx.wrisxdapp.domain.Facilitator;
import com.wrisx.wrisxdapp.domain.FacilitatorDao;
import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.domain.UserDao;
import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.wrisx.wrisxdapp.util.WrisxUtil.validateStringArgument;
import static com.wrisx.wrisxdapp.util.WrisxUtil.validateStringArguments;

@Service
public class FacilitatorService {
    private final Logger logger = LoggerFactory.getLogger(FacilitatorService.class);

    private final FacilitatorDao facilitatorDao;
    private final UserDao userDao;

    @Autowired
    public FacilitatorService(FacilitatorDao facilitatorDao,
                              UserDao userDao) {
        this.facilitatorDao = facilitatorDao;
        this.userDao = userDao;
    }

    public Facilitator createFacilitator(String facilitatorAddress, String name,
                                         String emailAddress, String description,
                                         String secret) {
        validateStringArgument(facilitatorAddress, "Address cannot be null or empty");
        validateStringArguments(name, emailAddress, description, secret);

        Facilitator facilitator = facilitatorDao.findByAddress(facilitatorAddress);

        if (facilitator == null) {
            User user = userDao.findByAddress(facilitatorAddress);
            if (user == null) {
                user = new User(facilitatorAddress, name, emailAddress, secret);
                user = userDao.save(user);
            }

            facilitator = new Facilitator(facilitatorAddress, description, user);
            facilitator = facilitatorDao.save(facilitator);
            return facilitator;
        }

        String msg = MessageFormat.format("Facilitator already exists {0}", facilitatorAddress);
        logger.error(msg);
        throw new BadRequestException(msg);
    }

    public List<Facilitator> getFacilitators() {
        List<Facilitator> facilitators = new ArrayList<>();
        facilitatorDao.findAll().forEach(facilitators::add);
        return facilitators;
    }
}
