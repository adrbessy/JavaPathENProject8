package com.tourguide.model.tripPricer;

import java.util.UUID;

public class Provider {

  public final String name;

  public final double price;

  public final UUID tripId;

  public Provider(UUID tripId, String name, double price) {
    this.name = name;
    this.tripId = tripId;
    this.price = price;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[name=" + name + ", price="
        + price + "]";
  }

}
