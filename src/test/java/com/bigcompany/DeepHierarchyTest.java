package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.parser.EmployeeCsvParser;
import com.bigcompany.service.OrgChartBuilder;
import com.bigcompany.service.ReportingLineAnalyzer;
import com.bigcompany.service.SalaryAnalyzer;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeepHierarchyTest {

    @Test
    void handlesDeepHierarchyOf1000Levels() throws Exception {
        Path temp = Files.createTempFile("employees-deep-1000", ".csv");

        try (FileWriter fw = new FileWriter(temp.toFile())) {
            fw.write("Id,firstName,lastName,salary,managerId\n");

            double[] salaries = new double[1001];
            double ratio = 1.30;
            salaries[1000] = 10000.0;

            for (int i = 999; i >= 1; i--) {
                salaries[i] = salaries[i + 1] * ratio;
            }

            fw.write("1,CEO,Root," + salaries[1] + ",\n");

            for (int i = 2; i <= 1000; i++) {
                fw.write(i + ",Emp" + i + ",Last" + i + "," + salaries[i] + "," + (i - 1) + "\n");
            }
        }

        EmployeeCsvParser parser = new EmployeeCsvParser();
        List<Employee> employees = parser.parse(temp.toString());

        assertEquals(1000, employees.size(), "Should parse exactly 1000 employees");

        OrgChartBuilder builder = new OrgChartBuilder();
        Employee ceo = builder.buildTree(employees);

        Employee current = ceo;
        for (int i = 2; i <= 1000; i++) {
            assertEquals(1, current.getSubordinates().size(), "Each employee should have exactly one subordinate");
            current = current.getSubordinates().get(0);
        }

        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        List<SalaryAnalyzer.SalaryViolation> salaryViolations = salaryAnalyzer.analyze(ceo);
        assertTrue(salaryViolations.isEmpty(), "No salary violations expected");

        ReportingLineAnalyzer depthAnalyzer = new ReportingLineAnalyzer();
        List<ReportingLineAnalyzer.DepthViolation> depthViolations = depthAnalyzer.analyze(ceo);

        assertFalse(depthViolations.isEmpty(), "Depth violations expected for deep hierarchy");
        assertTrue(depthViolations.size() > 900, "Most employees should exceed depth 4");
    }
}
