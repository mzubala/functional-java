package pl.com.bottega.functional.basics.employees;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class EmployeesFinder {
    private final Collection<Employee> employees = new HashSet<>();

    EmployeesFinder(Collection<Employee> employees) {
        this.employees.addAll(employees);
    }

    Optional<Employee> findById(String id) {
        return employees.stream()
            .filter(employee -> employee.getId().equals(id))
            .findFirst();
    }

    Employee getById(String id) throws NoSuchElementException {
        return findById(id).orElseThrow(NoSuchElementException::new);
    }

    Collection<Employee> findBy(EmployeeQuery query) {
        return employeesMatching(query)
            .collect(Collectors.toList());
    }

    private Stream<Employee> employeesMatching(EmployeeQuery query) {
        return employees.stream()
            .filter(query::matches);
    }

    Collection<BasicEmployeeData> findBasicEmployeeDataBy(EmployeeQuery query) {
        return employeesMatching(query)
            .map(BasicEmployeeData::new)
            .collect(Collectors.toList());
    }

    Optional<Double> avgSalary() {
        return employees.stream()
            .mapToDouble(Employee::getYearlySalary)
            .average()
            .stream()
            .boxed()
            .findFirst();
    }

    Optional<String> mostCommonFirstName() {
        return employees.stream()
            .collect(Collectors.groupingBy(Employee::getFirstName, Collectors.counting()))
            .entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
    }

    Optional<Integer> highestSalary() {
        return employees.stream()
            .mapToInt(Employee::getYearlySalary)
            .max().stream().boxed().findFirst();
    }

    Map<Department, Integer> lowestSalariesPerDepartment() {
        return employees.stream().collect(
            HashMap::new,
            this::mergeEmployeeSalariesToMinSalariesMap,
            this::mergeMinSalaryMaps
        );
    }

    private void mergeEmployeeSalariesToMinSalariesMap(Map<Department, Integer> acc, Employee employee) {
        employee.getDepartments().forEach(department -> acc.merge(department, employee.getYearlySalary(), Math::min));
    }

    private void mergeMinSalaryMaps(Map<Department, Integer> m1, Map<Department, Integer> m2) {
        m2.forEach((key, value) -> m1.merge(key, value, Math::min));
    }

    Set<Department> departmentsOfEmployeesMatching(EmployeeQuery query) {
        return employeesMatching(query).flatMap(employee -> employee.getDepartments().stream()).collect(Collectors.toSet());
    }

    Collection<EmployeesInDepartment> employeesInDepartments() {
        return employees.stream().collect(HashMap::new, this::addEmployee, this::mergeEmployeesInDepartmentsMaps).values();
    }

    private void addEmployee(Map<Department, EmployeesInDepartment> employeesInDepartmentMap, Employee employee) {
        employee.getDepartments().forEach(department -> {
            employeesInDepartmentMap.putIfAbsent(department, new EmployeesInDepartment(department));
            employeesInDepartmentMap.computeIfPresent(department, (dep, employeesInDepartment) -> employeesInDepartment.add(employee));
        });
    }

    private void mergeEmployeesInDepartmentsMaps(Map<Department, EmployeesInDepartment> first, Map<Department, EmployeesInDepartment> second) {
        second.forEach(((department, employeesInDepartment) -> {
            first.merge(department, employeesInDepartment, this::mergeEmployeesInDepartments);
        }));
    }

    private EmployeesInDepartment mergeEmployeesInDepartments(EmployeesInDepartment firstEmployeesInDepartment, EmployeesInDepartment secondEmployeesInDepartment) {
        secondEmployeesInDepartment.employees.forEach(firstEmployeesInDepartment::add);
        return firstEmployeesInDepartment;
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
            return toCheck.stream().anyMatch(departments::contains);
        }
    }

}
