# Java 8 Streams — Glossary of Common Terms

Every word you'll hear about streams, explained in **plain English** with a tiny example.
Skim it once, then come back whenever a word confuses you.

> Quick legend: 🧱 = a building block, ⚙️ = an operation, 💡 = a concept.

---

## Core building blocks

**🧱 Stream**
A pipeline that processes a sequence of data. It does NOT store data — it just
flows data through steps. Think "conveyor belt", not "box".
`list.stream()`

**🧱 Source**
Where the data comes from — the thing you call `.stream()` on.
A List, Set, array, or a range of numbers.
`names.stream()`, `Arrays.stream(arr)`, `IntStream.range(1, 10)`

**🧱 Pipeline**
The whole chain: source → middle steps → terminal step. One full "sentence" of stream code.

**🧱 Element / Item**
A single value flowing through the stream (one number, one name, one object).

---

## The two kinds of operations

**⚙️ Intermediate operation**
A *middle* step that returns **another stream**, so you can chain more. It is **lazy**
(does nothing until a terminal step runs). Examples: `filter`, `map`, `sorted`, `distinct`.

**⚙️ Terminal operation**
The *final* step that returns a **real value** (a list, number, boolean…) and actually
**runs** the pipeline. Examples: `collect`, `count`, `forEach`, `reduce`, `findFirst`.
A stream needs **exactly one** terminal operation.

**💡 Lazy / Lazy evaluation**
Middle steps are recorded but not executed until the terminal step triggers them.
No terminal step = nothing happens.

**💡 Eager**
The opposite of lazy — runs immediately. Terminal operations are eager.

**💡 Short-circuiting**
An operation that can stop early without looking at every element.
`findFirst`, `anyMatch`, `limit` — they quit as soon as they have the answer.

---

## The most common operations (verbs)

**⚙️ filter** — keep only items that pass a test; count can shrink.
`.filter(n -> n > 10)`

**⚙️ map** — transform each item into something else; count stays the same.
`.map(String::toUpperCase)`

**⚙️ flatMap** — turn each item into a stream, then flatten them all into one.
Used to unpack "lists of lists".
`.flatMap(List::stream)`

**⚙️ sorted** — put items in order.
`.sorted()` or `.sorted(Comparator.reverseOrder())`

**⚙️ distinct** — remove duplicates.

**⚙️ limit(n)** — keep only the first `n` items.

**⚙️ skip(n)** — drop the first `n` items.

**⚙️ peek** — look at each item without changing it (mainly for debugging).

**⚙️ reduce** — combine all items into a single value (sum, product, concatenation).
`.reduce(0, Integer::sum)`

**⚙️ collect** — gather the results into a List, Set, Map, or String.
`.collect(Collectors.toList())`

**⚙️ forEach** — do an action for each item (returns nothing).
`.forEach(System.out::println)`

**⚙️ count** — how many items (returns a `long`).

**⚙️ anyMatch / allMatch / noneMatch** — return true/false after testing items.

**⚙️ findFirst / findAny** — grab one item, wrapped in an `Optional`.

**⚙️ min / max** — smallest / largest item, wrapped in an `Optional`.

---

## Collecting results

**🧱 Collector**
A "recipe" telling `collect` how to build the final result. You usually get them
from the `Collectors` helper class.

**🧱 Collectors** (the class)
A toolbox of ready-made collectors: `toList()`, `toSet()`, `toMap()`, `joining()`,
`groupingBy()`, `counting()`, `summingInt()`, `partitioningBy()`.

**⚙️ groupingBy** — sort items into buckets keyed by something → a `Map`.
`groupingBy(Person::city)` → `{London=[...], Paris=[...]}`

**⚙️ partitioningBy** — special grouping into exactly two buckets: `true` and `false`.
`partitioningBy(n -> n % 2 == 0)`

**⚙️ joining** — glue strings together with a separator.
`joining(", ")` → `"a, b, c"`

**⚙️ Downstream collector**
A second collector used *inside* a group to aggregate it.
`groupingBy(city, counting())` → `{London=2, Paris=1}`

---

## Helper concepts

**🧱 Lambda expression**
A short inline function: `parameters -> body`. The "test" or "rule" you pass to operations.
`n -> n > 10`

**🧱 Method reference**
A shorter way to write a lambda that just calls one method. `String::toUpperCase`
means the same as `s -> s.toUpperCase()`.

**🧱 Predicate**
A lambda that returns **true/false** — used by `filter`, `anyMatch`, etc.
`n -> n % 2 == 0`

**🧱 Function**
A lambda that **transforms** input into output — used by `map`.
`s -> s.length()`

**🧱 Consumer**
A lambda that **does something** and returns nothing — used by `forEach`, `peek`.
`x -> System.out.println(x)`

**🧱 Supplier**
A lambda that **produces** a value, taking no input. `() -> new ArrayList<>()`

**🧱 Comparator**
An object that says how to **order** items, used by `sorted`, `min`, `max`.
`Comparator.comparingInt(Person::age)`

**🧱 Optional**
A box that may hold a value or be empty — avoids `null` errors. Returned by
`findFirst`, `max`, etc. Read it with `.orElse(default)` or `.ifPresent(...)`.

---

## Number streams

**🧱 IntStream / LongStream / DoubleStream**
Special streams of primitive numbers that unlock `.sum()`, `.average()`, `.max()`.

**⚙️ mapToInt / mapToObj**
`mapToInt` converts a regular stream into an `IntStream` (to do math).
`mapToObj` converts a number stream back into objects.

**🧱 summaryStatistics**
Computes count, sum, min, max, and average in **one pass**.
Returns `IntSummaryStatistics` / `DoubleSummaryStatistics`.

**🧱 boxing / unboxing**
Converting between a primitive (`int`) and its object (`Integer`).
`Integer::intValue` unboxes; this is why number streams exist (to avoid the cost).

---

## Parallel terms

**💡 Sequential stream**
The default — processes items one at a time on a single thread.

**💡 Parallel stream**
Splits data across multiple CPU cores to run at the same time, then joins results.
`list.parallelStream()` or `stream().parallel()`.

**💡 Associative operation**
An operation where grouping doesn't change the result: `(a+b)+c == a+(b+c)`.
Required for safe parallel `reduce`. `+`, `*`, `max` are associative; subtraction is NOT.

**💡 Stateless operation**
A step whose result for an item doesn't depend on other items or outside variables.
Safe for parallel. (Stateful steps like `sorted` need to see everything.)

**💡 Race condition**
A bug where multiple threads touch the same data at once and corrupt it.
Avoid by not modifying shared variables inside a stream — use `collect`/`reduce`.

**💡 ForkJoinPool**
The shared thread pool parallel streams use (size ≈ CPU cores − 1).

**⚙️ groupingByConcurrent**
A thread-safe version of `groupingBy` for parallel streams.

**⚙️ forEachOrdered**
Like `forEach` but keeps the original order (useful in parallel where order isn't guaranteed).

---

## "Wait, what's the difference?" quick table

| These get confused | The difference |
|--------------------|----------------|
| **filter vs map** | filter keeps/drops (size shrinks); map transforms (size same) |
| **map vs flatMap** | map: 1 item → 1 item; flatMap: 1 item → many, then flattened |
| **intermediate vs terminal** | intermediate returns a stream (lazy); terminal returns a value (runs it) |
| **findFirst vs findAny** | findFirst respects order; findAny is faster in parallel |
| **collect vs reduce** | collect builds a container (List/Map); reduce makes one combined value |
| **Stream vs Collection** | Collection stores data; Stream processes data and holds nothing |
| **lambda vs method reference** | both are inline functions; method reference is the short form |
| **sequential vs parallel** | sequential = 1 thread; parallel = many cores at once |

---

## Smallest possible mental model

> **Source** (`.stream()`) → some **intermediate** steps that **filter** and **map**
> (lazy) → exactly one **terminal** step that **collects** the answer. The original
> data is never changed, and a stream is **single-use**.
