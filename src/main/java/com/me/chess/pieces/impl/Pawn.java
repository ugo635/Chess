package com.me.chess.pieces.impl;

import com.me.chess.vectors.Direction;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public final int ONE = this.color == Piece.PieceColor.WHITE ? 1 : -1;
    public final Direction DIRECTION = this.color == Piece.PieceColor.WHITE ? Direction.UP : Direction.DOWN;
    public Pawn(Square square, Color color) {
        super(square, color);
    }

    @Override
    public List<Point> getLegalMoves() {
        List<Point> legalMoves = new ArrayList<>();

        // Going straight once if there's nothing
        if (this.getPosition().addY(ONE).isAnEmptySquare(board)) legalMoves.add(this.getPosition().addY(ONE));

        // Going straight twice if there's nothing in the first 2 & it's their first move
        if (this.isEmptyInRange(DIRECTION, 2) && this.hasntMoved()) legalMoves.add(this.getPosition().addY(2 * ONE));

        // Diagonal capture
        if (!this.getPosition().add(1, ONE).isAnEmptySquare(board)) legalMoves.add(this.getPosition().add(1, ONE));
        if (!this.getPosition().add(-1, ONE).isAnEmptySquare(board)) legalMoves.add(this.getPosition().add(-1, ONE));

        return legalMoves;
    }
}
