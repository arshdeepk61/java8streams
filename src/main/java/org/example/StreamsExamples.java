package org.example;

import java.util.*;
import java.util.stream.*;

/**
 * Runnable examples for the Java 8 Streams cheat sheet and questions.
 * Run this file and read the console output next to the code to learn by seeing.
 */
public class StreamsExamples {

    // A simple data type to represent a person.
    record Person(String name, String city, int age) {}

    public static void main(String[] args) {

        List<Integer> nums = List.of(5, 2, 9, 1, 7, 4, 8, 3, 6, 2);
        List<String> names = List.of("Amy", "Bob", "Anna", "Carl", "Alex", "Bea");

        List<Person> people = List.of(
                new Person("Amy", "London", 30),
                new Person("Bob", "Paris", 25),
                new Person("Cat", "London", 35),
                new Person("Dan", "Paris", 40),
                new Person("Eve", "Berlin", 28)
        );

        System.out.println("=== 1. filter: keep even numbers ===");
        List<Integer> evens = nums.stream()
                .filter(n -> n % 2 == 0)
                .toList();
        System.out.println(evens);

        System.out.println("\n=== 2. map: names to UPPERCASE ===");
        System.out.println(names.stream().map(String::toUpperCase).toList());

        System.out.println("\n=== 3. count names starting with 'A' ===");
        System.out.println(names.stream().filter(n -> n.startsWith("A")).count());

        System.out.println("\n=== 4. distinct + sorted ===");
        System.out.println(nums.stream().distinct().sorted().toList());

        System.out.println("\n=== 5. top 3 largest numbers ===");
        System.out.println(nums.stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .toList());

        System.out.println("\n=== 6. sum and average ===");
        int sum = nums.stream().mapToInt(Integer::intValue).sum();
        double avg = nums.stream().mapToInt(Integer::intValue).average().orElse(0);
        System.out.println("sum = " + sum + ", avg = " + avg);

        System.out.println("\n=== 7. names of people older than 28, sorted ===");
        System.out.println(people.stream()
                .filter(p -> p.age() > 28)
                .map(Person::name)
                .sorted()
                .toList());

        System.out.println("\n=== 8. oldest person ===");
        people.stream()
                .max(Comparator.comparingInt(Person::age))
                .ifPresent(p -> System.out.println(p.name() + " (" + p.age() + ")"));

        System.out.println("\n=== 9. group people by city ===");
        Map<String, List<Person>> byCity = people.stream()
                .collect(Collectors.groupingBy(Person::city));
        byCity.forEach((city, list) ->
                System.out.println(city + " -> " + list.stream().map(Person::name).toList()));

        System.out.println("\n=== 10. count people per city ===");
        System.out.println(people.stream()
                .collect(Collectors.groupingBy(Person::city, Collectors.counting())));

        System.out.println("\n=== 11. average age per city ===");
        System.out.println(people.stream()
                .collect(Collectors.groupingBy(Person::city,
                        Collectors.averagingInt(Person::age))));

        System.out.println("\n=== 12. join all names ===");
        System.out.println(people.stream()
                .map(Person::name)
                .collect(Collectors.joining(", ")));

        System.out.println("\n=== 13. partition numbers into even/odd ===");
        System.out.println(nums.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0)));

        System.out.println("\n=== 14. flatMap: flatten list of lists ===");
        List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4), List.of(5));
        System.out.println(nested.stream().flatMap(List::stream).toList());

        System.out.println("\n=== 15. character frequency in 'banana' ===");
        System.out.println("banana".chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting())));

        System.out.println("\n=== 16. second highest number ===");
        nums.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .ifPresent(n -> System.out.println("second highest = " + n));
    }
}
