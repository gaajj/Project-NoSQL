package org.example.projectsigma6.services;

import com.mongodb.client.MongoClient;
import org.bson.types.ObjectId;
import org.example.projectsigma6.dao.EmployeeDao;
import org.example.projectsigma6.models.Employee;

import java.util.List;

public class EmployeeService {

    private final EmployeeDao employeeDao;

    public EmployeeService(MongoClient mongoClient, String databaseName) {
        this.employeeDao = new EmployeeDao(mongoClient, databaseName, "Employee");
    }

    public List<Employee> getAllEmployees() {
        try {
            return employeeDao.getAllEmployees();
        } catch (Exception e) {
            System.err.println("Error in EmployeeService while fetching employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee getEmployee(int id) {
        try {
            Employee employee = employeeDao.getEmployeeById(id);
            if (employee == null) {
                throw new Exception("Employee with id " + id + " not found");
            }
            return employee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeService while fetching employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee addEmployee(Employee employee) {
        try {
            Employee returnEmployee = employeeDao.addEmployee(employee);
            if (returnEmployee == null) {
                throw new Exception("Failed to add employee");
            }
            return returnEmployee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeService while fetching employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
