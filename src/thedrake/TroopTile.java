package thedrake;

import java.util.ArrayList;
import java.util.List;

public class TroopTile implements Tile{

    private final Troop troop;
    private final PlayingSide side;
    private final TroopFace face;

    public TroopTile(Troop troop, PlayingSide side, TroopFace face)
    {
        this.troop = troop;
        this.side = side;
        this.face = face;
    }

    public PlayingSide side()
    {
        return side;
    }

    public TroopFace face()
    {
        return face;
    }

    public Troop troop()
    {
        return troop;
    }


    @Override
    // Returns false, can't step on a Tile with a Troop
    public boolean canStepOn()
    {
        return false;
    }

    @Override
    // Returns true, has Troop on a Tile
    public boolean hasTroop()
    {
        return true;
    }

    @Override
    public List<Move> movesFrom(BoardPos pos, GameState state) {

        List<Move> allActions = new ArrayList<>();

        for(TroopAction action : troop.actions(face))
        {
            allActions.addAll(action.movesFrom(pos, side, state));
        }

        return allActions;

    }

    // Creates new Tile with the TroopFace flipped
    public TroopTile flipped()
    {
        return new TroopTile(troop, side, face == TroopFace.AVERS ? TroopFace.REVERS : TroopFace.AVERS);
    }
}
