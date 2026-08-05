package com.me.chess.pieces;

import com.me.chess.board.Board;
import com.me.chess.game.Move;
import com.me.chess.vectors.Direction;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.impl.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.Objects;

import static com.me.chess.game.ChessGame.SQUARE_SIZE;

public abstract class Piece {
    public final ImageView render;
    public final PieceType piece;
    public final PieceColor color;
    protected final Board board;
    protected Square square;
    public int move;

    public Piece(Square square, Color color) {
        this.piece = PieceType.valueOf(getClass().getSimpleName().toUpperCase());
        this.color = color == Color.BLACK ? PieceColor.BLACK : PieceColor.WHITE;
        this.move = 0;
        this.square = square;
        this.board = square.board;
        this.render = getRender();
    }

    protected ImageView getRender() {
        ImageView render = new ImageView(
                new Image(
                        Objects.requireNonNull(getClass().getResource(
                                String.format("/pieces/%s_%s.png", this.color, this.piece) // Ex: WHITE_KING
                        )).toString()
                )
        );

        render.setFitHeight(SQUARE_SIZE);
        render.setFitWidth(SQUARE_SIZE);

        return render;
    }

    public void render(StackPane content) {
        content.getChildren().add(this.render);
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public boolean hasntMoved() {
        return this.move == 0;
    }

    public void empty() {
        this.square.empty();
        this.square.content.getChildren().remove(this.render);
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

    public abstract List<Point> getLegalMoves();

    public enum PieceType {
        KING, QUEEN, ROOK, KNIGHT, BISHOP, PAWN;

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
        WHITE, BLACK
    }
}
