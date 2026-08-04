package com.me.chess.board;

import com.me.chess.board.pieces.Pawn;
import com.me.chess.board.pieces.Piece;
import com.me.chess.game.Move;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

import static com.me.chess.board.pieces.Piece.PieceType.*;

public class Board {
    public List<Square> squares;
    public GridPane grid;
    public Move move;

    public Board(GridPane grid) {
        this.grid = grid;
        this.squares = new ArrayList<>();
        this.move = new Move(this, null, null);

        Color light = new Color(0.941f, 0.851f, 0.710f, 1.0f);
        Color dark = new Color(0.706f, 0.533f, 0.392f, 1.0f);

        for (int y = 1; y < 9; y++) {
            for (int x = 1; x < 9; x++) {
                Color color = (x + y) % 2 == 0 ? dark : light;
                Square square = new Square(this, color, x, y);
                //square.addElement(new Label(String.format("(%s, %s) %d", x, y, squares.size())));

                this.squares.add(square);
                this.grid.add(square.content, x, Math.abs(y - 8));

                square.onClick(() -> {
                    if (this.move.from == null) {
                        if (square.isEmpty()) return;
                        square.setHighlighted(!square.isHighlighted);
                        this.move.from = square;
                    } else {
                        if (this.move.from == square) {
                            this.move.from = null;
                            square.setHighlighted(false);
                            return;
                        }

                        this.move.to = square;
                        this.move.move();
                    }
                });
            }
        }

        this.addPieces();
    }

    private void addPieces() {
        // Add the paws
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
}
