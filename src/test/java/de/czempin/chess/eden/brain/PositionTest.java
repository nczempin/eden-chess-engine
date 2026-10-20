package de.czempin.chess.eden.brain;

import static org.junit.Assert.*;

import de.czempin.chess.eden.Move;
import org.junit.Test;

public class PositionTest {
    @Test
    public void parsesBlackLongCastlingFlag() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w q - 0 1";
        Position p = new Position();
        p.setFENPosition(fen);
        assertTrue(p.getCastleLongBlack());
        assertFalse(p.getCastleShortBlack());
    }

    @Test
    public void parsesPromotionMoveAndUpdatesBoardCleanly() throws Exception {
        String fen = "8/P7/8/8/8/8/7k/K7 w - - 0 1";
        Position p = new Position();
        p.setFENPosition(fen);
        Move m = Move.create(p, "a7a8q");
        assertEquals(5, m.promotedTo);
        assertEquals("a7a8q", m.getText());
        p.makeMove("a7a8q");
        assertEquals(5, p.board[Position.decodeSquare("a8")]);
    }

    @Test
    public void handlesThreefoldRepetitionInGameHistoryWithoutThrowing() throws Exception {
        EdenBrain.withinAlphabeta = false;
        EdenBrain.threeDrawsTable.clear();
        Position p = new Position();
        p.setToStartPosition();
        String[] moves = {
            "g1f3", "g8f6", "f3g1", "f6g8",
            "g1f3", "g8f6", "f3g1", "f6g8",
            "g1f3", "g8f6", "f3g1", "f6g8"
        };
        for (int i = 0; i < moves.length; i++) {
            p.makeMove(moves[i]);
        }
        assertTrue(p.onMove().equals(Position.WHITE));
    }
}
