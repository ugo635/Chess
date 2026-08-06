package com.me.chess.board;

import com.me.chess.game.ChessGame;
import com.me.chess.game.renderer.Renderable;
import com.me.chess.game.renderer.impl.BoardRenderer;
import com.me.chess.pieces.impl.King;
import com.me.chess.pieces.impl.Pawn;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.impl.Rook;
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

    public Board(ChessGame game) {
        this.game = game;
        this.squares = new ArrayList<>();
        this.move = new Move(this, null, null);
        this.totalMoves = 0;

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

    public boolean isKingChecked(Piece.PieceColor color) {
        return new Move(this, null, null).isKingChecked(color);
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

        List<Piece> pieces = this.getPieces()
                .stream()
                .filter(piece -> piece.color == color && !(piece instanceof King))
                .toList();

        for (Piece piece : pieces) {
            if (!(piece instanceof King)) allPiecesLegalMoves.addAll(piece.getLegalMoves());
        }

        return allPiecesLegalMoves;
    }
}
