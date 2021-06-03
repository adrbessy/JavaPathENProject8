package com.tourguide.unitTest.model;

import com.tourguide.model.UserPreferences;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import nl.jqno.equalsverifier.EqualsVerifier;

@SpringBootTest
public class TestUserPreferences {

  @Test
  public void simpleEqualsUserPreferences() {
    EqualsVerifier.simple().forClass(UserPreferences.class).verify();
  }

}
