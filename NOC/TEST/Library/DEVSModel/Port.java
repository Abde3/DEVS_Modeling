/*
 * Decompiled with CFR 0_123.
 */
package Library.DEVSModel;


public class Port {
    protected EVENTTYPE EventType;
    private DEVSModel Model;
    private String Name;
    private static /* synthetic */ int[] $SWITCH_TABLE$DEVSModel$Port$EVENTTYPE;

    public Port(DEVSModel model, String name) {
        this.Name = name;
        this.Model = model;
    }

    public Port(DEVSModel model, String name, EVENTTYPE type) {
        this.Name = name;
        this.Model = model;
        this.EventType = type;
    }

    public Port(DEVSModel AModel) {
        this.Model = AModel;
    }

    public String getName() {
        return this.Name;
    }

    public DEVSModel getModel() {
        return this.Model;
    }

    public String parseEvType() {
        switch (Port.$SWITCH_TABLE$DEVSModel$Port$EVENTTYPE()[this.EventType.ordinal()]) {
            case 1: {
                return "char";
            }
            case 2: {
                return "Integer";
            }
            case 3: {
                return "Float";
            }
            case 4: {
                return "Double";
            }
            case 5: {
                return "Event";
            }
        }
        return null;
    }

    public void checkEvPort(Object ev) throws Exception {
        String evtype = ev.getClass().getSimpleName();
        if (evtype.compareTo(this.parseEvType()) != 0) {
            new Exception("incorrect event port " + this.toString());
        }
    }

    public String toString() {
        return String.valueOf(this.Model.getName()) + ".Port." + this.Name;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$DEVSModel$Port$EVENTTYPE() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$DEVSModel$Port$EVENTTYPE;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[EVENTTYPE.values().length];
        try {
            arrn[EVENTTYPE.CHAR.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EVENTTYPE.DOUBLE.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EVENTTYPE.Event.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EVENTTYPE.FLOAT.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[EVENTTYPE.INT.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$DEVSModel$Port$EVENTTYPE = arrn;
        return $SWITCH_TABLE$DEVSModel$Port$EVENTTYPE;
    }

    public static enum EVENTTYPE {
        CHAR,
        INT,
        FLOAT,
        DOUBLE,
        Event;


        EVENTTYPE(String string2, int n2) {
        }

        EVENTTYPE() {

        }
    }

}

