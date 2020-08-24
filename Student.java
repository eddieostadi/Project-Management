import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Student implements Serializable {
    private String firstName;
    private String surName;
    private String studentID;
    Map <String, Integer> marks;
    ArrayList<String> conflicts;
    private String personality;
    private Project project;
    Map<Project, Integer> preference = new HashMap<>();

    public Student(String firstName, String surName, String studentID, Map<String, Integer> marks, ArrayList<String> conflicts, String personality) {
        this.firstName = firstName;
        this.surName = surName;
        this.studentID = studentID;
        this.marks = marks;
        this.conflicts = conflicts;
        this.personality = personality;
        System.out.println("Student ID : "+ this.studentID +" is registered\n");
    }
    public void add(Project proj){
        this.project = proj;
    }

    public Project getProject() {
        return project;
    }

    public String getStudentID() {
        return studentID;
    }

    public Map<String, Integer> getMarks() {
        return marks;
    }

    public ArrayList<String> getConflicts() {
        return conflicts;
    }

    public String getPersonality() {
        return personality;
    }

    public Map<Project, Integer> getPreference() {
        return preference;
    }

    public void setPreference(Map<Project, Integer> preference) {
        this.preference = preference;
    }

    public boolean isInterested (){
        for (Project p : this.preference.keySet()){
            if (p.getProjID().equals(project.getProjID()) && preference.get(p) > 2){
                return true;
            }
        }
        return false;
    }
}
