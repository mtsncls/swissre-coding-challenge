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

class HighVolumeTest {

    @Test
    void handlesCsvWith1000Rows() throws Exception {
        Path temp = Files.createTempFile("employees-1000", ".csv");

        try (FileWriter fw = new FileWriter(temp.toFile())) {
            fw.write("Id,firstName,lastName,salary,managerId\n");
            fw.write("1,CEO,Root,70000,\n");

            for (int i = 2; i <= 1000; i++) {
                fw.write(i + ",Emp" + i + ",Last" + i + ",50000,1\n");
            }
        }

        EmployeeCsvParser parser = new EmployeeCsvParser();
        List<Employee> employees = parser.parse(temp.toString());

        assertEquals(1000, employees.size(), "Should parse exactly 1000 employees");

        OrgChartBuilder builder = new OrgChartBuilder();
        Employee ceo = builder.buildTree(employees);

        assertEquals(999, ceo.getSubordinates().size(), "CEO should have 999 direct reports");

        SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
        List<SalaryAnalyzer.SalaryViolation> salaryViolations = salaryAnalyzer.analyze(ceo);

        assertTrue(salaryViolations.isEmpty(), "No salary violations expected");

        ReportingLineAnalyzer depthAnalyzer = new ReportingLineAnalyzer();
        List<ReportingLineAnalyzer.DepthViolation> depthViolations = depthAnalyzer.analyze(ceo);

        assertTrue(depthViolations.isEmpty(), "No depth violations expected");
    }
}
