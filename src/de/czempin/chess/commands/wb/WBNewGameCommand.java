/*    */ package de.czempin.chess.commands.wb;
/*    */ 
/*    */ import de.czempin.chess.Brain;
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.commands.AbstractCommand;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class WBNewGameCommand
/*    */   extends AbstractCommand
/*    */ {
/*    */   public WBNewGameCommand(Engine engine)
/*    */   {
/* 13 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance()
/*    */   {
/* 18 */     if (instance == null)
/* 19 */       instance = new WBNewGameCommand(null);
/* 20 */     return instance;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setParameters(String s) {}
/*    */   
/*    */ 
/*    */   public String execute(String parameters)
/*    */   {
/* 29 */     this.brain.initializeGame();
/* 30 */     return "";
/*    */   }
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 35 */     return "new";
/*    */   }
/*    */   
/*    */   protected String execute()
/*    */   {
/* 40 */     return null;
/*    */   }
/*    */   
/* 43 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBNewGameCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */