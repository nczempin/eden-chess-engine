/*    */ package de.czempin.chess.commands.wb;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class WBLevelCommand extends de.czempin.chess.commands.AbstractCommand
/*    */ {
/*    */   public WBLevelCommand(Engine engine)
/*    */   {
/* 10 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance(String parameters) {
/* 14 */     if (instance == null)
/* 15 */       instance = new WBLevelCommand(null);
/* 16 */     instance.setParameters(parameters);
/* 17 */     return instance;
/*    */   }
/*    */   
/*    */   public String execute() {
/* 21 */     String[] level = this.actualParams.split(" ");
/* 22 */     String movesToGo = level[0];
/* 23 */     String time = level[1];
/* 24 */     String inc = level[2];
/* 25 */     this.engine.setWbInc(1000 * Integer.parseInt(inc));
/* 26 */     this.engine.setWbTime(60000 * Integer.parseInt(time));
/* 27 */     int mtg = Integer.parseInt(movesToGo);
/* 28 */     this.engine.setMovesPerTimeControl(mtg);
/* 29 */     if (mtg == 0)
/* 30 */       mtg = 25;
/* 31 */     this.engine.setMovesToGo(mtg);
/* 32 */     return "";
/*    */   }
/*    */   
/*    */   protected String commandString() {
/* 36 */     return "level";
/*    */   }
/*    */   
/* 39 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBLevelCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */