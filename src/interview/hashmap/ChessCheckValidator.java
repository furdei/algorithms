package interview.hashmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Check if white king is checked
 *
 * In this board, whites are (C)apitals and blacks are (l)owercased
 *
 *   0 1 2 3 4 5 6 7
 * 0 r     K   R r
 * 1
 * 2   n
 * 3 q
 * 4
 * 5
 * 6
 * 7       k
 */
public class ChessCheckValidator {

    public enum PieceType {
        PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING;
    }

    public enum Color {
        BLACK, WHITE
    }

    public static class Piece {
        PieceType pieceType;
        Color color;
        int h;
        int v;

        public Piece(PieceType pieceType, Color color, int h, int v) {
            this.pieceType = pieceType;
            this.color = color;
            this.h = h;
            this.v = v;
        }
    }

    private static class Position {
        int h;
        int v;

        public Position(Piece piece) {
            this.h = piece.h;
            this.v = piece.v;
        }

        public Position(int h, int v) {
            this.h = h;
            this.v = v;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Position position = (Position) o;

            if (h != position.h) return false;
            if (v != position.v) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = h;
            result = 31 * result + v;
            return result;
        }

        boolean isValid() {
            return h >= 0 && h <= 7 && v >= 0 && v <= 7;
        }
    }

    public boolean isWhiteKingChecked(Piece[] pieces) {
        if (pieces == null || pieces.length == 0) {
            return false;
        }

        Piece whiteKing = null;
        Piece blackKing = null;

        // build an index of pieces by their positions
        Map<Position, Piece> board = new HashMap<Position, Piece>(pieces.length);
        for (Piece piece : pieces) {
            board.put(new Position(piece), piece);

            if (piece.pieceType == PieceType.KING && piece.color == Color.WHITE) {
                if (whiteKing != null) {
                    throw new IllegalStateException("More than 1 white king");
                }

                whiteKing = piece;
            }

            if (piece.pieceType == PieceType.KING && piece.color == Color.BLACK) {
                if (blackKing != null) {
                    throw new IllegalStateException("More than 1 black king");
                }

                blackKing = piece;
            }
        }

        if (whiteKing == null) {
            throw new IllegalStateException("No white king found");
        }

        if (blackKing == null) {
            throw new IllegalStateException("No black king found");
        }

        // constant-time checks here

        // check if black king hits the white king
        if (Math.abs(blackKing.h - whiteKing.h) <= 1
                && Math.abs(blackKing.v - whiteKing.v) <= 1) {
            return true;
        }

        // check if white king is checked by pawns
        if (whiteKing.v < 7) {
            if (whiteKing.h > 0) {
                Piece piece = board.get(new Position(whiteKing.h - 1, whiteKing.v + 1));
                if (piece != null && piece.color != whiteKing.color && piece.pieceType == PieceType.PAWN) {
                    return true;
                }
            }

            if (whiteKing.h < 7) {
                Piece piece = board.get(new Position(whiteKing.h + 1, whiteKing.v + 1));
                if (piece != null && piece.color != whiteKing.color && piece.pieceType == PieceType.PAWN) {
                    return true;
                }
            }
        }

        // check if white king is checked by knights
        int i = -2;
        while (i <= 2) {
            int j = i == -2 || i == 2 ? 1 : 2;

            for (int k = -1; k <= 1; k += 2) {
                int hPos = whiteKing.h + j * k;
                int vPos = whiteKing.v + i;

                if (hPos >= 0 && hPos <= 7 && vPos >= 0 && vPos <= 7) {
                    if (blackKnightInPos(board, hPos, vPos)) {
                        return true;
                    }
                }
            }

            i++;
            if (i == 0) {
                i++;
            }
        }

        // linear-time checks here
        // starting from white king, find the closest piece in every direction
        // and check if it is black and if it can hit the king

        CheckValidator horizontalCheckValidator = new CheckValidator() {
            @Override
            public boolean isCheck(Piece source) {
                return source.pieceType == PieceType.ROOK || source.pieceType == PieceType.QUEEN;
            }
        };

        // horizontally in both directions from king
        if (linearFindCheck(board, whiteKing, new PositionShifter() {
            @Override
            public Position shiftPosition(Position start, int shift) {
                return new Position(start.h + shift, start.v);
            }
        }, horizontalCheckValidator)) {
            return true;
        }

        // vertically in both directions from king
        if (linearFindCheck(board, whiteKing, new PositionShifter() {
            @Override
            public Position shiftPosition(Position start, int shift) {
                return new Position(start.h, start.v + shift);
            }
        }, horizontalCheckValidator)) {
            return true;
        }

        CheckValidator diagonalCheckValidator = new CheckValidator() {
            @Override
            public boolean isCheck(Piece source) {
                return source.pieceType == PieceType.BISHOP || source.pieceType == PieceType.QUEEN;
            }
        };

        // left diagonal in both directions from king
        if (linearFindCheck(board, whiteKing, new PositionShifter() {
            @Override
            public Position shiftPosition(Position start, int shift) {
                return new Position(start.h + shift, start.v + shift);
            }
        }, diagonalCheckValidator)) {
            return true;
        }

        // right diagonal in both directions from king
        if (linearFindCheck(board, whiteKing, new PositionShifter() {
            @Override
            public Position shiftPosition(Position start, int shift) {
                return new Position(start.h + shift, start.v - shift);
            }
        }, diagonalCheckValidator)) {
            return true;
        }

        return false;
    }

    private interface PositionShifter {
        Position shiftPosition(Position start, int shift);
    }

    private interface CheckValidator {
        boolean isCheck(Piece source);
    }

    /**
     * try to find the check of the target piece moving in both directions from it
     */
    private boolean linearFindCheck(Map<Position, Piece> board, Piece target,
                                    PositionShifter positionShifter, CheckValidator checkValidator) {
        Position targetPos = new Position(target);

        for (int k = -1; k <= 1; k += 2) {
            boolean isObstacleFound = false;
            int i = k;
            while (!isObstacleFound) {
                Position position = positionShifter.shiftPosition(targetPos, i);

                if (position.isValid()) {
                    Piece piece = board.get(position);

                    if (piece != null) {
                        if (piece.color != target.color) {
                            if (checkValidator.isCheck(piece)) {
                                return true;
                            }
                        }

                        isObstacleFound = true;
                    }
                } else {
                    isObstacleFound = true;
                }

                i += k;
            }
        }

        return false;
    }

    private boolean blackKnightInPos(Map<Position, Piece> board, int h, int v) {
        Piece piece = board.get(new Position(h, v));
        return piece != null && piece.color == Color.BLACK && piece.pieceType == PieceType.KNIGHT;
    }

    public static void main(String[] args) {
        Piece[] pieces = new Piece[] {
                new Piece(PieceType.ROOK, Color.BLACK, 0, 0),
                new Piece(PieceType.KING, Color.WHITE, 3, 0),
                new Piece(PieceType.ROOK, Color.WHITE, 5, 0),
                new Piece(PieceType.ROOK, Color.BLACK, 6, 0),
                new Piece(PieceType.KNIGHT, Color.BLACK, 1, 2),
                new Piece(PieceType.QUEEN, Color.BLACK, 0, 3),
                new Piece(PieceType.KING, Color.BLACK, 3, 7)
        };
        ChessCheckValidator chess = new ChessCheckValidator();
        System.out.println("Chess.main " + chess.isWhiteKingChecked(pieces));
    }

}
