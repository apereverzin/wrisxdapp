package com.wrisx.wrisxdapp.facilitator.service;

import com.wrisx.wrisxdapp.domain.Facilitator;
import com.wrisx.wrisxdapp.domain.FacilitatorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FacilitatorService {
    private final FacilitatorDao facilitatorDao;

    @Autowired
    public FacilitatorService(FacilitatorDao facilitatorDao) {
        this.facilitatorDao = facilitatorDao;
    }

    public Facilitator saveFacilitator(String address, String name,
                                       String emailAddress, String comment) {
        Facilitator facilitator = facilitatorDao.findByAddress(address);
        if (facilitator == null) {
            facilitator = new Facilitator(address, name, emailAddress, comment);
            facilitator = facilitatorDao.save(facilitator);
        }
        return facilitator;
    }

    public List<Facilitator> getFacilitators() {
        List<Facilitator> facilitators = new ArrayList<>();
        facilitatorDao.findAll().forEach(facilitators::add);
        return facilitators;
    }
}
