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
            value += getValueOfWhitePiece(piece);
        }

        return value;
    }

    private static float getBlackValue(Board board) {
        float value = 0;

        List<Piece> pieces = board.getPieces(Piece.PieceColor.BLACK);

        for (Piece piece : pieces) {
            value -= getValueOfBlackPiece(piece);
        }

        return value;
    }


    private static float getValueOfBlackPiece(Piece piece) {
        // The closer the pawn is the the center the more valuable it is
        // The more attack square pieces attack the better
        if (piece instanceof Pawn pawn) return PawnEvaluator.getValue(pawn);
        else return piece.value;
    }

    private static float getValueOfWhitePiece(Piece piece) {
        return piece.getValue();
    }
}
