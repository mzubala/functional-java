package pl.com.bottega.functional.basics.employees;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(of = "id")
@Value
class Employee {
    String id;
    String firstName, lastName;
    Integer age;
    Integer yearlySalary;
    Gender gender;
    Set<Department> departments;
}

@AllArgsConstructor
@EqualsAndHashCode
@ToString
class EmployeesInDepartment {
    Department department;
    List<Employee> employees = new LinkedList<>();
    BigDecimal avgAge = BigDecimal.ZERO;
    BigDecimal avgSalary = BigDecimal.ZERO;

    EmployeesInDepartment(Department department) {
        this.department = department;
    }

    EmployeesInDepartment add(Employee employee) {
        avgAge = this.avgAge.multiply(BigDecimal.valueOf(employees.size())).add(BigDecimal.valueOf(employee.getAge()))
            .divide(new BigDecimal(employees.size() + 1), 2, RoundingMode.HALF_DOWN);
        avgSalary = this.avgSalary.multiply(BigDecimal.valueOf(employees.size())).add(BigDecimal.valueOf(employee.getYearlySalary()))
            .divide(new BigDecimal(employees.size() + 1), 2, RoundingMode.HALF_DOWN);
        employees.add(employee);
        employees.sort(new Comparator<Employee>() {
            @Override
            public int compare(Employee o1, Employee o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        return this;
    }
}

enum Department {
    BUSINESS, IT, HR, ADMINISTRATION, BOARD
}

enum Gender {
    MALE, FEMALE
}

@Value
class BasicEmployeeData {
    String id;
    String firstName, lastName;

    public BasicEmployeeData(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
    }
}