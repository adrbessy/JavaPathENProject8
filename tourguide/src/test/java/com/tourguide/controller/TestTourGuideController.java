package com.tourguide.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.tourguide.model.User;
import com.tourguide.model.gpsUtil.Location;
import com.tourguide.model.gpsUtil.VisitedLocation;
import com.tourguide.service.TourGuideService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

}
