package org.example.projectsigma6.models;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.example.projectsigma6.models.enums.EmployeeType;
import org.example.projectsigma6.models.enums.Location;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Employee {

    @BsonId
    private ObjectId id;
    private String username;
    private String hashedPassword;
    private String salt;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private EmployeeType employeeType;
    private Location location;
    private boolean inEmployment;

    public Employee() {}

    @BsonCreator
    public Employee(@BsonProperty("_id") ObjectId id,
                    @BsonProperty("username") String username,
                    @BsonProperty("hashedPassword") String hashedPassword,
                    @BsonProperty("salt") String salt,
                    @BsonProperty("firstName") String firstName,
                    @BsonProperty("lastName") String lastName,
                    @BsonProperty("email") String email,
                    @BsonProperty("phoneNumber") String phoneNumber,
                    @BsonProperty("employeeType") EmployeeType employeeType,
                    @BsonProperty("location") Location location,
                    @BsonProperty("inEmployment") boolean inEmployment) {
        this.id = id;
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.employeeType = employeeType;
        this.location = location;
        this.inEmployment = inEmployment;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isInEmployment() {
        return inEmployment;
    }

    public void setInEmployment(boolean inEmployment) {
        this.inEmployment = inEmployment;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", employeeType='" + employeeType + '\'' +
                ", location='" + location + '\'' +
                ", inEmployment=" + inEmployment +
                '}';
    }

    public String toStringShort(){
        return "Employee{" +
                "id='" +id +
                "', username='" + username +
                "'}";
    }

}
