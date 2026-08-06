package com.me.chess.game.renderer.impl;

import com.me.chess.board.Board;
import com.me.chess.board.Square;
import com.me.chess.game.renderer.Renderer;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.impl.King;
import com.me.chess.pieces.impl.Pawn;
import com.me.chess.pieces.impl.Rook;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

import static com.me.chess.pieces.Piece.PieceType.*;
import static com.me.chess.pieces.Piece.PieceType.BISHOP;
import static com.me.chess.pieces.Piece.PieceType.KING;
import static com.me.chess.pieces.Piece.PieceType.KNIGHT;
import static com.me.chess.pieces.Piece.PieceType.QUEEN;
import static com.me.chess.pieces.Piece.PieceType.ROOK;

public class BoardRenderer implements Renderer {
    public Board board;
    public StackPane container;
    public GridPane grid;

    public BoardRenderer(Board board, StackPane container, GridPane grid) {
        this.board = board;
        this.container = container;
        this.grid = grid;
    }

    @Override
    public void render() {
        // Renders the squares
        this.board.squares.forEach(sq -> {
            sq.render();
            this.grid.add(sq.renderer.container, sq.x, Math.abs(sq.y - 8));
            sq.renderer.onClick(() -> this.squareClick(sq));
            this.addPieces();
        });
    }

    @SuppressWarnings("DataFlowIssue")
    private void squareClick(Square square) {
        long startMs = System.currentTimeMillis();

        if (this.board.move.from == null) { // If we select a piece
            if (square.isEmpty()) return;
            square.toggleSelect();

            if (this.board.isKingChecked(square.getPiece().color) && !(square.getPiece() instanceof King)) return; // Return if we're checked
            if (square.getPiece() != null) square.toggleShowingAttacks();

            this.board.move.from = square;
        } else {
            if (this.board.move.from == square) { // If we clicked the on the same square a second time (to unselect)
                this.board.move.from.toggleShowingAttacks();
                this.board.move.from = null;
                square.resetState();
                return;
            }

            if (!square.getPosition().isOppositeColorPieceOrEmpty(this.board, this.board.move.from.getPiece()) ||
                    !this.board.move.from.getPiece().getLegalMoves().contains(square.getPosition()) ||
                    this.board.isKingChecked(this.board.move.from.getPiece().color) && !(this.board.move.from.getPiece() instanceof King)) return; // If we try to move to one of our pieces or to a non-legal

            // If we move
            this.board.move.from.toggleShowingAttacks();
            this.board.move.to = square;

            this.board.move.move();
        }

        System.out.println(System.currentTimeMillis() - startMs + "ms");
    }

    private void addPieces() {
        // Add the pawns
        for (int i = 8; i < 56; i++) {
            if (i == 16) {
                i = 47;
                continue;
            }

            Square square = this.board.getSquareAt(i);
            square.setPiece(new Pawn(square, i < 16 ? Color.WHITE : Color.BLACK));
        }

        // Add the pieces
        List<Piece.PieceType> pieceOrder = List.of(ROOK, BISHOP, KNIGHT, QUEEN, KING, KNIGHT, BISHOP, ROOK);
        for (int i = 0; i < 64; i++) {
            if (i == 8) {
                i = 55;
                continue;
            }

            Square square = this.board.getSquareAt(i);
            square.setPiece(pieceOrder.get(i % 8).getInstance(square, i < 8 ? Color.WHITE : Color.BLACK));
        }

        for (int i = 0; i < 4; i++) {
            List<Rook> rooks = this.board.getPiecesofType(Rook.class);
            rooks.get(i).isRightRook = i % 2 == 1;
        }
    }
}
