package thedrake.core;

import java.io.PrintWriter;
import java.util.List;

public class Troop implements JSONSerializable{

    private final String name;
    private Offset2D aversPivot;
    private Offset2D reversePivot;

    private List<TroopAction> aversActions;
    private List<TroopAction> reversActions;

    public Troop(String name, Offset2D aversPivot, Offset2D reversePivot, List<TroopAction> aversActions, List<TroopAction> reversActions)
    {

        this.name = name;
        this.aversPivot = aversPivot;
        this.reversePivot = reversePivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }
    public Troop(String name, Offset2D pivot, List<TroopAction> aversActions, List<TroopAction> reversActions)
    {
        this.name = name;
        this.aversPivot = pivot;
        this.reversePivot = pivot;
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }
    public Troop(String name, List<TroopAction> aversActions, List<TroopAction> reversActions)
    {
        this.name = name;
        this.aversPivot = new Offset2D(1,1);
        this.reversePivot = new Offset2D(1,1);
        this.aversActions = aversActions;
        this.reversActions = reversActions;
    }
    public String name()
    {
        return this.name;
    }
    public Offset2D pivot(TroopFace face)
    {
        return (face.name().equals("AVERS") ? aversPivot : reversePivot);
    }

    public List<TroopAction> actions(TroopFace face)
    {
        return face == TroopFace.AVERS ? aversActions : reversActions;
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"" + this.name + "\"");
    }
}
