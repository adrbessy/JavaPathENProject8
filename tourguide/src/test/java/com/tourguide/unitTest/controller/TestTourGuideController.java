package com.tourguide.unitTest.controller;

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
import com.tourguide.service.UserService;
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

  @MockBean
  private UserService userServiceMock;

  @Test
  public void testGetLocation() throws Exception {
    String username = "jon";
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    Location location = new Location(0.1, 0.2);
    VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), location,
        new Date());

    when(userServiceMock.getUser(username)).thenReturn(user);
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

    when(userServiceMock.getUser(username)).thenReturn(user);
    when(tourGuideServiceMock.getNearByAttractions(visitedLocation, user)).thenReturn(attractionList);
    mockMvc.perform(get("/nearbyAttractions?userName=jon")).andExpect(status().isOk());
  }

  @Test
  public void testGetRewards() throws Exception {
    String username = "jon";
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    List<UserReward> rewards = null;

    when(userServiceMock.getUser(username)).thenReturn(user);
    when(rewardServiceMock.getUserRewards(user)).thenReturn(rewards);
    mockMvc.perform(get("/rewards?userName=jon")).andExpect(status().isOk());
  }

  @Test
  public void testGetLastSavedLocationForAllUsers() throws Exception {
    Map<String, Location> allCurrentLocations = null;
    when(tourGuideServiceMock.getLastSavedLocationForAllUsers()).thenReturn(allCurrentLocations);
    mockMvc.perform(get("/lastSavedLocationForAllUsers")).andExpect(status().isOk());
  }

  @Test
  public void testGetTripDeals() throws Exception {
    String username = "jon";
    User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
    List<Provider> providers = null;

    when(userServiceMock.getUser(username)).thenReturn(user);
    when(tourGuideServiceMock.getTripDeals(user)).thenReturn(providers);
    mockMvc.perform(get("/tripDeals?userName=jon")).andExpect(status().isOk());
  }

  /*
   * @Test public void testUpdateUserPreferences() throws Exception { User user =
   * new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
   * user.getUserPreferences().setNumberOfChildren(3);
   * 
   * when(tourGuideServiceMock.updateUserPreferences(user.getUserName(),
   * user.getUserPreferences())) .thenReturn(user.getUserPreferences());
   * 
   * MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
   * .put("/userPreferences/" + user.getUserPreferences())
   * .contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.
   * APPLICATION_JSON).characterEncoding("UTF-8") .content(new
   * ObjectMapper().writeValueAsString(user.getUserPreferences()));
   * this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk()
   * ); }
   */

}
