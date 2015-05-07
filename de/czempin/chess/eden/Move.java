/*     */ package de.czempin.chess.eden;
/*     */ 
/*     */ import de.czempin.chess.eden.brain.Position;
/*     */ 
/*     */ public class Move implements Comparable { public int from;
/*     */   public int promotedTo;
/*     */   public int to;
/*     */   
/*   9 */   public Move(Move m) { this.value = 0;
/*  10 */     if (m != null) {
/*  11 */       this.from = m.from;
/*  12 */       this.to = m.to;
/*  13 */       this.promotedTo = m.promotedTo;
/*  14 */       this.capturedPiece = m.capturedPiece;
/*  15 */       this.movingPiece = m.movingPiece;
/*  16 */       this.value = m.value; } }
/*     */   
/*     */   public int value;
/*     */   public int capturedPiece;
/*     */   public int movingPiece;
/*  21 */   public Move(Position positionBefore, int from, int to) { this(positionBefore, from, to, 0); }
/*     */   
/*     */   public Move(Position positionBefore, int from, int to, de.czempin.chess.eden.brain.Piece promotedTo)
/*     */   {
/*  25 */     this(positionBefore, from, to, promotedTo.getType());
/*     */   }
/*     */   
/*     */   public Move(Position positionBefore, int from, int to, int promotedTo) {
/*  29 */     this.value = 0;
/*  30 */     this.from = from;
/*  31 */     this.to = to;
/*  32 */     this.promotedTo = promotedTo;
/*  33 */     this.movingPiece = positionBefore.board[from];
/*     */   }
/*     */   
/*     */   public Move(Position position, int from2, int next, de.czempin.chess.eden.brain.Piece promotedTo, int capturedPiece) {
/*  37 */     this(position, from2, next, promotedTo);
/*  38 */     this.capturedPiece = capturedPiece;
/*  39 */     this.movingPiece = position.board[from2];
/*     */   }
/*     */   
/*     */   public Move(Position position, int from2, int next, int promotedTo, int capturedPiece) {
/*  43 */     this(position, from2, next, promotedTo);
/*  44 */     this.capturedPiece = capturedPiece;
/*  45 */     this.movingPiece = position.board[from2];
/*     */   }
/*     */   
/*     */   private Move(Position pos, String move) {
/*  49 */     this.value = 0;
/*  50 */     int from = Position.decodeSquare(move.substring(0, 2));
/*  51 */     int to = new Square(move.substring(2, 4)).getIndex();
/*  52 */     String promotedTo = move.substring(4);
/*  53 */     int newPiece = 0;
/*  54 */     if (!promotedTo.equals("")) {
/*  55 */       newPiece = Position.decodePiece(promotedTo);
/*  56 */       this.capturedPiece = newPiece;
/*     */     }
/*  58 */     this.movingPiece = pos.board[from];
/*  59 */     this.from = from;
/*  60 */     this.to = to;
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/*  64 */     if (other == this)
/*  65 */       return true;
/*  66 */     if (!(other instanceof Move)) {
/*  67 */       return false;
/*     */     }
/*  69 */     Move otherMove = (Move)other;
/*  70 */     return getText().equals(otherMove.getText());
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  75 */     return getText().hashCode();
/*     */   }
/*     */   
/*     */   public String toString() {
/*  79 */     String retVal = getText();
/*  80 */     return retVal;
/*     */   }
/*     */   
/*     */   public int compareTo(Object o) {
/*  84 */     if (this == o)
/*  85 */       return 0;
/*  86 */     Move other = (Move)o;
/*  87 */     int thisCaptureValue = Math.abs(this.capturedPiece);
/*  88 */     int otherCaptureValue = Math.abs(other.capturedPiece);
/*  89 */     if (thisCaptureValue == 6)
/*  90 */       return -1;
/*  91 */     if (otherCaptureValue == 6)
/*  92 */       return 1;
/*  93 */     if (this.promotedTo == 5)
/*  94 */       return -1;
/*  95 */     if (other.promotedTo == 5)
/*  96 */       return 1;
/*  97 */     if ((this.capturedPiece != 0) && (other.capturedPiece == 0))
/*  98 */       return -1;
/*  99 */     if ((this.capturedPiece == 0) && (other.capturedPiece != 0))
/* 100 */       return 1;
/* 101 */     if ((this.capturedPiece != 0) && (other.capturedPiece != 0)) {
/* 102 */       int thisMovingAbs = Math.abs(this.movingPiece);
/* 103 */       int otherMovingAbs = Math.abs(other.movingPiece);
/* 104 */       if ((thisMovingAbs == 1) && (otherMovingAbs != 1))
/* 105 */         return -1;
/* 106 */       if ((otherMovingAbs == 1) && (thisMovingAbs != 1))
/* 107 */         return 1;
/* 108 */       if ((thisCaptureValue == 5) && (otherCaptureValue != 5))
/* 109 */         return -1;
/* 110 */       if ((otherCaptureValue == 5) && (thisCaptureValue != 5))
/* 111 */         return 1;
/* 112 */       if ((thisCaptureValue == 1) && (otherCaptureValue != 1))
/* 113 */         return 1;
/* 114 */       if ((otherCaptureValue == 1) && (thisCaptureValue != 1))
/* 115 */         return -1;
/* 116 */       if (thisCaptureValue < otherCaptureValue)
/* 117 */         return 1;
/* 118 */       if (otherCaptureValue < thisCaptureValue)
/* 119 */         return -1;
/* 120 */       if (thisMovingAbs < otherMovingAbs)
/* 121 */         return -1;
/* 122 */       if (otherMovingAbs < thisMovingAbs)
/* 123 */         return 1;
/* 124 */       int thisPieceSquareValue = getPieceSquareValue(this.from, this.to, this.movingPiece);
/* 125 */       int otherPieceSquareValue = getPieceSquareValue(other.from, other.to, other.movingPiece);
/* 126 */       if (thisPieceSquareValue != otherPieceSquareValue)
/* 127 */         return otherPieceSquareValue - thisPieceSquareValue;
/* 128 */       if (this.promotedTo != 0)
/* 129 */         return 1;
/* 130 */       if (other.promotedTo != 0)
/* 131 */         return -1;
/* 132 */       int fromDiff = this.from - other.from;
/* 133 */       int toDiff = this.to - other.to;
/* 134 */       if (this.movingPiece > 0) {
/* 135 */         if (toDiff != 0)
/* 136 */           return -toDiff;
/* 137 */         if (fromDiff != 0)
/* 138 */           return -fromDiff;
/*     */       } else {
/* 140 */         if (toDiff != 0)
/* 141 */           return toDiff;
/* 142 */         if (fromDiff != 0)
/* 143 */           return fromDiff;
/*     */       }
/* 145 */       return 1;
/*     */     }
/* 147 */     if ((this.movingPiece == 6) || (this.movingPiece == -6)) {
/* 148 */       if (Math.abs(this.from - this.to) == 2)
/* 149 */         return -1;
/* 150 */     } else if (((other.movingPiece == 6) || (other.movingPiece == -6)) && (Math.abs(this.from - this.to) == 2))
/* 151 */       return 1;
/* 152 */     if ((this.promotedTo != 0) && (other.promotedTo == 0))
/* 153 */       return 1;
/* 154 */     if ((other.promotedTo != 0) && (this.promotedTo == 0))
/* 155 */       return -1;
/* 156 */     int promoteDiff = other.promotedTo - this.promotedTo;
/* 157 */     if (promoteDiff != 0)
/* 158 */       return promoteDiff;
/* 159 */     int thisPieceSquareValue = getPieceSquareValue(this.from, this.to, this.movingPiece);
/* 160 */     int otherPieceSquareValue = getPieceSquareValue(other.from, other.to, other.movingPiece);
/* 161 */     if (thisPieceSquareValue != otherPieceSquareValue)
/* 162 */       return otherPieceSquareValue - thisPieceSquareValue;
/* 163 */     int thisMovingAbs = Math.abs(this.movingPiece);
/* 164 */     int otherMovingAbs = Math.abs(other.movingPiece);
/* 165 */     if (thisMovingAbs < otherMovingAbs)
/* 166 */       return -1;
/* 167 */     if (otherMovingAbs < thisMovingAbs)
/* 168 */       return 1;
/* 169 */     int fromDiff = this.from - other.from;
/* 170 */     int toDiff = this.to - other.to;
/* 171 */     if (this.movingPiece > 0) {
/* 172 */       if (toDiff != 0)
/* 173 */         return -toDiff;
/* 174 */       if (fromDiff != 0)
/* 175 */         return -fromDiff;
/*     */     } else {
/* 177 */       if (toDiff != 0)
/* 178 */         return toDiff;
/* 179 */       if (fromDiff != 0)
/* 180 */         return fromDiff;
/*     */     }
/* 182 */     return 1;
/*     */   }
/*     */   
/*     */   private int getPieceSquareValue(int from, int to, int piece) {
/* 186 */     if (piece == 1) {
/* 187 */       if ((to > 80) || (to < 20)) {
/* 188 */         return 300;
/*     */       }
/* 190 */       return Position.whitePawnSquareValues[(79 - to)] - Position.whitePawnSquareValues[(79 - from)]; }
/* 191 */     if (piece == -1) {
/* 192 */       if ((to < 20) || (to > 80)) {
/* 193 */         return 300;
/*     */       }
/* 195 */       return Position.blackPawnSquareValues[(to - 20)] - Position.blackPawnSquareValues[(from - 20)]; }
/* 196 */     if ((piece == 2) || (piece == -2))
/* 197 */       return Position.getKnightPcSqValue(to) - Position.getKnightPcSqValue(from);
/* 198 */     if ((piece == 3) || (piece == -3))
/* 199 */       return Position.getBishopValue(to) - Position.getBishopValue(from);
/* 200 */     if ((piece == 5) || (piece == -5)) {
/* 201 */       int retVal = Position.getBishopValue(to) - Position.getBishopValue(from);
/* 202 */       return retVal;
/*     */     }
/* 204 */     return 0;
/*     */   }
/*     */   
/*     */   public String getText()
/*     */   {
/* 209 */     String promotedPiece = "";
/* 210 */     switch (Math.abs(this.promotedTo)) {
/*     */     case 2: 
/* 212 */       promotedPiece = "n";
/* 213 */       break;
/*     */     
/*     */     case 3: 
/* 216 */       promotedPiece = "b";
/* 217 */       break;
/*     */     
/*     */     case 4: 
/* 220 */       promotedPiece = "r";
/* 221 */       break;
/*     */     
/*     */     case 5: 
/* 224 */       promotedPiece = "q";
/* 225 */       break;
/*     */     
/*     */     case 1: 
/*     */     default: 
/* 229 */       throw new AssertionError("should never happen");
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 234 */     return Position.encodeSquare(this.from) + Position.encodeSquare(this.to) + promotedPiece;
/*     */   }
/*     */   
/*     */   public boolean isCastling(int piece) {
/* 238 */     return (piece == 6) && (Math.abs(this.from - this.to) == 2);
/*     */   }
/*     */   
/*     */   public static Move create(Position p, String moveString) {
/* 242 */     if ((moveString.length() < 3) || (moveString.length() > 5))
/* 243 */       throw new IllegalArgumentException("move not recognized: " + moveString);
/* 244 */     if ("...".equals(moveString)) {
/* 245 */       System.out.println("debug: nullmove not supported");
/* 246 */       return null;
/*     */     }
/* 248 */     return new Move(p, moveString);
/*     */   }
/*     */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/Move.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */