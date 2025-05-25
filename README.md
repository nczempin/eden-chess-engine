# eden-chess-engine

https://www.chessprogramming.org/Eden

In 1989 I was studying Artificial Intelligence & Computer Science at Edinburgh university.

In one of the classes we had the assigment to write an article/essay about an AI related topic.

I had become fascinated by computer chess, and had heard about an unusual (as in: not the standard minimax, alpha-beta, etc.) approach called "Paradise", by D. Wilkins. I decided to write an essay based on it.

Years later (around 2004) I had finally decided to write my own chess engine. Since my objective was purely educational, I decided that I'd use the language with which I was the most familiar at the time, Java.

Eden is what came out of it.

The name was meant to be an hommage to the "Paradise" approach, but used the traditional alpha-beta approach.

I managed to lose all my source code at one point (GitHub didn't exist yet). Eventually I decompiled it and cleaned it up a little bit.

Hence this repository.

## Setup

To build the project you need a working Java Development Kit and Maven installation.
The code was last compiled with Java 6, so any modern JDK should work.

Run the following commands to compile or package the engine:

```bash
mvn compile      # only compile the sources
mvn package      # creates target/foo-0.0.1-SNAPSHOT.jar
```

You can start the engine from the command line with:

```bash
java -jar target/foo-0.0.1-SNAPSHOT.jar
```

## Current Functional Status

The engine speaks both the UCI and WinBoard/xboard protocols. It can parse
FEN positions, perform a basic alpha–beta search with quiescence and read a
simple opening book (see `bookBlack.txt` and `bookWhite.txt`).

Only a small portion of the code is covered by tests. Currently the test suite
contains a single JUnit test which verifies parsing of black long castling in
a FEN string.

There are known limitations: parts of the code still contain TODOs that throw
exceptions and many engine features have not been extensively tested.

## Development Status

This repository hosts a cleaned–up version of the old Eden chess engine.
The last significant commit is from May 2025 and the project is no longer
actively developed. The code builds and the engine runs, but expect rough
edges.
