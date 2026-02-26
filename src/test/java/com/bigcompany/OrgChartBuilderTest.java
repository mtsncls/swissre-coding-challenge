package com.bigcompany;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.model.Employee;
import com.bigcompany.service.OrgChartBuilder;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OrgChartBuilderTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource("validScenarios")
  void testValidHierarchies(String description, List<Employee> employees, int expectedCeoId) {
    OrgChartBuilder builder = new OrgChartBuilder();
    Employee root = builder.buildTree(employees);

    assertNotNull(root);
    assertEquals(expectedCeoId, root.getId());
    // Verification of structure is implicit in the build process,
    // but we can add more specific checks in the provider if needed.
  }

  static Stream<Arguments> validScenarios() {
    return Stream.of(
        Arguments.of("Simple 3-level hierarchy", EmployeeTestData.createSimpleHierarchy(), 1),
        Arguments.of("Standard 2-employee list", EmployeeTestData.createStandardList(), 1));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidScenarios")
  void testInvalidHierarchies(String description, List<Employee> employees, String expectedError) {
    OrgChartBuilder builder = new OrgChartBuilder();
    IllegalStateException ex =
        assertThrows(IllegalStateException.class, () -> builder.buildTree(employees));
    assertTrue(ex.getMessage().contains(expectedError), description);
  }

  static Stream<Arguments> invalidScenarios() {
    return Stream.of(
        Arguments.of(
            "Missing CEO",
            List.of(
                new Employee.Builder()
                    .id(1)
                    .firstName("E")
                    .lastName("1")
                    .salary(50)
                    .managerId(10)
                    .build()),
            "No CEO found"),
        Arguments.of(
            "Missing Manager",
            List.of(
                new Employee.Builder().id(1).firstName("CEO").lastName("R").salary(100).build(),
                new Employee.Builder()
                    .id(2)
                    .firstName("E")
                    .lastName("2")
                    .salary(50)
                    .managerId(9)
                    .build()),
            "Manager with ID 9 not found for employee 2"));
  }
}
