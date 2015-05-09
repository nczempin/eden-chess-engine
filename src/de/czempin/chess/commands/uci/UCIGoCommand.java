 package de.czempin.chess.commands.uci;
 
 import de.czempin.chess.Engine;
 import de.czempin.chess.commands.Command;
 
 public class UCIGoCommand extends de.czempin.chess.commands.AbstractCommand
 {
   public UCIGoCommand(Engine engine, String line)
   {
     super(engine, line);
   }
   
   public static Command getInstance(String parameters) {
     if (instance == null)
       instance = new UCIGoCommand(null, null);
     instance.setParameters(parameters);
     return instance;
   }
   
   public void setParameters(String s) {}
   
   public String execute(String parameters)
   {
     int wtime = extractIntValue(parameters, "wtime");
     int btime = extractIntValue(parameters, "btime");
     int winc = extractIntValue(parameters, "winc");
     int binc = extractIntValue(parameters, "binc");
     int mtg = extractIntValue(parameters, "movestogo");
     if (mtg == 0)
       mtg = 25;
     this.engine.setMovesToGo(mtg);
     this.engine.setTimes(wtime, btime, winc, binc);
     int depth = extractIntValue(parameters, "depth");
     this.engine.setDepth(depth);
     if (this.engine.isSynchronous()) {
       String move = this.brain.move();
       return "bestmove " + move + "\n";
     }
     this.engine.startBrain();
     return "";
   }
   
   private int extractIntValue(String parameters, String string)
   {
     int index = parameters.indexOf(string);
     if (index == -1) {
       return 0;
     }
     String extracted = parameters.substring(
       parameters.indexOf(string) + string.length() + 1)
       .split(" ")[0];
     return Integer.parseInt(extracted);
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


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCIGoCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */