package com.me.chess.pieces;

import com.me.chess.board.Board;
import com.me.chess.game.movement.Move;
import com.me.chess.game.movement.MoveChecker;
import com.me.chess.game.renderer.Renderable;
import com.me.chess.game.renderer.impl.PieceRenderer;
import com.me.chess.vectors.Direction;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.impl.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Piece implements Renderable {
    public final PieceType pieceType;
    public PieceRenderer renderer;
    public final PieceColor color;
    public final Board board;
    protected Square square;
    public int move;
    public float value;

    public Piece(Square square, Color color) {
        this.pieceType = PieceType.valueOf(getClass().getSimpleName().toUpperCase());
        this.color = PieceColor.from(color);
        this.move = 0;
        this.square = square;
        this.board = square.board;
    }

    @Override
    public void render() {
        if (this.renderer == null) this.renderer = new PieceRenderer(this);
        this.renderer.render();
    }

    public void update() {
        if (this.renderer == null) {
            this.renderer = new PieceRenderer(this);
            this.renderer.render();
        }

    }

    public abstract float getValue();

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        if (this.renderer != null && this.square != null) this.renderer.update(this.square, square);
        this.square = square;
    }

    public boolean hasntMoved() {
        return this.move == 0;
    }

    public void delete() {
        this.square.empty();
        if (this.renderer != null) this.square.renderer.clearElements();
        this.square = null;
    }

    public Point getPosition() {
        return this.square.getPosition();
    }

    protected boolean isEmptyInRange(Direction direction, int range) {
        Point diff = direction.getDiff();

        // If there's one square that isn't empty it will return false
        for (int i = 1; i < 9 && i <= range; i++) {
            Point newPoint = this.getPosition().add(diff.times(i));
            if (newPoint.x < 1 || newPoint.x > 8) break;
            if (newPoint.y < 1 || newPoint.y > 8) break;

            if (!newPoint.isAnEmptySquare(this.board)) return false;
        }

        return true;
    }

    protected int isEmptyInRange(Direction direction) {
        for (int i = 1; i < 9; i++) {
            Point diff = direction.getDiff();
            Point newPoint = this.getPosition().add(diff.times(i));
            if (newPoint.x < 1 || newPoint.x > 9) return i - 1;
            if (newPoint.y < 1 || newPoint.y > 9) return i - 1;

            if (newPoint.isOppositeColorPiece(this.board, this)) return i;
            if (!newPoint.isAnEmptySquare(this.board)) return i - 1;
        }

        return -1;
    }

    public boolean isOppositeColor(Piece otherPiece) {
        return this.color != otherPiece.color;
    }

    public void onMove(Move move) {}

    public abstract List<Point> getDefaultLegalMoves();

    public final List<Point> getLegalMoves() {
        return this.getDefaultLegalMoves()
                .stream()
                .filter(point -> !MoveChecker.checkedAfterMove(new Move(this.board, this.getSquare(), point.getSquare(this.board))))
                .toList();
    }

    @Override
    public String toString() {
        return String.format("Piece(Type(%s), Color(%s), %s)", this.pieceType, this.color, this.square);
    }

    public @Nullable Piece renderlessCopy(Square square) {
        Piece piece = this.pieceType.getInstance(square, this.color.getPaintColor());
        piece.move = this.move;

        return piece;
    }

    public char getChar() {
        return this.pieceType == PieceType.KNIGHT ? 'N' : this.pieceType.toString().charAt(0);
    }

    public enum PieceType {
        KING,
        QUEEN,
        ROOK,
        KNIGHT,
        BISHOP,
        PAWN;

        public Piece getInstance(Square square, Color color) {
            return switch (this) {
                case KING -> new King(square, color);
                case QUEEN -> new Queen(square, color);
                case ROOK -> new Rook(square, color);
                case KNIGHT -> new Knight(square, color);
                case BISHOP -> new Bishop(square, color);
                case PAWN -> new Pawn(square, color);
            };
        }
    }

    public enum PieceColor {
        WHITE, BLACK;

        public PieceColor getOpposite() {
            return this == WHITE ? BLACK : WHITE;
        }

        public static PieceColor from(Color color) {
            return color == Color.BLACK ? BLACK : WHITE;
        }

        public boolean isWhite() {
            return this == WHITE;
        }

        public Color getPaintColor() {
            return this == WHITE ? Color.WHITE : Color.BLACK;
        }
    }
}