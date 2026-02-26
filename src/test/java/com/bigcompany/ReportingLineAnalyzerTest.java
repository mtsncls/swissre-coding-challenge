package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.service.ReportingLineAnalyzer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportingLineAnalyzerTest {

    @Test
    void detectsEmployeesWithExcessiveDepth() {

        Employee ceo = new Employee.Builder()
                .id(1)
                .firstName("A")
                .lastName("A")
                .salary(100)
                .managerId(null)
                .build();

        Employee e2 = new Employee.Builder()
                .id(2)
                .firstName("B")
                .lastName("B")
                .salary(90)
                .managerId(1)
                .build();

        Employee e3 = new Employee.Builder()
                .id(3)
                .firstName("C")
                .lastName("C")
                .salary(80)
                .managerId(2)
                .build();

        Employee e4 = new Employee.Builder()
                .id(4)
                .firstName("D")
                .lastName("D")
                .salary(70)
                .managerId(3)
                .build();

        Employee e5 = new Employee.Builder()
                .id(5)
                .firstName("E")
                .lastName("E")
                .salary(60)
                .managerId(4)
                .build();

        Employee e6 = new Employee.Builder()
                .id(6)
                .firstName("F")
                .lastName("F")
                .salary(50)
                .managerId(5)
                .build();

        ceo.addSubordinate(e2);
        e2.addSubordinate(e3);
        e3.addSubordinate(e4);
        e4.addSubordinate(e5);
        e5.addSubordinate(e6);

        ReportingLineAnalyzer analyzer = new ReportingLineAnalyzer();
        List<ReportingLineAnalyzer.DepthViolation> violations = analyzer.analyze(ceo);

        assertEquals(1, violations.size());
        ReportingLineAnalyzer.DepthViolation v = violations.get(0);

        assertEquals(e6, v.employee());
        assertEquals(5, v.depth());
    }
}
