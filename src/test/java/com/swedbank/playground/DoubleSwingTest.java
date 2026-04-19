package com.swedbank.playground;

import com.swedbank.playground.model.Kid;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DoubleSwingTest {

  @Mock
  private IVisitRepository visitRepository;

  private Kid kid(String name) {
    return Kid.builder().name(name).age(5).vip(false).build();
  }

  @Test
  void utilizationIsZeroUnlessTwoKidsPlaying() {
    DoubleSwing doubleSwing = new DoubleSwing(visitRepository);

    assertEquals(0.0, doubleSwing.utilization(), "empty");

    doubleSwing.addKid(kid("A"));
    assertEquals(0.0, doubleSwing.utilization(), "1 kid. Not in use");

    doubleSwing.addKid(kid("B"));
    assertEquals(100.0, doubleSwing.utilization(), "2 kids. In use");
  }
}
