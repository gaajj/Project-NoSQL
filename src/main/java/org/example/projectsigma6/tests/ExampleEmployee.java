package org.example.projectsigma6.tests;

import org.bson.types.ObjectId;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;

import java.util.List;

public class ExampleEmployee {

    private static EmployeeService employeeService;

    public static void main(String[] args) {
        employeeService = ServiceManager.getInstance().getEmployeeService();

        System.out.println(getEmployeeById("67758ef25cc5194708fe6c22"));
    }

    public static Employee getEmployeeById(String id) {
        return employeeService.getEmployee(id);
    }

    public static void getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        for (Employee employee : employees) {
            System.out.println(employee.toString());
        }
    }

    public static void add5Employees() {
        ObjectId id1 = new ObjectId();
        Employee employee1 = new Employee(id1,
                "john.doe",                  // username
                "hashedPassword1",           // hashedPassword
                "salt1",                     // salt
                "John",                      // firstName
                "Doe",                       // lastName
                "johndoe1@example.com",      // email
                "+31 20 123 4567",           // phoneNumber
                "REGULAR",                   // employeeType
                "AMSTERDAM",                 // location
                true                         // inEmployment
        );

        ObjectId id2 = new ObjectId();
        Employee employee2 = new Employee(id2,
                "jane.smith",                // username
                "hashedPassword2",           // hashedPassword
                "salt2",                     // salt
                "Jane",                      // firstName
                "Smith",                     // lastName
                "janesmith@example.com",     // email
                "+31 20 234 5678",           // phoneNumber
                "MANAGER",                   // employeeType
                "ROTTERDAM",                 // location
                true                         // inEmployment
        );

        ObjectId id3 = new ObjectId();
        Employee employee3 = new Employee(id3,
                "bob.brown",                 // username
                "hashedPassword3",           // hashedPassword
                "salt3",                     // salt
                "Bob",                       // firstName
                "Brown",                     // lastName
                "bobbrown@example.com",      // email
                "+31 20 345 6789",           // phoneNumber
                "ADMIN",                     // employeeType
                "THE HAGUE",                 // location
                true                         // inEmployment
        );

        ObjectId id4 = new ObjectId();
        Employee employee4 = new Employee(id4,
                "alice.johnson",             // username
                "hashedPassword4",           // hashedPassword
                "salt4",                     // salt
                "Alice",                     // firstName
                "Johnson",                   // lastName
                "alicejohnson@example.com",  // email
                "+31 20 456 7890",           // phoneNumber
                "REGULAR",                   // employeeType
                "AMSTERDAM",                 // location
                true                         // inEmployment
        );

        ObjectId id5 = new ObjectId();
        Employee employee5 = new Employee(id5,
                "charlie.williams",          // username
                "hashedPassword5",           // hashedPassword
                "salt5",                     // salt
                "Charlie",                   // firstName
                "Williams",                  // lastName
                "charliewilliams@example.com", // email
                "+31 20 567 8901",           // phoneNumber
                "REGULAR",                   // employeeType
                "ROTTERDAM",                 // location
                false                        // inEmployment
        );

        employeeService.addEmployee(employee1);
        employeeService.addEmployee(employee2);
        employeeService.addEmployee(employee3);
        employeeService.addEmployee(employee4);
        employeeService.addEmployee(employee5);
    }

}
