/*      */ package de.czempin.chess.eden.brain;
/*      */ 
/*      */ import de.czempin.chess.Info;
/*      */ import de.czempin.chess.Options;
/*      */ import de.czempin.chess.eden.Move;
/*      */ import de.czempin.chess.eden.Square;
/*      */ import java.io.PrintStream;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.TreeSet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Position
/*      */ {
/*      */   private void adjustKingSquaresForKBNK()
/*      */   {
/*   26 */     Boolean lightSquaredBishop = null;
/*   27 */     for (Iterator itW = this.whitePieces.iterator(); itW.hasNext();)
/*      */     {
/*   29 */       int s = ((Integer)itW.next()).intValue();
/*   30 */       int piece = this.board[s];
/*   31 */       if (piece == 3)
/*      */       {
/*   33 */         if (s % 2 == 0) {
/*   34 */           lightSquaredBishop = Boolean.TRUE; break;
/*      */         }
/*   36 */         lightSquaredBishop = Boolean.FALSE;
/*   37 */         break;
/*      */       }
/*      */     }
/*      */     
/*   41 */     if (lightSquaredBishop == null)
/*      */     {
/*   43 */       for (Iterator itB = this.blackPieces.iterator(); itB.hasNext();)
/*      */       {
/*   45 */         int s = ((Integer)itB.next()).intValue();
/*   46 */         int piece = this.board[s];
/*   47 */         if (piece == -3)
/*      */         {
/*   49 */           if (s % 2 == 0) {
/*   50 */             lightSquaredBishop = Boolean.TRUE; break;
/*      */           }
/*   52 */           lightSquaredBishop = Boolean.FALSE;
/*   53 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*   58 */     if (!lightSquaredBishop.booleanValue()) {
/*   59 */       this.kingEndgameSquareValues = this.kingEndgameSquareValuesKBNKLight;
/*      */     } else {
/*   61 */       this.kingEndgameSquareValues = this.kingEndgameSquareValuesKBNKDark;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void clearSquare(int square, int delta, int[] board) {
/*   66 */     board[(square + delta)] = 0;
/*      */   }
/*      */   
/*      */   static int convertToint(Boolean color, int rawint)
/*      */   {
/*   71 */     if (color == null)
/*   72 */       return 0;
/*   73 */     if (color == BLACK) {
/*   74 */       switch (rawint)
/*      */       {
/*      */       case 1: 
/*   77 */         return -1;
/*      */       
/*      */       case 2: 
/*   80 */         return -2;
/*      */       
/*      */       case 3: 
/*   83 */         return -3;
/*      */       
/*      */       case 4: 
/*   86 */         return -4;
/*      */       
/*      */       case 5: 
/*   89 */         return -5;
/*      */       
/*      */       case 6: 
/*   92 */         return -6;
/*      */       }
/*      */     } else
/*   95 */       switch (rawint)
/*      */       {
/*      */       case 1: 
/*   98 */         return 1;
/*      */       
/*      */       case 2: 
/*  101 */         return 2;
/*      */       
/*      */       case 3: 
/*  104 */         return 3;
/*      */       
/*      */       case 4: 
/*  107 */         return 4;
/*      */       
/*      */       case 5: 
/*  110 */         return 5;
/*      */       
/*      */       case 6: 
/*  113 */         return 6;
/*      */       }
/*  115 */     throw new AssertionError("should never happen");
/*      */   }
/*      */   
/*      */   static void copyBoard(int[] from, int[] to)
/*      */   {
/*  120 */     System.arraycopy(from, 0, to, 0, from.length);
/*      */   }
/*      */   
/*      */   static Position copyPosition(Position position)
/*      */   {
/*      */     try
/*      */     {
/*  127 */       return new Position(position);
/*      */ 
/*      */     }
/*      */     catch (StackOverflowError e)
/*      */     {
/*  132 */       throw e;
/*      */     }
/*      */   }
/*      */   
/*      */   public static Position createPosition(Position p, Move move)
/*      */     throws ThreeRepetitionsAB
/*      */   {
/*  139 */     Position newPos = copyPosition(p);
/*  140 */     newPos.makeMove(move);
/*  141 */     return newPos;
/*      */   }
/*      */   
/*      */   public static Position createTestPosition(Position p, Move move)
/*      */   {
/*  146 */     Position newPos = copyPosition(p);
/*  147 */     Info.testNodes += 1;
/*  148 */     newPos.makeTestMove(move);
/*  149 */     return newPos;
/*      */   }
/*      */   
/*      */   public static int decodePiece(String promotedTo)
/*      */   {
/*  154 */     int retValue = 0;
/*  155 */     if (promotedTo.equals("q")) {
/*  156 */       retValue = 5;
/*      */     }
/*  158 */     else if (promotedTo.equals("r")) {
/*  159 */       retValue = 4;
/*      */     }
/*  161 */     else if (promotedTo.equals("b")) {
/*  162 */       retValue = 3;
/*      */     }
/*  164 */     else if (promotedTo.equals("n"))
/*  165 */       retValue = 2;
/*  166 */     return retValue;
/*      */   }
/*      */   
/*      */   public static int decodeSquare(String square)
/*      */   {
/*  171 */     char letter = square.charAt(0);
/*  172 */     char digit = square.charAt(1);
/*  173 */     int one = Character.getNumericValue(letter) - Character.getNumericValue('a') + 1;
/*  174 */     int ten = Character.getNumericValue(digit);
/*  175 */     int index = 10 * ten + one;
/*  176 */     return index;
/*      */   }
/*      */   
/*      */   public static String encodeSquare(int square)
/*      */   {
/*  181 */     int ten = square / 10;
/*  182 */     int one = square - ten * 10;
/*  183 */     Character letter = new Character((char)(97 + one - 1));
/*  184 */     String digit = String.valueOf(ten);
/*  185 */     return letter + digit;
/*      */   }
/*      */   
/*      */   public static String encodeSquare(Square square)
/*      */   {
/*  190 */     return encodeSquare(square);
/*      */   }
/*      */   
/*      */   public static void generateBishopMoves(Position position, SortedSet moves, int from, Boolean color)
/*      */   {
/*  195 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  197 */       int next = from + i * 9;
/*  198 */       boolean finished = tryMove(position, moves, from, next, color);
/*  199 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  203 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  205 */       int next = from + i * -9;
/*  206 */       boolean finished = tryMove(position, moves, from, next, color);
/*  207 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  211 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  213 */       int next = from + i * 11;
/*  214 */       boolean finished = tryMove(position, moves, from, next, color);
/*  215 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  219 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  221 */       int next = from + i * -11;
/*  222 */       boolean finished = tryMove(position, moves, from, next, color);
/*  223 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void generateCastling(Position position, Set moves, int fromSquare)
/*      */   {
/*  231 */     int from = fromSquare;
/*  232 */     int p = position.board[from];
/*  233 */     int kingHome = -1;
/*      */     boolean castlingLong;
/*      */     boolean castlingShort;
/*  236 */     boolean castlingLong; if (p == 6)
/*      */     {
/*  238 */       kingHome = 15;
/*  239 */       boolean castlingShort = position.getCastleShortWhite();
/*  240 */       castlingLong = position.getCastleLongWhite();
/*      */     }
/*      */     else {
/*  243 */       kingHome = 85;
/*  244 */       castlingShort = position.getCastleShortBlack();
/*  245 */       castlingLong = position.getCastleLongBlack();
/*      */     }
/*  247 */     if (from != kingHome)
/*  248 */       return;
/*  249 */     int pieceNextToKingR = position.board[(from + 1)];
/*  250 */     if ((castlingShort) && (pieceNextToKingR == 0) && (position.board[(from + 2)] == 0))
/*      */     {
/*  252 */       if (position.isReceivingCheck())
/*  253 */         return;
/*  254 */       Move testCastleThrough = new Move(position, fromSquare, from + 1);
/*  255 */       Position testPosition = createTestPosition(position, testCastleThrough);
/*  256 */       Info.castlingNodes += 1;
/*  257 */       if (!testPosition.isGivingCheckForCastling(from + 1))
/*  258 */         moves.add(new Move(position, fromSquare, from + 2));
/*      */     }
/*  260 */     if ((castlingLong) && (Math.abs(position.board[(from - 1)]) == 0) && (position.board[(from - 2)] == 0) && (position.board[(from - 3)] == 0))
/*      */     {
/*  262 */       if (position.isReceivingCheck())
/*  263 */         return;
/*  264 */       Move testCastleThrough = new Move(position, fromSquare, from - 1);
/*  265 */       Position testPosition = createTestPosition(position, testCastleThrough);
/*  266 */       if (!testPosition.isGivingCheckForCastling(from - 1)) {
/*  267 */         moves.add(new Move(position, fromSquare, from - 2));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateKingCaptures(Position position, Set moves, int from, Boolean color) {
/*  273 */     int[] kingMoves = {
/*  274 */       9, 10, 11, -1, 1, -9, -10, -11 };
/*      */     
/*  276 */     for (int i = 0; i < kingMoves.length; i++)
/*      */     {
/*  278 */       int next = from + kingMoves[i];
/*  279 */       if (!invalidSquare(next))
/*      */       {
/*  281 */         int capturedPiece = position.board[next];
/*  282 */         if (capturedPiece != 0) {
/*  283 */           if ((capturedPiece < 0) && (color.equals(WHITE))) {
/*  284 */             moves.add(new Move(position, from, next, 0, capturedPiece));
/*      */           }
/*  286 */           else if ((capturedPiece > 0) && (color.equals(BLACK))) {
/*  287 */             moves.add(new Move(position, from, next, 0, capturedPiece));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static void generateKingMoves(Position position, Set moves, int from, Boolean color) {
/*  295 */     generateKingMovesNoCastling(position, moves, from, color);
/*  296 */     generateCastling(position, moves, from);
/*      */   }
/*      */   
/*      */   public static void generateKingMovesNoCastling(Position position, Set moves, int from, Boolean color)
/*      */   {
/*  301 */     int[] kingMoves = {
/*  302 */       9, 10, 11, -1, 1, -9, -10, -11 };
/*      */     
/*  304 */     for (int i = 0; i < kingMoves.length; i++)
/*      */     {
/*  306 */       int next = from + kingMoves[i];
/*  307 */       if (!invalidSquare(next))
/*      */       {
/*  309 */         int capturedPiece = position.board[next];
/*  310 */         if (capturedPiece == 0) {
/*  311 */           moves.add(new Move(position, from, next));
/*      */         }
/*  313 */         else if ((capturedPiece < 0) && (color.equals(WHITE))) {
/*  314 */           moves.add(new Move(position, from, next, 0, capturedPiece));
/*      */         }
/*  316 */         else if ((capturedPiece > 0) && (color.equals(BLACK))) {
/*  317 */           moves.add(new Move(position, from, next, 0, capturedPiece));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateKnightCaptures(Position position, Set moves, int from, Boolean color)
/*      */   {
/*  325 */     int[] knightMoves = {
/*  326 */       19, 21, 8, 12, -19, -21, -8, -12 };
/*      */     
/*  328 */     for (int i = 0; i < knightMoves.length; i++)
/*      */     {
/*  330 */       int next = from + knightMoves[i];
/*  331 */       if (!invalidSquare(next))
/*      */       {
/*  333 */         int capturedPiece = position.board[next];
/*  334 */         if (capturedPiece != 0)
/*      */         {
/*  336 */           if ((capturedPiece < 0) && (color.equals(WHITE)))
/*  337 */             moves.add(new Move(position, from, next, 0, capturedPiece));
/*  338 */           if ((capturedPiece > 0) && (color.equals(BLACK))) {
/*  339 */             moves.add(new Move(position, from, next, 0, capturedPiece));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateKnightMoves(Position position, Set moves, int from, Boolean color)
/*      */   {
/*  348 */     int[] knightMoves = {
/*  349 */       19, 21, 8, 12, -19, -21, -8, -12 };
/*      */     
/*  351 */     for (int i = 0; i < knightMoves.length; i++)
/*      */     {
/*  353 */       int next = from + knightMoves[i];
/*  354 */       if (!invalidSquare(next))
/*      */       {
/*  356 */         int capturedPiece = position.board[next];
/*  357 */         if (capturedPiece == 0) {
/*  358 */           moves.add(new Move(position, from, next));
/*      */         }
/*  360 */         else if ((capturedPiece < 0) && (color.equals(WHITE))) {
/*  361 */           moves.add(new Move(position, from, next, 0, capturedPiece));
/*      */         }
/*  363 */         else if ((capturedPiece > 0) && (color.equals(BLACK))) {
/*  364 */           moves.add(new Move(position, from, next, 0, capturedPiece));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void generatePawnCapture(Position position, SortedSet moves, int from, Boolean color, int multi, int next, int row)
/*      */   {
/*  372 */     if (invalidSquare(next))
/*  373 */       return;
/*  374 */     int capturedPiece = position.board[next];
/*  375 */     if ((capturedPiece != 0) && (((capturedPiece < 0) && (color == WHITE)) || ((capturedPiece > 0) && (color == BLACK))))
/*  376 */       if (next / (row + multi * 6) == 1)
/*      */       {
/*  378 */         moves.add(new Move(position, from, next, Piece.create(next, color, 2), capturedPiece));
/*  379 */         moves.add(new Move(position, from, next, Piece.create(next, color, 3), capturedPiece));
/*  380 */         moves.add(new Move(position, from, next, Piece.create(next, color, 4), capturedPiece));
/*  381 */         moves.add(new Move(position, from, next, Piece.create(next, color, 5), capturedPiece));
/*      */       }
/*      */       else {
/*  384 */         moves.add(new Move(position, from, next, 0, capturedPiece));
/*      */       }
/*  386 */     if (position.enPassantSquare == next) {
/*  387 */       moves.add(new Move(position, from, next, 0));
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generatePawnCaptures(Position position, SortedSet moves, int from, Boolean color) {
/*  392 */     int multi = 10;
/*  393 */     int row = 20;
/*  394 */     if (color == BLACK)
/*      */     {
/*  396 */       multi = -multi;
/*  397 */       row = 70;
/*      */     }
/*  399 */     int file = from % 10;
/*  400 */     int next = from + multi - 1;
/*  401 */     if (file != 1)
/*  402 */       generatePawnCapture(position, moves, from, color, multi, next, row);
/*  403 */     next = from + multi + 1;
/*  404 */     if (file != 8) {
/*  405 */       generatePawnCapture(position, moves, from, color, multi, next, row);
/*      */     }
/*      */   }
/*      */   
/*      */   static void generatePawnMoves(Position position, SortedSet moves, int from, Boolean color) {
/*  410 */     generatePawnNonCapture(position, moves, from, color);
/*  411 */     generatePawnCaptures(position, moves, from, color);
/*      */   }
/*      */   
/*      */   private static void generatePawnNonCapture(Position position, SortedSet moves, int from, Boolean color)
/*      */   {
/*  416 */     int multi = 10;
/*  417 */     int row = 20;
/*  418 */     if (color == BLACK)
/*      */     {
/*  420 */       multi = -multi;
/*  421 */       row = 70;
/*      */     }
/*  423 */     int next = from + multi;
/*  424 */     int p = position.board[next];
/*  425 */     if (p == 0)
/*      */     {
/*  427 */       if (next / (row + multi * 6) == 1)
/*      */       {
/*  429 */         moves.add(new Move(position, from, next, Piece.create(next, color, 2)));
/*  430 */         moves.add(new Move(position, from, next, Piece.create(next, color, 3)));
/*  431 */         moves.add(new Move(position, from, next, Piece.create(next, color, 4)));
/*  432 */         moves.add(new Move(position, from, next, Piece.create(next, color, 5)));
/*      */       }
/*      */       else {
/*  435 */         moves.add(new Move(position, from, next, 0));
/*      */       }
/*  437 */       if ((from > row) && (row + 9 > from))
/*      */       {
/*  439 */         next += multi;
/*  440 */         p = position.board[next];
/*  441 */         if (p == 0) {
/*  442 */           moves.add(new Move(position, from, next, 0));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   static void generateQueenCaptures(Position position, Set moves, int from, Boolean color) {
/*  449 */     position.generateBishopCaptures(moves, from, color);
/*  450 */     generateRookCaptures(position, moves, from, color);
/*      */   }
/*      */   
/*      */   static void generateQueenMoves(Position position, SortedSet moves, int from, Boolean color)
/*      */   {
/*  455 */     generateBishopMoves(position, moves, from, color);
/*  456 */     generateRookMoves(position, moves, from, color);
/*      */   }
/*      */   
/*      */   public static void generateRookCaptures(Position position, Set moves, int from, Boolean color)
/*      */   {
/*  461 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  463 */       int next = from + i * 10;
/*  464 */       if (next > 88)
/*      */         break;
/*  466 */       boolean finished = tryMoveCapture(position, moves, from, next, color);
/*  467 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  471 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  473 */       int next = from + i * -10;
/*  474 */       if (next < 0)
/*      */         break;
/*  476 */       boolean finished = tryMoveCapture(position, moves, from, next, color);
/*  477 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  481 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  483 */       int next = from + i * 1;
/*  484 */       boolean finished = tryMoveCapture(position, moves, from, next, color);
/*  485 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  489 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  491 */       int next = from + i * -1;
/*  492 */       boolean finished = tryMoveCapture(position, moves, from, next, color);
/*  493 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void generateRookMoves(Position position, SortedSet moves, int from, Boolean color)
/*      */   {
/*  501 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  503 */       int next = from + i * 10;
/*  504 */       boolean finished = tryMove(position, moves, from, next, color);
/*  505 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  509 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  511 */       int next = from + i * -10;
/*  512 */       boolean finished = tryMove(position, moves, from, next, color);
/*  513 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  517 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  519 */       int next = from + i * 1;
/*  520 */       boolean finished = tryMove(position, moves, from, next, color);
/*  521 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*  525 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  527 */       int next = from + i * -1;
/*  528 */       boolean finished = tryMove(position, moves, from, next, color);
/*  529 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static int getBishopValue(int square)
/*      */   {
/*  537 */     return bishopSquareValues[(square - 10)];
/*      */   }
/*      */   
/*      */   public static Position getEmpty()
/*      */   {
/*  542 */     return new Position();
/*      */   }
/*      */   
/*      */   public static int getKnightPcSqValue(int square)
/*      */   {
/*  547 */     return knightSquareValues[(square - 10)];
/*      */   }
/*      */   
/*      */   static boolean invalidSquare(int next)
/*      */   {
/*  552 */     boolean isInvalid = false;
/*  553 */     if ((next < 11) || (next > 88))
/*  554 */       isInvalid = true;
/*  555 */     int mod = next % 10;
/*  556 */     if ((mod == 0) || (mod == 9))
/*  557 */       isInvalid = true;
/*  558 */     return isInvalid;
/*      */   }
/*      */   
/*      */   private static boolean tryMove(Position position, SortedSet moves, int from, int next, Boolean color)
/*      */   {
/*  563 */     if (invalidSquare(next))
/*  564 */       return true;
/*  565 */     int type = position.typeOn(next);
/*  566 */     if (type == 0)
/*      */     {
/*  568 */       moves.add(new Move(position, from, next));
/*  569 */       return false;
/*      */     }
/*  571 */     if ((type < 0) && (color.equals(BLACK)))
/*  572 */       return true;
/*  573 */     if ((type > 0) && (color.equals(WHITE)))
/*      */     {
/*  575 */       return true;
/*      */     }
/*      */     
/*  578 */     Move m = new Move(position, from, next, 0, type);
/*  579 */     moves.add(m);
/*  580 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private static boolean tryMoveCapture(Position position, Set moves, int from, int next, Boolean color)
/*      */   {
/*  586 */     if (invalidSquare(next))
/*  587 */       return true;
/*  588 */     int type = position.board[next];
/*  589 */     if (type == 0)
/*  590 */       return false;
/*  591 */     if ((type < 0) && (color.equals(BLACK)))
/*  592 */       return true;
/*  593 */     if ((type > 0) && (color.equals(WHITE)))
/*      */     {
/*  595 */       return true;
/*      */     }
/*      */     
/*  598 */     Move m = new Move(position, from, next, 0, type);
/*  599 */     moves.add(m);
/*  600 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public Position()
/*      */   {
/*  606 */     this.bishopCaptureCount = 0;
/*  607 */     this.bishopNonCaptureCount = 0;
/*  608 */     this.blackKing = -1;
/*  609 */     this.blackPieces = new TreeSet();
/*  610 */     this.captureMoves = null;
/*  611 */     this.castleLongBlack = true;
/*  612 */     this.castleLongWhite = true;
/*  613 */     this.castleShortBlack = true;
/*  614 */     this.castleShortWhite = true;
/*  615 */     this.enPassantSquare = 0;
/*  616 */     this.hasCastledBlack = false;
/*  617 */     this.hasCastledWhite = false;
/*  618 */     this.isGivingCheck = null;
/*  619 */     this.isReceivingCheck = null;
/*  620 */     this.isStartPosition = false;
/*  621 */     this.moveNr = 0;
/*  622 */     this.onMove = WHITE;
/*  623 */     this.whiteKing = -1;
/*  624 */     this.whitePieces = new TreeSet();
/*  625 */     Info.nodes += 1L;
/*  626 */     this.board = new int[89];
/*  627 */     this.whitePieces.clear();
/*  628 */     this.whiteKing = -1;
/*  629 */     this.blackPieces.clear();
/*  630 */     this.blackKing = -1;
/*  631 */     clearBoard();
/*      */   }
/*      */   
/*      */   private Position(Position position)
/*      */   {
/*  636 */     this.bishopCaptureCount = 0;
/*  637 */     this.bishopNonCaptureCount = 0;
/*  638 */     this.blackKing = -1;
/*  639 */     this.blackPieces = new TreeSet();
/*  640 */     this.captureMoves = null;
/*  641 */     this.castleLongBlack = true;
/*  642 */     this.castleLongWhite = true;
/*  643 */     this.castleShortBlack = true;
/*  644 */     this.castleShortWhite = true;
/*  645 */     this.enPassantSquare = 0;
/*  646 */     this.hasCastledBlack = false;
/*  647 */     this.hasCastledWhite = false;
/*  648 */     this.isGivingCheck = null;
/*  649 */     this.isReceivingCheck = null;
/*  650 */     this.isStartPosition = false;
/*  651 */     this.moveNr = 0;
/*  652 */     this.onMove = WHITE;
/*  653 */     this.whiteKing = -1;
/*  654 */     this.whitePieces = new TreeSet();
/*  655 */     Info.nodes += 1L;
/*  656 */     this.board = new int[89];
/*  657 */     copyBoard(position.board, this.board);
/*  658 */     this.whitePieces.addAll(position.whitePieces);
/*  659 */     this.whiteKing = position.whiteKing;
/*  660 */     this.blackPieces.addAll(position.blackPieces);
/*  661 */     this.blackKing = position.blackKing;
/*  662 */     this.isStartPosition = position.isStartPosition();
/*  663 */     this.onMove = position.onMove();
/*  664 */     this.castleLongBlack = position.getCastleLongBlack();
/*  665 */     this.castleLongWhite = position.getCastleLongWhite();
/*  666 */     this.castleShortBlack = position.getCastleShortBlack();
/*  667 */     this.castleShortWhite = position.getCastleShortWhite();
/*  668 */     this.enPassantSquare = position.enPassant();
/*  669 */     this.hasCastledBlack = position.hasCastledBlack();
/*  670 */     this.hasCastledWhite = position.hasCastledWhite();
/*  671 */     this.isGivingCheck = null;
/*  672 */     this.isReceivingCheck = null;
/*  673 */     this.zobrist = position.zobrist;
/*  674 */     this.pawnZobrist = position.pawnZobrist;
/*  675 */     this.pzCache = position.pzCache;
/*  676 */     this.isEndGame = position.isEndGame;
/*      */   }
/*      */   
/*      */   private void addPiece(Piece piece, String square)
/*      */   {
/*  681 */     piece.addPiece(this, square);
/*      */   }
/*      */   
/*      */   private void addStartPieces()
/*      */   {
/*  686 */     addPiece(Piece.create(new Square("a2"), WHITE, 1), "a2");
/*  687 */     addPiece(Piece.create(new Square("b2"), WHITE, 1), "b2");
/*  688 */     addPiece(Piece.create(new Square("c2"), WHITE, 1), "c2");
/*  689 */     addPiece(Piece.create(new Square("d2"), WHITE, 1), "d2");
/*  690 */     addPiece(Piece.create(new Square("e2"), WHITE, 1), "e2");
/*  691 */     addPiece(Piece.create(new Square("f2"), WHITE, 1), "f2");
/*  692 */     addPiece(Piece.create(new Square("g2"), WHITE, 1), "g2");
/*  693 */     addPiece(Piece.create(new Square("h2"), WHITE, 1), "h2");
/*  694 */     addPiece(Piece.create(new Square("a1"), WHITE, 4), "a1");
/*  695 */     addPiece(Piece.create(new Square("b1"), WHITE, 2), "b1");
/*  696 */     addPiece(Piece.create(new Square("c1"), WHITE, 3), "c1");
/*  697 */     addPiece(Piece.create(new Square("d1"), WHITE, 5), "d1");
/*  698 */     addPiece(Piece.create(new Square("e1"), WHITE, 6), "e1");
/*  699 */     this.whiteKing = 15;
/*  700 */     addPiece(Piece.create(new Square("f1"), WHITE, 3), "f1");
/*  701 */     addPiece(Piece.create(new Square("g1"), WHITE, 2), "g1");
/*  702 */     addPiece(Piece.create(new Square("h1"), WHITE, 4), "h1");
/*  703 */     addPiece(Piece.create(new Square("a7"), BLACK, 1), "a7");
/*  704 */     addPiece(Piece.create(new Square("b7"), BLACK, 1), "b7");
/*  705 */     addPiece(Piece.create(new Square("c7"), BLACK, 1), "c7");
/*  706 */     addPiece(Piece.create(new Square("d7"), BLACK, 1), "d7");
/*  707 */     addPiece(Piece.create(new Square("e7"), BLACK, 1), "e7");
/*  708 */     addPiece(Piece.create(new Square("f7"), BLACK, 1), "f7");
/*  709 */     addPiece(Piece.create(new Square("g7"), BLACK, 1), "g7");
/*  710 */     addPiece(Piece.create(new Square("h7"), BLACK, 1), "h7");
/*  711 */     addPiece(Piece.create(new Square("a8"), BLACK, 4), "a8");
/*  712 */     addPiece(Piece.create(new Square("b8"), BLACK, 2), "b8");
/*  713 */     addPiece(Piece.create(new Square("c8"), BLACK, 3), "c8");
/*  714 */     addPiece(Piece.create(new Square("d8"), BLACK, 5), "d8");
/*  715 */     addPiece(Piece.create(new Square("e8"), BLACK, 6), "e8");
/*  716 */     this.blackKing = 85;
/*  717 */     addPiece(Piece.create(new Square("f8"), BLACK, 3), "f8");
/*  718 */     addPiece(Piece.create(new Square("g8"), BLACK, 2), "g8");
/*  719 */     addPiece(Piece.create(new Square("h8"), BLACK, 4), "h8");
/*      */   }
/*      */   
/*      */ 
/*      */   private void addToCheckHash(Integer integer) {}
/*      */   
/*      */ 
/*      */   public Move bestMove()
/*      */   {
/*  728 */     return this.bestMove;
/*      */   }
/*      */   
/*      */   public int bestValue()
/*      */   {
/*  733 */     return this.bestValue;
/*      */   }
/*      */   
/*      */   private void clearBoard()
/*      */   {
/*  738 */     for (int i = 0; i < 89; i++) {
/*  739 */       this.board[i] = 0;
/*      */     }
/*      */   }
/*      */   
/*      */   public void clearEnPassantCapture(Move move, int[] board)
/*      */   {
/*      */     long zobi;
/*  746 */     if (move.to > move.from)
/*      */     {
/*  748 */       long zobi = kleinerZobristEntfernen(move.to - 10);
/*  749 */       clearSquare(move.to, -10, board);
/*  750 */       this.blackPieces.remove(new Integer(move.to - 10));
/*      */     }
/*      */     else {
/*  753 */       zobi = kleinerZobristEntfernen(move.to + 10);
/*  754 */       clearSquare(move.to, 10, board);
/*  755 */       this.whitePieces.remove(new Integer(move.to + 10));
/*      */     }
/*  757 */     this.zobrist ^= zobi;
/*      */   }
/*      */   
/*      */   public int enPassant()
/*      */   {
/*  762 */     return this.enPassantSquare;
/*      */   }
/*      */   
/*      */   boolean enPrise(Move prospectiveMove)
/*      */   {
/*  767 */     int next = prospectiveMove.to;
/*  768 */     Position dummy = createTestPosition(this, prospectiveMove);
/*  769 */     Info.enPriseNodes += 1;
/*  770 */     return !dummy.isAttacked(next, onMove());
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj)
/*      */   {
/*  775 */     if ((obj instanceof Position))
/*      */     {
/*  777 */       Position position = (Position)obj;
/*  778 */       if (!this.board.equals(position.board))
/*  779 */         return false;
/*  780 */       if (this.isStartPosition != position.isStartPosition())
/*  781 */         return false;
/*  782 */       if (!this.onMove.equals(position.onMove()))
/*  783 */         return false;
/*  784 */       if (this.castleLongBlack != position.getCastleLongBlack())
/*  785 */         return false;
/*  786 */       if (this.castleLongWhite != position.getCastleLongWhite())
/*  787 */         return false;
/*  788 */       if (this.castleShortBlack != position.getCastleShortBlack())
/*  789 */         return false;
/*  790 */       if (this.castleShortWhite != position.getCastleShortWhite())
/*  791 */         return false;
/*  792 */       if (this.enPassantSquare != position.enPassant())
/*  793 */         return false;
/*  794 */       if (this.hasCastledBlack != position.hasCastledBlack())
/*  795 */         return false;
/*  796 */       if (this.hasCastledWhite != position.hasCastledWhite())
/*      */       {
/*  798 */         return false;
/*      */       }
/*      */       
/*  801 */       this.isGivingCheck = null;
/*  802 */       this.isReceivingCheck = null;
/*  803 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  807 */     return super.equals(obj);
/*      */   }
/*      */   
/*      */ 
/*      */   private int evaluatePawnStructureMidgame()
/*      */   {
/*  813 */     Info.pawnStructureProbes += 1;
/*  814 */     Long pz = getPawnZobrist();
/*  815 */     if (pawnHash.containsKey(pz))
/*      */     {
/*  817 */       Integer value = (Integer)pawnHash.get(pz);
/*  818 */       Info.pawnStructureHits += 1;
/*  819 */       return value.intValue();
/*      */     }
/*  821 */     Iterator whiteIt = this.whitePieces.iterator();
/*  822 */     int whiteCValue = 0;
/*  823 */     while (whiteIt.hasNext())
/*      */     {
/*  825 */       int whiteSquare = ((Integer)whiteIt.next()).intValue();
/*  826 */       int whitePiece = this.board[whiteSquare];
/*  827 */       if (whitePiece == 1)
/*      */       {
/*  829 */         whiteCValue += pieceValues[0];
/*  830 */         int file = whiteSquare % 10;
/*  831 */         whiteCValue += getPawnMidgameValue(whiteSquare, whitePiece, file);
/*      */       }
/*      */     }
/*  834 */     Iterator blackIt = this.blackPieces.iterator();
/*  835 */     int blackCValue = 0;
/*  836 */     while (blackIt.hasNext())
/*      */     {
/*  838 */       int blackSquare = ((Integer)blackIt.next()).intValue();
/*  839 */       int blackPiece = this.board[blackSquare];
/*  840 */       if (blackPiece == -1)
/*      */       {
/*  842 */         blackCValue += pieceValues[0];
/*  843 */         int file = blackSquare % 10;
/*  844 */         blackCValue += getPawnMidgameValue(blackSquare, blackPiece, file);
/*      */       }
/*      */     }
/*  847 */     int retVal = whiteCValue - blackCValue;
/*  848 */     Info.phSize = pawnHash.size();
/*  849 */     if (Info.phSize < 256000)
/*  850 */       pawnHash.put(pz, new Integer(retVal));
/*  851 */     return retVal;
/*      */   }
/*      */   
/*      */   private int evaluatePawnStructureEndgame()
/*      */   {
/*  856 */     Info.pawnStructureProbes += 1;
/*  857 */     Long pz = getPawnZobrist();
/*  858 */     if (pawnHash.containsKey(pz))
/*      */     {
/*  860 */       Integer value = (Integer)pawnHash.get(pz);
/*  861 */       Info.pawnStructureHits += 1;
/*  862 */       return value.intValue();
/*      */     }
/*  864 */     Iterator whiteIt = this.whitePieces.iterator();
/*  865 */     int whiteCValue = 0;
/*  866 */     while (whiteIt.hasNext())
/*      */     {
/*  868 */       int whiteSquare = ((Integer)whiteIt.next()).intValue();
/*  869 */       int whitePiece = this.board[whiteSquare];
/*  870 */       if (whitePiece == 1)
/*      */       {
/*  872 */         whiteCValue += pieceValues[0];
/*  873 */         int file = whiteSquare % 10;
/*  874 */         whiteCValue += getPawnEndgameValue(whiteSquare, whitePiece, file);
/*      */       }
/*      */     }
/*  877 */     Iterator blackIt = this.blackPieces.iterator();
/*  878 */     int blackCValue = 0;
/*  879 */     while (blackIt.hasNext())
/*      */     {
/*  881 */       int blackSquare = ((Integer)blackIt.next()).intValue();
/*  882 */       int blackPiece = this.board[blackSquare];
/*  883 */       if (blackPiece == -1)
/*      */       {
/*  885 */         blackCValue += pieceValues[0];
/*  886 */         int file = blackSquare % 10;
/*  887 */         blackCValue += getPawnEndgameValue(blackSquare, blackPiece, file);
/*      */       }
/*      */     }
/*  890 */     int retVal = whiteCValue - blackCValue;
/*  891 */     Info.phSize = pawnHash.size();
/*  892 */     if (Info.phSize < 256000)
/*  893 */       pawnHash.put(pz, new Integer(retVal));
/*  894 */     return retVal;
/*      */   }
/*      */   
/*      */   public void fillRank(int rank, String currentRank)
/*      */   {
/*  899 */     int rankStart = 10 * (rank + 1);
/*  900 */     int next = rankStart + 1;
/*  901 */     for (int i = 0; i < currentRank.length(); i++)
/*      */     {
/*  903 */       char currChar = currentRank.charAt(i);
/*  904 */       if (Character.isDigit(currChar))
/*      */       {
/*  906 */         int digit = Character.getNumericValue(currChar);
/*  907 */         for (int j = 0; j < digit; j++)
/*      */         {
/*  909 */           this.board[next] = 0;
/*  910 */           next++; if (next > 88) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*  915 */       else if (Character.isLetter(currChar))
/*      */       {
/*  917 */         switch (currChar)
/*      */         {
/*      */         case 'k': 
/*  920 */           this.board[next] = -6;
/*  921 */           this.blackPieces.add(new Integer(next));
/*  922 */           this.blackKing = next;
/*  923 */           break;
/*      */         
/*      */         case 'q': 
/*  926 */           this.board[next] = -5;
/*  927 */           this.blackPieces.add(new Integer(next));
/*  928 */           break;
/*      */         
/*      */         case 'r': 
/*  931 */           this.board[next] = -4;
/*  932 */           this.blackPieces.add(new Integer(next));
/*  933 */           break;
/*      */         
/*      */         case 'b': 
/*  936 */           this.board[next] = -3;
/*  937 */           this.blackPieces.add(new Integer(next));
/*  938 */           break;
/*      */         
/*      */         case 'n': 
/*  941 */           this.board[next] = -2;
/*  942 */           this.blackPieces.add(new Integer(next));
/*  943 */           break;
/*      */         
/*      */         case 'p': 
/*  946 */           this.board[next] = -1;
/*  947 */           this.blackPieces.add(new Integer(next));
/*  948 */           break;
/*      */         
/*      */         case 'K': 
/*  951 */           this.board[next] = 6;
/*  952 */           this.whitePieces.add(new Integer(next));
/*  953 */           this.whiteKing = next;
/*  954 */           break;
/*      */         
/*      */         case 'Q': 
/*  957 */           this.board[next] = 5;
/*  958 */           this.whitePieces.add(new Integer(next));
/*  959 */           break;
/*      */         
/*      */         case 'R': 
/*  962 */           this.board[next] = 4;
/*  963 */           this.whitePieces.add(new Integer(next));
/*  964 */           break;
/*      */         
/*      */         case 'B': 
/*  967 */           this.board[next] = 3;
/*  968 */           this.whitePieces.add(new Integer(next));
/*  969 */           break;
/*      */         
/*      */         case 'N': 
/*  972 */           this.board[next] = 2;
/*  973 */           this.whitePieces.add(new Integer(next));
/*  974 */           break;
/*      */         
/*      */         case 'P': 
/*  977 */           this.board[next] = 1;
/*  978 */           this.whitePieces.add(new Integer(next));
/*      */         }
/*      */         
/*  981 */         next++;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public int findKing(boolean colorWhite)
/*      */   {
/*  989 */     if (colorWhite) {
/*  990 */       return this.whiteKing;
/*      */     }
/*  992 */     return this.blackKing;
/*      */   }
/*      */   
/*      */   public void generateBishopCaptures(Set moves, int from, Boolean color)
/*      */   {
/*  997 */     for (int i = 1; i < 8; i++)
/*      */     {
/*  999 */       int next = from + i * 9;
/* 1000 */       if (next > 88)
/*      */         break;
/* 1002 */       boolean finished = tryMoveCapture(this, moves, from, next, color);
/* 1003 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1007 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1009 */       int next = from + i * -9;
/* 1010 */       if (next < 0)
/*      */         break;
/* 1012 */       boolean finished = tryMoveCapture(this, moves, from, next, color);
/* 1013 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1017 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1019 */       int next = from + i * 11;
/* 1020 */       if (next > 88)
/*      */         break;
/* 1022 */       boolean finished = tryMoveCapture(this, moves, from, next, color);
/* 1023 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1027 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1029 */       int next = from + i * -11;
/* 1030 */       if (next < 0)
/*      */         break;
/* 1032 */       boolean finished = tryMoveCapture(this, moves, from, next, color);
/* 1033 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void generateBishopNonCaptures(Set moves, int from, Boolean color)
/*      */   {
/* 1041 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1043 */       int next = from + i * 9;
/* 1044 */       if (next > 88)
/*      */         break;
/* 1046 */       boolean finished = tryMoveNonCapture(moves, from, next, color);
/* 1047 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1051 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1053 */       int next = from + i * -9;
/* 1054 */       if (next < 0)
/*      */         break;
/* 1056 */       boolean finished = tryMoveNonCapture(moves, from, next, color);
/* 1057 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1061 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1063 */       int next = from + i * 11;
/* 1064 */       if (next > 88)
/*      */         break;
/* 1066 */       boolean finished = tryMoveNonCapture(moves, from, next, color);
/* 1067 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1071 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1073 */       int next = from + i * -11;
/* 1074 */       if (next < 0)
/*      */         break;
/* 1076 */       boolean finished = tryMoveNonCapture(moves, from, next, color);
/* 1077 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public SortedSet generateCaptureMoves()
/*      */   {
/* 1085 */     SortedSet moves = new TreeSet();
/* 1086 */     for (int i = 11; i < 89; i++)
/*      */     {
/* 1088 */       int p = this.board[i];
/* 1089 */       int type = Math.abs(p);
/* 1090 */       if ((type >= 1) && (type <= 7))
/*      */       {
/* 1092 */         Boolean color = EdenBrain.convertColor(p);
/* 1093 */         if (color.equals(onMove()))
/*      */         {
/* 1095 */           SortedSet pieceMoves = generateCaptureMoves(i, p, color);
/* 1096 */           moves.addAll(pieceMoves);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1101 */     return moves;
/*      */   }
/*      */   
/*      */   SortedSet generateCaptureMoves(int i, int p, Boolean color)
/*      */   {
/* 1106 */     SortedSet moves = new TreeSet();
/* 1107 */     switch (p)
/*      */     {
/*      */     case -1: 
/*      */     case 1: 
/* 1111 */       generatePawnCaptures(this, moves, i, color);
/* 1112 */       break;
/*      */     
/*      */     case -2: 
/*      */     case 2: 
/* 1116 */       generateKnightCaptures(this, moves, i, color);
/* 1117 */       break;
/*      */     
/*      */     case -3: 
/*      */     case 3: 
/* 1121 */       generateBishopCaptures(moves, i, color);
/* 1122 */       break;
/*      */     
/*      */     case -4: 
/*      */     case 4: 
/* 1126 */       generateRookCaptures(this, moves, i, color);
/* 1127 */       break;
/*      */     
/*      */     case -5: 
/*      */     case 5: 
/* 1131 */       generateQueenCaptures(this, moves, i, color);
/* 1132 */       break;
/*      */     
/*      */     case -6: 
/*      */     case 6: 
/* 1136 */       generateKingCaptures(this, moves, i, color);
/*      */     }
/*      */     
/* 1139 */     return moves;
/*      */   }
/*      */   
/*      */   public void generateKingNonCapturesNoCastling(Set moves, int from, Boolean color)
/*      */   {
/* 1144 */     int[] kingMoves = {
/* 1145 */       9, 10, 11, -1, 1, -9, -10, -11 };
/*      */     
/* 1147 */     for (int i = 0; i < kingMoves.length; i++)
/*      */     {
/* 1149 */       int next = from + kingMoves[i];
/* 1150 */       if (!invalidSquare(next))
/*      */       {
/* 1152 */         int capturedPiece = this.board[next];
/* 1153 */         if (capturedPiece == 0) {
/* 1154 */           moves.add(new Move(this, from, next, 0, 0));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void generateKnightNonCaptures(Set moves, int from, Boolean color)
/*      */   {
/* 1162 */     int[] knightMoves = {
/* 1163 */       19, 21, 8, 12, -19, -21, -8, -12 };
/*      */     
/* 1165 */     for (int i = 0; i < knightMoves.length; i++)
/*      */     {
/* 1167 */       int next = from + knightMoves[i];
/* 1168 */       if (!invalidSquare(next))
/*      */       {
/* 1170 */         int capturedPiece = this.board[next];
/* 1171 */         if (capturedPiece == 0) {
/* 1172 */           moves.add(new Move(this, from, next));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public SortedSet generateLegalMoves()
/*      */   {
/* 1180 */     if (this.legalMoves != null)
/*      */     {
/* 1182 */       return this.legalMoves;
/*      */     }
/*      */     
/* 1185 */     SortedSet moves = generateMoves();
/* 1186 */     removeIllegalMoves(moves);
/* 1187 */     this.legalMoves = moves;
/* 1188 */     return moves;
/*      */   }
/*      */   
/*      */ 
/*      */   public SortedSet generateMoves()
/*      */   {
/* 1194 */     SortedSet moves = new TreeSet();
/* 1195 */     for (int i = 11; i < 89; i++)
/*      */     {
/* 1197 */       int p = this.board[i];
/* 1198 */       int type = Math.abs(p);
/* 1199 */       if ((type >= 1) && (type <= 7))
/*      */       {
/* 1201 */         Boolean color = EdenBrain.convertColor(p);
/* 1202 */         if (color.equals(onMove())) {
/* 1203 */           generateMoves(i, p, color, moves);
/*      */         }
/*      */       }
/*      */     }
/* 1207 */     return moves;
/*      */   }
/*      */   
/*      */   void generateMoves(int i, int p, Boolean color, SortedSet moves)
/*      */   {
/* 1212 */     switch (p)
/*      */     {
/*      */     case -1: 
/*      */     case 1: 
/* 1216 */       generatePawnMoves(this, moves, i, color);
/* 1217 */       break;
/*      */     
/*      */     case -2: 
/*      */     case 2: 
/* 1221 */       generateKnightMoves(this, moves, i, color);
/* 1222 */       break;
/*      */     
/*      */     case -3: 
/*      */     case 3: 
/* 1226 */       generateBishopMoves(this, moves, i, color);
/* 1227 */       break;
/*      */     
/*      */     case -4: 
/*      */     case 4: 
/* 1231 */       generateRookMoves(this, moves, i, color);
/* 1232 */       break;
/*      */     
/*      */     case -5: 
/*      */     case 5: 
/* 1236 */       generateQueenMoves(this, moves, i, color);
/* 1237 */       break;
/*      */     
/*      */     case -6: 
/*      */     case 6: 
/* 1241 */       generateKingMoves(this, moves, i, color);
/*      */     }
/*      */     
/*      */   }
/*      */   
/*      */   public Move generateNextBishopCapture(int from, Boolean color)
/*      */   {
/* 1248 */     SortedSet moves = new TreeSet();
/* 1249 */     if (this.bishopCaptureCount == 4)
/* 1250 */       return null;
/* 1251 */     if (this.bishopCaptureCount == 0)
/*      */     {
/* 1253 */       for (int i = 1; i < 8; i++)
/*      */       {
/* 1255 */         int next = from + i * 9;
/* 1256 */         if (next > 88)
/*      */           break;
/* 1258 */         boolean finished = tryMoveCapture(this, moves, from, next, color);
/* 1259 */         if (finished) {
/*      */           break;
/*      */         }
/*      */       }
/* 1263 */       this.bishopCaptureCount = 1;
/* 1264 */       if (moves.size() == 1)
/*      */       {
/* 1266 */         Move retVal = (Move)moves.first();
/* 1267 */         return retVal;
/*      */       }
/*      */     }
/* 1270 */     if (this.bishopCaptureCount == 1)
/*      */     {
/* 1272 */       for (int i = 1; i < 8; i++)
/*      */       {
/* 1274 */         int next = from + i * -9;
/* 1275 */         if (next < 0)
/*      */           break;
/* 1277 */         boolean finished = tryMoveCapture(this, moves, from, next, color);
/* 1278 */         if (finished) {
/*      */           break;
/*      */         }
/*      */       }
/* 1282 */       this.bishopCaptureCount = 2;
/* 1283 */       if (moves.size() == 1)
/*      */       {
/* 1285 */         Move retVal = (Move)moves.first();
/* 1286 */         return retVal;
/*      */       }
/*      */     }
/* 1289 */     if (this.bishopCaptureCount == 2)
/*      */     {
/* 1291 */       for (int i = 1; i < 8; i++)
/*      */       {
/* 1293 */         int next = from + i * 11;
/* 1294 */         if (next > 88)
/*      */           break;
/* 1296 */         boolean finished = tryMoveCapture(this, moves, from, next, color);
/* 1297 */         if (finished) {
/*      */           break;
/*      */         }
/*      */       }
/* 1301 */       this.bishopCaptureCount = 3;
/* 1302 */       if (moves.size() == 1)
/*      */       {
/* 1304 */         Move retVal = (Move)moves.first();
/* 1305 */         return retVal;
/*      */       }
/*      */     }
/* 1308 */     if (this.bishopCaptureCount == 3)
/*      */     {
/* 1310 */       for (int i = 1; i < 8; i++)
/*      */       {
/* 1312 */         int next = from + i * -11;
/* 1313 */         if (next < 0)
/*      */           break;
/* 1315 */         boolean finished = tryMoveCapture(this, moves, from, next, color);
/* 1316 */         if (finished) {
/*      */           break;
/*      */         }
/*      */       }
/* 1320 */       this.bishopCaptureCount = 4;
/* 1321 */       if (moves.size() == 1)
/*      */       {
/* 1323 */         Move retVal = (Move)moves.first();
/* 1324 */         return retVal;
/*      */       }
/*      */     }
/* 1327 */     return null;
/*      */   }
/*      */   
/*      */   Move generateNextCaptureMove(int i, int p, Boolean color)
/*      */   {
/* 1332 */     SortedSet moves = new TreeSet();
/* 1333 */     switch (p)
/*      */     {
/*      */     case -1: 
/*      */     case 1: 
/* 1337 */       return generateNextPawnCapture(moves, i, color);
/*      */     
/*      */     case -2: 
/*      */     case 2: 
/* 1341 */       return generateNextKnightCapture(i, color, moves);
/*      */     
/*      */     case -3: 
/*      */     case 3: 
/* 1345 */       return generateNextBishopCapture(i, color);
/*      */     
/*      */     case -4: 
/*      */     case 4: 
/* 1349 */       return generateNextRookCapture(i, color, moves);
/*      */     
/*      */     case -5: 
/*      */     case 5: 
/* 1353 */       return generateNextQueenCapture(i, color, moves);
/*      */     
/*      */     case -6: 
/*      */     case 6: 
/* 1357 */       return generateNextKingCapture(i, color, moves);
/*      */     }
/*      */     
/*      */     
/* 1361 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   private Move generateNextKingCapture(int i, Boolean color, SortedSet moves)
/*      */   {
/* 1367 */     if (this.kingCaptures == null)
/*      */     {
/* 1369 */       this.kingCaptures = new TreeSet();
/* 1370 */       generateKingCaptures(this, moves, i, color);
/* 1371 */       this.kingCaptures = moves;
/*      */     }
/* 1373 */     if (this.kingCaptures.size() == 0)
/*      */     {
/* 1375 */       return null;
/*      */     }
/*      */     
/* 1378 */     Move retVal = (Move)this.kingCaptures.first();
/* 1379 */     this.kingCaptures.remove(retVal);
/* 1380 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */   private Move generateNextKnightCapture(int i, Boolean color, SortedSet moves)
/*      */   {
/* 1386 */     if (this.knightCaptures == null)
/*      */     {
/* 1388 */       this.knightCaptures = new TreeSet();
/* 1389 */       generateKnightCaptures(this, moves, i, color);
/* 1390 */       this.knightCaptures = moves;
/*      */     }
/* 1392 */     if (this.knightCaptures.size() == 0)
/*      */     {
/* 1394 */       return null;
/*      */     }
/*      */     
/* 1397 */     Move retVal = (Move)this.knightCaptures.first();
/* 1398 */     this.knightCaptures.remove(retVal);
/* 1399 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */   Move generateNextNonCaptureMove(int i, int p, Boolean color)
/*      */   {
/* 1405 */     SortedSet moves = new TreeSet();
/* 1406 */     switch (p)
/*      */     {
/*      */     case -1: 
/*      */     case 1: 
/* 1410 */       return generateNextPawnNonCapture(moves, i, color);
/*      */     
/*      */     case -2: 
/*      */     case 2: 
/* 1414 */       return null;
/*      */     
/*      */     case -3: 
/*      */     case 3: 
/* 1418 */       return null;
/*      */     
/*      */     case -4: 
/*      */     case 4: 
/* 1422 */       return null;
/*      */     
/*      */     case -5: 
/*      */     case 5: 
/* 1426 */       return null;
/*      */     
/*      */     case -6: 
/*      */     case 6: 
/* 1430 */       return null;
/*      */     }
/*      */     
/*      */     
/* 1434 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Move generateNextPawnCapture(SortedSet moves, int from, Boolean color)
/*      */   {
/* 1440 */     if (this.lastPawnCapture < 0)
/*      */     {
/* 1442 */       if (this.lastPawnCapture == -from)
/* 1443 */         return null;
/* 1444 */       this.lastPawnCapture = 0;
/*      */     }
/* 1446 */     int multi = 10;
/* 1447 */     int row = 20;
/* 1448 */     if (color == BLACK)
/*      */     {
/* 1450 */       multi = -multi;
/* 1451 */       row = 70;
/*      */     }
/* 1453 */     int file = from % 10;
/*      */     
/* 1455 */     if (this.lastPawnCapture == 0)
/*      */     {
/* 1457 */       int next = from + multi - 1;
/* 1458 */       if (file != 1)
/* 1459 */         generatePawnCapture(this, moves, from, color, multi, next, row);
/* 1460 */       if (!moves.isEmpty())
/*      */       {
/* 1462 */         this.lastPawnCapture = from;
/* 1463 */         return (Move)moves.first();
/*      */       }
/*      */     }
/* 1466 */     int next = from + multi + 1;
/* 1467 */     if (file != 8)
/* 1468 */       generatePawnCapture(this, moves, from, color, multi, next, row);
/* 1469 */     this.lastPawnCapture = (-from);
/* 1470 */     if (!moves.isEmpty()) {
/* 1471 */       return (Move)moves.first();
/*      */     }
/* 1473 */     return null;
/*      */   }
/*      */   
/*      */   public Move generateNextPawnNonCapture(SortedSet moves, int from, Boolean color)
/*      */   {
/* 1478 */     if (this.lastPawnNonCapture == 2)
/* 1479 */       return null;
/* 1480 */     if (this.lastPawnNonCapture == 0)
/*      */     {
/* 1482 */       Move m = generatePawnNonCapture1(from, color);
/* 1483 */       if (m != null)
/*      */       {
/* 1485 */         this.lastPawnNonCapture = 1;
/* 1486 */         return m;
/*      */       }
/*      */     }
/* 1489 */     if (this.lastPawnNonCapture == 1)
/*      */     {
/* 1491 */       Move m = generatePawnNonCapture2(from, color);
/* 1492 */       if (m != null)
/*      */       {
/* 1494 */         this.lastPawnNonCapture = 2;
/* 1495 */         return m;
/*      */       }
/*      */     }
/* 1498 */     return null;
/*      */   }
/*      */   
/*      */   private Move generateNextQueenCapture(int i, Boolean color, SortedSet moves)
/*      */   {
/* 1503 */     if (this.queenCaptures == null)
/*      */     {
/* 1505 */       this.queenCaptures = new TreeSet();
/* 1506 */       generateQueenCaptures(this, moves, i, color);
/* 1507 */       this.queenCaptures = moves;
/*      */     }
/* 1509 */     if (this.queenCaptures.size() == 0)
/*      */     {
/* 1511 */       return null;
/*      */     }
/*      */     
/* 1514 */     Move retVal = (Move)this.queenCaptures.first();
/* 1515 */     this.queenCaptures.remove(retVal);
/* 1516 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */   private Move generateNextRookCapture(int i, Boolean color, SortedSet moves)
/*      */   {
/* 1522 */     if (this.rookCaptures == null)
/*      */     {
/* 1524 */       this.rookCaptures = new TreeSet();
/* 1525 */       generateRookCaptures(this, moves, i, color);
/* 1526 */       this.rookCaptures = moves;
/*      */     }
/* 1528 */     if (this.rookCaptures.size() == 0)
/*      */     {
/* 1530 */       return null;
/*      */     }
/*      */     
/* 1533 */     Move retVal = (Move)this.rookCaptures.first();
/* 1534 */     this.rookCaptures.remove(retVal);
/* 1535 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */   public SortedSet generateNonCaptureMoves()
/*      */   {
/* 1541 */     SortedSet moves = new TreeSet();
/* 1542 */     for (int i = 11; i < 89; i++)
/*      */     {
/* 1544 */       int p = this.board[i];
/* 1545 */       int type = Math.abs(p);
/* 1546 */       if ((type >= 1) && (type <= 7))
/*      */       {
/* 1548 */         Boolean color = EdenBrain.convertColor(p);
/* 1549 */         if (color.equals(onMove()))
/*      */         {
/* 1551 */           SortedSet pieceMoves = generateNonCaptureMoves(i, p, color);
/* 1552 */           moves.addAll(pieceMoves);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1557 */     return moves;
/*      */   }
/*      */   
/*      */   SortedSet generateNonCaptureMoves(int square, int piece, Boolean color)
/*      */   {
/* 1562 */     SortedSet moves = new TreeSet();
/* 1563 */     switch (piece)
/*      */     {
/*      */     case -1: 
/*      */     case 1: 
/* 1567 */       generatePawnNonCaptures(this, moves, square, color);
/* 1568 */       break;
/*      */     
/*      */     case -2: 
/*      */     case 2: 
/* 1572 */       generateKnightNonCaptures(moves, square, color);
/* 1573 */       break;
/*      */     
/*      */     case -3: 
/*      */     case 3: 
/* 1577 */       generateBishopNonCaptures(moves, square, color);
/* 1578 */       break;
/*      */     
/*      */     case -4: 
/*      */     case 4: 
/* 1582 */       generateRookNonCaptures(moves, square, color);
/* 1583 */       break;
/*      */     
/*      */     case -5: 
/*      */     case 5: 
/* 1587 */       generateQueenNonCaptures(moves, square, color);
/* 1588 */       break;
/*      */     
/*      */     case -6: 
/*      */     case 6: 
/* 1592 */       generateKingNonCapturesNoCastling(moves, square, color);
/* 1593 */       generateCastling(this, moves, square);
/*      */     }
/*      */     
/* 1596 */     return moves;
/*      */   }
/*      */   
/*      */   private Move generatePawnNonCapture1(int from, Boolean color)
/*      */   {
/* 1601 */     int multi = 10;
/* 1602 */     int row = 20;
/* 1603 */     if (color == BLACK)
/*      */     {
/* 1605 */       multi = -multi;
/* 1606 */       row = 70;
/*      */     }
/* 1608 */     int next = from + multi;
/* 1609 */     int p = this.board[next];
/* 1610 */     if (p == 0)
/*      */     {
/* 1612 */       if (next / (row + multi * 6) == 1) {
/* 1613 */         return new Move(this, from, next, Piece.create(next, color, 5));
/*      */       }
/* 1615 */       return new Move(this, from, next, 0);
/*      */     }
/*      */     
/* 1618 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   private Move generatePawnNonCapture2(int from, Boolean color)
/*      */   {
/* 1624 */     int multi = 10;
/* 1625 */     int row = 20;
/* 1626 */     if (color == BLACK)
/*      */     {
/* 1628 */       multi = -multi;
/* 1629 */       row = 70;
/*      */     }
/* 1631 */     int next = from + multi;
/* 1632 */     int p = this.board[next];
/* 1633 */     if ((p == 0) && (from > row) && (row + 9 > from))
/*      */     {
/* 1635 */       next += multi;
/* 1636 */       p = this.board[next];
/* 1637 */       if (p == 0)
/* 1638 */         return new Move(this, from, next, 0);
/*      */     }
/* 1640 */     return null;
/*      */   }
/*      */   
/*      */   public void generatePawnNonCaptures(Position position, SortedSet moves, int from, Boolean color)
/*      */   {
/* 1645 */     Move step1 = generatePawnNonCapture1(from, color);
/* 1646 */     if (step1 != null)
/* 1647 */       moves.add(step1);
/* 1648 */     Move step2 = generatePawnNonCapture2(from, color);
/* 1649 */     if (step2 != null) {
/* 1650 */       moves.add(step2);
/*      */     }
/*      */   }
/*      */   
/*      */   void generateQueenNonCaptures(Set moves, int from, Boolean color) {
/* 1655 */     generateBishopNonCaptures(moves, from, color);
/* 1656 */     generateRookNonCaptures(moves, from, color);
/*      */   }
/*      */   
/*      */   public void generateRookNonCaptures(Set moves, int from, Boolean color)
/*      */   {
/* 1661 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1663 */       int next = from + i * 10;
/* 1664 */       if (next > 88)
/*      */         break;
/* 1666 */       boolean finished = tryMoveNonCapture(moves, from, next, color);
/* 1667 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1671 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1673 */       int next = from + i * -10;
/* 1674 */       if (next < 0)
/*      */         break;
/* 1676 */       boolean finished = tryMoveNonCapture(moves, from, next, color);
/* 1677 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1681 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1683 */       int next = from + i * 1;
/* 1684 */       boolean finished = tryMoveNonCapture(moves, from, next, color);
/* 1685 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/* 1689 */     for (int i = 1; i < 8; i++)
/*      */     {
/* 1691 */       int next = from + i * -1;
/* 1692 */       boolean finished = tryMoveNonCapture(moves, from, next, color);
/* 1693 */       if (finished) {
/*      */         break;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private int getBishopValue(int i, int plusminus)
/*      */   {
/* 1701 */     int retValue = bishopSquareValues[(i - 10)];
/* 1702 */     if (plusminus > 0)
/*      */     {
/* 1704 */       if (i == 16)
/*      */       {
/* 1706 */         if (this.board[25] > 0)
/* 1707 */           retValue -= 3;
/* 1708 */         if (this.board[27] > 0)
/* 1709 */           retValue--;
/*      */       }
/* 1711 */       if ((i == 34) && (this.board[24] == 1)) {
/* 1712 */         retValue -= 25;
/*      */       }
/*      */     } else {
/* 1715 */       if (i == 86)
/*      */       {
/* 1717 */         if (this.board[75] < 0)
/* 1718 */           retValue -= 3;
/* 1719 */         if (this.board[77] < 0)
/* 1720 */           retValue--;
/*      */       }
/* 1722 */       if ((i == 64) && (this.board[74] == -1))
/* 1723 */         retValue -= 25;
/*      */     }
/* 1725 */     return retValue;
/*      */   }
/*      */   
/*      */   public boolean getCastleLongBlack()
/*      */   {
/* 1730 */     return this.castleLongBlack;
/*      */   }
/*      */   
/*      */   public boolean getCastleLongWhite()
/*      */   {
/* 1735 */     return this.castleLongWhite;
/*      */   }
/*      */   
/*      */   public boolean getCastleShortBlack()
/*      */   {
/* 1740 */     return this.castleShortBlack;
/*      */   }
/*      */   
/*      */   public boolean getCastleShortWhite()
/*      */   {
/* 1745 */     return this.castleShortWhite;
/*      */   }
/*      */   
/*      */   public int getEndgameValue()
/*      */   {
/* 1750 */     if ((this.wpc.isKBNK()) || (this.bpc.isKBNK()))
/* 1751 */       adjustKingSquaresForKBNK();
/* 1752 */     int whiteValue = 0;
/* 1753 */     int blackValue = 0;
/* 1754 */     boolean possibleBishopPairWhite = false;
/* 1755 */     boolean possibleBishopPairBlack = false;
/* 1756 */     int pawnStructureValue = evaluatePawnStructureEndgame();
/* 1757 */     Iterator whiteIt = this.whitePieces.iterator();
/* 1758 */     int whiteCValue = 0;
/* 1759 */     int whiteMValue = 0;
/* 1760 */     while (whiteIt.hasNext())
/*      */     {
/* 1762 */       int whiteSquare = ((Integer)whiteIt.next()).intValue();
/* 1763 */       int whitePiece = this.board[whiteSquare];
/* 1764 */       if (whitePiece != 1)
/*      */       {
/* 1766 */         whiteMValue += getPieceMaterialValue(whitePiece);
/* 1767 */         if (whitePiece == 3)
/*      */         {
/* 1769 */           if (possibleBishopPairWhite)
/* 1770 */             whiteCValue += 50;
/* 1771 */           possibleBishopPairWhite = true;
/*      */         }
/* 1773 */         int file = whiteSquare % 10;
/* 1774 */         whiteCValue += getPieceEndgameValue(whiteSquare, whitePiece, file);
/*      */       }
/*      */     }
/* 1777 */     Iterator blackIt = this.blackPieces.iterator();
/* 1778 */     int blackCValue = 0;
/* 1779 */     int blackMValue = 0;
/* 1780 */     while (blackIt.hasNext())
/*      */     {
/* 1782 */       int blackSquare = ((Integer)blackIt.next()).intValue();
/* 1783 */       int blackPiece = this.board[blackSquare];
/* 1784 */       if (blackPiece != -1)
/*      */       {
/* 1786 */         blackMValue += getPieceMaterialValue(blackPiece);
/* 1787 */         if (blackPiece == -3)
/*      */         {
/* 1789 */           if (possibleBishopPairBlack)
/* 1790 */             blackCValue += 50;
/* 1791 */           possibleBishopPairBlack = true;
/*      */         }
/* 1793 */         int file = blackSquare % 10;
/* 1794 */         blackCValue += getPieceEndgameValue(blackSquare, blackPiece, file);
/*      */       }
/*      */     }
/* 1797 */     blackValue += blackCValue;
/* 1798 */     whiteValue += whiteCValue;
/* 1799 */     if (whiteMValue > blackMValue + 105)
/*      */     {
/* 1801 */       int diff = whiteMValue + blackMValue - 500;
/* 1802 */       int bonus = (8000 - diff) / 100;
/* 1803 */       if (bonus > 74)
/* 1804 */         bonus = 74;
/* 1805 */       whiteValue += bonus;
/*      */     }
/* 1807 */     else if (blackMValue > whiteMValue + 105)
/*      */     {
/* 1809 */       int diff = blackMValue + whiteMValue - 500;
/* 1810 */       int bonus = (8000 - diff) / 100;
/* 1811 */       if (bonus > 74)
/* 1812 */         bonus = 74;
/* 1813 */       blackValue += bonus;
/*      */     }
/* 1815 */     int retVal = 0;
/* 1816 */     retVal += whiteValue + whiteMValue + pawnStructureValue - blackValue - blackMValue;
/* 1817 */     if (onMove() == BLACK)
/* 1818 */       retVal = -retVal;
/* 1819 */     return retVal;
/*      */   }
/*      */   
/*      */   private int getKingEndgameValue(int square, int piece)
/*      */   {
/* 1824 */     return this.kingEndgameSquareValues[(square - 10)];
/*      */   }
/*      */   
/*      */   private int getKingMidgameValue(int square, int piece, int file)
/*      */   {
/* 1829 */     int retValue = 0;
/* 1830 */     int openFiles = 0;
/* 1831 */     int halfOpenFiles = 0;
/* 1832 */     int rank = square / 10;
/* 1833 */     if (isOnOpenFile(square, file)) {
/* 1834 */       openFiles++;
/*      */     }
/* 1836 */     else if (isOnOpenOrHalfOpenFile(piece, file, rank))
/* 1837 */       halfOpenFiles++;
/* 1838 */     if (isOnOpenFile(square, file - 1)) {
/* 1839 */       openFiles++;
/*      */     }
/* 1841 */     else if (isOnOpenOrHalfOpenFile(piece, file - 1, rank))
/* 1842 */       halfOpenFiles++;
/* 1843 */     if (isOnOpenFile(square, file + 1)) {
/* 1844 */       openFiles++;
/*      */     }
/* 1846 */     else if (isOnOpenOrHalfOpenFile(piece, file + 1, rank))
/* 1847 */       halfOpenFiles++;
/* 1848 */     retValue = (int)(retValue - 30.0D * Math.pow(2.0D, openFiles));
/* 1849 */     retValue = (int)(retValue - 5.0D * Math.pow(2.0D, halfOpenFiles));
/* 1850 */     if (piece == 6)
/*      */     {
/* 1852 */       if ((square > 13) && (square < 17))
/* 1853 */         retValue -= 35;
/* 1854 */       if (square > 20)
/*      */       {
/* 1856 */         retValue -= 55;
/*      */       }
/*      */       else {
/* 1859 */         if ((square == 17) || (square == 18))
/*      */         {
/* 1861 */           if (this.board[26] != 1)
/* 1862 */             retValue -= 10;
/* 1863 */           if ((this.board[27] != 1) && (this.board[27] != 3))
/*      */           {
/* 1865 */             retValue -= 10;
/* 1866 */             if (this.board[37] != 1)
/* 1867 */               retValue -= 10;
/*      */           }
/* 1869 */           if (this.board[28] != 1)
/*      */           {
/* 1871 */             retValue -= 6;
/* 1872 */             if (this.board[38] != 1) {
/* 1873 */               retValue -= 11;
/*      */             }
/*      */           }
/* 1876 */         } else if ((square == 12) || (square == 11) || (square == 13))
/*      */         {
/* 1878 */           if (this.board[23] != 1)
/* 1879 */             retValue -= 10;
/* 1880 */           if ((this.board[22] != 1) && (this.board[22] != 3))
/*      */           {
/* 1882 */             retValue -= 10;
/* 1883 */             if (this.board[32] != 1)
/* 1884 */               retValue -= 10;
/*      */           }
/* 1886 */           if (this.board[21] != 1)
/*      */           {
/* 1888 */             retValue -= 10;
/* 1889 */             if (this.board[31] != 1)
/* 1890 */               retValue -= 10;
/*      */           }
/*      */         }
/* 1893 */         if (square == 13)
/* 1894 */           retValue -= 37;
/*      */       }
/*      */     }
/* 1897 */     if (piece == -6)
/*      */     {
/* 1899 */       if ((square > 83) && (square < 87))
/* 1900 */         retValue -= 35;
/* 1901 */       if (square < 80) {
/* 1902 */         retValue -= 55;
/*      */       }
/* 1904 */       else if ((square == 87) || (square == 88))
/*      */       {
/* 1906 */         if (this.board[76] != -1)
/* 1907 */           retValue -= 10;
/* 1908 */         if ((this.board[77] != -1) && (this.board[77] != -3))
/*      */         {
/* 1910 */           retValue -= 10;
/* 1911 */           if (this.board[67] != -1)
/* 1912 */             retValue -= 10;
/*      */         }
/* 1914 */         if (this.board[78] != -1)
/*      */         {
/* 1916 */           retValue -= 6;
/* 1917 */           if (this.board[68] != -1) {
/* 1918 */             retValue -= 11;
/*      */           }
/*      */         }
/* 1921 */       } else if ((square == 82) || (square == 81) || (square == 83))
/*      */       {
/* 1923 */         if (this.board[73] != -1)
/* 1924 */           retValue -= 10;
/* 1925 */         if ((this.board[72] != -1) || (this.board[72] != -3))
/*      */         {
/* 1927 */           retValue -= 10;
/* 1928 */           if (this.board[62] != -1)
/* 1929 */             retValue -= 10;
/*      */         }
/* 1931 */         if (this.board[71] != -1)
/*      */         {
/* 1933 */           retValue -= 10;
/* 1934 */           if (this.board[61] != -1)
/* 1935 */             retValue -= 10;
/*      */         }
/* 1937 */         if (square == 83)
/* 1938 */           retValue -= 37;
/*      */       }
/*      */     }
/* 1941 */     return retValue;
/*      */   }
/*      */   
/*      */   private boolean isOnOpenOrHalfOpenFile(int plusminus, int file, int rank)
/*      */   {
/* 1946 */     if ((file < 1) || (file > 8))
/* 1947 */       return false;
/* 1948 */     if (hasNoEnemyPawns(plusminus, file))
/* 1949 */       return true;
/* 1950 */     return hasNoEnemyPawns(-plusminus, file);
/*      */   }
/*      */   
/*      */   public int getMidgameValue()
/*      */   {
/* 1955 */     int whiteValue = 0;
/* 1956 */     int blackValue = 0;
/* 1957 */     boolean possibleBishopPairWhite = false;
/* 1958 */     boolean possibleBishopPairBlack = false;
/* 1959 */     int pawnStructureValue = evaluatePawnStructureMidgame();
/* 1960 */     Iterator whiteIt = this.whitePieces.iterator();
/* 1961 */     int whiteCValue = 0;
/* 1962 */     int whiteMValue = 0;
/* 1963 */     while (whiteIt.hasNext())
/*      */     {
/* 1965 */       int whiteSquare = ((Integer)whiteIt.next()).intValue();
/* 1966 */       int whitePiece = this.board[whiteSquare];
/* 1967 */       if (whitePiece != 1)
/*      */       {
/* 1969 */         whiteMValue += getPieceMaterialValue(whitePiece);
/* 1970 */         if (whitePiece == 3)
/*      */         {
/* 1972 */           if (possibleBishopPairWhite)
/* 1973 */             whiteCValue += 50;
/* 1974 */           possibleBishopPairWhite = true;
/*      */         }
/* 1976 */         int file = whiteSquare % 10;
/* 1977 */         whiteCValue += getPieceMidgameValue(whiteSquare, whitePiece, file);
/*      */       }
/*      */     }
/* 1980 */     Iterator blackIt = this.blackPieces.iterator();
/* 1981 */     int blackCValue = 0;
/* 1982 */     int blackMValue = 0;
/* 1983 */     while (blackIt.hasNext())
/*      */     {
/* 1985 */       int blackSquare = ((Integer)blackIt.next()).intValue();
/* 1986 */       int blackPiece = this.board[blackSquare];
/* 1987 */       if (blackPiece != -1)
/*      */       {
/* 1989 */         blackMValue += getPieceMaterialValue(blackPiece);
/* 1990 */         if (blackPiece == -3)
/*      */         {
/* 1992 */           if (possibleBishopPairBlack)
/* 1993 */             blackCValue += 50;
/* 1994 */           possibleBishopPairBlack = true;
/*      */         }
/* 1996 */         int file = blackSquare % 10;
/* 1997 */         blackCValue += getPieceMidgameValue(blackSquare, blackPiece, file);
/*      */       }
/*      */     }
/* 2000 */     blackValue += blackCValue;
/* 2001 */     whiteValue += whiteCValue;
/* 2002 */     if (!this.hasCastledBlack)
/* 2003 */       blackValue -= 61;
/* 2004 */     if (!this.hasCastledWhite)
/* 2005 */       whiteValue -= 61;
/* 2006 */     if (whiteMValue > blackMValue + 105)
/*      */     {
/* 2008 */       int diff = whiteMValue + blackMValue - 500;
/* 2009 */       int bonus = (8000 - diff) / 100;
/* 2010 */       if (bonus > 74)
/* 2011 */         bonus = 74;
/* 2012 */       whiteValue += bonus;
/*      */     }
/* 2014 */     else if (blackMValue > whiteMValue + 105)
/*      */     {
/* 2016 */       int diff = blackMValue + whiteMValue - 500;
/* 2017 */       int bonus = (8000 - diff) / 100;
/* 2018 */       if (bonus > 74)
/* 2019 */         bonus = 74;
/* 2020 */       blackValue += bonus;
/*      */     }
/* 2022 */     int retVal = 0;
/* 2023 */     retVal += whiteValue + whiteMValue + pawnStructureValue - blackValue - blackMValue;
/* 2024 */     if (onMove() == BLACK)
/* 2025 */       retVal = -retVal;
/* 2026 */     return retVal;
/*      */   }
/*      */   
/*      */   private int getPawnEndgameValue(int i, int plusminus, int file)
/*      */   {
/* 2031 */     int retValue = 0;
/* 2032 */     if (plusminus > 0) {
/* 2033 */       retValue = whitePawnSquareValues[(79 - i)] / 2;
/*      */     } else
/* 2035 */       retValue = blackPawnSquareValues[(i - 20)] / 2;
/* 2036 */     int rank = i / 10;
/* 2037 */     if (isPassed(i, plusminus, file, rank))
/* 2038 */       if (plusminus > 0) {
/* 2039 */         retValue = (int)(retValue + 1.5D * this.passedPawnProgression[(rank - 2)]);
/*      */       } else
/* 2041 */         retValue = (int)(retValue + 1.5D * this.passedPawnProgression[(7 - rank)]);
/* 2042 */     if (isIsolated(i, file))
/* 2043 */       retValue -= 15;
/* 2044 */     if (isDoubled(i, file))
/* 2045 */       retValue -= 18;
/* 2046 */     return retValue;
/*      */   }
/*      */   
/*      */   private int getPawnMidgameValue(int i, int plusminus, int file) {
/*      */     int retValue;
/*      */     int retValue;
/* 2052 */     if (plusminus > 0) {
/* 2053 */       retValue = whitePawnSquareValues[(79 - i)];
/*      */     } else
/* 2055 */       retValue = blackPawnSquareValues[(i - 20)];
/* 2056 */     int rank = i / 10;
/* 2057 */     if (isPassed(i, plusminus, file, rank))
/*      */     {
/* 2059 */       int passedValue = 0;
/* 2060 */       if (plusminus > 0) {
/* 2061 */         passedValue += this.passedPawnProgression[(rank - 2)];
/*      */       } else
/* 2063 */         passedValue += this.passedPawnProgression[(7 - rank)];
/* 2064 */       retValue += passedValue;
/*      */     }
/* 2066 */     if (isIsolated(i, file))
/* 2067 */       retValue -= 12;
/* 2068 */     if (isDoubled(i, file))
/* 2069 */       retValue -= 16;
/* 2070 */     return retValue;
/*      */   }
/*      */   
/*      */   private Long getPawnZobrist()
/*      */   {
/* 2075 */     if (this.pawnZobrist != null)
/* 2076 */       return this.pawnZobrist;
/* 2077 */     long grosserZobrist = 0L;
/* 2078 */     for (Iterator wIt = this.whitePieces.iterator(); wIt.hasNext();)
/*      */     {
/* 2080 */       int square = ((Integer)wIt.next()).intValue();
/* 2081 */       int figur = this.board[square];
/* 2082 */       if (figur == 1)
/*      */       {
/* 2084 */         long kleinerZobrist = kleinerZobristSetzen(figur, square);
/* 2085 */         grosserZobrist ^= kleinerZobrist;
/*      */       }
/*      */     }
/*      */     
/* 2089 */     for (Iterator bIt = this.blackPieces.iterator(); bIt.hasNext();)
/*      */     {
/* 2091 */       int square = ((Integer)bIt.next()).intValue();
/* 2092 */       int figur = this.board[square];
/* 2093 */       if (figur == -1)
/*      */       {
/* 2095 */         long kleinerZobrist = kleinerZobristSetzen(figur, square);
/* 2096 */         grosserZobrist ^= kleinerZobrist;
/*      */       }
/*      */     }
/*      */     
/* 2100 */     Long pz = new Long(grosserZobrist);
/* 2101 */     this.pawnZobrist = pz;
/* 2102 */     return pz;
/*      */   }
/*      */   
/*      */   public int getPieceEndgameValue(int square, int piece, int file)
/*      */   {
/* 2107 */     int retValue = 0;
/* 2108 */     switch (piece)
/*      */     {
/*      */     case -6: 
/*      */     case 6: 
/* 2112 */       retValue = getKingEndgameValue(square, piece);
/* 2113 */       break;
/*      */     
/*      */     case -5: 
/*      */     case 5: 
/* 2117 */       retValue = -25;
/* 2118 */       break;
/*      */     
/*      */     case -4: 
/*      */     case 4: 
/* 2122 */       retValue = 25;
/* 2123 */       break;
/*      */     
/*      */     case -3: 
/*      */     case 3: 
/* 2127 */       retValue = 3;
/* 2128 */       break;
/*      */     
/*      */     case -2: 
/*      */     case 2: 
/* 2132 */       retValue = -3;
/* 2133 */       break;
/*      */     
/*      */     case -1: 
/*      */     case 1: 
/* 2137 */       retValue = getPawnEndgameValue(square, piece, file);
/* 2138 */       break;
/*      */     
/*      */     case 0: 
/*      */     default: 
/* 2142 */       retValue = 0;
/*      */     }
/*      */     
/* 2145 */     return retValue;
/*      */   }
/*      */   
/*      */   public int getPieceMaterialValue(int piece)
/*      */   {
/* 2150 */     if (piece == 0)
/*      */     {
/* 2152 */       System.out.println("moveStack: " + EdenBrain.moveStack);
/* 2153 */       throw new IllegalStateException("piece==0");
/*      */     }
/*      */     
/* 2156 */     int retVal = pieceValues[(Math.abs(piece) - 1)];
/* 2157 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getPieceMidgameValue(int square, int piece, int file)
/*      */   {
/* 2163 */     int retValue = 0;
/* 2164 */     switch (piece)
/*      */     {
/*      */     case -6: 
/*      */     case 6: 
/* 2168 */       retValue = getKingMidgameValue(square, piece, file);
/* 2169 */       break;
/*      */     
/*      */     case -5: 
/*      */     case 5: 
/* 2173 */       retValue = getQueenValue(piece, square, file);
/* 2174 */       break;
/*      */     
/*      */     case -4: 
/*      */     case 4: 
/* 2178 */       retValue = getRookValue(piece, square, file);
/* 2179 */       break;
/*      */     
/*      */     case -3: 
/*      */     case 3: 
/* 2183 */       retValue = getBishopValue(square, piece);
/* 2184 */       break;
/*      */     
/*      */     case -2: 
/*      */     case 2: 
/* 2188 */       retValue = getKnightValue(piece, square);
/* 2189 */       break;
/*      */     
/*      */     case -1: 
/*      */     case 1: 
/* 2193 */       retValue = getPawnMidgameValue(square, piece, file);
/* 2194 */       break;
/*      */     
/*      */     case 0: 
/*      */     default: 
/* 2198 */       retValue = 0;
/*      */     }
/*      */     
/* 2201 */     return retValue;
/*      */   }
/*      */   
/*      */   private int getKnightValue(int piece, int square)
/*      */   {
/* 2206 */     int retVal = getKnightPcSqValue(square);
/* 2207 */     int x = getHorizontalKingTropism(piece, square);
/* 2208 */     int y = getVerticalKingTropism(piece, square);
/* 2209 */     int manhattanTropism = x + y;
/* 2210 */     retVal -= manhattanTropism / 3;
/* 2211 */     return retVal;
/*      */   }
/*      */   
/*      */   private int getQueenValue(int plusminus, int square, int file)
/*      */   {
/* 2216 */     int retValue = getRookValue(plusminus, square, file) / 4;
/* 2217 */     return retValue;
/*      */   }
/*      */   
/*      */   private int getHorizontalKingTropism(int plusminus, int square) {
/*      */     int kingPosition;
/*      */     int kingPosition;
/* 2223 */     if (plusminus > 0) {
/* 2224 */       kingPosition = this.blackKing;
/*      */     } else
/* 2226 */       kingPosition = this.whiteKing;
/* 2227 */     int myRank = square % 10;
/* 2228 */     int yourRank = kingPosition % 10;
/* 2229 */     return Math.abs(myRank - yourRank);
/*      */   }
/*      */   
/*      */   private int getVerticalKingTropism(int plusminus, int square) {
/*      */     int kingPosition;
/*      */     int kingPosition;
/* 2235 */     if (plusminus > 0) {
/* 2236 */       kingPosition = this.blackKing;
/*      */     } else
/* 2238 */       kingPosition = this.whiteKing;
/* 2239 */     int myRank = square / 10;
/* 2240 */     int yourRank = kingPosition / 10;
/* 2241 */     return Math.abs(myRank - yourRank);
/*      */   }
/*      */   
/*      */   private int getRookValue(int plusminus, int square, int file)
/*      */   {
/* 2246 */     int retVal = isOnOpenFile(square, file) ? 12 : 0;
/* 2247 */     int x = getHorizontalKingTropism(plusminus, square);
/* 2248 */     int y = getVerticalKingTropism(plusminus, square);
/* 2249 */     int minTropism = Math.min(x, y);
/* 2250 */     retVal -= minTropism / 4;
/* 2251 */     return retVal;
/*      */   }
/*      */   
/*      */   public int getValue()
/*      */   {
/* 2256 */     this.wpc = countWhitePieces();
/* 2257 */     this.bpc = countBlackPieces();
/* 2258 */     if (isInsufficientMaterial(this.wpc, this.bpc))
/* 2259 */       return 0;
/* 2260 */     if (isInsufficientMaterial(this.bpc, this.wpc))
/* 2261 */       return 0;
/* 2262 */     if (isEndgame()) {
/* 2263 */       return getEndgameValue();
/*      */     }
/* 2265 */     return getMidgameValue();
/*      */   }
/*      */   
/*      */   private boolean isInsufficientMaterial(PieceCount we, PieceCount other)
/*      */   {
/* 2270 */     return (isInsufficientMaterial(we)) && (isInsufficientMaterial(other));
/*      */   }
/*      */   
/*      */   private boolean isInsufficientMaterial(PieceCount pc)
/*      */   {
/* 2275 */     return (pc.loneKing()) || (pc.oneLight()) || (pc.isNN());
/*      */   }
/*      */   
/*      */   public long getZobrist()
/*      */   {
/* 2280 */     if (this.zobrist != 0L)
/* 2281 */       return this.zobrist;
/* 2282 */     long grosserZobrist = 0L;
/* 2283 */     for (int i = 11; i < 89; i++)
/*      */     {
/* 2285 */       int figur = this.board[i];
/* 2286 */       if (figur != 0)
/*      */       {
/* 2288 */         int spalte = i % 10;
/* 2289 */         if ((spalte != 0) && (spalte != 9))
/*      */         {
/* 2291 */           long kleinerZobrist = kleinerZobristSetzen(figur, i);
/* 2292 */           grosserZobrist ^= kleinerZobrist;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2297 */     if ((this.onMove != null) && (this.onMove.equals(BLACK)))
/* 2298 */       grosserZobrist ^= EdenBrain.onMoveZobrist;
/* 2299 */     if (this.enPassantSquare != 0)
/* 2300 */       grosserZobrist ^= EdenBrain.enPassantZobrist;
/* 2301 */     this.zobrist = grosserZobrist;
/* 2302 */     return grosserZobrist;
/*      */   }
/*      */   
/*      */   public boolean hasCastledBlack()
/*      */   {
/* 2307 */     return this.hasCastledBlack;
/*      */   }
/*      */   
/*      */   public boolean hasCastledWhite()
/*      */   {
/* 2312 */     return this.hasCastledWhite;
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 2317 */     int base1 = this.board.hashCode();
/* 2318 */     int base2 = 0;
/* 2319 */     if (this.castleLongBlack)
/* 2320 */       base2++;
/* 2321 */     if (this.castleLongWhite)
/* 2322 */       base2 += 2;
/* 2323 */     if (this.castleShortBlack)
/* 2324 */       base2 += 4;
/* 2325 */     if (this.castleShortWhite)
/* 2326 */       base2 += 8;
/* 2327 */     if (this.hasCastledWhite)
/* 2328 */       base2 += 16;
/* 2329 */     if (this.hasCastledBlack)
/* 2330 */       base2 += 32;
/* 2331 */     if (this.onMove.booleanValue())
/* 2332 */       base2 += 64;
/* 2333 */     int retVal = base1 * base2;
/* 2334 */     return retVal;
/*      */   }
/*      */   
/*      */   public boolean hasNextCaptureMove()
/*      */   {
/* 2339 */     updateCaptureMove();
/* 2340 */     return this.nextCaptureMove != null;
/*      */   }
/*      */   
/*      */   public boolean hasNextNonCaptureMove()
/*      */   {
/* 2345 */     updateNonCaptureMove();
/* 2346 */     return this.nextNonCaptureMove != null;
/*      */   }
/*      */   
/*      */   private boolean hasNoEnemyPawnsAhead(int piece, int file, int rank)
/*      */   {
/* 2351 */     if ((file < 1) || (file > 8))
/* 2352 */       return true;
/* 2353 */     int plusminus = Math.abs(piece) / piece;
/* 2354 */     int direction = plusminus * 10;
/* 2355 */     int conversion = plusminus <= 0 ? 10 + file : 80 + file;
/* 2356 */     int start = rank * 10 + file + direction;
/* 2357 */     for (int i = start; i != conversion; i += direction) {
/* 2358 */       if (this.board[i] == -plusminus)
/* 2359 */         return false;
/*      */     }
/* 2361 */     return true;
/*      */   }
/*      */   
/*      */   private boolean hasNoEnemyPawns(int piece, int file)
/*      */   {
/* 2366 */     if ((file < 1) || (file > 8))
/* 2367 */       return true;
/* 2368 */     int plusminus = Math.abs(piece) / piece;
/* 2369 */     int conversion = 80 + file;
/* 2370 */     int start = file + 10;
/* 2371 */     for (int i = start; i < conversion; i += 10) {
/* 2372 */       if (this.board[i] == -plusminus)
/* 2373 */         return false;
/*      */     }
/* 2375 */     return true;
/*      */   }
/*      */   
/*      */   public boolean hasNoPawns(int square, int file)
/*      */   {
/* 2380 */     if ((file < 1) || (file > 8))
/* 2381 */       return true;
/* 2382 */     for (int i = 20 + file; i < 79; i += 10) {
/* 2383 */       if (this.board[i] == this.board[square])
/* 2384 */         return false;
/*      */     }
/* 2386 */     return true;
/*      */   }
/*      */   
/*      */   public void initCaptureMoveGenerator()
/*      */   {
/* 2391 */     this.nextCaptureMove = null;
/* 2392 */     this.persistentSquareForCapture = 11;
/* 2393 */     initializeCaptureMoveCounters();
/*      */   }
/*      */   
/*      */   private void initializeCaptureMoveCounters()
/*      */   {
/* 2398 */     this.lastPawnCapture = 0;
/* 2399 */     this.bishopCaptureCount = 0;
/* 2400 */     this.knightCaptures = null;
/* 2401 */     this.bishopCaptures = null;
/* 2402 */     this.rookCaptures = null;
/* 2403 */     this.queenCaptures = null;
/* 2404 */     this.kingCaptures = null;
/*      */   }
/*      */   
/*      */   private void initializeNonCaptureMoveCounters()
/*      */   {
/* 2409 */     this.lastPawnNonCapture = 0;
/* 2410 */     this.bishopNonCaptureCount = 0;
/* 2411 */     this.knightNonCaptures = null;
/* 2412 */     this.bishopNonCaptures = null;
/* 2413 */     this.rookNonCaptures = null;
/* 2414 */     this.queenNonCaptures = null;
/* 2415 */     this.kingNonCaptures = null;
/*      */   }
/*      */   
/*      */   public void initNonCaptureMoveGenerator()
/*      */   {
/* 2420 */     this.nextNonCaptureMove = null;
/* 2421 */     this.persistentSquareForNonCapture = 11;
/* 2422 */     initializeNonCaptureMoveCounters();
/*      */   }
/*      */   
/*      */   public boolean isAttacked(int i, Boolean color)
/*      */   {
/* 2427 */     SortedSet moves = new TreeSet();
/* 2428 */     generateBishopCaptures(moves, i, color);
/* 2429 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2431 */       Move move = (Move)it.next();
/* 2432 */       if ((pieceOn(move.to).getType() == 3) || (pieceOn(move.to).getType() == 5)) {
/* 2433 */         return true;
/*      */       }
/*      */     }
/* 2436 */     moves.clear();
/* 2437 */     generateRookCaptures(this, moves, i, color);
/* 2438 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2440 */       Move move = (Move)it.next();
/* 2441 */       if ((pieceOn(move.to).getType() == 4) || (pieceOn(move.to).getType() == 5)) {
/* 2442 */         return true;
/*      */       }
/*      */     }
/* 2445 */     moves.clear();
/* 2446 */     generateKingCaptures(this, moves, i, color);
/* 2447 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2449 */       Move move = (Move)it.next();
/* 2450 */       if (pieceOn(move.to).getType() == 6) {
/* 2451 */         return true;
/*      */       }
/*      */     }
/* 2454 */     moves.clear();
/* 2455 */     generateKnightCaptures(this, moves, i, color);
/* 2456 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2458 */       Move move = (Move)it.next();
/* 2459 */       Piece p = pieceOn(move.to);
/* 2460 */       if ((p != null) && (p.getType() == 2)) {
/* 2461 */         return true;
/*      */       }
/*      */     }
/* 2464 */     moves.clear();
/* 2465 */     generatePawnCaptures(this, moves, i, color);
/* 2466 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2468 */       Move move = (Move)it.next();
/* 2469 */       if (pieceOn(move.to).getType() == 1) {
/* 2470 */         return true;
/*      */       }
/*      */     }
/* 2473 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isDoubled(int square, int file)
/*      */   {
/* 2478 */     for (int i = 20 + file; i < 79; i += 10) {
/* 2479 */       if ((i != square) && (this.board[i] == this.board[square]))
/* 2480 */         return true;
/*      */     }
/* 2482 */     return false;
/*      */   }
/*      */   
/*      */   private boolean isEndgame()
/*      */   {
/* 2487 */     if ((this.isEndGame != null) && (this.isEndGame.equals(Boolean.TRUE)))
/* 2488 */       return true;
/* 2489 */     int lightPiecesCount = this.wpc.knightsCount + this.wpc.bishopsCount;
/* 2490 */     int heavyPiecesCount = this.wpc.rookCount + this.wpc.queensCount;
/* 2491 */     if (this.wpc.queensCount >= 1)
/*      */     {
/* 2493 */       if (this.wpc.rookCount > 1)
/* 2494 */         return false;
/* 2495 */       if ((this.wpc.rookCount == 1) && (lightPiecesCount > 0))
/* 2496 */         return false;
/* 2497 */       return lightPiecesCount <= 2;
/*      */     }
/* 2499 */     if ((this.wpc.rookCount > 1) && (lightPiecesCount > 2))
/* 2500 */       return false;
/* 2501 */     if ((this.wpc.rookCount == 1) && (lightPiecesCount > 3))
/*      */     {
/* 2503 */       return false;
/*      */     }
/*      */     
/* 2506 */     this.isEndGame = Boolean.TRUE;
/* 2507 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private PieceCount countWhitePieces()
/*      */   {
/* 2513 */     int queensCount = 0;
/* 2514 */     int rookCount = 0;
/* 2515 */     int knightsCount = 0;
/* 2516 */     int bishopsCount = 0;
/* 2517 */     int pawnsCount = 0;
/* 2518 */     for (Iterator wpIt = this.whitePieces.iterator(); wpIt.hasNext();)
/*      */     {
/* 2520 */       int i = ((Integer)wpIt.next()).intValue();
/* 2521 */       int piece = this.board[i];
/* 2522 */       switch (piece)
/*      */       {
/*      */       case 1: 
/* 2525 */         pawnsCount++;
/* 2526 */         break;
/*      */       
/*      */       case 4: 
/* 2529 */         rookCount++;
/* 2530 */         break;
/*      */       
/*      */       case 3: 
/* 2533 */         bishopsCount++;
/* 2534 */         break;
/*      */       
/*      */       case 2: 
/* 2537 */         knightsCount++;
/* 2538 */         break;
/*      */       
/*      */       case 5: 
/* 2541 */         queensCount++;
/*      */       }
/*      */       
/*      */     }
/*      */     
/* 2546 */     PieceCount wpc = new PieceCount(pawnsCount, knightsCount, bishopsCount, rookCount, queensCount);
/* 2547 */     return wpc;
/*      */   }
/*      */   
/*      */   private PieceCount countBlackPieces()
/*      */   {
/* 2552 */     int queensCount = 0;
/* 2553 */     int rookCount = 0;
/* 2554 */     int knightsCount = 0;
/* 2555 */     int bishopsCount = 0;
/* 2556 */     int pawnsCount = 0;
/* 2557 */     for (Iterator wpIt = this.blackPieces.iterator(); wpIt.hasNext();)
/*      */     {
/* 2559 */       int i = ((Integer)wpIt.next()).intValue();
/* 2560 */       int piece = this.board[i];
/* 2561 */       switch (piece)
/*      */       {
/*      */       case -1: 
/* 2564 */         pawnsCount++;
/* 2565 */         break;
/*      */       
/*      */       case -4: 
/* 2568 */         rookCount++;
/* 2569 */         break;
/*      */       
/*      */       case -3: 
/* 2572 */         bishopsCount++;
/* 2573 */         break;
/*      */       
/*      */       case -2: 
/* 2576 */         knightsCount++;
/* 2577 */         break;
/*      */       
/*      */       case -5: 
/* 2580 */         queensCount++;
/*      */       }
/*      */       
/*      */     }
/*      */     
/* 2585 */     PieceCount wpc = new PieceCount(pawnsCount, knightsCount, bishopsCount, rookCount, queensCount);
/* 2586 */     return wpc;
/*      */   }
/*      */   
/*      */   boolean isGivingCheck()
/*      */   {
/* 2591 */     if (this.isGivingCheck != null)
/* 2592 */       return this.isGivingCheck.booleanValue();
/* 2593 */     Boolean othercolor = Boolean.valueOf(!onMove().booleanValue());
/* 2594 */     int currentPiece = findKing(othercolor.booleanValue());
/* 2595 */     if (currentPiece == -1)
/* 2596 */       throw new IllegalStateException("no king found");
/* 2597 */     int i = currentPiece;
/* 2598 */     SortedSet moves = new TreeSet();
/* 2599 */     generateBishopCaptures(moves, i, othercolor);
/* 2600 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2602 */       Move move = (Move)it.next();
/* 2603 */       int piece = Math.abs(this.board[move.to]);
/* 2604 */       if ((piece == 3) || (piece == 5))
/*      */       {
/* 2606 */         int t = this.board[move.to];
/* 2607 */         Boolean c = EdenBrain.convertColor(t);
/* 2608 */         if (!c.equals(othercolor))
/*      */         {
/* 2610 */           this.isGivingCheck = new Boolean(true);
/* 2611 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2616 */     moves.clear();
/* 2617 */     generateRookCaptures(this, moves, i, othercolor);
/* 2618 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2620 */       Move move = (Move)it.next();
/* 2621 */       int piece = Math.abs(this.board[move.to]);
/* 2622 */       if ((piece == 4) || (piece == 5))
/*      */       {
/* 2624 */         int t = this.board[move.to];
/* 2625 */         Boolean c = EdenBrain.convertColor(t);
/* 2626 */         if (!c.equals(othercolor))
/*      */         {
/* 2628 */           this.isGivingCheck = new Boolean(true);
/* 2629 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2634 */     moves.clear();
/* 2635 */     generateKingCaptures(this, moves, i, othercolor);
/* 2636 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2638 */       Move move = (Move)it.next();
/* 2639 */       int piece = Math.abs(this.board[move.to]);
/* 2640 */       if (piece == 6)
/*      */       {
/* 2642 */         this.isGivingCheck = new Boolean(true);
/* 2643 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2647 */     moves.clear();
/* 2648 */     generateKnightCaptures(this, moves, i, othercolor);
/* 2649 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2651 */       Move move = (Move)it.next();
/* 2652 */       int piece = Math.abs(this.board[move.to]);
/* 2653 */       if (piece == 2)
/*      */       {
/* 2655 */         int t = this.board[move.to];
/* 2656 */         Boolean c = EdenBrain.convertColor(t);
/* 2657 */         if (!c.equals(othercolor))
/*      */         {
/* 2659 */           this.isGivingCheck = new Boolean(true);
/* 2660 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2665 */     moves.clear();
/* 2666 */     generatePawnCaptures(this, moves, i, othercolor);
/* 2667 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2669 */       Move move = (Move)it.next();
/* 2670 */       int piece = Math.abs(this.board[move.to]);
/* 2671 */       if ((piece == 1) && (Math.abs(this.board[move.to]) == 1))
/*      */       {
/* 2673 */         int t = this.board[move.to];
/* 2674 */         Boolean c = EdenBrain.convertColor(t);
/* 2675 */         if (!c.equals(othercolor))
/*      */         {
/* 2677 */           this.isGivingCheck = new Boolean(true);
/* 2678 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2683 */     this.isGivingCheck = new Boolean(false);
/* 2684 */     return false;
/*      */   }
/*      */   
/*      */   boolean isGivingCheckForCastling(int square)
/*      */   {
/* 2689 */     if (this.isGivingCheck != null)
/* 2690 */       return this.isGivingCheck.booleanValue();
/* 2691 */     int i = square;
/* 2692 */     Boolean c1 = EdenBrain.convertColor(this.board[i]);
/* 2693 */     SortedSet moves = new TreeSet();
/* 2694 */     generateBishopCaptures(moves, i, c1);
/* 2695 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2697 */       Move move = (Move)it.next();
/* 2698 */       int piece = Math.abs(this.board[move.to]);
/* 2699 */       if ((piece == 3) || (piece == 5))
/*      */       {
/* 2701 */         int t = this.board[move.to];
/* 2702 */         Boolean c = EdenBrain.convertColor(t);
/* 2703 */         if (!c.equals(c1))
/*      */         {
/* 2705 */           this.isGivingCheck = new Boolean(true);
/* 2706 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2711 */     moves.clear();
/* 2712 */     generateRookCaptures(this, moves, i, c1);
/* 2713 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2715 */       Move move = (Move)it.next();
/* 2716 */       int piece = Math.abs(this.board[move.to]);
/* 2717 */       if ((piece == 4) || (piece == 5))
/*      */       {
/* 2719 */         int t = this.board[move.to];
/* 2720 */         Boolean c = EdenBrain.convertColor(t);
/* 2721 */         if (!c.equals(c1))
/*      */         {
/* 2723 */           this.isGivingCheck = new Boolean(true);
/* 2724 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2729 */     moves.clear();
/* 2730 */     generateKingCaptures(this, moves, i, c1);
/* 2731 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2733 */       Move move = (Move)it.next();
/* 2734 */       int piece = Math.abs(this.board[move.to]);
/* 2735 */       if (piece == 6)
/*      */       {
/* 2737 */         this.isGivingCheck = new Boolean(true);
/* 2738 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2742 */     moves.clear();
/* 2743 */     generateKnightCaptures(this, moves, i, c1);
/* 2744 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2746 */       Move move = (Move)it.next();
/* 2747 */       int piece = Math.abs(this.board[move.to]);
/* 2748 */       if (piece == 2)
/*      */       {
/* 2750 */         int t = this.board[move.to];
/* 2751 */         Boolean c = EdenBrain.convertColor(t);
/* 2752 */         if (!c.equals(c1))
/*      */         {
/* 2754 */           this.isGivingCheck = new Boolean(true);
/* 2755 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2760 */     moves.clear();
/* 2761 */     generatePawnCaptures(this, moves, i, Boolean.valueOf(!onMove().booleanValue()));
/* 2762 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2764 */       Move move = (Move)it.next();
/* 2765 */       int piece = Math.abs(this.board[move.to]);
/* 2766 */       if ((piece == 1) && (Math.abs(this.board[move.to]) == 1))
/*      */       {
/* 2768 */         int t = this.board[move.to];
/* 2769 */         Boolean c = EdenBrain.convertColor(t);
/* 2770 */         if (!c.equals(c1))
/*      */         {
/* 2772 */           this.isGivingCheck = new Boolean(true);
/* 2773 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2778 */     this.isGivingCheck = new Boolean(false);
/* 2779 */     return false;
/*      */   }
/*      */   
/*      */   boolean isGivingCheckNonKingMoving(int moveFrom)
/*      */   {
/* 2784 */     Integer zobrist = null;
/* 2785 */     if (this.isGivingCheck != null)
/* 2786 */       return this.isGivingCheck.booleanValue();
/* 2787 */     Boolean othercolor = Boolean.valueOf(!onMove().booleanValue());
/* 2788 */     int kingPosition = findKing(othercolor.booleanValue());
/* 2789 */     if (kingPosition == -1)
/* 2790 */       throw new IllegalStateException("no king found");
/* 2791 */     SortedSet moves = new TreeSet();
/* 2792 */     if (onDiagonal(moveFrom, kingPosition))
/*      */     {
/* 2794 */       moves.clear();
/* 2795 */       generateBishopCaptures(moves, kingPosition, othercolor);
/* 2796 */       for (Iterator it = moves.iterator(); it.hasNext();)
/*      */       {
/* 2798 */         Move move = (Move)it.next();
/* 2799 */         int piece = Math.abs(this.board[move.to]);
/* 2800 */         if ((piece == 3) || (piece == 5))
/*      */         {
/* 2802 */           int t = this.board[move.to];
/* 2803 */           Boolean c = EdenBrain.convertColor(t);
/* 2804 */           if (!c.equals(othercolor))
/*      */           {
/* 2806 */             this.isGivingCheck = new Boolean(true);
/* 2807 */             addToCheckHash(zobrist);
/* 2808 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 2813 */       moves.clear();
/* 2814 */       generatePawnCaptures(this, moves, kingPosition, othercolor);
/* 2815 */       for (Iterator it = moves.iterator(); it.hasNext();)
/*      */       {
/* 2817 */         Move move = (Move)it.next();
/* 2818 */         int piece = Math.abs(this.board[move.to]);
/* 2819 */         if (piece == 1)
/*      */         {
/* 2821 */           int t = this.board[move.to];
/* 2822 */           Boolean c = EdenBrain.convertColor(t);
/* 2823 */           if (c.equals(othercolor))
/*      */           {
/* 2825 */             this.isGivingCheck = new Boolean(true);
/* 2826 */             addToCheckHash(zobrist);
/* 2827 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2833 */     if ((onRank(moveFrom, kingPosition)) || (onFile(moveFrom, kingPosition)))
/*      */     {
/* 2835 */       moves.clear();
/* 2836 */       generateRookCaptures(this, moves, kingPosition, othercolor);
/* 2837 */       for (Iterator it = moves.iterator(); it.hasNext();)
/*      */       {
/* 2839 */         Move move = (Move)it.next();
/* 2840 */         int piece = Math.abs(this.board[move.to]);
/* 2841 */         if ((piece == 4) || (piece == 5))
/*      */         {
/* 2843 */           int t = this.board[move.to];
/* 2844 */           Boolean c = EdenBrain.convertColor(t);
/* 2845 */           if (!c.equals(othercolor))
/*      */           {
/* 2847 */             this.isGivingCheck = new Boolean(true);
/* 2848 */             addToCheckHash(zobrist);
/* 2849 */             return true;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 2855 */     moves.clear();
/* 2856 */     generateKingCaptures(this, moves, kingPosition, othercolor);
/* 2857 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2859 */       Move move = (Move)it.next();
/* 2860 */       int piece = Math.abs(this.board[move.to]);
/* 2861 */       if (piece == 6)
/*      */       {
/* 2863 */         this.isGivingCheck = new Boolean(true);
/* 2864 */         addToCheckHash(zobrist);
/* 2865 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2869 */     this.isGivingCheck = new Boolean(false);
/* 2870 */     addToCheckHash(zobrist);
/* 2871 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isIsolated(int square, int file)
/*      */   {
/* 2876 */     return (hasNoPawns(square, file - 1)) && (hasNoPawns(square, file + 1));
/*      */   }
/*      */   
/*      */   public boolean isLegal(int from, int to, int promotedTo)
/*      */   {
/* 2881 */     Move toConsider = new Move(this, from, to, promotedTo);
/* 2882 */     SortedSet legalMoves = generateLegalMoves();
/* 2883 */     for (Iterator it = legalMoves.iterator(); it.hasNext();)
/*      */     {
/* 2885 */       Move move = (Move)it.next();
/* 2886 */       if (toConsider.getText().equals(move.getText())) {
/* 2887 */         return true;
/*      */       }
/*      */     }
/* 2890 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isMate()
/*      */   {
/* 2895 */     if (!noMoves())
/* 2896 */       return false;
/* 2897 */     return isReceivingCheck();
/*      */   }
/*      */   
/*      */   public boolean isOnOpenFile(int square, int file)
/*      */   {
/* 2902 */     if ((file < 1) || (file > 8))
/* 2903 */       return false;
/* 2904 */     for (int i = 20 + file; i < 80; i += 10) {
/* 2905 */       if ((this.board[i] == 1) || (this.board[i] == -1))
/* 2906 */         return false;
/*      */     }
/* 2908 */     return true;
/*      */   }
/*      */   
/*      */   private boolean isPassed(int square, int plusminus, int file, int rank)
/*      */   {
/* 2913 */     if (!hasNoEnemyPawnsAhead(plusminus, file - 1, rank))
/* 2914 */       return false;
/* 2915 */     if (!hasNoEnemyPawnsAhead(plusminus, file + 1, rank))
/* 2916 */       return false;
/* 2917 */     return hasNoEnemyPawnsAhead(plusminus, file, rank);
/*      */   }
/*      */   
/*      */   public boolean isReceivingCheck()
/*      */   {
/* 2922 */     if (this.isReceivingCheck != null)
/* 2923 */       return this.isReceivingCheck.booleanValue();
/* 2924 */     Boolean kingColor = onMove();
/* 2925 */     int kingPosition = findKing(kingColor.booleanValue());
/* 2926 */     if (kingPosition == -1)
/* 2927 */       throw new IllegalStateException("no king found");
/* 2928 */     int i = kingPosition;
/* 2929 */     SortedSet moves = new TreeSet();
/* 2930 */     Iterator it = moves.iterator();
/* 2931 */     generateRookCaptures(this, moves, i, kingColor);
/* 2932 */     for (it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2934 */       Move move = (Move)it.next();
/* 2935 */       if ((pieceOn(move.to).getType() == 4) || (pieceOn(move.to).getType() == 5))
/*      */       {
/* 2937 */         this.isReceivingCheck = new Boolean(true);
/* 2938 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2942 */     moves.clear();
/* 2943 */     generatePawnCaptures(this, moves, i, kingColor);
/* 2944 */     for (it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2946 */       Move move = (Move)it.next();
/* 2947 */       if (pieceOn(move.to).getType() == 1)
/*      */       {
/* 2949 */         this.isReceivingCheck = new Boolean(true);
/* 2950 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2954 */     moves.clear();
/* 2955 */     generateKnightCaptures(this, moves, i, kingColor);
/* 2956 */     for (it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2958 */       Move move = (Move)it.next();
/* 2959 */       Piece p = pieceOn(move.to);
/* 2960 */       if ((p != null) && (p.getType() == 2))
/*      */       {
/* 2962 */         this.isReceivingCheck = new Boolean(true);
/* 2963 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2967 */     moves.clear();
/* 2968 */     generateBishopCaptures(moves, i, kingColor);
/* 2969 */     for (it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2971 */       Move move = (Move)it.next();
/* 2972 */       if ((pieceOn(move.to).getType() == 3) || (pieceOn(move.to).getType() == 5))
/*      */       {
/* 2974 */         this.isReceivingCheck = new Boolean(true);
/* 2975 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2979 */     moves.clear();
/* 2980 */     generateKingMovesNoCastling(this, moves, i, kingColor);
/* 2981 */     for (it = moves.iterator(); it.hasNext();)
/*      */     {
/* 2983 */       Move move = (Move)it.next();
/* 2984 */       if (pieceOn(move.to).getType() == 6)
/*      */       {
/* 2986 */         this.isReceivingCheck = new Boolean(true);
/* 2987 */         return true;
/*      */       }
/*      */     }
/*      */     
/* 2991 */     this.isReceivingCheck = new Boolean(false);
/* 2992 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isStartPosition()
/*      */   {
/* 2997 */     return this.isStartPosition;
/*      */   }
/*      */   
/*      */   private long kleinerZobristEntfernen(int i)
/*      */   {
/* 3002 */     int figur = this.board[i];
/* 3003 */     return kleinerZobristSetzen(figur, i);
/*      */   }
/*      */   
/*      */   private long kleinerZobristSetzen(int figur, int square)
/*      */   {
/* 3008 */     if (figur == 0)
/* 3009 */       return 0L;
/* 3010 */     if (figur > 0) {
/* 3011 */       return EdenBrain.zobrists[0][(figur - 1)][(square - 11)];
/*      */     }
/* 3013 */     return EdenBrain.zobrists[1][(-figur - 1)][(square - 11)];
/*      */   }
/*      */   
/*      */   private void makeMove(int from, int to, int promotedTo)
/*      */   {
/* 3018 */     Move move = new Move(this, from, to, promotedTo);
/* 3019 */     boolean isLegal = isLegal(from, to, promotedTo);
/* 3020 */     if (isLegal) {
/*      */       try
/*      */       {
/* 3023 */         makeMove(move);
/*      */       }
/*      */       catch (ThreeRepetitionsAB e)
/*      */       {
/* 3027 */         e.printStackTrace();
/*      */       }
/*      */     } else {
/* 3030 */       System.out.println("Illegal move:" + move);
/*      */     }
/*      */   }
/*      */   
/*      */   void makeMove(Move move) throws ThreeRepetitionsAB
/*      */   {
/* 3036 */     long z = getZobrist();
/* 3037 */     this.zobrist = updateZobristForMove(move, z);
/* 3038 */     long pz = getPawnZobrist().longValue();
/* 3039 */     long incrementalZobrist = updatePawnZobristForMove(move, pz);
/* 3040 */     this.pawnZobrist = new Long(incrementalZobrist);
/* 3041 */     boolean updateDrawCount = false;
/* 3042 */     if ((move.capturedPiece != 0) || (Math.abs(EdenBrain.position.board[move.from]) == 1))
/* 3043 */       updateDrawCount = true;
/* 3044 */     this.isGivingCheck = null;
/* 3045 */     this.legalMoves = null;
/* 3046 */     setStartPosition(false);
/* 3047 */     int movingPiece = moveRaw(move);
/* 3048 */     int movingPieceType = Math.abs(movingPiece);
/* 3049 */     if ((movingPieceType == 1) && (move.to == enPassant()))
/* 3050 */       clearEnPassantCapture(move, this.board);
/* 3051 */     if (this.enPassantSquare != 0)
/*      */     {
/* 3053 */       this.enPassantSquare = 0;
/* 3054 */       updateZobristEnPassant();
/*      */     }
/* 3056 */     if ((movingPieceType == 1) && (Math.abs(move.to - move.from) == 20))
/*      */     {
/* 3058 */       this.enPassantSquare = ((move.from + move.to) / 2);
/* 3059 */       updateZobristEnPassant();
/*      */     }
/* 3061 */     if ((movingPieceType == 6) && (Math.abs(move.from - move.to) == 2)) {
/*      */       int rookTo;
/*      */       int rookFrom;
/*      */       int rookTo;
/* 3065 */       if (move.to == 17)
/*      */       {
/* 3067 */         int rookFrom = 18;
/* 3068 */         rookTo = 16;
/*      */       } else { int rookTo;
/* 3070 */         if (move.to == 87)
/*      */         {
/* 3072 */           int rookFrom = 88;
/* 3073 */           rookTo = 86;
/*      */         } else { int rookTo;
/* 3075 */           if (move.to == 13)
/*      */           {
/* 3077 */             int rookFrom = 11;
/* 3078 */             rookTo = 14;
/*      */           } else { int rookTo;
/* 3080 */             if (move.to == 83)
/*      */             {
/* 3082 */               int rookFrom = 81;
/* 3083 */               rookTo = 84;
/*      */             }
/*      */             else {
/* 3086 */               rookFrom = -1;
/* 3087 */               rookTo = -1;
/* 3088 */               System.out.println("move.from: " + move.from);
/* 3089 */               throw new RuntimeException("move.to.getIndex: " + move.to + " which is impossible!");
/*      */             } } } }
/* 3091 */       Move rookJump = new Move(this, rookFrom, rookTo);
/* 3092 */       moveRaw(rookJump);
/* 3093 */       if (movingPiece > 0) {
/* 3094 */         this.hasCastledWhite = true;
/*      */       } else
/* 3096 */         this.hasCastledBlack = true;
/*      */     }
/* 3098 */     if (movingPieceType == 6)
/*      */     {
/* 3100 */       if (this.onMove.equals(WHITE))
/*      */       {
/* 3102 */         this.castleShortWhite = false;
/* 3103 */         this.castleLongWhite = false;
/*      */       }
/*      */       else {
/* 3106 */         this.castleShortBlack = false;
/* 3107 */         this.castleLongBlack = false;
/*      */       }
/*      */     }
/* 3110 */     else if (movingPieceType == 4)
/* 3111 */       if (this.onMove.equals(WHITE))
/*      */       {
/* 3113 */         if (move.from == 18) {
/* 3114 */           this.castleShortWhite = false;
/*      */         }
/* 3116 */         else if (move.from == 11) {
/* 3117 */           this.castleLongWhite = false;
/*      */         }
/* 3119 */       } else if (move.from == 88) {
/* 3120 */         this.castleShortBlack = false;
/*      */       }
/* 3122 */       else if (move.from == 81)
/* 3123 */         this.castleLongBlack = false;
/* 3124 */     int p = this.board[11];
/* 3125 */     if (p != 4)
/* 3126 */       this.castleLongWhite = false;
/* 3127 */     p = this.board[18];
/* 3128 */     if (p != 4)
/* 3129 */       this.castleShortWhite = false;
/* 3130 */     p = this.board[81];
/* 3131 */     if (p != -4)
/* 3132 */       this.castleLongBlack = false;
/* 3133 */     p = this.board[88];
/* 3134 */     if (p != -4)
/* 3135 */       this.castleShortBlack = false;
/* 3136 */     this.onMove = Boolean.valueOf(!this.onMove.booleanValue());
/* 3137 */     if (!EdenBrain.withinAlphabeta)
/*      */     {
/* 3139 */       if (updateDrawCount)
/*      */       {
/* 3141 */         EdenBrain.threeDrawsTable.clear();
/*      */       }
/*      */       else {
/* 3144 */         long myZobrist = getZobrist();
/* 3145 */         Long zobi = new Long(myZobrist);
/* 3146 */         if (EdenBrain.threeDrawsTable.containsKey(zobi))
/*      */         {
/* 3148 */           Integer count = (Integer)EdenBrain.threeDrawsTable.get(zobi);
/* 3149 */           EdenBrain.threeDrawsTable.remove(zobi);
/* 3150 */           if (count.intValue() >= 2)
/* 3151 */             throw new IllegalStateException("three-fold repetition!");
/* 3152 */           count = new Integer(count.intValue() + 1);
/* 3153 */           EdenBrain.threeDrawsTable.put(zobi, count);
/*      */         }
/*      */         else {
/* 3156 */           Integer count = new Integer(1);
/* 3157 */           EdenBrain.threeDrawsTable.put(zobi, count);
/*      */         }
/*      */       }
/*      */     }
/* 3161 */     else if (updateDrawCount)
/*      */     {
/* 3163 */       EdenBrain.alphaBetaDraws.clear();
/*      */     }
/*      */     else {
/* 3166 */       long myZobrist = getZobrist();
/* 3167 */       Long zobi = new Long(myZobrist);
/* 3168 */       int total = 0;
/* 3169 */       if (EdenBrain.alphaBetaDraws.containsKey(zobi))
/*      */       {
/* 3171 */         total += ((Integer)EdenBrain.alphaBetaDraws.get(zobi)).intValue();
/*      */         
/* 3173 */         if (EdenBrain.threeDrawsTable.containsKey(zobi))
/*      */         {
/* 3175 */           total += ((Integer)EdenBrain.threeDrawsTable.get(zobi)).intValue();
/*      */         }
/*      */         else {
/* 3178 */           Integer count = new Integer(1);
/* 3179 */           EdenBrain.alphaBetaDraws.put(zobi, count);
/*      */         }
/* 3181 */         EdenBrain.alphaBetaDraws.remove(zobi);
/* 3182 */         if (total >= 2)
/* 3183 */           throw new ThreeRepetitionsAB("three-fold repetition!");
/* 3184 */         Integer count = new Integer(total + 1);
/* 3185 */         EdenBrain.alphaBetaDraws.put(zobi, count);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void makeMove(String moveString)
/*      */     throws ThreeRepetitionsAB
/*      */   {
/* 3193 */     Move m = Move.create(this, moveString);
/* 3194 */     makeMove(m.from, m.to, m.capturedPiece);
/* 3195 */     this.isGivingCheck = null;
/* 3196 */     this.isReceivingCheck = null;
/*      */   }
/*      */   
/*      */   public Piece makeNewPiece(Square to, String promotedTo, Piece newPiece)
/*      */   {
/* 3201 */     if (promotedTo.equals("q")) {
/* 3202 */       newPiece = Piece.create(to, onMove(), 5);
/*      */     }
/* 3204 */     else if (promotedTo.equals("r")) {
/* 3205 */       newPiece = Piece.create(to, onMove(), 4);
/*      */     }
/* 3207 */     else if (promotedTo.equals("b")) {
/* 3208 */       newPiece = Piece.create(to, onMove(), 3);
/*      */     }
/* 3210 */     else if (promotedTo.equals("n"))
/* 3211 */       newPiece = Piece.create(to, onMove(), 2);
/* 3212 */     return newPiece;
/*      */   }
/*      */   
/*      */   void makeTestMove(Move move)
/*      */   {
/* 3217 */     this.isGivingCheck = null;
/* 3218 */     this.legalMoves = null;
/* 3219 */     setStartPosition(false);
/* 3220 */     int movingPiece = moveRaw(move);
/* 3221 */     int movingPieceType = Math.abs(movingPiece);
/* 3222 */     if ((movingPieceType == 1) && (move.to == enPassant()))
/* 3223 */       testClearEnPassantCapture(move, this.board);
/* 3224 */     if ((movingPieceType == 1) && (Math.abs(move.to - move.from) == 20)) {
/* 3225 */       this.enPassantSquare = ((move.from + move.to) / 2);
/*      */     } else
/* 3227 */       this.enPassantSquare = 0;
/* 3228 */     if ((movingPieceType == 6) && (Math.abs(move.from - move.to) == 2)) {
/*      */       int rookTo;
/*      */       int rookFrom;
/*      */       int rookTo;
/* 3232 */       if (move.to == 17)
/*      */       {
/* 3234 */         int rookFrom = 18;
/* 3235 */         rookTo = 16;
/*      */       } else { int rookTo;
/* 3237 */         if (move.to == 87)
/*      */         {
/* 3239 */           int rookFrom = 88;
/* 3240 */           rookTo = 86;
/*      */         } else { int rookTo;
/* 3242 */           if (move.to == 13)
/*      */           {
/* 3244 */             int rookFrom = 11;
/* 3245 */             rookTo = 14;
/*      */           } else { int rookTo;
/* 3247 */             if (move.to == 83)
/*      */             {
/* 3249 */               int rookFrom = 81;
/* 3250 */               rookTo = 84;
/*      */             }
/*      */             else {
/* 3253 */               rookFrom = -1;
/* 3254 */               rookTo = -1;
/* 3255 */               System.out.println("move.from: " + move.from);
/* 3256 */               throw new RuntimeException("move.to.getIndex: " + move.to + " which is impossible!");
/*      */             } } } }
/* 3258 */       Move rookJump = new Move(this, rookFrom, rookTo);
/* 3259 */       moveRaw(rookJump);
/* 3260 */       if (movingPiece > 0) {
/* 3261 */         this.hasCastledWhite = true;
/*      */       } else
/* 3263 */         this.hasCastledBlack = true;
/*      */     }
/* 3265 */     if (movingPieceType == 6)
/*      */     {
/* 3267 */       if (this.onMove.equals(WHITE))
/*      */       {
/* 3269 */         this.castleShortWhite = false;
/* 3270 */         this.castleLongWhite = false;
/*      */       }
/*      */       else {
/* 3273 */         this.castleShortBlack = false;
/* 3274 */         this.castleLongBlack = false;
/*      */       }
/*      */     }
/* 3277 */     else if (movingPieceType == 4)
/* 3278 */       if (this.onMove.equals(WHITE))
/*      */       {
/* 3280 */         if (move.from == 18) {
/* 3281 */           this.castleShortWhite = false;
/*      */         }
/* 3283 */         else if (move.from == 11) {
/* 3284 */           this.castleLongWhite = false;
/*      */         }
/* 3286 */       } else if (move.from == 88) {
/* 3287 */         this.castleShortBlack = false;
/*      */       }
/* 3289 */       else if (move.from == 81)
/* 3290 */         this.castleLongBlack = false;
/* 3291 */     int p = this.board[11];
/* 3292 */     if (p != 4)
/* 3293 */       this.castleLongWhite = false;
/* 3294 */     p = this.board[18];
/* 3295 */     if (p != 4)
/* 3296 */       this.castleShortWhite = false;
/* 3297 */     p = this.board[81];
/* 3298 */     if (p != -4)
/* 3299 */       this.castleLongBlack = false;
/* 3300 */     p = this.board[88];
/* 3301 */     if (p != -4)
/* 3302 */       this.castleShortBlack = false;
/* 3303 */     this.onMove = Boolean.valueOf(!this.onMove.booleanValue());
/*      */   }
/*      */   
/*      */   private int moveRaw(Move move)
/*      */   {
/* 3308 */     int movingPiece = 0;
/* 3309 */     Integer fromSquare = new Integer(move.from);
/* 3310 */     Integer capturedSquare = new Integer(move.to);
/* 3311 */     int capturedPiece = this.board[move.to];
/* 3312 */     if (capturedPiece > 0)
/*      */     {
/* 3314 */       this.whitePieces.remove(capturedSquare);
/* 3315 */       if (capturedPiece == 6) {
/* 3316 */         this.whiteKing = -1;
/*      */       }
/* 3318 */     } else if (capturedPiece < 0)
/*      */     {
/* 3320 */       this.blackPieces.remove(capturedSquare);
/* 3321 */       if (capturedPiece == -6)
/* 3322 */         this.blackKing = -1;
/*      */     }
/* 3324 */     if (move.promotedTo == 0)
/*      */     {
/* 3326 */       movingPiece = this.board[move.from];
/* 3327 */       if (movingPiece > 0)
/*      */       {
/* 3329 */         this.whitePieces.add(capturedSquare);
/* 3330 */         this.whitePieces.remove(fromSquare);
/* 3331 */         if (movingPiece == 6) {
/* 3332 */           this.whiteKing = move.to;
/*      */         }
/*      */       } else {
/* 3335 */         this.blackPieces.add(capturedSquare);
/* 3336 */         this.blackPieces.remove(fromSquare);
/* 3337 */         if (movingPiece == -6)
/* 3338 */           this.blackKing = move.to;
/*      */       }
/* 3340 */       this.board[move.to] = movingPiece;
/* 3341 */       clearSquare(move.from, 0, this.board);
/*      */     }
/*      */     else {
/* 3344 */       movingPiece = move.promotedTo;
/* 3345 */       if (this.board[move.from] < 0)
/* 3346 */         movingPiece *= -1;
/* 3347 */       this.board[move.to] = movingPiece;
/* 3348 */       clearSquare(move.from, 0, this.board);
/* 3349 */       if (movingPiece > 0)
/*      */       {
/* 3351 */         this.whitePieces.add(new Integer(move.to));
/* 3352 */         this.whitePieces.remove(fromSquare);
/* 3353 */         if (movingPiece == 6) {
/* 3354 */           this.whiteKing = move.to;
/*      */         }
/*      */       } else {
/* 3357 */         this.blackPieces.add(new Integer(move.to));
/* 3358 */         this.blackPieces.remove(fromSquare);
/* 3359 */         if (movingPiece == -6)
/* 3360 */           this.blackKing = move.to;
/*      */       }
/*      */     }
/* 3363 */     return movingPiece;
/*      */   }
/*      */   
/*      */   public Move nextCaptureMove()
/*      */   {
/* 3368 */     updateCaptureMove();
/* 3369 */     if (this.nextCaptureMove == null)
/*      */     {
/* 3371 */       throw new NoSuchElementException("next without hasnext");
/*      */     }
/*      */     
/* 3374 */     Move retVal = new Move(this.nextCaptureMove);
/* 3375 */     this.nextCaptureMove = null;
/* 3376 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */   Move nextNonCaptureMove()
/*      */   {
/* 3382 */     updateNonCaptureMove();
/* 3383 */     if (this.nextNonCaptureMove == null)
/*      */     {
/* 3385 */       throw new NoSuchElementException("next without hasnext");
/*      */     }
/*      */     
/* 3388 */     Move retVal = new Move(this.nextNonCaptureMove);
/* 3389 */     this.nextNonCaptureMove = null;
/* 3390 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean noMoves()
/*      */   {
/* 3396 */     Collection moves = generateLegalMoves();
/* 3397 */     return moves.size() == 0;
/*      */   }
/*      */   
/*      */   private boolean onDiagonal(int moveFrom, int kingPosition)
/*      */   {
/* 3402 */     int diff = moveFrom - kingPosition;
/* 3403 */     return (diff % 11 == 0) || (diff % 9 == 0);
/*      */   }
/*      */   
/*      */   private boolean onFile(int moveFrom, int kingPosition)
/*      */   {
/* 3408 */     int diff = moveFrom - kingPosition;
/* 3409 */     return diff / 10 == 0;
/*      */   }
/*      */   
/*      */   public Boolean onMove()
/*      */   {
/* 3414 */     return this.onMove;
/*      */   }
/*      */   
/*      */   private boolean onRank(int moveFrom, int kingPosition)
/*      */   {
/* 3419 */     int col1 = moveFrom / 10;
/* 3420 */     int col2 = moveFrom / 10;
/* 3421 */     return col1 == col2;
/*      */   }
/*      */   
/*      */   public Piece pieceOn(int index)
/*      */   {
/* 3426 */     return Piece.makePiece(index, this.board);
/*      */   }
/*      */   
/*      */   public Piece pieceOn(String string)
/*      */   {
/* 3431 */     int index = decodeSquare(string);
/* 3432 */     return pieceOn(index);
/*      */   }
/*      */   
/*      */   public TransEntry readHashtable(int depth, double extensions)
/*      */   {
/* 3437 */     if (!Options.useTranspositionTable)
/* 3438 */       return null;
/* 3439 */     Info.hashReads += 1;
/* 3440 */     int depthToGo = Info.idDepth + (int)extensions - depth;
/* 3441 */     long z = getZobrist();
/* 3442 */     Long zobi = new Long(z);
/* 3443 */     if (EdenBrain.transpositions.containsKey(zobi))
/*      */     {
/* 3445 */       TransEntry te = (TransEntry)EdenBrain.transpositions.get(zobi);
/* 3446 */       int height = te.getDepth();
/* 3447 */       if (depthToGo > height)
/*      */       {
/* 3449 */         Info.hashDepthMiss += 1;
/* 3450 */         return null;
/*      */       }
/*      */       
/* 3453 */       Info.hashHitCount += 1;
/* 3454 */       return te;
/*      */     }
/*      */     
/*      */ 
/* 3458 */     Info.hashMiss += 1;
/* 3459 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   private void removeIllegalMoves(SortedSet moves)
/*      */   {
/* 3465 */     Collection markForDelete = new TreeSet();
/* 3466 */     for (Iterator it = moves.iterator(); it.hasNext();)
/*      */     {
/* 3468 */       Move move = (Move)it.next();
/* 3469 */       Info.ensureLegalNodes += 1;
/* 3470 */       Position nextPos = createTestPosition(this, move);
/* 3471 */       if (isReceivingCheck())
/*      */       {
/* 3473 */         if (nextPos.isGivingCheck()) {
/* 3474 */           markForDelete.add(move);
/*      */         }
/*      */       } else {
/* 3477 */         int movingPiece = Math.abs(this.board[move.from]);
/* 3478 */         switch (movingPiece)
/*      */         {
/*      */         default: 
/*      */           break;
/*      */         
/*      */         case 6: 
/* 3484 */           if (nextPos.isGivingCheck())
/* 3485 */             markForDelete.add(move);
/* 3486 */           break;
/*      */         
/*      */         case 1: 
/*      */         case 2: 
/*      */         case 3: 
/*      */         case 4: 
/*      */         case 5: 
/* 3493 */           if (nextPos.isGivingCheckNonKingMoving(move.from)) {
/* 3494 */             markForDelete.add(move);
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 3500 */     moves.removeAll(markForDelete);
/* 3501 */     for (Iterator it1 = markForDelete.iterator(); it1.hasNext();)
/*      */     {
/* 3503 */       Move m = (Move)it1.next();
/* 3504 */       for (Iterator it2 = moves.iterator(); it2.hasNext();)
/*      */       {
/* 3506 */         Move m2 = (Move)it2.next();
/* 3507 */         if (m.getText().equals(m2.getText()))
/*      */         {
/* 3509 */           it2.remove();
/* 3510 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setBestMove(Move bestMove)
/*      */   {
/* 3520 */     this.bestMove = bestMove;
/*      */   }
/*      */   
/*      */   public void setBestValue(int bestValue)
/*      */   {
/* 3525 */     this.bestValue = bestValue;
/*      */   }
/*      */   
/*      */   public void setFENPosition(String fenString)
/*      */   {
/* 3530 */     clearBoard();
/* 3531 */     String[] fenFields = fenString.split(" ");
/* 3532 */     String fenPosition = fenFields[0];
/* 3533 */     String[] ranks = fenPosition.split("/");
/* 3534 */     for (int i = 0; i < 8; i++)
/*      */     {
/* 3536 */       String currentRank = ranks[(7 - i)];
/* 3537 */       fillRank(i, currentRank);
/*      */     }
/*      */     
/* 3540 */     String fenOnMove = fenFields[1];
/* 3541 */     if (fenOnMove.equals("w")) {
/* 3542 */       this.onMove = WHITE;
/*      */     }
/* 3544 */     else if (fenOnMove.equals("b"))
/* 3545 */       this.onMove = BLACK;
/* 3546 */     String fenCastling = fenFields[2];
/* 3547 */     this.castleShortWhite = (fenCastling.indexOf('K') != -1);
/* 3548 */     this.castleLongWhite = (fenCastling.indexOf('Q') != -1);
/* 3549 */     this.castleShortBlack = (fenCastling.indexOf('k') != -1);
/* 3550 */     this.castleLongBlack = (fenCastling.indexOf('k') != -1);
/* 3551 */     String fenEnPassant = fenFields[3];
/* 3552 */     if (fenEnPassant.equals("-")) {
/* 3553 */       this.enPassantSquare = 0;
/*      */     } else {
/* 3555 */       this.enPassantSquare = decodeSquare(fenEnPassant);
/*      */     }
/*      */   }
/*      */   
/*      */   public void setOnMove(Boolean onMove) {
/* 3560 */     this.onMove = onMove;
/*      */   }
/*      */   
/*      */   public void setStartPosition(boolean startPosition)
/*      */   {
/* 3565 */     this.isStartPosition = startPosition;
/*      */   }
/*      */   
/*      */   public void setToStartPosition()
/*      */   {
/* 3570 */     clearBoard();
/* 3571 */     addStartPieces();
/* 3572 */     this.castleShortWhite = true;
/* 3573 */     this.castleLongWhite = true;
/* 3574 */     this.castleShortBlack = true;
/* 3575 */     this.castleLongBlack = true;
/* 3576 */     this.enPassantSquare = 0;
/* 3577 */     this.onMove = WHITE;
/* 3578 */     this.isGivingCheck = null;
/* 3579 */     this.isReceivingCheck = null;
/* 3580 */     setStartPosition(true);
/*      */   }
/*      */   
/*      */   public void testClearEnPassantCapture(Move move, int[] board)
/*      */   {
/* 3585 */     if (move.to > move.from)
/*      */     {
/* 3587 */       clearSquare(move.to, -10, board);
/* 3588 */       this.blackPieces.remove(new Integer(move.to - 10));
/*      */     }
/*      */     else {
/* 3591 */       clearSquare(move.to, 10, board);
/* 3592 */       this.whitePieces.remove(new Integer(move.to + 10));
/*      */     }
/*      */   }
/*      */   
/*      */   public String toString()
/*      */   {
/* 3598 */     return this.board.toString();
/*      */   }
/*      */   
/*      */   private boolean tryMoveNonCapture(Set moves, int from, int next, Boolean color)
/*      */   {
/* 3603 */     if (invalidSquare(next))
/* 3604 */       return true;
/* 3605 */     int type = this.board[next];
/* 3606 */     if (type != 0)
/*      */     {
/* 3608 */       return true;
/*      */     }
/*      */     
/* 3611 */     Move m = new Move(this, from, next, 0, type);
/* 3612 */     moves.add(m);
/* 3613 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public int typeOn(int nextIndex)
/*      */   {
/* 3619 */     return this.board[nextIndex];
/*      */   }
/*      */   
/*      */   private void updateCaptureMove()
/*      */   {
/* 3624 */     if (this.nextCaptureMove != null)
/* 3625 */       return;
/* 3626 */     Boolean color = null;
/* 3627 */     boolean finished = false;
/* 3628 */     while (!finished)
/*      */     {
/* 3630 */       if (this.persistentSquareForCapture > 88)
/* 3631 */         return;
/* 3632 */       int p = this.board[this.persistentSquareForCapture];
/* 3633 */       int type = Math.abs(p);
/* 3634 */       if ((type < 1) || (type > 6))
/*      */       {
/* 3636 */         this.persistentSquareForCapture += 1;
/*      */       }
/*      */       else {
/* 3639 */         color = EdenBrain.convertColor(p);
/* 3640 */         if (!color.equals(onMove()))
/*      */         {
/* 3642 */           this.persistentSquareForCapture += 1;
/*      */         }
/*      */         else {
/* 3645 */           Move nextCapture = generateNextCaptureMove(this.persistentSquareForCapture, p, color);
/* 3646 */           if (nextCapture != null)
/*      */           {
/* 3648 */             this.nextCaptureMove = nextCapture;
/* 3649 */             finished = true;
/* 3650 */             break;
/*      */           }
/* 3652 */           initializeCaptureMoveCounters();
/* 3653 */           this.persistentSquareForCapture += 1;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/* 3659 */   private void updateNonCaptureMove() { if (this.nextNonCaptureMove != null)
/* 3660 */       return;
/* 3661 */     Boolean color = null;
/* 3662 */     boolean finished = false;
/* 3663 */     while (!finished)
/*      */     {
/* 3665 */       if (this.persistentSquareForNonCapture > 88)
/* 3666 */         return;
/* 3667 */       int p = this.board[this.persistentSquareForNonCapture];
/* 3668 */       int type = Math.abs(p);
/* 3669 */       if ((type < 1) || (type > 6))
/*      */       {
/* 3671 */         this.persistentSquareForNonCapture += 1;
/*      */       }
/*      */       else {
/* 3674 */         color = EdenBrain.convertColor(p);
/* 3675 */         if (!color.equals(onMove()))
/*      */         {
/* 3677 */           this.persistentSquareForNonCapture += 1;
/*      */         }
/*      */         else {
/* 3680 */           Move nextNonCapture = generateNextNonCaptureMove(this.persistentSquareForNonCapture, p, color);
/* 3681 */           if (nextNonCapture != null)
/*      */           {
/* 3683 */             this.nextNonCaptureMove = nextNonCapture;
/* 3684 */             finished = true;
/* 3685 */             break;
/*      */           }
/* 3687 */           initializeNonCaptureMoveCounters();
/* 3688 */           this.persistentSquareForNonCapture += 1;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/* 3694 */   private long updatePawnZobristForMove(Move move, long pzobrist) { int sourceType = this.board[move.from];
/* 3695 */     long zoFrom = 0L;
/* 3696 */     long zoTo = 0L;
/* 3697 */     if ((sourceType == 1) || (sourceType == -1))
/*      */     {
/* 3699 */       zoFrom = kleinerZobristEntfernen(move.from);
/* 3700 */       if (move.promotedTo == 0)
/* 3701 */         zoTo = kleinerZobristSetzen(sourceType, move.to);
/*      */     }
/* 3703 */     long zoKick = 0L;
/* 3704 */     if ((this.board[move.to] == 1) || (this.board[move.to] == -1))
/* 3705 */       zoKick = kleinerZobristEntfernen(move.to);
/* 3706 */     pzobrist ^= zoFrom;
/* 3707 */     pzobrist ^= zoTo;
/* 3708 */     pzobrist ^= zoKick;
/* 3709 */     return pzobrist;
/*      */   }
/*      */   
/*      */   private void updateZobristEnPassant()
/*      */   {
/* 3714 */     this.zobrist ^= EdenBrain.enPassantZobrist;
/*      */   }
/*      */   
/*      */   private long updateZobristForMove(Move move, long z)
/*      */   {
/* 3719 */     int sourceType = this.board[move.from];
/* 3720 */     long zoFrom = kleinerZobristEntfernen(move.from);
/* 3721 */     long zoKick = 0L;
/* 3722 */     if (move.capturedPiece != 0)
/* 3723 */       zoKick = kleinerZobristEntfernen(move.to);
/* 3724 */     long zoTo = kleinerZobristSetzen(sourceType, move.to);
/* 3725 */     if (move.promotedTo != 0)
/*      */     {
/* 3727 */       int sgn = Math.abs(sourceType) / sourceType;
/* 3728 */       int pieceToPromote = sgn * move.promotedTo;
/* 3729 */       zoTo = kleinerZobristSetzen(pieceToPromote, move.to);
/*      */     }
/* 3731 */     long zoRookJumpFrom = 0L;
/* 3732 */     long zoRookJumpTo = 0L;
/* 3733 */     if (move.isCastling(Math.abs(this.board[move.from]))) {
/*      */       int rookJumpTo;
/*      */       int rookJumpFrom;
/*      */       int rookJumpTo;
/* 3737 */       if (move.to % 10 == 7)
/*      */       {
/* 3739 */         int rookJumpFrom = move.to + 1;
/* 3740 */         rookJumpTo = move.to - 1;
/*      */       }
/*      */       else {
/* 3743 */         rookJumpFrom = move.to - 2;
/* 3744 */         rookJumpTo = move.to + 1;
/*      */       }
/* 3746 */       zoRookJumpFrom = kleinerZobristEntfernen(rookJumpFrom);
/* 3747 */       zoRookJumpTo = kleinerZobristSetzen(this.board[rookJumpFrom], rookJumpTo);
/*      */     }
/* 3749 */     z ^= zoFrom;
/* 3750 */     z ^= zoTo;
/* 3751 */     z ^= zoRookJumpFrom;
/* 3752 */     z ^= zoRookJumpTo;
/* 3753 */     z ^= zoKick;
/* 3754 */     z ^= EdenBrain.onMoveZobrist;
/* 3755 */     return z;
/*      */   }
/*      */   
/*      */   public void writeHashtable(Move move, int depth, double extensions, int value, int originalAlpha, int beta)
/*      */   {
/* 3760 */     assert (value < 99999);
/*      */     
/* 3762 */     if (!Options.useTranspositionTable)
/* 3763 */       return;
/* 3764 */     if (move == null)
/*      */     {
/* 3766 */       System.out.println("debug: move==null: movestack:" + EdenBrain.moveStack);
/* 3767 */       RuntimeException e = new IllegalStateException("move = null");
/* 3768 */       e.printStackTrace();
/* 3769 */       if (Options.DEBUG)
/* 3770 */         throw e;
/*      */     }
/* 3772 */     int depthToGo = Info.idDepth + (int)extensions - depth;
/* 3773 */     long z = getZobrist();
/* 3774 */     Long zobi = new Long(z);
/* 3775 */     ValidFlag validFlag = new ValidFlag();
/* 3776 */     if ((value > originalAlpha) && (value < beta)) {
/* 3777 */       validFlag.setNr(1);
/*      */     }
/* 3779 */     else if (value >= beta) {
/* 3780 */       validFlag.setNr(4);
/*      */     }
/* 3782 */     else if (value <= originalAlpha)
/*      */     {
/* 3784 */       validFlag.setNr(2);
/* 3785 */       move = null;
/*      */     }
/* 3787 */     TransEntry te = new TransEntry(move, value, depthToGo, z, validFlag);
/* 3788 */     Info.hashFull = EdenBrain.transpositions.size() / (Options.MAX_TRANSPOSITIONS / 1000);
/* 3789 */     if (Info.hashFull < 1000) {
/* 3790 */       EdenBrain.transpositions.put(zobi, te);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3802 */   static int[] bishopSquareValues = {
/* 3803 */     0, -15, -10, -8, -10, -10, -9, -10, -15, 
/* 3804 */     0, 0, -7, 1, -1, 2, 3, -1, 2, -9, 
/* 3805 */     0, 0, 0, -1, -2, 3, 4, -1, 0, 1, 
/* 3806 */     0, 0, 1, 5, 8, 6, 6, 8, 5, 1, 
/* 3807 */     0, 0, 1, 5, 8, 6, 6, 8, 5, 1, 
/* 3808 */     0, 0, 0, -1, -2, 3, 4, -1, 0, 1, 
/* 3809 */     0, 0, -7, 1, -1, 2, 3, -1, 2, -9, 
/* 3810 */     0, 0, -15, -10, -8, -10, -10, -9, -10, -15 };
/*      */   
/*      */ 
/*      */ 
/* 3814 */   public static int[] blackPawnSquareValues = {
/* 3815 */     0, 16, 22, 35, 45, 45, 35, 22, 16, 
/* 3816 */     0, 0, 7, 14, 20, 24, 24, 20, 14, 7, 
/* 3817 */     0, 0, 3, 4, 5, 6, 8, 5, 4, 3, 
/* 3818 */     0, 0, 0, 0, 0, 6, 8, 
/* 3819 */     0, 0, 0, 0, 0, -2, 1, -1, 2, 1, -2, 1, -2, 
/* 3820 */     0, 0, 2, 2, 2, -6, -6, 3, 2, 2 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3830 */   private int[] kingEndgameSquareValues = {
/* 3831 */     0, -5, -3, 1, 2, 2, 1, -3, -5, 
/* 3832 */     0, 0, -3, 1, 2, 3, 3, 2, 1, -3, 
/* 3833 */     0, 0, 1, 2, 3, 4, 4, 3, 2, 1, 
/* 3834 */     0, 0, 2, 3, 4, 5, 5, 4, 3, 2, 
/* 3835 */     0, 0, 2, 3, 4, 5, 5, 4, 3, 2, 
/* 3836 */     0, 0, 1, 2, 3, 4, 4, 3, 2, 1, 
/* 3837 */     0, 0, -3, 1, 2, 3, 3, 2, 1, -3, 
/* 3838 */     0, 0, -5, -3, 1, 2, 2, 1, -3, -5 };
/*      */   
/* 3840 */   private int[] kingEndgameSquareValuesKBNKLight = {
/* 3841 */     0, -50, -40, -30, -20, -15, -8, -5, 
/* 3842 */     0, 0, 0, -40, 1, 2, 3, 3, 2, 1, -5, 
/* 3843 */     0, 0, -30, 2, 3, 4, 4, 3, 2, -8, 
/* 3844 */     0, 0, -20, 3, 4, 5, 5, 4, 3, -15, 
/* 3845 */     0, 0, -15, 3, 4, 5, 5, 4, 3, -20, 
/* 3846 */     0, 0, -8, 2, 3, 4, 4, 3, 2, -30, 
/* 3847 */     0, 0, -5, 1, 2, 3, 3, 2, 1, -40, 
/* 3848 */     0, 0, 0, -5, -6, -7, -8, -10, -40, -50 };
/*      */   
/* 3850 */   private int[] kingEndgameSquareValuesKBNKDark = {
/* 3851 */     0, 0, -5, -8, -15, -20, -30, -40, -50, 
/* 3852 */     0, 0, -5, 1, 2, 3, 3, 2, 1, -40, 
/* 3853 */     0, 0, -8, 2, 3, 4, 4, 3, 2, -30, 
/* 3854 */     0, 0, -15, 3, 4, 5, 5, 4, 3, -20, 
/* 3855 */     0, 0, -20, 3, 4, 5, 5, 4, 3, -15, 
/* 3856 */     0, 0, -30, 2, 3, 4, 4, 3, 2, -8, 
/* 3857 */     0, 0, -40, 1, 2, 3, 3, 2, 1, -5, 
/* 3858 */     0, 0, -50, -40, -30, -20, -15, -8, -5 };
/*      */   
/*      */ 
/* 3861 */   static int[] knightSquareValues = {
/* 3862 */     0, -31, -4, -4, -4, -4, -4, -8, -31, 
/* 3863 */     0, 0, -9, -4, -2, -1, -2, -2, -4, -9, 
/* 3864 */     0, 0, -5, 0, 3, 1, 1, 4, 0, -5, 
/* 3865 */     0, 0, -4, 1, 2, 8, 8, 2, 1, -5, 
/* 3866 */     0, 0, -4, 1, 2, 8, 8, 2, 1, -5, 
/* 3867 */     0, 0, -5, 0, 3, 1, 1, 4, 0, -5, 
/* 3868 */     0, 0, -9, -4, -2, -1, -2, -2, -4, -9, 
/* 3869 */     0, 0, -31, -4, -4, -4, -4, -4, -8, -31 };
/*      */   
/*      */ 
/* 3872 */   static int[] pieceValues = {
/* 3873 */     100, 325, 325, 500, 975, 250 };
/*      */   
/*      */ 
/*      */ 
/* 3877 */   public static int[] whitePawnSquareValues = {
/* 3878 */     0, 16, 22, 35, 45, 45, 35, 22, 16, 
/* 3879 */     0, 0, 7, 14, 20, 24, 24, 20, 14, 7, 
/* 3880 */     0, 0, 3, 4, 5, 8, 6, 5, 4, 3, 
/* 3881 */     0, 0, 0, 0, 0, 8, 6, 
/* 3882 */     0, 0, 0, 0, 0, -2, 1, -2, 2, 1, -1, 1, -2, 
/* 3883 */     0, 0, 2, 2, 3, -6, -6, 2, 2, 2 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3916 */   private int[] passedPawnProgression = {
/* 3917 */     7, 14, 27, 38, 45, 60 };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3932 */   private static final Map pawnHash = new HashMap(256000, 1.0F);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3938 */   public static final Boolean WHITE = Boolean.valueOf(true);
/* 3939 */   public static final Boolean BLACK = Boolean.valueOf(!WHITE.booleanValue());
/*      */   private static final int MAX_BOARD_SIZE = 89;
/*      */   public int[] board;
/*      */   public static final int WK = 6;
/*      */   public static final int WN = 2;
/*      */   public static final int WP = 1;
/*      */   public static final int WQ = 5;
/*      */   public static final int WB = 3;
/*      */   static final int WR = 4;
/*      */   public static final int BB = -3;
/*      */   public static final int BISHOP = 3;
/*      */   public static final int BK = -6;
/*      */   public static final int BN = -2;
/*      */   public static final int BOARD_SIZE = 89;
/*      */   public static final int BP = -1;
/*      */   public static final int BQ = -5;
/*      */   static final int BR = -4;
/*      */   public static final int EMPTY = 0;
/*      */   public static final int INVALID = Integer.MAX_VALUE;
/*      */   public static final int KING = 6;
/*      */   public static final int KNIGHT = 2;
/*      */   public static final int PAWN = 1;
/*      */   public static final int QUEEN = 5;
/*      */   public static final int ROOK = 4;
/*      */   private Move bestMove;
/*      */   private int bestValue;
/*      */   private int bishopCaptureCount;
/*      */   private SortedSet bishopCaptures;
/*      */   private int bishopNonCaptureCount;
/*      */   private SortedSet bishopNonCaptures;
/*      */   private int blackKing;
/*      */   SortedSet blackPieces;
/*      */   public SortedSet captureMoves;
/*      */   boolean castleLongBlack;
/*      */   boolean castleLongWhite;
/*      */   boolean castleShortBlack;
/*      */   boolean castleShortWhite;
/*      */   int enPassantSquare;
/*      */   private boolean hasCastledBlack;
/*      */   private boolean hasCastledWhite;
/*      */   private Boolean isEndGame;
/*      */   private Boolean isGivingCheck;
/*      */   private Boolean isReceivingCheck;
/*      */   boolean isStartPosition;
/*      */   private SortedSet kingCaptures;
/*      */   private SortedSet kingNonCaptures;
/*      */   private SortedSet knightCaptures;
/*      */   private SortedSet knightNonCaptures;
/*      */   private int lastPawnCapture;
/*      */   private int lastPawnNonCapture;
/*      */   private SortedSet legalMoves;
/*      */   int moveNr;
/*      */   private Move nextCaptureMove;
/*      */   private Move nextNonCaptureMove;
/*      */   private Boolean onMove;
/*      */   private int persistentSquareForCapture;
/*      */   private int persistentSquareForNonCapture;
/*      */   private SortedSet queenCaptures;
/*      */   private SortedSet queenNonCaptures;
/*      */   private SortedSet rookCaptures;
/*      */   private SortedSet rookNonCaptures;
/*      */   private int whiteKing;
/*      */   SortedSet whitePieces;
/*      */   private long zobrist;
/*      */   private Long pzCache;
/*      */   private Long pawnZobrist;
/*      */   private PieceCount wpc;
/*      */   private PieceCount bpc;
/*      */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/Position.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */