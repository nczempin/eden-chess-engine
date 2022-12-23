 package de.czempin.chess.commands;
 
 import java.io.PrintStream;
 
 public class QuitCommand implements Command
 {
   public static Command getInstance()
   {
     if (instance == null)
       instance = new QuitCommand();
     return instance;
   }
   
   public String execute(String parameters) {
     System.out.println("Thanks for playing with " + parameters + ". Good-bye until next time!");
     try {
       Thread.sleep(1000L);
     } catch (InterruptedException e) {
       e.printStackTrace();
     }
     System.out.flush();
     System.exit(0);
     return null;
   }
   
 
 
 
   private static Command instance = null;
   
   public void setParameters(String s) {}
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/QuitCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */