package com.swedbank.playground;

import com.swedbank.playground.model.Kid;
import com.swedbank.playground.model.PlaysiteType;
import com.swedbank.playground.model.Visit;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class Playsite implements IPlaysite {

  private final PlaysiteType playsiteType;
  private final int maxKidsPlaying;
  private final IVisitRepository visitRepository;
  private final List<Kid> kidsPlaying = new ArrayList<>();
  private final List<Kid> kidsWaiting = new ArrayList<>();
  private final Set<Kid> vipSkipped = new HashSet<>();

  @Override
  public boolean addKid(Kid kid) {
    if (kidsPlaying.size() >= maxKidsPlaying) {
      if (kid.vip()) {
        insertVip(kid);
      } else {
        kidsWaiting.add(kid);
      }
      return false;
    }
    kidsPlaying.add(kid);
    saveVisit(kid, true);
    return true;
  }

  @Override
  public void removeKid(Kid kid) {
    if (kidsWaiting.remove(kid)) {
      vipSkipped.remove(kid);
      return;
    }

    if (!kidsPlaying.remove(kid)) {
      throw new IllegalArgumentException("No kid " + kid + " in playsite: " + this);
    }

    saveVisit(kid, false);

    if (!kidsWaiting.isEmpty()) {
      Kid next = kidsWaiting.remove(0);
      vipSkipped.remove(next);
      kidsPlaying.add(next);
      saveVisit(next, true);
    }
  }

  // front-most 3 non-VIPs can each be skipped at most once.
  // Example: KKKKK + VV -> VKKKVKK
  private void insertVip(Kid vip) {
    int pos = 0;
    for (int i = 0; i < kidsWaiting.size(); i++) {
      if (kidsWaiting.get(i).vip()) {
        pos = i + 1;
      }
    }
    int nonVipCount = 0;
    for (int i = 0; i < kidsWaiting.size(); i++) {
      Kid k = kidsWaiting.get(i);
      if (k.vip()) continue;
      if (nonVipCount < 3 && vipSkipped.contains(k)) {
        pos = Math.max(pos, i + 1);
      }
      nonVipCount++;
    }
    for (int i = pos; i < kidsWaiting.size(); i++) {
      Kid k = kidsWaiting.get(i);
      if (!k.vip()) vipSkipped.add(k);
    }
    kidsWaiting.add(pos, vip);
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
