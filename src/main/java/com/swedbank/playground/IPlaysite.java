package com.swedbank.playground;

import java.util.List;

import com.swedbank.playground.model.Kid;
import com.swedbank.playground.model.PlaysiteType;

public interface IPlaysite {

  PlaysiteType getPlaysiteType();

  boolean addKid(Kid kid);

  void removeKid(Kid kid);

  List<Kid> kidsPlaying();

  List<Kid> kidsWaiting();

}
