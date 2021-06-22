package com.tourguide.service;

import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import java.util.List;

public interface RewardsService {

  /**
   * Calculate the rewards with the visitedLocations of the user
   * 
   * @param user An user
   */
  void calculateRewards(User user);

  /**
   * Check if a location is within an attraction proximity.
   * 
   * @param attraction An attraction
   * @param location   A location
   * @return true if the attraction is enough near from the location.
   */
  boolean isWithinAttractionProximity(Attraction attraction, Location location);

  /**
   * Calculate the distance between two locations.
   * 
   * @param loc1 A location
   * @param loc2 A location
   * @return the distance.
   */
  double getDistance(Location loc1, Location loc2);

  /**
   * Retrieve the list of rewards of an user.
   * 
   * @param user An user
   * @return the list of rewards.
   */
  List<UserReward> getUserRewards(User user);

}
