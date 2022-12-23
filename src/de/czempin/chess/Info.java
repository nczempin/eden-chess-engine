 package de.czempin.chess;
 
 
 
 
 public class Info
 {
   public static int idDepth = -1;
   public static int seldepth;
   public static long time;
   public static long nodes = 0L;
   public static int hashFull = 0;
   public static long searchtime;
   public static long qs_nodes = 0L;
   public static int ab_nodes;
   public static int hashReads;
   public static int hashHitCount;
   public static int hashDepthMiss;
   public static int hashMiss;
   public static int killerCutoffs;
   public static int killerHits;
   public static int killerReads;
   public static int betaCutoffs;
   public static int betaCutoffsFirstMove;
   public static int hashCutoffs;
   public static int killerAdds;
   public static int countValidFlag;
   public static int countValidAttempts;
   public static int pawnStructureProbes;
   public static int pawnStructureHits;
   public static int castlingNodes;
   public static int phSize;
   public static int quiescentMateCheckNodes;
   public static int testNodes = 0;
   public static int[] pieceCapturedCutoffs = new int[7];
   public static int[] pieceCapturingCutoffs = new int[7];
   public static int[] pieceMovingCutoffs = new int[7];
   public static int drawCountNodes;
   public static long wbInc;
   public static int enPriseNodes = 0;
   public static int ensureLegalNodes;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Info.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */