package com.bigcompany.service;

import com.bigcompany.model.Employee;
import java.util.List;

/**
 * Base interface for organizational analysis.
 *
 * @param <T> The type of violation found during analysis.
 */
public interface Analyzer<T> {
  /**
   * Analyzes the organizational structure starting from the given root.
   *
   * @param root The root employee (usually the CEO) to start analysis from.
   * @return A list of violations found.
   */
  List<T> analyze(Employee root);
}
