package Verification;

import Library.DEVSSimulator.Root;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class StateRepresentation {

    static Vector<State> visitedState = new Vector<>(100000);
    private static float currentTime = 0F;

    public static void addState( State state ) {
        if (visitedState.size() > 100000) {
            return;
        }

        visitedState.add(state);
    }


    public static void addState(String name, String state) {
        if (visitedState.size() > 100000) {
            return;
        }

        visitedState.add( new State(name, currentTime, state) );
    }

    public static void writeFile(String filePath) {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(filePath));
            visitedState.forEach( state -> writeState(out, state) );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeState ( PrintWriter out, Object element ) {
        out.println(element);
        out.flush();
    }

    public static void updateCurrentTime(float currentTime) {
        StateRepresentation.currentTime = currentTime;
    }
}
