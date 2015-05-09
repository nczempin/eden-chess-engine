 package de.czempin.chess.commands.wb;
 
 import de.czempin.chess.Engine;
 import de.czempin.chess.Info;
 import de.czempin.chess.Variation;
 import de.czempin.chess.commands.Command;
 import de.czempin.chess.commands.IgnoredCommand;
 import de.czempin.chess.commands.UnknownCommand;
 import java.io.PrintStream;
 import java.util.SortedSet;
 import java.util.TreeSet;
 import java.util.Vector;
 
 public class WinBoardProtocol implements de.czempin.chess.Protocol
 {
   Engine engine;
   
   public WinBoardProtocol(Engine engine)
   {
     this.engine = engine;
   }
   
   public Command parse(String line) {
     if (line.equals("xboard"))
       return IgnoredCommand.getInstance();
     if (line.equals("quit"))
       return de.czempin.chess.commands.QuitCommand.getInstance();
     if (line.equals("force"))
       return new WBForceCommand(this.engine);
     if (line.equals("?"))
       return new de.czempin.chess.commands.StopCommand(this.engine);
     if (line.startsWith("new"))
       return new WBNewGameCommand(this.engine);
     if (line.startsWith("level"))
       return new WBLevelCommand(this.engine);
     if (line.startsWith("time"))
       return new WBTimeCommand(this.engine);
     if (line.startsWith("otim"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("random"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("post"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("hard"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("easy"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("computer"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("name"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("setboard"))
       return WBSetboardCommand.getInstance();
     if (line.startsWith("accepted"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("exit"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("."))
       return IgnoredCommand.getInstance();
     if (line.startsWith("bk"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("hint"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("undo"))
       return UnknownCommand.getInstance();
     if (line.startsWith("sd "))
       return UnknownCommand.getInstance();
     if (line.startsWith("st "))
       return UnknownCommand.getInstance();
     if (line.startsWith("result"))
       return IgnoredCommand.getInstance();
     if (line.startsWith("go"))
       return new WBGoCommand(this.engine);
     if (line.startsWith("protover"))
       return new WBIDCommand(this.engine);
     String command = line.split(" ")[0];
     int length = command.length();
     if ((length == 4) || (length == 5)) {
       return new WBMoveCommand(this.engine, line);
     }
     return UnknownCommand.getInstance();
   }
   
   public String printMoveMade(String move) {
     this.engine.updateMoveCount();
     this.engine.updateInternalClock();
     return "move " + move + "\n";
   }
   
 
   public void printInfo() {}
   
   public void printPV()
   {
     SortedSet values = new TreeSet();
     values.addAll(this.engine.getMultiPvs());
     Variation variation = (Variation)values.first();
     Vector v = variation.getLine();
     String pv = this.engine.getBrain().extractVariationString(v);
     int multiPvValue = variation.getValue();
     int multiPvDepth = variation.getDepth();
     System.out.print(multiPvDepth + "\t");
     System.out.print(multiPvValue + "\t");
     int time = (int)(Info.time / 10L);
     System.out.print(time + "\t");
     System.out.print(Info.nodes + "\t");
     System.out.print(pv);
     System.out.println();
   }
   
 
   public static void printWBdata() {}
   
   public void calculateTimePerMove()
   {
     long timeLeft = this.engine.getTimeToNextControl();
     if (de.czempin.chess.Options.DEBUG)
       System.out.println("debug: base time left: " + timeLeft);
     long wbinc = this.engine.getWbinc();
     long tpm = this.engine.calculateTimePerMove(timeLeft, wbinc);
     this.engine.setTimePerMove(tpm);
   }
   
   public void printCurrmove(int i, String s, boolean flag) {}
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WinBoardProtocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */