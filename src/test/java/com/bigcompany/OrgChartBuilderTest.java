package com.bigcompany;

import static org.junit.jupiter.api.Assertions.*;

import com.bigcompany.model.Employee;
import com.bigcompany.service.OrgChartBuilder;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrgChartBuilderTest {

  @Test
  void buildsHierarchyCorrectly() {

    Employee ceo =
        new Employee.Builder()
            .id(1)
            .firstName("CEO")
            .lastName("Root")
            .salary(100000)
            .managerId(null)
            .build();

    Employee m1 =
        new Employee.Builder()
            .id(2)
            .firstName("Manager")
            .lastName("One")
            .salary(80000)
            .managerId(1)
            .build();

    Employee e1 =
        new Employee.Builder()
            .id(3)
            .firstName("Worker")
            .lastName("A")
            .salary(50000)
            .managerId(2)
            .build();

    List<Employee> employees = List.of(ceo, m1, e1);

    OrgChartBuilder builder = new OrgChartBuilder();
    Employee root = builder.buildTree(employees);

    assertEquals(ceo, root);
    assertEquals(1, ceo.getSubordinates().size());
    assertEquals(m1, ceo.getSubordinates().get(0));

    assertEquals(1, m1.getSubordinates().size());
    assertEquals(e1, m1.getSubordinates().get(0));
  }

  @Test
  void throwsExceptionWhenNoCeoFound() {
    Employee e1 =
        new Employee.Builder()
            .id(1)
            .firstName("E")
            .lastName("1")
            .salary(50000)
            .managerId(10)
            .build();
    List<Employee> list = List.of(e1);

    OrgChartBuilder builder = new OrgChartBuilder();
    IllegalStateException ex =
        assertThrows(IllegalStateException.class, () -> builder.buildTree(list));
    assertTrue(ex.getMessage().contains("No CEO found"));
    assertTrue(ex.getMessage().contains("Employee IDs present: [1]"));
  }

  @Test
  void throwsExceptionOnMissingManager() {
    Employee e1 =
        new Employee.Builder()
            .id(1)
            .firstName("CEO")
            .lastName("Root")
            .salary(100000)
            .managerId(null)
            .build();
    Employee e2 =
        new Employee.Builder()
            .id(2)
            .firstName("E")
            .lastName("2")
            .salary(50000)
            .managerId(99)
            .build();

    List<Employee> list = List.of(e1, e2);
    OrgChartBuilder builder = new OrgChartBuilder();
    IllegalStateException ex =
        assertThrows(IllegalStateException.class, () -> builder.buildTree(list));
    assertTrue(ex.getMessage().contains("Manager with ID 99 not found for employee 2"));
  }
}
