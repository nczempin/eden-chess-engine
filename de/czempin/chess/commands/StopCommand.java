/*    */ package de.czempin.chess.commands;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ 
/*    */ public class StopCommand extends AbstractCommand
/*    */ {
/*    */   public StopCommand(Engine engine) {
/*  8 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance() {
/* 12 */     if (instance == null)
/* 13 */       instance = new StopCommand(null);
/* 14 */     return instance;
/*    */   }
/*    */   
/*    */   public String execute(String parameters) {
/* 18 */     this.engine.stopBrain();
/* 19 */     return "";
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   protected String execute()
/*    */   {
/* 26 */     return null;
/*    */   }
/*    */   
/*    */   protected String commandString() {
/* 30 */     return null;
/*    */   }
/*    */   
/* 33 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/StopCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */