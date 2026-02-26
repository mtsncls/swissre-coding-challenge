package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.service.OrgChartBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrgChartBuilderTest {

    @Test
    void buildsHierarchyCorrectly() {

        Employee ceo = new Employee.Builder()
                .id(1)
                .firstName("CEO")
                .lastName("Root")
                .salary(100000)
                .managerId(null)
                .build();

        Employee m1 = new Employee.Builder()
                .id(2)
                .firstName("Manager")
                .lastName("One")
                .salary(80000)
                .managerId(1)
                .build();

        Employee e1 = new Employee.Builder()
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
}
