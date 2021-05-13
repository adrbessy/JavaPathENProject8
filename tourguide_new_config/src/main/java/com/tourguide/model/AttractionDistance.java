package com.tourguide.model;

import gpsUtil.location.Attraction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttractionDistance {

  private Attraction attraction;

  private double distance;

  public AttractionDistance(Attraction attraction, double distance) {
    this.attraction = attraction;
    this.distance = distance;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[distance=" + distance + ", attraction=" + attraction + "]";
  }

}
