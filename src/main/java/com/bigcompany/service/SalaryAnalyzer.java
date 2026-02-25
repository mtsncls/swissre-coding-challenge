package com.bigcompany.service;

import com.bigcompany.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class SalaryAnalyzer {

    public record SalaryViolation(Employee manager, double difference, boolean underpaid) {}

    public List<SalaryViolation> analyze(Employee root) {
        List<SalaryViolation> violations = new ArrayList<>();
        analyzeRecursively(root, violations);
        return violations;
    }

    private void analyzeRecursively(Employee manager, List<SalaryViolation> violations) {
        if (hasSubordinates(manager)) {
            evaluateManager(manager, violations);
        }

        for (Employee sub : manager.getSubordinates()) {
            analyzeRecursively(sub, violations);
        }
    }

    private boolean hasSubordinates(Employee e) {
        return !e.getSubordinates().isEmpty();
    }

    private void evaluateManager(Employee manager, List<SalaryViolation> violations) {
        double avg = averageSalary(manager);
        double minAllowed = avg * 1.20;
        double maxAllowed = avg * 1.50;
        double salary = manager.getSalary();

        if (salary < minAllowed) {
            violations.add(new SalaryViolation(manager, minAllowed - salary, true));
        } else if (salary > maxAllowed) {
            violations.add(new SalaryViolation(manager, salary - maxAllowed, false));
        }
    }

    private double averageSalary(Employee manager) {
        return manager.getSubordinates().stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);
    }
}
