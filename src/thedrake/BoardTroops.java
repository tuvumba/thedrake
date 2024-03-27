package thedrake;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BoardTroops {
    private final PlayingSide playingSide;
    private final Map<BoardPos, TroopTile> troopMap;
    private final TilePos leaderPosition;
    private final int guards;


    public BoardTroops(PlayingSide playingSide) {
        this.playingSide = playingSide;
        troopMap = Collections.emptyMap();
        leaderPosition = TilePos.OFF_BOARD;
        guards = 0;
    }

    public BoardTroops(
            PlayingSide playingSide,
            Map<BoardPos, TroopTile> troopMap,
            TilePos leaderPosition,
            int guards) {
        this.playingSide = playingSide;
        this.troopMap = troopMap;
        this.leaderPosition = leaderPosition;
        this.guards = guards;
    }

    public Optional<TroopTile> at(TilePos pos) {
        if (troopMap.containsKey(pos)) {
            return Optional.of(troopMap.get(pos));
        } else return Optional.empty();
    }

    public PlayingSide playingSide() {
        return playingSide;
    }

    public TilePos leaderPosition() {
        return leaderPosition;
    }

    public int guards() {
        return guards;
    }

    public boolean isLeaderPlaced() {
        return !(leaderPosition.equals(TilePos.OFF_BOARD));
    }

    public boolean isPlacingGuards() {
        return isLeaderPlaced() && guards < 2;
    }

    public Set<BoardPos> troopPositions() {
        return troopMap.keySet();
    }

    public BoardTroops placeTroop(Troop troop, BoardPos target) {

        if (troopMap.containsKey(target)) {
            throw new IllegalArgumentException("Cannot place a troop on an occupied position.");
        }

        Map<BoardPos, TroopTile> tmpMap = new HashMap<>(troopMap);


        if (!isLeaderPlaced()) {
            //create and place a leader
            tmpMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
            return new BoardTroops(playingSide, tmpMap, target, guards);

        } else if (isPlacingGuards()) {
            tmpMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
            return new BoardTroops(playingSide, tmpMap, leaderPosition, guards + 1);
            // create and place a guard, update guards
        } else {
            tmpMap.put(target, new TroopTile(troop, playingSide, TroopFace.AVERS));
            return new BoardTroops(playingSide, tmpMap, leaderPosition, guards);
        }

    }

    public BoardTroops troopStep(BoardPos origin, BoardPos target) {
        Map<BoardPos, TroopTile> tmpMap = new HashMap<>(troopMap);

        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (at(origin).isEmpty())
            throw new IllegalArgumentException("Origin is empty while moving troop");

        if (tmpMap.containsKey(target))
            throw new IllegalArgumentException("Target tile is occupied");


        tmpMap.put(target, tmpMap.get(origin).flipped());
        tmpMap.remove(origin);

        if (origin.equals(leaderPosition)) {
            return new BoardTroops(playingSide == PlayingSide.BLUE ? PlayingSide.ORANGE : PlayingSide.BLUE,
                    tmpMap, target, guards);
        } else {
            return new BoardTroops(playingSide == PlayingSide.BLUE ? PlayingSide.ORANGE : PlayingSide.BLUE,
                    tmpMap, leaderPosition, guards);
        }


    }

    public BoardTroops troopFlip(BoardPos origin) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot move troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot move troops before guards are placed.");
        }

        if (!at(origin).isPresent())
            throw new IllegalArgumentException();

        Map<BoardPos, TroopTile> newTroops = new HashMap<>(troopMap);
        TroopTile tile = newTroops.remove(origin);
        newTroops.put(origin, tile.flipped());

        return new BoardTroops(playingSide(), newTroops, leaderPosition, guards);
    }

    public BoardTroops removeTroop(BoardPos target) {
        if (!isLeaderPlaced()) {
            throw new IllegalStateException(
                    "Cannot remove troops before the leader is placed.");
        }

        if (isPlacingGuards()) {
            throw new IllegalStateException(
                    "Cannot remove troops before guards are placed.");
        }

        if (!at(target).isPresent())
            throw new IllegalArgumentException();

        if (!troopMap.containsKey(target)) {
            throw new IllegalStateException(
                    "Empty tile when attempting to remove troop");
        }

        Map<BoardPos, TroopTile> tmpMap = new HashMap<>(troopMap);
        tmpMap.remove(target);

        if (target.equals(leaderPosition)) {
            return new BoardTroops(playingSide, tmpMap, TilePos.OFF_BOARD, guards);
        } else {
            return new BoardTroops(playingSide, tmpMap, leaderPosition, guards);
        }
    }
}
