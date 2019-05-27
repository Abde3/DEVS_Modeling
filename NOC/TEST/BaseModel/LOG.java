package BaseModel;

import java.io.*;

public class LOG {

    public static void logThis(Object coordinate, Object currentData) {

        try (FileWriter fw = new FileWriter("C:\\Users\\Abdelhak khemiri\\Desktop\\RESULTSSIMU\\"+coordinate+".txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println( coordinate + "->" + currentData  );
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printThis(Object source , String data) {
        System.out.println(source + " : " + data);
    }

}
