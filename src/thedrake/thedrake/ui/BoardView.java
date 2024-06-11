package thedrake.ui;

import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import thedrake.core.*;

public class BoardView extends GridPane implements TileViewContext {

    private GameState gameState;

    private ValidMoves validMoves;

    private TileView selected;

    private final ObjectProperty<PlayingSide> playingSide = new SimpleObjectProperty<>();

    private final ObjectProperty<GameResult> gameResult = new SimpleObjectProperty<>();

    private final ObjectProperty<List<Troop>> stackBlue = new SimpleObjectProperty<>();
    private final ObjectProperty<List<Troop>> stackOrange = new SimpleObjectProperty<>();

    public BoardView(GameState gameState) {

        this.gameState = gameState;
        this.validMoves = new ValidMoves(gameState);


        PositionFactory positionFactory = gameState.board().positionFactory();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                BoardPos boardPos = positionFactory.pos(x, 3 - y);
                TileView tobeInserted = new TileView(boardPos, gameState.tileAt(boardPos), this, true);
                tobeInserted.setEffect(new DropShadow());
                tobeInserted.setStyle("-fx-border-color: GREY;");
                add(tobeInserted, x, y);
            }
        }



        setHgap(3);
        setVgap(3);
        setPadding(new Insets(10));
        setAlignment(Pos.CENTER);
        update();
    }

    public void resign()
    {
        if(!gameResult.get().equals(GameResult.VICTORY))
        {
            playingSide.set(this.currentSide() == PlayingSide.ORANGE ? PlayingSide.BLUE : PlayingSide.ORANGE);
            gameResult.set(GameResult.VICTORY);
        }

    }

    public ObjectProperty<GameResult> gameResultProperty() {
        return gameResult;
    }
    public ObjectProperty<List<Troop>> stackBlueProperty() {
        return stackBlue;
    }
    public ObjectProperty<List<Troop>> stackOrangeProperty() {
        return stackOrange;
    }

    public ObjectProperty<PlayingSide> playingSide(){return playingSide;}

    private void update()
    {
        updateStackBlue();
        updateStackOrange();
        updateGameResult();
        updatePlayingSide();
        updateTiles();

        if(validMoves.allMoves().isEmpty())
        {
            resign();
        }
    }
    private void updateGameResult() {
        gameResult.set(gameState.result());
    }

    private void updateStackBlue() {
        stackBlue.set(gameState.army(PlayingSide.BLUE).stack());
    }

    private void updateStackOrange() {
        stackOrange.set(gameState.army(PlayingSide.ORANGE).stack());
    }

    private void updatePlayingSide()
    {
        playingSide.set(this.gameState.sideOnTurn());
    }


    @Override
    public void tileViewSelected(TileView tileView) {
        if (selected != null && selected != tileView)
            selected.unselect();

        selected = tileView;

        clearMoves();
        if(tileView.position() != TilePos.OFF_BOARD)
        {
            showMoves(validMoves.boardMoves(new BoardPos(4, tileView.position().i(), tileView.position().j())));
        }
        else {
            showMoves(validMoves.movesFromStack());
        }
    }


    @Override
    public void executeMove(Move move) {
        selected.unselect();
        selected = null;
        clearMoves();
        gameState = move.execute(gameState);
        validMoves = new ValidMoves(gameState);
        update();
        // so, when victory -> won the opposite side
    }

    @Override
    public PlayingSide currentSide() {
        return gameState.sideOnTurn();
    }

    private void updateTiles() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.setTile(gameState.tileAt(tileView.position()));
            tileView.update();
        }
    }

    private void clearMoves() {
        for (Node node : getChildren()) {
            TileView tileView = (TileView) node;
            tileView.clearMove();
        }
    }

    private void showMoves(List<Move> moveList) {
        for (Move move : moveList)
            tileViewAt(move.target()).setMove(move);

    }

    private TileView tileViewAt(BoardPos target) {
        int index = (3 - target.j()) * 4 + target.i();
        return (TileView) getChildren().get(index);
    }

}
