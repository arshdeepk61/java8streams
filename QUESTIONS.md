# Java 8 Streams — Practice Questions

Try each question **yourself first**, then click "Show Answer" to check.
Difficulty: 🟢 Easy → 🟡 Medium → 🔴 Hard.

Use this sample data for many questions:

```java
List<Integer> nums = List.of(5, 2, 9, 1, 7, 4, 8, 3, 6, 2);
List<String> names = List.of("Amy", "Bob", "Anna", "Carl", "Alex", "Bea");

record Person(String name, String city, int age) {}
List<Person> people = List.of(
    new Person("Amy",  "London", 30),
    new Person("Bob",  "Paris",  25),
    new Person("Cat",  "London", 35),
    new Person("Dan",  "Paris",  40),
    new Person("Eve",  "Berlin", 28)
);
```

---

## Part A — Basics 🟢

### Q1. Print every name in the `names` list.
<details><summary>Show Answer</summary>

```java
names.stream().forEach(System.out::println);
```
</details>

### Q2. Keep only the even numbers from `nums`.
<details><summary>Show Answer</summary>

```java
List<Integer> evens = nums.stream()
        .filter(n -> n % 2 == 0)
        .toList();   // [2, 4, 8, 6, 2]
```
</details>

### Q3. Turn every name into UPPERCASE.
<details><summary>Show Answer</summary>

```java
List<String> upper = names.stream()
        .map(String::toUpperCase)
        .toList();
```
</details>

### Q4. How many names start with the letter "A"?
<details><summary>Show Answer</summary>

```java
long count = names.stream()
        .filter(n -> n.startsWith("A"))
        .count();   // 3  (Amy, Anna, Alex)
```
</details>

### Q5. Remove duplicate numbers from `nums`.
<details><summary>Show Answer</summary>

```java
List<Integer> unique = nums.stream()
        .distinct()
        .toList();
```
</details>

---

## Part B — Sorting, limit, skip 🟢🟡

### Q6. Sort `nums` from smallest to largest.
<details><summary>Show Answer</summary>

```java
List<Integer> sorted = nums.stream().sorted().toList();
```
</details>

### Q7. Sort `nums` from largest to smallest.
<details><summary>Show Answer</summary>

```java
List<Integer> desc = nums.stream()
        .sorted(Comparator.reverseOrder())
        .toList();
```
</details>

### Q8. Get the top 3 largest numbers.
<details><summary>Show Answer</summary>

```java
List<Integer> top3 = nums.stream()
        .sorted(Comparator.reverseOrder())
        .limit(3)
        .toList();   // [9, 8, 7]
```
</details>

### Q9. Sort names by their length (shortest first).
<details><summary>Show Answer</summary>

```java
List<String> byLen = names.stream()
        .sorted(Comparator.comparingInt(String::length))
        .toList();
```
</details>

---

## Part C — Sum, average, min, max 🟡

### Q10. Find the sum of all numbers in `nums`.
<details><summary>Show Answer</summary>

```java
int sum = nums.stream().mapToInt(Integer::intValue).sum();
// or: int sum = nums.stream().reduce(0, Integer::sum);
```
</details>

### Q11. Find the average of `nums`.
<details><summary>Show Answer</summary>

```java
double avg = nums.stream().mapToInt(Integer::intValue).average().orElse(0);
```
</details>

### Q12. Find the largest number.
<details><summary>Show Answer</summary>

```java
int max = nums.stream().max(Comparator.naturalOrder()).orElseThrow();
// or: int max = nums.stream().mapToInt(Integer::intValue).max().orElseThrow();
```
</details>

---

## Part D — Working with Objects 🟡

### Q13. Get a list of all people's names.
<details><summary>Show Answer</summary>

```java
List<String> allNames = people.stream()
        .map(Person::name)
        .toList();
```
</details>

### Q14. Find all people who live in "London".
<details><summary>Show Answer</summary>

```java
List<Person> londoners = people.stream()
        .filter(p -> p.city().equals("London"))
        .toList();
```
</details>

### Q15. Find the oldest person.
<details><summary>Show Answer</summary>

```java
Optional<Person> oldest = people.stream()
        .max(Comparator.comparingInt(Person::age));   // Dan, 40
```
</details>

### Q16. Get names of people older than 28, sorted alphabetically.
<details><summary>Show Answer</summary>

```java
List<String> result = people.stream()
        .filter(p -> p.age() > 28)
        .map(Person::name)
        .sorted()
        .toList();   // [Amy, Cat, Dan]
```
</details>

---

## Part E — Grouping & Collectors 🔴

### Q17. Group people by their city. Result: `Map<String, List<Person>>`.
<details><summary>Show Answer</summary>

```java
Map<String, List<Person>> byCity = people.stream()
        .collect(Collectors.groupingBy(Person::city));
```
</details>

### Q18. Count how many people live in each city. Result: `{London=2, Paris=2, Berlin=1}`.
<details><summary>Show Answer</summary>

```java
Map<String, Long> countByCity = people.stream()
        .collect(Collectors.groupingBy(Person::city, Collectors.counting()));
```
</details>

### Q19. Find the average age per city.
<details><summary>Show Answer</summary>

```java
Map<String, Double> avgAge = people.stream()
        .collect(Collectors.groupingBy(
                 Person::city,
                 Collectors.averagingInt(Person::age)));
```
</details>

### Q20. Join all names into one string: `"Amy, Bob, Cat, Dan, Eve"`.
<details><summary>Show Answer</summary>

```java
String joined = people.stream()
        .map(Person::name)
        .collect(Collectors.joining(", "));
```
</details>

### Q21. Split numbers into even and odd groups. Result: `{false=[odds], true=[evens]}`.
<details><summary>Show Answer</summary>

```java
Map<Boolean, List<Integer>> parts = nums.stream()
        .collect(Collectors.partitioningBy(n -> n % 2 == 0));
```
</details>

---

## Part F — Tricky / Interview Favourites 🔴

### Q22. Find the FIRST name longer than 3 letters.
<details><summary>Show Answer</summary>

```java
Optional<String> first = names.stream()
        .filter(n -> n.length() > 3)
        .findFirst();   // "Anna"
```
</details>

### Q23. Check: are ALL people older than 18?
<details><summary>Show Answer</summary>

```java
boolean allAdults = people.stream().allMatch(p -> p.age() > 18);  // true
```
</details>

### Q24. Flatten a list of lists into a single list.
<details><summary>Show Answer</summary>

```java
List<List<Integer>> nested = List.of(List.of(1,2), List.of(3,4), List.of(5));
List<Integer> flat = nested.stream()
        .flatMap(List::stream)
        .toList();   // [1, 2, 3, 4, 5]
```
</details>

### Q25. Count how many times each character appears in `"banana"`.
<details><summary>Show Answer</summary>

```java
Map<Character, Long> freq = "banana".chars()
        .mapToObj(c -> (char) c)
        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));
// {a=3, b=1, n=2}
```
</details>

### Q26. Find the second highest number in `nums`.
<details><summary>Show Answer</summary>

```java
Optional<Integer> second = nums.stream()
        .distinct()
        .sorted(Comparator.reverseOrder())
        .skip(1)
        .findFirst();   // 8
```
</details>

### Q27. Make a `Map<name, age>` from the people list.
<details><summary>Show Answer</summary>

```java
Map<String, Integer> nameToAge = people.stream()
        .collect(Collectors.toMap(Person::name, Person::age));
```
</details>

### Q28. Sum the ages of everyone in "Paris".
<details><summary>Show Answer</summary>

```java
int parisAge = people.stream()
        .filter(p -> p.city().equals("Paris"))
        .mapToInt(Person::age)
        .sum();   // 65
```
</details>

---

## Part G — Parallel Streams (Advanced) 🔴🔴

> **What is a parallel stream?** A normal stream does the work on **one** worker
> (one CPU thread), one item after another. A **parallel** stream splits the data
> into chunks and uses **many** workers at the same time, then joins the results.
>
> You turn it on by adding **`.parallel()`** or starting with **`.parallelStream()`**.
>
> ```java
> list.parallelStream()...        // parallel from the start
> list.stream().parallel()...     // switch a normal stream to parallel
> ```
>
> **Golden rule:** Parallel is only worth it for **big data + heavy work**.
> For small lists it is usually *slower* (splitting/joining costs more than it saves).
> And your operations must be **stateless** and **not depend on order**, or you get bugs.

