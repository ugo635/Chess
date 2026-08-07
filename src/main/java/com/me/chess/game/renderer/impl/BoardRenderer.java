package com.me.chess.game.renderer.impl;

import com.me.chess.board.Board;
import com.me.chess.board.Square;
import com.me.chess.game.movement.Move;
import com.me.chess.game.movement.MoveChecker;
import com.me.chess.game.renderer.Renderer;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.impl.Pawn;
import com.me.chess.pieces.impl.Rook;
import com.me.chess.vectors.Point;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.List;

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

            this.board.move.from = square;

            // Return if we're checked & can't block check
            if (MoveChecker.isKingChecked(this.board, square.getPiece().color)) {
                for (Point possibleMove : square.getPiece().getLegalMoves()) {
                    boolean willBeChecked = MoveChecker.checkedAfterMove(new Move(this.board, square, possibleMove.getSquare(this.board)));
                    if (!willBeChecked) {
                        break;
                    }

                    this.board.move.from = null;
                    return;
                }
            }

            if (square.getPiece() != null) square.toggleShowingAttacks();

        } else {
            // If we clicked the on the same square a second time (to unselect)
            if (this.board.move.from == square) {
                this.board.move.from.toggleShowingAttacks();
                this.board.move.from = null;
                square.resetState();
                return;
            }

            // If we try to move to one of our pieces or an illegal move
            boolean notCapturable = !square.getPosition().isOppositeColorPieceOrEmpty(this.board, this.board.move.from.getPiece());
            boolean illegalMove = !this.board.move.from.getPiece().getLegalMoves().contains(square.getPosition());
            boolean kingChecked = MoveChecker.isKingChecked(this.board, this.board.move.from.getPiece().color);
            boolean stillCheckedAfterMove = !MoveChecker.checkedAfterMove(new Move(this.board, this.board.move.from, square));

            boolean invalidMove =
                    notCapturable ||
                            illegalMove ||
                            ((!kingChecked && !stillCheckedAfterMove) || (kingChecked && !stillCheckedAfterMove));

            if (invalidMove) return;

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
