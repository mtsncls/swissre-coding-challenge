package com.bigcompany;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.model.Employee;
import com.bigcompany.service.SalaryAnalyzer;
import java.util.List;
import org.junit.jupiter.api.Test;

class SalaryAnalyzerTest {

  @Test
  void detectsUnderpaidManager() {
    Employee manager =
        new Employee.Builder()
            .id(1)
            .firstName("Boss")
            .lastName("Man")
            .salary(45000)
            .managerId(null)
            .build();

    Employee s1 =
        new Employee.Builder()
            .id(2)
            .firstName("A")
            .lastName("A")
            .salary(40000)
            .managerId(1)
            .build();

    Employee s2 =
        new Employee.Builder()
            .id(3)
            .firstName("B")
            .lastName("B")
            .salary(40000)
            .managerId(1)
            .build();

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
    Employee manager =
        new Employee.Builder()
            .id(1)
            .firstName("Boss")
            .lastName("Man")
            .salary(100000)
            .managerId(null)
            .build();

    Employee s1 =
        new Employee.Builder()
            .id(2)
            .firstName("A")
            .lastName("A")
            .salary(40000)
            .managerId(1)
            .build();

    Employee s2 =
        new Employee.Builder()
            .id(3)
            .firstName("B")
            .lastName("B")
            .salary(40000)
            .managerId(1)
            .build();

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
    Employee manager =
        new Employee.Builder()
            .id(1)
            .firstName("Boss")
            .lastName("Man")
            .salary(60000)
            .managerId(null)
            .build();

    Employee s1 =
        new Employee.Builder()
            .id(2)
            .firstName("A")
            .lastName("A")
            .salary(40000)
            .managerId(1)
            .build();

    Employee s2 =
        new Employee.Builder()
            .id(3)
            .firstName("B")
            .lastName("B")
            .salary(40000)
            .managerId(1)
            .build();

    manager.addSubordinate(s1);
    manager.addSubordinate(s2);

    SalaryAnalyzer analyzer = new SalaryAnalyzer();
    List<SalaryAnalyzer.SalaryViolation> violations = analyzer.analyze(manager);

    assertTrue(violations.isEmpty());
  }
}
