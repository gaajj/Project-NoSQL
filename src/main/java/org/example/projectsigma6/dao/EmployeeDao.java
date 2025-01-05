package org.example.projectsigma6.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import javafx.application.Platform;
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
            return collection.find().into(new ArrayList<>());
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao retrieving employee by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee getEmployeeById(String id) {
        try {
            return collection.find(Filters.eq("_id", id)).first();
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao retrieving employee by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee getEmployeeByUsername(String username) {
        try {
            return collection.find(Filters.eq("username", username)).first();
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao retrieving employee by username: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee addEmployee(Employee employee) {
        try {
            collection.insertOne(employee);
            System.out.println("Employee added: " + employee);
            return employee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao adding employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
