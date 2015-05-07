/*    */ package de.czempin.chess.commands.wb;
/*    */ 
/*    */ import de.czempin.chess.Brain;
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.Options;
/*    */ import de.czempin.chess.Protocol;
/*    */ import de.czempin.chess.commands.AbstractCommand;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class WBMoveCommand extends AbstractCommand
/*    */ {
/*    */   public WBMoveCommand(Engine engine, String line)
/*    */   {
/* 14 */     super(engine, line);
/*    */   }
/*    */   
/*    */   public static Command getInstance(String parameters)
/*    */   {
/* 19 */     if (instance == null)
/* 20 */       instance = new WBMoveCommand(null, null);
/* 21 */     instance.setParameters(parameters);
/* 22 */     return instance;
/*    */   }
/*    */   
/*    */   public String execute(String moveIn)
/*    */   {
/* 27 */     this.brain.makeMove(moveIn);
/* 28 */     if (this.engine.getForceMode())
/* 29 */       return "";
/* 30 */     if (Options.synchronous)
/*    */     {
/* 32 */       String move = this.brain.move();
/* 33 */       String moveString = Options.protocol.printMoveMade(move);
/* 34 */       return moveString;
/*    */     }
/*    */     
/* 37 */     this.engine.startBrain();
/* 38 */     return "";
/*    */   }
/*    */   
/*    */ 
/*    */   protected String execute()
/*    */   {
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 49 */     return null;
/*    */   }
/*    */   
/* 52 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBMoveCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */