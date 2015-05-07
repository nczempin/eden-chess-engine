/*    */ package de.czempin.chess.eden.brain;
/*    */ 
/*    */ import de.czempin.chess.eden.Move;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class KillerEntry
/*    */ {
/*    */   private Move move;
/*    */   private int count;
/*    */   
/*    */   public KillerEntry(Move move)
/*    */   {
/* 15 */     this.move = move;
/* 16 */     this.count = 0;
/*    */   }
/*    */   
/*    */   public Move getMove()
/*    */   {
/* 21 */     return this.move;
/*    */   }
/*    */   
/*    */   public void increment()
/*    */   {
/* 26 */     this.count += 1;
/*    */   }
/*    */   
/*    */   public int getCount()
/*    */   {
/* 31 */     return this.count;
/*    */   }
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/KillerEntry.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */