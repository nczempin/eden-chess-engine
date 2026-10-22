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

    @Test
    public void detectsInTreeThreefoldRepetitionInAlphabeta() throws Exception {
        EdenBrain.withinAlphabeta = true;
        EdenBrain.threeDrawsTable.clear();
        EdenBrain.alphaBetaDraws.clear();
        Position p = new Position();
        p.setToStartPosition();
        String[] moves = {
            "g1f3", "g8f6", "f3g1", "f6g8",
            "g1f3", "g8f6", "f3g1", "f6g8"
        };
        for (int i = 0; i < moves.length; i++) {
            Move m = Move.create(p, moves[i]);
            p.makeMove(m);
        }
        boolean threw = false;
        try {
            Move m1 = Move.create(p, "g1f3");
            p.makeMove(m1);
            Move m2 = Move.create(p, "g8f6");
            p.makeMove(m2);
            Move m3 = Move.create(p, "f3g1");
            p.makeMove(m3);
            Move m4 = Move.create(p, "f6g8");
            p.makeMove(m4);
        } catch (ThreeRepetitionsAB e) {
            threw = true;
        } finally {
            EdenBrain.withinAlphabeta = false;
            EdenBrain.alphaBetaDraws.clear();
        }
        assertTrue(threw);
    }

    @Test
    public void zobristIncludesCastlingAndEnPassantConsistently() throws Exception {
        Position p1 = new Position();
        p1.setFENPosition("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        Position p2 = new Position();
        p2.setFENPosition("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w - - 0 1");
        assertNotEquals(p1.getZobrist(), p2.getZobrist());

        Position ep = new Position();
        ep.setFENPosition("rnbqkbnr/ppp1pppp/8/3pP3/8/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 3");
        Move epMove = Move.create(ep, "e5d6");
        ep.makeMove(epMove);
        long incZobrist = ep.getZobrist();
        ep.zobrist = 0L;
        long recomputedZobrist = ep.getZobrist();
        assertEquals(recomputedZobrist, incZobrist);
    }

    @Test
    public void passesTrickyPerftSuite() {
        Position start = new Position();
        start.setToStartPosition();
        assertEquals(8902L, EdenBrain.perft(start, 3));

        Position kiwipete = new Position();
        kiwipete.setFENPosition("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1");
        assertEquals(2039L, EdenBrain.perft(kiwipete, 2));

        Position pos3 = new Position();
        pos3.setFENPosition("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1");
        assertEquals(2812L, EdenBrain.perft(pos3, 3));

        Position pos4 = new Position();
        pos4.setFENPosition("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1");
        assertEquals(264L, EdenBrain.perft(pos4, 2));
    }
}
