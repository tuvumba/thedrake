package thedrake.ui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import thedrake.core.*;

import java.util.Optional;

public class TileView extends Pane {

    private TilePos tilepos;

    private Tile tile;

    private TileBackgrounds backgrounds = new TileBackgrounds();

    private Border selectBorder = new Border(
        new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(3)));

    private TileViewContext tileViewContext;

    private Move move;

    private final ImageView moveImage;

    public final boolean clickable;


    public TileView(TilePos pos, Tile tile, TileViewContext tileViewContext, boolean clickable) {
        this.tilepos = pos;
        this.tile = tile;
        this.tileViewContext = tileViewContext;
        this.clickable = clickable;

        setPrefSize(100, 100);
        update();

        setOnMouseClicked(e -> onClick());

        moveImage = new ImageView(getClass().getResource("/icons/move.png").toString());
        moveImage.setVisible(false);
        getChildren().add(moveImage);
    }


    private void onClick() {
        if (move != null)
            tileViewContext.executeMove(move);
        else if (tile.hasTroop() && clickable)
            select();
    }

    public void select() {
        setBorder(selectBorder);
        tileViewContext.tileViewSelected(this);
    }

    public void unselect() {
        setBorder(null);
    }

    public void update() {
        setBackground(backgrounds.get(tile));
    }

    public void setMove(Move move) {
        this.move = move;
        moveImage.setVisible(true);

    }

    public void clearMove() {
        this.move = null;
        moveImage.setVisible(false);
    }

    public TilePos position() {
        return tilepos;
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

}
