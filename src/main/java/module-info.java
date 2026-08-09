module Chess.main {
    requires javafx.graphics;
    requires javafx.controls;
    requires org.jetbrains.annotations;

    exports com.me.chess;
    exports com.me.chess.game;
    exports com.me.chess.board;
    exports com.me.chess.vectors;
    exports com.me.chess.pieces.impl;
    exports com.me.chess.game.renderer.impl;
    exports com.me.chess.game.renderer;
    exports com.me.chess.game.movement;
    exports com.me.chess.pieces;
    exports com.me.chess.engine;
    exports com.me.chess.engine.evaluator;
}