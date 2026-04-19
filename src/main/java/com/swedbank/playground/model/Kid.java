package com.swedbank.playground.model;

import lombok.Builder;

@Builder
public record Kid(String name, int age, boolean vip) {}
