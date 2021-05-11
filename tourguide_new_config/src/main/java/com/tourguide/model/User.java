package com.tourguide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import gpsUtil.location.VisitedLocation;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import tripPricer.Provider;

@EqualsAndHashCode
@Getter
@Setter
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private final UUID userId;

  private final String userName;
  private String phoneNumber;
  private String emailAddress;
  private Date latestLocationTimestamp;

  @Column
  @ElementCollection(targetClass = VisitedLocation.class)
  private List<VisitedLocation> visitedLocations = new ArrayList<>();

  @Column
  @ElementCollection(targetClass = UserReward.class)
  private List<UserReward> userRewards = new ArrayList<>();

  @Column
  @ElementCollection(targetClass = UserPreferences.class)
  private UserPreferences userPreferences = new UserPreferences();

  @Column
  @ElementCollection(targetClass = Provider.class)
  private List<Provider> tripDeals = new ArrayList<>();

  public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
    this.userId = userId;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.emailAddress = emailAddress;
  }

  public void addToVisitedLocations(VisitedLocation visitedLocation) {
    visitedLocations.add(visitedLocation);
  }

  public List<VisitedLocation> getVisitedLocations() {
    return visitedLocations;
  }

  public void clearVisitedLocations() {
    visitedLocations.clear();
  }

  public void addUserReward(UserReward userReward) {
    if (userRewards.stream().filter(r -> !r.attraction.attractionName.equals(userReward.attraction)).count() == 0) {
      userRewards.add(userReward);
    }
  }

  public List<UserReward> getUserRewards() {
    return userRewards;
  }

  public UserPreferences getUserPreferences() {
    return userPreferences;
  }

  public void setUserPreferences(UserPreferences userPreferences) {
    this.userPreferences = userPreferences;
  }

  public VisitedLocation getLastVisitedLocation() {
    return visitedLocations.get(visitedLocations.size() - 1);
  }

  public void setTripDeals(List<Provider> tripDeals) {
    this.tripDeals = tripDeals;
  }

  public List<Provider> getTripDeals() {
    return tripDeals;
  }

}
