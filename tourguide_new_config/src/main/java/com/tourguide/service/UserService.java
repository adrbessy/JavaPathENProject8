package com.tourguide.service;

import com.tourguide.model.User;
import java.util.List;

public interface UserService {

  /**
   * Get an User from an id
   * 
   * @param id The id of the user in the User table
   * @return The user
   */
  User getUser(Integer id);

  /**
   * Get all users
   * 
   * @return all users
   */
  List<User> getUsers();

}
