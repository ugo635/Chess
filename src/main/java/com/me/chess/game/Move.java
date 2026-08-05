package com.me.chess.game;

import com.me.chess.board.Board;
import com.me.chess.pieces.impl.*;
import com.me.chess.vectors.Point;
import com.me.chess.board.Square;
import com.me.chess.pieces.Piece;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;

import static com.me.chess.game.ChessGame.SQUARE_SIZE;

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

        return legalMoves.contains(to.getPosition());
    }

    @SuppressWarnings("DataFlowIssue")
    public void move() throws IllegalMoveException {
        if (!this.isLegal() || this.isKingChecked(this.from.getPiece().color) && !(this.from.getPiece() instanceof King)) throw new IllegalMoveException("Move isn't legal");
        Piece fromPiece = this.from.getPiece();

        // Update the moves counter
        this.board.totalMoves++;
        fromPiece.move++;

        // Remove the piece from the destination square if exists
        if (!this.to.isEmpty()) {
            this.to.getPiece().delete();
        }

        // Promotion
        if (fromPiece instanceof Pawn pawn && this.to.getPosition().y == pawn.EIGHT_RANK) {
            this.createPopupForPromotion(pawn);
        } else {
            this.movesThePiece(fromPiece);
        }

    }

    private void movesThePiece(Piece fromPiece) {
        // Handles special moves
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

    private void createPopupForPromotion(Pawn pawn) {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(SQUARE_SIZE * 5);
        rectangle.setHeight(SQUARE_SIZE * 1.25);
        rectangle.setFill(Color.WHITE);

        ImageView queen = Queen.getRender(Piece.PieceColor.WHITE, "QUEEN");
        ImageView knight = Knight.getRender(Piece.PieceColor.WHITE, "KNIGHT");
        ImageView rook = Rook.getRender(Piece.PieceColor.WHITE, "ROOK");
        ImageView bishop = Bishop.getRender(Piece.PieceColor.WHITE, "BISHOP");

        HBox pieces = new HBox(10, queen, knight, rook, bishop);
        pieces.setAlignment(Pos.CENTER);

        final Piece.PieceType[] pieceTypes = new Piece.PieceType[] { Piece.PieceType.QUEEN, Piece.PieceType.KNIGHT, Piece.PieceType.ROOK, Piece.PieceType.BISHOP };

        // Handles clicks & style
        for (int i = 0; i < pieces.getChildren().size(); i++) {
            Node child = pieces.getChildren().get(i);
            child.setStyle("-fx-effect: dropshadow(gaussian, rgb(0,0,0), 15, 0.3, 0, 6); -fx-cursor: hand");

            final int index = i;
            child.setOnMouseClicked(event -> {
                // Changes the piece
                Piece promotedPiece = pieceTypes[index].getInstance(this.from, pawn.color.getColor());
                pawn.delete();

                this.board.container.getChildren().removeAll(rectangle, pieces);
                this.movesThePiece(promotedPiece);
            });
        }

        this.board.container.getChildren().addAll(rectangle, pieces);

    }

    public int getTotalMove() {
        return this.board.totalMoves;
    }

    public boolean isKingChecked(Piece.PieceColor color) {
        return this.board.getPiecesofType(King.class)
                .stream()
                .filter(k -> k.color == color)
                .toList()
                .getFirst()
                .isChecked(this.board.getAllLegalMovesOfColor(color.getOpposite()));
    }

    @Override
    public String toString() {
        //return String.format("Move(%s -> %s)", this.from, this.to);
        return String.format("Move(%s -> %s)", this.from.getPosition(), this.to.getPosition());
    }
}
