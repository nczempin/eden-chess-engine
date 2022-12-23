 package de.czempin.chess.commands.wb;
 
 import de.czempin.chess.Engine;
 import de.czempin.chess.commands.Command;
 
 public class WBIDCommand extends de.czempin.chess.commands.AbstractCommand
 {
   public WBIDCommand(Engine engine)
   {
     super(engine);
   }
   
   public static Command getInstance() {
     if (instance == null)
       instance = new WBIDCommand(null);
     return instance;
   }
   
   public String execute(String parameters) {
     String retVal = "";
     retVal = retVal + "feature done=0";
     retVal = retVal + " myname=\"" + this.brain.getName() + " " + this.brain.getVersion() + "\"";
     retVal = retVal + " variants=\"normal\"";
     retVal = retVal + " name=1";
     retVal = retVal + " time=1";
     retVal = retVal + " setboard=0";
     retVal = retVal + " reuse=0";
     retVal = retVal + " ping=0";
     retVal = retVal + " playother=0";
     retVal = retVal + " san=0";
     retVal = retVal + " usermove=0";
     retVal = retVal + " draw=0";
     retVal = retVal + " sigint=0";
     retVal = retVal + " sigterm=0";
     retVal = retVal + " analyze=0";
     retVal = retVal + " pause=0";
     retVal = retVal + " colors=0";
     retVal = retVal + " ics=0";
     retVal = retVal + " done=1\n";
     return retVal;
   }
   
   public void setParameters(String s) {}
   
   protected String commandString()
   {
     return "uci";
   }
   
   protected String execute() {
     return null;
   }
   
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBIDCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */