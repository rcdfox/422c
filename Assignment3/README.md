# MiniSpotify - Assignment #3

This project is the completed Assignment #3 implementation based on the supplied MiniSpotify 0.8 starter project.

## Build and test

The project targets Java 17 and uses only JUnit 5 as an external dependency.

```bash
mvn test
```

The command-line demo can be run with:

```bash
mvn -q compile exec:java
```

## Implementation summary

The inherited implementation was refactored in place while preserving the frozen `MusicService` API and grading entry point.

Key changes include:

- expected O(1) song lookup by ID using an insertion-ordered map;
- indexed, case-insensitive exact artist and genre search;
- case-insensitive title substring search in catalog insertion order;
- map-based user and playlist lookup;
- ordered playlist contents with duplicate songs and first-occurrence removal;
- set-based collaborator, follow, and favorite membership;
- immutable public `Playlist` descriptors and defensive collection returns;
- bounded 50-play recent history plus separate permanent ever-played tracking;
- global play-count ranking with the required tie breakers;
- recommendation filtering and ranking;
- consistent `IllegalArgumentException` handling for invalid operations.

`Design.pdf` at the project root documents the collection choices, invariants, and expected operation complexities.

## Submission contents

The submitted ZIP should contain this complete project, including source files, JUnit tests, test resources, and `Design.pdf`. Generated build output such as `target/` is intentionally excluded.
