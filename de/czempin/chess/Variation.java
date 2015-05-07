/*    */ package de.czempin.chess;
/*    */ 
/*    */ import java.util.Vector;
/*    */ 
/*    */ public class Variation implements Comparable { Vector variation;
/*    */   int value;
/*    */   int depth;
/*    */   
/*  9 */   public Variation(String move, Vector variation, int value, int depth) { this.variation = new Vector();
/* 10 */     this.variation.add(move);
/* 11 */     this.variation.addAll(variation);
/* 12 */     this.value = value;
/* 13 */     this.depth = depth;
/*    */   }
/*    */   
/*    */   public Vector getLine() {
/* 17 */     return this.variation;
/*    */   }
/*    */   
/*    */   public int getValue() {
/* 21 */     return this.value;
/*    */   }
/*    */   
/*    */   public int getDepth() {
/* 25 */     return this.depth;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 29 */     int multiPvValue = getValue();
/* 30 */     int multiPvDepth = getDepth();
/* 31 */     String retVal = "";
/* 32 */     retVal = retVal + "depth " + multiPvDepth;
/* 33 */     retVal = retVal + " score cp " + multiPvValue;
/* 34 */     retVal = retVal + " pv " + this.variation;
/* 35 */     return retVal;
/*    */   }
/*    */   
/*    */   public int compareTo(Object arg0) {
/* 39 */     Variation other = (Variation)arg0;
/* 40 */     if (this.depth > other.depth)
/* 41 */       return -1;
/* 42 */     if (this.depth < other.depth)
/* 43 */       return 1;
/* 44 */     if (this.value > other.value)
/* 45 */       return -1;
/* 46 */     if (this.value < other.value)
/* 47 */       return 1;
/* 48 */     if (this.variation.size() > other.variation.size())
/* 49 */       return -1;
/* 50 */     if (this.variation.size() < other.variation.size()) {
/* 51 */       return 1;
/*    */     }
/* 53 */     String thisFirstMove = (String)this.variation.firstElement();
/* 54 */     String otherFirstMove = (String)other.variation.firstElement();
/* 55 */     return thisFirstMove.compareTo(otherFirstMove);
/*    */   }
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Variation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */