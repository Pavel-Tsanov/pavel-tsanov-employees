/**
 * Solution(a bit slow) for the task to find the couple of employees from text file who have the most days working together.
 *
 * @author Pavel Tsanov
 * @version 1.0 2021-07-20
 */
package com.company;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Finds the solution by reading all the employees from the text file into a hashmap with keys- EmpID and values- list of all the lines with the corresponding key,
 * and then iterating through key to key to find the maximum number of mutual days between two keys.
 *
 * @param args command-line comments
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.print("Type a text file name to read from: ");
        Scanner in = new Scanner(System.in);
        String fileName = in.nextLine();
        InputStream is = Main.class.getResourceAsStream(fileName);


        Map<String, List<String>> mapEmployees = new HashMap<>();

        //try-with-resources statement where the file is open
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;

            //reads every line from the file
            while ((line = br.readLine()) != null) {
                int delimiterIndex = line.indexOf(',');
                String employeeID = line.substring(0, delimiterIndex);

                //adds to the list of a key in the map new ProjectID, DateFrom, DateTo
                if (mapEmployees.containsKey(employeeID)) {
                    mapEmployees.get(employeeID).add(line.substring(delimiterIndex + 2));
                }
                //adds to the map new key with a value of list- the current line of ProjectID, DateFrom, DateTo
                else {
                    List<String> newListValue = new ArrayList<>();
                    newListValue.add(line.substring(delimiterIndex + 2));
                    mapEmployees.put(employeeID, newListValue);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
        }


        //the set of keys of the map
        Set<String> keySet = mapEmployees.keySet();
        //iterator of the key set
        Iterator<String> keyIterator = keySet.iterator();
        //long variable to keep the current maximum days
        long maxMutualDays = 0;
        //string builder to keep the current best couple of employees
        StringBuilder resultingEmployees = new StringBuilder();

        //iterate through the key set
        while (keyIterator.hasNext()) {
            String currentEmployee = keyIterator.next();

            //find the maximum mutual working days between the current employee and the rest
            for (String otherEmployee : mapEmployees.keySet()) {
                if (!otherEmployee.equals(currentEmployee)) {
                    long currentMutualDays = findMutualDaysBetweenTwoEmployees(mapEmployees.get(currentEmployee), mapEmployees.get(otherEmployee));

                    //found new maximum of mutual days
                    if (currentMutualDays > maxMutualDays) {
                        maxMutualDays = currentMutualDays;
                        resultingEmployees.setLength(0);
                        resultingEmployees.append(currentEmployee).append(" and ").append(otherEmployee);
                    }
                }
            }

            //we don't need to check for this employee anymore
            keyIterator.remove();
        }

        //print result
        System.out.println("Longest mutually working employees are with IDs " + resultingEmployees + " for " + maxMutualDays + " days.");
    }

    /**
     *Finds the mutual days between two employees by iterating through the entries of the first and find for each entry mutual days in the entries of the second.
     *
     *
     * @param employee1 list of 3-tuples(ProjectID, DateFrom, DateTo) for first employee
     * @param employee2 list of 3-tuples(ProjectID, DateFrom, DateTo) for second employee
     * @return number of days between employee1 and employee2 working together
     */
    public static long findMutualDaysBetweenTwoEmployees(List<String> employee1, List<String> employee2) {
        long mutualDays = 0;

        for (String employeeProject1 : employee1) {
            mutualDays += findProjectMatchInList(employeeProject1, employee2);
        }

        return mutualDays;
    }

    /**
     * Finds the mutual days between a single date(FromDate,ToDate) for a projectID and an employees entries
     *
     * @param employeeToMatchProject single 3-tuple of (ProjectID,FromDate,ToDate)
     * @param employee the list of 3-tuples for an employee
     * @return number of mutual days between a single entry of an empID and an another empID
     */
    public static long findProjectMatchInList(String employeeToMatchProject, List<String> employee) {
        int employeeToMatchDelimiter = employeeToMatchProject.indexOf(',');
        long days = 0;

        for (String project : employee) {
            int projectDelimiterIndex = project.indexOf(',');
            //compares ProjectIDs
            if (project.substring(0, projectDelimiterIndex).equals(employeeToMatchProject.substring(0, employeeToMatchDelimiter))) {

                //takes mutual days of From/To dates
                days +=
                        findDaysBetweenTwoMatches(employeeToMatchProject.substring(employeeToMatchDelimiter + 2), project.substring(projectDelimiterIndex + 2));
            }
        }

        return days;
    }

    /**
     * Parses two 2-tuples of (FromDate,ToDate) into variables of type LocalDate. NULL is parsed as of todays date of writing- 2021-07-20
     *
     * @param dates1 2-tuple of (FromDate, ToDate) of an entry
     * @param dates2 2-tuple of (FromDate, ToDate) of another entry
     * @return the number of mutual days between the two dates
     */
    public static long findDaysBetweenTwoMatches(String dates1, String dates2) {
        int dateDelimiter1 = dates1.indexOf(',');
        int dateDelimiter2 = dates2.indexOf(',');
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate[] formattedDates1 = new LocalDate[2];
        LocalDate[] formattedDates2 = new LocalDate[2];

        //parses FromDate
        formattedDates1[0] = dates1.substring(0, dateDelimiter1).equals("NULL") ? LocalDate.now() : LocalDate.parse(dates1.substring(0, dateDelimiter1), dtf);
        formattedDates2[0] = dates2.substring(0, dateDelimiter2).equals("NULL") ? LocalDate.now() : LocalDate.parse(dates2.substring(0, dateDelimiter2), dtf);
        //parses ToDate
        formattedDates1[1] = dates1.substring(dateDelimiter1 + 2).equals("NULL") ? LocalDate.now() : LocalDate.parse(dates1.substring(dateDelimiter1 + 2), dtf);
        formattedDates2[1] = dates2.substring(dateDelimiter2 + 2).equals("NULL") ? LocalDate.now() : LocalDate.parse(dates2.substring(dateDelimiter2 + 2), dtf);

        if (formattedDates2[1].compareTo(formattedDates1[1]) > 0) {
            return daysBetweenDates(formattedDates1, formattedDates2);
        } else return daysBetweenDates(formattedDates2, formattedDates1);

    }

    /**
     * Determines the number of mutual days based on that ToDate of dates2 is later in time than ToDate of dates1(second element of the arrays)
     *
     * @param dates1 array of FromDate,ToDate
     * @param dates2 array of FromDate,ToDate
     * @return 0 if the ToDate of dates2 is later in time than the FromDate of dates1 or else the difference of days of the intersection of the dates
     */
    public static long daysBetweenDates(LocalDate[] dates1, LocalDate[] dates2) {
        if (dates2[0].compareTo(dates1[1]) > 0)
            return 0;
        //number of days is FromDate of dates2 - ToDate of dates1
        else if (dates2[0].compareTo(dates1[0]) >= 0)
            return ChronoUnit.DAYS.between(dates2[0], dates1[1]);

        //number of days is ToDate and FromDate of dates1
        return ChronoUnit.DAYS.between(dates1[0], dates1[1]);

    }

}
