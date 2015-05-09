 package de.czempin.chess.commands.wb;
 
 import de.czempin.chess.Engine;
 import de.czempin.chess.commands.Command;
 
 public class WBGoCommand extends de.czempin.chess.commands.AbstractCommand
 {
   public WBGoCommand(Engine engine)
   {
     super(engine);
   }
   
   public static Command getInstance(String parameters) {
     if (instance == null)
       instance = new WBGoCommand(null);
     instance.setParameters(parameters);
     return instance;
   }
   
   public void setParameters(String s) {}
   
   public String execute(String parameters)
   {
     this.engine.setForceMode(false);
     if (this.engine.isSynchronous()) {
       String move = this.brain.move();
       return "move " + move + "\n";
     }
     this.engine.startBrain();
     return "";
   }
   
   protected String commandString()
   {
     return "go";
   }
   
   protected String execute() {
     return null;
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBGoCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */