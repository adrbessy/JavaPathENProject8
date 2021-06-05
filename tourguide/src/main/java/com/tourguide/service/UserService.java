package com.tourguide.service;

import com.tourguide.exception.NotFoundException;
import com.tourguide.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private TourGuideServiceImpl tourGuideServiceImpl;

  private Logger logger = LoggerFactory.getLogger(UserService.class);

  public List<User> getAllUsers() {
    return tourGuideServiceImpl.internalUserMap.values().stream().collect(Collectors.toList());
  }

  public User getUser(String userName) {
    if (!tourGuideServiceImpl.internalUserMap.containsKey(userName)) {
      logger.error("This username does not exist :" + userName);
      throw new NotFoundException(userName);
    }
    return tourGuideServiceImpl.internalUserMap.get(userName);
  }

}
