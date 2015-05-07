/*     */ package de.czempin.chess.eden.brain;
/*     */ 
/*     */ import de.czempin.chess.eden.Square;
/*     */ 
/*     */ public class Piece {
/*     */   private int square;
/*     */   
/*   8 */   public Piece(int square) { this.square = square;
/*   9 */     this.color = new Boolean(null);
/*  10 */     this.pieceType = Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public Piece(int square, Boolean color, int pieceType) {
/*  14 */     this.square = square;
/*  15 */     this.color = color;
/*  16 */     this.pieceType = pieceType;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/*  20 */     Piece other = (Piece)obj;
/*  21 */     if (this.square != other.square)
/*  22 */       return false;
/*  23 */     if (this.pieceType != other.pieceType)
/*  24 */       return false;
/*  25 */     if ((this.color == null) && (other.color != null))
/*  26 */       return false;
/*  27 */     if ((other.color == null) && (this.color != null))
/*  28 */       return false;
/*  29 */     return this.color.equals(other.color);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  33 */     return hashCode(this.square, this.color, this.pieceType);
/*     */   }
/*     */   
/*     */   private static int hashCode(int square, Boolean color, int pieceType) {
/*  37 */     int base1 = square + 1;
/*  38 */     int base2 = 1;
/*  39 */     if (color != null)
/*  40 */       base2 = color.equals(Boolean.FALSE) ? 2 : 3;
/*  41 */     int retVal = base1 + pieceType * 304 + 101 * base2;
/*  42 */     return retVal;
/*     */   }
/*     */   
/*     */   public Piece(int square, Piece piece) {
/*  46 */     this.square = square;
/*  47 */     if (piece != null) {
/*  48 */       this.color = piece.getColor();
/*  49 */       this.pieceType = piece.getType();
/*     */     }
/*     */   }
/*     */   
/*     */   Boolean getColor() {
/*  54 */     return this.color;
/*     */   }
/*     */   
/*     */   public int getType() {
/*  58 */     return this.pieceType;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  62 */     String retValue = "white ";
/*  63 */     if (this.color == null) {
/*  64 */       retValue = "unknown ";
/*  65 */     } else if (!this.color.booleanValue())
/*  66 */       retValue = "black ";
/*  67 */     switch (this.pieceType) {
/*     */     case 6: 
/*  69 */       retValue = retValue + "king";
/*  70 */       break;
/*     */     
/*     */     case 5: 
/*  73 */       retValue = retValue + "queen";
/*  74 */       break;
/*     */     
/*     */     case 4: 
/*  77 */       retValue = retValue + "rook";
/*  78 */       break;
/*     */     
/*     */     case 3: 
/*  81 */       retValue = retValue + "bishop";
/*  82 */       break;
/*     */     
/*     */     case 2: 
/*  85 */       retValue = retValue + "knight";
/*  86 */       break;
/*     */     
/*     */     case 1: 
/*  89 */       retValue = retValue + "pawn";
/*  90 */       break;
/*     */     
/*     */     default: 
/*  93 */       retValue = retValue + "nothing: " + Integer.toString(this.pieceType);
/*     */     }
/*     */     
/*  96 */     retValue = retValue + " on " + this.square;
/*  97 */     return retValue;
/*     */   }
/*     */   
/*     */   public int getSquare() {
/* 101 */     return this.square;
/*     */   }
/*     */   
/*     */   public static Piece create(int to, Boolean onMove, int pieceType) {
/* 105 */     Piece p = new Piece(to, onMove, pieceType);
/* 106 */     return p;
/*     */   }
/*     */   
/*     */   public static Piece create(Square to, Boolean onMove, int pieceType) {
/* 110 */     Piece p = new Piece(to.getIndex(), onMove, pieceType);
/* 111 */     return p;
/*     */   }
/*     */   
/*     */   public int simpleHash() {
/* 115 */     int base2 = 0;
/* 116 */     if (this.color != null)
/* 117 */       base2 = this.color.equals(Boolean.FALSE) ? 1 : 0;
/* 118 */     int retVal = this.pieceType + 7 * base2;
/* 119 */     return retVal;
/*     */   }
/*     */   
/*     */   public void addPiece(Position pos, String square) {
/* 123 */     int index = Position.decodeSquare(square);
/* 124 */     Boolean color = getColor();
/* 125 */     int rawint = getType();
/* 126 */     int tmp = convertToint(color, rawint);
/* 127 */     pos.board[index] = tmp;
/* 128 */     if (tmp > 0) {
/* 129 */       pos.whitePieces.add(new Integer(index));
/* 130 */     } else if (tmp < 0) {
/* 131 */       pos.blackPieces.add(new Integer(index));
/*     */     } else
/* 133 */       throw new AssertionError("trying to add invalid piece on square" + square);
/*     */   }
/*     */   
/*     */   static int convertToint(Boolean color, int rawint) {
/* 137 */     if (color == null)
/* 138 */       return 0;
/* 139 */     if (color == Position.BLACK) {
/* 140 */       switch (rawint) {
/*     */       case 1: 
/* 142 */         return -1;
/*     */       
/*     */       case 2: 
/* 145 */         return -2;
/*     */       
/*     */       case 3: 
/* 148 */         return -3;
/*     */       
/*     */       case 4: 
/* 151 */         return -4;
/*     */       
/*     */       case 5: 
/* 154 */         return -5;
/*     */       
/*     */       case 6: 
/* 157 */         return -6;
/*     */       }
/*     */     } else
/* 160 */       switch (rawint) {
/*     */       case 1: 
/* 162 */         return 1;
/*     */       
/*     */       case 2: 
/* 165 */         return 2;
/*     */       
/*     */       case 3: 
/* 168 */         return 3;
/*     */       
/*     */       case 4: 
/* 171 */         return 4;
/*     */       
/*     */       case 5: 
/* 174 */         return 5;
/*     */       
/*     */       case 6: 
/* 177 */         return 6;
/*     */       }
/* 179 */     throw new AssertionError("should never happen");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Boolean color;
/*     */   
/*     */ 
/*     */   int pieceType;
/*     */   
/*     */   static Piece makePiece(int next, int[] board)
/*     */   {
/* 191 */     int b = board[next];
/* 192 */     Boolean color = EdenBrain.convertColor(b);
/* 193 */     return makePiece(next, color, b);
/*     */   }
/*     */   
/*     */   static Piece makePiece(int i, Boolean color, int b) {
/* 197 */     Piece p = new Piece(i, color, Math.abs(b));
/* 198 */     return p;
/*     */   }
/*     */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/Piece.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */