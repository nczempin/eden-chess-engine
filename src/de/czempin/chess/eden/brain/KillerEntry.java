 package de.czempin.chess.eden.brain;
 
 import de.czempin.chess.eden.Move;
 
 
 
 
 public class KillerEntry
 {
   private Move move;
   private int count;
   
   public KillerEntry(Move move)
   {
     this.move = move;
     this.count = 0;
   }
   
   public Move getMove()
   {
     return this.move;
   }
   
   public void increment()
   {
     this.count += 1;
   }
   
   public int getCount()
   {
     return this.count;
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/KillerEntry.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */