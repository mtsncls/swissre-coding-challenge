package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.service.ReportingLineAnalyzer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReportingLineAnalyzerTest {

    @Test
    void detectsEmployeesWithExcessiveDepth() {
        Employee ceo = new Employee(1, "A", "A", 100, null);
        Employee e2 = new Employee(2, "B", "B", 90, 1);
        Employee e3 = new Employee(3, "C", "C", 80, 2);
        Employee e4 = new Employee(4, "D", "D", 70, 3);
        Employee e5 = new Employee(5, "E", "E", 60, 4);
        Employee e6 = new Employee(6, "F", "F", 50, 5);

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
