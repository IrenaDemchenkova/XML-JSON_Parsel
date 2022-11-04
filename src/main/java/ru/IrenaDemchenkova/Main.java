package ru.IrenaDemchenkova;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(list);
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        List<Employee> employees = parseXML("data.xml");
        writeString(listToJson(employees), "data2.json");
    }

    public static List<Employee> parseXML(String fileName) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;

        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        try {
            doc = builder.parse(new File(fileName));
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        List<Employee> employees = new ArrayList<>();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element element = (Element) node;

                long id = Long.parseLong(getElementValue(element, "id"));
                String firstName = getElementValue(element, "firstName");
                String lastName = getElementValue(element, "lastName");
                String country = getElementValue(element, "country");
                int age = Integer.parseInt(getElementValue(element, "age"));

                Employee employee = new Employee(id, firstName, lastName, country, age);
                employees.add(employee);
            }
        }

        return employees;
    }

    public static String getElementValue(Element element, String path) {
        return element.getElementsByTagName(path).item(0).getTextContent();
    }
}