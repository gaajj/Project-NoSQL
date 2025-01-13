package org.example.projectsigma6.tests;

import org.bson.types.ObjectId;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.enums.EmployeeType;
import org.example.projectsigma6.models.enums.Location;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;

import java.util.List;

public class ExampleEmployee {

    private static EmployeeService employeeService;

    public static void main(String[] args) {
        employeeService = ServiceManager.getInstance().getEmployeeService();

    }

    public static void exampleGetEmployeeById() {
        Employee employee = employeeService.getEmployeeById("677594186f6a1620428e1d73");
        System.out.println("Employee for id: '" + employee.getId() + "', Full name: " + employee.getFirstName() + " " + employee.getLastName());
    }

    public static void exampleGetEmployeeByUsername() {
        Employee employee = employeeService.getEmployeeByUsername("jan.de.molen");
        System.out.println("Employee for username: '" + employee.getUsername() + "', ID: " + employee.getId() + ", Full name: " + employee.getFirstName() + " " + employee.getLastName() + ", " + employee.getEmail());
    }

    public static void getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee employee : employees) {
            System.out.println(employee.toString());
        }
    }

    public static void exampleAddEmployee() {
        String salt = EmployeeService.generateSalt();
        String password = employeeService.hashPassword("password123", salt);

        Employee employee = new Employee();
        employee.setId(new ObjectId());
        employee.setUsername("jan.de.molen");
        employee.setHashedPassword(password);
        employee.setSalt(salt);
        employee.setFirstName("Jan");
        employee.setLastName("de Molen");
        employee.setEmail("jandemolen@gmail.com");
        employee.setPhoneNumber("+31 20 123 4567");
        employee.setEmployeeType(EmployeeType.REGULAR);
        employee.setLocation(Location.ROTTERDAM);
        employee.setInEmployment(true);

        employeeService.addEmployee(employee);
    }

    public static void exampleRemoveEmployee() {
        Employee employee = employeeService.getEmployeeByUsername("jan.de.molen");
        employeeService.removeEmployee(employee);
    }

    public static void exampleUpdateEmployee() {
        Employee employee = employeeService.getEmployeeByUsername("jan.de.molen");

        employee.setEmail("jandemolen@hotmail.com");

        employeeService.updateEmployee(employee);
    }

    public static void add5Employees() {
        // Employee 1
        String salt1 = EmployeeService.generateSalt();
        String password1 = employeeService.hashPassword("password1", salt1);
        Employee employee1 = new Employee();
        employee1.setId(new ObjectId());
        employee1.setUsername("john.doe");
        employee1.setHashedPassword(password1);
        employee1.setSalt(salt1);
        employee1.setFirstName("John");
        employee1.setLastName("Doe");
        employee1.setEmail("johndoe1@example.com");
        employee1.setPhoneNumber("+31 20 123 4567");
        employee1.setEmployeeType(EmployeeType.REGULAR);
        employee1.setLocation(Location.ROTTERDAM);
        employee1.setInEmployment(true);
        employeeService.addEmployee(employee1);

        // Employee 2
        String salt2 = EmployeeService.generateSalt();
        String password2 = employeeService.hashPassword("password2", salt2);
        Employee employee2 = new Employee();
        employee2.setId(new ObjectId());
        employee2.setUsername("jane.smith");
        employee2.setHashedPassword(password2);
        employee2.setSalt(salt2);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmail("janesmith@example.com");
        employee2.setPhoneNumber("+31 20 234 5678");
        employee2.setEmployeeType(EmployeeType.SERVICEDESK);
        employee2.setLocation(Location.UTRECHT);
        employee2.setInEmployment(true);
        employeeService.addEmployee(employee2);

        // Employee 3
        String salt3 = EmployeeService.generateSalt();
        String password3 = employeeService.hashPassword("password3", salt3);
        Employee employee3 = new Employee();
        employee3.setId(new ObjectId());
        employee3.setUsername("bob.brown");
        employee3.setHashedPassword(password3);
        employee3.setSalt(salt3);
        employee3.setFirstName("Bob");
        employee3.setLastName("Brown");
        employee3.setEmail("bobbrown@example.com");
        employee3.setPhoneNumber("+31 20 345 6789");
        employee3.setEmployeeType(EmployeeType.SERVICEDESK);
        employee3.setLocation(Location.AMSTERDAM);
        employee3.setInEmployment(true);
        employeeService.addEmployee(employee3);

        // Employee 4
        String salt4 = EmployeeService.generateSalt();
        String password4 = employeeService.hashPassword("password4", salt4);
        Employee employee4 = new Employee();
        employee4.setId(new ObjectId());
        employee4.setUsername("alice.johnson");
        employee4.setHashedPassword(password4);
        employee4.setSalt(salt4);
        employee4.setFirstName("Alice");
        employee4.setLastName("Johnson");
        employee4.setEmail("alicejohnson@example.com");
        employee4.setPhoneNumber("+31 20 456 7890");
        employee4.setEmployeeType(EmployeeType.REGULAR);
        employee4.setLocation(Location.AMSTERDAM);
        employee4.setInEmployment(true);
        employeeService.addEmployee(employee4);

        // Employee 5
        String salt5 = EmployeeService.generateSalt();
        String password5 = employeeService.hashPassword("password5", salt5);
        Employee employee5 = new Employee();
        employee5.setId(new ObjectId());
        employee5.setUsername("charlie.williams");
        employee5.setHashedPassword(password5);
        employee5.setSalt(salt5);
        employee5.setFirstName("Charlie");
        employee5.setLastName("Williams");
        employee5.setEmail("charliewilliams@example.com");
        employee5.setPhoneNumber("+31 20 567 8901");
        employee5.setEmployeeType(EmployeeType.REGULAR);
        employee5.setLocation(Location.ROTTERDAM);
        employee5.setInEmployment(false);
        employeeService.addEmployee(employee5);
    }

}
