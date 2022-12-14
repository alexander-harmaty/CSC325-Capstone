/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.farmingdale.csc325_project;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author trintydarbouze
 */
public class Submission {

    protected int grade;
    protected String student;
    protected String studentComment;
    protected boolean submitted;
    protected Timestamp submittedDate;
    protected String teacherFeedback;
    protected String assignment;
    protected String CRN;
    protected String ID;
    protected String submissionDetails;
    protected String fullName;

   

    public Submission(int grade, String student, String studentComment, boolean submitted, Timestamp submittedDate, String teacherFeedback, String assignment, String course, String ID, String submissionDetails ) {

        this.grade = grade;
        this.student = student;
        this.studentComment = studentComment;
        this.submitted = submitted;
        this.submittedDate = submittedDate;
        this.teacherFeedback = teacherFeedback;
        this.assignment = assignment;
        this.CRN = course;
        this.ID = ID;
        this.submissionDetails = submissionDetails;
    }


    public Submission(QueryDocumentSnapshot document) {

        this.grade = Integer.parseInt(document.getData().get("grade").toString());
        this.student = (String) document.getData().get("student");
        this.studentComment = (String) document.getData().get("studentComment");
        this.submitted = (Boolean) document.getData().get("submitted");
        this.submittedDate = (Timestamp) document.getData().get("submittedDate");
        this.teacherFeedback = (String) document.getData().get("teacherFeedback");
        this.assignment = (String) document.getData().get("assignment");
        this.CRN = (String) document.getData().get("CRN");
        this.ID = document.getId();
        this.submissionDetails = (String) document.getData().get("submissionDetails");
        this.fullName = getFullName();
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public String getStudentComment() {
        return studentComment;
    }

    public void setStudentComment(String studentComment) {
        this.studentComment = studentComment;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public Timestamp getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Timestamp submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getTeacherFeedback() {
        return teacherFeedback;
    }

    public void setTeacherFeedback(String teacherFeedback) {
        this.teacherFeedback = teacherFeedback;
    }

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public String getCRN() {
        return CRN;
    }

    public void setCRN(String CRN) {
        this.CRN = CRN;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    
    
    public String getSubmissionDetails() {
        return submissionDetails;
    }

    public void setSubmissionDetails(String submissionDetails) {
        this.submissionDetails = submissionDetails;
    }
    
     public String getFullName() {
        User user;
                    List<QueryDocumentSnapshot> documents;
                    
                    //get assignment collection
                    ApiFuture<QuerySnapshot> future = App.fstore.collection("users").get();
                 
                    try {
                        //add collection into list
                        documents = future.get().getDocuments();

                        //check if empty
                        if (!documents.isEmpty()) {
                            
                            //loop through assignments
                            for (QueryDocumentSnapshot document : documents) {
                                
                                //use assignment document constructor to hold assignment data
                                user = new User(document);

                                //if the CRN of any course matches the selected course CRN...
                                if (user.userID.equals(this.student)) {
                                    //set currentAssignment to the selected course
                                    fullName = user.firstName + " "+ user.lastName;
                                   
                                }
                            }
                        }
                    } 
                    catch (InterruptedException | ExecutionException  ex) {}
        //QueryDocumentSnapshot docRef = App.fstore.collection("users").document(this.student);
        
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
