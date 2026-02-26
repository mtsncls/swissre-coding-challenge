# Big Company Organizational Analysis

This project implements a solution for **Code Exercise 106**, analyzing an organizational structure from a CSV file. The application evaluates salary consistency across management levels and identifies employees with excessively long reporting lines. The implementation uses **Java SE**, **Maven**, and **JUnit 5**, following principles of clarity, simplicity, and maintainability.

---

## Features

- Parses employee data from a CSV file.
- Builds an in-memory organizational hierarchy.
- Detects managers who earn:
    - Less than 20% above the average salary of their direct subordinates.
    - More than 50% above that average.
- Detects employees with more than four management layers between them and the CEO.
- Outputs results using Java’s built-in logging facilities.

---

## Project Structure

```
src/
 ├── main/
 │    ├── java/com/bigcompany/
 │    │         ├── App.java
 │    │         ├── model/Employee.java
 │    │         ├── repository/
 │    │         │     ├── EmployeeRepository.java
 │    │         │     └── CsvEmployeeRepository.java
 │    │         └── service/
 │    │               ├── OrgChartBuilder.java
 │    │               ├── SalaryAnalyzer.java
 │    │               ├── ReportingLineAnalyzer.java
 │    │               └── Analyzer.java
 │    └── resources/employees.csv
 └── test/java/com/bigcompany/
      ├── CsvEmployeeRepositoryTest.java
      ├── SalaryAnalyzerTest.java
      ├── ReportingLineAnalyzerTest.java
      ├── OrgChartBuilderTest.java
      ├── EmployeeTestData.java
      ├── DeepHierarchyTest.java
      └── HighVolumeTest.java
```

---

## Running the Application

Build the project:

```
mvn clean package
```

Run the application:

```
java -jar target/big-company-challenge-1.0-SNAPSHOT.jar src/main/resources/employees.csv
```

---

## CSV Format

The input file must follow this structure:

```
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```

- The CEO is the only employee without a `managerId`.
- Salaries must be numeric.
- The file may contain up to 1000 rows.

---

## Tests

JUnit 5 tests cover:

- Repository-based data access (EmployeeRepository)
- Hierarchy construction
- Salary rule evaluation
- Reporting line depth evaluation

Run tests:

```
mvn test
```

---

## Code Formatting

This project uses [Google Java Format](https://github.com/google/google-java-format) to maintain a consistent code style. Formatting is enforced via the `fmt-maven-plugin`.

To reformat the entire project, run:

```
mvn fmt:format
```

---

## Robustness & Error Handling

- **CSV Validation**: The parser validates for minimum required columns per row.
- **Detailed Error Messages**: If a CEO is not found, the application logs all available employee IDs to assist in diagnosing broken hierarchies.
- **Reporting Line Integrity**: Detects and reports if an employee references a manager ID that does not exist in the dataset.

---

## Assumptions

- Exactly one CEO exists.
- The hierarchy contains no cycles.
- All IDs and salaries are valid numbers.
- Only direct subordinates are considered for salary calculations.

---


