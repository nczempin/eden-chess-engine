/*    */ package de.czempin.chess.eden.brain;
/*    */ 
/*    */ 
/*    */ public class PieceCount
/*    */ {
/*    */   int pawnsCount;
/*    */   int knightsCount;
/*    */   int bishopsCount;
/*    */   int rookCount;
/*    */   int queensCount;
/*    */   
/*    */   public PieceCount(int pawnsCount, int knightsCount, int bishopsCount, int rookCount, int queensCount)
/*    */   {
/* 14 */     this.pawnsCount = pawnsCount;
/* 15 */     this.knightsCount = knightsCount;
/* 16 */     this.bishopsCount = bishopsCount;
/* 17 */     this.rookCount = rookCount;
/* 18 */     this.queensCount = queensCount;
/*    */   }
/*    */   
/*    */   public boolean loneKing()
/*    */   {
/* 23 */     if (!lightsOnly())
/* 24 */       return false;
/* 25 */     return (this.knightsCount == 0) && (this.bishopsCount == 0);
/*    */   }
/*    */   
/*    */   public boolean oneLight()
/*    */   {
/* 30 */     if (!lightsOnly())
/* 31 */       return false;
/* 32 */     if ((this.knightsCount == 1) && (this.bishopsCount == 0))
/* 33 */       return true;
/* 34 */     return (this.knightsCount == 0) && (this.bishopsCount == 1);
/*    */   }
/*    */   
/*    */   private boolean lightsOnly()
/*    */   {
/* 39 */     return (this.pawnsCount <= 0) && (this.rookCount <= 0) && (this.queensCount <= 0);
/*    */   }
/*    */   
/*    */   public boolean isNN()
/*    */   {
/* 44 */     if (!lightsOnly())
/* 45 */       return false;
/* 46 */     return (this.knightsCount == 2) && (this.bishopsCount == 0);
/*    */   }
/*    */   
/*    */   public boolean isKBNK()
/*    */   {
/* 51 */     if (!lightsOnly())
/* 52 */       return false;
/* 53 */     return (this.knightsCount == 1) && (this.bishopsCount == 1);
/*    */   }
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/PieceCount.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */