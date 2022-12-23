 package de.czempin.chess.eden;
 
 import de.czempin.chess.eden.brain.Position;
 
 
 
 
 
 public class Square
 {
   private int index;
   
   public Square(String string)
   {
     this.index = Position.decodeSquare(string);
   }
   
   public int getIndex()
   {
     return this.index;
   }
   
   public int hashCode()
   {
     return this.index + 1;
   }
   
   public boolean equals(Object other)
   {
     if (other == this)
       return true;
     if (!(other instanceof Square))
       return false;
     Square otherSquare = (Square)other;
     return otherSquare.getIndex() == getIndex();
   }
   
   public String toString()
   {
     return Position.encodeSquare(this.index);
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/Square.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */