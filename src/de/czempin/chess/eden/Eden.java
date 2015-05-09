 package de.czempin.chess.eden;
 
 import de.czempin.chess.AbstractEngine;
 import de.czempin.chess.Chess;
 import de.czempin.chess.Engine;
 import de.czempin.chess.Info;
 import de.czempin.chess.Options;
 import de.czempin.chess.Protocol;
 import de.czempin.chess.eden.brain.EdenBrain;
 import java.util.Collection;
 import java.util.SortedMap;
 import java.util.TreeMap;
 
 public class Eden extends AbstractEngine
 {
   public static final String VERSION = "0.0.13";
   
   public Eden()
   {
     this.brain = new EdenBrain(this);
   }
   
   public static void main(String[] args)
   {
     Engine e = new Eden();
     e.run();
   }
   
   public void run()
   {
     this.c = new Chess(this);
     this.c.run();
   }
   
   public void setMovesToGo(int mtg)
   {
     this.movestogo = mtg;
   }
   
   public static void printCurrmove(int currmovenumber, String currentmove, boolean withInfo)
   {
     Options.protocol.printCurrmove(currmovenumber, currentmove, withInfo);
   }
   
   public static void printInfo()
   {
     Options.protocol.printInfo();
   }
   
   public static void calculateTimePerMove()
   {
     Options.protocol.calculateTimePerMove();
   }
   
   public static void printPV()
   {
     Options.protocol.printPV();
   }
   
   public Collection getMultiPvs()
   {
     return multiPvs.values();
   }
   
   public boolean whiteToMove()
   {
     return this.brain.whiteToMove();
   }
   
   public long calculateTimePerMove(long t, long inc)
   {
     if ((t == 0L) && (inc == 0L))
       return -1L;
     int slice = getMovestogo() + 1;
     long retVal = (t - inc) / slice + inc - 500L;
     if (retVal < 100L)
       retVal = 100L;
     return retVal;
   }
   
   public long getTimeSpent()
   {
     return 0L;
   }
   
   public boolean timeUp()
   {
     if (this.timePerMove <= 0L)
       return false;
     Info.time = System.currentTimeMillis() - Info.searchtime;
     return Info.time >= this.timePerMove;
   }
   
 
   public static boolean DEBUG = false;
   private Chess c;
   public static long btime;
   public static long binc;
   public static long timeSpent;
   public static int timeToNextControl;
   public static int currmovenumber;
   public static Move currentmove;
   public static SortedMap multiPvs = new TreeMap();
   static Thread brainThread;
   static Thread printInfoThread;
   public static boolean useQuiescence = true;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/Eden.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */