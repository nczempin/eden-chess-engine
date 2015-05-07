/*    */ package de.czempin.chess.commands.uci;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class UCIGoCommand extends de.czempin.chess.commands.AbstractCommand
/*    */ {
/*    */   public UCIGoCommand(Engine engine, String line)
/*    */   {
/* 10 */     super(engine, line);
/*    */   }
/*    */   
/*    */   public static Command getInstance(String parameters) {
/* 14 */     if (instance == null)
/* 15 */       instance = new UCIGoCommand(null, null);
/* 16 */     instance.setParameters(parameters);
/* 17 */     return instance;
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   public String execute(String parameters)
/*    */   {
/* 24 */     int wtime = extractIntValue(parameters, "wtime");
/* 25 */     int btime = extractIntValue(parameters, "btime");
/* 26 */     int winc = extractIntValue(parameters, "winc");
/* 27 */     int binc = extractIntValue(parameters, "binc");
/* 28 */     int mtg = extractIntValue(parameters, "movestogo");
/* 29 */     if (mtg == 0)
/* 30 */       mtg = 25;
/* 31 */     this.engine.setMovesToGo(mtg);
/* 32 */     this.engine.setTimes(wtime, btime, winc, binc);
/* 33 */     int depth = extractIntValue(parameters, "depth");
/* 34 */     this.engine.setDepth(depth);
/* 35 */     if (this.engine.isSynchronous()) {
/* 36 */       String move = this.brain.move();
/* 37 */       return "bestmove " + move + "\n";
/*    */     }
/* 39 */     this.engine.startBrain();
/* 40 */     return "";
/*    */   }
/*    */   
/*    */   private int extractIntValue(String parameters, String string)
/*    */   {
/* 45 */     int index = parameters.indexOf(string);
/* 46 */     if (index == -1) {
/* 47 */       return 0;
/*    */     }
/* 49 */     String extracted = parameters.substring(
/* 50 */       parameters.indexOf(string) + string.length() + 1)
/* 51 */       .split(" ")[0];
/* 52 */     return Integer.parseInt(extracted);
/*    */   }
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 57 */     return "go";
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 61 */     return null;
/*    */   }
/*    */   
/* 64 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCIGoCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */