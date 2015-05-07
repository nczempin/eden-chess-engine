/*    */ package de.czempin.chess.commands.wb;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.commands.AbstractCommand;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class WBTimeCommand
/*    */   extends AbstractCommand
/*    */ {
/*    */   public WBTimeCommand(Engine engine)
/*    */   {
/* 12 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance(String parameters)
/*    */   {
/* 17 */     if (instance == null)
/* 18 */       instance = new WBTimeCommand(null);
/* 19 */     instance.setParameters(parameters);
/* 20 */     return instance;
/*    */   }
/*    */   
/*    */   protected String execute()
/*    */   {
/* 25 */     int timeLeft = Integer.parseInt(this.actualParams);
/* 26 */     this.engine.setTimeToNextControl(timeLeft * 10);
/* 27 */     return "";
/*    */   }
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 32 */     return "time";
/*    */   }
/*    */   
/* 35 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBTimeCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */