package com.tourguide.controller;

import com.tourguide.model.User;
import com.tourguide.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private static final Logger logger = LogManager.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  /**
   * Read - Get all users
   * 
   * @return - An Iterable object of users full filled
   */
  @GetMapping("/users")
  public List<User> getUsers() {
    List<User> userList = new ArrayList<>();
    try {
      logger.info("Get request with the endpoint 'users'");
      userList = userService.getUsers();
      logger.info(
          "response following the GET on the endpoint 'users'.");
    } catch (Exception exception) {
      logger.error("Error in the UserRestController in the method getUsers :"
          + exception.getMessage());
    }
    return userList;
  }

  /**
   * Read - Get an user
   * 
   * @param emailAddress An email address
   * @return - A user account
   */
  @GetMapping("/user")
  public User getUser(@RequestParam String username) {
    User user = null;
    try {
      logger.info("Get request with the endpoint 'user'");
      user = userService.getUser(username);
      logger.info(
          "response following the GET on the endpoint 'user'.");
    } catch (Exception exception) {
      logger.error("Error in the UserController in the method getUser :" +
          exception.getMessage());
    }
    return user;
  }

}
