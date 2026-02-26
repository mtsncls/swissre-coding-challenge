package com.bigcompany.repository;

import com.bigcompany.model.Employee;
import java.util.List;

/** Data access abstraction for Employees. */
public interface EmployeeRepository {
  /**
   * Retrieves all employees from the source.
   *
   * @return A list of all employees.
   */
  List<Employee> findAll();
}
