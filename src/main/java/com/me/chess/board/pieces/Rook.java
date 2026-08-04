package com.me.chess.board.pieces;

import com.me.chess.board.Point;
import com.me.chess.board.Square;
import javafx.scene.paint.Color;

import java.util.List;

public class Rook extends Piece {
    public Rook(Square square, Color color) {
        super(square, color);
    }

    @Override
    public List<Point> getLegalMoves() {
        return List.of();
    }
}
