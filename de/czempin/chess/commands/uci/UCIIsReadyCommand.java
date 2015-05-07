/*    */ package de.czempin.chess.commands.uci;
/*    */ 
/*    */ import de.czempin.chess.commands.AbstractCommand;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ 
/*    */ public class UCIIsReadyCommand
/*    */   extends AbstractCommand
/*    */ {
/*    */   public static Command getInstance()
/*    */   {
/* 12 */     if (instance == null)
/* 13 */       instance = new UCIIsReadyCommand();
/* 14 */     return instance;
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   public String execute(String parameters)
/*    */   {
/*    */     try {
/* 22 */       Thread.sleep(1000L);
/*    */     } catch (InterruptedException e) {
/* 24 */       e.printStackTrace();
/*    */     }
/* 26 */     return "\nreadyok\n";
/*    */   }
/*    */   
/*    */   protected String commandString() {
/* 30 */     return "isready";
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 34 */     return null;
/*    */   }
/*    */   
/* 37 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCIIsReadyCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */