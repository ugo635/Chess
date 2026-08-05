package com.me.chess.board;

import com.me.chess.pieces.impl.King;
import com.me.chess.pieces.impl.Pawn;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.impl.Rook;
import com.me.chess.vectors.Point;
import com.me.chess.game.Move;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.me.chess.pieces.Piece.PieceType.*;

public class Board {
    public List<Square> squares;
    public StackPane container;
    public GridPane grid;
    public Move move;
    public int totalMoves;

    public Board(StackPane container, GridPane grid) {
        this.grid = grid;
        this.container = container;
        this.squares = new ArrayList<>();
        this.move = new Move(this, null, null);
        this.totalMoves = 0;

        Color light = new Color(0.941f, 0.851f, 0.710f, 1.0f);
        Color dark = new Color(0.706f, 0.533f, 0.392f, 1.0f);

        for (int y = 1; y < 9; y++) {
            for (int x = 1; x < 9; x++) {
                Color color = (x + y) % 2 == 0 ? dark : light;
                Square square = new Square(this, color, x, y);
                //square.addElement(new Label(String.format("(%s, %s) %d", x, y, squares.size())));

                this.squares.add(square);
                this.grid.add(square.container, x, Math.abs(y - 8));

                square.onClick(() -> squareClick(square));
            }
        }

        this.addPieces();
    }

    @SuppressWarnings("DataFlowIssue")
    private void squareClick(Square square) {
        long startMs = System.currentTimeMillis();

        if (this.move.from == null) { // If we select a piece
            if (square.isEmpty()) return;
            square.toggleSelect();

            if (this.isKingChecked(square.getPiece().color) && !(square.getPiece() instanceof King)) return; // Return if we're checked
            if (square.getPiece() != null) square.toggleShowingAttacks();

            this.move.from = square;
        } else {
            if (this.move.from == square) { // If we clicked the on the same square a second time (to unselect)
                this.move.from.toggleShowingAttacks();
                this.move.from = null;
                square.resetState();
                return;
            }

            if (!square.getPosition().isOppositeColorPieceOrEmpty(this, this.move.from.getPiece()) ||
                    !this.move.from.getPiece().getLegalMoves().contains(square.getPosition()) ||
                    this.isKingChecked(this.move.from.getPiece().color) && !(this.move.from.getPiece() instanceof King)) return; // If we try to move to one of our pieces or to a non-legal

            // If we move
            this.move.from.toggleShowingAttacks();
            this.move.to = square;

            this.move.move();
        }

        System.out.println(System.currentTimeMillis() - startMs + "ms");
    }

    public boolean isKingChecked(Piece.PieceColor color) {
        return new Move(this, null, null).isKingChecked(color);
    }

    private void addPieces() {
        // Add the pawns
        for (int i = 8; i < 56; i++) {
            if (i == 16) {
                i = 47;
                continue;
            }

            Square square = this.getSquareAt(i);
            square.setPiece(new Pawn(square, i < 16 ? Color.WHITE : Color.BLACK));
        }

        // Add the pieces
        List<Piece.PieceType> pieceOrder = List.of(ROOK, BISHOP, KNIGHT, QUEEN, KING, KNIGHT, BISHOP, ROOK);
        for (int i = 0; i < 64; i++) {
            if (i == 8) {
                i = 55;
                continue;
            }

            Square square = this.getSquareAt(i);
            square.setPiece(pieceOrder.get(i % 8).getInstance(square, i < 8 ? Color.WHITE : Color.BLACK));
        }

        for (int i = 0; i < 4; i++) {
            List<Rook> rooks = this.getPiecesofType(Rook.class);
            rooks.get(i).isRightRook = i % 2 == 1;
        }
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
