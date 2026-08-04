package com.me.chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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
