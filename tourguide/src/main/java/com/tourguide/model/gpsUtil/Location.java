package com.tourguide.model.gpsUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {

  public final double longitude;

  public final double latitude;

  public Location(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[latitude=" + latitude + ", longitude="
        + longitude + "]";
  }

}
