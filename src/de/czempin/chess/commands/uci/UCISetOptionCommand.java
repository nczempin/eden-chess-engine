 package de.czempin.chess.commands.uci;
 
 import de.czempin.chess.Options;
 import de.czempin.chess.commands.AbstractCommand;
 import de.czempin.chess.commands.Command;
 import java.io.PrintStream;
 
 public class UCISetOptionCommand
   extends AbstractCommand
 {
   public static Command getInstance(String parameters)
   {
     if (instance == null)
       instance = new UCISetOptionCommand();
     instance.setParameters(parameters);
     return instance;
   }
   
   public void setParameters(String s) {}
   
   public String execute(String parameters)
   {
     String optionName = extractStringValue(parameters, "name");
     String optionValue = extractStringValue(parameters, "value");
     if (Options.DEBUG)
       System.out.println("debug: name=" + optionName + ", value=" + 
         optionValue);
     Options.setOptions(optionName, optionValue);
     return "";
   }
   
   private String extractStringValue(String parameters, String string) {
     int index = parameters.indexOf(string);
     if (index == -1) {
       return null;
     }
     String extracted = parameters.substring(
       parameters.indexOf(string) + string.length() + 1)
       .split(" ")[0];
     return extracted;
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


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCISetOptionCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */