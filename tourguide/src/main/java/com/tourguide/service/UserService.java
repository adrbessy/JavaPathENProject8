package com.tourguide.service;

import com.tourguide.model.User;
import java.util.List;

public interface UserService {

  /**
   * Retrieve all users.
   * 
   * @return the list of all users.
   */
  List<User> getAllUsers();

  /**
   * Retrieve the user corresponding to a given username.
   * 
   * @param userName An username
   * @return the user.
   */
  User getUser(String userName);

}
