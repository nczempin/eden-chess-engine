/*    */ package de.czempin.chess.commands.uci;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class UCINewGameCommand extends de.czempin.chess.commands.AbstractCommand
/*    */ {
/*    */   public UCINewGameCommand(Engine engine)
/*    */   {
/* 10 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance() {
/* 14 */     if (instance == null)
/* 15 */       instance = new UCINewGameCommand(null);
/* 16 */     return instance;
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   public String execute(String parameters)
/*    */   {
/* 23 */     this.brain.initializeGame();
/* 24 */     return "isready\n";
/*    */   }
/*    */   
/*    */   protected String commandString() {
/* 28 */     return "ucinewgame";
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 32 */     return null;
/*    */   }
/*    */   
/* 35 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCINewGameCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */