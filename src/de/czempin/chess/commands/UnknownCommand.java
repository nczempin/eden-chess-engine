/*    */ package de.czempin.chess.commands;
/*    */ 
/*    */ 
/*    */ public class UnknownCommand
/*    */   extends AbstractCommand
/*    */ {
/*    */   public static Command getInstance()
/*    */   {
/*  9 */     if (instance == null)
/* 10 */       instance = new UnknownCommand();
/* 11 */     return instance;
/*    */   }
/*    */   
/*    */   public String execute(String parameters) {
/* 15 */     return "Error (unknown command): " + parameters + "\n";
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 22 */     return null;
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 26 */     return null;
/*    */   }
/*    */   
/* 29 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/UnknownCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */