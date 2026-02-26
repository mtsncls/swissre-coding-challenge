package com.bigcompany;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.model.Employee;
import com.bigcompany.service.ReportingLineAnalyzer;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ReportingLineAnalyzerTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource("depthScenarios")
  void testDepthAnalysis(String description, int totalEmployees, int expectedViolationCount) {
    Employee ceo = EmployeeTestData.createLineHierarchy(totalEmployees);
    ReportingLineAnalyzer analyzer = new ReportingLineAnalyzer();
    List<ReportingLineAnalyzer.DepthViolation> violations = analyzer.analyze(ceo);

    assertEquals(expectedViolationCount, violations.size(), description);

    if (expectedViolationCount > 0) {
      ReportingLineAnalyzer.DepthViolation v = violations.get(0);

      assertEquals(6, v.employee().getId(), "First violating employee should be ID 6");
      assertEquals(5, v.depth(), "Depth should be 5 for ID 6");
    }
  }

  static Stream<Arguments> depthScenarios() {
    return Stream.of(
        Arguments.of("No violations at depth 3", 4, 0),
        Arguments.of("No violations at depth 4", 5, 0),
        Arguments.of("One violation at depth 5", 6, 1),
        Arguments.of("Multiple violations at depth 10", 11, 6));
  }
}
