package org.example;

import java.util.*;
import java.util.stream.*;

/**
 * Runnable solutions for the real-world coding problems in PROBLEMS.md.
 * Run this and match each printed block to the matching problem.
 */
public class RealWorldProblems {

    record Employee(String name, String dept, String city, double salary, int age) {}
    record Order(String id, String customer, String product, int qty, double price, String status) {}
    record Transaction(String account, String type, double amount, String month) {}

    public static void main(String[] args) {

        List<Employee> employees = List.of(
                new Employee("Amy", "Engineering", "London", 85000, 30),
                new Employee("Bob", "Engineering", "Paris", 72000, 45),
                new Employee("Cat", "Sales", "London", 60000, 38),
                new Employee("Dan", "Sales", "Paris", 55000, 50),
                new Employee("Eve", "HR", "Berlin", 48000, 29),
                new Employee("Finn", "Engineering", "Berlin", 91000, 41),
                new Employee("Gina", "Sales", "London", 67000, 33)
        );

        List<Order> orders = List.of(
                new Order("O1", "Amy", "Laptop", 1, 1200, "DELIVERED"),
                new Order("O2", "Bob", "Mouse", 3, 25, "DELIVERED"),
                new Order("O3", "Amy", "Monitor", 2, 300, "CANCELLED"),
                new Order("O4", "Cat", "Laptop", 1, 1200, "PENDING"),
                new Order("O5", "Bob", "Keyboard", 2, 75, "DELIVERED"),
                new Order("O6", "Amy", "Mouse", 5, 25, "DELIVERED")
        );

        System.out.println("P1. Total salary per department:");
        System.out.println(employees.stream()
                .collect(Collectors.groupingBy(Employee::dept,
                        Collectors.summingDouble(Employee::salary))));

        System.out.println("\nP2. Highest-paid name per department:");
        System.out.println(employees.stream()
                .collect(Collectors.groupingBy(Employee::dept,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparingDouble(Employee::salary)),
                                opt -> opt.map(Employee::name).orElse("-")))));

        System.out.println("\nP3. 10% raise (new list, originals unchanged):");
        List<Employee> raised = employees.stream()
                .map(e -> new Employee(e.name(), e.dept(), e.city(), e.salary() * 1.10, e.age()))
                .toList();
        System.out.println("Amy before=" + employees.get(0).salary() + ", after=" + raised.get(0).salary());

        System.out.println("\nP4. Average age per city (cities with 2+ employees):");
        System.out.println(employees.stream()
                .collect(Collectors.groupingBy(Employee::city, Collectors.toList()))
                .entrySet().stream()
                .filter(e -> e.getValue().size() >= 2)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().stream().mapToInt(Employee::age).average().orElse(0))));

        System.out.println("\nP5. Revenue from DELIVERED orders:");
        System.out.println(orders.stream()
                .filter(o -> o.status().equals("DELIVERED"))
                .mapToDouble(o -> o.qty() * o.price())
                .sum());

        System.out.println("\nP6. Top customers by spend (delivered):");
        Map<String, Double> spend = orders.stream()
                .filter(o -> o.status().equals("DELIVERED"))
                .collect(Collectors.groupingBy(Order::customer,
                        Collectors.summingDouble(o -> o.qty() * o.price())));
        System.out.println(spend.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList());

        System.out.println("\nP7. Units sold per product (delivered):");
        System.out.println(orders.stream()
                .filter(o -> o.status().equals("DELIVERED"))
                .collect(Collectors.groupingBy(Order::product,
                        Collectors.summingInt(Order::qty))));

        System.out.println("\nP8. Email list for London employees:");
        System.out.println(employees.stream()
                .filter(e -> e.city().equals("London"))
                .map(e -> e.name().toLowerCase() + "@corp.com")
                .collect(Collectors.joining(", ")));

        System.out.println("\nP9. Names grouped by salary band:");
        System.out.println(employees.stream()
                .collect(Collectors.groupingBy(
                        e -> e.salary() >= 70000 ? "High" : "Standard",
                        Collectors.mapping(Employee::name, Collectors.toList()))));

        System.out.println("\nP10. Net balance per account:");
        List<Transaction> txns = List.of(
                new Transaction("ACC1", "CREDIT", 1000, "Jan"),
                new Transaction("ACC1", "DEBIT", 300, "Jan"),
                new Transaction("ACC2", "CREDIT", 500, "Jan"),
                new Transaction("ACC1", "CREDIT", 200, "Feb"),
                new Transaction("ACC2", "DEBIT", 100, "Feb")
        );
        System.out.println(txns.stream()
                .collect(Collectors.groupingBy(Transaction::account,
                        Collectors.summingDouble(t ->
                                t.type().equals("CREDIT") ? t.amount() : -t.amount()))));

        System.out.println("\nP11. Duplicate IDs:");
        List<String> ids = List.of("A", "B", "A", "C", "B", "A");
        System.out.println(ids.stream()
                .collect(Collectors.groupingBy(id -> id, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet()));

        System.out.println("\nP12. Errors per hour from logs:");
        List<String> logs = List.of(
                "09:15 INFO  started",
                "09:42 ERROR db timeout",
                "09:50 ERROR null pointer",
                "10:05 WARN  slow query",
                "10:30 ERROR disk full"
        );
        System.out.println(logs.stream()
                .filter(line -> line.contains("ERROR"))
                .collect(Collectors.groupingBy(line -> line.substring(0, 2),
                        Collectors.counting())));

        System.out.println("\nP13. Distinct product catalogue (sorted):");
        System.out.println(orders.stream()
                .map(Order::product)
                .distinct()
                .sorted()
                .toList());

        System.out.println("\nP14. First Engineer in London over 80k:");
        employees.stream()
                .filter(e -> e.dept().equals("Engineering"))
                .filter(e -> e.city().equals("London"))
                .filter(e -> e.salary() > 80000)
                .findFirst()
                .ifPresentOrElse(
                        e -> System.out.println("Found: " + e.name()),
                        () -> System.out.println("No match"));

        System.out.println("\nP15. Salary summary statistics (one pass):");
        DoubleSummaryStatistics stats = employees.stream()
                .mapToDouble(Employee::salary)
                .summaryStatistics();
        System.out.printf("count=%d sum=%.0f min=%.0f max=%.0f avg=%.2f%n",
                stats.getCount(), stats.getSum(), stats.getMin(), stats.getMax(), stats.getAverage());
    }
}
