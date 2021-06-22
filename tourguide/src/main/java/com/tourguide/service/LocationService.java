package com.tourguide.service;

import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.VisitedLocation;

public interface LocationService {

  /**
   * Localize the current location of an user, add it to the visitedLocationList,
   * then calculate rewards.
   * 
   * @param user An user
   * @return the new location
   */
  VisitedLocation trackUserLocation(User user);

}
