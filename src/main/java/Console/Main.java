package Console;

import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    static Scanner choiceInput = new Scanner(System.in);

    public static void main(String[] args) throws ParserConfigurationException {
        boolean wantsToContinue = true;
        while(wantsToContinue){
            System.out.println("Welcome, please select what you would like to do");
            System.out.println("1: Show student information");
            System.out.println("2: Edit or remove student");
            System.out.println("3: Add grades");
            System.out.println("4: Create new student");
            System.out.println("");
            System.out.println("Or type 'x' to exit the application");

            String choice = choiceInput.nextLine();

            switch(choice){
                case "1":
                    showStudents();
                    break;
                case "2":
                    editStudent();
                    break;
                case "3":
                    addGrade();
                    break;
                case "4":
                    createStudent();
                    break;
                case "x":
                case "X":
                    wantsToContinue = false;
                    break;
                default:
                    System.out.println("Invalid input");
                    break;
            }

        }

    }

    private static void addGrade() throws ParserConfigurationException {
        ArrayList<String[]> students = Xml.getStudentInfo();

        for(int i = 0; i < students.size(); i++){
            System.out.println(students.get(i)[0] + ": " + students.get(i)[1] + ", " + students.get(i)[2]);
        }
        System.out.println("Please type the number of the student you would like to add a grade to");
        System.out.println("Or type 'x' to return to the previous menu");
        String choice = choiceInput.nextLine();
        if(!Objects.equals(choice, "x") && !Objects.equals(choice, "X")) {
            System.out.println("Please enter the name of the subject.");
            String subject = choiceInput.nextLine();

            System.out.println("Please enter the grade.");
            String mark = choiceInput.nextLine();

            Xml.addGrade(choice, subject, mark);
        }
    }

    private static void editStudent() throws ParserConfigurationException {
        ArrayList<String[]> students = Xml.getStudentInfo();

        for(int i = 0; i < students.size(); i++){
            System.out.println((students.get(i)[0]) + ": " + students.get(i)[1] + ", " + students.get(i)[2]);
        }
        System.out.println("Please type the number of the student you would like to edit");
        System.out.println("Or type 'x' to return to the previous menu");
        String choice = choiceInput.nextLine();
        if(!Objects.equals(choice, "x") && !Objects.equals(choice, "X")) {
            System.out.println("Please enter the new name for this student.");
            String name = choiceInput.nextLine();

            System.out.println("Please enter the new date of birth for this student.");
            System.out.println("Format: 28-07-2002");
            String dob = choiceInput.nextLine();

            Xml.editStudent(choice, name, dob);
        }
    }

    private static void createStudent() throws ParserConfigurationException {
        System.out.println("Please enter the name of the student");
        String name = choiceInput.nextLine();

        System.out.println("Please enter the date of birth of the student in the following format: ");
        System.out.println("28-07-2002");
        String dob = choiceInput.nextLine();

        Xml.createStudent(name, dob);
    }

    private static void showStudents() throws ParserConfigurationException {
        ArrayList<String[]> students = Xml.getStudentInfo();

        for(int i = 0; i < students.size(); i++){
            System.out.println((students.get(i)[0]) + ": " + students.get(i)[1] + ", " + students.get(i)[2]);
        }
        System.out.println("Please type the number of the student you would like to see the grades of");
        System.out.println("Or type 'x' to return to the previous menu");

        String choice = choiceInput.nextLine();
        if(!Objects.equals(choice, "x") && !Objects.equals(choice, "X")) {
            int intChoice = Integer.parseInt(choice);
            ArrayList<String[]> grades = Xml.getStudentGrades(intChoice - 1);

            for (int i = 0; i < grades.size(); i++) {
                System.out.println(grades.get(i)[0] + ": " + grades.get(i)[1]);
            }
            System.out.println("Press enter to return to the previous menu");
            choice = choiceInput.nextLine();
            showStudents();
        }

    }
}
