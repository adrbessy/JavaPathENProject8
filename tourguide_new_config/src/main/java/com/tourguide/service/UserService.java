package com.tourguide.service;

import com.tourguide.model.User;
import java.util.List;

public interface UserService {

  /**
   * Get all users
   * 
   * @return all users
   */
  List<User> getUsers();

  /**
   * Get an User from a username
   * 
   * @param username The username of the user in the User table
   * @return The user
   */
  User getUser(String username);

}
