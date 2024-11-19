package de.thu.thutorium;

import static org.assertj.core.api.Assertions.assertThat;

import de.thu.thutorium.controller.SampleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ThutoriumApplicationTests {

  @Autowired private SampleController sampleController;

  @Test
  void contextLoads() throws Exception {
    assertThat(sampleController).isNotNull();
  }
}
