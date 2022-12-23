 package de.czempin.chess.eden.brain;
 
 import de.czempin.chess.eden.Square;
 
 public class Piece {
   private int square;
   
   public Piece(int square) { this.square = square;
     this.color = new Boolean(null);
     this.pieceType = Integer.MAX_VALUE;
   }
   
   public Piece(int square, Boolean color, int pieceType) {
     this.square = square;
     this.color = color;
     this.pieceType = pieceType;
   }
   
   public boolean equals(Object obj) {
     Piece other = (Piece)obj;
     if (this.square != other.square)
       return false;
     if (this.pieceType != other.pieceType)
       return false;
     if ((this.color == null) && (other.color != null))
       return false;
     if ((other.color == null) && (this.color != null))
       return false;
     return this.color.equals(other.color);
   }
   
   public int hashCode() {
     return hashCode(this.square, this.color, this.pieceType);
   }
   
   private static int hashCode(int square, Boolean color, int pieceType) {
     int base1 = square + 1;
     int base2 = 1;
     if (color != null)
       base2 = color.equals(Boolean.FALSE) ? 2 : 3;
     int retVal = base1 + pieceType * 304 + 101 * base2;
     return retVal;
   }
   
   public Piece(int square, Piece piece) {
     this.square = square;
     if (piece != null) {
       this.color = piece.getColor();
       this.pieceType = piece.getType();
     }
   }
   
   Boolean getColor() {
     return this.color;
   }
   
   public int getType() {
     return this.pieceType;
   }
   
   public String toString() {
     String retValue = "white ";
     if (this.color == null) {
       retValue = "unknown ";
     } else if (!this.color.booleanValue())
       retValue = "black ";
     switch (this.pieceType) {
     case 6: 
       retValue = retValue + "king";
       break;
     
     case 5: 
       retValue = retValue + "queen";
       break;
     
     case 4: 
       retValue = retValue + "rook";
       break;
     
     case 3: 
       retValue = retValue + "bishop";
       break;
     
     case 2: 
       retValue = retValue + "knight";
       break;
     
     case 1: 
       retValue = retValue + "pawn";
       break;
     
     default: 
       retValue = retValue + "nothing: " + Integer.toString(this.pieceType);
     }
     
     retValue = retValue + " on " + this.square;
     return retValue;
   }
   
   public int getSquare() {
     return this.square;
   }
   
   public static Piece create(int to, Boolean onMove, int pieceType) {
     Piece p = new Piece(to, onMove, pieceType);
     return p;
   }
   
   public static Piece create(Square to, Boolean onMove, int pieceType) {
     Piece p = new Piece(to.getIndex(), onMove, pieceType);
     return p;
   }
   
   public int simpleHash() {
     int base2 = 0;
     if (this.color != null)
       base2 = this.color.equals(Boolean.FALSE) ? 1 : 0;
     int retVal = this.pieceType + 7 * base2;
     return retVal;
   }
   
   public void addPiece(Position pos, String square) {
     int index = Position.decodeSquare(square);
     Boolean color = getColor();
     int rawint = getType();
     int tmp = convertToint(color, rawint);
     pos.board[index] = tmp;
     if (tmp > 0) {
       pos.whitePieces.add(new Integer(index));
     } else if (tmp < 0) {
       pos.blackPieces.add(new Integer(index));
     } else
       throw new AssertionError("trying to add invalid piece on square" + square);
   }
   
   static int convertToint(Boolean color, int rawint) {
     if (color == null)
       return 0;
     if (color == Position.BLACK) {
       switch (rawint) {
       case 1: 
         return -1;
       
       case 2: 
         return -2;
       
       case 3: 
         return -3;
       
       case 4: 
         return -4;
       
       case 5: 
         return -5;
       
       case 6: 
         return -6;
       }
     } else
       switch (rawint) {
       case 1: 
         return 1;
       
       case 2: 
         return 2;
       
       case 3: 
         return 3;
       
       case 4: 
         return 4;
       
       case 5: 
         return 5;
       
       case 6: 
         return 6;
       }
     throw new AssertionError("should never happen");
   }
   
 
 
   public Boolean color;
   
 
   int pieceType;
   
   static Piece makePiece(int next, int[] board)
   {
     int b = board[next];
     Boolean color = EdenBrain.convertColor(b);
     return makePiece(next, color, b);
   }
   
   static Piece makePiece(int i, Boolean color, int b) {
     Piece p = new Piece(i, color, Math.abs(b));
     return p;
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/Piece.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */