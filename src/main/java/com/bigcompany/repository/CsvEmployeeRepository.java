package com.bigcompany.repository;

import com.bigcompany.model.Employee;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** CSV-based implementation of the EmployeeRepository. */
public class CsvEmployeeRepository implements EmployeeRepository {

  private static final int MIN_COLUMNS = 4;
  private final String filePath;

  public CsvEmployeeRepository(String filePath) {
    this.filePath = filePath;
  }

  @Override
  public List<Employee> findAll() {
    List<Employee> employees = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line = readNextNonEmptyLine(reader);

      // Skip header if present
      if (line != null && isHeader(line)) {
        line = readNextNonEmptyLine(reader);
      }

      while (line != null) {
        employees.add(parseEmployee(line));
        line = readNextNonEmptyLine(reader);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read employees from CSV at: " + filePath, e);
    }

    return employees;
  }

  private String readNextNonEmptyLine(BufferedReader reader) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      if (!line.trim().isEmpty()) {
        return line;
      }
    }
    return null;
  }

  private boolean isHeader(String line) {
    return line.toLowerCase().startsWith("id,");
  }

  private Employee parseEmployee(String line) {
    String[] parts = line.split(",");
    if (parts.length < MIN_COLUMNS) {
      throw new IllegalArgumentException("Malformed CSV line: " + line);
    }

    try {
      int id = Integer.parseInt(parts[0].trim());
      String firstName = parts[1].trim();
      String lastName = parts[2].trim();
      double salary = Double.parseDouble(parts[3].trim());

      Integer managerId = null;
      if (parts.length > MIN_COLUMNS && !parts[MIN_COLUMNS].trim().isEmpty()) {
        managerId = Integer.parseInt(parts[MIN_COLUMNS].trim());
      }

      return new Employee.Builder()
          .id(id)
          .firstName(firstName)
          .lastName(lastName)
          .salary(salary)
          .managerId(managerId)
          .build();
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid numeric value on CSV line: " + line, e);
    }
  }
}
