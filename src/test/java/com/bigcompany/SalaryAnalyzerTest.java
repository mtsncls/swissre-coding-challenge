package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.service.SalaryAnalyzer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalaryAnalyzerTest {

    @Test
    void detectsUnderpaidManager() {
        Employee manager = new Employee(1, "Boss", "Man", 45000, null);
        Employee s1 = new Employee(2, "A", "A", 40000, 1);
        Employee s2 = new Employee(3, "B", "B", 40000, 1);

        manager.addSubordinate(s1);
        manager.addSubordinate(s2);

        SalaryAnalyzer analyzer = new SalaryAnalyzer();
        List<SalaryAnalyzer.SalaryViolation> violations = analyzer.analyze(manager);

        assertEquals(1, violations.size());
        SalaryAnalyzer.SalaryViolation v = violations.get(0);

        assertTrue(v.underpaid());
        assertEquals(manager, v.manager());

        double expectedDifference = (40000 * 1.20) - 45000;
        assertEquals(expectedDifference, v.difference(), 0.001);
    }

    @Test
    void detectsOverpaidManager() {
        Employee manager = new Employee(1, "Boss", "Man", 100000, null);
        Employee s1 = new Employee(2, "A", "A", 40000, 1);
        Employee s2 = new Employee(3, "B", "B", 40000, 1);

        manager.addSubordinate(s1);
        manager.addSubordinate(s2);

        SalaryAnalyzer analyzer = new SalaryAnalyzer();
        List<SalaryAnalyzer.SalaryViolation> violations = analyzer.analyze(manager);

        assertEquals(1, violations.size());
        SalaryAnalyzer.SalaryViolation v = violations.get(0);

        assertFalse(v.underpaid());
        assertEquals(manager, v.manager());

        double expectedDifference = 100000 - (40000 * 1.50);
        assertEquals(expectedDifference, v.difference(), 0.001);
    }

    @Test
    void noViolationWhenSalaryIsWithinRange() {
        Employee manager = new Employee(1, "Boss", "Man", 60000, null);
        Employee s1 = new Employee(2, "A", "A", 40000, 1);
        Employee s2 = new Employee(3, "B", "B", 40000, 1);

        manager.addSubordinate(s1);
        manager.addSubordinate(s2);

        SalaryAnalyzer analyzer = new SalaryAnalyzer();
        List<SalaryAnalyzer.SalaryViolation> violations = analyzer.analyze(manager);

        assertTrue(violations.isEmpty());
    }
}
