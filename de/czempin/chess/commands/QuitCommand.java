/*    */ package de.czempin.chess.commands;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class QuitCommand implements Command
/*    */ {
/*    */   public static Command getInstance()
/*    */   {
/*  9 */     if (instance == null)
/* 10 */       instance = new QuitCommand();
/* 11 */     return instance;
/*    */   }
/*    */   
/*    */   public String execute(String parameters) {
/* 15 */     System.out.println("Thanks for playing with " + parameters + ". Good-bye until next time!");
/*    */     try {
/* 17 */       Thread.sleep(1000L);
/*    */     } catch (InterruptedException e) {
/* 19 */       e.printStackTrace();
/*    */     }
/* 21 */     System.out.flush();
/* 22 */     System.exit(0);
/* 23 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 29 */   private static Command instance = null;
/*    */   
/*    */   public void setParameters(String s) {}
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/QuitCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */