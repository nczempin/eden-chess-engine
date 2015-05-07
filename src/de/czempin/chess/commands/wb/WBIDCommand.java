/*    */ package de.czempin.chess.commands.wb;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ import de.czempin.chess.commands.Command;
/*    */ 
/*    */ public class WBIDCommand extends de.czempin.chess.commands.AbstractCommand
/*    */ {
/*    */   public WBIDCommand(Engine engine)
/*    */   {
/* 10 */     super(engine);
/*    */   }
/*    */   
/*    */   public static Command getInstance() {
/* 14 */     if (instance == null)
/* 15 */       instance = new WBIDCommand(null);
/* 16 */     return instance;
/*    */   }
/*    */   
/*    */   public String execute(String parameters) {
/* 20 */     String retVal = "";
/* 21 */     retVal = retVal + "feature done=0";
/* 22 */     retVal = retVal + " myname=\"" + this.brain.getName() + " " + this.brain.getVersion() + "\"";
/* 23 */     retVal = retVal + " variants=\"normal\"";
/* 24 */     retVal = retVal + " name=1";
/* 25 */     retVal = retVal + " time=1";
/* 26 */     retVal = retVal + " setboard=0";
/* 27 */     retVal = retVal + " reuse=0";
/* 28 */     retVal = retVal + " ping=0";
/* 29 */     retVal = retVal + " playother=0";
/* 30 */     retVal = retVal + " san=0";
/* 31 */     retVal = retVal + " usermove=0";
/* 32 */     retVal = retVal + " draw=0";
/* 33 */     retVal = retVal + " sigint=0";
/* 34 */     retVal = retVal + " sigterm=0";
/* 35 */     retVal = retVal + " analyze=0";
/* 36 */     retVal = retVal + " pause=0";
/* 37 */     retVal = retVal + " colors=0";
/* 38 */     retVal = retVal + " ics=0";
/* 39 */     retVal = retVal + " done=1\n";
/* 40 */     return retVal;
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 47 */     return "uci";
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 51 */     return null;
/*    */   }
/*    */   
/* 54 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBIDCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */