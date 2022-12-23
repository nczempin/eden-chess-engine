 package de.czempin.chess.eden.brain;
 
 
 public class PieceCount
 {
   int pawnsCount;
   int knightsCount;
   int bishopsCount;
   int rookCount;
   int queensCount;
   
   public PieceCount(int pawnsCount, int knightsCount, int bishopsCount, int rookCount, int queensCount)
   {
     this.pawnsCount = pawnsCount;
     this.knightsCount = knightsCount;
     this.bishopsCount = bishopsCount;
     this.rookCount = rookCount;
     this.queensCount = queensCount;
   }
   
   public boolean loneKing()
   {
     if (!lightsOnly())
       return false;
     return (this.knightsCount == 0) && (this.bishopsCount == 0);
   }
   
   public boolean oneLight()
   {
     if (!lightsOnly())
       return false;
     if ((this.knightsCount == 1) && (this.bishopsCount == 0))
       return true;
     return (this.knightsCount == 0) && (this.bishopsCount == 1);
   }
   
   private boolean lightsOnly()
   {
     return (this.pawnsCount <= 0) && (this.rookCount <= 0) && (this.queensCount <= 0);
   }
   
   public boolean isNN()
   {
     if (!lightsOnly())
       return false;
     return (this.knightsCount == 2) && (this.bishopsCount == 0);
   }
   
   public boolean isKBNK()
   {
     if (!lightsOnly())
       return false;
     return (this.knightsCount == 1) && (this.bishopsCount == 1);
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/PieceCount.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */