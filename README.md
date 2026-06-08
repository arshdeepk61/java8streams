# Java 8 Streams — Learn from Zero

This repo teaches **Java 8 Streams** in very simple language. No prior knowledge needed.

## What's a Stream? (in one line)
A stream is a **conveyor belt** for data: put a list on it, attach machines that
**filter** and **change** the items, and at the end **collect** the result.
Your original list is never changed.

## Files in this repo

| File | What it is |
|------|------------|
| [CHEATSHEET.md](CHEATSHEET.md) | The full cheat sheet — every operation explained simply with examples. **Start here.** |
| [DIAGRAMS.md](DIAGRAMS.md) | Visual diagrams — the conveyor-belt picture, filter vs map, groupingBy, parallel, and more. Great if you learn by seeing. |
| [GLOSSARY.md](GLOSSARY.md) | Plain-English definitions of every stream term (stream, lazy, collector, predicate, parallel…). Look up any confusing word. |
| [QUESTIONS.md](QUESTIONS.md) | 40 practice questions + answers (easy → hard, includes parallel streams), plus interview theory. |
| [PROBLEMS.md](PROBLEMS.md) | 15 **real-world** coding problems (employees, orders, logs, transactions) with full solutions. |
| [INTERVIEW.md](INTERVIEW.md) | The most commonly asked **interview questions** — theory, whiteboard coding, and "spot the bug" — with short model answers. |
| [StreamsExamples.java](src/main/java/org/example/StreamsExamples.java) | Runnable Java code — see every example print its output. |
| [RealWorldProblems.java](src/main/java/org/example/RealWorldProblems.java) | Runnable solutions for every problem in PROBLEMS.md. |

## How to study (suggested order)
1. Skim **DIAGRAMS.md** for the visual mental model (5 min).
2. Read **CHEATSHEET.md** top to bottom (15 min).
3. Run **StreamsExamples.java** and match each printed output to the code.
4. Try **QUESTIONS.md** — answer each yourself before opening "Show Answer".
5. Solve **PROBLEMS.md** — real-world scenarios with full solutions.

## How to run the examples
Requires Java 17+ (the code uses `record` and `.toList()`).

```bash
# compile
javac -d out src/main/java/org/example/StreamsExamples.java
# run
java -cp out org.example.StreamsExamples
```

Or just open the project in IntelliJ IDEA and run `StreamsExamples.main()`.

## The 3-part mental model (remember this!)
```
SOURCE  ──►  MIDDLE STEPS  ──►  FINAL STEP
list.stream()  filter, map, sorted   collect, count, forEach
```
- **Middle steps are lazy** — they do nothing until the final step runs.
- **A stream is single-use** — once finished, you can't reuse it.
