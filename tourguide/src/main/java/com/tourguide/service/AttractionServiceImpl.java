package com.tourguide.service;

import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.VisitedLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttractionServiceImpl implements AttractionService {

  private static final Logger logger = LogManager.getLogger(AttractionServiceImpl.class);

  @Autowired
  RewardsService rewardsService;

  // proximity in miles
  private int defaultProximityBuffer = 10;
  private int proximityBuffer = defaultProximityBuffer;

  /**
   * Set the distance threshold to use nearAttraction method
   * 
   * @param proximityBuffer The distance threshold
   */
  @Override
  public void setProximityBuffer(int proximityBuffer) {
    this.proximityBuffer = proximityBuffer;
  }

  public void setDefaultProximityBuffer() {
    proximityBuffer = defaultProximityBuffer;
  }

  /**
   * Check if the visited attraction is near the attraction.
   * 
   * @param visitedLocation The visitedLocation of an user
   * @param attraction      An attraction
   * @return true if the attraction is enough near from the visited location.
   */
  @Override
  public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
    logger.debug("in the method nearAttraction in the class AttractionServiceImpl");
    return rewardsService.getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
  }

}
