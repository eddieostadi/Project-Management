import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class StudentTest {

    Map<String , Integer> scores = new HashMap<>();
    ArrayList<String> conflicts = new ArrayList<>();
    @org.junit.Before
    public void setup(){
        scores.put("P", 1);
        conflicts.add("S-15");
        Student student = new Student("John", "Snow", "S-12", scores, conflicts, "A");

    }

}