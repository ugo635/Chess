package com.me.chess.engine;

import com.me.chess.board.Board;
import com.me.chess.board.Square;
import com.me.chess.engine.evaluator.Evaluator;
import com.me.chess.game.movement.Move;
import com.me.chess.pieces.Piece;
import com.me.chess.pieces.Piece.PieceColor;
import com.me.chess.utils.Pair;
import com.me.chess.vectors.Point;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Engine {
    private final Board board;
    private final PieceColor color;
    private static final int depth = 3;

    public Engine(Board board) {
        this.color = PieceColor.BLACK;
        this.board = board;
    }

    public Move getChosenMove() {
        Move minimax = this.minimax(this.board, depth, this.color, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY).second;
        if (minimax != null) return minimax;

        // Default if minimax doesnt find anything
        for (Piece piece : board.getPieces(color)) {
            for (Point destination : piece.getLegalMoves()) {
                return new Move(this.board, piece.getSquare(), destination.getSquare(this.board));
            }
        }

        return null;
    }

    private Pair<Float, Move> minimax(Board board, int depth, PieceColor color, float alpha, float beta) {
        if (depth == 0) return this.getDepthOne(board);

        Pair<Float, Move> value = new Pair<>(
                color.isWhite() ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY,
                new Move(null, null, null)
        );

        if (color.isWhite()) {
            outer:
            for (Piece piece : board.getPieces(color)) {
                for (Point destination : piece.getLegalMoves()) {
                    Simulator sim = new Simulator(board, piece.getSquare(), destination.getSquare(board), null);
                    Board b = sim.simulate();
                    Pair<Float, Move> minimaxRes = this.minimax(b, depth - 1, color.getOpposite(), alpha, beta);

                    if (value.first < minimaxRes.first) {
                        value = new Pair<>(minimaxRes.first, sim.getMove());
                    }

                    if (value.first >= beta) break outer;
                    alpha = Math.max(alpha, value.first);
                }
            }
        } else {
            outer:
            for (Piece piece : board.getPieces(color)) {
                for (Point destination : piece.getLegalMoves()) {
                    Simulator sim = new Simulator(board, piece.getSquare(), destination.getSquare(board), null);
                    Board b = sim.simulate();
                    Pair<Float, Move> minimaxRes = this.minimax(b, depth - 1, color.getOpposite(), alpha, beta);

                    if (value.first > minimaxRes.first) {
                        value = new Pair<>(minimaxRes.first, sim.getMove());
                    }

                    if (value.first <= alpha) break outer;
                    beta = Math.min(beta, value.first);
                }
            }
        }

        return value;
    }

    private Pair<Float, Move> getDepthOne(Board board) {
        return new Pair<>(
                Evaluator.evaluate(board),
                new Move(null, null, null)
        );
    }

    private record Simulator(Board board, Square origin, Square destination, @Nullable Simulator source) implements Comparable<Simulator> {
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
            Board simulatedBoard = infos.first;
            Move simulatedMove = infos.second;

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
