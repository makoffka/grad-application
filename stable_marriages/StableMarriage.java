//CS 211
//Anastasia Kurakova
//Assignment 4 : Stable Marriages

// This program reads an input file of preferences and find a stable marriage
// scenario.  The algorithm gives preference to either men or women depending
// upon whether this call is made from main:
//      makeMatches(men, women);
// or whether this call is made:
//      makeMatches(women, men);

import java.io.*;
import java.util.*;

public class StableMarriage {

    public static final String LIST_END = "END";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.print("What is the input file? ");
        String fileName = console.nextLine();
        Scanner input = new Scanner(new File(fileName));
        System.out.println();

        List<Person> men = readHalf(input);
        List<Person> women = readHalf(input);
        makeMatches(men, women);
        writeList(men, women, "Matches for men");
        writeList(women, men, "Matches for women");
    }

    public static Person readPerson(String line) {
        int index = line.indexOf(":");
        Person result = new Person(line.substring(0, index));
        Scanner data = new Scanner(line.substring(index + 1));
        while (data.hasNextInt()) {
            result.addChoice(data.nextInt());
        }
        return result;
    }

    public static List<Person> readHalf(Scanner input) {
        List<Person> result = new ArrayList<Person>();
        String line = input.nextLine();
        while (!line.equals(LIST_END)) {
            result.add(readPerson(line));
            line = input.nextLine();
        }
        return result;
    }

    /* In this method we create an algorithm using which the stable marriage program is able to make 
    stable connections between partners. This specific algorithm favors the men and will consider their
    preferences in making the decision.
     */
    public static void makeMatches(List<Person> list1, List<Person> list2) {

        // First step is to set each person to be free of partners.
        for (Person woman : list2) {
            woman.erasePartner();
        }
        for (Person man : list1) {
            man.erasePartner();

        }
        // I  am doing this set up to start the loop with the first person. The rest of the iteration will be handled
        //inside the while loop
        int m = 0;
        Person man = list1.get(m);
        
        //The man will enter this while loop if it has a non empty preference list and doesn't have a partner.
        while (man.hasChoices() && !man.hasPartner()) {
            
            // Woman who is the first choice on the man's list
            int w = man.getFirstChoice();
            Person woman = list2.get(w);
            // if the woman of interest already has a partner - set him free.
            if (woman.hasPartner()) {
                int p = woman.getPartner();
                list1.get(p).erasePartner();
            }
            //Setting the man and the woman engaged to each other. 
            man.setPartner(w);
            woman.setPartner(m);
            //creating an array list of successors (men who stand after the man of choice on the woman's list of preferences.) 
            List<Integer> successors = new ArrayList<Integer>();
            for (int i = woman.getChoices().size() - 1; i >= woman.getPartnerRank(); i--) {
                successors.add(woman.getChoices().get(i));
                //removing successors from the list of the engaged woman's preferences.
                woman.getChoices().remove(i);
            }
            // Removing the engaged woman from the preference lists of the successors.
            for (int successor : successors) {
                int x = list1.get(successor).getChoices().indexOf(w);
                list1.get(successor).getChoices().remove(x);
            }

            // I created this For-loop to allow men who did not find Partners by the end of the first While-loop
            //to return there and go through the loop again.
            for (Person freeman : list1) {
                
                if (!freeman.hasPartner()) {
                    man = freeman;
                    m = list1.indexOf(man);
                    break;
                }
            }

        }

    }

    public static void writeList(List<Person> list1, List<Person> list2,
            String title) {
        System.out.println(title);
        System.out.println("Name           Choice  Partner");
        System.out.println("--------------------------------------");
        int sum = 0;
        int count = 0;
        for (Person p : list1) {
            System.out.printf("%-15s", p.getName());
            if (!p.hasPartner()) {
                System.out.println("  --    nobody");
            } else {
                int rank = p.getPartnerRank();
                sum += rank;
                count++;
                System.out.printf("%4d    %s\n", rank,
                        list2.get(p.getPartner()).getName());
            }
        }
        System.out.println("Mean choice = " + (double) sum / count);
        System.out.println();
    }
}
