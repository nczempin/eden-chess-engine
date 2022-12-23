 package de.czempin.chess;
 
 import java.io.PrintStream;
 
 
 public class Options
 {
   public static String printOptions()
   {
     String retVal = "";
     retVal = retVal + "option name OwnBook type check default " + ownBook + "\n";
     return retVal;
   }
   
   public static void setOptions(String optionName, String optionValue) {
     if ("OwnBook".equals(optionName)) {
       boolean ownBook = toBoolean(optionValue);
       if (DEBUG)
         System.out.println("debug: ownBook set to " + ownBook);
     }
   }
   
   private static boolean toBoolean(String name) {
     return (name != null) && (name.equalsIgnoreCase("true"));
   }
   
   public static boolean useCheckExtensions = true;
   public static final int MOVES_PER_TIME_CONTROL = 25;
   public static boolean ownBook = true;
   public static int multiPvDisplayNumber = 1;
   public static boolean useKiller = true;
   public static boolean useForcedMoveExtension = true;
   public static boolean useTranspositionTable = true;
   public static Protocol protocol;
   public static boolean synchronous;
   public static boolean DEBUG = false;
   public static int minimumPlyForDisplay = 3;
   public static final String EDEN_HOME = "";
   public static final int MAX_CHECK_EXTENSIONS = 4;
   public static final int PAWNHASH_MAXSIZE = 256000;
   public static final int MAX_TRANSPOSITIONS = (int)(0.3D * Math.pow(10.0D, 6.0D));
   public static int MAXDEPTH_;
   public static int maxQuiescence = 99;
   public static boolean useQuiescence = true;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Options.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */