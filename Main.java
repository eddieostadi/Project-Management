import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class Main implements Serializable {

    public static void main(String[] args) throws Throwable {

        HashMap<String, Company> companyList = new HashMap<>();
        HashMap<String, Project> projectList = new HashMap<>();
        HashMap<String, ProjOwner> ownerList = new HashMap<>();
        HashMap<String, Student> studentList = new HashMap<>();
        HashMap<Project, Integer> shortListed = new HashMap<>();
        HashMap<Project, HashMap<String, Student>> teams = new HashMap<>();
        Map<Student, HashMap<Project, Integer>> votedProjects = new HashMap<>();
        HashMap<Project, HashMap<String, Double>> summary = new HashMap<>();

        String option;
        Scanner in = new Scanner(System.in);
        //Menu
        do {
            System.out.println("\nPlease Choose your option by inserting the corresponding letter\n------------------\n"+
                    "A.\tAdd Company\n"+
                    "B.\tAdd Project Owner\n"+
                    "C.\tAdd Project\n"+
                    "D.\tCapture Students Personalities\n"+
                    "E.\tAdd Student Preference\n"+
                    "F.\tShortlist Project\n"+
                    "G.\tForm the Teams\n"+
                    "H:\tDisplay Team Fitness Metrics\n"+
                    "L.\tLoad Progress\n"+
                    "S.\tSave Progress\n"+
                    "Q.\tQuit"+
                    "\n");
            option = in.nextLine();
            switch (option.toUpperCase()){

                //Add Company
                case "A":{

                    System.out.println("Insert Company name:");
                    String coName = in.nextLine();
                    String coID = "C-"+ (companyList.size()+1);
                    String ABN;
                    boolean validator = false;
                    do{
                        System.out.println("Insert ABN number");
                        ABN = in.nextLine();
                        try{
                            if( (ABN.length() == 11) && (Long.parseLong(ABN))/1 == (Long.parseLong(ABN))){
                                validator = true;
                            }
                        }catch (Exception e){
                            continue;
                        }
                    }while (!validator);
                    System.out.println("Insert company URL for "+ coName);
                    String URL = in.nextLine();
                    System.out.println("Insert Address for "+ coName);
                    String coAddress = in.nextLine();
                    companyList.put(coID,new Company(coID, coName, ABN, URL, coAddress));
                    PrintWriter pw = Utility.writer("companies.txt");
                    pw.printf("%-20s ",coName + "\n"+ coID +"\n"+ ABN + "\n"+ URL + "\n"+ coAddress+
                            "\n   ===================================================");
                    pw.println();
                    pw.close();
                    break;
                }
                //Add Owner
                case "B":{
                    if (!companyList.isEmpty()){
                        System.out.println("Insert the owner's first name:");
                        String ownerName = in.nextLine();
                        System.out.println("Insert the owner's surname");
                        String ownerFamily = in.nextLine();
                        String ownerID = "OWN-"+ (ownerList.size()+1);
                        String coID;
                        do{
                            System.out.println("Insert related company ID");
                            coID = in.nextLine().toUpperCase();
                        }while (companyList.get(coID) == null);

                        System.out.println("Insert the owner's role");
                        String role = in.nextLine();
                        System.out.println("Insert the owner's email address");
                        String email = in.nextLine();
                        try{
                            ownerList.put(ownerID, new ProjOwner(ownerName,ownerFamily,ownerID, role, email, companyList.get(coID)));
                            PrintWriter pw = Utility.writer("owners.txt");
                            pw.printf("%-20s ", "First name: " +ownerName+ "\nLast name: "+ ownerFamily +
                                    "\nOwner ID: "+ ownerID + "\nRole: "+ role+ "\nEmail: "+ email+
                                    "\nCompany ID: "+ coID+ "\n =========================================");
                            pw.println();
                            pw.close();
                        }catch (Exception e){
                            System.out.println("Registration failed, please check company ID and try again");
                            break;
                        }

                    } else {
                        System.out.println("You should register the company list first (option A)\n");
                    }


                    break;
                }
                //Add Project
                case "C":{
                    if (!ownerList.isEmpty()){
                        System.out.println("Insert the project title");
                        String projTitle = in.nextLine();
                        String projID = "PR-"+ (projectList.size()+1);
                        System.out.println("Insert project description");
                        String projDesc = in.nextLine();
                        String ownerID;
                        do{
                            System.out.println("Insert Project owner's ID");
                            ownerID = in.nextLine().toUpperCase();
                        }while (ownerList.get(ownerID) == null);

                        boolean valid = false;
                        HashMap<String, Integer> skillsList;
                        int n, p , w, a;
                        do {
                            skillsList =  new HashMap<>();
                            try{
                                System.out.println("Insert a score from 1 to 4 for each skill regarding to project");
                                System.out.println("Networking & Security");
                                n = in.nextInt();
                                System.out.println("Programming & Software Engineering");
                                p = in.nextInt();
                                System.out.println("Web & Mobile applications ");
                                w = in.nextInt();
                                System.out.println("Analytics and Big Data");
                                a = in.nextInt();
                                in.nextLine();
                                if(a > 0 && w > 0 && n > 0 && p > 0 && a < 5 && w < 5 && n < 5 && p < 5 && (a+w+n+p ==10)){
                                    valid = true;
                                    skillsList.put("N", n);
                                    skillsList.put("W", w);
                                    skillsList.put("A", a);
                                    skillsList.put("P", p);
                                }

                            }catch (Exception e){
                                System.out.println("Invalid data entry");
                                break;
                            }

                        }while (!valid);

                        try{
                            projectList.put(projID, new Project(projTitle, projID, projDesc, ownerList.get(ownerID), skillsList));
                            PrintWriter pw = Utility.writer("projects.txt");
                            pw.printf("%-20s ", "Title: " + projTitle +"\nID: "+ projID +"\nDescription: "+ projDesc +
                                    "\nOwner ID: "+ ownerID + "Skills required: "+ skillsList + "\n=====================================");
                            pw.println();
                            pw.close();

                        }catch (Exception e){
                            System.out.println("Invalid data entry, please check the owner ID and try again");
                            break;
                        }

                        System.out.println();


                    } else {
                        System.out.println("You should register the project owners first (option B)");
                    }
                    projectList.forEach((k,v)-> {
                        System.out.println(k);
                        v.getSkill().forEach((i,j)-> {
                            System.out.println(i+ "===>"+j);
                        });
                    });

                    break;

                }
                //Capture Student
                case "D":{
                    Scanner input = Utility.reader("students.txt");
                    PrintWriter pw = Utility.writer("studentInfo.txt");
                    Map<String , Integer> personalityCounterList = new HashMap<>();
                    while(input.hasNext()){
                        Map<String, Integer> markList = new HashMap<>();
                        String stID = input.next();
                        String firstName = input.next();
                        String surName = input.next();
                        for (int i = 0; i < 4; i++ ){
                            String subject = input.next();
                            int grade = Integer.parseInt(input.next());
                            markList.put(subject, grade);
                        }

                        String personality;
                        do{
                            System.out.println("Please insert Personality type for "+ firstName +" "+ surName +
                                    "\nValid types: A, B, C or D");
                            personality = in.next().toUpperCase();
                            if ((personality.equalsIgnoreCase("A") || personality.equalsIgnoreCase("B") ||
                                    personality.equalsIgnoreCase("D") ||personality.equalsIgnoreCase("C") )){
                                if ((!personalityCounterList.containsKey(personality) || (personalityCounterList.get(personality) < 5))){
                                    if (personalityCounterList.containsKey(personality)){
                                        personalityCounterList.put(personality, personalityCounterList.get(personality)+1);
                                    } else {
                                        personalityCounterList.put(personality , 1);
                                    }
                                    break;
                                } else if (personalityCounterList.get(personality) == 5){
                                    System.out.println(personality + " has reached the maximum available count. please choose another type of personality\n");
                                }

                            }  else {
                                System.out.println(">>>>> Invalid Entry for personality <<<<<\n");
                            }

                        }while (true);

                        ArrayList<String> conflictList = new ArrayList<>();
                        boolean valid = false;
                        in.nextLine();
                        do{
                            System.out.println("Conflict with other student (insert student ID)"+
                                    "\nHit Enter key to skip");

                            String conflict = in.nextLine().toUpperCase();

                            conflictList.add(conflict);
                            if( conflictList.size() == 2 || conflict.length() == 0 ){
                                valid = true;
                            }
                        }while (!valid);

                        studentList.put(stID, new Student(firstName, surName, stID, markList, conflictList, personality.toUpperCase()));
                        pw.printf("%-20s ", stID+ "  "+ firstName+" "+surName );
                        markList.forEach((k,v) -> { pw.printf("%-20s ",k +" "+ v);});
                        pw.printf("%-10s ",personality);
                        conflictList.forEach(v -> pw.printf("%-10s ",v));
                        pw.println();
                    }
                    pw.close();

                    break;
                }
                //Add Student Preference
                case "E":{
                    if (!studentList.isEmpty() && !projectList.isEmpty()){
                        PrintWriter pw = Utility.writer("Preferences.txt");



                        for (Entry<String, Student> e : studentList.entrySet()) {
                            String k = e.getKey();
                            Student v = e.getValue();
                            HashMap<Project, Integer> preference = new HashMap<>();
                            System.out.println("Project preference for student ID :" + k);
                            System.out.println("you may choose 4 projects from the list");
                            for (Entry<String, Project> entry : projectList.entrySet()) {
                                String s = entry.getKey();
                                Project value = entry.getValue();
                                System.out.println(s + " " + value.print());
                            }
                            for (int i = 0; i < 4; i++) {
                                boolean valid = false;
                                do {
                                    System.out.println("Type the project ID for priority number " + (i + 1));
                                    System.out.print("PR-");
                                    String pID = in.next();
                                    if (projectList.containsKey(("PR-" + pID))) {
                                        valid = true;
                                        System.out.println("successfully scored a project");
                                        preference.put(projectList.get(("PR-" + pID)), (4 - i));
                                        v.getPreference().put(projectList.get(("PR-" + pID)), (4 - i));
                                    }

                                } while (!valid);
                            }
                            pw.printf("%-10s ", v.getStudentID());
                            pw.println();
                            preference.forEach((key, val) -> {
                                pw.printf("%-10s ", key.getProjID() + " " + val);
                            });
                            pw.println("\n----------------------------------------------------------");
                            pw.println();
                            votedProjects.put(v, preference);

                        }
                        pw.close();

                    } else {
                        System.out.println("Please register projects and students first \n");
                    }

                    break;
                }
                //Short listing projects
                case "F":{
                    if (projectList.isEmpty()){
                        System.out.println("Please make sure you registered projects first");
                    } else if (shortListed.isEmpty()){
                        Map<Project, Integer> projScore = new HashMap<>();
                        studentList.forEach((k , v) -> {
                            v.getPreference().forEach((key,value) -> {
                                if( projScore.containsKey(key)){
                                    projScore.put(key ,(projScore.get(key) + value));
                                } else {
                                    projScore.put(key, value);
                                }
                            });
                        });

                        Map<Project,Integer> sorted = projScore.entrySet().stream()
                                .sorted(Entry.<Project, Integer>comparingByValue().reversed())
                                .collect(toMap(Entry::getKey,
                                        Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                        int counter = 1;
                        ;
                        sorted.keySet().removeAll(Arrays.asList(sorted.keySet().toArray()).subList(5, 10));


                        for (Entry set : sorted.entrySet()) {
                            if ( counter < 6){
                                shortListed.put((Project)set.getKey(), (Integer) set.getValue());

                            }
                            counter ++;
                        }
                        shortListed = shortListed.entrySet().stream()
                                .sorted(Entry.<Project, Integer>comparingByValue().reversed())
                                .collect(toMap(Entry::getKey,Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

                        System.out.println("All Projects");
                        sorted.forEach((k,v) -> {
                            System.out.println(k.getProjID() + " --> "+ v);
                        });

                        System.out.println("\n=============\nShortlisted Projects:\nID\t      Score\n--------+----");
                        shortListed.forEach((k,v) -> {
                            System.out.println(k.getProjID() + "\t|  "+ v);
                        });
                        System.out.println();

                    } else {
                        System.out.println(" Shortlist is already created");
                        System.out.println("\n=============\nShortlisted Projects:\nID\t      Score\n--------+----");
                        shortListed.forEach((k,v) -> {
                            System.out.println(k.getProjID() + "\t|  "+ v);
                        });
                        System.out.println();
                    }
                    break;


                }
                case "G":{
                    if (shortListed.isEmpty()) {
                        System.out.println("Please make sure You shortlisted the projects first\n");
                    } else {
                        System.out.println("How would you like to shape your teams? (Insert the corresponding letter)" +
                                "\nA. Manual                 B. Automatic          C.Go Back to menu");
                        String selection;
                        do {
                            selection = in.next().toUpperCase();
                            switch (selection) {
                                case "A": {
                                    String stID;
                                    for (Project p : shortListed.keySet()) {
                                        System.out.println("Now Allocating for " + p.getProjID());
                                        do {
                                            try {
                                                System.out.println("Insert the Student ID in S-x format");
                                                stID = in.next();
                                                p.addTeamMember(studentList.get(stID));
                                            } catch (Exception exp) {
                                                System.out.println("This student does not exist or can't be added");
                                            }
                                        } while (p.getTeam().size() == 4);
                                    }
                                    break;
                                }
                                case "B": {
                                    HashMap<Project, LinkedHashMap> tempList = new HashMap<>();
                                    HashMap<String, Student> tempStudentList = new HashMap<>(studentList);

                                    for (Entry<String, Student> mapEntry : studentList.entrySet()) {
                                        String id = mapEntry.getKey();
                                        Student obj = mapEntry.getValue();
                                        if (obj.getConflicts().size() > 0) {
                                            for (String conflict : obj.getConflicts()) {
                                                if (studentList.containsKey(conflict))
                                                    studentList.get(conflict.toUpperCase()).getConflicts().add(id);
                                            }
                                        }

                                    }

                                    for (Entry<Project, Integer> entry : shortListed.entrySet()) {

                                        Project project = entry.getKey();
                                        TreeMap<String, Integer> list = new TreeMap<>();
                                        for (Entry<String, Integer> stringIntegerEntry : project.getSkill().entrySet()) {
                                            list.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                                        }
                                        LinkedHashMap<String, Integer> sortedMap =
                                                list.entrySet().stream()
                                                        .sorted(Entry.<String, Integer>comparingByValue().reversed())
                                                        .collect(toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                                        tempList.put(project, sortedMap);
                                    }

                                    int comparator;

                                    for (String i : new String[]{"A", "B", "C", "D"}) {
                                        for (Entry<Project, LinkedHashMap> entry : tempList.entrySet()) {
                                            Project proj = entry.getKey();
                                            LinkedHashMap<String, Integer> skillSet = entry.getValue();

                                            String currentPriority = (String.valueOf(skillSet.entrySet().stream()
                                                    .limit(1).collect(toList()))).substring(1, 2);

                                            Student eligible = null;
                                            comparator = 0;
                                            for (Entry<String, Student> e : tempStudentList.entrySet()) {
                                                Student student = e.getValue();
                                                if (student.getPersonality().equals(i)) {
                                                    if (student.getMarks().get(currentPriority) > comparator) {
                                                        eligible = (student);
                                                        comparator = (student.getMarks().get(currentPriority));
                                                        if (comparator == 4) {
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            boolean hasConflict = false;
                                            for (String c : eligible.getConflicts()) {
                                                if (proj.getTeam().containsKey(studentList.get(c))) {
                                                    hasConflict = true;
                                                    break;
                                                }
                                            }
                                            if (!hasConflict) {
                                                System.out.println("for personality type: " + i + "\t skill type: " +
                                                        currentPriority + "\t Student " + eligible.getStudentID() + " \tpicked for " + proj.getProjID());
                                                skillSet.remove(currentPriority);
                                                tempStudentList.remove(eligible.getStudentID());
                                                proj.addTeamMember(eligible);
                                            }

                                        }
                                    }

                                    shortListed.forEach((project, value) -> {
                                        teams.put(project, project.getTeam());
                                    });
                                    break;
                                }
                                case "C": {
                                    break;
                                }
                                default: {
                                    System.out.println("Invalid option");
                                }
                            }
                        } while (selection.equals("C"));


                        System.out.println("\n=============Teams Summary ==============");
                        shortListed.forEach((k, v) -> {
                            System.out.println(k.getProjID() + " :");
                            k.getTeam().forEach((key, val) -> {
                                System.out.print(key + "   ");
                            });
                            System.out.println("\n----------");
                        });
                        System.out.println("==========================================\n");
                    }
                    break;
                }
                case "H":{
                    if (Utility.teamShaped(shortListed)){

                        for (Entry< Project, Integer> e : shortListed.entrySet()) {
                            Project v = e.getKey();
                            v.statistics();
                            HashMap<String , Double> stats = new HashMap<>(v.getStats());

                            System.out.println("\n======= Project "+ v.getTitle()+ " Team Metrics Summary =======");
                            System.out.println("Project ID: "+ v.getProjID() +
                                    "\n " +
                                    "\n             Skills                    Average       Skill gap"+
                                    "\n\tProgramming & Software Engineering: "+ stats.get("p_Avg")+ "         "+ stats.get("P_Gap").doubleValue()+
                                    "\n\tNetworking and Security:            "+ stats.get("n_Avg") +"         "+ stats.get("N_Gap").doubleValue()+
                                    "\n\tAnalytics and Big Data:             "+ stats.get("a_Avg") +"         "+ stats.get("A_Gap").doubleValue()+
                                    "\n\tWeb & Mobile applications:          "+ stats.get("w_Avg") +"         "+ stats.get("W_Gap").doubleValue()+
                                    "\nGrand Average:                          "+ stats.get("GrandAvg"));
                            System.out.println("Percentage of Interested members:       "+ stats.get("Interested"));
                            System.out.println("Skill Shortfall:                        "+ stats.get("ShortFall"));
                            System.out.println("Standard Deviation of skills            "+ stats.get("stDev"));
                        }
                    }
                    break;
                }
                case "L":{
                    try{
                        companyList = (HashMap<String, Company>) Utility.deserializer("companies").readObject();
                        //projectList = (HashMap<String, Project>) Utility.deserializer("projects").readObject();
                        ownerList = (HashMap<String, ProjOwner>) Utility.deserializer("owners").readObject();
                        //studentList = (HashMap<String, Student>) Utility.deserializer("students").readObject();
                        //votedProjects = (Map<Student, HashMap<Project, Integer>>) Utility.deserializer("preferences").readObject();
                        //shortListed = (HashMap<Project, Integer>) Utility.deserializer("shortListed").readObject();

                        System.out.println("********** All records have successfully loaded ***********");
                    } catch (Exception exp){
                        System.out.println("Loading failed: Saved files was not found!\n");
                    }

                    break;
                }
                case "S":{

                    ObjectOutputStream serializer = Utility.serializer("companies");
                    serializer.writeObject(companyList);
                    serializer.close();
                    ObjectOutputStream serializer2 = Utility.serializer("owners");
                    serializer2.writeObject(ownerList);
                    serializer2.close();
                    ObjectOutputStream serializer3 = Utility.serializer("projects");
                    serializer3.writeObject(projectList);
                    serializer3.close();
                    ObjectOutputStream serializer4 = Utility.serializer("students");
                    serializer4.writeObject(studentList);
                    serializer4.close();
                    ObjectOutputStream serializer5 = Utility.serializer("preferences");
                    serializer5.writeObject(votedProjects);
                    serializer5.close();
                    ObjectOutputStream serializer6 = Utility.serializer("shortListed");
                    serializer6.writeObject(shortListed);
                    serializer6.close();
                    ObjectOutputStream serializer7 = Utility.serializer("teams");
                    serializer7.writeObject(teams);
                    serializer7.close();


                    System.out.println("\n>>>>>>>> All records have successfully saved <<<<<<<<<\n");

                    break;
                }
                //Quit
                case "Q":{
                    break;
                }
                default: {
                    System.out.println("<<<<<<<< Invalid Option >>>>>>>>\n");
                }
            }
        }while (!(option.equalsIgnoreCase("Q")));

    }

}
