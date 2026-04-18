package com.swedbank.playground;

import com.swedbank.playground.model.Kid;
import com.swedbank.playground.model.PlaysiteType;
import com.swedbank.playground.model.Visit;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Playsite implements IPlaysite {

  private PlaysiteType playsiteType;

  private List<Kid> kidsPlaying = new LinkedList<>();
  private List<Kid> kidsWaiting = new LinkedList<>();
  private int maxKidsPlaying;
  private IVisitRepository visitRepository;

  public Playsite(IVisitRepository visitRepository) {
    this.visitRepository = visitRepository;
  }

  @Override
  public boolean addKid(Kid kid) {
    if (kidsPlaying.size() >= maxKidsPlaying) {
      kidsWaiting.add(kid);
      return false;
    }
    else {
      kidsPlaying.add(kid);
      visitRepository.save(Visit.builder()
          .playsite(this)
          .kid(kid)
          .time(new Date())
          .isStart(true)
          .build());
      return true;
    }
  }

  @Override
  public void removeKid(Kid kid) {
    if (kidsWaiting.remove(kid)) {
      return;
    }

    if (kidsPlaying.remove(kid)) {
      visitRepository.save(Visit.builder()
          .playsite(this)
          .kid(kid)
          .time(new Date())
          .isStart(false)
          .build());

      if (kidsWaiting.size() > 0) {
        Kid kid2 = kidsWaiting.remove(0);
        visitRepository.save(Visit.builder()
            .playsite(this)
            .kid(kid2)
            .time(new Date())
            .isStart(true)
            .build());
        kidsPlaying.add(kid2);
      }
    }
    else {
      throw new RuntimeException("No kid " + kid + " in playsite: " + this);
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

  public void setPlaysiteType(PlaysiteType playsiteType) {
    this.playsiteType = playsiteType;
  }

  public int getMaxKidsPlaying() {
    return maxKidsPlaying;
  }

  public void setMaxKidsPlaying(int maxKidsPlaying) {
    this.maxKidsPlaying = maxKidsPlaying;
  }
}
