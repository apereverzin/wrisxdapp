package com.wrisx.wrisxdapp.facilitator.service;

import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.data.request.FacilitatorRequest;
import com.wrisx.wrisxdapp.data.response.FacilitatorData;
import com.wrisx.wrisxdapp.domain.Facilitator;
import com.wrisx.wrisxdapp.domain.FacilitatorApproval;
import com.wrisx.wrisxdapp.domain.FacilitatorApprovalDao;
import com.wrisx.wrisxdapp.domain.FacilitatorDao;
import com.wrisx.wrisxdapp.domain.Research;
import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.domain.UserDao;
import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import com.wrisx.wrisxdapp.errorhandling.ResourceNotFoundException;
import com.wrisx.wrisxdapp.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.wrisx.wrisxdapp.domain.State.COMMITTED;
import static com.wrisx.wrisxdapp.domain.State.CONFIRMED;
import static com.wrisx.wrisxdapp.domain.State.CREATED;
import static com.wrisx.wrisxdapp.util.WrisxUtil.validateStringArgument;
import static com.wrisx.wrisxdapp.util.WrisxUtil.validateStringArguments;

@Service
public class FacilitatorService {
    private final Logger logger = LoggerFactory.getLogger(FacilitatorService.class);

    private final FacilitatorDao facilitatorDao;
    private final FacilitatorApprovalDao facilitatorApprovalDao;
    private final UserService userService;
    private final UserDao userDao;
    private final EntityProvider entityProvider;

    @Autowired
    public FacilitatorService(FacilitatorDao facilitatorDao,
                              FacilitatorApprovalDao facilitatorApprovalDao,
                              UserService userService,
                              UserDao userDao,
                              EntityProvider entityProvider) {
        this.facilitatorDao = facilitatorDao;
        this.facilitatorApprovalDao = facilitatorApprovalDao;
        this.userService = userService;
        this.userDao = userDao;
        this.entityProvider = entityProvider;
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
                        facilitatorRequest.getPassword(),
                        facilitatorRequest.getProfileLink(),
                        facilitatorRequest.getWebsiteLink());
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

    public FacilitatorData getFacilitator(String facilitatorAddress)
            throws ResourceNotFoundException {
        Facilitator facilitator =
                entityProvider.getFacilitatorByAddress(facilitatorAddress);

        return new FacilitatorData(facilitator);
    }

    public void deleteFacilitator(String facilitatorAddress) throws ResourceNotFoundException {
        Facilitator facilitator =
                entityProvider.getFacilitatorByAddress(facilitatorAddress);

        facilitatorDao.delete(facilitator);

        userService.deleteUserIfPossible(facilitatorAddress);
    }

    public void confirmFacilitatorCreation(String facilitatorAddress, String transactionHash)
            throws ResourceNotFoundException {
        Facilitator facilitator = entityProvider.getFacilitatorByAddress(facilitatorAddress);

        facilitator.setState(CONFIRMED);
        facilitator.setTransactionHash(transactionHash);

        User user = entityProvider.getUserByAddress(facilitatorAddress);
        if (user.getState() == CREATED) {
            user.setState(CONFIRMED);
            user.setTransactionHash(transactionHash);
            userDao.save(user);
        }

        facilitatorDao.save(facilitator);
    }

    public void commitFacilitatorCreation(String facilitatorAddress, String transactionHash)
            throws ResourceNotFoundException {
        Facilitator facilitator = entityProvider.getFacilitatorByAddressAndTransactionHash(
                facilitatorAddress, transactionHash);

        if (facilitator.getState() != CONFIRMED) {
            throw new BadRequestException(MessageFormat.format(
                    "Illegal state of facilitator {0}", facilitatorAddress));
        }
        facilitator.setState(COMMITTED);

        User user = entityProvider.getUserByAddress(facilitatorAddress);
        if (user.getState() == CONFIRMED) {
            user.setState(COMMITTED);
            user.setTransactionHash(transactionHash);
            userDao.save(user);
        }

        facilitatorDao.save(facilitator);
    }

    public List<Facilitator> getFacilitators() {
        List<Facilitator> facilitators = new ArrayList<>();
        facilitatorDao.findAll().forEach(facilitators::add);
        return facilitators;
    }

    @Transactional
    public FacilitatorApproval approveResearch(String facilitatorAddress, String uuid)
            throws ResourceNotFoundException {
        Facilitator facilitator = entityProvider.getFacilitatorByAddress(facilitatorAddress);
        Research research = entityProvider.getResearchByPdfUuid(uuid);

        FacilitatorApproval facilitatorApproval = new FacilitatorApproval("",
                new Date(), facilitator, research);

        FacilitatorApproval facilitatorApproval1 =
                facilitatorApprovalDao.save(facilitatorApproval);

        return facilitatorApproval;
    }
}
