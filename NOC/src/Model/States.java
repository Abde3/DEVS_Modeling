package Model;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class States {
    HashMap<Map.Entry<Float, String>, String> states = new HashMap( );

    private static States ourInstance = new States();

    public static States getInstance() {
        return ourInstance;
    }

    private States() {

    }

    public void addVisitedState(Float time, String nocName, String state) {
        states.put( new AbstractMap.SimpleEntry<>( time, nocName), state);
    }
}
