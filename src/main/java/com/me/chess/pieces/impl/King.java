package com.me.chess.pieces.impl;

import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;
import javafx.scene.paint.Color;

import java.util.List;

public class King extends Piece {
    public King(Square square, Color color) {
        super(square, color);
    }

    @Override
    public List<Point> getLegalMoves() {
        return List.of();
    }
}
