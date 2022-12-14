/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package edu.farmingdale.csc325_project;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.calendarfx.view.CalendarView;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javax.xml.namespace.QName;

/**
 * FXML Controller class
 *
 * @author AlexH
 */
public class CalendarController extends HomePageController implements Initializable {

    @FXML
    private VBox VBox_navBar;

    @FXML
    private VBox VBox_navButtons;

    private Calendar cal = new Calendar("Cal");
    private CalendarSource calSource = new CalendarSource("Source");
    @FXML
    private CalendarView calendarView_cal = new CalendarView();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateMenu();

        Interval inter = new Interval(LocalDate.now(), LocalTime.now(), LocalDate.now(), LocalTime.now());
        Entry entry = new Entry("test", inter);
        cal.addEntry(entry);

        try {
            readEntriesIntoCalendar();
        } catch (InterruptedException | ExecutionException ex) {
        }
    }

    public void readEntriesIntoCalendar() throws InterruptedException, ExecutionException {
        switch (App.currentUser.type) {
            case "STUDENT":

                List<Calendar> calendars = new ArrayList<>();

                //declare course and its documents list
                Course course;
                List<Course> courses = new ArrayList<>();
                List<QueryDocumentSnapshot> documents_course;

                //get courses collection
                ApiFuture<QuerySnapshot> future_courses = App.fstore.collection("courses").get();

                try {
                    //add collection into list
                    documents_course = future_courses.get().getDocuments();

                    //check if empty
                    if (!documents_course.isEmpty()) {

                        //loop through courses
                        for (QueryDocumentSnapshot document : documents_course) {

                            //use course document constructor to hold assignment data
                            course = new Course(document);

                            //loop thorugh all courses
                            for (String student : course.students) {

                                //if the currentUser ID is found in any of the courses...
                                if (App.currentUser.userID.equals(student)) {
                                    //add course to list
                                    courses.add(course);
                                    //add a calendar with course name
                                    calendars.add(new Calendar(course.subject + "-" + course.code));
                                }
                            }
                        }
                    }
                } catch (InterruptedException | ExecutionException ex) {
                }

                ////////////////////////////////////////////////////////////////////////
                ////////////////////////////////////////////////////////////////////////
                //declare course and its documents list
                Assignment assignment;
                List<QueryDocumentSnapshot> documents_assignment;

                //get assignments collection
                ApiFuture<QuerySnapshot> future_assignments = App.fstore.collection("assignments").get();

                try {
                    //add collection into list
                    documents_assignment = future_assignments.get().getDocuments();

                    //check if empty
                    if (!documents_assignment.isEmpty()) {

                        //loop through all courses
                        int i = 0;
                        for (Course thisCourse : courses) {

                            //loop through all assignments
                            for (QueryDocumentSnapshot document : documents_assignment) {

                                //use assignments document constructor to hold assignment data
                                assignment = new Assignment(document);

                                //loop through all the courseCRNs within assignment
                                for (String thisAssignCourseCRN : assignment.course) {

                                    //if an assignment.courseCRN matches thisCourseCRN...
                                    if (thisAssignCourseCRN.equals(thisCourse.CRN)) {

                                        //add an entry into the current course calendar
                                        Entry entry = entryFactory(assignment);
                                        calendars.get(i).addEntry(entry);
                                    }
                                }

                            }
                            i++;
                        }
                    }
                } catch (InterruptedException | ExecutionException ex) {
                }
                calSource.getCalendars().addAll(calendars);
                calendarView_cal.getCalendarSources().set(0, calSource);

                break;
            case "PROFESSOR":
                Assignment assignment2;
                List<Calendar> Professorcalendars = new ArrayList<>();
                int i = 0;
                for (Course curcourse : App.currentListOfCourses) {
                    Professorcalendars.add(new Calendar(curcourse.subject + "-" + curcourse.code));

                    List<QueryDocumentSnapshot> documents;
                    ApiFuture<QuerySnapshot> future2 = App.fstore.collection("assignments").get();

                    try {
                        documents = future2.get().getDocuments();
                        if (!documents.isEmpty()) {
                            for (QueryDocumentSnapshot document : documents) {
                                assignment2 = new Assignment(document);
                                for (String course2 : assignment2.course) {
                                    if (course2.equals(curcourse.CRN)) {
                                        Entry entry2 = entryFactory(assignment2);
                                        Professorcalendars.get(i).addEntry(entry2);
                                    }
                                }

                            }
                        }
                    } catch (InterruptedException | ExecutionException ex) {
                    }

                }
                calSource.getCalendars().addAll(Professorcalendars);
                calendarView_cal.getCalendarSources().set(0, calSource);
        }
    }

    public Entry entryFactory(Assignment assignment) {
        //assignment.dueDate.toDate()
        //assignment.dueDate.
        LocalDateTime localDateTime = assignment.dueDate.toSqlTimestamp().toLocalDateTime();
        Interval interval = new Interval(localDateTime, localDateTime);
        Entry entry = new Entry(assignment.title, interval);
        return entry;
    }

}
