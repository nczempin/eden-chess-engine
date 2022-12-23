 package de.czempin.chess.commands.uci;
 
 import de.czempin.chess.Brain;
 import de.czempin.chess.Engine;
 import de.czempin.chess.commands.Command;
 
 public class UCIIDCommand extends de.czempin.chess.commands.AbstractCommand
 {
   public UCIIDCommand(Engine engine)
   {
     super(engine);
   }
   
   public static Command getInstance() {
     if (instance == null)
       instance = new UCIIDCommand(null);
     return instance;
   }
   
   public String execute(String parameters) {
     String retVal = "id name " + this.brain.getName() + " " + this.brain.getVersion() + "\n";
     retVal = retVal + "id author " + this.brain.getAuthor() + "\n";
     retVal = retVal + de.czempin.chess.Options.printOptions();
     retVal = retVal + "uciok\n";
     return retVal;
   }
   
   public void setParameters(String s) {}
   
   protected String commandString()
   {
     return "uci";
   }
   
   protected String execute() {
     return null;
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCIIDCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */