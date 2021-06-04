package com.tourguide.service;

import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttractionService {

  @Autowired
  RewardsService rewardsService;

  // proximity in miles
  private int defaultProximityBuffer = 10;
  private int proximityBuffer = defaultProximityBuffer;

  public void setProximityBuffer(int proximityBuffer) {
    this.proximityBuffer = proximityBuffer;
  }

  public void setDefaultProximityBuffer() {
    proximityBuffer = defaultProximityBuffer;
  }

  public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
    return rewardsService.getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
  }

}
