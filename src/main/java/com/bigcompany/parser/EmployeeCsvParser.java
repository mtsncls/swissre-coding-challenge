package com.bigcompany.parser;

import com.bigcompany.model.Employee;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeCsvParser {

    public List<Employee> parse(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = readNextNonEmptyLine(reader);

            if (line != null && isHeader(line)) {
                line = readNextNonEmptyLine(reader);
            }

            while (line != null) {
                employees.add(parseEmployee(line));
                line = readNextNonEmptyLine(reader);
            }
        }

        return employees;
    }

    private String readNextNonEmptyLine(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line;
            }
        }
        return null;
    }

    private boolean isHeader(String line) {
        return line.toLowerCase().startsWith("id,");
    }

    private Employee parseEmployee(String line) {
        String[] parts = line.split(",");

        int id = Integer.parseInt(parts[0].trim());
        String firstName = parts[1].trim();
        String lastName = parts[2].trim();
        double salary = Double.parseDouble(parts[3].trim());

        Integer managerId = null;
        if (parts.length > 4 && !parts[4].trim().isEmpty()) {
            managerId = Integer.parseInt(parts[4].trim());
        }

        return new Employee.Builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .salary(salary)
                .managerId(managerId)
                .build();
    }
}
