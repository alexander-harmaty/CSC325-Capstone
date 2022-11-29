/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.farmingdale.csc325_project;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author skysn
 */
public class Course {
    protected int CRN;
    protected int code;
    protected int credits;
    protected int room;
    protected String building;
    protected String professor;
    protected String section;
    protected List<String> students;
    protected String subject;
    protected String title;
    protected List<String> assignments;

    public Course(int CRN, int code, int credits, int room, String building, String professor, String section, List<String> students, String subject, String title,List<String> assignments) {
        this.CRN = CRN;
        this.code = code;
        this.credits = credits;
        this.room = room;
        this.building = building;
        this.professor = professor;
        this.section = section;
        this.students = students;
        this.subject = subject;
        this.title = title;
        this.assignments = assignments;
    }
    
    public Course(QueryDocumentSnapshot document)
    {
        this.CRN =  Integer.parseInt(document.getData().get("CRN").toString());
        this.code = Integer.parseInt(document.getData().get("code").toString());
        this.credits = Integer.parseInt(document.getData().get("credits").toString());
        this.room = Integer.parseInt(document.getData().get("room").toString());
        this.building = (String)document.getData().get("building");
        this.professor = (String)document.getData().get("professor");
        this.section = (String)document.getData().get("section");
        this.students = (List<String>)document.getData().get("students");
        this.subject = (String)document.getData().get("subject");
        this.title = (String)document.getData().get("title");
        this.assignments=(List<String>)document.getData().get("assignments");
    }

    public Course(Course course) {
        this.CRN = course.CRN;
        this.code = course.code;
        this.credits = course.credits;
        this.room = course.room;
        this.building = course.building;
        this.professor = course.professor;
        this.section = course.section;
        this.students = course.students;
        this.subject = course.subject;
        this.title = course.title;
        this.assignments = course.assignments;
    }
    
    public int getCRN() {
        return CRN;
    }

    public void setCRN(int CRN) {
        this.CRN = CRN;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<String> getStudents() {
        return students;
    }

    public void setStudents(List<String> students) {
        this.students = students;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public List<String> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<String> assignments) {
        this.assignments = assignments;
    }
            
}