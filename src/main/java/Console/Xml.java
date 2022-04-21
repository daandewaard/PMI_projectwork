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
import javax.xml.transform.stream.StreamSource;

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

            /*This isn't the perfect solution yet, will cause problems when a student in the middle of the file
            gets deleted. Need to look into this, should find the id of the student at the end of the file,
            or save the most recent id in some kind of file (so that id's will never be re-used)
             */

            nodeElement.setAttribute("id", String.valueOf(studentsList.getLength() + 1));

            nodeElement.appendChild(textNode);
            nodeElement.appendChild(textNode1);

            documentElement.appendChild(nodeElement);
            document.replaceChild(documentElement, documentElement);

            document.getDocumentElement().normalize();

            Transformer tFormer =
                    TransformerFactory.newInstance().newTransformer();

            tFormer.setOutputProperty(OutputKeys.METHOD, "xml");

            Source source = new DOMSource(document);
            Result result = new StreamResult(xmlFile);

            tFormer.transform(source, result);

            System.out.println("Succesfully added new student " + name);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void editStudent(String id, String name, String dob) throws ParserConfigurationException {
        Document doc = getXmlDocument();
        NodeList studentsList = doc.getElementsByTagName("student");

        for (int i = 0; i < studentsList.getLength(); i++) {
            Node student = studentsList.item(i);

            String studentId = student.getAttributes().getNamedItem("id").getTextContent();
            if(id.equals(studentId.trim())){
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

        // The default add many empty new line, not sure why?
        // https://mkyong.com/java/pretty-print-xml-with-java-dom-and-xslt/
         Transformer transformer = transformerFactory.newTransformer();

        // pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

}
