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

## Bonus interview theory questions (explain in words)

1. **What is a stream?** A pipeline that processes a sequence of data with operations like filter/map, without changing the original source.
2. **Intermediate vs terminal operation?** Intermediate returns another stream and is *lazy* (filter, map, sorted). Terminal triggers the work and returns a result (collect, count, forEach).
3. **What does "lazy" mean?** Middle steps don't run until a terminal operation is called.
4. **Can you reuse a stream?** No — a stream is single-use. After a terminal operation it's closed.
5. **filter vs map?** `filter` keeps/removes items (size can shrink). `map` transforms each item (size stays the same).
6. **What is Optional?** A container that may or may not hold a value, used to avoid `null` errors.
7. **map vs flatMap?** `map` = one item → one item. `flatMap` = one item → many items, then flattened into a single stream.
8. **Stream vs Collection?** A Collection *stores* data; a Stream *processes* data and holds nothing.
