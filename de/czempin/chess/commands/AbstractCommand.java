/*    */ package de.czempin.chess.commands;
/*    */ 
/*    */ import de.czempin.chess.Engine;
/*    */ 
/*    */ public abstract class AbstractCommand implements Command { protected de.czempin.chess.Brain brain;
/*    */   protected Engine engine;
/*    */   protected String actualParams;
/*    */   
/*  9 */   public AbstractCommand(Engine engine) { this.actualParams = null;
/* 10 */     this.engine = engine;
/* 11 */     if (this.engine != null)
/* 12 */       this.brain = engine.getBrain();
/*    */   }
/*    */   
/*    */   public AbstractCommand() {
/* 16 */     this(null);
/*    */   }
/*    */   
/*    */   public AbstractCommand(Engine engine, String line) {
/* 20 */     this(engine);
/* 21 */     this.actualParams = line;
/*    */   }
/*    */   
/*    */   public String execute(String parameters) {
/* 25 */     setParameters(parameters);
/* 26 */     return execute();
/*    */   }
/*    */   
/*    */   protected abstract String execute();
/*    */   
/*    */   public void setParameters(String parameters) {
/* 32 */     this.actualParams = stripCommand(parameters);
/*    */   }
/*    */   
/*    */   private String stripCommand(String parameters) {
/* 36 */     String commandString = commandString();
/* 37 */     return parameters.replaceFirst(commandString + " ", "");
/*    */   }
/*    */   
/*    */   protected abstract String commandString();
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/AbstractCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */