# Java 8 Streams — Common Interview Questions

The questions that actually come up in interviews, with **short model answers** you
can say out loud. Theory first, then the coding problems they love to ask at a whiteboard.

> How to use this: cover the answer, say yours, then check. If you can explain it in
> 2–3 sentences, you're ready.

---

## Part 1 — Theory (the ones asked almost every time)

### Q1. What is a Stream in Java 8?
A pipeline for processing a sequence of elements. It does **not store data** — it
takes data from a source (List, array, range), runs operations like `filter`/`map`,
and produces a result. It never changes the original source.

### Q2. Stream vs Collection — what's the difference?
A **Collection** *stores* data in memory. A **Stream** *processes* data and holds
nothing. Collections are about storage; streams are about computation. Also, you
iterate a Collection yourself (external iteration); a Stream iterates internally for you.

### Q3. Intermediate vs Terminal operations?
- **Intermediate** returns another Stream and is **lazy** — e.g. `filter`, `map`,
  `sorted`, `distinct`. You can chain many.
- **Terminal** returns a result (value/collection) and **triggers execution** — e.g.
  `collect`, `count`, `forEach`, `reduce`. A pipeline has **exactly one** terminal op.

### Q4. What does "lazy evaluation" mean?
Intermediate operations don't run when you write them — they only run when a terminal
operation is called. Without a terminal operation, **nothing happens**. This lets Java
optimise (e.g. stop early with `findFirst`).

### Q5. Can you reuse a Stream?
**No.** A stream is single-use. After a terminal operation it's closed; using it again
throws `IllegalStateException`. Create a new stream from the source instead.

### Q6. filter vs map?
`filter` **keeps or drops** elements based on a true/false test — the count can shrink.
`map` **transforms** each element into something else — the count stays the same.

### Q7. map vs flatMap?
`map` turns one element into one element. `flatMap` turns one element into a **stream**
of elements and then **flattens** everything into a single stream. Use `flatMap` to
unpack nested collections (a list of lists into one list).

### Q8. What is a Collector / what does collect() do?
`collect` is a terminal operation that gathers stream elements into a result container.
A **Collector** is the recipe for how to build it — `Collectors.toList()`,
`toSet()`, `toMap()`, `joining()`, `groupingBy()`, etc.

### Q9. What is Optional and why use it?
A container that may or may not hold a value. It's returned by operations that might
find nothing (`findFirst`, `max`). It forces you to handle the "empty" case and helps
**avoid `NullPointerException`**. Read it with `orElse`, `ifPresent`, `map`.

### Q10. What are reduce()'s three forms?
- `reduce(BinaryOperator)` → returns an `Optional`.
- `reduce(identity, BinaryOperator)` → returns a value (identity is the start, e.g. `0`).
- `reduce(identity, accumulator, combiner)` → for parallel/combining different types.
It folds all elements into a single result (sum, product, concatenation).

### Q11. What is a short-circuiting operation?
One that doesn't need to process every element. `findFirst`, `findAny`, `anyMatch`,
`allMatch`, `noneMatch`, and `limit` can stop as soon as the answer is known.

### Q12. What's the difference between findFirst() and findAny()?
`findFirst` returns the first element **in order**. `findAny` returns **any** matching
element — which is faster in **parallel** streams because it doesn't enforce order.

### Q13. What is a parallel stream and when should you use it?
It splits data across multiple CPU cores to process simultaneously, then joins results.
Use it **only** for large datasets with heavy, CPU-bound, stateless, associative work
and no blocking I/O — and only after measuring. For small data it's usually slower.

### Q14. What makes an operation safe for a parallel reduce?
It must be **stateless**, **non-interfering** (not modifying the source), and
**associative** — meaning grouping doesn't change the result, like `+` or `*`.
Subtraction is not associative, so it's unsafe.

### Q15. What is the difference between Collection.stream() and Collection.parallelStream()?
`stream()` is sequential (one thread). `parallelStream()` is the same pipeline run in
parallel across the common ForkJoinPool.

### Q16. peek() vs forEach()?
`peek` is an **intermediate** op meant for debugging — it looks at elements as they
pass and returns a stream. `forEach` is a **terminal** op that consumes the stream to
perform a final action.

### Q17. What functional interfaces back the lambdas you pass to streams?
- `Predicate<T>` (returns boolean) → `filter`, `anyMatch`
- `Function<T,R>` (transforms) → `map`
- `Consumer<T>` (does something, returns nothing) → `forEach`, `peek`
- `Supplier<T>` (produces a value) → `Stream.generate`
- `BinaryOperator<T>` (combines two) → `reduce`

### Q18. Why use IntStream/LongStream/DoubleStream instead of Stream<Integer>?
They avoid **boxing/unboxing** (converting between `int` and `Integer`), which is faster
and less memory, and they add handy methods like `sum()`, `average()`, `range()`.

---

## Part 2 — Classic coding questions (whiteboard favourites)

### C1. Find the second highest number in a list.
```java
Optional<Integer> second = nums.stream()
        .distinct()
        .sorted(Comparator.reverseOrder())
        .skip(1)
        .findFirst();
```

### C2. Count the frequency of each character in a string.
```java
Map<Character, Long> freq = str.chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
```

### C3. Find the first non-repeated character in a string.
```java
Optional<Character> first = str.chars().mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() == 1)
        .map(Map.Entry::getKey)
        .findFirst();
// LinkedHashMap keeps insertion order so "first" is meaningful
```

### C4. Find duplicate elements in a list.
```java
Set<Integer> duplicates = nums.stream()
        .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
```

### C5. Sum all even numbers in a list.
```java
int sum = nums.stream().filter(n -> n % 2 == 0).mapToInt(Integer::intValue).sum();
```

### C6. Group a list of strings by their length.
```java
Map<Integer, List<String>> byLen = words.stream()
        .collect(Collectors.groupingBy(String::length));
```

### C7. Convert a List to a Map (e.g. id → object).
```java
Map<Integer, Employee> byId = employees.stream()
        .collect(Collectors.toMap(Employee::id, e -> e));
// add a merge function if keys can collide:
// Collectors.toMap(Employee::id, e -> e, (a, b) -> a)
```

### C8. Sort a Map by its values (descending).
```java
Map<String, Integer> sorted = map.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue,
                (a, b) -> a, LinkedHashMap::new));
```

### C9. Remove duplicates from a list (keep order).
```java
List<Integer> unique = nums.stream().distinct().toList();
```

### C10. Check if any/all/no element matches a condition.
```java
boolean anyEven  = nums.stream().anyMatch(n -> n % 2 == 0);
boolean allPos   = nums.stream().allMatch(n -> n > 0);
boolean noneNeg  = nums.stream().noneMatch(n -> n < 0);
```

### C11. Concatenate a list of strings into one CSV line.
```java
String csv = words.stream().collect(Collectors.joining(", ", "[", "]"));
// "[a, b, c]"  — middle separator, prefix, suffix
```

### C12. Find the max/min object by a field.
```java
Optional<Employee> highestPaid = employees.stream()
        .max(Comparator.comparingDouble(Employee::salary));
```

### C13. Flatten a list of lists.
```java
List<Integer> flat = listOfLists.stream()
        .flatMap(List::stream)
        .toList();
```

### C14. Partition numbers into even and odd.
```java
Map<Boolean, List<Integer>> parts = nums.stream()
        .collect(Collectors.partitioningBy(n -> n % 2 == 0));
```

### C15. Count words by first letter.
```java
Map<Character, Long> byFirst = words.stream()
        .collect(Collectors.groupingBy(w -> w.charAt(0), Collectors.counting()));
```

---

## Part 3 — "Spot the bug" questions

### B1. Why does this print nothing?
```java
nums.stream().filter(n -> n > 2).map(n -> n * 2);   // no output
```
**Answer:** No terminal operation. Intermediate ops are lazy — add `.forEach(...)` or
`.toList()` to actually run the pipeline.

### B2. Why does this throw IllegalStateException?
```java
Stream<Integer> s = nums.stream();
s.forEach(System.out::println);
long c = s.count();   // boom
```
**Answer:** A stream is **single-use**. After the first terminal op (`forEach`) the
stream is closed. Create a new one for the second operation.

### B3. Why is this parallel code unreliable?
```java
List<Integer> result = new ArrayList<>();
nums.parallelStream().forEach(result::add);
```
**Answer:** `ArrayList` isn't thread-safe — concurrent `add` causes a **race condition**.
Use `.collect(Collectors.toList())` instead and avoid side effects.

### B4. What's wrong with using subtraction in a parallel reduce?
```java
int r = nums.parallelStream().reduce(0, (a, b) -> a - b);
```
**Answer:** Subtraction isn't **associative**, so different parallel groupings give
different results — the answer becomes unpredictable. Only use associative operations.

---

## Two-line summary to leave them with

> "A Stream is a lazy, single-use pipeline that processes data without storing it:
> a source, some intermediate operations (`filter`/`map`), and one terminal operation
> (`collect`/`reduce`) that triggers it all — never mutating the original source."
