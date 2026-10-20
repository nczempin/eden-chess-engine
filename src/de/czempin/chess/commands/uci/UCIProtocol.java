 package de.czempin.chess.commands.uci;
 
 import de.czempin.chess.AbstractProtocol;
 import de.czempin.chess.Engine;
 import de.czempin.chess.Info;
 import de.czempin.chess.Options;
 import de.czempin.chess.Variation;
 import de.czempin.chess.commands.Command;
 import de.czempin.chess.commands.QuitCommand;
 import de.czempin.chess.commands.StopCommand;
 import java.io.PrintStream;
 import java.util.Iterator;
 import java.util.SortedSet;
 import java.util.TreeSet;
 import java.util.Vector;
 
 public class UCIProtocol extends AbstractProtocol
 {
   public UCIProtocol(Engine engine)
   {
     super(engine);
   }
   
   public Command parse(String line) {
     if (line.equals("uci"))
       return new UCIIDCommand(this.engine);
     if (line.equals("isready"))
       return UCIIsReadyCommand.getInstance();
     if (line.equals("quit"))
       return QuitCommand.getInstance();
     if (line.equals("stop"))
       return new StopCommand(this.engine);
     if (line.startsWith("ucinewgame"))
       return new UCINewGameCommand(this.engine);
     if (line.startsWith("position"))
       return new de.czempin.chess.commands.PositionCommand(this.engine);
     if (line.startsWith("setoption"))
       return UCISetOptionCommand.getInstance(line);
     if (line.startsWith("go")) {
       return new UCIGoCommand(this.engine, line);
     }
     return de.czempin.chess.commands.UnknownCommand.getInstance();
   }
   
   public String printMoveMade(String move) {
     return "bestmove " + move;
   }
   
   public void printInfo() {
     if ((!Options.DEBUG) && (Info.idDepth < Options.minimumPlyForDisplay))
       return;
     synchronized (System.out) {
       System.out.print("info");
       System.out.print(" nodes " + Info.nodes);
       Info.time = System.currentTimeMillis() - Info.searchtime;
       long elapsed = Math.max(1L, Info.time + 1L);
       long nps = Math.max(0L, Info.nodes * 1000L / elapsed);
       System.out.print(" nps " + nps);
       if (Options.useTranspositionTable)
         System.out.print(" hashfull " + Info.hashFull);
       System.out.println();
       printDebugInfo();
       System.out.flush();
     }
   }
   
   public void printPV()
   {
     synchronized (System.out) {
       SortedSet values = new TreeSet();
       synchronized (de.czempin.chess.eden.Eden.multiPvs) {
         values.addAll(this.engine.getMultiPvs());
       }
       Iterator it = values.iterator();
       int i = 0;
       for (; it.hasNext(); System.out.println()) {
         i++; if (i > Options.multiPvDisplayNumber)
           break;
         Variation variation = (Variation)it.next();
         Vector v = variation.getLine();
         String pv = this.engine.getBrain().extractVariationString(v);
         System.out.print("info");
         int multipvRank = i;
         int multiPvValue = variation.getValue();
         int multiPvDepth = variation.getDepth();
         System.out.print(" depth " + multiPvDepth);
         if (Options.multiPvDisplayNumber > 1)
           System.out.print(" multipv " + multipvRank);
         if ((multiPvValue > 80000) && (multiPvValue <= 90000)) {
           int mateIn = (90000 - multiPvValue) / 2 + 1;
           System.out.print(" score mate " + mateIn);
         } else if ((multiPvValue < -80000) && (multiPvValue >= -90000)) {
           int mateIn = (-90000 - multiPvValue) / 2 - 1;
           System.out.print(" score mate " + mateIn);
         } else {
           System.out.print(" score cp " + multiPvValue);
         }
         System.out.print(" pv " + pv);
       }
       System.out.flush();
     }
   }
   
   public void printCurrmove(int currmovenumber, String currentmove, boolean withInfoPrefix)
   {
     if ((!Options.DEBUG) && (Info.idDepth < 5))
       return;
     if (currmovenumber == 0)
       return;
     synchronized (System.out) {
       if (withInfoPrefix)
         System.out.print("info");
       System.out.print(" currmove " + currentmove + " currmovenumber " + currmovenumber);
       System.out.print(" depth " + Info.idDepth);
       if (Info.seldepth > Info.idDepth)
         System.out.print(" seldepth " + Info.seldepth);
       if (withInfoPrefix) {
         System.out.println();
       }
       System.out.flush();
     }
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCIProtocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */