/*    */ package de.czempin.chess.commands.wb;
/*    */ 
/*    */ import de.czempin.chess.Brain;
/*    */ import de.czempin.chess.commands.AbstractCommand;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class WBSetboardCommand
/*    */   extends AbstractCommand
/*    */ {
/*    */   public static Command getInstance()
/*    */   {
/* 12 */     if (instance == null)
/* 13 */       instance = new WBSetboardCommand();
/* 14 */     return instance;
/*    */   }
/*    */   
/*    */   protected String commandString() {
/* 18 */     return "setboard";
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 22 */     this.brain.initializeGame();
/* 23 */     setPosition();
/* 24 */     handleMoves();
/* 25 */     return "";
/*    */   }
/*    */   
/*    */   private void handleMoves() {
/* 29 */     String movesString = extractMoves(this.actualParams);
/* 30 */     String[] moves = movesString.split(" ");
/* 31 */     this.brain.clearThreeDraws();
/* 32 */     if ((movesString != "") && (moves.length != 0)) {
/* 33 */       for (int i = 0; i < moves.length; i++) {
/* 34 */         this.brain.makeMove(moves[i]);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private void setPosition() {}
/*    */   
/*    */   private static String extractMoves(String parameters)
/*    */   {
/* 43 */     String pattern = " moves ";
/* 44 */     int index = parameters.indexOf(pattern);
/* 45 */     if (index == -1) {
/* 46 */       return "";
/*    */     }
/* 48 */     String moves = parameters.substring(index + pattern.length());
/* 49 */     return moves;
/*    */   }
/*    */   
/*    */ 
/* 53 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBSetboardCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */