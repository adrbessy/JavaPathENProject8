package com.tourguide.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

@SpringBootTest
public class TestUserPreferences {

  @Test
  public void simpleEqualsUserPreferences() {
    EqualsVerifier.forClass(UserPreferences.class).suppress(Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
  }

}
