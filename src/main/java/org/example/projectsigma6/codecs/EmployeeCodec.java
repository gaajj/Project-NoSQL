package org.example.projectsigma6.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.types.ObjectId;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.enums.EmployeeType;
import org.example.projectsigma6.models.enums.Location;

public class EmployeeCodec implements Codec<Employee> {

    @Override
    public Employee decode(BsonReader reader, org.bson.codecs.DecoderContext decoderContext) {
        reader.readStartDocument();

        ObjectId id = new ObjectId(reader.readString("_id"));
        String username = reader.readString("username");
        String hashedPassword = reader.readString("hashedPassword");
        String salt = reader.readString("salt");
        String firstName = reader.readString("firstName");
        String lastName = reader.readString("lastName");
        String email = reader.readString("email");
        String phoneNumber = reader.readString("phoneNumber");
        String employeeTypeString = reader.readString("employeeType"); // String
        EmployeeType employeeType = EmployeeType.valueOf(employeeTypeString); // String->Enum
        String locationString = reader.readString("location"); // String
        Location location = Location.valueOf(locationString); // String->Enum
        boolean inEmployment = reader.readBoolean("inEmployment");
        reader.readEndDocument();

        return new Employee(id, username, hashedPassword, salt, firstName, lastName, email, phoneNumber, employeeType, location, inEmployment);
    }

    public Employee decodeEmbedded(BsonReader reader) {
        ObjectId id = null;
        String username = null;
        String lastName = null;
        String firstName = null;
        String email = null;
        String phoneNumber = null;
        EmployeeType employeeType = null;
        Location location = null;
        boolean inEmployment = false;

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            switch (fieldName) {
                case "_id":
                    id = new ObjectId(reader.readString());
                    break;
                case "username":
                    username = reader.readString();
                    break;
                case "lastName":
                    lastName = reader.readString();
                    break;
                case "firstName":
                    firstName = reader.readString();
                    break;
                case "email":
                    email = reader.readString();
                    break;
                case "phoneNumber":
                    phoneNumber = reader.readString();
                    break;
                case "employeeType":
                    String employeeTypeString = reader.readString();
                    employeeType = EmployeeType.valueOf(employeeTypeString);
                    break;
                case "location":
                    String locationString = reader.readString();
                    location = Location.valueOf(locationString);
                    break;
                case "inEmployment":
                    inEmployment = reader.readBoolean();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.readEndDocument();

        if (id == null || username == null || firstName == null || lastName == null || email == null ||
                phoneNumber == null || employeeType == null || location == null) {
            throw new IllegalStateException("Missing required fields for Employee object");
        }

        return new Employee(id, username, firstName, lastName, email, phoneNumber, employeeType, location, inEmployment);
    }

    @Override
    public void encode(BsonWriter writer, Employee employee, org.bson.codecs.EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeString("_id", employee.getId().toString());
        writer.writeString("username", employee.getUsername());
        writer.writeString("hashedPassword", employee.getHashedPassword());
        writer.writeString("salt", employee.getSalt());
        writer.writeString("firstName", employee.getFirstName());
        writer.writeString("lastName", employee.getLastName());
        writer.writeString("email", employee.getEmail());
        writer.writeString("phoneNumber", employee.getPhoneNumber());
        writer.writeString("employeeType", employee.getEmployeeType().name()); // Enum->String
        writer.writeString("location", employee.getLocation().name()); // Enum->String
        writer.writeBoolean("inEmployment", employee.isInEmployment());

        writer.writeEndDocument();
    }

    public void encodeEmbedded(BsonWriter writer, Employee employee) {
        writer.writeStartDocument();

        writer.writeString("_id", employee.getId().toString());
        writer.writeString("username", employee.getUsername());
        writer.writeString("firstName", employee.getFirstName());
        writer.writeString("lastName", employee.getLastName());
        writer.writeString("email", employee.getEmail());
        writer.writeString("phoneNumber", employee.getPhoneNumber());
        writer.writeString("employeeType", employee.getEmployeeType().name()); // Enum->String
        writer.writeString("location", employee.getLocation().name()); // Enum->String
        writer.writeBoolean("inEmployment", employee.isInEmployment());

        writer.writeEndDocument();
    }

    @Override
    public Class<Employee> getEncoderClass() {
        return Employee.class;
    }

}
