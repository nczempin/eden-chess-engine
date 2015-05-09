 package de.czempin.chess.commands.wb;
 
 import de.czempin.chess.Engine;
 import de.czempin.chess.commands.AbstractCommand;
 import de.czempin.chess.commands.Command;
 
 public class WBTimeCommand
   extends AbstractCommand
 {
   public WBTimeCommand(Engine engine)
   {
     super(engine);
   }
   
   public static Command getInstance(String parameters)
   {
     if (instance == null)
       instance = new WBTimeCommand(null);
     instance.setParameters(parameters);
     return instance;
   }
   
   protected String execute()
   {
     int timeLeft = Integer.parseInt(this.actualParams);
     this.engine.setTimeToNextControl(timeLeft * 10);
     return "";
   }
   
   protected String commandString()
   {
     return "time";
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBTimeCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */