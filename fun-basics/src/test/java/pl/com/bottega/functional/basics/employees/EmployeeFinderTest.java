package pl.com.bottega.functional.basics.employees;

import org.junit.jupiter.api.Test;
import pl.com.bottega.functional.basics.employees.EmployeesFinder.EmployeeQuery;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import static java.util.regex.Pattern.compile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pl.com.bottega.functional.basics.employees.Department.ADMINISTRATION;
import static pl.com.bottega.functional.basics.employees.Department.BOARD;
import static pl.com.bottega.functional.basics.employees.Department.BUSINESS;
import static pl.com.bottega.functional.basics.employees.Department.HR;
import static pl.com.bottega.functional.basics.employees.Department.IT;

class EmployeeFinderTest {
    private List<Employee> employees = List.of(
        new Employee("1", "John", "Doe", 40, 100_000, Gender.MALE, Set.of(BUSINESS)),
        new Employee("2", "Jane", "Philips", 35, 40_000, Gender.FEMALE, Set.of(BUSINESS, HR)),
        new Employee("3", "Jimi", "Choo", 22, 90_000, Gender.MALE, Set.of(IT, ADMINISTRATION)),
        new Employee("4", "Peter", "Parker", 61, 200_000, Gender.MALE, Set.of(BUSINESS, ADMINISTRATION)),
        new Employee("5", "Jessica", "Simpson", 43, 80_000, Gender.FEMALE, Set.of(IT))
    );
    private EmployeesFinder finder = new EmployeesFinder(employees);

    @Test
    void findsEmployeeById() {
        assertThat(finder.findById("1")).contains(employees.get(0));
        assertThat(finder.findById("3")).contains(employees.get(2));
        assertThat(finder.findById("999")).isEmpty();
    }

