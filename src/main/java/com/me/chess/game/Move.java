package com.me.chess.game;

import com.me.chess.board.Board;
import com.me.chess.pieces.impl.Pawn;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;

import java.util.List;

public class Move {
    public final Board board;
    public Square from;
    public Square to;

    public Move(Board board, Square from, Square to) {
        this.board = board;
        this.from = from;
        this.to = to;
    }

    @SuppressWarnings("DataFlowIssue")
    public boolean isLegal() {
        List<Point> legalMoves = from.getPiece().getLegalMoves();
        boolean isLegal = legalMoves.contains(to.getPosition());

        //System.out.println("Legal moves:" + legalMoves);
        //System.out.println("From: " + from.getPosition());
        //System.out.println("To:" + to.getPosition());

        return isLegal;
    }

    @SuppressWarnings("DataFlowIssue")
    public void move() throws IllegalMoveException {
        if (!this.isLegal()) throw new IllegalMoveException("Move isn't legal");
        Piece fromPiece = this.from.getPiece();

        // Update the moves counter
        this.board.totalMoves++;
        fromPiece.move++;

        // Remove the piece from the destination square if exists
        if (!this.to.isEmpty()) {
            this.to.getPiece().empty();
        }

        // Mark the pawn capturable En Passant if it just moved 2 squares
        this.board.getPieces().forEach(p -> p.onMove(this));

        // Adds the piece to the destination square and remove the piece from the start square
        this.to.setPiece(fromPiece);
        this.from.empty();

        // Update the piece to have the new square
        fromPiece.setSquare(this.to);

        // Resets the highlight
        this.from.resetState();

        // Resets everything to null
        this.from = null;
        this.to = null;
    }

    public int getTotalMove() {
        return this.board.totalMoves;
    }

    @Override
    public String toString() {
        return String.format("Move(%s, %s)", this.from, this.to);
    }
}
