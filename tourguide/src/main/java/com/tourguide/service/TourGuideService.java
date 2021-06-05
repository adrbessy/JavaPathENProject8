package com.tourguide.service;

import com.tourguide.model.NearbyAttractions;
import com.tourguide.model.User;
import com.tourguide.model.UserPreferences;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.model.tripPricer.Provider;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TourGuideService {

  VisitedLocation getUserLocation(User user);

  void addUser(User user);

  List<Provider> getTripDeals(User user);

  List<NearbyAttractions> getNearByAttractions(VisitedLocation visitedLocation, User user);

  void addShutDownHook();

  Map<String, Location> getLastSavedLocationForAllUsers();

  UserPreferences updateUserPreferences(String userName, UserPreferences userPreferences);

  void initializeInternalUsers();

  void generateUserLocationHistory(User user);

  Date getRandomTime();

}
