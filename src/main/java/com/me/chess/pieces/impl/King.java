package com.me.chess.pieces.impl;

import com.me.chess.game.Move;
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
    public List<Point> getLegalMoves() {
        List<Point> legalMoves = new ArrayList<>();

        // Normal moves
        for (Direction dir : Direction.values()) {
            if (this.getPosition().add(dir.getDiff()).isOppositeColorPieceOrEmpty(this.board, this)) legalMoves.add(this.getPosition().add(dir.getDiff()));
        }

        // For small castle
        if (this.hasntMoved()) {
            Rook rook = this.board.getPiecesofType(Rook.class)
                    .stream()
                    .filter(r -> r.isRightRook && r.isOppositeColor(this))
                    .toList()
                    .getFirst();

            if (rook.hasntMoved() && this.isEmptyInRange(Direction.RIGHT, 2)) legalMoves.add(this.getPosition().addX(2));
        }



        return legalMoves;
    }

    @Override
    public void onMove(Move move) {
        if (move.from == this.getSquare() && move.to == move.from.getPosition().addX(2).getSquare(this.board)) {
            Square oldRookPos = this.getPosition().addX(3).getSquare(this.board);
            Square newRookPos = this.getPosition().addX(1).getSquare(this.board);

            Piece rook = oldRookPos.getPiece();
            oldRookPos.empty();
            rook.setSquare(newRookPos);
            newRookPos.setPiece(rook);
        }
    }
}
