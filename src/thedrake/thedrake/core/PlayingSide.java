package thedrake.core;

import java.io.PrintWriter;

public enum PlayingSide implements JSONSerializable{
    ORANGE, BLUE;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"" + (this == PlayingSide.BLUE ? "BLUE" : "ORANGE") + "\"");
    }
}
