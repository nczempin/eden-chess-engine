/*    */ package de.czempin.chess;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public abstract class AbstractProtocol implements Protocol
/*    */ {
/*    */   protected Engine engine;
/*    */   
/*    */   public AbstractProtocol(Engine engine)
/*    */   {
/* 11 */     this.engine = engine;
/*    */   }
/*    */   
/*    */   protected static void printDebugInfo()
/*    */   {
/* 16 */     if (Options.DEBUG)
/*    */     {
/* 18 */       System.out.print("debug:");
/* 19 */       System.out.print(" qs-nodes " + Info.qs_nodes);
/* 20 */       System.out.print(" ab-nodes " + Info.ab_nodes);
/* 21 */       System.out.println();
/* 22 */       if (Options.useTranspositionTable)
/*    */       {
/* 24 */         System.out.print("debug:");
/* 25 */         System.out.print(" hash-reads " + Info.hashReads);
/* 26 */         System.out.print(" hash-hits " + Info.hashHitCount);
/* 27 */         int hitRate = 100 * Info.hashHitCount / (Info.hashReads + 1);
/* 28 */         System.out.print(" hash-hit-rate: " + hitRate + "%");
/* 29 */         System.out.print(" hash-depth-misses " + Info.hashDepthMiss);
/* 30 */         System.out.print(" hash-misses " + Info.hashMiss);
/* 31 */         System.out.println();
/*    */       }
/* 33 */       System.out.print("debug:");
/* 34 */       System.out.print(" pawn-hash-reads " + Info.pawnStructureProbes);
/* 35 */       System.out.print(" pawn-hash-hits " + Info.pawnStructureHits);
/* 36 */       int phitRate = 100 * Info.pawnStructureHits / (Info.pawnStructureProbes + 1);
/* 37 */       System.out.print(" pawn-hash-hit-rate: " + phitRate + "%");
/* 38 */       System.out.print(" pawn-hash-size " + Info.phSize);
/* 39 */       System.out.print(" pawn-hash-maxSize 256000");
/* 40 */       int phFull = 100 * Info.phSize / 256001;
/* 41 */       System.out.print(" pawn-hash-full: " + phFull + "%");
/* 42 */       System.out.println();
/* 43 */       System.out.print("debug:");
/* 44 */       System.out.print(" valid-flag-attempts " + Info.countValidAttempts);
/* 45 */       System.out.print(" valid-flag-hits " + Info.countValidFlag);
/* 46 */       System.out.println();
/* 47 */       System.out.print("debug:");
/* 48 */       System.out.print(" hash-cutoffs " + Info.hashCutoffs);
/* 49 */       System.out.print(" killer-cutoffs " + Info.killerCutoffs);
/* 50 */       System.out.print(" killer-adds " + Info.killerAdds);
/* 51 */       System.out.print(" killer-reads " + Info.killerReads);
/* 52 */       System.out.print(" killer-hits " + Info.killerHits);
/* 53 */       System.out.print(" beta-cutoffs " + Info.betaCutoffs);
/* 54 */       System.out.print(" beta-cutoffs-1st " + Info.betaCutoffsFirstMove);
/* 55 */       int betaRatio = 100 * Info.betaCutoffsFirstMove / (Info.betaCutoffs + 1);
/* 56 */       System.out.print(" beta-cutoffs-ratio " + betaRatio + "%");
/* 57 */       System.out.println();
/*    */     }
/*    */   }
/*    */   
/*    */   public void calculateTimePerMove() {
/*    */     long tpm;
/*    */  /* 64 */     if (this.engine.whiteToMove()) {
/* 65 */       tpm = this.engine.calculateTimePerMove(this.engine.getWtime(), this.engine.getWinc());
/*    */     } else
/* 67 */       tpm = this.engine.calculateTimePerMove(this.engine.getBtime(), this.engine.getBinc());
/* 68 */     this.engine.setTimePerMove(tpm);
/*    */   }
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/AbstractProtocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */