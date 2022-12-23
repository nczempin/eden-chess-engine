 package de.czempin.chess;
 
 import java.util.Vector;
 
 public class Variation implements Comparable { Vector variation;
   int value;
   int depth;
   
   public Variation(String move, Vector variation, int value, int depth) { this.variation = new Vector();
     this.variation.add(move);
     this.variation.addAll(variation);
     this.value = value;
     this.depth = depth;
   }
   
   public Vector getLine() {
     return this.variation;
   }
   
   public int getValue() {
     return this.value;
   }
   
   public int getDepth() {
     return this.depth;
   }
   
   public String toString() {
     int multiPvValue = getValue();
     int multiPvDepth = getDepth();
     String retVal = "";
     retVal = retVal + "depth " + multiPvDepth;
     retVal = retVal + " score cp " + multiPvValue;
     retVal = retVal + " pv " + this.variation;
     return retVal;
   }
   
   public int compareTo(Object arg0) {
     Variation other = (Variation)arg0;
     if (this.depth > other.depth)
       return -1;
     if (this.depth < other.depth)
       return 1;
     if (this.value > other.value)
       return -1;
     if (this.value < other.value)
       return 1;
     if (this.variation.size() > other.variation.size())
       return -1;
     if (this.variation.size() < other.variation.size()) {
       return 1;
     }
     String thisFirstMove = (String)this.variation.firstElement();
     String otherFirstMove = (String)other.variation.firstElement();
     return thisFirstMove.compareTo(otherFirstMove);
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Variation.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */