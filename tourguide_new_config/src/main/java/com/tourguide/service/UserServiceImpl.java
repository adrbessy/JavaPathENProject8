package com.tourguide.service;

import com.tourguide.model.User;
import com.tourguide.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

  @Autowired
  private UserRepository userRepository;

  /**
   * Get all users
   * 
   * @return all users
   */
  @Override
  public List<User> getUsers() {
    logger.debug("in the method getUsers in the class UserServiceImpl");
    List<User> userList = new ArrayList<>();
    try {
      userList = userRepository.findAll();
    } catch (Exception exception) {
      logger.error("Error in the method getUsers :" + exception.getMessage());
    }
    return userList;
  }

  /**
   * Get an User from an id
   * 
   * @param id The id of the user in the User table
   * @return The user
   */
  @Override
  public User getUser(Integer id) {
    logger.debug("in the method getUser in the class UserServiceImpl");
    User user = null;
    try {
      user = userRepository.findById(id);
    } catch (Exception exception) {
      logger.error("Error in the method getUser :" + exception.getMessage());
    }
    return user;
  }

}
