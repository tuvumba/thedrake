package thedrake.core;

import java.io.PrintWriter;

public interface JSONSerializable {
    public void toJSON(PrintWriter writer);
}
