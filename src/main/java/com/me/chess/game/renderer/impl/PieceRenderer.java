package com.me.chess.game.renderer.impl;

import com.me.chess.board.Square;
import com.me.chess.game.renderer.Renderer;
import com.me.chess.pieces.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

import static com.me.chess.game.ChessGame.SQUARE_SIZE;

public class PieceRenderer implements Renderer {
    public Piece piece;
    public ImageView render;

    public PieceRenderer(Piece piece) {
        this.piece = piece;
        this.render = this.getRender();
    }

    @Override
    public void render() {
        this.piece.getSquare().renderer.addElement(this.render);
    }

    public void update(Square oldSquare, Square newSquare) {
        oldSquare.renderer.clearElements();
        newSquare.renderer.addElement(this.render);
    }

    private ImageView getRender() {
        ImageView render = new ImageView(
                new Image(
                        Objects.requireNonNull(getClass().getResource(
                                String.format("/pieces/%s_%s.png", this.piece.color, this.piece.pieceType) // Ex: WHITE_KING
                        )).toString()
                )
        );

        render.setFitHeight(SQUARE_SIZE);
        render.setFitWidth(SQUARE_SIZE);

        return render;
    }

    public static ImageView getRender(Piece.PieceColor color, String pieceName) {
        ImageView render = new ImageView(
                new Image(
                        Objects.requireNonNull(Piece.class.getResource(
                                String.format("/pieces/%s_%s.png", color, pieceName) // Ex: WHITE_KING
                        )).toString()
                )
        );

        render.setFitHeight(SQUARE_SIZE);
        render.setFitWidth(SQUARE_SIZE);

        return render;
    }
}
