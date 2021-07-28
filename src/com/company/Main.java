package com.company;

import java.io.*;
import java.util.*;

public class Main {

    /**
     * Finds the solution by reading all the employees from the text file into a list of employees,
     * and then comparing every different couple of employees to find the maximum number of mutual days between them.
     *
     * @param args command-line arguments
     * @throws IOException Working with files could cause it.
     */
    public static void main(String[] args) throws IOException {
        System.out.print("Type a text file name to read from: ");
        Scanner in = new Scanner(System.in);
        String fileName = in.nextLine();
        InputStream is = Main.class.getResourceAsStream(fileName);

        List<Employee> employeeList = new ArrayList<>();

        //try-with-resources statement where the file is open
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;

            //reads every line from the file
            while ((line = br.readLine()) != null) {
                int delimiterIndex = line.indexOf(',');

                //gets the index of the current employee in the list of employees
                int indOfEmployee = listContainsEmployeeIndex(employeeList,line.substring(0,delimiterIndex));

                //list doesn't contain current employee
                if(indOfEmployee == -1) {
                    employeeList.add(new Employee(line));
                }
                //add new project for the existing employee
                else{
                    employeeList.get(indOfEmployee).addProject(new Project(line.substring(delimiterIndex+2)));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist.");
        }


        long maxMutualDays = 0;
        StringBuilder resultingEmployees = new StringBuilder();

        //comparing every couple of employees in the list
        for(int i =0;i<employeeList.size()-1;i++) {
            Employee currentEmployee = employeeList.get(i);

            for (int j = i + 1; j < employeeList.size(); j++) {
                long currentMutualDays = currentEmployee.findMutualDaysWithEmployee(employeeList.get(j));

                //found new maximum of mutual days
                if (currentMutualDays > maxMutualDays) {
                    maxMutualDays = currentMutualDays;
                    resultingEmployees.setLength(0);
                    resultingEmployees.append(currentEmployee.getEmployeeID()).append(" and ").append(employeeList.get(j).getEmployeeID());
                }
            }
        }

        System.out.println("Longest mutually working employees are with IDs " + resultingEmployees + " for " + maxMutualDays + " days.");
    }

    /**
     * Checks if a list of employees contains employeeID
     * @param list List of employees
     * @param employeeID Employee to look for in the list
     * @return Index of employeeID in the list or -1 if it is not there.
     */
    public static int listContainsEmployeeIndex(List<Employee> list, String employeeID) {
        for (int i=0;i<list.size();i++)
            if (list.get(i).getEmployeeID().equals(employeeID))
                return i;

        return -1;
    }
}
