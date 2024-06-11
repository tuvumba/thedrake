package thedrake.core;

import java.io.PrintWriter;

public enum GameResult implements JSONSerializable{
    VICTORY, DRAW, IN_PLAY;

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("\"");
        writer.print(this.name());
        writer.print("\"");
    }
}
