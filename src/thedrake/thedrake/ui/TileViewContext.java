package thedrake.ui;

import thedrake.core.Move;
import thedrake.core.PlayingSide;

public interface TileViewContext {

    void tileViewSelected(TileView tileView);

    void executeMove(Move move);

    PlayingSide currentSide();

}
