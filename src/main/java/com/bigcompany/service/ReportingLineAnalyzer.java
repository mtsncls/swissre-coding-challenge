package com.bigcompany.service;

import com.bigcompany.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class ReportingLineAnalyzer {

    public record DepthViolation(Employee employee, int depth) {
    }

    public List<DepthViolation> analyze(Employee ceo) {
        List<DepthViolation> violations = new ArrayList<>();
        dfs(ceo, 0, violations);
        return violations;
    }

    private void dfs(Employee e, int depth, List<DepthViolation> violations) {
        if (depth > 4) {
            violations.add(new DepthViolation(e, depth));
        }

        for (Employee sub : e.getSubordinates()) {
            dfs(sub, depth + 1, violations);
        }
    }
}
