package Model;

public class Pretty_print {
    public static void trace(String name, String s) {
        System.out.println(name + " : " + s);
    }

    public static void trace_err(String name, String received_unexpected_cmd) {
        System.err.println(name + " : " + received_unexpected_cmd);
    }
}
