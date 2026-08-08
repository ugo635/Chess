package com.me.chess.pieces.impl;

import com.me.chess.game.movement.Move;
import com.me.chess.vectors.Direction;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Square square, Color color) {
        super(square, color);
    }

    @Override
    public List<Point> getDefaultLegalMoves() {
        List<Point> legalMoves = new ArrayList<>();

        // Normal moves
        for (Direction dir : Direction.values()) {
            if (this.getPosition().add(dir.getDiff()).isOppositeColorPieceOrEmpty(this.board, this)) legalMoves.add(this.getPosition().add(dir.getDiff()));
        }

        // For Small Castle
        if (this.hasntMoved()) {
            List<Rook> rooks = this.board.getPiecesofType(Rook.class)
                    .stream()
                    .filter(r -> r.isRightRook && r.color == this.color)
                    .toList();

            Rook rook = rooks.isEmpty() ? null : rooks.getFirst();

            if (rook != null && rook.hasntMoved() && this.isEmptyInRange(Direction.RIGHT, 2)) legalMoves.add(this.getPosition().addX(2));
        }

        // For Long Castle
        if (this.hasntMoved()) {
            List<Rook> rooks = this.board.getPiecesofType(Rook.class)
                    .stream()
                    .filter(r -> !r.isRightRook && r.color == this.color)
                    .toList();

            Rook rook = rooks.isEmpty() ? null : rooks.getFirst();
            if (rook != null && rook.hasntMoved() && this.isEmptyInRange(Direction.LEFT, 2)) legalMoves.add(this.getPosition().addX(-2));
        }




        // KING CHECKS FILTERING
        List<Point> allPiecesLegalMoves = this.getAllPiecesLegalMoves();

        return legalMoves
                .stream()
                .filter(p -> !allPiecesLegalMoves.contains(p)) // Remove points that are attacked by a piece from the legal moves
                .toList();
    }

    public boolean isKingChecked(List<Point> allPiecesLegalMoves) {
        for (Point p : allPiecesLegalMoves) {
            if (p.equals(this.getPosition())) {
                return true;
            }
        }

        return false;
    }

    /**
     * ONLY THE LEGAL MOVES OF THIS COLOR AND THAT ISN'T A KING
     */
    public List<Point> getAllPiecesLegalMoves() {
        return this.board.getAllLegalMovesOfColor(this.color.getOpposite());
    }

    @Override
    @SuppressWarnings("DataFlowIssue")
    public void onMove(Move move) {
        // Short Castle
        if (move.from == this.getSquare() && move.to == move.from.getPosition().addX(2).getSquare(this.board)) {
            Square oldRookPos = this.getPosition().addX(3).getSquare(this.board);
            Square newRookPos = this.getPosition().addX(1).getSquare(this.board);

            Piece rook = oldRookPos.getPiece();
            oldRookPos.empty();
            rook.setSquare(newRookPos);
            newRookPos.setPiece(rook);
        }

        // Long Castle
        if (move.from == this.getSquare() && move.to == move.from.getPosition().addX(-2).getSquare(this.board)) {
            Square oldRookPos = this.getPosition().addX(-4).getSquare(this.board);
            Square newRookPos = this.getPosition().addX(-1).getSquare(this.board);

            Piece rook = oldRookPos.getPiece();
            oldRookPos.empty();
            rook.setSquare(newRookPos);
            newRookPos.setPiece(rook);
        }
    }
}