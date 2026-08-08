package com.me.chess.game.movement;

import com.me.chess.board.Board;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.impl.King;
import com.me.chess.vectors.Point;

import java.util.ArrayList;
import java.util.List;

public class MoveChecker {
    @SuppressWarnings("DataFlowIssue")
    public static boolean isLegal(Move move) {
        // Return true directly if it's a renderless move
        if (move.from.renderer == null) return true;

        Piece piece = move.from.getPiece();
        List<Point> legalMoves = piece.getLegalMoves();

        // Must be one of the piece's pseudo-legal moves, and must not leave our own king in check
        if (!legalMoves.contains(move.to.getPosition())) return false;
        if (checkedAfterMove(move)) return false;

        // Castling: can't castle while in check, and can't castle through an attacked square
        if (piece instanceof King && Math.abs(move.to.getPosition().x - move.from.getPosition().x) == 2) {
            if (isKingChecked(move.board, piece.color)) return false;

            int step = move.to.getPosition().x > move.from.getPosition().x ? 1 : -1;
            Point passThrough = move.from.getPosition().addX(step);
            Move throughMove = new Move(move.board, move.from, passThrough.getSquare(move.board));
            if (checkedAfterMove(throughMove)) return false;
        }

        return true;
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
        Board board = currentBoard.copy();
        board.move = new Move(board, move.from.getPosition().getSquare(board), move.to.getPosition().getSquare(board));
        board.move.move();

        return board;
    }

    private static boolean isStaleMate(List<Point> whiteLegalMoves, List<Point> blackLegalMoves) {
        return whiteLegalMoves.isEmpty() || blackLegalMoves.isEmpty();
    }


    private static List<Point> getAllRealLegalMovesOfColor(Board board, Piece.PieceColor color) {
        List<Point> moves = new ArrayList<>();

        for (Piece piece : board.getPieces().stream().filter(p -> p.color == color).toList()) {
            moves.addAll(piece.getLegalMoves());
        }

        return moves;
    }

    public static void isWinOrStaleMate(Board board) {
        List<Point> whiteLegalMoves = getAllRealLegalMovesOfColor(board, Piece.PieceColor.WHITE);
        List<Point> blackLegalMoves = getAllRealLegalMovesOfColor(board, Piece.PieceColor.BLACK);

        if (!isStaleMate(whiteLegalMoves, blackLegalMoves)) return;

        if (whiteLegalMoves.isEmpty() && isKingChecked(board, Piece.PieceColor.WHITE)) {
            System.out.println("Black wins!");
        } else if (blackLegalMoves.isEmpty() && isKingChecked(board, Piece.PieceColor.BLACK)) {
            System.out.println("White wins!");
        } else {
            System.out.println("Stalemate!");
        }

    }

}