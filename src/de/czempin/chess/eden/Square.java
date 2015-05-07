/*    */ package de.czempin.chess.eden;
/*    */ 
/*    */ import de.czempin.chess.eden.brain.Position;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Square
/*    */ {
/*    */   private int index;
/*    */   
/*    */   public Square(String string)
/*    */   {
/* 15 */     this.index = Position.decodeSquare(string);
/*    */   }
/*    */   
/*    */   public int getIndex()
/*    */   {
/* 20 */     return this.index;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 25 */     return this.index + 1;
/*    */   }
/*    */   
/*    */   public boolean equals(Object other)
/*    */   {
/* 30 */     if (other == this)
/* 31 */       return true;
/* 32 */     if (!(other instanceof Square))
/* 33 */       return false;
/* 34 */     Square otherSquare = (Square)other;
/* 35 */     return otherSquare.getIndex() == getIndex();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 40 */     return Position.encodeSquare(this.index);
/*    */   }
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/Square.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */