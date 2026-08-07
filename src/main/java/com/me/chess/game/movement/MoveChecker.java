package com.me.chess.game.movement;

import com.me.chess.board.Board;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.impl.King;
import com.me.chess.vectors.Point;

import java.util.List;

public class MoveChecker {
    @SuppressWarnings("DataFlowIssue")
    public static boolean isLegal(Move move) {
        // Return true directly if it's a renderless move
        if (move.from.renderer == null) return true;

        List<Point> legalMoves = move.from.getPiece().getLegalMoves();

        boolean contains = legalMoves.contains(move.to.getPosition());
        boolean checked = isKingChecked(move.board, move.from.getPiece().color) && !(move.from.getPiece() instanceof King);
        boolean checkedAfterMove = checkedAfterMove(move);
        return contains && ((!checked && !checkedAfterMove) || (checked && !checkedAfterMove));
    }

    public static boolean isKingChecked(Board board, Piece.PieceColor color) {
        return board.getPiecesofType(King.class)
                .stream()
                .filter(k -> k.color == color)
                .toList()
                .getFirst()
                .isKingChecked(board.getAllLegalMovesOfColor(color.getOpposite()));
    }

    public static boolean checkedAfterMove(Move move) {
        return isKingChecked(simulateBoardAfterMove(move.board, move), move.from.getPiece().color);
    }

    public static Board simulateBoardAfterMove(Board currentBoard, Move move) {
        //System.out.println(currentBoard);
        //System.out.println(move);
        Board board = currentBoard.copy();
        board.move = new Move(board, move.from.getPosition().getSquare(board), move.to.getPosition().getSquare(board));
        board.move.move();
        //System.out.println(board);

        return board;
    }
}
