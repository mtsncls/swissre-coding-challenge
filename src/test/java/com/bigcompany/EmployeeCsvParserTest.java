package com.bigcompany;

import com.bigcompany.model.Employee;
import com.bigcompany.parser.EmployeeCsvParser;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeCsvParserTest {

    @Test
    void parsesValidCsvCorrectly() throws Exception {
        String csv =
                """
            Id,firstName,lastName,salary,managerId
            1,John,Doe,50000,
            2,Alice,Smith,40000,1
            """;

        Path temp = Files.createTempFile("employees", ".csv");
        try (FileWriter fw = new FileWriter(temp.toFile())) {
            fw.write(csv);
        }

        EmployeeCsvParser parser = new EmployeeCsvParser();
        List<Employee> employees = parser.parse(temp.toString());

        assertEquals(2, employees.size());

        Employee ceo = employees.stream()
                .filter(e -> e.getManagerId() == null)
                .findFirst()
                .orElseThrow();

        assertEquals(1, ceo.getId());
        assertEquals("John", ceo.getFirstName());
        assertEquals("Doe", ceo.getLastName());
        assertEquals(50000, ceo.getSalary());

        Employee subordinate = employees.stream()
                .filter(e -> e.getId() == 2)
                .findFirst()
                .orElseThrow();

        assertEquals("Alice", subordinate.getFirstName());
        assertEquals("Smith", subordinate.getLastName());
        assertEquals(40000, subordinate.getSalary());
        assertEquals(1, subordinate.getManagerId());
    }
}
