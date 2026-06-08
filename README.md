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
| [QUESTIONS.md](QUESTIONS.md) | 28 practice questions + answers (easy → hard), plus interview theory. |
| [StreamsExamples.java](src/main/java/org/example/StreamsExamples.java) | Runnable Java code — see every example print its output. |

## How to study (suggested order)
1. Read **CHEATSHEET.md** top to bottom (15 min).
2. Run **StreamsExamples.java** and match each printed output to the code.
3. Try **QUESTIONS.md** — answer each yourself before opening "Show Answer".

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
