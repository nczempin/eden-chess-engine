/*    */ package de.czempin.chess.commands.uci;
/*    */ 
/*    */ import de.czempin.chess.Brain;
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class UCIIDCommand extends de.czempin.chess.commands.AbstractCommand
/*    */ {
/*    */   public UCIIDCommand(Engine engine)
/*    */   {
/* 11 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance() {
/* 15 */     if (instance == null)
/* 16 */       instance = new UCIIDCommand(null);
/* 17 */     return instance;
/*    */   }
/*    */   
/*    */   public String execute(String parameters) {
/* 21 */     String retVal = "id name " + this.brain.getName() + " " + this.brain.getVersion() + "\n";
/* 22 */     retVal = retVal + "id author " + this.brain.getAuthor() + "\n";
/* 23 */     retVal = retVal + de.czempin.chess.Options.printOptions();
/* 24 */     retVal = retVal + "uciok\n";
/* 25 */     return retVal;
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 32 */     return "uci";
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 36 */     return null;
/*    */   }
/*    */   
/* 39 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCIIDCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */