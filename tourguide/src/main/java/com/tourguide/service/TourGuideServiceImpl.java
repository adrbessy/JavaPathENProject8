package com.tourguide.service;

import com.tourguide.model.AttractionDistance;
import com.tourguide.model.NearbyAttractions;
import com.tourguide.model.User;
import com.tourguide.model.UserPreferences;
import com.tourguide.model.gpsUtil.Attraction;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.model.tripPricer.Provider;
import com.tourguide.proxies.MicroserviceGpsUtilProxy;
import com.tourguide.proxies.MicroserviceRewardCentralProxy;
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
public class TourGuideServiceImpl implements TourGuideService {

  @Autowired
  MicroserviceGpsUtilProxy mGpsUtilProxy;
  @Autowired
  MicroserviceTripPricerProxy TripPricerProxy;
  @Autowired
  LocationService locationService;
  @Autowired
  MicroserviceRewardCentralProxy mRewardCentralProxy;
  @Autowired
  UserService userService;

  private Logger logger = LoggerFactory.getLogger(TourGuideServiceImpl.class);
  private final RewardsService rewardsService;
  public final Tracker tracker;
  boolean testMode = true;
  int numberOfNearestAttractions = 5;

  public TourGuideServiceImpl(MicroserviceGpsUtilProxy mGpsUtilProxy, RewardsService rewardsService) {
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

  /**
   * Retrieve the last visited location of the user.
   * 
   * @param user An user
   * @return the last visited location.
   */
  @Override
  public VisitedLocation getUserLocation(User user) {
    logger.debug("in the method getUserLocation in the class TourGuideServiceImpl");
    VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
        : locationService.trackUserLocation(user);
    return visitedLocation;
  }

  /**
   * Add an user in the internalUserMap.
   * 
   * @param user An user
   */
  @Override
  public void addUser(User user) {
    logger.debug("in the method addUser in the class TourGuideServiceImpl");
    if (!internalUserMap.containsKey(user.getUserName())) {
      internalUserMap.put(user.getUserName(), user);
    }
  }

  /**
   * Retrieve a list of Provider (an offer by provider) according to the
   * preferences of an user.
   * 
   * @param user An user
   * @return a list of Provider
   */
  @Override
  public List<Provider> getTripDeals(User user) {
    logger.debug("in the method getTripDeals in the class TourGuideServiceImpl");
    int cumulatativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();
    List<Provider> providers = TripPricerProxy.getPrice(tripPricerApiKey, user.getUserId(),
        user.getUserPreferences().getNumberOfAdults(),
        user.getUserPreferences().getNumberOfChildren(), user.getUserPreferences().getTripDuration(),
        cumulatativeRewardPoints);
    user.setTripDeals(providers);
    return providers;
  }

  /**
   * Retrieve a list of NearbyAttraction (attractionName, attractionLatitude,
   * attractionLongitude, userLatitude, userLongitude,
   * distanceBetweenUserAndAttraction, rewardPoints)
   * 
   * @param visitedLocation A visitedLocation
   * @param user            An user
   * @return a list of NearbyAttraction
   */
  @Override
  public List<NearbyAttractions> getNearByAttractions(VisitedLocation visitedLocation, User user) {
    logger.debug("in the method getNearByAttractions in the class TourGuideServiceImpl");
    List<Attraction> attractions = mGpsUtilProxy.getAttractions();
    List<AttractionDistance> attractionDistanceList = new ArrayList<>();
    List<NearbyAttractions> nearbyAttractionsList = new ArrayList<>();

    // make a list of AttractionDistance (attraction, distance between attraction
    // and visited location)
    for (Attraction attraction : attractions) {
      double distance = rewardsService.getDistance(attraction, visitedLocation.location);
      AttractionDistance locDist = new AttractionDistance(attraction, distance);
      attractionDistanceList.add(locDist);
    }

    // sort the attractioDistanceList according to the distance from the smaller to
    // the greater
    attractionDistanceList.sort(Comparator.comparing(AttractionDistance::getDistance));

    // if the number of attraction is inferior to the number of attractions we want
    // to keep, the number of attracitons we want to keep becomes the number of
    // attractions
    if (attractions.size() < numberOfNearestAttractions) {
      numberOfNearestAttractions = attractions.size();
    }

    // we keep the numberOfNearestAttractions nearest attractions
    List<AttractionDistance> nearestDistanceAttractionList = attractionDistanceList.subList(0,
        numberOfNearestAttractions);

    // we retrieve the attractions from the nearestDistanceAttractionList
    List<Attraction> sortedNearestDistanceAttractionList = nearestDistanceAttractionList.stream()
        .map(AttractionDistance::getAttraction)
        .collect(Collectors.toList());

    int i = 0;
    for (Attraction nearbyAttraction : sortedNearestDistanceAttractionList) {
      NearbyAttractions nearbyAttractionObject = new NearbyAttractions(nearbyAttraction.attractionName,
          nearbyAttraction.latitude,
          nearbyAttraction.longitude, visitedLocation.location.latitude,
          visitedLocation.location.longitude,
          nearestDistanceAttractionList.get(i).distance,
          mRewardCentralProxy.getAttractionRewardPoints(nearbyAttraction.attractionId, user.getUserId()));
      nearbyAttractionsList.add(nearbyAttractionObject);
      i++;
    }
    return nearbyAttractionsList;
  }

  /**
   * Registers a new virtual machine shutdown hook. When the program shuts down,
   * the stopTracking() method is invoked.
   * 
   */
  @Override
  public void addShutDownHook() {
    logger.debug("in the method addShutDownHook in the class TourGuideServiceImpl");
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        tracker.stopTracking();
      }
    });
  }


  /**
   * Get a map (user id, last visited location) of the most recent location saved
   * in the history of every users
   *
   * @return the map
   */
  @Override
  public Map<String, Location> getLastSavedLocationForAllUsers() {
    logger.debug("in the method getLastSavedLocationAllUsers in the class TourGuideServiceImpl");
    List<User> usersList = userService.getAllUsers();
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
  @Override
  public UserPreferences updateUserPreferences(String userName, UserPreferences userPreferences) {
    logger.debug("in the method updateUserPreferences in the class TourGuideServiceImpl");
    User userToUpdate = userService.getUser(userName);
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
  public final Map<String, User> internalUserMap = new HashMap<>();

  /**
   * Initialize users for tests
   * 
   */
  @Override
  public void initializeInternalUsers() {
    logger.debug("in the method initializeInternalUsers in the class TourGuideServiceImpl");
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

  /**
   * Generate a location history for the users (for the tests)
   * 
   * @param user An user
   */
  @Override
  public void generateUserLocationHistory(User user) {
    logger.debug("in the method generateUserLocationHistory in the class TourGuideServiceImpl");
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

  /**
   * Get a random time
   *
   * @return a Date
   */
  @Override
  public Date getRandomTime() {
    logger.debug("in the method getRandomTime in the class TourGuideServiceImpl");
    LocalDateTime localDateTime = LocalDateTime.now().minusDays(new Random().nextInt(30));
    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
  }

}
