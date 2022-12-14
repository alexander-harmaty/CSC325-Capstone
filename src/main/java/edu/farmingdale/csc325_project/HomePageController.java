package edu.farmingdale.csc325_project;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

public class HomePageController implements Initializable {

    @FXML
    private VBox VBox_navBar;

    @FXML
    private VBox VBox_navButtons;

    @FXML
    private MFXButton button_resetPassword;

    @FXML
    private MFXTextField textField_password;

    @FXML
    private MFXTextField textField_retypePassword;

    @FXML
    protected final MFXButton button_home = new MFXButton("Home");

    @FXML
    protected final MFXButton button_courses = new MFXButton("Courses");

    @FXML
    protected final MFXButton button_grades = new MFXButton("Grades");

    @FXML
    protected final MFXButton button_calendar = new MFXButton("Calendar");

    @FXML
    protected final MFXButton button_accounts = new MFXButton("Accounts");

    @FXML
    protected final MFXButton button_logout = new MFXButton("Logout");

    @FXML
    private TableColumn<Assignment, Timestamp> tableColumn_due;

    @FXML
    private TableColumn<Assignment, String> tableColumn_title;

    @FXML
    private TableView<?> tableView_assignments;

    @FXML
    private TableView<Course> tableView_popup;

    @FXML
    private TableColumn<Course, Integer> CRNCol, codeCol;

    @FXML
    private TableColumn<Course, String> subjectCol, titleCol;

    @FXML
    void handleButton_resetPassword(ActionEvent event) {

        if (textField_password.getText().equals(textField_retypePassword.getText())) {

            User user;
            List<QueryDocumentSnapshot> documents;

            ApiFuture<QuerySnapshot> future = App.fstore.collection("users").get();

            try {

                documents = future.get().getDocuments();

                if (!documents.isEmpty()) {
                    for (QueryDocumentSnapshot document : documents) {
                        user = new User(document);

                        if (user.email.equals(App.currentUser.email)) {
                            DocumentReference docRef = App.fstore.collection("users").document(user.userID);
                            ApiFuture<WriteResult> result = docRef.update("password", textField_password.getText());
                        }
                    }
                }

            } catch (InterruptedException | ExecutionException e) {

            }

        }
    }

    private ObservableList<Course> listOfCourses = FXCollections.observableArrayList();

    public ObservableList<Course> getListOfCourses() {
        return listOfCourses;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        App.currentListOfCourses.clear();
        //add buttons to menu based on current user
        updateMenu();

        //set columns with cell factory
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        CRNCol.setCellValueFactory(new PropertyValueFactory<>("CRN"));

        //set list of courses
        readCoursesIntoTable();

        //set code to switch to course view after double clicking a table selection
        setOnMousePressed();

    }

    protected void updateMenu() {
        VBox_navButtons.getChildren().clear();

        List<MFXButton> buttons = new ArrayList<>();

        switch (App.currentUser.type) {

            case "STUDENT":

                button_home.setOnAction(event -> {
                    try {
                        App.setRoot("HomePage");
                    } catch (IOException ex) {
                    }
                });

                button_grades.setOnAction(event -> {
                    try {
                        App.setRoot("Grades");
                    } catch (IOException ex) {
                    }
                });
//                
                button_calendar.setOnAction(event -> {
                    try {
                        App.setRoot("Calendar");
                    } catch (IOException ex) {
                    }
                });
//                
//                button_registrar.setOnAction(event -> {
//                    try { App.setRoot("Registrar.fxml"); } 
//                    catch (IOException ex) {}
//                });
                button_logout.setOnAction(event -> {
                    try {
                        App.currentUser.logOut();
                    } catch (IOException ex) {
                    }
                });

                buttons.add(button_home);
                buttons.add(button_grades);
                buttons.add(button_calendar);
                buttons.add(button_logout);

                break;

            case "PROFESSOR":

                button_home.setOnAction(event -> {
                    try {
                        App.setRoot("HomePage");
                    } catch (IOException ex) {
                    }
                });

                button_grades.setOnAction(event -> {
                    try {
                        App.setRoot("Grades");
                    } catch (IOException ex) {
                    }
                });

                button_calendar.setOnAction(event -> {
                    try {
                        App.setRoot("Calendar");
                    } catch (IOException ex) {
                    }
                });
                button_logout.setOnAction(event -> {
                    try {
                        App.currentUser.logOut();
                    } catch (IOException ex) {
                    }
                });

                buttons.add(button_home);
                buttons.add(button_grades);
                buttons.add(button_calendar);
                buttons.add(button_logout);

                break;

            case "ADMIN":

                button_logout.setOnAction(event -> {
                    try {
                        App.currentUser.logOut();
                    } catch (IOException ex) {
                    }
                });

                buttons.add(button_logout);

                break;

            default:

                break;

        }

        for (MFXButton button : buttons) {
            Font font = new Font("System", 26);
            button.setFont(font);
            button.setPrefHeight(50);
            button.setMaxWidth(Double.MAX_VALUE);
            button.setMaxHeight(50);
            VBox_navButtons.getChildren().add(button);
        }

    }

    private void readCoursesIntoTable() {

        //declare course and its documents list
        Course course;
        List<QueryDocumentSnapshot> documents;

        //get assignments collection
        ApiFuture<QuerySnapshot> future = App.fstore.collection("courses").get();

        try {
            //add collection into list
            documents = future.get().getDocuments();

            //check if empty
            if (!documents.isEmpty()) {

                //loop through assignments
                for (QueryDocumentSnapshot document : documents) {

                    //use course document constructor to hold assignment data
                    course = new Course(document);

                    switch (App.currentUser.type) {
                        case "STUDENT":
                            //loop thorugh all courses
                            for (String student : course.students) {

                                //if the currentUser ID is found in any of the courses...
                                if (App.currentUser.userID.equals(student)) {
                                    //add course to list
                                    listOfCourses.add(course);
                                    App.currentListOfCourses.add(course);
                                }
                            }
                            break;
                        case "PROFESSOR":
                            //loop thorugh all courses
                            String professor = course.professor;

                            //if the currentUser ID is found in any of the courses...
                            if (App.currentUser.userID.equals(professor)) {
                                //add course to list
                                listOfCourses.add(course);
                                App.currentListOfCourses.add(course);
                            }
                            break;
                        default:
                            break;
                    }
                }
                //set tableview to assignments list
                tableView_popup.setItems(listOfCourses);
            }
        } catch (InterruptedException | ExecutionException ex) {
        }
    }

    private void setOnMousePressed() {

        tableView_popup.setOnMousePressed((MouseEvent event) -> {

            //check for primary mouse clicks
            if (event.getButton().equals(MouseButton.PRIMARY)) {

                //if click is double click...
                if (event.getClickCount() == 2) {

                    //read selected course CRN
                    String selectedCRN = tableView_popup.getSelectionModel().getSelectedItem().getCRN();

                    //declare course and its list
                    Course course;
                    List<QueryDocumentSnapshot> documents;

                    //get courses collection
                    ApiFuture<QuerySnapshot> future = App.fstore.collection("courses").get();

                    try {
                        //add collection into list
                        documents = future.get().getDocuments();

                        //check if empty
                        if (!documents.isEmpty()) {

                            //loop through courses
                            for (QueryDocumentSnapshot document : documents) {

                                //use assignment document constructor to hold assignment data
                                course = new Course(document);

                                //if the CRN of any course matches the selected course CRN...
                                if (course.getCRN().equals(selectedCRN)) {
                                    //set currentCourse to the selected course
                                    App.currentCourse = new Course(course);
                                    //change view to course
                                    App.setRoot("Course");
                                }
                            }
                        }
                    } catch (InterruptedException | ExecutionException | IOException ex) {
                    }
                }
            }
        });

    }

}
