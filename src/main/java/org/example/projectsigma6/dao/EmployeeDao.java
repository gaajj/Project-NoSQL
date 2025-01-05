package org.example.projectsigma6.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.application.Platform;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.example.projectsigma6.models.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDao extends BaseDao<Employee> {

    public EmployeeDao(MongoClient mongoClient, String databaseName, String collectionName) {
        super(mongoClient, databaseName, collectionName, Employee.class);
    }

    public List<Employee> getAllEmployees() {
        try {
            List<Employee> employees = collection.find().into(new ArrayList<>());
            System.out.println("Employees retreived successfully");
            return employees;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao retrieving employee by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee getEmployeeById(String id) {
        try {
            Employee employee = collection.find(Filters.eq("_id", id)).first();
            System.out.println("Employee retrieved successfully: " + employee.toStringShort());
            return employee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao retrieving employee by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee getEmployeeByUsername(String username) {
        try {
            Employee employee = collection.find(Filters.eq("username", username)).first();
            System.out.println("Employee retrieved successfully: " + employee.toStringShort());
            return employee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao retrieving employee by username: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee addEmployee(Employee employee) {
        try {
            collection.insertOne(employee);
            System.out.println("Employee added successfully: " + employee.toStringShort());
            return employee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao adding employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee removeEmployee(Employee employee) {
        try {
            collection.updateOne(
                    Filters.eq("_id", employee.getId().toString()),
                    Updates.set("inEmployment", false)
            );
            System.out.println("Employee removed successfully: " + employee.toStringShort());
            return employee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao removing employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee updateEmployee(Employee employee) {
        try {
            Employee existingEmployee = getEmployeeById(employee.getId().toString());
            List<Bson> updates = new ArrayList<>();

            if (!existingEmployee.getUsername().equals(employee.getUsername())) {
                updates.add(Updates.set("username", employee.getUsername()));
            }
            if (!existingEmployee.getFirstName().equals(employee.getFirstName())) {
                updates.add(Updates.set("firstName", employee.getFirstName()));
            }
            if (!existingEmployee.getLastName().equals(employee.getLastName())) {
                updates.add(Updates.set("lastName", employee.getLastName()));
            }
            if (!existingEmployee.getEmail().equals(employee.getEmail())) {
                updates.add(Updates.set("email", employee.getEmail()));
            }
            if (!existingEmployee.getPhoneNumber().equals(employee.getPhoneNumber())) {
                updates.add(Updates.set("phoneNumber", employee.getPhoneNumber()));
            }
            if (!existingEmployee.getEmployeeType().equals(employee.getEmployeeType())) {
                updates.add(Updates.set("employeeType", employee.getEmployeeType()));
            }
            if (!existingEmployee.getLocation().equals(employee.getLocation())) {
                updates.add(Updates.set("location", employee.getLocation()));
            }
            if (existingEmployee.isInEmployment() == employee.isInEmployment()) {
                updates.add(Updates.set("inEmployment", employee.isInEmployment()));
            }

            if (!updates.isEmpty()) {
                collection.updateOne(
                        Filters.eq("_id", employee.getId().toString()),
                        Updates.combine(updates)
                );
            }
            System.out.println("Employee updated successfully: " + employee.toStringShort());
            return employee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao updating employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
