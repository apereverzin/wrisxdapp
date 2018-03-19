package com.wrisx.wrisxdapp.facilitator.service;

import com.wrisx.wrisxdapp.data.request.FacilitatorRequest;
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

    public Facilitator createFacilitator(FacilitatorRequest facilitatorRequest) {
        validateStringArgument(facilitatorRequest.getAddress(),
                "Address cannot be null or empty");
        validateStringArguments(facilitatorRequest.getName(),
                facilitatorRequest.getEmailAddress(),
                facilitatorRequest.getKeywords(),
                facilitatorRequest.getDescription(),
                facilitatorRequest.getPassword());

        Facilitator facilitator =
                facilitatorDao.findByAddress(facilitatorRequest.getAddress());

        if (facilitator == null) {
            User user = userDao.findByAddress(facilitatorRequest.getAddress());
            if (user == null) {
                user = new User(facilitatorRequest.getAddress(),
                        facilitatorRequest.getName(),
                        facilitatorRequest.getEmailAddress(),
                        facilitatorRequest.getProfileLink(),
                        facilitatorRequest.getWebsiteLink(),
                        facilitatorRequest.getPassword());
                user = userDao.save(user);
            }

            facilitator = new Facilitator(facilitatorRequest.getAddress(),
                    facilitatorRequest.getKeywords(),
                    facilitatorRequest.getDescription(),
                    user);
            facilitator = facilitatorDao.save(facilitator);
            return facilitator;
        }

        String msg = MessageFormat.format(
                "Facilitator already exists {0}", facilitatorRequest.getAddress());
        logger.error(msg);
        throw new BadRequestException(msg);
    }

    public List<Facilitator> getFacilitators() {
        List<Facilitator> facilitators = new ArrayList<>();
        facilitatorDao.findAll().forEach(facilitators::add);
        return facilitators;
    }
}
