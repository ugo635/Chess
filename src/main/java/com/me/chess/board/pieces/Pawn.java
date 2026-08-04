package com.me.chess.board.pieces;

import com.me.chess.board.Point;
import com.me.chess.board.Square;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public final int ONE = this.color == Piece.PieceColor.WHITE ? 1 : -1;
    public Pawn(Square square, Color color) {
        super(square, color);
    }

    @Override
    public List<Point> getLegalMoves() {
        List<Point> legalMoves = new ArrayList<>();
        if (this.getPosition().addY(ONE).isAnEmptySquare(board)) legalMoves.add(this.getPosition().addY(ONE));

        return legalMoves;
    }
}
