package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.service.OrgChartBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrgChartBuilderTest {

    @Test
    void buildsHierarchyCorrectly() {
        Employee ceo = new Employee(1, "CEO", "Root", 100000, null);
        Employee m1 = new Employee(2, "Manager", "One", 80000, 1);
        Employee e1 = new Employee(3, "Worker", "A", 50000, 2);

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
