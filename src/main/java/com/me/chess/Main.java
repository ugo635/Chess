package com.me.chess;

import com.me.chess.game.ChessGame;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        ChessGame game = new ChessGame();

        stage.setScene(game.scene);
        stage.setTitle("Chess");
        stage.show();
    }

}
