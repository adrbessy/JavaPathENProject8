package com.tourguide.model;

import lombok.Data;

@Data
public class NearbyAttractions {

  private String attractionName;

  private double attractionLatitude;

  private double attractionLongitude;

  private double userLatitude;

  private double userLongitude;

  private double distanceBetweenUserAndAttraction;

  private int rewardPoints;

  public NearbyAttractions(String attractionName, double attractionLatitude, double attractionLongitude,
      double userLatitude,
      double userLongitude, double distanceBetweenUserAndAttraction, int rewardPoints) {
    this.attractionName = attractionName;
    this.attractionLatitude = attractionLatitude;
    this.attractionLongitude = attractionLongitude;
    this.userLatitude = userLatitude;
    this.userLongitude = userLongitude;
    this.distanceBetweenUserAndAttraction = distanceBetweenUserAndAttraction;
    this.rewardPoints = rewardPoints;
  }

}
