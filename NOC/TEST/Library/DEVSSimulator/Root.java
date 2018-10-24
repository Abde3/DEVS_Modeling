/*
 * Decompiled with CFR 0_123.
 */
package Library.DEVSSimulator;


import Library.DEVSModel.DEVSAtomic;
import Library.DEVSModel.DEVSCoupled;
import Library.DEVSModel.DEVSModel;
import Library.Messages.DMSG;
import Library.Messages.IMSG;
import Library.Messages.INMSG;
import Library.Messages.Message;
import Verification.StateRepresentation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

public class Root
extends DEVSProcess {

    public float getCurrentTime() {
        return CurrentTime;
    }

    private volatile float CurrentTime = 0.0f;
    private long start_time;
    private long exec_time;
    private boolean printed = false;
    private float ENDSIMULATIONTIME;
    private static PrintWriter report;
    DEVSCoupled ModelToSimulate;

    public Root() {
        this.Parent = null;
        try {
            report = new PrintWriter(new BufferedWriter(new FileWriter("output/report")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Root(float end) {
        this.ENDSIMULATIONTIME = end;
        this.Parent = null;
        try {
            report = new PrintWriter(new BufferedWriter(new FileWriter("output/report")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Root(DEVSProcess p, float end) {
        this.ENDSIMULATIONTIME = end;
        this.Child.add(p);
        ((DEVSProcess)this.Child.get((int)0)).Parent = this;
        this.start_time = System.nanoTime();
    }

    public Root(DEVSCoupled coupledmodel, float end) {
        this.ModelToSimulate = coupledmodel;
        DEVSProcess coordinator = this.makeCoordinator(coupledmodel);
        this.Child.add(coordinator);
        coordinator.setParent(this);
        this.ENDSIMULATIONTIME = end;
        this.start_time = System.nanoTime();
        try {
            report = new PrintWriter(new BufferedWriter(new FileWriter("output/report")));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void receiveDMSG(Message msg) {
        if (this.continueS(msg)) {
            float lasttime = this.CurrentTime;
            this.CurrentTime = ((DMSG)msg).getTime();

            StateRepresentation.updateCurrentTime(CurrentTime);

            if (lasttime != this.CurrentTime) {
                report.println("\ntime:" + this.CurrentTime);
            }
            this.notifyChild(new INMSG(this.CurrentTime, this, msg.getSource()));
        }
    }

    @Override
    public void receiveIMSG(Message msg) {
    }

    @Override
    public void receiveINMSG(Message msg) {
    }

    @Override
    public void receiveXMSG(Message msg) {
    }

    @Override
    public void receiveYMSG(Message msg) {
    }

    public void sendIMSG(Message msg) {
        msg.setSource(this);
        msg.setTarget((DEVSProcess)this.Child.get(0));
        this.notifyChild(msg);
    }

    public synchronized void sendIMSG(float time) {
        IMSG imsg = new IMSG(0.0f);
        imsg.setSource(this);
        imsg.setTarget((DEVSProcess)this.Child.get(0));
        if (this.continueS(imsg)) {
            this.notifyChildP = new NotificationChild(this, imsg);
        }
    }

    public void sendINMSG(Message msg) {
        msg.setSource(this);
        msg.setTarget((DEVSProcess)this.Child.get(0));
    }

    @Override
    public void update(Message msg) {

        switch (msg.getType()) {
            case 4: {
                this.receiveDMSG(msg);
                break;
            }
            case 3: {
                break;
            }
            default: {
                System.err.println("error");
            }
        }
    }

    public boolean continueS(Message msg) {
        if (msg.getTime() >= this.ENDSIMULATIONTIME) {
            if (!this.printed) {
                this.CurrentTime = this.ENDSIMULATIONTIME;
                this.printed = true;
                this.exec_time = System.nanoTime() - this.start_time;
                report.println("\n======================================================");
                this.printReport(this.ModelToSimulate);
                report.println("|Simulation Time: " + this.ENDSIMULATIONTIME + " t.u");
                report.println("|Simulation Execution Time: " + this.exec_time + " ns");
                report.println("======================================================");
                this.getChild().firstElement().getModel().endSim();
                report.close();
                this.printed = true;
            }
            return false;
        }
        return true;
    }

    public void startSimulation() {
        report.println("\ntime:" + this.CurrentTime);
        this.sendIMSG(0.0f);
    }

    private DEVSProcess make(DEVSCoupled coupledmodel) {
        Coordinator coord = new Coordinator(coupledmodel);
        return coord;
    }

    public DEVSProcess makeCoordinator(DEVSCoupled coupledmodel) {
        Vector<DEVSProcess> vector = new Vector<DEVSProcess>();
        Coordinator coordinator = new Coordinator(coupledmodel);
        int i = 0;
        while (i < coupledmodel.getSubModels().size()) {
            if (coupledmodel.getSubModels().get(i) instanceof DEVSAtomic) {
                vector.add(new Simulator((DEVSAtomic)coupledmodel.getSubModels().get(i)));
                vector.lastElement().setParent(coordinator);
            } else {
                vector.add(this.makeCoordinator((DEVSCoupled)coupledmodel.getSubModels().get(i)));
                vector.lastElement().setParent(coordinator);
            }
            ++i;
        }
        coordinator.addChild(vector);
        return coordinator;
    }

    public void printReport(DEVSCoupled model) {
        if (model instanceof DEVSCoupled) {
            int i = 0;
            while (i < model.getSubModels().size()) {
                if (model.getSubModels().get(i) instanceof DEVSCoupled) {
                    this.printReport((DEVSCoupled)model.getSubModels().get(i));
                } else {
                    ((DEVSAtomic)model.getSubModels().get(i)).printFinalReport();
                }
                ++i;
            }
        }
    }

    @Override
    public DEVSModel getModel() {
        return null;
    }

    public static PrintWriter getReport() {
        return report;
    }
}

