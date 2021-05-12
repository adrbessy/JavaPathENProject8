package com.tourguide.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
@Entity
@Table(name = "user", schema = "public")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String userName;

  private String phoneNumber;

  private String emailAddress;

  private Date latestLocationTimestamp;

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