    @Test
    void getsEmployeeById() {
        assertThat(finder.getById("1")).isEqualTo(employees.get(0));
        assertThat(finder.getById("3")).isEqualTo(employees.get(2));
        assertThatThrownBy(() -> finder.getById("999")).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findsEmployeeByFirstName() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().firstNamePattern(compile(".*")).build())
        ).containsAll(employees);
        assertThat(
            finder.findBy(EmployeeQuery.builder().firstNamePattern(compile("J.*")).build())
        ).containsExactlyInAnyOrder(employees.get(0), employees.get(1), employees.get(2), employees.get(4));
        assertThat(
            finder.findBy(EmployeeQuery.builder().firstNamePattern(compile("^J.*z$")).build())
        ).isEmpty();
    }

    @Test
    void findsEmployeeByLastName() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().lastNamePattern(compile(".*")).build())
        ).containsAll(employees);
        assertThat(
            finder.findBy(EmployeeQuery.builder().lastNamePattern(compile(".*p.*")).build())
        ).containsExactlyInAnyOrder(employees.get(1), employees.get(4));
        assertThat(
            finder.findBy(EmployeeQuery.builder().lastNamePattern(compile("^J.*z$")).build())
        ).isEmpty();
    }

    @Test
    void findsEmployeeByAgeFrom() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().ageFromInclusive(22).build())
        ).containsAll(employees);
        assertThat(
            finder.findBy(EmployeeQuery.builder().ageFromInclusive(40).build())
        ).containsExactlyInAnyOrder(employees.get(0), employees.get(3), employees.get(4));
        assertThat(
            finder.findBy(EmployeeQuery.builder().ageFromInclusive(65).build())
        ).isEmpty();
    }

    @Test
    void findsEmployeeByAgeTo() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().ageToExclusive(62).build())
        ).containsAll(employees);
        assertThat(
            finder.findBy(EmployeeQuery.builder().ageToExclusive(61).build())
        ).containsExactlyInAnyOrder(employees.get(0), employees.get(1), employees.get(2), employees.get(4));
        assertThat(
            finder.findBy(EmployeeQuery.builder().ageToExclusive(22).build())
        ).isEmpty();
    }

    @Test
    void findsEmployeeBySalaryFrom() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().salaryFromInclusive(10_000).build())
        ).containsAll(employees);
        assertThat(
            finder.findBy(EmployeeQuery.builder().salaryFromInclusive(100_000).build())
        ).containsExactlyInAnyOrder(employees.get(0), employees.get(3));
        assertThat(
            finder.findBy(EmployeeQuery.builder().salaryFromInclusive(500_000).build())
        ).isEmpty();
    }

    @Test
    void findsEmployeeBySalaryTo() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().salaryToExclusive(200_001).build())
        ).containsAll(employees);
        assertThat(
            finder.findBy(EmployeeQuery.builder().salaryToExclusive(200_000).build())
        ).containsExactlyInAnyOrder(employees.get(0), employees.get(1), employees.get(2), employees.get(4));
        assertThat(
            finder.findBy(EmployeeQuery.builder().salaryToExclusive(40_000).build())
        ).isEmpty();
    }

    @Test
    void findsEmployeeByGender() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().gender(Gender.MALE).build())
        ).containsExactlyInAnyOrder(employees.get(0), employees.get(2), employees.get(3));
        assertThat(
            finder.findBy(EmployeeQuery.builder().gender(Gender.FEMALE).build())
        ).containsExactlyInAnyOrder(employees.get(1), employees.get(4));
    }

    @Test
    void findsEmployeeByDepartments() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().departments(Set.of(BUSINESS)).build())
        ).containsExactlyInAnyOrder(employees.get(0), employees.get(1), employees.get(3));
        assertThat(
            finder.findBy(EmployeeQuery.builder().departments(Set.of()).build())
        ).isEmpty();
        assertThat(
            finder.findBy(EmployeeQuery.builder().departments(Set.of(BUSINESS, HR)).build())
        ).containsExactlyInAnyOrder(employees.get(0), employees.get(1), employees.get(3));
        assertThat(
            finder.findBy(EmployeeQuery.builder().departments(Set.of(BOARD)).build())
        ).isEmpty();
    }

    @Test
    void findsEmployeesByCombinationOfParams() {
        assertThat(
            finder.findBy(EmployeeQuery.builder().build())
        ).containsAll(employees);
        assertThat(
            finder.findBy(EmployeeQuery.builder()
                .firstNamePattern(compile("J.*"))
                .lastNamePattern(compile(".*i.*"))
                .departments(Set.of(IT))
                .build()
            )
        ).containsExactly(employees.get(4));
        assertThat(
            finder.findBy(EmployeeQuery.builder()
                .firstNamePattern(compile("J.*"))
                .lastNamePattern(compile(".*i.*"))
                .departments(Set.of(IT))
                .build()
            )
        ).containsExactly(employees.get(4));
        assertThat(
            finder.findBy(EmployeeQuery.builder()
                .departments(Set.of(BUSINESS))
                .ageFromInclusive(20).ageToExclusive(45)
                .salaryFromInclusive(100_000)
                .gender(Gender.MALE)
                .build()
            )
        ).containsExactly(employees.get(0));
        assertThat(
            finder.findBy(EmployeeQuery.builder()
                .departments(Set.of(BUSINESS))
                .ageFromInclusive(20).ageToExclusive(45)
                .salaryFromInclusive(100_000)
                .gender(Gender.MALE)
                .lastNamePattern(compile("Xyz"))
                .build()
            )
        ).isEmpty();
    }

    @Test
    void findsBasicEmployeeDataByCombinationOfParams() {
        assertThat(
            finder.findBasicEmployeeDataBy(EmployeeQuery.builder()
                .firstNamePattern(compile("J.*"))
                .lastNamePattern(compile(".*i.*"))
                .departments(Set.of(IT))
                .build()
            )
        ).containsExactly(new BasicEmployeeData(employees.get(4)));
        assertThat(
            finder.findBasicEmployeeDataBy(EmployeeQuery.builder()
                .firstNamePattern(compile("J.*"))
                .lastNamePattern(compile(".*i.*"))
                .departments(Set.of(IT))
                .build()
            )
        ).containsExactly(new BasicEmployeeData(employees.get(4)));
        assertThat(
            finder.findBasicEmployeeDataBy(EmployeeQuery.builder()
                .departments(Set.of(BUSINESS))
                .ageFromInclusive(20).ageToExclusive(45)
                .salaryFromInclusive(100_000)
                .gender(Gender.MALE)
                .build()
            )
        ).containsExactly(new BasicEmployeeData(employees.get(0)));
        assertThat(
            finder.findBasicEmployeeDataBy(EmployeeQuery.builder()
                .departments(Set.of(BUSINESS))
                .ageFromInclusive(20).ageToExclusive(45)
                .salaryFromInclusive(100_000)
                .gender(Gender.MALE)
                .lastNamePattern(compile("Xyz"))
                .build()
            )
        ).isEmpty();
    }

    @Test
    void returnsAverageSalary() {
        assertThat(finder.avgSalary()).contains(510_000 / 5.0);
        assertThat(new EmployeesFinder(List.of()).avgSalary()).isEmpty();
        assertThat(new EmployeesFinder(List.of(employees.get(0), employees.get(1))).avgSalary()).contains(70_000.0);
    }

    @Test
    void returnsMostCommonFirstName() {
        assertThat(new EmployeesFinder(List.of()).mostCommonFirstName()).isEmpty();
        assertThat(new EmployeesFinder(List.of(
            employeeWithFirstName("John"),
            employeeWithFirstName("John"),
            employeeWithFirstName("Jane"),
            employeeWithFirstName("Jane"),
            employeeWithFirstName("Jimi")
        )).mostCommonFirstName()).contains("John");
        assertThat(new EmployeesFinder(List.of(
            employeeWithFirstName("John"),
            employeeWithFirstName("Jane"),
            employeeWithFirstName("Jane"),
            employeeWithFirstName("Jimi")
        )).mostCommonFirstName()).contains("Jane");
    }

    @Test
    void returnsHighestSalary() {
        assertThat(new EmployeesFinder(List.of()).highestSalary()).isEmpty();
        assertThat(finder.highestSalary()).contains(200_000);
        assertThat(new EmployeesFinder(List.of(employees.get(0), employees.get(1))).highestSalary()).contains(100_000);
    }

    @Test
    void returnsLowestSalariesPerDepartment() {
        assertThat(new EmployeesFinder(List.of()).lowestSalariesPerDepartment()).isEmpty();
        assertThat(finder.lowestSalariesPerDepartment())
            .containsEntry(ADMINISTRATION, 90_000)
            .containsEntry(HR, 40_000)
            .containsEntry(BUSINESS, 40_000)
            .containsEntry(IT, 80_000);
    }

    @Test
    void returnsDepartmentsOfEmployeesMatchingQuery() {
        assertThat(finder.departmentsOfEmployeesMatching(
            EmployeeQuery.builder()
                .firstNamePattern(compile("J.*"))
                .salaryFromInclusive(10_000)
                .salaryToExclusive(200_000)
                .build()
        )).contains(BUSINESS, HR, IT);

        assertThat(finder.departmentsOfEmployeesMatching(
            EmployeeQuery.builder()
                .salaryFromInclusive(50_000)
                .salaryToExclusive(200_000)
                .gender(Gender.FEMALE)
                .build()
        )).contains(IT);

        assertThat(finder.departmentsOfEmployeesMatching(
            EmployeeQuery.builder()
                .departments(Set.of(IT, HR, ADMINISTRATION))
                .build()
        )).contains(IT, HR, ADMINISTRATION);
    }

    @Test
    void returnsEmployeesInDepartments() {
        assertThat(finder.employeesInDepartments()).containsExactlyInAnyOrder(
            new EmployeesInDepartment(
                BUSINESS, List.of(employees.get(0), employees.get(1), employees.get(3)),
                new BigDecimal("45.33"), new BigDecimal("113333.33")
            ),
            new EmployeesInDepartment(
                HR, List.of(employees.get(1)),
                new BigDecimal("35.00"), new BigDecimal("40000.00")
            ),
            new EmployeesInDepartment(
                ADMINISTRATION, List.of(employees.get(2), employees.get(3)),
                new BigDecimal("41.50"), new BigDecimal("145000.00")
            ),
            new EmployeesInDepartment(
                IT, List.of(employees.get(2), employees.get(4)),
                new BigDecimal("32.50"), new BigDecimal("85000.00")
            )
        );
    }

    Employee employeeWithFirstName(String name) {
        return new Employee(
            UUID.randomUUID().toString(),
            name,
            "Any",
            20,
            20_000,
            Gender.MALE,
            Set.of()
        );
    }
}
