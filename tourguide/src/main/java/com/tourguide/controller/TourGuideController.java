package com.tourguide.controller;

import com.jsoniter.output.JsonStream;
import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.service.TourGuideService;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tripPricer.Provider;

@RestController
public class TourGuideController {

  private static final Logger logger = LogManager.getLogger(TourGuideController.class);

  @Autowired
  TourGuideService tourGuideService;

  @RequestMapping("/")
  public String index() {
    return "Greetings from TourGuide!";
  }

  /**
   * Read - Get all users
   * 
   * @return - An Iterable object of users full filled
   */
  @GetMapping("/users")
  public List<User> getUsers() {
    List<User> userList = new ArrayList<>();
    try {
      logger.info("Get request with the endpoint 'users'");
      userList = tourGuideService.getAllUsers();
      logger.info(
          "response following the GET on the endpoint 'users'.");
    } catch (Exception exception) {
      logger.error("Error in the TourGuideController in the method getUsers :"
          + exception.getMessage());
    }
    return userList;
  }

  /**
   * Read - Get the current location of one user
   * 
   * @return - A Location object (latitude and longitude)
   */
  @GetMapping("/location")
  public Location getLocation(@RequestParam String userName) {
    VisitedLocation visitedLocation = tourGuideService.getUserLocation(tourGuideService.getUser(userName));
    return visitedLocation.location;
  }

  /*
   * Get the closest five tourist attractions to the user - no matter how far away
   * they are.
   */

  // Return a new JSON object that contains:
  // Name of Tourist attraction,
  // Tourist attractions lat/long,
  // The user's location lat/long,
  // The distance in miles between the user's location and each of the
  // attractions.
  // The reward points for visiting each Attraction.
  // Note: Attraction reward points can be gathered from RewardsCentral
  @GetMapping("/nearbyAttractions")
  public List<Attraction> getNearbyAttractions(@RequestParam String userName) {
    VisitedLocation visitedLocation = tourGuideService.getUserLocation(tourGuideService.getUser(userName));
    return tourGuideService.getNearByAttractions(visitedLocation);
  }

  @RequestMapping("/getRewards")
  public String getRewards(@RequestParam String userName) {
    return JsonStream.serialize(tourGuideService.getUserRewards(tourGuideService.getUser(userName)));
  }

  @RequestMapping("/getAllCurrentLocations")
  public String getAllCurrentLocations() {
    // TODO: Get a list of every user's most recent location as JSON
    // - Note: does not use gpsUtil to query for their current location,
    // but rather gathers the user's current location from their stored location
    // history.
    //
    // Return object should be the just a JSON mapping of userId to Locations
    // similar to:
    // {
    // "019b04a9-067a-4c76-8817-ee75088c3822":
    // {"longitude":-48.188821,"latitude":74.84371}
    // ...
    // }

    return JsonStream.serialize("");
  }

  @RequestMapping("/getTripDeals")
  public String getTripDeals(@RequestParam String userName) {
    List<Provider> providers = tourGuideService.getTripDeals(tourGuideService.getUser(userName));
    return JsonStream.serialize(providers);
  }

}