package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.parser.EmployeeCsvParser;
import com.bigcompany.service.OrgChartBuilder;
import com.bigcompany.service.ReportingLineAnalyzer;
import com.bigcompany.service.SalaryAnalyzer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final Logger log = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        if (args.length == 0) {
            log.warning("Usage: java -jar app.jar <path-to-csv-file>");
            return;
        }

        String filePath = args[0];

        try {
            List<Employee> employees = loadEmployees(filePath);
            Employee ceo = buildOrgChart(employees);

            reportSalaryViolations(ceo);
            reportDepthViolations(ceo);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Unexpected error while running the application", e);
        }
    }


    private static List<Employee> loadEmployees(String filePath) throws IOException {
        EmployeeCsvParser parser = new EmployeeCsvParser();
        return parser.parse(filePath);
    }

    private static Employee buildOrgChart(List<Employee> employees) {
        OrgChartBuilder builder = new OrgChartBuilder();
        return builder.buildTree(employees);
    }

    private static void reportSalaryViolations(Employee ceo) {
        SalaryAnalyzer analyzer = new SalaryAnalyzer();
        List<SalaryAnalyzer.SalaryViolation> violations = analyzer.analyze(ceo);

        log.info("=== Salary Violations ===");

        if (violations.isEmpty()) {
            log.info("No salary violations found.");
            return;
        }

        for (SalaryAnalyzer.SalaryViolation v : violations) {
            log.info(() -> {
                String type = v.underpaid() ? "UNDERPAID" : "OVERPAID";
                return String.format("%s: %s by %.2f", type, v.manager(), v.difference());
            });
        }
    }

    private static void reportDepthViolations(Employee ceo) {
        ReportingLineAnalyzer analyzer = new ReportingLineAnalyzer();
        List<ReportingLineAnalyzer.DepthViolation> violations = analyzer.analyze(ceo);

        log.info("=== Reporting Line Violations ===");

        if (violations.isEmpty()) {
            log.info("No reporting line violations found.");
            return;
        }

        for (ReportingLineAnalyzer.DepthViolation v : violations) {
            log.info(() -> String.format("%s depth=%d", v.employee(), v.depth()));
        }
    }
}
