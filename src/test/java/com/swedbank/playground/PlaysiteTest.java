package com.swedbank.playground;

import com.swedbank.playground.model.Kid;
import com.swedbank.playground.model.PlaysiteType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlaysiteTest {

  @Mock
  private IVisitRepository visitRepository;

  private Kid kid(String name) {
    return Kid.builder().name(name).age(5).vip(false).build();
  }

  private Kid vipKid(String name) {
    return Kid.builder().name(name).age(5).vip(true).build();
  }

  @Test
  void kidPlaysWhenPlaysiteHasRoom() {
    Playsite playsite = new Playsite(PlaysiteType.SLIDE, 1, visitRepository);
    Kid john = kid("John");

    boolean playing = playsite.addKid(john);

    assertTrue(playing);
    assertEquals(List.of(john), playsite.kidsPlaying());
    verify(visitRepository).save(argThat(v -> v.kid().equals(john) && v.isStart()));
  }

  @Test
  void endVisitSavedWhenKidRemoved() {
    Playsite playsite = new Playsite(PlaysiteType.SLIDE, 1, visitRepository);
    Kid john = kid("John");
    playsite.addKid(john);

    playsite.removeKid(john);

    verify(visitRepository).save(argThat(v -> v.kid().equals(john) && !v.isStart()));
  }

  @Test
  void kidWaitsWhenPlaysiteFull() {
    Playsite playsite = new Playsite(PlaysiteType.SLIDE, 1, visitRepository);
    playsite.addKid(kid("First"));
    Kid second = kid("Second");

    boolean playing = playsite.addKid(second);

    assertFalse(playing);
    assertEquals(List.of(second), playsite.kidsWaiting());
  }

  @Test
  void waitingKidPromotedWhenSlotOpens() {
    Playsite playsite = new Playsite(PlaysiteType.SLIDE, 1, visitRepository);
    Kid first = kid("First");
    Kid second = kid("Second");
    playsite.addKid(first);
    playsite.addKid(second);

    playsite.removeKid(first);

    assertEquals(List.of(second), playsite.kidsPlaying());
    assertTrue(playsite.kidsWaiting().isEmpty());
  }

  @Test
  void removingUnknownKidThrows() {
    Playsite playsite = new Playsite(PlaysiteType.SLIDE, 1, visitRepository);
    Kid unknownKid = kid("Unknown");

    assertThrows(IllegalArgumentException.class, () -> playsite.removeKid(unknownKid));
  }

  @Test
  void twoVipsAtFullQueueFairSkip() {
    // KKKKK + VV -> VKKKVKK
    Playsite playsite = new Playsite(PlaysiteType.SLIDE, 1, visitRepository);
    playsite.addKid(kid("Playing"));

    Kid k1 = kid("K1"), k2 = kid("K2"), k3 = kid("K3"), k4 = kid("K4"), k5 = kid("K5");
    playsite.addKid(k1);
    playsite.addKid(k2);
    playsite.addKid(k3);
    playsite.addKid(k4);
    playsite.addKid(k5);

    Kid v1 = vipKid("V1"), v2 = vipKid("V2");
    playsite.addKid(v1);
    playsite.addKid(v2);

    assertEquals(List.of(v1, k1, k2, k3, v2, k4, k5), playsite.kidsWaiting(),
        "KKKKK + VV should produce VKKKVKK");
  }

  @Test
  void regularUtilization() {
    Playsite playsite = new Playsite(PlaysiteType.CAROUSEL, 4, visitRepository);
    assertEquals(0.0, playsite.utilization());

    playsite.addKid(kid("A"));
    playsite.addKid(kid("B"));
    assertEquals(50.0, playsite.utilization());

    playsite.addKid(kid("C"));
    playsite.addKid(kid("D"));
    assertEquals(100.0, playsite.utilization());
  }

}
