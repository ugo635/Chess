package com.me.chess.board.pieces;

import com.me.chess.board.Point;
import com.me.chess.board.Square;
import javafx.scene.paint.Color;

import java.util.List;

public class Bishop extends Piece {
    public Bishop(Square square, Color color) {
        super(square, color);
    }

    @Override
    public List<Point> getLegalMoves() {
        return List.of();
    }
}
