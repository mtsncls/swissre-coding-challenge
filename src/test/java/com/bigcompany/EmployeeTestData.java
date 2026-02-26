package com.bigcompany;

import com.bigcompany.model.Employee;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;

public class EmployeeTestData {

  public static List<Employee> createSimpleHierarchy() {
    return List.of(
        emp(1, "CEO", "Root", 100, null), emp(2, "M", "1", 80, 1), emp(3, "W", "A", 50, 2));
  }

  public static List<Employee> createStandardList() {
    return List.of(emp(1, "John", "Doe", 50000, null), emp(2, "Alice", "Smith", 40000, 1));
  }

  public static Employee createLineHierarchy(int count) {
    Employee root = emp(1, "CEO", "Root", 100000, null);
    Employee current = root;
    for (int i = 2; i <= count; i++) {
      Employee sub = emp(i, "E" + i, "L" + i, 50000, i - 1);
      current.addSubordinate(sub);
      current = sub;
    }
    return root;
  }

  public static Employee createManagerWithSubordinatesSalary(double mSalary, double... sSalaries) {
    Employee manager = emp(1, "Manager", "X", mSalary, null);
    IntStream.range(0, sSalaries.length)
        .forEach(i -> manager.addSubordinate(emp(i + 2, "Sub", "" + i, sSalaries[i], 1)));
    return manager;
  }

  public static Path createTempCsv(String content) throws IOException {
    Path temp = Files.createTempFile("test-employees", ".csv");
    Files.writeString(temp, content);
    return temp;
  }

  public static String toCsvString(List<Employee> employees) {
    StringBuilder sb = new StringBuilder("Id,firstName,lastName,salary,managerId\n");
    for (Employee e : employees) {
      sb.append(e.getId())
          .append(",")
          .append(e.getFirstName())
          .append(",")
          .append(e.getLastName())
          .append(",")
          .append(e.getSalary())
          .append(",")
          .append(e.getManagerId() == null ? "" : e.getManagerId())
          .append("\n");
    }
    return sb.toString();
  }

  private static Employee emp(int id, String f, String l, double s, Integer mId) {
    return new Employee.Builder().id(id).firstName(f).lastName(l).salary(s).managerId(mId).build();
  }
}
