package thedrake;

import java.util.Optional;

public class GameState {
    private final Board board;
    private final PlayingSide sideOnTurn;
    private final Army blueArmy;
    private final Army orangeArmy;
    private final GameResult result;

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy) {
        this(board, blueArmy, orangeArmy, PlayingSide.BLUE, GameResult.IN_PLAY);
    }

    public GameState(
            Board board,
            Army blueArmy,
            Army orangeArmy,
            PlayingSide sideOnTurn,
            GameResult result) {
        this.board = board;
        this.sideOnTurn = sideOnTurn;
        this.blueArmy = blueArmy;
        this.orangeArmy = orangeArmy;
        this.result = result;
    }

    public Board board() {
        return board;
    }

    public PlayingSide sideOnTurn() {
        return sideOnTurn;
    }

    public GameResult result() {
        return result;
    }

    public Army army(PlayingSide side) {
        if (side == PlayingSide.BLUE) {
            return blueArmy;
        }

        return orangeArmy;
    }

    public Army armyOnTurn() {
        return army(sideOnTurn);
    }

    public Army armyNotOnTurn() {
        if (sideOnTurn == PlayingSide.BLUE)
            return orangeArmy;

        return blueArmy;
    }

    public Tile tileAt(TilePos pos) {

        Optional<TroopTile> tmp;

        tmp = blueArmy.boardTroops().at(pos);
        if (tmp.isPresent())
            return tmp.get();

        tmp = orangeArmy.boardTroops().at(pos);
        if (tmp.isPresent())
            return tmp.get();

        return board.at(pos);
    }

    private boolean canStepFrom(TilePos origin) {
        if (result != GameResult.IN_PLAY)
            return false;


        if (sideOnTurn == PlayingSide.ORANGE) {
            if (!orangeArmy.boardTroops().isLeaderPlaced() || orangeArmy.boardTroops().isPlacingGuards())
                return false;
            else return orangeArmy.boardTroops().at(origin).isPresent();
        } else {
            if (!blueArmy.boardTroops().isLeaderPlaced() || blueArmy.boardTroops().isPlacingGuards())
                return false;
            else return blueArmy.boardTroops().at(origin).isPresent();

        }
    }

    private boolean canStepTo(TilePos target) {
        if (result != GameResult.IN_PLAY)
            return false;
        else if(target == TilePos.OFF_BOARD)
            return false;
        else return tileAt(target).canStepOn();
    }

    private boolean canCaptureOn(TilePos target) {
        if (result != GameResult.IN_PLAY)
            return false;
        else if(target == TilePos.OFF_BOARD)
            return false;
        else {
            if (sideOnTurn == PlayingSide.ORANGE) {
                return blueArmy.boardTroops().at(target).isPresent();
            } else {
                return orangeArmy.boardTroops().at(target).isPresent();
            }
        }
    }

    public boolean canStep(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canStepTo(target);
    }

    public boolean canCapture(TilePos origin, TilePos target) {
        return canStepFrom(origin) && canCaptureOn(target);
    }

    public boolean canPlaceFromStack(TilePos target) {
        if (result != GameResult.IN_PLAY)
            return false;

        if(target == TilePos.OFF_BOARD)
            return false;

        if(blueArmy.boardTroops().at(target).isPresent() || orangeArmy.boardTroops().at(target).isPresent() || !board.at(target).canStepOn())
        {
            return false;
        }

        if (sideOnTurn == PlayingSide.BLUE) {
            if(blueArmy.stack().isEmpty())
                return false;
            if (!blueArmy.boardTroops().isLeaderPlaced()) {
                return target.row() == 1 && board.at(target).canStepOn();
            } else if (blueArmy.boardTroops().isPlacingGuards()) {
                return target.isNextTo(blueArmy.boardTroops().leaderPosition());
            } else {
                for (TilePos pos : target.neighbours()) //get all neighbours
                {
                    if (blueArmy.boardTroops().at(pos).isPresent()) // check presence of a tile in an Army
                    {
                        if (blueArmy.boardTroops().at(pos).get().hasTroop()) // if it has neighbour Troop, placement is correct
                            return true;
                    }
                }
                return false;
            }
        } else {
            if(orangeArmy.stack().isEmpty())
                return false;
            if (!orangeArmy.boardTroops().isLeaderPlaced()) {
                return target.row() == board.dimension() && board.at(target).canStepOn();
            } else if (orangeArmy.boardTroops().isPlacingGuards()) {
                return target.isNextTo(orangeArmy.boardTroops().leaderPosition());
            } else {

                for (TilePos pos : target.neighbours()) {
                    if (orangeArmy.boardTroops().at(pos).isPresent()) {
                        if (orangeArmy.boardTroops().at(pos).get().hasTroop())
                            return true;
                    }
                }
                return false;
            }
        }
    }

    public GameState stepOnly(BoardPos origin, BoardPos target) {
        if (canStep(origin, target))
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().troopStep(origin, target), GameResult.IN_PLAY);

        throw new IllegalArgumentException();
    }

    public GameState stepAndCapture(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopStep(origin, target).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState captureOnly(BoardPos origin, BoardPos target) {
        if (canCapture(origin, target)) {
            Troop captured = armyNotOnTurn().boardTroops().at(target).get().troop();
            GameResult newResult = GameResult.IN_PLAY;

            if (armyNotOnTurn().boardTroops().leaderPosition().equals(target))
                newResult = GameResult.VICTORY;

            return createNewGameState(
                    armyNotOnTurn().removeTroop(target),
                    armyOnTurn().troopFlip(origin).capture(captured), newResult);
        }

        throw new IllegalArgumentException();
    }

    public GameState placeFromStack(BoardPos target) {
        if (canPlaceFromStack(target)) {
            return createNewGameState(
                    armyNotOnTurn(),
                    armyOnTurn().placeFromStack(target),
                    GameResult.IN_PLAY);
        }

        throw new IllegalArgumentException();
    }

    public GameState resign() {
        return createNewGameState(
                armyNotOnTurn(),
                armyOnTurn(),
                GameResult.VICTORY);
    }

    public GameState draw() {
        return createNewGameState(
                armyOnTurn(),
                armyNotOnTurn(),
                GameResult.DRAW);
    }

    private GameState createNewGameState(Army armyOnTurn, Army armyNotOnTurn, GameResult result) {
        if (armyOnTurn.side() == PlayingSide.BLUE) {
            return new GameState(board, armyOnTurn, armyNotOnTurn, PlayingSide.BLUE, result);
        }

        return new GameState(board, armyNotOnTurn, armyOnTurn, PlayingSide.ORANGE, result);
    }
}
