package com.example.final_case_social_web.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class JAXBExample {

    public static void main(String[] args) {

        // Create a Person object
        Person person = new Person();
        person.setId("1");
        person.setName("John Doe");
        person.setAge(30);
        person.setDog(new Dog("bine ", "red"));

        try {
            // Marshal the Person object to XML
            JAXBContext context = JAXBContext.newInstance(Person.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            StringWriter writer = new StringWriter();
            marshaller.marshal(person, writer);
            String xml = writer.toString();
            System.out.println("XML representation:\n" + xml);

            // Unmarshal the XML back to a Person object
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Person unmarshalledPerson = (Person) unmarshaller.unmarshal(new StringReader(xml));
            System.out.println("\nUnmarshalled Person:");
            System.out.println("Name: " + unmarshalledPerson.getName());
            System.out.println("Age: " + unmarshalledPerson.getAge());
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
