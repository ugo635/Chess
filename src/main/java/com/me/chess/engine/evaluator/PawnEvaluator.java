package com.me.chess.engine.evaluator;

import com.me.chess.pieces.impl.Pawn;
import com.me.chess.vectors.Point;

public class PawnEvaluator {
    public static float getValue(Pawn pawn) {
        Point pos = pawn.getPosition();
        if ((pos.y == 4 || pos.y == 5) && (pos.x == 4 || pos.x == 5)) return 1.5f;
        else if (pos.y == pawn.EIGHT_RANK) return 9f;
        else if (pos.y == (pawn.EIGHT_RANK - pawn.ONE)) {
            if (pos.addY(1).getPiece(pawn.board) == null) return 3.5f;
            else return 2f;
        } else return 1f;
    }
}
