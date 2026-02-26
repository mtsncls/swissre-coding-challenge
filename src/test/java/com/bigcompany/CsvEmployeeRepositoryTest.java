package com.bigcompany;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.model.Employee;
import com.bigcompany.repository.CsvEmployeeRepository;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class CsvEmployeeRepositoryTest {

  @Test
  void retrievesAllEmployeesCorrectly() throws Exception {
    List<Employee> expectedEmployees = EmployeeTestData.createStandardList();
    String csvContent = EmployeeTestData.toCsvString(expectedEmployees);
    Path temp = EmployeeTestData.createTempCsv(csvContent);

    CsvEmployeeRepository repository = new CsvEmployeeRepository(temp.toString());
    List<Employee> employees = repository.findAll();

    assertEquals(2, employees.size());
    Employee ceo =
        employees.stream().filter(e -> e.getManagerId() == null).findFirst().orElseThrow();
    assertEquals(1, ceo.getId());
    assertEquals("John", ceo.getFirstName());
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidCsvScenarios")
  void testInvalidCsv(String description, String csvContent, String expectedError)
      throws Exception {
    Path temp = EmployeeTestData.createTempCsv(csvContent);
    CsvEmployeeRepository repository = new CsvEmployeeRepository(temp.toString());

    Exception ex = assertThrows(Exception.class, repository::findAll);
    assertTrue(ex.getMessage().contains(expectedError), description);
  }

  @Test
  void throwsExceptionOnMissingFile() {
    CsvEmployeeRepository repository = new CsvEmployeeRepository("non_existent_file.csv");
    RuntimeException ex = assertThrows(RuntimeException.class, repository::findAll);
    assertTrue(ex.getMessage().contains("Failed to read employees from CSV"));
  }

  static Stream<Arguments> invalidCsvScenarios() {
    return Stream.of(
        Arguments.of(
            "Malformed line (too few columns)",
            "Id,firstName,lastName,salary,managerId\n" + "1,John,Doe",
            "Malformed CSV line"),
        Arguments.of(
            "Non-numeric ID",
            "Id,firstName,lastName,salary,managerId\n" + "ABC,John,Doe,50000,",
            "Invalid numeric value"),
        Arguments.of(
            "Non-numeric Salary",
            "Id,firstName,lastName,salary,managerId\n" + "1,John,Doe,expensive,",
            "Invalid numeric value"));
  }
}
