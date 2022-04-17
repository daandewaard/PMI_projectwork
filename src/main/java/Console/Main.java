package Console;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean wantsToContinue = true;
        while(wantsToContinue){
            Scanner choiceInput = new Scanner(System.in);
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
                    System.out.println("1");
                    break;
                case "2":
                    System.out.println("2");
                    break;
                case "3":
                    System.out.println("3");
                    break;
                case "4":
                    System.out.println("4");
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
}
