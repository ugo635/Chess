package com.me.chess.pieces.impl;

import com.me.chess.vectors.Direction;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    public boolean isRightRook;

    public Rook(Square square, Color color) {
        super(square, color);
    }

    @Override
    public List<Point> getLegalMoves() {
        List<Point> legalMoves = new ArrayList<>();

        for (Direction dir : new Direction[] {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT}) {
            int range = this.isEmptyInRange(dir);
            for (int i = 1; i <= range; i++) {
                legalMoves.add(this.getPosition().add(dir.getDiff().times(i)));
            }
        }

        return legalMoves;
    }
}
