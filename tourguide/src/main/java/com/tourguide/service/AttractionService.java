package com.tourguide.service;

import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.VisitedLocation;

public interface AttractionService {

  /**
   * Check if the visited attraction is near the attraction.
   * 
   * @param visitedLocation The visitedLocation of an user
   * @param attraction      An attraction
   * @return true if the attraction is enough near from the visited location.
   */
  boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction);

  /**
   * Set the distance threshold to use nearAttraction method
   * 
   * @param proximityBuffer The distance threshold
   */
  void setProximityBuffer(int proximityBuffer);

}
