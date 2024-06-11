package thedrake.core;

import java.io.PrintWriter;

public class Board implements JSONSerializable{

    // for some reason the TilePos views i as column and j as row number, which goes against the usual ij pair
    private final BoardTile[][] tiles;
    private final int dimension;

    // Constructor. Makes a 2D array of BoardTile.Empty
    public Board(int dimension) {
        this.dimension = dimension;
        tiles = new BoardTile[dimension][dimension];

        for (int row = 0; row < dimension; row++)
        {
            for (int column = 0; column < dimension; column++)
            {
                tiles[row][column] = BoardTile.EMPTY;
            }
        }
    }
    public int dimension() {
        return this.dimension;
    }

    // Vrací dlaždici na zvolené pozici.
    public BoardTile at(TilePos pos) {
        return tiles[pos.j()][pos.i()];
    }

    // Vytváří novou hrací desku s novými dlaždicemi. Všechny ostatní dlaždice zůstávají stejné
    public Board withTiles(Board.TileAt ...ats) {
        Board newBoard = new Board(dimension);
        for(int row = 0 ; row < dimension; row++)
        {
            newBoard.tiles[row] = tiles[row].clone();
        }

        for(Board.TileAt tile: ats)
        {
            newBoard.tiles[tile.pos.j()][tile.pos.i()] = tile.tile;
        }

        return newBoard;
    }

    // Vytvoří instanci PositionFactory pro výrobu pozic na tomto hracím plánu
    public PositionFactory positionFactory() {
        return new PositionFactory(dimension);
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{\"dimension\":" + dimension() + ",\"tiles\":[");
        for(int row = 0; row < dimension; row++)
        {
            for(int column = 0; column < dimension; column++)
            {
                this.tiles[row][column].toJSON(writer);
                if(row != dimension - 1 || column != dimension - 1)
                {
                    writer.print(",");
                }
            }
        }
        writer.print("]}");
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }
}

