package com.tourguide.controller;

import com.tourguide.model.NearbyAttractions;
import com.tourguide.model.User;
import com.tourguide.model.UserPreferences;
import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.model.tripPricer.Provider;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import com.tourguide.service.UserService;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TourGuideController {

  private static final Logger logger = LogManager.getLogger(TourGuideController.class);

  @Autowired
  TourGuideService tourGuideService;

  @Autowired
  UserService userService;

  @Autowired
  RewardsService rewardsService;

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;

  /**
   * @return string
   */
  @GetMapping("/")
  public String index() {
    logger.info("Redirecting to greeting message");
    return "Greetings from TourGuide!";
  }

  /**
   * Read - Get all users
   * 
   * @return - An Iterable object of users full filled
   */
  @GetMapping("/users")
  public List<User> getUsers() {
    logger.info("Get request with the endpoint 'users'.");
    List<User> userList = userService.getAllUsers();
    logger.info(
        "response following the GET on the endpoint 'users'.");
    return userList;
  }

  /**
   * Get the current location of one user
   * 
   * @param userName A user name
   * @return - A Location object (latitude and longitude)
   */
  @GetMapping("/location")
  public Location getLocation(@RequestParam String userName) {
    logger.info("Get request with the endpoint 'location'.");
    VisitedLocation visitedLocation = tourGuideService.getUserLocation(userService.getUser(userName));
    logger.info(
        "response following the GET on the endpoint 'location'.");
    return visitedLocation.location;
  }

  /**
   * Get the current location of all users
   * 
   * @return - A List of Map <user id, Location>
   */
  @GetMapping("/currentLocationForAllUsers")
  public List<Map<String, Location>> getCurrentLocationForAllUsers() {
    logger.info("Get request with the endpoint 'currentLocationForAllUsers'.");
    List<Map<String, Location>> currentLocationAllUsersList = tourGuideService.getCurrentLocationForAllUsers();
    logger.info(
        "response following the GET on the endpoint 'currentLocationForAllUsers'.");
    return currentLocationAllUsersList;
  }


  /*
   * Get the closest five tourist attractions to the user - no matter how far away
   * they are.
   * 
   * @param userName A user name
   * 
   * @return - A List of NearbyAttractions objects (Name of Tourist attraction,
   * Tourist attractions lat/long, The user's location lat/long, The distance in
   * miles between the user's location and each of the attractions, The reward
   * points for visiting each Attraction.)
   */
  @GetMapping("/nearbyAttractions")
  public List<NearbyAttractions> getNearbyAttractions(@RequestParam String userName) {
    logger.info("Get request with the endpoint 'nearbyAttractions'.");
    VisitedLocation visitedLocation = tourGuideService.getUserLocation(userService.getUser(userName));
    List<NearbyAttractions> attractionList = tourGuideService.getNearByAttractions(visitedLocation,
        userService.getUser(userName));
    logger.info(
        "response following the GET on the endpoint 'nearbyAttractions'.");
    return attractionList;
  }

  /*
   * Get the rewards of a username
   * 
   * @param userName A user name
   * 
   * @return - A List of rewards
   */
  @GetMapping("/rewards")
  public List<UserReward> getRewards(@RequestParam String userName) {
    logger.info("Get request with the endpoint 'rewards'.");
    List<UserReward> rewards = rewardsService.getUserRewards(userService.getUser(userName));
    logger.info(
        "response following the GET on the endpoint 'nearbyAttractions'.");
    return rewards;
  }

  /**
   * Get the last location (from their stored location history, not their current
   * location using gpsUtil) of all users.
   * 
   * @return - A Map similar to { "019b04a9-067a-4c76-8817-ee75088c3822":
   *         {"longitude":-48.188821,"latitude":74.84371} ...}
   */
  @GetMapping("/lastSavedLocationForAllUsers")
  public Map<String, Location> getLastSavedLocationForAllUsers() {
    logger.info("Get request with the endpoint 'allLastLocations'.");
    Map<String, Location> allCurrentLocations = tourGuideService.getLastSavedLocationAllUsers();
    logger.info(
        "response following the GET on the endpoint 'allLastLocations'.");
    return allCurrentLocations;
  }

  /*
   * Get the trip deals
   * 
   * @param userName A user name
   * 
   * @return - A List of trip deals
   */
  @GetMapping("/tripDeals")
  public List<Provider> getTripDeals(@RequestParam String userName) {
    logger.info("Get request with the endpoint 'tripDeals'.");
    List<Provider> providers = tourGuideService.getTripDeals(userService.getUser(userName));
    logger.info(
        "response following the GET on the endpoint 'tripDeals'.");
    return providers;
  }

  /**
   * Update an user preferences
   * 
   * @param userName        A user name
   * @param userPreferences A userPreferences object with modifications
   * @return The updated userPreferences object
   */
  @PutMapping("/userPreferences/{userName}")
  public UserPreferences updateUserPreferences(@PathVariable("userName") final String userName,
      @RequestBody UserPreferences userPreferences) {
    logger.info("Put request with the endpoint 'userPreferences/{userName}'.");
    UserPreferences userPreferencesToUpdate = tourGuideService.updateUserPreferences(userName, userPreferences);
    logger.info(
        "response following the PUT on the endpoint 'userPreferences/{userName}'.");
    return userPreferencesToUpdate;
  }

}