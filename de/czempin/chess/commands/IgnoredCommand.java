/*    */ package de.czempin.chess.commands;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ 
/*    */ public class IgnoredCommand extends AbstractCommand
/*    */ {
/*    */   public IgnoredCommand(Engine engine) {
/*  8 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance() {
/* 12 */     if (instance == null)
/* 13 */       instance = new IgnoredCommand(null);
/* 14 */     return instance;
/*    */   }
/*    */   
/*    */   public String execute(String parameters) {
/* 18 */     return "\n";
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 25 */     return null;
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 29 */     return null;
/*    */   }
/*    */   
/* 32 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/IgnoredCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */