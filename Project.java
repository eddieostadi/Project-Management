import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.util.*;

public class Project implements Serializable {
    private String title;
    private String projID;
    private String description;
    private ProjOwner owner;
    HashMap<String, Integer> skill;
    HashMap<String, Student> team = new HashMap<>();
    HashMap<String, Double> stats = new HashMap<>();
    ArrayList<String> conflictList = new ArrayList<>();
    ArrayList<String> personalityList = new ArrayList<>();
    private Throwable InvalidParameterException;
    private Throwable DuplicatedMemberException;

    public Project(String title, String projID, String description, ProjOwner owner, Map<String, Integer> skill) {
        this.title = title;
        this.projID = projID;
        this.description = description;
        this.owner = owner;
        this.skill = (HashMap<String, Integer>) skill;
        System.out.println("Project ID : "+ this.projID+ "is registered successfully\n");
    }



    public String print(){
        return (this.title +" --> "+ description);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ProjOwner getOwner() {
        return owner;
    }

    public HashMap<String, Integer> getSkill() {
        return skill;
    }

    public HashMap<String, Student> getTeam() {
        return team;
    }

    public String getProjID() {
        return projID;
    }

    public void addTeamMember(Student student) throws Throwable {
        if (this.team.containsKey(student.getStudentID())){
            throw new DuplicatedMemberException ("This Member is in this group already");
        } else if (!conflictList.contains(student.getStudentID())) {
            throw new ConflictMemberException("This member has conflict with other existing member");
        } else if (team.size() == 3 && !personalityList.contains("A") && !student.getPersonality().equals("A")) {
            throw new NoLeaderException("This team needs a leader, Please change this member to a Leader");
        } else if (student.getProject() != null) {
            throw new MemberOfOtherTeamException("This student is already allocated to other project");
        } else if(personalityList.contains("A") && student.getPersonality() == "A") {
            throw new TwoLeaderException("This team has a leader already");
        } else if((new HashSet<String>(personalityList)).size()==2 && personalityList.size() ==3 && personalityList.contains(student.getPersonality())) {
            throw new NotEnoughPersonalityException("This team needs more personality variety");
        } else {
            this.team.put(student.getStudentID(), student);
            student.add(this);
            conflictList.addAll(student.getConflicts());
            personalityList.add(student.getPersonality());
        }

    }
    public void statistics(){
        int p_Total, a_Total, w_Total, n_Total, interested = 0;
        double p_Avg, a_Avg, w_Avg, n_Avg, stDev, shortFall = 0;

        a_Total = 0;
        w_Total = 0;
        p_Total = 0;
        n_Total = 0;

        for (Map.Entry<String, Student> entry : this.team.entrySet()) {
            Student student = entry.getValue();
            a_Total += student.marks.get("A");
            w_Total += student.marks.get("W");
            n_Total += student.marks.get("N");
            p_Total += student.marks.get("P");
            if (student.isInterested()){
                interested += 1;
            }
        }
        p_Avg = (double) p_Total / 4;
        a_Avg = (double) a_Total / 4;
        n_Avg = (double) n_Total / 4;
        w_Avg = (double) w_Total / 4;
        stats.put("P",p_Avg);
        stats.put("W",w_Avg);
        stats.put("A", a_Avg);
        stats.put("N", n_Avg);
        ArrayList<Double> avgScoreList = new ArrayList<>(stats.values());
        stDev = Utility.StDev(avgScoreList);
        for (Map.Entry<String, Double> score : stats.entrySet()){
            String skills = score.getKey();
            double scr = score.getValue();
            for (Map.Entry<String, Integer> entry : this.skill.entrySet()) {
                String skill = entry.getKey();
                Integer value = entry.getValue();

                if (skill.equals(skills)) {
                    double gap =  value - scr;
                    shortFall += gap;
                    stats.put(skill+"_Gap", gap);
                }
            }
        }
        stats.put("stDev", stDev);
        stats.put("GrandAvg", (p_Avg+a_Avg+n_Avg+w_Avg)/4);
        stats.put("Interested", 100 * ((double) interested)/4);
        stats.put("ShortFall", shortFall);
    }

    public HashMap<String, Double> getStats() {
        return stats;
    }
}
class NotEnoughPersonalityException extends Exception{
    public NotEnoughPersonalityException(String message) {
        super(message);
    }
}
class NoLeaderException extends Exception{
    public NoLeaderException(String message) {
        super(message);
    }
}
class DuplicatedMemberException extends Exception{
    public DuplicatedMemberException(String message) {
        super(message);
    }
}
class MemberOfOtherTeamException extends Exception{
    public MemberOfOtherTeamException(String message) {
        super(message);
    }
}
class ConflictMemberException extends Exception{
    public ConflictMemberException(String message) {
        super(message);
    }
}
class TwoLeaderException extends Exception {
    public TwoLeaderException(String message) {
        super(message);
    }
}