### Q29. 🟢 Turn a normal stream into a parallel one and sum 1..1,000,000.
<details><summary>Show Answer</summary>

```java
long sum = LongStream.rangeClosed(1, 1_000_000)
        .parallel()
        .sum();   // splitting works perfectly because + can be done in any order
```
</details>

### Q30. Why is this code BROKEN in parallel? Fix it.
```java
List<Integer> result = new ArrayList<>();
nums.parallelStream().forEach(result::add);   // <-- danger
```
<details><summary>Show Answer</summary>

**Problem:** `ArrayList` is **not thread-safe**. Many threads call `add()` at the
same time → you can get a corrupted list, missing items, or an exception. This is a
**race condition**.

**Fix — never add into a shared mutable list yourself. Let `collect` do it:**
```java
List<Integer> result = nums.parallelStream()
        .collect(Collectors.toList());   // collect is built to be parallel-safe
```
> Lesson: avoid **side effects** (modifying outside variables) inside streams,
> especially parallel ones. Use `collect`/`reduce` instead.
</details>

### Q31. Why might `forEach` print numbers out of order in parallel, and what fixes it?
<details><summary>Show Answer</summary>

`forEach` in parallel runs on many threads, so output order is **not guaranteed**.
If you need the original order, use **`forEachOrdered`**:
```java
nums.parallelStream().forEachOrdered(System.out::println);  // keeps order
```
> Trade-off: keeping order removes some of the speed benefit.
</details>

### Q32. 🔴 What's wrong with this parallel reduce, and how do you reduce correctly?
```java
int sum = nums.parallelStream().reduce(0, (a, b) -> a + b);   // this one is fine
String joined = words.parallelStream().reduce("", (a, b) -> a + b); // why slow/risky?
```
<details><summary>Show Answer</summary>

