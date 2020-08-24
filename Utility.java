import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Utility {
    private static String s;
    private static Map<Object,Integer> map = new HashMap<>();
    // a general method for writing file
    public static PrintWriter writer(String outputFile) throws IOException {
        FileWriter fw = new FileWriter(outputFile, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        return pw;
    }

    //a general method for reading file
    public static Scanner reader(String fileName) throws Exception {
        File myFile = new File(fileName);
        Scanner input = new Scanner(myFile);
        return input;
    }

    public static ObjectOutputStream serializer (String outputFile) throws IOException {
        return new ObjectOutputStream (new FileOutputStream(outputFile+ ".dat", true));
    }

    public static ObjectInputStream deserializer (String inputFile) throws IOException {
        return new ObjectInputStream (new FileInputStream(inputFile + ".dat"));
    }

    public static boolean teamShaped(HashMap< Project, Integer> proj){
        int totalAllocated = 0;
        for (Map.Entry< Project, Integer> entry : proj.entrySet()) {
            Project v = entry.getKey();
            totalAllocated += v.getTeam().size();
        }
        if (totalAllocated == 20){
            return true;
        }
        return false;
    }
    public static double StDev(ArrayList<Double> theList){
        double total = 0;
        int n = 0;
        for (double instance: theList){
            total += instance;
            n++;
        }
        double mean = total / n;
        double sd = 0;
        double sigma = 0;
        for (double instance: theList){
            sigma += Math.pow((instance - mean), 2);
        }
        sd = Math.pow((sigma / n), 0.5);
        return sd;
    }



}
