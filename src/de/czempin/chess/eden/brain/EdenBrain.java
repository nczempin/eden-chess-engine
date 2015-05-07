/*      */ package de.czempin.chess.eden.brain;
/*      */ 
/*      */ import de.czempin.chess.Engine;
/*      */ import de.czempin.chess.Info;
/*      */ import de.czempin.chess.Options;
/*      */ import de.czempin.chess.Variation;
/*      */ import de.czempin.chess.eden.Eden;
/*      */ import de.czempin.chess.eden.Move;

/*      */ import java.io.PrintStream;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Stack;
/*      */ import java.util.TreeSet;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class EdenBrain extends de.czempin.chess.AbstractBrain
/*      */ {
/*      */   public static final String NAME = "Eden";
/*      */   
/*      */   public EdenBrain(Engine engine)
/*      */   {
/*   27 */     this.engine = engine;
/*      */   }
/*      */   
/*      */   private int alphabeta(int depth, double extension, Position position, Move move, int originalAlpha, int beta, Vector upPv, int checkExtensions, boolean justExtended)
/*      */   {
/*   32 */     if (move == null)
/*   33 */       throw new IllegalStateException("move is null!");
/*   34 */     Move bestMove = null;
/*   35 */     int alpha = originalAlpha;
/*   36 */     int value = 666662;
/*   37 */     withinAlphabeta = true;
/*   38 */     if (timeUp()) {
/*   39 */       timeIsUp = true;
/*   40 */       return 0;
/*      */     }
Position nextPos;
/*      */     try
/*      */     {
/*   44 */       Info.ab_nodes += 1;
/*   45 */       nextPos = Position.createPosition(position, move);
/*      */     } catch (ThreeRepetitionsAB e) {
/*   47 */       System.out.println("debug: three repetitions!");
/*   48 */       return 0; }
/*      */     if ((!justExtended) && (Options.useCheckExtensions) && (checkExtensions < 4) && (Info.idDepth > 4) && (nextPos.isReceivingCheck()))
/*   51 */       return checkExtension(depth, extension, position, move, originalAlpha, beta, upPv, checkExtensions, true);
/*   52 */     Set markForDelete = new java.util.HashSet();
/*   53 */     int retVal = 0;
/*   54 */     int loopCount = 0;
/*   55 */     kingCapture = false;
/*   56 */     TransEntry te = nextPos.readHashtable(depth, extension);
/*   57 */     Move hashMove = null;
/*   58 */     int v = 666668;
/*   59 */     if (te != null) {
/*   60 */       hashMove = te.getMove();
/*   61 */       Info.countValidAttempts += 1;
/*   62 */       int validFlag = te.getValidFlag().getNr();
/*   63 */       int hashValue = te.getValue();
/*   64 */       if (hashMove == null) {
/*   65 */         if ((validFlag == 2) && (hashValue < beta)) {
/*   66 */           Info.hashCutoffs += 1;
/*   67 */           return -hashValue;
/*      */         }
/*      */       } else {
/*   70 */         int from = hashMove.from;
/*   71 */         if (nextPos.board[from] == 0) {
/*   72 */           hashMove = null;
/*      */         } else {
/*   74 */           Vector downPv = new Vector();
/*   75 */           if (validFlag == 1) {
/*   76 */             Info.countValidFlag += 1;
/*   77 */             loopCount++;
/*   78 */             value = hashValue;
/*   79 */             if (value >= beta) {
/*   80 */               Info.hashCutoffs += 1;
/*   81 */               retVal = -beta;
/*   82 */               return retVal;
/*      */             }
/*   84 */             alpha = value;
/*   85 */             upPv.clear();
/*   86 */             upPv.add(hashMove);
/*   87 */             upPv.addAll(downPv);
/*   88 */             bestMove = hashMove;
/*      */           } else {
/*   90 */             if (validFlag == 4) {
/*   91 */               if (hashValue >= beta) {
/*   92 */                 Info.hashCutoffs += 1;
/*   93 */                 return -hashValue;
/*      */               }
/*   95 */             } else if ((validFlag == 2) && (hashValue < beta)) {
/*   96 */               Info.hashCutoffs += 1;
/*   97 */               return -hashValue;
/*      */             }
/*   99 */             moveStack.push(hashMove);
/*  100 */             v = alphabeta(depth + 1, extension, nextPos, hashMove, -beta, -alpha, downPv, checkExtensions, false);
/*  101 */             assert (v < 99999);
/*      */             
/*  103 */             assert (v > -99999);
/*      */             
/*  105 */             moveStack.pop();
/*  106 */             if (kingCapture) {
/*  107 */               illegalCount += 1;
/*  108 */               hashMove = null;
/*  109 */               kingCapture = false;
/*      */             } else {
/*  111 */               loopCount++;
/*  112 */               value = v;
/*      */             }
/*  114 */             if (value >= beta) {
/*  115 */               Info.hashCutoffs += 1;
/*  116 */               retVal = -beta;
/*  117 */               return retVal;
/*      */             }
/*  119 */             alpha = value;
/*  120 */             upPv.clear();
/*  121 */             upPv.add(hashMove);
/*  122 */             upPv.addAll(downPv);
/*  123 */             bestMove = hashMove;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  128 */     if (depth >= Info.idDepth + extension) {
/*  129 */       if (Options.useQuiescence) {
/*  130 */         Vector downPv = new Vector();
/*  131 */         value = quiescence_alphabeta(depth, position, move, alpha, beta, downPv, nextPos);
/*  132 */         upPv.clear();
/*  133 */         upPv.addAll(downPv);
/*  134 */         return value;
/*      */       }
/*  136 */       if (nextPos.isGivingCheck()) {
/*  137 */         illegalCount += 1;
/*  138 */         kingCapture = true;
/*  139 */         return -666661;
/*      */       }
/*  141 */       value = nextPos.getValue();
/*  142 */       return -value;
/*      */     }
/*      */     
/*  145 */     int loop2 = 0;
/*  146 */     SortedSet captureMoves = nextPos.generateCaptureMoves();
/*  147 */     Iterator it = captureMoves.iterator();
/*  148 */     SortedSet deferredMoves = new TreeSet();
/*  149 */     while (it.hasNext()) {
/*  150 */       Move newMove = (Move)it.next();
/*  151 */       if ((hashMove == null) || (!newMove.equals(hashMove))) {
/*  152 */         int capture = newMove.capturedPiece;
/*  153 */         int captureAbs = Math.abs(capture);
/*  154 */         int movingAbs = Math.abs(newMove.movingPiece);
/*  155 */         if (captureAbs == 6) {
/*  156 */           illegalCount += 1;
/*  157 */           kingCapture = true;
/*  158 */           return -666666;
/*      */         }
/*  160 */         if (timeUp()) {
/*  161 */           timeIsUp = true;
/*  162 */           return 0;
/*      */         }
/*  164 */         moveStack.push(newMove);
/*  165 */         Vector downPv = new Vector();
/*  166 */         v = alphabeta(depth + 1, extension, nextPos, newMove, -beta, -alpha, downPv, checkExtensions, false);
/*  167 */         moveStack.pop();
/*  168 */         if (kingCapture) {
/*  169 */           illegalCount += 1;
/*  170 */           markForDelete.add(newMove);
/*  171 */           kingCapture = false;
/*      */         } else {
/*  173 */           if (bestMove == null)
/*  174 */             bestMove = newMove;
/*  175 */           loopCount++;
/*  176 */           loop2++;
/*  177 */           value = v;
/*  178 */           if (value >= beta) {
/*  179 */             Info.betaCutoffs += 1;
/*  180 */             if (loop2 == 1)
/*  181 */               Info.betaCutoffsFirstMove += 1;
/*  182 */             Info.pieceCapturingCutoffs[movingAbs] += 1;
/*  183 */             Info.pieceCapturedCutoffs[captureAbs] += 1;
/*  184 */             nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
/*  185 */             retVal = -beta;
/*  186 */             return retVal;
/*      */           }
/*  188 */           if (value > alpha) {
/*  189 */             alpha = value;
/*  190 */             upPv.clear();
/*  191 */             upPv.add(newMove);
/*  192 */             upPv.addAll(downPv);
/*  193 */             if (alpha > 80000) {
/*  194 */               nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
/*  195 */               return -alpha;
/*      */             }
/*  197 */             bestMove = newMove;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  202 */     SortedSet nonCaptureMoves = new TreeSet();
/*  203 */     nonCaptureMoves = nextPos.generateNonCaptureMoves();
/*  204 */     Move killerMove1 = checkKillerMove1(depth);
/*  205 */     if ((killerMove1 != null) && (!killerMove1.equals(hashMove))) {
/*  206 */       Iterator itx = nonCaptureMoves.iterator();
/*  207 */       boolean killerIsLegal = false;
/*  208 */       while (itx.hasNext()) {
/*  209 */         Move m = (Move)itx.next();
/*  210 */         if (m.getText().equals(killerMove1.getText())) {
/*  211 */           killerIsLegal = true;
/*  212 */           break;
/*      */         }
/*      */       }
/*  215 */       if (!killerIsLegal) {
/*  216 */         for (itx = deferredMoves.iterator(); itx.hasNext();) {
/*  217 */           Move m = (Move)itx.next();
/*  218 */           if (m.getText().equals(killerMove1.getText())) {
/*  219 */             killerIsLegal = true;
/*  220 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  224 */       if (!killerIsLegal) {
/*  225 */         killerMove1 = null;
/*      */       } else {
/*  227 */         moveStack.push(killerMove1);
/*  228 */         Vector downPv = new Vector();
/*  229 */         value = alphabeta(depth + 1, extension, nextPos, killerMove1, -beta, -alpha, downPv, checkExtensions, false);
/*  230 */         moveStack.pop();
/*  231 */         if (kingCapture) {
/*  232 */           illegalCount += 1;
/*  233 */           killerMove1 = null;
/*  234 */           kingCapture = false;
/*      */         } else {
/*  236 */           loopCount++;
/*  237 */           Info.killerHits += 1;
/*  238 */           if (value >= beta) {
/*  239 */             Info.killerCutoffs += 1;
/*  240 */             nextPos.writeHashtable(killerMove1, depth, extension, value, originalAlpha, beta);
/*  241 */             retVal = -beta;
/*  242 */             return retVal;
/*      */           }
/*  244 */           if (bestMove == null)
/*  245 */             bestMove = killerMove1;
/*  246 */           if (value > alpha) {
/*  247 */             alpha = value;
/*  248 */             upPv.clear();
/*  249 */             upPv.add(killerMove1);
/*  250 */             upPv.addAll(downPv);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  255 */     Move killerMove2 = checkKillerMove2(depth);
/*  256 */     if ((killerMove2 != null) && (!killerMove2.equals(hashMove))) {
/*  257 */       Iterator itx = nonCaptureMoves.iterator();
/*  258 */       boolean killerIsLegal = false;
/*  259 */       while (itx.hasNext()) {
/*  260 */         Move m = (Move)itx.next();
/*  261 */         if (m.getText().equals(killerMove2.getText())) {
/*  262 */           killerIsLegal = true;
/*  263 */           break;
/*      */         }
/*      */       }
/*  266 */       if (!killerIsLegal) {
/*  267 */         for (itx = deferredMoves.iterator(); itx.hasNext();) {
/*  268 */           Move m = (Move)itx.next();
/*  269 */           if (m.getText().equals(killerMove2.getText())) {
/*  270 */             killerIsLegal = true;
/*  271 */             break;
/*      */           }
/*      */         }
/*      */       }
/*  275 */       if (!killerIsLegal) {
/*  276 */         killerMove2 = null;
/*      */       } else {
/*  278 */         moveStack.push(killerMove2);
/*  279 */         Vector downPv = new Vector();
/*  280 */         value = alphabeta(depth + 1, extension, nextPos, killerMove2, -beta, -alpha, downPv, checkExtensions, false);
/*  281 */         moveStack.pop();
/*  282 */         if (kingCapture) {
/*  283 */           illegalCount += 1;
/*  284 */           killerMove2 = null;
/*  285 */           kingCapture = false;
/*      */         } else {
/*  287 */           loopCount++;
/*  288 */           Info.killerHits += 1;
/*  289 */           if (bestMove == null)
/*  290 */             bestMove = killerMove2;
/*  291 */           if (value >= beta) {
/*  292 */             Info.killerCutoffs += 1;
/*  293 */             nextPos.writeHashtable(killerMove2, depth, extension, value, originalAlpha, beta);
/*  294 */             retVal = -beta;
/*  295 */             return retVal;
/*      */           }
/*  297 */           if (value > alpha) {
/*  298 */             alpha = value;
/*  299 */             upPv.clear();
/*  300 */             upPv.add(killerMove2);
/*  301 */             upPv.addAll(downPv);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  306 */     for (Iterator it2 = nonCaptureMoves.iterator(); it2.hasNext();) {
/*  307 */       Move newMove = (Move)it2.next();
/*  308 */       if (((hashMove == null) || (!newMove.equals(hashMove))) && ((killerMove1 == null) || (!newMove.equals(killerMove1))) && (
/*  309 */         (killerMove2 == null) || (!newMove.equals(killerMove2)))) {
/*  310 */         int capture = newMove.capturedPiece;
/*  311 */         int captureAbs = Math.abs(capture);
/*  312 */         int movingAbs = Math.abs(newMove.movingPiece);
/*  313 */         if (captureAbs == 6) {
/*  314 */           illegalCount += 1;
/*  315 */           kingCapture = true;
/*  316 */           return -666667;
/*      */         }
/*  318 */         if (timeUp()) {
/*  319 */           timeIsUp = true;
/*  320 */           return 0;
/*      */         }
/*  322 */         moveStack.push(newMove);
/*  323 */         Vector downPv = new Vector();
/*  324 */         v = alphabeta(depth + 1, extension, nextPos, newMove, -beta, -alpha, downPv, checkExtensions, false);
/*  325 */         assert (v < 99999);
/*      */         
/*  327 */         assert (v != -99999);
/*      */         
/*  329 */         moveStack.pop();
/*  330 */         if (kingCapture) {
/*  331 */           illegalCount += 1;
/*  332 */           markForDelete.add(newMove);
/*  333 */           kingCapture = false;
/*      */         } else {
/*  335 */           if (bestMove == null)
/*  336 */             bestMove = newMove;
/*  337 */           loopCount++;
/*  338 */           value = v;
/*  339 */           assert (value < 99999);
/*      */           
/*  341 */           loop2++;
/*  342 */           if (value >= beta) {
/*  343 */             Info.betaCutoffs += 1;
/*  344 */             if (loop2 == 1)
/*  345 */               Info.betaCutoffsFirstMove += 1;
/*  346 */             Info.pieceMovingCutoffs[movingAbs] += 1;
/*  347 */             nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
/*  348 */             addToKillers(depth, newMove);
/*  349 */             retVal = -beta;
/*  350 */             return retVal;
/*      */           }
/*  352 */           if (value > alpha) {
/*  353 */             upPv.clear();
/*  354 */             upPv.add(newMove);
/*  355 */             upPv.addAll(downPv);
/*  356 */             if (!newMove.equals(hashMove)) {
/*  357 */               nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
/*  358 */               addToKillers(depth, newMove);
/*      */             }
/*  360 */             if (alpha > 80000) {
/*  361 */               nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
/*  362 */               return -value;
/*      */             }
/*  364 */             alpha = value;
/*  365 */             bestMove = newMove;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  371 */     for (Iterator it3 = deferredMoves.iterator(); it3.hasNext();) {
/*  372 */       Move newMove = (Move)it3.next();
/*  373 */       if (((hashMove == null) || (!newMove.equals(hashMove))) && ((killerMove1 == null) || (!newMove.equals(killerMove1))) && (
/*  374 */         (killerMove2 == null) || (!newMove.equals(killerMove2)))) {
/*  375 */         int capture = newMove.capturedPiece;
/*  376 */         int captureAbs = Math.abs(capture);
/*  377 */         int movingAbs = Math.abs(newMove.movingPiece);
/*  378 */         if (captureAbs == 6) {
/*  379 */           illegalCount += 1;
/*  380 */           kingCapture = true;
/*  381 */           return -666665;
/*      */         }
/*  383 */         if (timeUp()) {
/*  384 */           timeIsUp = true;
/*  385 */           return 0;
/*      */         }
/*  387 */         moveStack.push(newMove);
/*  388 */         Vector downPv = new Vector();
/*  389 */         v = alphabeta(depth + 1, extension, nextPos, newMove, -beta, -alpha, downPv, checkExtensions, false);
/*  390 */         moveStack.pop();
/*  391 */         if (kingCapture) {
/*  392 */           illegalCount += 1;
/*  393 */           markForDelete.add(newMove);
/*  394 */           kingCapture = false;
/*      */         } else {
/*  396 */           if (bestMove == null)
/*  397 */             bestMove = newMove;
/*  398 */           loopCount++;
/*  399 */           value = v;
/*  400 */           loop2++;
/*  401 */           if (value >= beta) {
/*  402 */             Info.betaCutoffs += 1;
/*  403 */             if (loop2 == 1)
/*  404 */               Info.betaCutoffsFirstMove += 1;
/*  405 */             Info.pieceCapturingCutoffs[movingAbs] += 1;
/*  406 */             Info.pieceCapturedCutoffs[captureAbs] += 1;
/*  407 */             nextPos.writeHashtable(bestMove, depth, extension, value, originalAlpha, beta);
/*  408 */             retVal = -beta;
/*  409 */             return retVal;
/*      */           }
/*  411 */           if (value > alpha) {
/*  412 */             alpha = value;
/*  413 */             upPv.clear();
/*  414 */             upPv.add(newMove);
/*  415 */             upPv.addAll(downPv);
/*  416 */             if (!newMove.equals(hashMove))
/*  417 */               addToKillers(depth, newMove);
/*  418 */             if (alpha > 80000) {
/*  419 */               nextPos.writeHashtable(newMove, depth, extension, value, originalAlpha, beta);
/*  420 */               return -alpha;
/*      */             }
/*  422 */             bestMove = newMove;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  428 */     SortedSet moves = new TreeSet();
/*  429 */     moves.addAll(captureMoves);
/*  430 */     moves.addAll(nonCaptureMoves);
/*  431 */     moves.removeAll(markForDelete);
/*  432 */     if ((loopCount == 0) || (moves.size() == 0)) {
/*  433 */       if (nextPos.isReceivingCheck()) {
/*  434 */         value = -(90000 - depth);
/*  435 */         return -value;
/*      */       }
/*  437 */       return 0;
/*      */     }
/*  439 */     if ((loopCount == 1) || (moves.size() == 1)) {
/*  440 */       if (bestMove == null) {
/*  441 */         bestMove = (Move)moves.first();
/*  442 */         if (bestMove == null)
/*  443 */           throw new IllegalStateException("move is null!");
/*      */       }
/*  445 */       Move onlyMove = bestMove;
/*  446 */       alpha = forcedMoveExtension(depth, extension, originalAlpha, beta, upPv, nextPos, onlyMove, alpha, checkExtensions);
/*  447 */       bestMove = onlyMove;
/*      */     }
/*  449 */     addToKillers(depth, bestMove);
/*  450 */     nextPos.writeHashtable(bestMove, depth, extension, alpha, originalAlpha, beta);
/*  451 */     return -alpha;
/*      */   }
/*      */   
/*      */ 
/*      */   private int pawnSeventhExtension(int depth, double extension, Position position, Move move, int originalAlpha, int beta, Vector upPv, int checkExtensions)
/*      */   {
/*  457 */     double pawnSeventhExtension = extension + 1.0D;
/*  458 */     return alphabeta(depth, pawnSeventhExtension, position, move, originalAlpha, beta, upPv, checkExtensions, true);
/*      */   }
/*      */   
/*      */   private int checkExtension(int depth, double extension, Position position, Move move, int originalAlpha, int beta, Vector upPv, int checkExtensions, boolean b)
/*      */   {
/*  463 */     double checkExtension = extension + 0.9D;
/*  464 */     int value = alphabeta(depth, checkExtension, position, move, originalAlpha, beta, upPv, checkExtensions + 1, true);
/*  465 */     return value;
/*      */   }
/*      */   
/*      */   private int forcedMoveExtension(int depth, double extension, int originalAlpha, int beta, Vector upPv, Position nextPos, Move onlyMove, int originalValue, int checkExtensions)
/*      */   {
/*  470 */     if (!Options.useForcedMoveExtension)
/*  471 */       return originalValue;
/*  472 */     moveStack.push(onlyMove);
/*  473 */     Vector downPv = new Vector();
/*  474 */     extension += 1.501D;
/*  475 */     int value = alphabeta(depth + 1, extension, nextPos, onlyMove, -beta, -originalAlpha, downPv, checkExtensions, false);
/*  476 */     assert (value < 99999);
/*      */     
/*  478 */     assert (value > -99999);
/*      */     
/*      */ 
/*  481 */     moveStack.pop();
/*  482 */     upPv.clear();
/*  483 */     upPv.add(onlyMove);
/*  484 */     upPv.addAll(downPv);
/*  485 */     return value;
/*      */   }
/*      */   
/*      */   public long calculateTimePerMove(long t, long inc)
/*      */   {
/*  490 */     if ((t == 0L) && (inc == 0L))
/*  491 */       return -1L;
/*  492 */     int slice = this.engine.getMovestogo() + 1;
/*  493 */     long retVal = (t - inc) / slice + inc - 500L;
/*  494 */     if (retVal < 100L)
/*  495 */       retVal = 100L;
/*  496 */     return retVal;
/*      */   }
/*      */   
/*      */   static Long getZobrist(Position sp) {
/*  500 */     return new Long(sp.getZobrist());
/*      */   }
/*      */   
/*      */   public void initializeGame() {
/*  504 */     position.setToStartPosition();
/*  505 */     transpositions.clear();
/*  506 */     threeDrawsTable.clear();
/*  507 */     OpeningBook.prepareOpeningBook();
/*  508 */     initializeKillerMoves();
/*      */   }
/*      */   
/*      */   private static void initializeKillerMoves() {
/*  512 */     for (int i = 0; i < killerMoves1.length; i++) {
/*  513 */       killerMoves1[i] = null;
/*  514 */       killerMoves2[i] = null;
/*      */     }
/*      */     
/*  517 */     if (Options.DEBUG) {
/*  518 */       System.out.println("debug: killer moves initialized.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String move()
/*      */   {
/*  525 */     if (Options.DEBUG)
/*  526 */       System.out.println("debug: EdenBrain.move()");
/*  527 */     Info.nodes = 0L;
/*  528 */     Info.seldepth = 0;
/*  529 */     illegalCount = 0;
/*  530 */     Info.enPriseNodes = 0;
/*  531 */     Info.qs_nodes = 0L;
/*  532 */     Info.ab_nodes = 0;
/*  533 */     Info.drawCountNodes = 0;
/*  534 */     Info.castlingNodes = 0;
/*  535 */     Info.quiescentMateCheckNodes = 0;
/*  536 */     Info.hashReads = 0;
/*  537 */     Info.hashHitCount = 0;
/*  538 */     Info.hashMiss = 0;
/*  539 */     Info.hashDepthMiss = 0;
/*  540 */     System.gc();
/*  541 */     Info.searchtime = System.currentTimeMillis();
/*  542 */     Eden.calculateTimePerMove();
/*  543 */     Move move = analyze();
/*  544 */     if (move == null)
/*  545 */       return "Can't move!\n";
/*      */     try {
/*  547 */       position.makeMove(move);
/*      */     } catch (ThreeRepetitionsAB e) {
/*  549 */       e.printStackTrace();
/*      */     }
/*  551 */     if (Options.DEBUG)
/*  552 */       System.out.println("debug: returning move: " + move);
/*  553 */     return move.getText();
/*      */   }
/*      */   
/*      */   private static void updateMultiPv(Move move, Vector variation, int value, int depth) {
/*  557 */     String key = move.getText();
/*  558 */     int size = variation.size();
/*  559 */     Vector variationStrings = new Vector(size);
/*  560 */     for (int i = 0; i < size; i++) {
/*  561 */       Move m = (Move)variation.get(i);
/*  562 */       variationStrings.add(m.getText());
/*      */     }
/*      */     
/*  565 */     Variation v2 = new Variation(move.getText(), variationStrings, value, depth);
/*  566 */     Eden.multiPvs.clear();
/*  567 */     Eden.multiPvs.put(key, v2);
/*      */   }
/*      */   
/*      */   public String extractVariationString(Vector variation) {
/*  571 */     Iterator it1 = variation.iterator();
/*      */     
/*      */     String m;
String pv;
/*  574 */     for (pv = ""; it1.hasNext(); pv = pv + m + " ") {
/*  575 */       m = (String)it1.next();
/*      */     }
/*  577 */     return pv;
/*      */   }
/*      */   
/*      */   private int quiescence_alphabeta(int depth, Position position, Move move, int originalAlpha, int beta, Vector upPv, Position nextPos)
/*      */   {
/*  582 */     int alpha = originalAlpha;
/*  583 */     ValidFlag bestMoveValidFlag = new ValidFlag();
/*  584 */     if ((timeUp()) || (timeIsUp)) {
/*  585 */       timeIsUp = true;
/*  586 */       return 0;
/*      */     }
/*  588 */     if (depth > Info.seldepth)
/*  589 */       Info.seldepth = depth;
/*  590 */     if (nextPos == null)
/*      */       try {
/*  592 */         Info.qs_nodes += 1L;
/*  593 */         nextPos = Position.createPosition(position, move);
/*      */       } catch (ThreeRepetitionsAB e) {
/*  595 */         System.err.println("three repetitions. what to do?");
/*  596 */         return 0;
/*      */       }
/*  598 */     int v = nextPos.getValue();
/*  599 */     if (v >= beta)
/*  600 */       return -beta;
/*  601 */     if (v > alpha) {
/*  602 */       bestMoveValidFlag.setNr(1);
/*  603 */       alpha = v;
/*      */     }
/*  605 */     kingCapture = false;
/*  606 */     int loopCount = 0;
/*  607 */     nextPos.initCaptureMoveGenerator();
/*  608 */     while (nextPos.hasNextCaptureMove()) {
/*  609 */       Move newMove = nextPos.nextCaptureMove();
/*  610 */       int capture = Math.abs(newMove.capturedPiece);
/*  611 */       int capturing = Math.abs(position.board[newMove.from]);
/*  612 */       if (capture == 6) {
/*  613 */         kingCapture = true;
/*  614 */         illegalCount += 1;
/*  615 */         return -666663;
/*      */       }
/*  617 */       if (!shouldBeIgnored(nextPos, newMove, capture, capturing)) {
/*  618 */         moveStack.push(newMove);
/*  619 */         Vector downPv = new Vector();
/*  620 */         int value = quiescence_alphabeta(depth + 1, nextPos, newMove, -beta, -alpha, downPv, null);
/*  621 */         if (kingCapture) {
/*  622 */           illegalCount += 1;
/*  623 */           moveStack.pop();
/*  624 */           kingCapture = false;
/*      */         } else {
/*  626 */           loopCount++;
/*  627 */           if (value >= beta) {
/*  628 */             moveStack.pop();
/*  629 */             return -beta;
/*      */           }
/*  631 */           bestMoveValidFlag.setNr(-1);
/*  632 */           if (value > alpha) {
/*  633 */             alpha = value;
/*  634 */             upPv.clear();
/*  635 */             upPv.add(newMove);
/*  636 */             upPv.addAll(downPv);
/*      */           }
/*  638 */           moveStack.pop();
/*      */         }
/*      */       }
/*      */     }
/*  642 */     if (loopCount == 0) {
/*  643 */       nextPos.initNonCaptureMoveGenerator();
/*  644 */       boolean legal = false;
/*  645 */       while (nextPos.hasNextNonCaptureMove()) {
/*  646 */         Move testMove = nextPos.nextNonCaptureMove();
/*  647 */         Info.quiescentMateCheckNodes += 1;
/*  648 */         Position tp = Position.createTestPosition(nextPos, testMove);
/*  649 */         if (!tp.isGivingCheck()) {
/*  650 */           legal = true;
/*  651 */           break;
/*      */         }
/*      */       }
/*  654 */       if (!legal) {
/*  655 */         SortedSet lMoves = nextPos.generateLegalMoves();
/*  656 */         if (lMoves.size() == 0) {
/*  657 */           if (nextPos.isMate()) {
/*  658 */             int value = -90000 + depth;
/*  659 */             return -value;
/*      */           }
/*  661 */           return 0;
/*      */         }
/*      */       }
/*  664 */       alpha = v;
/*      */     }
/*  666 */     return -alpha;
/*      */   }
/*      */   
/*      */   private static long randomZobrist() {
/*  670 */     return r.nextLong();
/*      */   }
/*      */   
/*      */   private static boolean shouldBeIgnored(Position nextPos, Move newMove, int capture, int capturing) {
/*  674 */     return (capturing > capture) && ((capturing != 3) || (capture != 2)) && (!nextPos.enPrise(newMove));
/*      */   }
/*      */   
/*      */   private boolean timeUp() {
/*  678 */     return this.engine.timeUp();
/*      */   }
/*      */   
/*      */   static Boolean convertColor(int p) {
/*  682 */     Boolean retVal = null;
/*  683 */     switch (p) {
/*      */     case 0: 
/*  685 */       return null;
/*      */     
/*      */     case -6: 
/*      */     case -5: 
/*      */     case -4: 
/*      */     case -3: 
/*      */     case -2: 
/*      */     case -1: 
/*  693 */       retVal = Position.BLACK;
/*  694 */       break;
/*      */     
/*      */     case 1: 
/*      */     case 2: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*  702 */       retVal = Position.WHITE;
/*  703 */       break;
/*      */     
/*      */     default: 
/*  706 */       throw new IllegalStateException("should never happen");
/*      */     }
/*  708 */     return retVal;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void printCurrmove(boolean withInfo)
/*      */   {
/*  716 */     Eden.printCurrmove(Eden.currmovenumber, Eden.currentmove.getText(), withInfo);
/*      */   }
/*      */   
/*      */   private Move analyze() {
/*  720 */     if (Options.DEBUG)
/*  721 */       System.out.println("debug: EdenBrain.analyze()");
/*  722 */     Eden.multiPvs.clear();
/*  723 */     Move bookMove = OpeningBook.checkOpeningBook(position);
/*  724 */     if (Options.DEBUG)
/*  725 */       System.out.println("debug: EdenBrain.checkOpeningBook() finished.");
/*  726 */     if (bookMove != null) {
/*  727 */       updateMultiPv(bookMove, new Vector(), 0, 0);
/*  728 */       return bookMove;
/*      */     }
/*  730 */     Move bestMove = null;
/*  731 */     position.setBestMove(null);
/*  732 */     position.setBestValue(bestValue);
/*  733 */     SortedSet orderedMoves = null;
/*  734 */     int i = 0;
/*  735 */     boolean exit = false;
/*  736 */     timeIsUp = false;
/*  737 */     Move oldBestMove = null;
/*  738 */     bestValue = -99999;
/*  739 */     Vector oldMovesVector = new Vector();
/*  740 */     Vector tmpMovesVector = new Vector();
/*      */     do {
/*  742 */       oldBestMove = bestMove;
/*  743 */       bestMove = null;
/*  744 */       bestValue = -99999;
/*  745 */       i++;
/*  746 */       if (Options.DEBUG)
/*  747 */         System.out.println("debug: initializing Killer moves.");
/*  748 */       initializeKillerMoves();
/*  749 */       Options.maxQuiescence = 99;
/*  750 */       Info.idDepth = i;
/*  751 */       Eden.currmovenumber = 0;
/*  752 */       SortedSet nextRoundOrderedMoves = new TreeSet();
/*  753 */       if (orderedMoves == null)
/*  754 */         orderedMoves = position.generateLegalMoves();
/*  755 */       int size = orderedMoves.size();
/*  756 */       if (size == 1) {
/*  757 */         bestMove = (Move)orderedMoves.first();
/*  758 */         return bestMove;
/*      */       }
/*  760 */       if (size == 0) {
/*  761 */         System.out.println("debug: mate or stalemate!");
/*  762 */         return null;
/*      */       }
/*  764 */       if (oldMovesVector.isEmpty())
/*  765 */         oldMovesVector.addAll(orderedMoves);
/*  766 */       for (Iterator it = oldMovesVector.iterator(); it.hasNext();) {
/*  767 */         Move move = (Move)it.next();
/*  768 */         Eden.currmovenumber += 1;
/*  769 */         Eden.currentmove = move;
/*  770 */         printCurrmove(true);
/*  771 */         Eden.printInfo();
/*  772 */         moveStack.clear();
/*  773 */         moveStack.push(move);
/*  774 */         alphaBetaDraws.putAll(threeDrawsTable);
/*  775 */         Vector pvec = new Vector();
/*  776 */         int value = alphabeta(1, 0.0D, position, move, -99999, -bestValue, pvec, 0, false);
/*  777 */         assert (value < 99999);
/*      */         
/*  779 */         assert (value > -99999);
/*      */         
/*  781 */         switch (Math.abs(value)) {
/*      */         case 666660: 
/*      */         case 666661: 
/*      */         case 666662: 
/*      */         case 666663: 
/*      */         case 666664: 
/*      */         case 666665: 
/*      */         case 666666: 
/*      */         case 666667: 
/*  790 */           throw new IllegalStateException("invalid value: " + value);
/*      */         }
/*  792 */         moveStack.clear();
/*  793 */         if (timeIsUp)
/*      */           break;
/*  795 */         move.value = value;
/*  796 */         nextRoundOrderedMoves.add(move);
/*  797 */         if ((value > bestValue) || (move.equals(bestMove))) {
/*  798 */           updateMultiPv(move, pvec, value, Info.idDepth);
/*  799 */           tmpMovesVector.add(0, move);
/*  800 */           bestMove = move;
/*  801 */           bestValue = value;
/*  802 */           position.setBestMove(bestMove);
/*  803 */           position.setBestValue(bestValue);
/*  804 */           bestValue = value;
/*  805 */           if (value > 80000) {
/*  806 */             Eden.printPV();
/*  807 */             return bestMove;
/*      */           }
/*  809 */           if (Options.multiPvDisplayNumber == 1)
/*  810 */             Eden.printPV();
/*      */         } else {
/*  812 */           tmpMovesVector.add(move);
/*      */         }
/*  814 */         if (Options.multiPvDisplayNumber > 1) {
/*  815 */           Eden.printPV();
/*      */         }
/*      */       }
/*  818 */       orderedMoves = nextRoundOrderedMoves;
/*  819 */       if (Options.DEBUG) {
/*  820 */         System.out.println("debug: oldMoves:" + oldMovesVector);
/*  821 */         System.out.println("debug: newMoves" + tmpMovesVector);
/*      */       }
/*  823 */       oldMovesVector.clear();
/*  824 */       oldMovesVector.addAll(tmpMovesVector);
/*  825 */       tmpMovesVector.clear();
/*  826 */       if (orderedMoves.size() == 0)
/*  827 */         exit = true;
/*  828 */       int maxDepth = this.engine.getDepth();
/*  829 */       if ((maxDepth > 0) && (i >= maxDepth))
/*  830 */         exit = true;
/*  831 */       Eden.printPV();
/*  832 */       printInfo();
/*  833 */       if (Options.DEBUG)
/*  834 */         System.out.println("***End of Iteration " + i + "***");
/*  835 */     } while ((!timeUp()) && (!exit));
/*  836 */     System.gc();
/*  837 */     if (bestMove != null) {
/*  838 */       return bestMove;
/*      */     }
/*  840 */     return oldBestMove;
/*      */   }
/*      */   
/*      */   public String getBestMoveSoFar() {
/*  844 */     if (Options.DEBUG)
/*  845 */       System.out.println("debug: multiPVs: " + Eden.multiPvs);
/*  846 */     String bestMove = (String)Eden.multiPvs.keySet().iterator().next();
/*  847 */     return bestMove;
/*      */   }
/*      */   
/*      */   private static void addToKillers(int depth, Move potentialKiller) {
/*  851 */     if (potentialKiller == null)
/*  852 */       return;
/*  853 */     KillerEntry k1 = killerMoves1[depth];
/*  854 */     if (k1 == null) {
/*  855 */       Info.killerAdds += 1;
/*  856 */       killerMoves1[depth] = new KillerEntry(potentialKiller);
/*  857 */       return;
/*      */     }
/*  859 */     Move m1 = k1.getMove();
/*  860 */     if (potentialKiller.equals(m1)) {
/*  861 */       k1.increment();
/*  862 */       return;
/*      */     }
/*  864 */     KillerEntry k2 = killerMoves2[depth];
/*  865 */     if (k2 == null) {
/*  866 */       Info.killerAdds += 1;
/*  867 */       killerMoves2[depth] = new KillerEntry(potentialKiller);
/*  868 */       return;
/*      */     }
/*  870 */     Move m2 = k2.getMove();
/*  871 */     if (potentialKiller.equals(m2)) {
/*  872 */       k1.increment();
/*  873 */       return;
/*      */     }
/*  875 */     int c1 = k1.getCount();
/*  876 */     int c2 = k2.getCount();
/*  877 */     if (c1 > c2) {
/*  878 */       Info.killerAdds += 1;
/*  879 */       killerMoves2[depth] = new KillerEntry(potentialKiller);
/*      */     }
/*  881 */     Info.killerAdds += 1;
/*  882 */     killerMoves1[depth] = new KillerEntry(potentialKiller);
/*      */   }
/*      */   
/*      */   private static Move checkKillerMove1(int depth) {
/*  886 */     if (!Options.useKiller)
/*  887 */       return null;
/*  888 */     KillerEntry k1 = killerMoves1[depth];
/*  889 */     if (k1 == null) {
/*  890 */       return null;
/*      */     }
/*  892 */     Move killerMove = k1.getMove();
/*  893 */     Info.killerReads += 1;
/*  894 */     return killerMove;
/*      */   }
/*      */   
/*      */   private static Move checkKillerMove2(int depth)
/*      */   {
/*  899 */     if (!Options.useKiller)
/*  900 */       return null;
/*  901 */     KillerEntry k2 = killerMoves2[depth];
/*  902 */     if (k2 == null) {
/*  903 */       return null;
/*      */     }
/*  905 */     Move killerMove = k2.getMove();
/*  906 */     Info.killerReads += 1;
/*  907 */     return killerMove;
/*      */   }
/*      */   
/*      */   public String getAuthor()
/*      */   {
/*  912 */     return "Nicolai Czempin";
/*      */   }
/*      */   
/*      */   public String getName() {
/*  916 */     return "Eden";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setToStartPosition()
/*      */   {
/*  924 */     position.setToStartPosition();
/*      */   }
/*      */   
/*      */   public void setFENPosition(String positionFen) {
/*  928 */     position.setFENPosition(positionFen);
/*      */   }
/*      */   
/*      */   public void clearThreeDraws() {
/*  932 */     threeDrawsTable.clear();
/*      */   }
/*      */   
/*      */   public void setWithinAlphabeta(boolean b) {
/*  936 */     withinAlphabeta = b;
/*      */   }
/*      */   
/*      */   public void makeMove(String string) {
/*  940 */     setWithinAlphabeta(false);
/*      */     try {
/*  942 */       position.makeMove(string);
/*      */     } catch (ThreeRepetitionsAB e) {
/*  944 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */   public void setWbInc(int i) {
/*  949 */     throw new IllegalStateException("TODO");
/*      */   }
/*      */   
/*      */   public void setWbTime(int i) {
/*  953 */     throw new IllegalStateException("TODO");
/*      */   }
/*      */   
/*      */   public Boolean onMove() {
/*  957 */     return position.onMove();
/*      */   }
/*      */   
/*      */   public boolean isStartPosition() {
/*  961 */     return position.isStartPosition();
/*      */   }
/*      */   
/*      */   public void SetEmptyPosition() {
/*  965 */     position = Position.getEmpty();
/*      */   }
/*      */   
/*      */   public boolean whiteToMove() {
/*  969 */     return position.onMove().equals(Position.WHITE);
/*      */   }
/*      */   
/*      */   public int getStaticValue() {
/*  973 */     return position.getValue();
/*      */   }
/*      */   
/*  976 */   static boolean withinAlphabeta = false;
/*  977 */   static Map alphaBetaDraws = new HashMap();
/*      */   static Position drawCountPosition1;
/*  979 */   private static boolean kingCapture = false;
/*  980 */   static Stack moveStack = new Stack();
/*  981 */   public static Position position = Position.getEmpty();
/*  982 */   private static Random r = new Random(0L);
/*  983 */   static long onMoveZobrist = randomZobrist();
/*  984 */   static Map threeDrawsTable = new HashMap();
/*      */   
/*      */ 
/*      */   private static boolean timeIsUp;
/*      */   
/*      */ 
/*      */   static Map transpositions;
/*      */   
/*      */ 
/*      */   static long[][][] zobrists;
/*      */   
/*      */ 
/*      */   private static int bestValue;
/*      */   
/*  998 */   static long enPassantZobrist = randomZobrist();
/*  999 */   private static KillerEntry[] killerMoves1 = new KillerEntry[99];
/* 1000 */   private static KillerEntry[] killerMoves2 = new KillerEntry[99];
/*      */   
/*      */   private static int illegalCount;
/*      */   private Engine engine;
/*      */   
/*      */   static
/*      */   {
/* 1007 */     transpositions = new HashMap(Options.MAX_TRANSPOSITIONS);
/* 1008 */     zobrists = new long[2][6][89];
/*      */     
/*      */     Long randLong;
Set zob;
/* 1011 */     for (zob = new java.util.HashSet(1068); zob.size() < 1068; zob.add(randLong)) {
/* 1012 */       randLong = new Long(randomZobrist());
/*      */     }
/* 1014 */     Iterator it = zob.iterator();
/* 1015 */     for (int i = 0; i < 2; i++) {
/* 1016 */       for (int j = 0; j < 6; j++) {
/* 1017 */         for (int k = 0; k < 89; k++) {
/* 1018 */           long l = ((Long)it.next()).longValue();
/* 1019 */           zobrists[i][j][k] = l;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void initializeGo() {}
/*      */   
/*      */   public static void printInfo() {}
/*      */   
/*      */   public String getVersion()
/*      */   {
/*      */     return "0.0.13";
/*      */   }
/*      */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/EdenBrain.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */