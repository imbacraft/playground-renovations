# Playground renovations requirements

An old playground has secured EU funds for renovations.
However, their management application cannot support the planned changes.
Your task is to update the component/library which keeps track of kids in playsites:

- EU funds aren't limitless, and the library is old.
Clean code & project will be of utmost importance for maintenance & future-proofing.
The project should be updated to at least JAVA 17 standards.

- When a playsite is full, kids can form a queue and wait for players to leave.
Kids with VIP tickets will be able to skip to the front of the queue.
However, to keep things fair, up to 3 kids cannot be skipped by VIPs twice,
i.e. KKKKK + VV -> VKKKVKK

- The application stores various data, such as time spent by a kid in a playsite,
for reporting purposes.
EU expects another data point to be tracked: playsite utilization.
It is measured in % of capacity used and is usually proportional to the amount of kids playing.
The application will be querying your library periodically for this data.

- A new type of playsite will be available: double swings.
EU has strict regulations regarding this playsite:
no more than 2 kids can play and is not considered in use unless 2 kids are playing.
Also, the EU has already informed that 5 other types are to be introduced in the near future.
Ensure that these new types can be implemented quickly if needed: zip line, rock wall, balance beam, music panel, rope tower.
Who knows what other restrictions EU may impose on existing or future playsite types...

- Stakeholders warn you on the scrutiny of the EU bureaucrats and expect that you will document your changes clearly and visibly.
No additional document is needed, but an occasional comment or multiple commits will be appreciated.