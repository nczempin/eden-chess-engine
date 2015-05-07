/*    */ package de.czempin.chess.eden.brain;
/*    */ 
/*    */ import de.czempin.chess.eden.Move;
/*    */ 
/*    */ public class TransEntry
/*    */ {
/*    */   private ValidFlag vf;
/*    */   private Move move;
/*    */   private int value;
/*    */   private int depth;
/*    */   
/*    */   public TransEntry(Move move, int value, int depthToGo, long z, ValidFlag validFlag) {
/* 13 */     this.move = move;
/* 14 */     this.value = value;
/* 15 */     this.vf = validFlag;
/* 16 */     this.depth = depthToGo;
/*    */   }
/*    */   
/*    */   public int getDepth()
/*    */   {
/* 21 */     return this.depth;
/*    */   }
/*    */   
/*    */   public Move getMove()
/*    */   {
/* 26 */     return this.move;
/*    */   }
/*    */   
/*    */   public ValidFlag getValidFlag()
/*    */   {
/* 31 */     return this.vf;
/*    */   }
/*    */   
/*    */   public int getValue()
/*    */   {
/* 36 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/TransEntry.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */