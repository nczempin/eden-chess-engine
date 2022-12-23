 package de.czempin.chess.commands.wb;
 
 import de.czempin.chess.Brain;
 import de.czempin.chess.Engine;
 import de.czempin.chess.Options;
 import de.czempin.chess.Protocol;
 import de.czempin.chess.commands.AbstractCommand;
 import de.czempin.chess.commands.Command;
 
 public class WBMoveCommand extends AbstractCommand
 {
   public WBMoveCommand(Engine engine, String line)
   {
     super(engine, line);
   }
   
   public static Command getInstance(String parameters)
   {
     if (instance == null)
       instance = new WBMoveCommand(null, null);
     instance.setParameters(parameters);
     return instance;
   }
   
   public String execute(String moveIn)
   {
     this.brain.makeMove(moveIn);
     if (this.engine.getForceMode())
       return "";
     if (Options.synchronous)
     {
       String move = this.brain.move();
       String moveString = Options.protocol.printMoveMade(move);
       return moveString;
     }
     
     this.engine.startBrain();
     return "";
   }
   
 
   protected String execute()
   {
     return null;
   }
   
   protected String commandString()
   {
     return null;
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBMoveCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */