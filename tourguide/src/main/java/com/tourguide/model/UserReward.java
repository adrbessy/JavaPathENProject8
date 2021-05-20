package com.tourguide.model;

import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.VisitedLocation;

public class UserReward {

  public final VisitedLocation visitedLocation;
  public final Attraction attraction;
  private int rewardPoints;

  public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
    this.visitedLocation = visitedLocation;
    this.attraction = attraction;
    this.rewardPoints = rewardPoints;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[attraction=" + attraction + "]";
  }

  public UserReward(VisitedLocation visitedLocation, Attraction attraction) {
    this.visitedLocation = visitedLocation;
    this.attraction = attraction;
  }

  public void setRewardPoints(int rewardPoints) {
    this.rewardPoints = rewardPoints;
  }

  public int getRewardPoints() {
    return rewardPoints;
  }

}
