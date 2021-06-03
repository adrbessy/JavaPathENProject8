package com.tourguide.service;

import com.tourguide.exception.NotFoundException;
import com.tourguide.model.AttractionDistance;
import com.tourguide.model.NearbyAttractions;
import com.tourguide.model.User;
import com.tourguide.model.UserPreferences;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.model.tripPricer.Provider;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.proxies.MicroserviceTripPricerProxy;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.money.Monetary;
import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourGuideService {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;
  @Autowired
  MicroserviceTripPricerProxy TripPricerProxy;
  @Autowired
  LocationService locationService;

  private Logger logger = LoggerFactory.getLogger(TourGuideService.class);
  private final RewardsService rewardsService;
  public final Tracker tracker;
  boolean testMode = true;
  int numberOfAttractionsNearest = 5;

  public TourGuideService(MicroserviceGpsUtilProxy mGpsUtilProxy, RewardsService rewardsService) {
    this.rewardsService = rewardsService;

    if (testMode) {
      logger.info("TestMode enabled");
      logger.debug("Initializing users");
      initializeInternalUsers();
      logger.debug("Finished initializing users");
    }
    tracker = new Tracker(this);
    addShutDownHook();
  }

  public VisitedLocation getUserLocation(User user) {
    VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
        : locationService.trackUserLocation(user);
    return visitedLocation;
  }


  public List<Map<String, Location>> getCurrentLocationAllUsers() {
    Map<String, Location> usersLocations = new HashMap<>();
    List<Map<String, Location>> AllUserLocation = new ArrayList<>();
    for (User user : getAllUsers()) {
      VisitedLocation visitedLocation = locationService.trackUserLocation(user);
      usersLocations.put(user.getUserId().toString(), visitedLocation.location);
      AllUserLocation.add(usersLocations);
    }
    return AllUserLocation;
  }


  public User getUser(String userName) {
    if (!internalUserMap.containsKey(userName)) {
      logger.error("This username does not exist" + userName);
      throw new NotFoundException(userName);
    }
    return internalUserMap.get(userName);
  }

  public List<User> getAllUsers() {
    return internalUserMap.values().stream().collect(Collectors.toList());
  }

  public void addUser(User user) {
    if (!internalUserMap.containsKey(user.getUserName())) {
      internalUserMap.put(user.getUserName(), user);
    }
  }

  public List<Provider> getTripDeals(User user) {
    int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
    List<Provider> providers = TripPricerProxy.getPrice(tripPricerApiKey, user.getUserId(),
        user.getUserPreferences().getNumberOfAdults(),
        user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(),
        cumulatativeRewardPoints);
    user.setTripDeals(providers);
    return providers;
  }



  public List<NearbyAttractions> getNearByAttractions(VisitedLocation visitedLocation, User user) {

    List<Attraction> attractions = mGpsUtilProxy.getAttractions();
    List<AttractionDistance> attractionDistanceList = new ArrayList<>();
    List<NearbyAttractions> nearbyAttractionsList = new ArrayList<>();

    for (Attraction attraction : attractions) {
      double distance = rewardsService.getDistance(attraction, visitedLocation.location);
      AttractionDistance locDist = new AttractionDistance(attraction, distance);
      attractionDistanceList.add(locDist);
    }

    attractionDistanceList.sort(Comparator.comparing(AttractionDistance::getDistance));

    List<AttractionDistance> attractionDistanceListFirst5 = attractionDistanceList.subList(0,
        numberOfAttractionsNearest);

    List<Attraction> nearbyAttractions = attractionDistanceListFirst5.stream()
        .map(AttractionDistance::getAttraction)
        .collect(Collectors.toList());

    int i = 0;
    for (Attraction nearbyAttraction : nearbyAttractions) {
      NearbyAttractions nearbyAttractionObject = new NearbyAttractions(nearbyAttraction.attractionName,
          nearbyAttraction.latitude,
          nearbyAttraction.longitude, visitedLocation.location.latitude,
          visitedLocation.location.longitude,
          attractionDistanceListFirst5.get(i).distance,
          rewardsService.getRewardPoints(nearbyAttraction, user));
      nearbyAttractionsList.add(nearbyAttractionObject);
      i++;
    }

    return nearbyAttractionsList;
  }

  private void addShutDownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        tracker.stopTracking();
      }
    });
  }


  /**
   * Get a list of every users most recent location saved in the history
   *
   * @return list of users recent locations
   */
  public Map<String, Location> getLastSavedLocationAllUsers() {

    List<User> usersList = getAllUsers();
    Map<String, Location> usersLocations = new HashMap<>();

    usersList.forEach(user -> {

      if (user.getVisitedLocations().size() > 0) {
        usersLocations.put(user.getUserId().toString(), user.getLastVisitedLocation().location);
      }
    });

    return usersLocations;
  }

  /**
   * Update user preferences from a username and a userPreferences object
   * 
   * @param userName        A user name
   * @param userPreferences the user preferences
   * @return the updated user preferences
   */
  public UserPreferences updateUserPreferences(String userName, UserPreferences userPreferences) {
    User userToUpdate = getUser(userName);
    if (userPreferences.getAttractionProximity() != Integer.MAX_VALUE) {
      userToUpdate.getUserPreferences().setAttractionProximity(userPreferences.getAttractionProximity());
    }
    if (userPreferences.getCurrency() != Monetary.getCurrency("USD")) {
      userToUpdate.getUserPreferences().setCurrency(userPreferences.getCurrency());
    }
    if (userPreferences.getLowerPricePoint() != Money.of(0, Monetary.getCurrency("USD"))) {
      userToUpdate.getUserPreferences().setLowerPricePoint(userPreferences.getLowerPricePoint());
    }
    if (userPreferences.getHighPricePoint() != Money.of(Integer.MAX_VALUE, Monetary.getCurrency("USD"))) {
      userToUpdate.getUserPreferences().setHighPricePoint(userPreferences.getHighPricePoint());
    }
    if (userPreferences.getTripDuration() != 1) {
      userToUpdate.getUserPreferences().setTripDuration(userPreferences.getTripDuration());
    }
    if (userPreferences.getTicketQuantity() != 1) {
      userToUpdate.getUserPreferences().setTicketQuantity(userPreferences.getTicketQuantity());
    }
    if (userPreferences.getNumberOfAdults() != 1) {
      userToUpdate.getUserPreferences().setNumberOfAdults(userPreferences.getNumberOfAdults());
    }
    if (userPreferences.getNumberOfChildren() != 0) {
      userToUpdate.getUserPreferences().setNumberOfChildren(userPreferences.getNumberOfChildren());
    }
    return userToUpdate.getUserPreferences();
  }


  /**********************************************************************************
   * 
   * Methods Below: For Internal Testing
   * 
   **********************************************************************************/
  private static final String tripPricerApiKey = "test-server-api-key";
  private static final Random RANDOM = new Random();
  // Database connection will be used for external users, but for testing purposes
  // internal users are provided and stored in memory
  private final Map<String, User> internalUserMap = new HashMap<>();

  private void initializeInternalUsers() {
    IntStream.range(0, InternalTestHelper.getInternalUserNumber()).forEach(i -> {
      String userName = "internalUser" + i;
      String phone = "000";
      String email = userName + "@tourGuide.com";
      User user = new User(UUID.randomUUID(), userName, phone, email);
      generateUserLocationHistory(user);

      internalUserMap.put(userName, user);
    });
    logger.debug("Created " + InternalTestHelper.getInternalUserNumber() + " internal test users.");
  }

  private void generateUserLocationHistory(User user) {
    double leftLimitLong = -180;
    double rightLimitLong = 180;
    double leftLimitLat = -85.05112878;
    double rightLimitLat = 85.05112878;
    IntStream.range(0, 3).forEach(i -> {
      user.addToVisitedLocations(new VisitedLocation(user.getUserId(),
          new Location(
              leftLimitLat + RANDOM.nextDouble() * (rightLimitLat - leftLimitLat),
              leftLimitLong + RANDOM.nextDouble() * (rightLimitLong - leftLimitLong)),
          getRandomTime()));
    });
  }

  private Date getRandomTime() {
    LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
  }

}
