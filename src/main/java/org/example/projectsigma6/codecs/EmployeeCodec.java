package org.example.projectsigma6.codecs;

import org.bson.BsonReader;
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
        reader.readStartDocument();
        ObjectId id = new ObjectId(reader.readString("_id"));
        String username = reader.readString("username");
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
