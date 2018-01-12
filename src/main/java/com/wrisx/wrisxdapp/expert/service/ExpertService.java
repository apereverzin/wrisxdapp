package com.wrisx.wrisxdapp.expert.service;

import com.wrisx.wrisxdapp.common.EntityProvider;
import com.wrisx.wrisxdapp.data.request.ExpertRequest;
import com.wrisx.wrisxdapp.data.response.ExpertData;
import com.wrisx.wrisxdapp.domain.Expert;
import com.wrisx.wrisxdapp.domain.ExpertDao;
import com.wrisx.wrisxdapp.domain.User;
import com.wrisx.wrisxdapp.domain.UserDao;
import com.wrisx.wrisxdapp.errorhandling.BadRequestException;
import com.wrisx.wrisxdapp.errorhandling.ResourceNotFoundException;
import com.wrisx.wrisxdapp.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.wrisx.wrisxdapp.domain.State.COMMITTED;
import static com.wrisx.wrisxdapp.domain.State.CONFIRMED;
import static com.wrisx.wrisxdapp.domain.State.CREATED;
import static com.wrisx.wrisxdapp.util.WrisxUtil.getKeywordList;
import static com.wrisx.wrisxdapp.util.WrisxUtil.validateStringArgument;
import static com.wrisx.wrisxdapp.util.WrisxUtil.validateStringArguments;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class ExpertService {
    private final Logger logger = LoggerFactory.getLogger(ExpertService.class);

    private final ExpertDao expertDao;
    private final UserService userService;
    private final UserDao userDao;
    private final EntityProvider entityProvider;

    @Autowired
    public ExpertService(ExpertDao expertDao, UserService userService, UserDao userDao,
                         EntityProvider entityProvider) {
        this.expertDao = expertDao;
        this.userService = userService;
        this.userDao = userDao;
        this.entityProvider = entityProvider;
    }

    public ExpertData createExpert(ExpertRequest expertRequest) {
        validateStringArgument(expertRequest.getAddress(), "Address cannot be empty");
        validateStringArguments(expertRequest.getName(), expertRequest.getEmailAddress(),
                expertRequest.getKeyWords(), expertRequest.getDescription(),
                expertRequest.getSecret());

        Expert expert = expertDao.findByAddress(expertRequest.getAddress());

        if (expert == null) {
            User user = userDao.findByAddress(expertRequest.getAddress());
            if (user == null) {
                user = new User(expertRequest.getAddress(), expertRequest.getName(),
                        expertRequest.getEmailAddress(), expertRequest.getSecret());
                user = userDao.save(user);
            }

            expert = new Expert(expertRequest.getAddress(), expertRequest.getKeyWords(),
                    expertRequest.getDescription(), user);
            expert = expertDao.save(expert);

            return new ExpertData(expert);
        }

        String msg = MessageFormat.format("ExpertRequest already exists {0}", expertRequest.getAddress());
        logger.error(msg);
        throw new BadRequestException(msg);
    }

    public ExpertData getExpert(String expertAddress) throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(expertAddress);

        return new ExpertData(expert);
    }

    public void deleteExpert(String expertAddress) throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(expertAddress);

        expertDao.delete(expert);

        userService.deleteUserIfPossible(expertAddress);
    }

    public void confirmExpertCreation(String clientAddress, String transactionHash)
            throws ResourceNotFoundException {
        Expert expert = entityProvider.getExpertByAddress(clientAddress);

        expert.setState(CONFIRMED);
        expert.setTransactionHash(transactionHash);

        User user = entityProvider.getUserByAddress(clientAddress);
        if (user.getState() == CREATED) {
            user.setState(CONFIRMED);
            user.setTransactionHash(transactionHash);
            userDao.save(user);
        }

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

        User user = entityProvider.getUserByAddress(expertAddress);
        if (user.getState() == CONFIRMED) {
            user.setState(COMMITTED);
            user.setTransactionHash(transactionHash);
            userDao.save(user);
        }

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
