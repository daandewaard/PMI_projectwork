package Console;

import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Xml {
    private static final String FILENAME = "students.xml";

    public static Document getXmlDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            doc = db.parse(new File(FILENAME));

            doc.getDocumentElement().normalize();

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
    public static NodeList studentsList() throws ParserConfigurationException {
        Document doc = getXmlDocument();

        return doc.getElementsByTagName("student");
    }

    public static ArrayList<String[]> getStudentInfo() throws ParserConfigurationException {
        ArrayList<String[]> students = new ArrayList<String[]>();

        NodeList studentsList = studentsList();

        for(int i = 0; i < studentsList.getLength(); i++){
            Node node = studentsList.item(i);

            Element element = (Element) node;

            String name = element.getElementsByTagName("name").item(0).getTextContent();
            String dob = element.getElementsByTagName("dateOfBirth").item(0).getTextContent();
            students.add(new String[]{name, dob});
        }

        return students;
    }

    public static ArrayList<String[]> getStudentGrades(int choice) throws ParserConfigurationException {
        NodeList studentsList = studentsList();
        Node student = studentsList.item(choice);
        NodeList studentGrades = student.getChildNodes();

        ArrayList<String[]> grades = new ArrayList<String[]>();

        for(int i = 0; i < studentGrades.getLength(); i++){
            //System.out.println(studentGrades.item(choice).toString());
            Node node = studentGrades.item(i);
            NodeList childNodes = node.getChildNodes();
            String subject = "";
            String mark = "";
            boolean isGrade = false;
            for(int j = 0; j < childNodes.getLength(); j++){
                if(childNodes.item(j).getNodeName().equals("mark")){
                    mark = childNodes.item(j).getTextContent();
                    isGrade = true;
                }
                if(childNodes.item(j).getNodeName().equals("subject")){
                    subject = childNodes.item(j).getTextContent();
                }

            }
            if(isGrade){
                grades.add(new String[]{subject, mark});
            }
        }

        return grades;
    }
}
