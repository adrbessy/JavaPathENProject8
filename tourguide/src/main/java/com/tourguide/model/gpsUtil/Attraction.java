package com.tourguide.model.gpsUtil;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attraction extends Location {

  public final String attractionName;

  public final String city;

  public final String state;

  public final UUID attractionId;

  public double latitude;

  public double longitude;

  public Attraction(String attractionName, String city, String state, double latitude, double longitude) {
    super(latitude, longitude);
    this.attractionName = attractionName;
    this.city = city;
    this.state = state;
    this.attractionId = UUID.randomUUID();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[latitude=" + latitude + ", longitude="
        + longitude + "]";
  }
}