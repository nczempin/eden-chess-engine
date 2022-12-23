 package de.czempin.chess.commands.uci;
 
 import de.czempin.chess.Engine;
 import de.czempin.chess.commands.Command;
 
 public class UCINewGameCommand extends de.czempin.chess.commands.AbstractCommand
 {
   public UCINewGameCommand(Engine engine)
   {
     super(engine);
   }
   
   public static Command getInstance() {
     if (instance == null)
       instance = new UCINewGameCommand(null);
     return instance;
   }
   
   public void setParameters(String s) {}
   
   public String execute(String parameters)
   {
     this.brain.initializeGame();
     return "isready\n";
   }
   
   protected String commandString() {
     return "ucinewgame";
   }
   
   protected String execute() {
     return null;
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCINewGameCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */