package com.company;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Represents a project from the file(the last 3 columns)
 */
public class Project {
    private String projectID;
    private LocalDate fromDate;
    private LocalDate toDate;

    /**
     * Creates a project from a read line by parsing on the delimiter ','.
     * @param fileLine read line from the text file
     */
    public Project(String fileLine){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int delimiterIndex = fileLine.indexOf(',');
        projectID = fileLine.substring(0,delimiterIndex);

        int delimiterIndex2 = fileLine.indexOf(',',delimiterIndex+1);
        fromDate = fileLine.substring(delimiterIndex+2, delimiterIndex2).equals("NULL") ? LocalDate.now() : LocalDate.parse(fileLine.substring(delimiterIndex+2,delimiterIndex2), dtf);

        toDate =   fileLine.substring(delimiterIndex2+2).equals("NULL") ? LocalDate.now() : LocalDate.parse(fileLine.substring(delimiterIndex2+2), dtf);
    }

    /**
     * Gets the project ID.
     * @return A string representing the project ID.
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the fromDate of the project.
     * @return the fromDate of the project from type LocalDate
     */
    public LocalDate getFromDate() {
        return fromDate;
    }

    /**
     * Gets the toDate of the project.
     * @return the toDate of the project from type LocalDate
     */
    public LocalDate getToDate() {
        return toDate;
    }

    /**
     * Gets the mutual days between the current project and 'other' project with a note that
     * toDate of 'other' is later in time than the toDate of current project.
     *
     * @param other The other project whose dates we compare with.
     * @return The number of mutual days of type long between the projects in long(it could be a big number)
     */
    public long daysBetweenDates(Project other) {
        if (other.getFromDate().compareTo(toDate) > 0)
            return 0;
            //number of days is FromDate of other - ToDate of current object
        else if (other.getFromDate().compareTo(fromDate) >= 0)
            return ChronoUnit.DAYS.between(other.getFromDate(), toDate);

        //number of days is ToDate and FromDate of current object
        return ChronoUnit.DAYS.between(fromDate, toDate);

    }
}
