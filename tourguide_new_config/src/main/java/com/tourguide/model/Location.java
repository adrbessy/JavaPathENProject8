package com.tourguide.model;

import javax.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Entity
public class Location {

  public double longitude;

  public double latitude;

  public Integer VisitedLocationId;

}
