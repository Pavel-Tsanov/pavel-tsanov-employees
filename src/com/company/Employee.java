package com.company;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an employee from the file(a single line in the file).
 */
public class Employee {
    private String employeeID;
    //a list of all projects where the id of employee is equal to employeeID
    private List<Project> projects = new ArrayList<>();

    /**
     * Creates an employee from a read line in the file.
     * @param fileLine A single read line.
     */
    public Employee(String fileLine) {
        //takes first column
        int delimiterIndex = fileLine.indexOf(',');
        employeeID = fileLine.substring(0, delimiterIndex);

        //takes the rest of the columns and creates a project
        Project project = new Project(fileLine.substring(delimiterIndex + 2));
        projects.add(project);
    }

    /**
     * Gets the id of the employee
     * @return A string representing the id of the employee
     */
    public String getEmployeeID() {
        return employeeID;
    }

    /**
     * Gets all the projects with corresponding id of employee equal to employeeID
     * @return List with elements of type Project
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * Adds new project to the list of projects of this employee
     * @param project Object of type Project to add.
     */
    public void addProject(Project project) {
        projects.add(project);
    }

    /**
     * Finds the mutual days between two employees.
     * @param employee the other employee
     * @return Mutual days of type long between current employee and the other employee.
     */
    public long findMutualDaysWithEmployee(Employee employee){
        long mutualDays = 0;

        //Check for every project of the current employee if the other employee has it and if so gets the mutual days
        for (Project project : getProjects()) {
            mutualDays += findProjectMatchInList(project, employee.getProjects());
        }

        return mutualDays;
    }

    /**
     * Helper method to get the mutual days between a project and a list of projects
     * @param projectToMatch the project to match
     * @param projects list of projects
     * @return The sum of mutual days for all the matches.
     */
    private long findProjectMatchInList(Project projectToMatch,List<Project> projects){
        long days = 0;

        for(Project project : projects){
            if(project.getProjectID().equals(projectToMatch.getProjectID())){
                if(project.getToDate().compareTo(projectToMatch.getToDate()) > 0)
                    days += projectToMatch.daysBetweenDates(project);
                else days += project.daysBetweenDates(projectToMatch);
            }
        }

        return days;
    }
}
