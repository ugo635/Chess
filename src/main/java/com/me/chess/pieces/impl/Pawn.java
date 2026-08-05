package com.me.chess.pieces.impl;

import com.me.chess.game.Move;
import com.me.chess.vectors.Direction;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public final int ONE = this.color == Piece.PieceColor.WHITE ? 1 : -1;
    public final int TWO = this.color == Piece.PieceColor.WHITE ? 2 : -2;
    public final int START_RANK = (this.color == Piece.PieceColor.WHITE ? 2 : 7);
    public final Direction DIRECTION = this.color == Piece.PieceColor.WHITE ? Direction.UP : Direction.DOWN;
    private boolean canBeCapturedEnPassant = false;
    private int capturableEnPassantMove = -1;

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
        if (this.getPosition().add(1, ONE).isOppositeColorPiece(board, this)) legalMoves.add(this.getPosition().add(1, ONE));
        if (this.getPosition().add(-1, ONE).isOppositeColorPiece(board, this)) legalMoves.add(this.getPosition().add(-1, ONE));

        // En passant
        if (canEnPassantOnTheRight()) legalMoves.add(this.getPosition().add(1, ONE));
        if (canEnPassantOnTheLeft()) legalMoves.add(this.getPosition().add(-1, ONE));

        return legalMoves;
    }

    /**
     * Mark the pawn capturable En Passant if it just moved 2 squares
     * Handles the En Passant
     */
    @Override
    public void onMove(Move move) {
        // Handle the En Passant
        if (move.from.getPiece() instanceof Pawn pawn) {
            if (pawn.canEnPassantOnTheLeft()) {
                pawn.getPosition().addX(-1).getPiece(this.board).empty();
            } else if (pawn.canEnPassantOnTheRight()) {
                pawn.getPosition().addX(1).getPiece(this.board).empty();
            }
        }

        // If it moved 2 squares straight & there was no move between that moment & now
        if (this.move == 1 && move.to.getPosition().addY(-this.TWO).y == this.START_RANK && (this.capturableEnPassantMove == -1 || this.capturableEnPassantMove == move.getTotalMove())) {
            this.capturableEnPassantMove = move.getTotalMove();
            this.canBeCapturedEnPassant = true;
        } else {
            this.canBeCapturedEnPassant = false;
        }
    }

    public boolean canBeCapturedEnPassant() {
        return this.canBeCapturedEnPassant;
    }

    public boolean canEnPassantOnTheRight() {
        Piece piece = this.getPosition().addX(1).getPiece(this.board);
        if (!(piece instanceof Pawn pawn)) return false;

        return pawn.isOppositeColor(this) && pawn.canBeCapturedEnPassant();
    }

    public boolean canEnPassantOnTheLeft() {
        Piece piece = this.getPosition().addX(-1).getPiece(this.board);
        if (!(piece instanceof Pawn pawn)) return false;

        return pawn.isOppositeColor(this) && pawn.canBeCapturedEnPassant();
    }

}
