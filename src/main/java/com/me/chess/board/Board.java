package com.me.chess.board;

import com.me.chess.game.ChessGame;
import com.me.chess.game.renderer.Renderable;
import com.me.chess.game.renderer.impl.BoardRenderer;
import com.me.chess.pieces.impl.King;
import com.me.chess.pieces.impl.Pawn;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.impl.Rook;
import com.me.chess.vectors.Direction;
import com.me.chess.vectors.Point;
import com.me.chess.game.movement.Move;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.me.chess.pieces.Piece.PieceType.*;

public class Board {
    public List<Square> squares;
    public Move move;
    public int totalMoves;
    public ChessGame game;
    public Piece.PieceColor currentTurn;

    public Board(ChessGame game) {
        this.game = game;
        this.squares = new ArrayList<>();
        this.move = new Move(this, null, null);
        this.totalMoves = 0;
        this.currentTurn = Piece.PieceColor.WHITE;

        Color light = new Color(0.941f, 0.851f, 0.710f, 1.0f);
        Color dark = new Color(0.706f, 0.533f, 0.392f, 1.0f);

        for (int y = 1; y < 9; y++) {
            for (int x = 1; x < 9; x++) {
                Color color = (x + y) % 2 == 0 ? dark : light;
                Square square = new Square(this, color, x, y);

                this.squares.add(square);
            }
        }
    }

    /**
     * Switches whose turn it is.
     * Only real, on-screen moves should call this (not simulated/renderless moves
     * used for check-detection).
     */
    public void switchTurn() {
        this.currentTurn = this.currentTurn.getOpposite();
    }

    public Square getSquareAt(int x, int y) {
        return this.squares
                .stream()
                .filter(s -> s.x == x && s.y == y)
                .toList()
                .getFirst();
    }

    public Square getSquareAt(Point p) {
        return this.getSquareAt(p.x, p.y);
    }

    public Square getSquareAt(int index) {
        return this.squares.get(index);
    }

    public List<Piece> getPieces() {
        return this.squares
                .stream()
                .map(Square::getPiece)
                .filter(Objects::nonNull)
                .toList();
    }

    public <T extends Piece> List<T> getPiecesofType(Class<T> clazz) {
        return this.squares
                .stream()
                .map(Square::getPiece)
                .filter(Objects::nonNull)
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .toList();
    }

    /**
     * DOES NOT INCLUDE THE KING !!
     */
    public List<Point> getAllLegalMovesOfColor(Piece.PieceColor color) {
        List<Point> allPiecesLegalMoves = new ArrayList<>();

        for (Piece piece : this.getPieces()) {
            if (piece.color != color) continue;

            if (piece instanceof King king) {
                for (Direction dir : Direction.values()) {
                    Point p = king.getPosition().add(dir.getDiff());
                    if (p.isOppositeColorPieceOrEmpty(this, king)) allPiecesLegalMoves.add(p);
                }
            } else {
                allPiecesLegalMoves.addAll(piece.getAttackedSquares());
            }
        }

        return allPiecesLegalMoves;
    }

    public Board copy() {
        Board board = new Board(this.game);
        board.totalMoves = this.totalMoves;
        board.currentTurn = this.currentTurn;

        board.squares = this.squares
                .stream()
                .map(sq -> sq.renderlessCopy(board))
                .toList();

        return board;
    }

    public List<Piece> getPieces(Piece.PieceColor pieceColor) {
        return this.getPieces()
                .stream()
                .filter(piece -> piece.color == pieceColor)
                .toList();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 8; i > 0; i--) {
            for (int j = 1; j < 9; j++) {
                Square sq = this.getSquareAt(j, i);
                sb
                        .append((sq.getPiece() == null ? "0" : sq.getPiece().getChar()))
                        .append(" | ");
            }

            sb.append("\n");

        }

        return sb.toString();
    }
}