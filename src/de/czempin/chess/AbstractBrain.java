 package de.czempin.chess;
 
 
 public abstract class AbstractBrain
   implements Brain
 {
   public void setToStartPosition()
   {
     setFENPosition("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
   }
   
   public abstract void setFENPosition(String paramString);
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/AbstractBrain.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */