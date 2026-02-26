package com.bigcompany.service;

import com.bigcompany.model.Employee;
import java.util.List;

public interface Analyzer<T> {
  List<T> analyze(Employee root);
}
