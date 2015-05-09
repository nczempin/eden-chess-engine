 package de.czempin.chess.commands;
 
 import de.czempin.chess.Engine;
 
 public abstract class AbstractCommand implements Command { protected de.czempin.chess.Brain brain;
   protected Engine engine;
   protected String actualParams;
   
   public AbstractCommand(Engine engine) { this.actualParams = null;
     this.engine = engine;
     if (this.engine != null)
       this.brain = engine.getBrain();
   }
   
   public AbstractCommand() {
     this(null);
   }
   
   public AbstractCommand(Engine engine, String line) {
     this(engine);
     this.actualParams = line;
   }
   
   public String execute(String parameters) {
     setParameters(parameters);
     return execute();
   }
   
   protected abstract String execute();
   
   public void setParameters(String parameters) {
     this.actualParams = stripCommand(parameters);
   }
   
   private String stripCommand(String parameters) {
     String commandString = commandString();
     return parameters.replaceFirst(commandString + " ", "");
   }
   
   protected abstract String commandString();
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/AbstractCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */