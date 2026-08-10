package com.me.chess.pieces.impl;

import com.me.chess.engine.evaluator.KnightEvaluator;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Square square, Color color) {
        super(square, color);
        this.value = 3;
    }

    @Override
    public float getValue() {
        return KnightEvaluator.getValue(this);
    }

    @Override
    public List<Point> getDefaultLegalMoves() {
        List<Point> legalMoves = new ArrayList<>();

        for (int i : new int[] {-2, 2}) {
            for (int j : new int[] {-1, 1}) {
                if (this.getPosition().add(i, j).isOppositeColorPieceOrEmpty(this.board, this)) legalMoves.add(this.getPosition().add(i, j));
                if (this.getPosition().add(j, i).isOppositeColorPieceOrEmpty(this.board, this)) legalMoves.add(this.getPosition().add(j, i));
            }
        }

        return legalMoves;
    }
}
