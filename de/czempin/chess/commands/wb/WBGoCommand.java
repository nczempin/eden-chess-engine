/*    */ package de.czempin.chess.commands.wb;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class WBGoCommand extends de.czempin.chess.commands.AbstractCommand
/*    */ {
/*    */   public WBGoCommand(Engine engine)
/*    */   {
/* 10 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance(String parameters) {
/* 14 */     if (instance == null)
/* 15 */       instance = new WBGoCommand(null);
/* 16 */     instance.setParameters(parameters);
/* 17 */     return instance;
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   public String execute(String parameters)
/*    */   {
/* 24 */     this.engine.setForceMode(false);
/* 25 */     if (this.engine.isSynchronous()) {
/* 26 */       String move = this.brain.move();
/* 27 */       return "move " + move + "\n";
/*    */     }
/* 29 */     this.engine.startBrain();
/* 30 */     return "";
/*    */   }
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 35 */     return "go";
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 39 */     return null;
/*    */   }
/*    */   
/* 42 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBGoCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */