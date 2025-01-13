package org.example.projectsigma6.services;

import com.mongodb.client.MongoClient;
import org.bson.types.ObjectId;
import org.example.projectsigma6.dao.EmployeeDao;
import org.example.projectsigma6.models.Employee;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
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

    public Employee getEmployeeById(String id) {
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

    public Employee getEmployeeByUsername(String username) {
        try {
            Employee employee = employeeDao.getEmployeeByUsername(username);
            if (employee == null) {
                throw new Exception("Employee with username " + username + " not found");
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
            System.err.println("Error in EmployeeService while adding employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee removeEmployee(Employee employee) {
        try {
            Employee returnEmployee = employeeDao.removeEmployee(employee);
            if (returnEmployee == null) {
                throw new Exception("Failed to remove employee");
            }
            return returnEmployee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeService while removing employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Employee updateEmployee(Employee employee) {
        try {
            Employee returnEmployee = employeeDao.updateEmployee(employee);
            if (returnEmployee == null) {
                throw new Exception("Failed to update employee");
            }
            return returnEmployee;
        } catch (Exception e) {
            System.err.println("Error in EmployeeService while updating employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Password
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public String hashPassword(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;
            byte[] hash = digest.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

}
