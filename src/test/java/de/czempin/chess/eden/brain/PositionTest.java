package de.czempin.chess.eden.brain;

import static org.junit.Assert.*;

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
}
