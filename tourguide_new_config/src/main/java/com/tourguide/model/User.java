package com.tourguide.model;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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

  public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
    this.userId = userId;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.emailAddress = emailAddress;
  }

  /*
   * public void addToVisitedLocations(VisitedLocation visitedLocation) {
   * visitedLocations.add(visitedLocation); }
   */

  /*
   * public void clearVisitedLocations() { visitedLocations.clear(); }
   */

  /*
   * public void addUserReward(UserReward userReward) { if
   * (userRewards.stream().filter(r ->
   * !r.attraction.attractionName.equals(userReward.attraction)).count() == 0) {
   * userRewards.add(userReward); } }
   */

  /*
   * public VisitedLocation getLastVisitedLocation() { return
   * visitedLocations.get(visitedLocations.size() - 1); }
   */


}
