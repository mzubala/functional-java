package pl.com.bottega.functional.basics.employees;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

class EmployeesFinder {
    private final Collection<Employee> employees = new HashSet<>();

    EmployeesFinder(Collection<Employee> employees) {
        this.employees.addAll(employees);
    }

    Optional<Employee> findById(String id) {
        for (Employee employee : employees) {
            if (employee.getId().equals(id)) {
                return Optional.of(employee);
            }
        }
        return Optional.empty();
    }

    Employee getById(String id) throws NoSuchElementException {
        var employeeOptional = findById(id);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        throw new NoSuchElementException();
    }

    Collection<Employee> findBy(EmployeeQuery query) {
        var results = new LinkedList<Employee>();
        for (Employee employee : employees) {
            if (query.matches(employee)) {
                results.add(employee);
            }
        }
        return results;
    }

    Collection<BasicEmployeeData> findBasicEmployeeDataBy(EmployeeQuery query) {
        var results = new LinkedList<BasicEmployeeData>();
        for (Employee employee : employees) {
            if (query.matches(employee)) {
                results.add(new BasicEmployeeData(employee));
            }
        }
        return results;
    }

    Optional<Double> avgSalary() {
        if (employees.isEmpty()) {
            return Optional.empty();
        }
        var avg = 0.0;
        int count = 0;
        for (var employee : employees) {
            avg = (avg * count + employee.getYearlySalary()) / ++count;
        }
        return Optional.of(avg);
    }

    Optional<String> mostCommonFirstName() {
        var namesCounts = new HashMap<String, Integer>();
        for (var employee : employees) {
            var currentCount = namesCounts.getOrDefault(employee.getFirstName(), 0);
            namesCounts.put(employee.getFirstName(), currentCount + 1);
        }
        var namesList = new LinkedList<>(namesCounts.entrySet());
        namesList.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        if (namesList.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(namesList.get(0).getKey());
        }
    }

    Optional<Integer> highestSalary() {
        if (employees.isEmpty()) {
            return Optional.empty();
        }
        var highest = Integer.MIN_VALUE;
        for (var employee : employees) {
            if (employee.getYearlySalary() > highest) {
                highest = employee.getYearlySalary();
            }
        }
        return Optional.of(highest);
    }

    Map<Department, Integer> lowestSalariesPerDepartment() {
        var salaries = new HashMap<Department, Integer>();
        for (var employee : employees) {
            for (var department : employee.getDepartments()) {
                var minSalary = salaries.getOrDefault(department, Integer.MAX_VALUE);
                if (employee.getYearlySalary() < minSalary) {
                    salaries.put(department, employee.getYearlySalary());
                }
            }
        }
        return salaries;
    }

    Set<Department> departmentsOfEmployeesMatching(EmployeeQuery query) {
        var results = new HashSet<Department>();
        for (Employee employee : employees) {
            if (query.matches(employee)) {
                results.addAll(employee.getDepartments());
            }
        }
        return results;
    }

    Collection<EmployeesInDepartment> employeesInDepartments() {
        Map<Department, EmployeesInDepartment> map = new HashMap<>();
        for (var employee : employees) {
            for (var department : employee.getDepartments()) {
                var employeesInDepartment = map.get(department);
                if (employeesInDepartment == null) {
                    employeesInDepartment = new EmployeesInDepartment(department);
                    map.put(department, employeesInDepartment);
                }
                employeesInDepartment.add(employee);
            }
        }
        return map.values();
    }

    @Value
    @Builder
    static class EmployeeQuery {
        Pattern firstNamePattern, lastNamePattern;
        Integer ageFromInclusive, ageToExclusive;
        Integer salaryFromInclusive, salaryToExclusive;
        Gender gender;
        Set<Department> departments;


        boolean matches(Employee employee) {
            return (firstNamePattern == null || firstNamePattern.matcher(employee.getFirstName()).matches()) &&
                (lastNamePattern == null || lastNamePattern.matcher(employee.getLastName()).matches()) &&
                (ageFromInclusive == null || employee.getAge() >= ageFromInclusive) &&
                (ageToExclusive == null || employee.getAge() < ageToExclusive) &&
                (salaryFromInclusive == null || employee.getYearlySalary() >= salaryFromInclusive) &&
                (salaryToExclusive == null || employee.getYearlySalary() < salaryToExclusive) &&
                (gender == null || employee.getGender().equals(gender)) &&
                (departments == null ||
                    (employee.getDepartments().isEmpty() && departments.isEmpty()) ||
                    (!departments.isEmpty() && containsAny(employee.getDepartments(), departments))
                );
        }

        private boolean containsAny(Set<Department> departments, Set<Department> toCheck) {
            for (Department department : toCheck) {
                if (departments.contains(department)) {
                    return true;
                }
            }
            return false;

        }
    }

}
