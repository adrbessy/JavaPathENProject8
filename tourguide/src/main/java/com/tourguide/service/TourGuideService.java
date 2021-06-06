package com.tourguide.service;

import com.tourguide.model.NearbyAttractions;
import com.tourguide.model.User;
import com.tourguide.model.UserPreferences;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.model.tripPricer.Provider;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TourGuideService {

  /**
   * Retrieve the last visited location of the user.
   * 
   * @param user An user
   * @return the last visited location.
   */
  VisitedLocation getUserLocation(User user);

  /**
   * Add an user in the internalUserMap.
   * 
   * @param user An user
   */
  void addUser(User user);

  /**
   * Retrieve a list of Provider (an offer by provider) according to the
   * preferences of an user.
   * 
   * @param user An user
   * @return a list of Provider
   */
  List<Provider> getTripDeals(User user);

  /**
   * Retrieve a list of NearbyAttraction (attractionName, attractionLatitude,
   * attractionLongitude, userLatitude, userLongitude,
   * distanceBetweenUserAndAttraction, rewardPoints)
   * 
   * @param visitedLocation A visitedLocation
   * @param user            An user
   * @return a list of NearbyAttraction
   */
  List<NearbyAttractions> getNearByAttractions(VisitedLocation visitedLocation, User user);

  /**
   * Registers a new virtual machine shutdown hook. When the program shuts down,
   * the stopTracking() method is invoked.
   * 
   */
  void addShutDownHook();

  /**
   * Get a map (user id, last visited location) of the most recent location saved
   * in the history of every users
   *
   * @return the map
   */
  Map<String, Location> getLastSavedLocationForAllUsers();

  /**
   * Update user preferences from a username and a userPreferences object
   * 
   * @param userName        A user name
   * @param userPreferences the user preferences
   * @return the updated user preferences
   */
  UserPreferences updateUserPreferences(String userName, UserPreferences userPreferences);

  /**
   * Initialize users for tests
   * 
   */
  void initializeInternalUsers();

  /**
   * Generate a location history for the users (for the tests)
   * 
   * @param user An user
   */
  void generateUserLocationHistory(User user);

  Date getRandomTime();

}
