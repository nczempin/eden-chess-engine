 package de.czempin.chess.eden.brain;
 
 import de.czempin.chess.Engine;
 import de.czempin.chess.Info;
 import de.czempin.chess.Options;
 import de.czempin.chess.Variation;
 import de.czempin.chess.eden.Eden;
 import de.czempin.chess.eden.Move;

 import java.io.PrintStream;
 import java.util.HashMap;
 import java.util.Iterator;
 import java.util.Map;
 import java.util.Random;
 import java.util.Set;
 import java.util.SortedMap;
 import java.util.SortedSet;
 import java.util.Stack;
 import java.util.TreeSet;
 import java.util.Vector;
 
 public class EdenBrain extends de.czempin.chess.AbstractBrain
 {
   public static final String NAME = "Eden";
   
   public EdenBrain(Engine engine)
   {
     this.engine = engine;
   }
   
   private int alphabeta(int depth, double extension, Position position, Move move, int originalAlpha, int beta, Vector upPv, int checkExtensions, boolean justExtended)
   {
     if (move == null)
       throw new IllegalStateException("move is null!");
     Move bestMove = null;
     int alpha = originalAlpha;
     int value = 666662;
     withinAlphabeta = true;
     if (timeUp()) {
       timeIsUp = true;
       return 0;
     }
Position nextPos;
     try
     {
       Info.ab_nodes += 1;
       nextPos = Position.createPosition(position, move);
     } catch (ThreeRepetitionsAB e) {
       System.out.println("debug: three repetitions!");
       return 0; }
     if ((!justExtended) && (Options.useCheckExtensions) && (checkExtensions < 4) && (Info.idDepth > 4) && (nextPos.isReceivingCheck()))
       return checkExtension(depth, extension, position, move, originalAlpha, beta, upPv, checkExtensions, true);
     Set markForDelete = new java.util.HashSet();
     int retVal = 0;
     int loopCount = 0;
     kingCapture = false;
     TransEntry te = nextPos.readHashtable(depth, extension);
     Move hashMove = null;
     int v = 666668;
     if (te != null) {
       hashMove = te.getMove();
       Info.countValidAttempts += 1;
       int validFlag = te.getValidFlag().getNr();
       int hashValue = te.getValue();
       if (hashMove == null) {
         if ((validFlag == 2) && (hashValue < beta)) {
           Info.hashCutoffs += 1;
           return -hashValue;
         }
       } else {
         int from = hashMove.from;
         if (nextPos.board[from] == 0) {
           hashMove = null;
         } else {
           Vector downPv = new Vector();
           if (validFlag == 1) {
             Info.countValidFlag += 1;
             loopCount++;
             value = hashValue;
             if (value >= beta) {
               Info.hashCutoffs += 1;
               retVal = -beta;
               return retVal;
             }
             alpha = value;
             upPv.clear();
             upPv.add(hashMove);
             upPv.addAll(downPv);
             bestMove = hashMove;
           } else {
             if (validFlag == 4) {
               if (hashValue >= beta) {
                 Info.hashCutoffs += 1;
                 return -hashValue;
               }
             } else if ((validFlag == 2) && (hashValue < beta)) {
               Info.hashCutoffs += 1;
               return -hashValue;
             }
             moveStack.push(hashMove);
             v = alphabeta(depth + 1, extension, nextPos, hashMove, -beta, -alpha, downPv, checkExtensions, false);
             assert (v < 99999);
             
             assert (v > -99999);
             
             moveStack.pop();
             if (kingCapture) {
               illegalCount += 1;
               hashMove = null;
               kingCapture = false;
             } else {
               loopCount++;
               value = v;
             }
             if (value >= beta) {
               Info.hashCutoffs += 1;
               retVal = -beta;
               return retVal;
             }
             alpha = value;
             upPv.clear();
             upPv.add(hashMove);
             upPv.addAll(downPv);
             bestMove = hashMove;
           }
         }
       }
     }
     if (depth >= Info.idDepth + extension) {
       if (Options.useQuiescence) {
         Vector downPv = new Vector();
         value = quiescence_alphabeta(depth, position, move, alpha, beta, downPv, nextPos);
         upPv.clear();
         upPv.addAll(downPv);
         return value;
       }
       if (nextPos.isGivingCheck()) {
         illegalCount += 1;
         kingCapture = true;
         return -666661;
       }
       value = nextPos.getValue();
       return -value;
     }
     
     int loop2 = 0;
     SortedSet captureMoves = nextPos.generateCaptureMoves();
     Iterator it = captureMoves.iterator();
     SortedSet deferredMoves = new TreeSet();
     while (it.hasNext()) {
       Move newMove = (Move)it.next();
       if ((hashMove == null) || (!newMove.equals(hashMove))) {
         int capture = newMove.capturedPiece;
         int captureAbs = Math.abs(capture);
         int movingAbs = Math.abs(newMove.movingPiece);
         if (captureAbs == 6) {
           illegalCount += 1;
           kingCapture = true;
           return -666666;
         }
         if (timeUp()) {
           timeIsUp = true;
           return 0;
         }
         moveStack.push(newMove);
         Vector downPv = new Vector();
         v = alphabeta(depth + 1, extension, nextPos, newMove, -beta, -alpha, downPv, checkExtensions, false);
         moveStack.pop();
         if (kingCapture) {
           illegalCount += 1;
           markForDelete.add(newMove);
           kingCapture = false;
         } else {
           if (bestMove == null)
             bestMove = newMove;
           loopCount++;
           loop2++;
           value = v;
           if (value >= beta) {
             Info.betaCutoffs += 1;
             if (loop2 == 1)
               Info.betaCutoffsFirstMove += 1;
             Info.pieceCapturingCutoffs[movingAbs] += 1;
             Info.pieceCapturedCutoffs[captureAbs] += 1;
             nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
             retVal = -beta;
             return retVal;
           }
           if (value > alpha) {
             alpha = value;
             upPv.clear();
             upPv.add(newMove);
             upPv.addAll(downPv);
             if (alpha > 80000) {
               nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
               return -alpha;
             }
             bestMove = newMove;
           }
         }
       }
     }
     SortedSet nonCaptureMoves = new TreeSet();
     nonCaptureMoves = nextPos.generateNonCaptureMoves();
     Move killerMove1 = checkKillerMove1(depth);
     if ((killerMove1 != null) && (!killerMove1.equals(hashMove))) {
       Iterator itx = nonCaptureMoves.iterator();
       boolean killerIsLegal = false;
       while (itx.hasNext()) {
         Move m = (Move)itx.next();
         if (m.getText().equals(killerMove1.getText())) {
           killerIsLegal = true;
           break;
         }
       }
       if (!killerIsLegal) {
         for (itx = deferredMoves.iterator(); itx.hasNext();) {
           Move m = (Move)itx.next();
           if (m.getText().equals(killerMove1.getText())) {
             killerIsLegal = true;
             break;
           }
         }
       }
       if (!killerIsLegal) {
         killerMove1 = null;
       } else {
         moveStack.push(killerMove1);
         Vector downPv = new Vector();
         value = alphabeta(depth + 1, extension, nextPos, killerMove1, -beta, -alpha, downPv, checkExtensions, false);
         moveStack.pop();
         if (kingCapture) {
           illegalCount += 1;
           killerMove1 = null;
           kingCapture = false;
         } else {
           loopCount++;
           Info.killerHits += 1;
           if (value >= beta) {
             Info.killerCutoffs += 1;
             nextPos.writeHashtable(killerMove1, depth, extension, value, originalAlpha, beta);
             retVal = -beta;
             return retVal;
           }
           if (bestMove == null)
             bestMove = killerMove1;
           if (value > alpha) {
             alpha = value;
             upPv.clear();
             upPv.add(killerMove1);
             upPv.addAll(downPv);
           }
         }
       }
     }
     Move killerMove2 = checkKillerMove2(depth);
     if ((killerMove2 != null) && (!killerMove2.equals(hashMove))) {
       Iterator itx = nonCaptureMoves.iterator();
       boolean killerIsLegal = false;
       while (itx.hasNext()) {
         Move m = (Move)itx.next();
         if (m.getText().equals(killerMove2.getText())) {
           killerIsLegal = true;
           break;
         }
       }
       if (!killerIsLegal) {
         for (itx = deferredMoves.iterator(); itx.hasNext();) {
           Move m = (Move)itx.next();
           if (m.getText().equals(killerMove2.getText())) {
             killerIsLegal = true;
             break;
           }
         }
       }
       if (!killerIsLegal) {
         killerMove2 = null;
       } else {
         moveStack.push(killerMove2);
         Vector downPv = new Vector();
         value = alphabeta(depth + 1, extension, nextPos, killerMove2, -beta, -alpha, downPv, checkExtensions, false);
         moveStack.pop();
         if (kingCapture) {
           illegalCount += 1;
           killerMove2 = null;
           kingCapture = false;
         } else {
           loopCount++;
           Info.killerHits += 1;
           if (bestMove == null)
             bestMove = killerMove2;
           if (value >= beta) {
             Info.killerCutoffs += 1;
             nextPos.writeHashtable(killerMove2, depth, extension, value, originalAlpha, beta);
             retVal = -beta;
             return retVal;
           }
           if (value > alpha) {
             alpha = value;
             upPv.clear();
             upPv.add(killerMove2);
             upPv.addAll(downPv);
           }
         }
       }
     }
     for (Iterator it2 = nonCaptureMoves.iterator(); it2.hasNext();) {
       Move newMove = (Move)it2.next();
       if (((hashMove == null) || (!newMove.equals(hashMove))) && ((killerMove1 == null) || (!newMove.equals(killerMove1))) && (
         (killerMove2 == null) || (!newMove.equals(killerMove2)))) {
         int capture = newMove.capturedPiece;
         int captureAbs = Math.abs(capture);
         int movingAbs = Math.abs(newMove.movingPiece);
         if (captureAbs == 6) {
           illegalCount += 1;
           kingCapture = true;
           return -666667;
         }
         if (timeUp()) {
           timeIsUp = true;
           return 0;
         }
         moveStack.push(newMove);
         Vector downPv = new Vector();
         v = alphabeta(depth + 1, extension, nextPos, newMove, -beta, -alpha, downPv, checkExtensions, false);
         assert (v < 99999);
         
         assert (v != -99999);
         
         moveStack.pop();
         if (kingCapture) {
           illegalCount += 1;
           markForDelete.add(newMove);
           kingCapture = false;
         } else {
           if (bestMove == null)
             bestMove = newMove;
           loopCount++;
           value = v;
           assert (value < 99999);
           
           loop2++;
           if (value >= beta) {
             Info.betaCutoffs += 1;
             if (loop2 == 1)
               Info.betaCutoffsFirstMove += 1;
             Info.pieceMovingCutoffs[movingAbs] += 1;
             nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
             addToKillers(depth, newMove);
             retVal = -beta;
             return retVal;
           }
           if (value > alpha) {
             upPv.clear();
             upPv.add(newMove);
             upPv.addAll(downPv);
             if (!newMove.equals(hashMove)) {
               nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
               addToKillers(depth, newMove);
             }
             if (alpha > 80000) {
               nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
               return -value;
             }
             alpha = value;
             bestMove = newMove;
           }
         }
       }
     }
     
     for (Iterator it3 = deferredMoves.iterator(); it3.hasNext();) {
       Move newMove = (Move)it3.next();
       if (((hashMove == null) || (!newMove.equals(hashMove))) && ((killerMove1 == null) || (!newMove.equals(killerMove1))) && (
         (killerMove2 == null) || (!newMove.equals(killerMove2)))) {
         int capture = newMove.capturedPiece;
         int captureAbs = Math.abs(capture);
         int movingAbs = Math.abs(newMove.movingPiece);
         if (captureAbs == 6) {
           illegalCount += 1;
           kingCapture = true;
           return -666665;
         }
         if (timeUp()) {
           timeIsUp = true;
           return 0;
         }
         moveStack.push(newMove);
         Vector downPv = new Vector();
         v = alphabeta(depth + 1, extension, nextPos, newMove, -beta, -alpha, downPv, checkExtensions, false);
         moveStack.pop();
         if (kingCapture) {
           illegalCount += 1;
           markForDelete.add(newMove);
           kingCapture = false;
         } else {
           if (bestMove == null)
             bestMove = newMove;
           loopCount++;
           value = v;
           loop2++;
           if (value >= beta) {
             Info.betaCutoffs += 1;
             if (loop2 == 1)
               Info.betaCutoffsFirstMove += 1;
             Info.pieceCapturingCutoffs[movingAbs] += 1;
             Info.pieceCapturedCutoffs[captureAbs] += 1;
             nextPos.writeHashtable(bestMove, depth, extension, value, originalAlpha, beta);
             retVal = -beta;
             return retVal;
           }
           if (value > alpha) {
             alpha = value;
             upPv.clear();
             upPv.add(newMove);
             upPv.addAll(downPv);
             if (!newMove.equals(hashMove))
               addToKillers(depth, newMove);
             if (alpha > 80000) {
               nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
               return -alpha;
             }
             bestMove = newMove;
           }
         }
       }
     }
     
     SortedSet moves = new TreeSet();
     moves.addAll(captureMoves);
     moves.addAll(nonCaptureMoves);
     moves.removeAll(markForDelete);
     if ((loopCount == 0) || (moves.size() == 0)) {
       if (nextPos.isReceivingCheck()) {
         value = -(90000 - depth);
         return -value;
       }
       return 0;
     }
     if ((loopCount == 1) || (moves.size() == 1)) {
       if (bestMove == null) {
         bestMove = (Move)moves.first();
         if (bestMove == null)
           throw new IllegalStateException("move is null!");
       }
       Move onlyMove = bestMove;
       alpha = forcedMoveExtension(depth, extension, originalAlpha, beta, upPv, nextPos, onlyMove, alpha, checkExtensions);
       bestMove = onlyMove;
     }
     addToKillers(depth, bestMove);
     nextPos.writeHashtable(bestMove, depth, extension, alpha, originalAlpha, beta);
     return -alpha;
   }
   
 
   private int pawnSeventhExtension(int depth, double extension, Position position, Move move, int originalAlpha, int beta, Vector upPv, int checkExtensions)
   {
     double pawnSeventhExtension = extension + 1.0D;
     return alphabeta(depth, pawnSeventhExtension, position, move, originalAlpha, beta, upPv, checkExtensions, true);
   }
   
   private int checkExtension(int depth, double extension, Position position, Move move, int originalAlpha, int beta, Vector upPv, int checkExtensions, boolean b)
   {
     double checkExtension = extension + 0.9D;
     int value = alphabeta(depth, checkExtension, position, move, originalAlpha, beta, upPv, checkExtensions + 1, true);
     return value;
   }
   
   private int forcedMoveExtension(int depth, double extension, int originalAlpha, int beta, Vector upPv, Position nextPos, Move onlyMove, int originalValue, int checkExtensions)
   {
     if (!Options.useForcedMoveExtension)
       return originalValue;
     moveStack.push(onlyMove);
     Vector downPv = new Vector();
     extension += 1.501D;
     int value = alphabeta(depth + 1, extension, nextPos, onlyMove, -beta, -originalAlpha, downPv, checkExtensions, false);
     assert (value < 99999);
     
     assert (value > -99999);
     
 
     moveStack.pop();
     upPv.clear();
     upPv.add(onlyMove);
     upPv.addAll(downPv);
     return value;
   }
   
   public long calculateTimePerMove(long t, long inc)
   {
     if ((t == 0L) && (inc == 0L))
       return -1L;
     int slice = this.engine.getMovestogo() + 1;
     long retVal = (t - inc) / slice + inc - 500L;
     if (retVal < 100L)
       retVal = 100L;
     return retVal;
   }
   
   static Long getZobrist(Position sp) {
     return new Long(sp.getZobrist());
   }
   
   public void initializeGame() {
     position.setToStartPosition();
     transpositions.clear();
     threeDrawsTable.clear();
     OpeningBook.prepareOpeningBook();
     initializeKillerMoves();
   }
   
   private static void initializeKillerMoves() {
     for (int i = 0; i < killerMoves1.length; i++) {
       killerMoves1[i] = null;
       killerMoves2[i] = null;
     }
     
     if (Options.DEBUG) {
       System.out.println("debug: killer moves initialized.");
     }
   }
   
 
   public String move()
   {
     if (Options.DEBUG)
       System.out.println("debug: EdenBrain.move()");
     Info.nodes = 0L;
     Info.seldepth = 0;
     illegalCount = 0;
     Info.enPriseNodes = 0;
     Info.qs_nodes = 0L;
     Info.ab_nodes = 0;
     Info.drawCountNodes = 0;
     Info.castlingNodes = 0;
     Info.quiescentMateCheckNodes = 0;
     Info.hashReads = 0;
     Info.hashHitCount = 0;
     Info.hashMiss = 0;
     Info.hashDepthMiss = 0;
     System.gc();
     Info.searchtime = System.currentTimeMillis();
     Eden.calculateTimePerMove();
     Move move = analyze();
     if (move == null)
       return "Can't move!\n";
     try {
       position.makeMove(move);
     } catch (ThreeRepetitionsAB e) {
       e.printStackTrace();
     }
     if (Options.DEBUG)
       System.out.println("debug: returning move: " + move);
     return move.getText();
   }
   
   private static void updateMultiPv(Move move, Vector variation, int value, int depth) {
     String key = move.getText();
     int size = variation.size();
     Vector variationStrings = new Vector(size);
     for (int i = 0; i < size; i++) {
       Move m = (Move)variation.get(i);
       variationStrings.add(m.getText());
     }
     
     Variation v2 = new Variation(move.getText(), variationStrings, value, depth);
     Eden.multiPvs.clear();
     Eden.multiPvs.put(key, v2);
   }
   
   public String extractVariationString(Vector variation) {
     Iterator it1 = variation.iterator();
     
     String m;
String pv;
     for (pv = ""; it1.hasNext(); pv = pv + m + " ") {
       m = (String)it1.next();
     }
     return pv;
   }
   
   private int quiescence_alphabeta(int depth, Position position, Move move, int originalAlpha, int beta, Vector upPv, Position nextPos)
   {
     int alpha = originalAlpha;
     ValidFlag bestMoveValidFlag = new ValidFlag();
     if ((timeUp()) || (timeIsUp)) {
       timeIsUp = true;
       return 0;
     }
     if (depth > Info.seldepth)
       Info.seldepth = depth;
     if (nextPos == null)
       try {
         Info.qs_nodes += 1L;
         nextPos = Position.createPosition(position, move);
       } catch (ThreeRepetitionsAB e) {
         System.err.println("three repetitions. what to do?");
         return 0;
       }
     int v = nextPos.getValue();
     if (v >= beta)
       return -beta;
     if (v > alpha) {
       bestMoveValidFlag.setNr(1);
       alpha = v;
     }
     kingCapture = false;
     int loopCount = 0;
     nextPos.initCaptureMoveGenerator();
     while (nextPos.hasNextCaptureMove()) {
       Move newMove = nextPos.nextCaptureMove();
       int capture = Math.abs(newMove.capturedPiece);
       int capturing = Math.abs(position.board[newMove.from]);
       if (capture == 6) {
         kingCapture = true;
         illegalCount += 1;
         return -666663;
       }
       if (!shouldBeIgnored(nextPos, newMove, capture, capturing)) {
         moveStack.push(newMove);
         Vector downPv = new Vector();
         int value = quiescence_alphabeta(depth + 1, nextPos, newMove, -beta, -alpha, downPv, null);
         if (kingCapture) {
           illegalCount += 1;
           moveStack.pop();
           kingCapture = false;
         } else {
           loopCount++;
           if (value >= beta) {
             moveStack.pop();
             return -beta;
           }
           bestMoveValidFlag.setNr(-1);
           if (value > alpha) {
             alpha = value;
             upPv.clear();
             upPv.add(newMove);
             upPv.addAll(downPv);
           }
           moveStack.pop();
         }
       }
     }
     if (loopCount == 0) {
       nextPos.initNonCaptureMoveGenerator();
       boolean legal = false;
       while (nextPos.hasNextNonCaptureMove()) {
         Move testMove = nextPos.nextNonCaptureMove();
         Info.quiescentMateCheckNodes += 1;
         Position tp = Position.createTestPosition(nextPos, testMove);
         if (!tp.isGivingCheck()) {
           legal = true;
           break;
         }
       }
       if (!legal) {
         SortedSet lMoves = nextPos.generateLegalMoves();
         if (lMoves.size() == 0) {
           if (nextPos.isMate()) {
             int value = -90000 + depth;
             return -value;
           }
           return 0;
         }
       }
       alpha = v;
     }
     return -alpha;
   }
   
   private static long randomZobrist() {
     return r.nextLong();
   }
   
   private static boolean shouldBeIgnored(Position nextPos, Move newMove, int capture, int capturing) {
     return (capturing > capture) && ((capturing != 3) || (capture != 2)) && (!nextPos.enPrise(newMove));
   }
   
   private boolean timeUp() {
     return this.engine.timeUp();
   }
   
   static Boolean convertColor(int p) {
     Boolean retVal = null;
     switch (p) {
     case 0: 
       return null;
     
     case -6: 
     case -5: 
     case -4: 
     case -3: 
     case -2: 
     case -1: 
       retVal = Position.BLACK;
       break;
     
     case 1: 
     case 2: 
     case 3: 
     case 4: 
     case 5: 
     case 6: 
       retVal = Position.WHITE;
       break;
     
     default: 
       throw new IllegalStateException("should never happen");
     }
     return retVal;
   }
   
 
 
 
   public static void printCurrmove(boolean withInfo)
   {
     Eden.printCurrmove(Eden.currmovenumber, Eden.currentmove.getText(), withInfo);
   }
   
   private Move analyze() {
     if (Options.DEBUG)
       System.out.println("debug: EdenBrain.analyze()");
     Eden.multiPvs.clear();
     Move bookMove = OpeningBook.checkOpeningBook(position);
     if (Options.DEBUG)
       System.out.println("debug: EdenBrain.checkOpeningBook() finished.");
     if (bookMove != null) {
       updateMultiPv(bookMove, new Vector(), 0, 0);
       return bookMove;
     }
     Move bestMove = null;
     position.setBestMove(null);
     position.setBestValue(bestValue);
     SortedSet orderedMoves = null;
     int i = 0;
     boolean exit = false;
     timeIsUp = false;
     Move oldBestMove = null;
     bestValue = -99999;
     Vector oldMovesVector = new Vector();
     Vector tmpMovesVector = new Vector();
     do {
       oldBestMove = bestMove;
       bestMove = null;
       bestValue = -99999;
       i++;
       if (Options.DEBUG)
         System.out.println("debug: initializing Killer moves.");
       initializeKillerMoves();
       Options.maxQuiescence = 99;
       Info.idDepth = i;
       Eden.currmovenumber = 0;
       SortedSet nextRoundOrderedMoves = new TreeSet();
       if (orderedMoves == null)
         orderedMoves = position.generateLegalMoves();
       int size = orderedMoves.size();
       if (size == 1) {
         bestMove = (Move)orderedMoves.first();
         return bestMove;
       }
       if (size == 0) {
         System.out.println("debug: mate or stalemate!");
         return null;
       }
       if (oldMovesVector.isEmpty())
         oldMovesVector.addAll(orderedMoves);
       for (Iterator it = oldMovesVector.iterator(); it.hasNext();) {
         Move move = (Move)it.next();
         Eden.currmovenumber += 1;
         Eden.currentmove = move;
         printCurrmove(true);
         Eden.printInfo();
         moveStack.clear();
         moveStack.push(move);
         alphaBetaDraws.putAll(threeDrawsTable);
         Vector pvec = new Vector();
         int value = alphabeta(1, 0.0D, position, move, -99999, -bestValue, pvec, 0, false);
         assert (value < 99999);
         
         assert (value > -99999);
         
         switch (Math.abs(value)) {
         case 666660: 
         case 666661: 
         case 666662: 
         case 666663: 
         case 666664: 
         case 666665: 
         case 666666: 
         case 666667: 
           throw new IllegalStateException("invalid value: " + value);
         }
         moveStack.clear();
         if (timeIsUp)
           break;
         move.value = value;
         nextRoundOrderedMoves.add(move);
         if ((value > bestValue) || (move.equals(bestMove))) {
           updateMultiPv(move, pvec, value, Info.idDepth);
           tmpMovesVector.add(0, move);
           bestMove = move;
           bestValue = value;
           position.setBestMove(bestMove);
           position.setBestValue(bestValue);
           bestValue = value;
           if (value > 80000) {
             Eden.printPV();
             return bestMove;
           }
           if (Options.multiPvDisplayNumber == 1)
             Eden.printPV();
         } else {
           tmpMovesVector.add(move);
         }
         if (Options.multiPvDisplayNumber > 1) {
           Eden.printPV();
         }
       }
       orderedMoves = nextRoundOrderedMoves;
       if (Options.DEBUG) {
         System.out.println("debug: oldMoves:" + oldMovesVector);
         System.out.println("debug: newMoves" + tmpMovesVector);
       }
       oldMovesVector.clear();
       oldMovesVector.addAll(tmpMovesVector);
       tmpMovesVector.clear();
       if (orderedMoves.size() == 0)
         exit = true;
       int maxDepth = this.engine.getDepth();
       if ((maxDepth > 0) && (i >= maxDepth))
         exit = true;
       Eden.printPV();
       printInfo();
       if (Options.DEBUG)
         System.out.println("***End of Iteration " + i + "***");
     } while ((!timeUp()) && (!exit));
     System.gc();
     if (bestMove != null) {
       return bestMove;
     }
     return oldBestMove;
   }
   
   public String getBestMoveSoFar() {
     if (Options.DEBUG)
       System.out.println("debug: multiPVs: " + Eden.multiPvs);
     String bestMove = (String)Eden.multiPvs.keySet().iterator().next();
     return bestMove;
   }
   
   private static void addToKillers(int depth, Move potentialKiller) {
     if (potentialKiller == null)
       return;
     KillerEntry k1 = killerMoves1[depth];
     if (k1 == null) {
       Info.killerAdds += 1;
       killerMoves1[depth] = new KillerEntry(potentialKiller);
       return;
     }
     Move m1 = k1.getMove();
     if (potentialKiller.equals(m1)) {
       k1.increment();
       return;
     }
     KillerEntry k2 = killerMoves2[depth];
     if (k2 == null) {
       Info.killerAdds += 1;
       killerMoves2[depth] = new KillerEntry(potentialKiller);
       return;
     }
     Move m2 = k2.getMove();
     if (potentialKiller.equals(m2)) {
       k1.increment();
       return;
     }
     int c1 = k1.getCount();
     int c2 = k2.getCount();
     if (c1 > c2) {
       Info.killerAdds += 1;
       killerMoves2[depth] = new KillerEntry(potentialKiller);
     }
     Info.killerAdds += 1;
     killerMoves1[depth] = new KillerEntry(potentialKiller);
   }
   
   private static Move checkKillerMove1(int depth) {
     if (!Options.useKiller)
       return null;
     KillerEntry k1 = killerMoves1[depth];
     if (k1 == null) {
       return null;
     }
     Move killerMove = k1.getMove();
     Info.killerReads += 1;
     return killerMove;
   }
   
   private static Move checkKillerMove2(int depth)
   {
     if (!Options.useKiller)
       return null;
     KillerEntry k2 = killerMoves2[depth];
     if (k2 == null) {
       return null;
     }
     Move killerMove = k2.getMove();
     Info.killerReads += 1;
     return killerMove;
   }
   
   public String getAuthor()
   {
     return "Nicolai Czempin";
   }
   
   public String getName() {
     return "Eden";
   }
   
 
 
 
   public void setToStartPosition()
   {
     position.setToStartPosition();
   }
   
   public void setFENPosition(String positionFen) {
     position.setFENPosition(positionFen);
   }
   
   public void clearThreeDraws() {
     threeDrawsTable.clear();
   }
   
   public void setWithinAlphabeta(boolean b) {
     withinAlphabeta = b;
   }
   
   public void makeMove(String string) {
     setWithinAlphabeta(false);
     try {
       position.makeMove(string);
     } catch (ThreeRepetitionsAB e) {
       e.printStackTrace();
     }
   }
   
   public void setWbInc(int i) {
     throw new IllegalStateException("TODO");
   }
   
   public void setWbTime(int i) {
     throw new IllegalStateException("TODO");
   }
   
   public Boolean onMove() {
     return position.onMove();
   }
   
   public boolean isStartPosition() {
     return position.isStartPosition();
   }
   
   public void SetEmptyPosition() {
     position = Position.getEmpty();
   }
   
   public boolean whiteToMove() {
     return position.onMove().equals(Position.WHITE);
   }
   
   public int getStaticValue() {
     return position.getValue();
   }
   
   static boolean withinAlphabeta = false;
   static Map alphaBetaDraws = new HashMap();
   static Position drawCountPosition1;
   private static boolean kingCapture = false;
   static Stack moveStack = new Stack();
   public static Position position = Position.getEmpty();
   private static Random r = new Random(0L);
   static long onMoveZobrist = randomZobrist();
   static Map threeDrawsTable = new HashMap();
   
 
   private static boolean timeIsUp;
   
 
   static Map transpositions;
   
 
   static long[][][] zobrists;
   
 
   private static int bestValue;
   
   static long enPassantZobrist = randomZobrist();
   private static KillerEntry[] killerMoves1 = new KillerEntry[99];
   private static KillerEntry[] killerMoves2 = new KillerEntry[99];
   
   private static int illegalCount;
   private Engine engine;
   
   static
   {
     transpositions = new HashMap(Options.MAX_TRANSPOSITIONS);
     zobrists = new long[2][6][89];
     
     Long randLong;
Set zob;
     for (zob = new java.util.HashSet(1068); zob.size() < 1068; zob.add(randLong)) {
       randLong = new Long(randomZobrist());
     }
     Iterator it = zob.iterator();
     for (int i = 0; i < 2; i++) {
       for (int j = 0; j < 6; j++) {
         for (int k = 0; k < 89; k++) {
           long l = ((Long)it.next()).longValue();
           zobrists[i][j][k] = l;
         }
       }
     }
   }
   
   public static void initializeGo() {}
   
   public static void printInfo() {}
   
   public String getVersion()
   {
     return "0.0.13";
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/EdenBrain.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */