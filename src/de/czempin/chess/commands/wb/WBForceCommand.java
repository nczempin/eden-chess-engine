 package de.czempin.chess.commands.wb;
 
 import de.czempin.chess.Engine;
 
 public class WBForceCommand extends de.czempin.chess.commands.AbstractCommand
 {
   public WBForceCommand(Engine engine)
   {
     super(engine);
   }
   
   protected String execute() {
     this.engine.setForceMode(true);
     return "";
   }
   
   protected String commandString() {
     return null;
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBForceCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */