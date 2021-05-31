package com.tourguide.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.tourguide.model.NearbyAttractions;
import com.tourguide.model.User;
import com.tourguide.model.UserReward;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.model.tripPricer.Provider;
import com.tourguide.service.RewardsService;
import com.tourguide.service.TourGuideService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TestTourGuideController {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TourGuideService tourGuideServiceMock;

  @MockBean
  private RewardsService rewardServiceMock;

  @Test
  public void testGetUsers() throws Exception {
    List<User> userList = new ArrayList<>();
    when(tourGuideServiceMock.getAllUsers()).thenReturn(userList);
    mockMvc.perform(get("/users")).andExpect(status().isOk());
  }

  @Test
  public void testGetLocation() throws Exception {
    String username = "jon";
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location,
        new Date());

    when(tourGuideServiceMock.getUser(username)).thenReturn(user);
    when(tourGuideServiceMock.getUserLocation(user)).thenReturn(visitedLocation);
    mockMvc.perform(get("/location?userName=jon")).andExpect(status().isOk());
  }

  @Test
  public void testGetNearbyAttractions() throws Exception {
    String username = "jon";
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location,
        new Date());
    List<NearbyAttractions> attractionList = null;

    when(tourGuideServiceMock.getUser(username)).thenReturn(user);
    when(tourGuideServiceMock.getNearByAttractions(visitedLocation, user)).thenReturn(attractionList);
    mockMvc.perform(get("/nearbyAttractions?userName=jon")).andExpect(status().isOk());
  }

  @Test
  public void testGetRewards() throws Exception {
    String username = "jon";
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    List<UserReward> rewards = null;

    when(tourGuideServiceMock.getUser(username)).thenReturn(user);
    when(rewardServiceMock.getUserRewards(user)).thenReturn(rewards);
    mockMvc.perform(get("/rewards?userName=jon")).andExpect(status().isOk());
  }

  @Test
  public void testGetAllCurrentLocations() throws Exception {
    Map<String, Location> allCurrentLocations = null;
    when(tourGuideServiceMock.getLastSavedLocationAllUsers()).thenReturn(allCurrentLocations);
    mockMvc.perform(get("/allCurrentLocations")).andExpect(status().isOk());
  }

  @Test
  public void testGetTripDeals() throws Exception {
    String username = "jon";
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    List<Provider> providers = null;

    when(tourGuideServiceMock.getUser(username)).thenReturn(user);
    when(tourGuideServiceMock.getTripDeals(user)).thenReturn(providers);
    mockMvc.perform(get("/tripDeals?userName=jon")).andExpect(status().isOk());
  }

}
