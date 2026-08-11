package com.me.chess;

import com.me.chess.game.ChessGame;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ChessGame game = new ChessGame();

        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/icon.png")).toString()));
        stage.setScene(game.scene);
        stage.setTitle("Chess");
        stage.show();
    }

}
