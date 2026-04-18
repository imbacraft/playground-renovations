package com.swedbank.playground;

import com.swedbank.playground.model.Kid;
import com.swedbank.playground.model.PlaysiteType;
import com.swedbank.playground.model.Visit;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class Playsite implements IPlaysite {

  private final PlaysiteType playsiteType;
  private final int maxKidsPlaying;
  private final IVisitRepository visitRepository;
  private final List<Kid> kidsPlaying = new ArrayList<>();
  private final List<Kid> kidsWaiting = new ArrayList<>();

  @Override
  public boolean addKid(Kid kid) {
    if (kidsPlaying.size() >= maxKidsPlaying) {
      kidsWaiting.add(kid);
      return false;
    }
    kidsPlaying.add(kid);
    saveVisit(kid, true);
    return true;
  }

  @Override
  public void removeKid(Kid kid) {
    if (kidsWaiting.remove(kid)) {
      return;
    }

    if (!kidsPlaying.remove(kid)) {
      throw new IllegalArgumentException("No kid " + kid + " in playsite: " + this);
    }

    saveVisit(kid, false);

    if (!kidsWaiting.isEmpty()) {
      Kid next = kidsWaiting.remove(0);
      kidsPlaying.add(next);
      saveVisit(next, true);
    }
  }

  @Override
  public List<Kid> kidsPlaying() {
    return List.copyOf(kidsPlaying);
  }

  @Override
  public List<Kid> kidsWaiting() {
    return List.copyOf(kidsWaiting);
  }

  @Override
  public PlaysiteType getPlaysiteType() {
    return playsiteType;
  }

  @Override
  public double utilization() {
    return maxKidsPlaying == 0 ? 0.0 : 100.0 * kidsPlaying.size() / maxKidsPlaying;
  }

  private void saveVisit(Kid kid, boolean isStart) {
    visitRepository.save(Visit.builder()
        .playsite(this)
        .kid(kid)
        .time(new Date())
        .isStart(isStart)
        .build());
  }
}
