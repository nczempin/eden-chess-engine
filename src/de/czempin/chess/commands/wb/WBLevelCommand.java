 package de.czempin.chess.commands.wb;
 
 import de.czempin.chess.Engine;
 import de.czempin.chess.commands.Command;
 
 public class WBLevelCommand extends de.czempin.chess.commands.AbstractCommand
 {
   public WBLevelCommand(Engine engine)
   {
     super(engine);
   }
   
   public static Command getInstance(String parameters) {
     if (instance == null)
       instance = new WBLevelCommand(null);
     instance.setParameters(parameters);
     return instance;
   }
   
   public String execute() {
     String[] level = this.actualParams.split(" ");
     String movesToGo = level[0];
     String time = level[1];
     String inc = level[2];
     this.engine.setWbInc(1000 * Integer.parseInt(inc));
     this.engine.setWbTime(60000 * Integer.parseInt(time));
     int mtg = Integer.parseInt(movesToGo);
     this.engine.setMovesPerTimeControl(mtg);
     if (mtg == 0)
       mtg = 25;
     this.engine.setMovesToGo(mtg);
     return "";
   }
   
   protected String commandString() {
     return "level";
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBLevelCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */