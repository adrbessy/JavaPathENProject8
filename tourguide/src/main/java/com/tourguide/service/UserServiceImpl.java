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
public class UserServiceImpl implements UserService {

  @Autowired
  private TourGuideServiceImpl tourGuideServiceImpl;

  private Logger logger = LoggerFactory.getLogger(UserService.class);

  /**
   * Retrieve all users.
   * 
   * @return the list of all users.
   */
  @Override
  public List<User> getAllUsers() {
    logger.debug("in the method getAllUsers in the class UserServiceImpl");
    return tourGuideServiceImpl.internalUserMap.values().stream().collect(Collectors.toList());
  }

  /**
   * Retrieve the user corresponding to a given username.
   * 
   * @param userName An username
   * @return the user.
   */
  @Override
  public User getUser(String userName) {
    logger.debug("in the method getUser in the class UserServiceImpl");
    if (!tourGuideServiceImpl.internalUserMap.containsKey(userName)) {
      logger.error("This username does not exist :" + userName);
      throw new NotFoundException(userName);
    }
    return tourGuideServiceImpl.internalUserMap.get(userName);
  }

}
