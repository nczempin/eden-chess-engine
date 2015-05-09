 package de.czempin.chess.commands;
 
 import de.czempin.chess.Engine;
 
 public class IgnoredCommand extends AbstractCommand
 {
   public IgnoredCommand(Engine engine) {
     super(engine);
   }
   
   public static Command getInstance() {
     if (instance == null)
       instance = new IgnoredCommand(null);
     return instance;
   }
   
   public String execute(String parameters) {
     return "\n";
   }
   
   public void setParameters(String s) {}
   
   protected String commandString()
   {
     return null;
   }
   
   protected String execute() {
     return null;
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/IgnoredCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */