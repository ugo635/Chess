package com.me.chess.engine;

import com.me.chess.board.Board;
import com.me.chess.board.Square;
import com.me.chess.engine.evaluator.Evaluator;
import com.me.chess.game.movement.Move;
import com.me.chess.pieces.Piece;
import com.me.chess.utils.Pair;
import com.me.chess.vectors.Point;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Engine {
    private final Board board;
    private final Piece.PieceColor color;

    public Engine(Board board) {
        this.board = board;
        this.color = Piece.PieceColor.BLACK;
    }

    public Move getChosenMove() {
        List<Simulator> simulators = new ArrayList<>();

        for (Piece piece : this.board.getPieces(this.color)) {
            for (Point destination : piece.getLegalMoves()) {
                simulators.add(new Simulator(this.board, piece.getSquare(), destination.getSquare(this.board)));
            }
        }

        simulators.sort(Simulator::compareTo);
        Simulator bestSimulator = simulators.getFirst();

        return bestSimulator.getMove();
    }

    private record Simulator(Board board, Square origin, Square destination) implements Comparable<Simulator> {
        public float getValueOfMove() {
            return Evaluator.evaluate(this.simulate());
        }

        public Move getMove() {
            return new Move(this.board, this.origin, this.destination);
        }

        private Pair<Board, Move> getInfos() {
            Board boardCopy = this.board.copy();
            Move move = new Move(boardCopy, boardCopy.getSquareAt(origin.getPosition()), boardCopy.getSquareAt(destination.getPosition()));

            return new Pair<>(
                    boardCopy,
                    move
            );
        }

        private Board simulate() {
            Pair<Board, Move> infos = this.getInfos();
            Board simulatedBoard = infos.first();
            Move simulatedMove = infos.second();

            simulatedBoard.move = simulatedMove;
            simulatedBoard.move.move();

            return simulatedBoard;
        }

        @Override
        public int compareTo(@NotNull Engine.Simulator other) {
            return Float.compare(this.getValueOfMove(), other.getValueOfMove());
        }
    }
}
