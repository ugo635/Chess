package com.me.chess.engine.evaluator;

import com.me.chess.board.Board;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.impl.Pawn;

import java.util.List;

public class Evaluator {
    public static float evaluate(Board board) {
        return getWhiteValue(board) + getBlackValue(board);
    }

    private static float getWhiteValue(Board board) {
        float value = 0;

        List<Piece> pieces = board.getPieces(Piece.PieceColor.WHITE);

        for (Piece piece : pieces) {
            value += getValueOf(piece);
        }

        return value;
    }

    private static float getBlackValue(Board board) {
        float value = 0;

        List<Piece> pieces = board.getPieces(Piece.PieceColor.BLACK);

        for (Piece piece : pieces) {
            value -= getValueOf(piece);
        }

        return value;
    }


    private static float getValueOf(Piece piece) {
        return piece.getValue();
    }
}