package com.bigcompany.service;

import com.bigcompany.model.Employee;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrgChartBuilder {

  public Employee buildTree(List<Employee> employees) {
    Map<Integer, Employee> index = indexEmployees(employees);
    Employee ceo = findCeo(employees);

    for (Employee e : employees) {
      attachToManager(e, index);
    }

    return ceo;
  }

  private Map<Integer, Employee> indexEmployees(List<Employee> employees) {
    Map<Integer, Employee> map = new HashMap<>();
    for (Employee e : employees) {
      map.put(e.getId(), e);
    }
    return map;
  }

  private Employee findCeo(List<Employee> employees) {
    return employees.stream()
        .filter(e -> e.getManagerId() == null)
        .findFirst()
        .orElseThrow(
            () -> {
              String ids =
                  employees.stream()
                      .map(e -> String.valueOf(e.getId()))
                      .collect(java.util.stream.Collectors.joining(", "));
              return new IllegalStateException(
                  "No CEO found (employee without managerId). Employee IDs present: [" + ids + "]");
            });
  }

  private void attachToManager(Employee e, Map<Integer, Employee> index) {
    if (e.getManagerId() == null) {
      return;
    }

    Employee manager = index.get(e.getManagerId());
    if (manager == null) {
      throw new IllegalStateException(
          "Manager with ID " + e.getManagerId() + " not found for employee " + e.getId());
    }

    manager.addSubordinate(e);
  }
}
