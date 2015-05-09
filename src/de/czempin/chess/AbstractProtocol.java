 package de.czempin.chess;
 
 import java.io.PrintStream;
 
 public abstract class AbstractProtocol implements Protocol
 {
   protected Engine engine;
   
   public AbstractProtocol(Engine engine)
   {
     this.engine = engine;
   }
   
   protected static void printDebugInfo()
   {
     if (Options.DEBUG)
     {
       System.out.print("debug:");
       System.out.print(" qs-nodes " + Info.qs_nodes);
       System.out.print(" ab-nodes " + Info.ab_nodes);
       System.out.println();
       if (Options.useTranspositionTable)
       {
         System.out.print("debug:");
         System.out.print(" hash-reads " + Info.hashReads);
         System.out.print(" hash-hits " + Info.hashHitCount);
         int hitRate = 100 * Info.hashHitCount / (Info.hashReads + 1);
         System.out.print(" hash-hit-rate: " + hitRate + "%");
         System.out.print(" hash-depth-misses " + Info.hashDepthMiss);
         System.out.print(" hash-misses " + Info.hashMiss);
         System.out.println();
       }
       System.out.print("debug:");
       System.out.print(" pawn-hash-reads " + Info.pawnStructureProbes);
       System.out.print(" pawn-hash-hits " + Info.pawnStructureHits);
       int phitRate = 100 * Info.pawnStructureHits / (Info.pawnStructureProbes + 1);
       System.out.print(" pawn-hash-hit-rate: " + phitRate + "%");
       System.out.print(" pawn-hash-size " + Info.phSize);
       System.out.print(" pawn-hash-maxSize 256000");
       int phFull = 100 * Info.phSize / 256001;
       System.out.print(" pawn-hash-full: " + phFull + "%");
       System.out.println();
       System.out.print("debug:");
       System.out.print(" valid-flag-attempts " + Info.countValidAttempts);
       System.out.print(" valid-flag-hits " + Info.countValidFlag);
       System.out.println();
       System.out.print("debug:");
       System.out.print(" hash-cutoffs " + Info.hashCutoffs);
       System.out.print(" killer-cutoffs " + Info.killerCutoffs);
       System.out.print(" killer-adds " + Info.killerAdds);
       System.out.print(" killer-reads " + Info.killerReads);
       System.out.print(" killer-hits " + Info.killerHits);
       System.out.print(" beta-cutoffs " + Info.betaCutoffs);
       System.out.print(" beta-cutoffs-1st " + Info.betaCutoffsFirstMove);
       int betaRatio = 100 * Info.betaCutoffsFirstMove / (Info.betaCutoffs + 1);
       System.out.print(" beta-cutoffs-ratio " + betaRatio + "%");
       System.out.println();
     }
   }
   
   public void calculateTimePerMove() {
     long tpm;
     if (this.engine.whiteToMove()) {
       tpm = this.engine.calculateTimePerMove(this.engine.getWtime(), this.engine.getWinc());
     } else
       tpm = this.engine.calculateTimePerMove(this.engine.getBtime(), this.engine.getBinc());
     this.engine.setTimePerMove(tpm);
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/AbstractProtocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */