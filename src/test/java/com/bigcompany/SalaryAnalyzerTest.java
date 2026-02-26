package com.bigcompany;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.model.Employee;
import com.bigcompany.service.SalaryAnalyzer;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SalaryAnalyzerTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource("salaryScenarios")
  void testSalaryAnalysis(
      String description,
      double managerSalary,
      double[] subordinateSalaries,
      int expectedViolations,
      Boolean expectedUnderpaid,
      Double expectedDifference) {

    Employee manager =
        EmployeeTestData.createManagerWithSubordinatesSalary(managerSalary, subordinateSalaries);
    SalaryAnalyzer analyzer = new SalaryAnalyzer();
    List<SalaryAnalyzer.SalaryViolation> violations = analyzer.analyze(manager);

    assertEquals(expectedViolations, violations.size(), description);

    if (expectedViolations > 0 && expectedUnderpaid != null) {
      SalaryAnalyzer.SalaryViolation v = violations.get(0);
      assertEquals(expectedUnderpaid, v.underpaid(), description + " (underpaid check)");
      if (expectedDifference != null) {
        assertEquals(
            expectedDifference, v.difference(), 0.001, description + " (difference check)");
      }
    }
  }

  static Stream<Arguments> salaryScenarios() {
    return Stream.of(
        Arguments.of("Underpaid manager", 45000.0, new double[] {40000, 40000}, 1, true, 3000.0),
        Arguments.of("Overpaid manager", 100000.0, new double[] {40000, 40000}, 1, false, 40000.0),
        Arguments.of(
            "Manager with correct salary", 50000.0, new double[] {40000, 40000}, 0, null, null));
  }
}
