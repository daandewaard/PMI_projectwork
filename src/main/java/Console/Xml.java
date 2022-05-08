package Console;

import java.io.*;
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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Xml {
    private static final String FILENAME = "students.xml";

    public static Document getXmlDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

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

            String id = element.getAttributes().getNamedItem("id").getTextContent();
            String name = element.getElementsByTagName("name").item(0).getTextContent();
            String dob = element.getElementsByTagName("dateOfBirth").item(0).getTextContent();
            students.add(new String[]{id, name, dob});
        }

        return students;
    }

    public static Node getStudent(int searchingFor, NodeList studentsList){
        Node student = null;
        for (int i = 0; i < studentsList.getLength(); i++) {
            student = studentsList.item(i);
            int studentId = Integer.parseInt(student.getAttributes().getNamedItem("id").getTextContent());
            if (studentId == searchingFor) {
                return student;
            }
        }

        return student;
    }

    public static ArrayList<String[]> getStudentGrades(int choice) throws ParserConfigurationException {
        NodeList studentsList = studentsList();
        Node student = getStudent(choice, studentsList);
        NodeList studentGrades = student.getChildNodes();

        ArrayList<String[]> grades = new ArrayList<String[]>();

        for (int j = 0; j < studentGrades.getLength(); j++) {
            Node node = studentGrades.item(j);
            NodeList childNodes = node.getChildNodes();
            String subject = "";
            String mark = "";
            boolean isGrade = false;
            for (int k = 0; k < childNodes.getLength(); k++) {
                if (childNodes.item(k).getNodeName().equals("mark")) {
                    mark = childNodes.item(k).getTextContent();
                    isGrade = true;
                }
                if (childNodes.item(k).getNodeName().equals("subject")) {
                    subject = childNodes.item(k).getTextContent();
                }

            }
            if (isGrade) {
                grades.add(new String[]{subject, mark});
            }
        }

        return grades;
    }

    public static void createStudent(String name, String dob) throws ParserConfigurationException {
        try {
            File xmlFile = new File("students.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);
            NodeList studentsList = studentsList();

            Element documentElement = document.getDocumentElement();

            Element textNode = document.createElement("name");
            textNode.setTextContent(name);

            Element textNode1 = document.createElement("dateOfBirth");
            textNode1.setTextContent(dob);

            Element nodeElement = document.createElement("student");

            String studentId = "";
            for (int i = 0; i < studentsList.getLength(); i++) {
                Node student = studentsList.item(i);
                if(i == studentsList().getLength() - 1){
                    studentId = student.getAttributes().getNamedItem("id").getTextContent();
                }
            }

            nodeElement.setAttribute("id", String.valueOf(Integer.parseInt(studentId) + 1));

            nodeElement.appendChild(textNode);
            nodeElement.appendChild(textNode1);

            documentElement.appendChild(nodeElement);
            document.replaceChild(documentElement, documentElement);

            try (FileOutputStream output =
                         new FileOutputStream("students.xml")) {
                writeXml(document, output);
            }catch (IOException | TransformerException e) {
                e.printStackTrace();
            }

            System.out.println("Succesfully added new student " + name);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void editStudent(String id, String name, String dob) throws ParserConfigurationException {
        Document doc = getXmlDocument();
        NodeList studentsList = doc.getElementsByTagName("student");
        Node student = getStudent(Integer.parseInt(id), studentsList);
        assert student != null;

                NodeList childNodes = student.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node item = childNodes.item(j);

                    if("name".equalsIgnoreCase(item.getNodeName())){
                        item.setTextContent(name);
                    }
                    if("dateOfBirth".equalsIgnoreCase(item.getNodeName())){
                        item.setTextContent(dob);
                    }
                }


        try (FileOutputStream output =
                     new FileOutputStream("students.xml")) {
            writeXml(doc, output);
        }catch (IOException | TransformerException e) {
            e.printStackTrace();
    }}
    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException, UnsupportedEncodingException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

         Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

    public static void addGrade(String id, String subject, String mark) throws ParserConfigurationException {
        Document doc = getXmlDocument();
        NodeList studentsList = doc.getElementsByTagName("student");
        Node student = getStudent(Integer.parseInt(id), studentsList);
        assert student != null;

        Element gradeElement = doc.createElement("grade");
        Element subjectElement = doc.createElement("subject");
        subjectElement.setTextContent(subject);
        Element markEle = doc.createElement("mark");
        markEle.setTextContent(mark);
        gradeElement.appendChild(subjectElement);
        gradeElement.appendChild(markEle);
        student.appendChild(gradeElement);

        try (FileOutputStream output =
                     new FileOutputStream("students.xml")) {
            writeXml(doc, output);
        }catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void deleteStudent(String choice) throws ParserConfigurationException {
        Document doc = getXmlDocument();
        NodeList studentsList = doc.getElementsByTagName("student");
        Node student = getStudent(Integer.parseInt(choice), studentsList);
        assert student != null;
        student.getParentNode().removeChild(student);


        try (FileOutputStream output =
                     new FileOutputStream("students.xml")) {
            writeXml(doc, output);
        }catch (IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