The number `reduce` is **fine** — `+` is *associative* (grouping doesn't matter),
which is exactly what parallel reduce needs.

For strings, `(a, b) -> a + b` works but is **inefficient** in parallel because it
creates lots of intermediate strings. Use the **3-argument reduce** (with a combiner)
or, better, a `Collector`:
```java
String joined = words.parallelStream()
        .collect(Collectors.joining());   // efficient + parallel-safe
```
> Rule: parallel `reduce` needs an **identity** (like `0` or `""`) and an
> **associative** operation. `+` and `*` qualify; subtraction does NOT.
</details>

### Q33. 🔴 Why is `subtraction` unsafe as a parallel reduce operation?
<details><summary>Show Answer</summary>

Parallel reduce may group items differently, e.g. `(a - b) - c` vs `a - (b - c)`.
Subtraction is **not associative**, so different groupings give **different answers**.
Result becomes unpredictable. Only use **associative** operations (`+`, `*`, `max`,
`min`, string concatenation) in parallel reduce.
</details>

### Q34. 🔴🔴 Measure the speed difference: sequential vs parallel for a heavy task.
<details><summary>Show Answer</summary>

```java
long n = 50_000_000L;

long t1 = System.currentTimeMillis();
long seq = LongStream.rangeClosed(1, n).filter(x -> x % 2 == 0).sum();
long t2 = System.currentTimeMillis();
long par = LongStream.rangeClosed(1, n).parallel().filter(x -> x % 2 == 0).sum();
long t3 = System.currentTimeMillis();

System.out.println("sequential: " + (t2 - t1) + " ms");
System.out.println("parallel:   " + (t3 - t2) + " ms");
```
> On a multi-core machine the parallel version is usually faster for large `n`.
> For a tiny list it would be **slower** — always measure, don't assume.
</details>

### Q35. 🔴 Which data sources split well (good for parallel) and which don't?
<details><summary>Show Answer</summary>

- **Good (split cheaply):** arrays, `ArrayList`, `IntStream.range`, `HashSet`.
  They know their size and can be cut in half instantly.
- **Bad (split poorly):** `LinkedList`, streams from `iterator`, `Stream.iterate`,
  I/O sources. They must be walked one-by-one, so splitting gives little benefit.

> Pick parallel when the source is **easily splittable** and the work per item is heavy.
</details>

### Q36. 🔴🔴 What thread pool do parallel streams use, and why does that matter?
<details><summary>Show Answer</summary>

By default ALL parallel streams in your app share **one** pool: the
**common ForkJoinPool** (size = number of CPU cores − 1). That means a slow
**blocking** task (like a network/database call) inside a parallel stream can
**starve** every other parallel stream in the app.

> Lesson: **don't put blocking I/O inside parallel streams.** Keep them for
> CPU-heavy, in-memory work. (Advanced: you can run a stream inside your own
> `ForkJoinPool` to isolate it, but prefer not to block at all.)
</details>

### Q37. 🔴 Will `findFirst()` and `findAny()` behave differently in parallel? Why?
<details><summary>Show Answer</summary>

- `findFirst()` always returns the **first by order** — even in parallel, so it may
  do extra coordination to honour order.
- `findAny()` returns **whichever** matching item any thread finds first — faster in
  parallel because it doesn't care about order.

> If you don't need *the first* specifically, prefer `findAny()` in parallel.
</details>

### Q38. 🔴🔴 Group a large list by city in parallel, safely and efficiently.
<details><summary>Show Answer</summary>

```java
Map<String, List<Person>> byCity = people.parallelStream()
        .collect(Collectors.groupingByConcurrent(Person::city));
```
> Use **`groupingByConcurrent`** (not plain `groupingBy`) for parallel grouping —
> it uses a thread-safe `ConcurrentHashMap` and avoids merging overhead.
> Note: the order inside each group is no longer guaranteed.
</details>

### Q39. 🔴 Spot the bug: a counter that gives the wrong total in parallel.
```java
int[] count = {0};
nums.parallelStream().forEach(n -> count[0]++);   // wrong total!
```
<details><summary>Show Answer</summary>

`count[0]++` is **not atomic** (it's read → add → write). Multiple threads overwrite
each other → the final count is too low. Don't share a mutable counter.

**Fix — use the stream's own counting:**
```java
long count = nums.parallelStream().count();
```
Or, if you truly must count manually, use an atomic type:
```java
LongAdder count = new LongAdder();
nums.parallelStream().forEach(n -> count.increment());
```
</details>

### Q40. 🔴🔆 Checklist: when SHOULD you use a parallel stream? (theory)
<details><summary>Show Answer</summary>

Use parallel **only when ALL of these are true**:
1. **Large** dataset (thousands+ of elements).
2. **Heavy** work per element (CPU-bound math/processing), not trivial work.
3. Operations are **stateless** and **associative** (order doesn't change the result).
4. Source is **easily splittable** (array, ArrayList, range).
5. **No blocking I/O** (no DB/network/file calls inside).
6. You actually **measured** it and it's faster.

If any are false → use a normal (sequential) stream. When unsure, **stay sequential**.
</details>

---

## Bonus interview theory questions (explain in words)

1. **What is a stream?** A pipeline that processes a sequence of data with operations like filter/map, without changing the original source.
2. **Intermediate vs terminal operation?** Intermediate returns another stream and is *lazy* (filter, map, sorted). Terminal triggers the work and returns a result (collect, count, forEach).
3. **What does "lazy" mean?** Middle steps don't run until a terminal operation is called.
4. **Can you reuse a stream?** No — a stream is single-use. After a terminal operation it's closed.
5. **filter vs map?** `filter` keeps/removes items (size can shrink). `map` transforms each item (size stays the same).
6. **What is Optional?** A container that may or may not hold a value, used to avoid `null` errors.
7. **map vs flatMap?** `map` = one item → one item. `flatMap` = one item → many items, then flattened into a single stream.
8. **Stream vs Collection?** A Collection *stores* data; a Stream *processes* data and holds nothing.
9. **Sequential vs parallel stream?** Sequential uses one thread, item by item. Parallel splits the data across many CPU cores at once, then joins results — faster only for large data + heavy work.
10. **Is parallel always faster?** No. For small lists or light work the splitting/joining overhead makes it *slower*. Always measure.
11. **What pool do parallel streams use?** The shared common ForkJoinPool (cores − 1). Blocking I/O inside one can starve all others — keep parallel streams CPU-bound.
12. **What must operations be for safe parallel reduce?** *Stateless*, *non-interfering*, and *associative* (e.g. `+`, `*`, `max`) so different groupings give the same answer.
