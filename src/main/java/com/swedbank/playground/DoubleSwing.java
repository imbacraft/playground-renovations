package com.swedbank.playground;

import com.swedbank.playground.model.PlaysiteType;

public class DoubleSwing extends Playsite {

  public DoubleSwing(IVisitRepository visitRepository) {
    super(PlaysiteType.DOUBLE_SWING, 2, visitRepository);
  }

  @Override
  public synchronized double utilization() {
    return kidsPlaying().size() < 2 ? 0.0 : 100.0;
  }
}
