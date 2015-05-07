/*    */ package de.czempin.chess.commands;
/*    */ 
/*    */ import de.czempin.chess.Brain;
/*    */ import de.czempin.chess.Engine;
/*    */ 
/*    */ public class PositionCommand extends AbstractCommand
/*    */ {
/*    */   public PositionCommand(Engine engine)
/*    */   {
/* 10 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance()
/*    */   {
/* 15 */     if (instance == null)
/* 16 */       instance = new PositionCommand(null);
/* 17 */     return instance;
/*    */   }
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 22 */     return "position";
/*    */   }
/*    */   
/*    */   protected String execute()
/*    */   {
/* 27 */     this.engine.initializeGame();
/* 28 */     setPosition();
/* 29 */     handleMoves();
/* 30 */     return "";
/*    */   }
/*    */   
/*    */   private void handleMoves()
/*    */   {
/* 35 */     String movesString = extractMoves(this.actualParams);
/* 36 */     String[] moves = movesString.split(" ");
/* 37 */     this.brain.clearThreeDraws();
/* 38 */     if ((movesString != "") && (moves.length != 0))
/*    */     {
/* 40 */       for (int i = 0; i < moves.length; i++) {
/* 41 */         this.brain.makeMove(moves[i]);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private void setPosition()
/*    */   {
/* 48 */     this.brain.SetEmptyPosition();
/* 49 */     String positionString = extractPosition(this.actualParams);
/* 50 */     if (positionString.equals("startpos")) {
/* 51 */       this.brain.setToStartPosition();
/*    */     }
/* 53 */     else if (positionString.startsWith("fen"))
/*    */     {
/* 55 */       String positionFen = extractFen(positionString);
/* 56 */       this.brain.setFENPosition(positionFen);
/*    */     }
/*    */   }
/*    */   
/*    */   private String extractFen(String positionString)
/*    */   {
/* 62 */     String retValue = positionString.replaceFirst("fen ", "");
/* 63 */     if (retValue.indexOf("moves ") != -1)
/* 64 */       retValue = retValue.split("moves")[0];
/* 65 */     return retValue;
/*    */   }
/*    */   
/*    */   private static String extractMoves(String parameters)
/*    */   {
/* 70 */     String pattern = " moves ";
/* 71 */     int index = parameters.indexOf(pattern);
/* 72 */     if (index == -1)
/*    */     {
/* 74 */       return "";
/*    */     }
/*    */     
/* 77 */     String moves = parameters.substring(index + pattern.length());
/* 78 */     return moves;
/*    */   }
/*    */   
/*    */ 
/*    */   private String extractPosition(String parameters)
/*    */   {
/* 84 */     String pattern = " moves ";
/* 85 */     int index = parameters.indexOf(pattern);
/* 86 */     if (index == -1)
/*    */     {
/* 88 */       return parameters;
/*    */     }
/*    */     
/* 91 */     String position = parameters.substring(0, index);
/* 92 */     return position;
/*    */   }
/*    */   
/*    */ 
/* 96 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/PositionCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */