 package de.czempin.chess.commands.uci;
 
 import de.czempin.chess.commands.AbstractCommand;
 import de.czempin.chess.commands.Command;
 
 
 public class UCIIsReadyCommand
   extends AbstractCommand
 {
   public static Command getInstance()
   {
     if (instance == null)
       instance = new UCIIsReadyCommand();
     return instance;
   }
   
   public void setParameters(String s) {}
   
   public String execute(String parameters)
   {
     try {
       Thread.sleep(1000L);
     } catch (InterruptedException e) {
       e.printStackTrace();
     }
     return "\nreadyok\n";
   }
   
   protected String commandString() {
     return "isready";
   }
   
   protected String execute() {
     return null;
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCIIsReadyCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */