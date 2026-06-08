# Java 8 Streams — Real-World Coding Problems (with Solutions)

These are problems shaped like **real work**: employees, orders, e-commerce carts,
bank transactions, server logs. Each has a **scenario**, the **data**, and a
**full solution** with comments. Try it yourself, then read the solution.

A runnable version of every solution lives in
[`RealWorldProblems.java`](src/main/java/org/example/RealWorldProblems.java) — run it
to see the output.

### Shared data used below

```java
record Employee(String name, String dept, String city, double salary, int age) {}
record Order(String id, String customer, String product, int qty, double price, String status) {}
record Transaction(String account, String type, double amount, String month) {}

List<Employee> employees = List.of(
    new Employee("Amy",   "Engineering", "London", 85000, 30),
    new Employee("Bob",   "Engineering", "Paris",  72000, 45),
    new Employee("Cat",   "Sales",       "London", 60000, 38),
    new Employee("Dan",   "Sales",       "Paris",  55000, 50),
    new Employee("Eve",   "HR",          "Berlin", 48000, 29),
    new Employee("Finn",  "Engineering", "Berlin", 91000, 41),
    new Employee("Gina",  "Sales",       "London", 67000, 33)
);

List<Order> orders = List.of(
    new Order("O1", "Amy", "Laptop",  1, 1200, "DELIVERED"),
    new Order("O2", "Bob", "Mouse",   3,   25, "DELIVERED"),
    new Order("O3", "Amy", "Monitor", 2,  300, "CANCELLED"),
    new Order("O4", "Cat", "Laptop",  1, 1200, "PENDING"),
    new Order("O5", "Bob", "Keyboard",2,   75, "DELIVERED"),
    new Order("O6", "Amy", "Mouse",   5,   25, "DELIVERED")
);
```

---

## Problem 1 — Total salary bill per department
**Scenario:** Finance wants the total salary cost for each department.

<details><summary>Show Solution</summary>

```java
Map<String, Double> salaryByDept = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::dept,
                Collectors.summingDouble(Employee::salary)));
// {Engineering=248000.0, Sales=182000.0, HR=48000.0}
```
**Why:** `groupingBy(dept)` makes the buckets, `summingDouble(salary)` adds inside each.
</details>

---

## Problem 2 — Highest-paid employee in each department
**Scenario:** HR needs the top earner per department for a bonus review.

<details><summary>Show Solution</summary>

```java
Map<String, Optional<Employee>> topPerDept = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::dept,
                Collectors.maxBy(Comparator.comparingDouble(Employee::salary))));

// Cleaner: just the name, no Optional
Map<String, String> topNamePerDept = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::dept,
                Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingDouble(Employee::salary)),
                        opt -> opt.map(Employee::name).orElse("-"))));
// {Engineering=Finn, Sales=Gina, HR=Eve}
```
**Why:** `maxBy` finds the max inside each group; `collectingAndThen` unwraps the
`Optional` into just the name.
</details>

---

## Problem 3 — Give everyone a 10% raise (without changing originals)
**Scenario:** Payroll applies a 10% raise and returns a new list. Originals untouched.

<details><summary>Show Solution</summary>

```java
List<Employee> raised = employees.stream()
        .map(e -> new Employee(e.name(), e.dept(), e.city(),
                               e.salary() * 1.10, e.age()))
        .toList();
```
**Why:** `map` builds a **new** Employee for each — streams never mutate the source.
</details>

---

## Problem 4 — Average age per city, but only cities with 2+ employees
**Scenario:** A report should skip cities that have only one employee.

<details><summary>Show Solution</summary>

```java
Map<String, Double> avgAge = employees.stream()
        .collect(Collectors.groupingBy(Employee::city, Collectors.toList()))
        .entrySet().stream()
        .filter(e -> e.getValue().size() >= 2)          // keep cities with 2+
        .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().stream()
                       .mapToInt(Employee::age).average().orElse(0)));
// {Berlin=35.0, London=33.67, Paris=47.5}
// (all 3 cities have 2+ employees here; a 1-person city would be dropped)
```
**Why:** group first, then `filter` the grouped map, then compute the average per kept group.
</details>

---

## Problem 5 — Order revenue (only successful orders)
**Scenario:** Revenue = qty × price, counting only `DELIVERED` orders.

<details><summary>Show Solution</summary>

```java
double revenue = orders.stream()
        .filter(o -> o.status().equals("DELIVERED"))
        .mapToDouble(o -> o.qty() * o.price())
        .sum();
// 1200 + 75 + 150 + 125 = 1550.0
```
**Why:** `filter` keeps successful orders, `mapToDouble` turns each into its line total, `sum` adds them.
</details>

---

## Problem 6 — Top 3 customers by spend
**Scenario:** Marketing wants the 3 customers who spent the most (delivered orders).

<details><summary>Show Solution</summary>

```java
Map<String, Double> spendByCustomer = orders.stream()
        .filter(o -> o.status().equals("DELIVERED"))
        .collect(Collectors.groupingBy(
                Order::customer,
                Collectors.summingDouble(o -> o.qty() * o.price())));

List<String> top3 = spendByCustomer.entrySet().stream()
        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        .limit(3)
        .map(Map.Entry::getKey)
        .toList();
// [Amy, Bob]  (only 2 customers have delivered orders here)
```
**Why:** group→sum spend, then sort the map entries by value descending and take the top 3.
</details>

---

## Problem 7 — How many of each product were sold?
**Scenario:** Inventory wants total quantity sold per product (delivered only).

<details><summary>Show Solution</summary>

```java
Map<String, Integer> unitsSold = orders.stream()
        .filter(o -> o.status().equals("DELIVERED"))
        .collect(Collectors.groupingBy(
                Order::product,
                Collectors.summingInt(Order::qty)));
// {Laptop=1, Mouse=8, Keyboard=2}
```
</details>

---

## Problem 8 — Build a comma-separated email list
**Scenario:** Make `"amy@corp.com, bob@corp.com, ..."` for all London employees.

<details><summary>Show Solution</summary>

```java
String emails = employees.stream()
        .filter(e -> e.city().equals("London"))
        .map(e -> e.name().toLowerCase() + "@corp.com")
        .collect(Collectors.joining(", "));
// amy@corp.com, cat@corp.com, gina@corp.com
```
**Why:** `map` builds each email, `joining(", ")` glues them with separators.
</details>

---

## Problem 9 — Names grouped by salary band
**Scenario:** Split employees into "High" (≥70k) and "Standard" (<70k), names only.

<details><summary>Show Solution</summary>

```java
Map<String, List<String>> bands = employees.stream()
        .collect(Collectors.groupingBy(
                e -> e.salary() >= 70000 ? "High" : "Standard",
                Collectors.mapping(Employee::name, Collectors.toList())));
// {High=[Amy, Bob, Finn], Standard=[Cat, Dan, Eve, Gina]}
```
**Why:** the classifier returns the band label; `mapping(name, toList)` collects just names per band.
</details>

---

## Problem 10 — Monthly net balance per bank account
**Scenario:** For each account, net = sum(CREDIT) − sum(DEBIT).

```java
List<Transaction> txns = List.of(
    new Transaction("ACC1", "CREDIT", 1000, "Jan"),
    new Transaction("ACC1", "DEBIT",   300, "Jan"),
    new Transaction("ACC2", "CREDIT",  500, "Jan"),
    new Transaction("ACC1", "CREDIT",  200, "Feb"),
    new Transaction("ACC2", "DEBIT",   100, "Feb")
);
```
<details><summary>Show Solution</summary>

```java
Map<String, Double> netByAccount = txns.stream()
        .collect(Collectors.groupingBy(
                Transaction::account,
                Collectors.summingDouble(t ->
                        t.type().equals("CREDIT") ? t.amount() : -t.amount())));
// {ACC1=900.0, ACC2=400.0}
```
**Why:** treat debits as negative inside `summingDouble`, so the sum is the net balance.
</details>

---

## Problem 11 — Find duplicate values in a list
**Scenario:** Given order IDs, find which IDs appear more than once.

```java
List<String> ids = List.of("A", "B", "A", "C", "B", "A");
```
<details><summary>Show Solution</summary>

```java
Set<String> duplicates = ids.stream()
        .collect(Collectors.groupingBy(id -> id, Collectors.counting()))
        .entrySet().stream()
        .filter(e -> e.getValue() > 1)
        .map(Map.Entry::getKey)
        .collect(Collectors.toSet());
// [A, B]
```
**Why:** count each value, then keep only those whose count is greater than 1.
</details>

---

## Problem 12 — Parse server logs: count errors per hour
**Scenario:** Each log line is `"HH:MM LEVEL message"`. Count ERROR lines per hour.

```java
List<String> logs = List.of(
    "09:15 INFO  started",
    "09:42 ERROR db timeout",
    "09:50 ERROR null pointer",
    "10:05 WARN  slow query",
    "10:30 ERROR disk full"
);
```
<details><summary>Show Solution</summary>

```java
Map<String, Long> errorsPerHour = logs.stream()
        .filter(line -> line.contains("ERROR"))
        .collect(Collectors.groupingBy(
                line -> line.substring(0, 2),   // the hour "09", "10"
                Collectors.counting()));
// {09=2, 10=1}
```
**Why:** keep only ERROR lines, group by the hour substring, count each group.
</details>

---

## Problem 13 — Flatten orders to a unique product catalogue
**Scenario:** From all orders, get the sorted set of distinct products.

<details><summary>Show Solution</summary>

```java
List<String> catalogue = orders.stream()
        .map(Order::product)
        .distinct()
        .sorted()
        .toList();
// [Keyboard, Laptop, Monitor, Mouse]
```
</details>

---

## Problem 14 — First employee matching a complex rule
**Scenario:** Find the first Engineer in London earning over 80k.

<details><summary>Show Solution</summary>

```java
Optional<Employee> match = employees.stream()
        .filter(e -> e.dept().equals("Engineering"))
        .filter(e -> e.city().equals("London"))
        .filter(e -> e.salary() > 80000)
        .findFirst();

match.ifPresentOrElse(
        e -> System.out.println("Found: " + e.name()),
        () -> System.out.println("No match"));
// Found: Amy
```
**Why:** stack `filter`s for each rule; `findFirst` stops at the first hit (efficient).
</details>

---

## Problem 15 — Summary statistics in one pass
**Scenario:** Get count, sum, min, max, and average salary together — efficiently.

<details><summary>Show Solution</summary>

```java
DoubleSummaryStatistics stats = employees.stream()
        .mapToDouble(Employee::salary)
        .summaryStatistics();

System.out.println("count = " + stats.getCount());
System.out.println("sum   = " + stats.getSum());
System.out.println("min   = " + stats.getMin());
System.out.println("max   = " + stats.getMax());
System.out.println("avg   = " + stats.getAverage());
```
**Why:** `summaryStatistics()` computes everything in a **single pass** instead of 5 separate streams.
</details>

---

## Tips you just practised
- **Group then aggregate** is the workhorse: `groupingBy(key, summingX/counting/maxBy/mapping)`.
- **Sort a Map by value:** stream its `entrySet()`, sort with `Map.Entry.comparingByValue()`.
- **Unwrap Optionals** from `maxBy`/`minBy` with `collectingAndThen`.
- **Never mutate** the source — `map` to new objects.
- **One pass beats many** — use `summaryStatistics()` when you need several numbers.
