# Java 8 Streams — Cheat Sheet (Simple Language)

> A **Stream** is like a **conveyor belt** for your data.
> You put a list of things on the belt, attach little machines (operations)
> that change/filter the items, and at the end you collect the result.
>
> **Important:** A stream does NOT change your original list. It makes a new result.

---

## 1. The 3 parts of every stream

```
SOURCE  ───►  MIDDLE STEPS (0 or many)  ───►  FINAL STEP (exactly 1)
(a list)      (filter, map, sorted...)         (collect, count, forEach...)
```

| Part | Name | Examples | Returns |
|------|------|----------|---------|
| Start | Source | `list.stream()` | a Stream |
| Middle | Intermediate | `filter`, `map`, `sorted`, `distinct`, `limit` | another Stream (lazy) |
| End | Terminal | `collect`, `forEach`, `count`, `reduce`, `findFirst` | a real value/list |

**Key idea:** Middle steps do *nothing* until a final step runs. (This is called "lazy".)

---

## 2. How to START a stream (get data onto the belt)

```java
List<String> names = List.of("Amy", "Bob", "Cat");

names.stream()                       // from a List/Set
Arrays.stream(myArray)               // from an array
Stream.of("a", "b", "c")             // from loose values
IntStream.range(1, 5)                // 1,2,3,4  (5 not included)
IntStream.rangeClosed(1, 5)          // 1,2,3,4,5 (5 included)
```

---

## 3. MIDDLE steps (change the belt) — most used

| Operation | What it does (plain English) | Example |
|-----------|------------------------------|---------|
| `filter`  | **Keep** items that pass a test | `.filter(n -> n > 10)` |
| `map`     | **Transform** each item into something else | `.map(s -> s.toUpperCase())` |
| `sorted`  | **Sort** items | `.sorted()` |
| `distinct`| **Remove duplicates** | `.distinct()` |
| `limit`   | Keep only the **first N** | `.limit(3)` |
| `skip`    | **Skip** the first N | `.skip(2)` |
| `peek`    | **Look** at items (for debugging) | `.peek(System.out::println)` |

**filter vs map — the #1 confusion:**
- `filter` = decide YES/NO to keep an item (count may shrink).
- `map` = change WHAT each item is (count stays the same).

```java
// filter: keep even numbers ->  [2, 4]
Stream.of(1,2,3,4).filter(n -> n % 2 == 0)

// map: double every number ->   [2, 4, 6, 8]
Stream.of(1,2,3,4).map(n -> n * 2)
```

---

## 4. FINAL steps (take result off the belt)

| Operation | What you get back |
|-----------|-------------------|
| `collect(Collectors.toList())` | a **List** of results |
| `collect(Collectors.toSet())`  | a **Set** (no duplicates) |
| `forEach(x -> ...)`            | nothing — just does an action per item |
| `count()`                      | a number (how many items) |
| `findFirst()` / `findAny()`   | one item wrapped in `Optional` |
| `anyMatch` / `allMatch` / `noneMatch` | true/false |
| `min` / `max`                  | smallest/largest (Optional) |
| `reduce`                       | combine all into ONE value (e.g. sum) |

```java
List<Integer> evens = numbers.stream()
        .filter(n -> n % 2 == 0)
        .collect(Collectors.toList());
```

> Java 16+: you can write `.toList()` instead of `.collect(Collectors.toList())`.

---

## 5. Collectors (advanced collecting) — the cheat lines

```java
// Join strings:  "Amy, Bob, Cat"
.collect(Collectors.joining(", "))

// Count -> Long
.collect(Collectors.counting())

// Sum / average of a field
.collect(Collectors.summingInt(Person::getAge))
.collect(Collectors.averagingInt(Person::getAge))

// Group items into a Map by some key
// { true=[evens...], false=[odds...] }
.collect(Collectors.groupingBy(n -> n % 2 == 0))

// Group AND count  -> { "London"=3, "Paris"=2 }
.collect(Collectors.groupingBy(Person::getCity, Collectors.counting()))

// Make a Map<id, person>
.collect(Collectors.toMap(Person::getId, p -> p))

// Split into two lists by a yes/no test
.collect(Collectors.partitioningBy(n -> n > 10))
```

---

## 6. Number streams (sum, average — the easy way)

```java
int total = IntStream.rangeClosed(1, 100).sum();          // 5050

double avg = numbers.stream()
        .mapToInt(Integer::intValue)   // turn into IntStream
        .average()                     // returns OptionalDouble
        .orElse(0);
```

`mapToInt`, `mapToLong`, `mapToDouble` unlock `.sum()`, `.average()`, `.max()`, `.min()`.

---

## 7. Optional (the "maybe empty box")

`findFirst`, `max`, etc. return an **Optional** — a box that may or may not hold a value.

```java
Optional<String> first = names.stream().filter(s -> s.startsWith("B")).findFirst();

first.ifPresent(System.out::println);   // print only if found
String name = first.orElse("Not found"); // give a default if empty
```

---

## 8. flatMap — flatten a "list of lists" into one list

```java
List<List<Integer>> nested = List.of(List.of(1,2), List.of(3,4));

List<Integer> flat = nested.stream()
        .flatMap(List::stream)   // glue inner lists together
        .collect(Collectors.toList());   // [1, 2, 3, 4]
```

Use `flatMap` when each item itself contains a collection you want to "unpack".

---

## 9. Method references (shortcut for lambdas)

| Lambda | Method reference (shorter) |
|--------|----------------------------|
| `s -> s.toUpperCase()` | `String::toUpperCase` |
| `s -> System.out.println(s)` | `System.out::println` |
| `n -> Integer.parseInt(n)` | `Integer::parseInt` |

They mean the same thing — method references are just cleaner.

---

## 10. Full real example (read this top to bottom)

```java
record Person(String name, String city, int age) {}

List<Person> people = List.of(
    new Person("Amy", "London", 30),
    new Person("Bob", "Paris",  25),
    new Person("Cat", "London", 35),
    new Person("Dan", "Paris",  40)
);

// Names of people older than 28, from London, sorted, UPPERCASE
List<String> result = people.stream()
        .filter(p -> p.city().equals("London"))  // keep Londoners
        .filter(p -> p.age() > 28)               // keep age > 28
        .map(Person::name)                       // person -> name
        .map(String::toUpperCase)                // name -> NAME
        .sorted()                                // alphabetical
        .toList();                               // [AMY, CAT]

// Average age per city -> { London=32.5, Paris=32.5 }
Map<String, Double> avgByCity = people.stream()
        .collect(Collectors.groupingBy(
                 Person::city,
                 Collectors.averagingInt(Person::age)));
```

---

## 11. Parallel streams (use MANY CPU cores at once)

Normal stream = **one** worker, item by item.
Parallel stream = data is **split** across many CPU cores, then results are **joined**.

```java
list.parallelStream()...          // parallel from the start
list.stream().parallel()...       // switch a normal stream to parallel
LongStream.rangeClosed(1, 1_000_000).parallel().sum();
```

**Use parallel ONLY when ALL are true:**
1. **Large** data (thousands+ items).
2. **Heavy** CPU work per item.
3. Operations are **stateless** + **associative** (`+`, `*`, `max` — order doesn't matter).
4. Source **splits easily** (array, `ArrayList`, range — NOT `LinkedList`).
5. **No blocking I/O** (no DB/network/file calls inside).
6. You **measured** it and it's actually faster.

> If unsure → stay **sequential**. For small lists, parallel is usually *slower*.

**Parallel traps to avoid:**
| Trap | Why it breaks | Fix |
|------|---------------|-----|
| Adding to a shared `ArrayList` in `forEach` | not thread-safe → corrupt data | use `.collect(...)` |
| Shared counter `count[0]++` | race condition → wrong total | use `.count()` / `LongAdder` |
| `groupingBy` in parallel | slow merging | use `groupingByConcurrent` |
| Need order but use `forEach` | order not guaranteed | use `forEachOrdered` |
| Blocking I/O inside | starves the shared ForkJoinPool | keep it CPU-only |

---

## 12. Common mistakes (avoid these!)

1. **Reusing a stream** — a stream is single-use. After a terminal step it's dead.
   ```java
   Stream<Integer> s = list.stream();
   s.count();
   s.forEach(...);   // ERROR: stream already used
   ```
2. **Forgetting the terminal step** — `list.stream().filter(...)` alone does NOTHING.
3. **Expecting the original list to change** — streams never modify the source.
4. **Using streams for everything** — a simple `for` loop is fine for tiny/simple tasks.

---

## 13. The one-line summary

> `source.stream()` → middle steps that **filter** and **map** → a terminal step that **collects** the answer. Original data stays untouched.
