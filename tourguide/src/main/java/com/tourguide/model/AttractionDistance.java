package com.tourguide.model;


import com.tourguide.model.gpsUtil.Attraction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttractionDistance {

  private Attraction attraction;

  public double distance;

  public AttractionDistance(Attraction attraction, double distance) {
    this.attraction = attraction;
    this.distance = distance;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[distance=" + distance + ", attraction=" + attraction + "]";
  }

}
