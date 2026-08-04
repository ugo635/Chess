package com.me.chess.game;

import com.me.chess.board.Board;
import com.me.chess.board.Point;
import com.me.chess.board.Square;
import com.me.chess.board.pieces.Piece;

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

    public boolean isLegal() {
        List<Point> legalMoves = from.getPiece().getLegalMoves();
        boolean isLegal = legalMoves.contains(to.getPosition());

        //System.out.println("Legal moves:" + legalMoves);
        //System.out.println("From: " + from.getPosition());
        //System.out.println("To:" + to.getPosition());

        return isLegal;
    }

    public void move() throws IllegalMoveException {
        if (!this.isLegal()) throw new IllegalMoveException("Move isn't legal");
        Piece fromPiece = this.from.getPiece();
        fromPiece.move++;

        // Replace the square we go to with the piece & make the square from empty
        if (!this.to.isEmpty()) {
            this.to.getPiece().empty();
        }
        this.to.setPiece(fromPiece);
        this.from.empty();

        // Update the piece to have the new square
        fromPiece.setSquare(this.to);

        // Resets the highlight
        this.from.setHighlighted(false);

        // Resets everything to null
        this.from = null;
        this.to = null;
    }
}
