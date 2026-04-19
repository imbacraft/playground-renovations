# Playground renovations

Java library that keeps track of kids in playsites. Details in [REQUIREMENTS.md](REQUIREMENTS.md).

## Build & test

```
./gradlew build
```

Java 21 required.

## Assumptions

Up to 3 kids cannot be skipped by VIPs twice: I interpreted this as a rule that the front-most 3 non-VIPs in the waiting queue can each be skipped at most once. Matches the given example KKKKK + VV → VKKKVKK.