package com.swedbank.playground.model;

import com.swedbank.playground.Playsite;
import lombok.Builder;

import java.util.Date;

@Builder
public record Visit(Playsite playsite, Kid kid, Date time, boolean isStart) {}
