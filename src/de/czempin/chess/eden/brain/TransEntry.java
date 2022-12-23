 package de.czempin.chess.eden.brain;
 
 import de.czempin.chess.eden.Move;
 
 public class TransEntry
 {
   private ValidFlag vf;
   private Move move;
   private int value;
   private int depth;
   
   public TransEntry(Move move, int value, int depthToGo, long z, ValidFlag validFlag) {
     this.move = move;
     this.value = value;
     this.vf = validFlag;
     this.depth = depthToGo;
   }
   
   public int getDepth()
   {
     return this.depth;
   }
   
   public Move getMove()
   {
     return this.move;
   }
   
   public ValidFlag getValidFlag()
   {
     return this.vf;
   }
   
   public int getValue()
   {
     return this.value;
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/TransEntry.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */