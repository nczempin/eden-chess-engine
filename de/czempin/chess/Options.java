/*    */ package de.czempin.chess;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ 
/*    */ public class Options
/*    */ {
/*    */   public static String printOptions()
/*    */   {
/* 10 */     String retVal = "";
/* 11 */     retVal = retVal + "option name OwnBook type check default " + ownBook + "\n";
/* 12 */     return retVal;
/*    */   }
/*    */   
/*    */   public static void setOptions(String optionName, String optionValue) {
/* 16 */     if ("OwnBook".equals(optionName)) {
/* 17 */       boolean ownBook = toBoolean(optionValue);
/* 18 */       if (DEBUG)
/* 19 */         System.out.println("debug: ownBook set to " + ownBook);
/*    */     }
/*    */   }
/*    */   
/*    */   private static boolean toBoolean(String name) {
/* 24 */     return (name != null) && (name.equalsIgnoreCase("true"));
/*    */   }
/*    */   
/* 27 */   public static boolean useCheckExtensions = true;
/*    */   public static final int MOVES_PER_TIME_CONTROL = 25;
/* 29 */   public static boolean ownBook = true;
/* 30 */   public static int multiPvDisplayNumber = 1;
/* 31 */   public static boolean useKiller = true;
/* 32 */   public static boolean useForcedMoveExtension = true;
/* 33 */   public static boolean useTranspositionTable = true;
/*    */   public static Protocol protocol;
/*    */   public static boolean synchronous;
/* 36 */   public static boolean DEBUG = false;
/* 37 */   public static int minimumPlyForDisplay = 3;
/*    */   public static final String EDEN_HOME = "";
/*    */   public static final int MAX_CHECK_EXTENSIONS = 4;
/*    */   public static final int PAWNHASH_MAXSIZE = 256000;
/* 41 */   public static final int MAX_TRANSPOSITIONS = (int)(0.3D * Math.pow(10.0D, 6.0D));
/*    */   public static int MAXDEPTH_;
/* 43 */   public static int maxQuiescence = 99;
/* 44 */   public static boolean useQuiescence = true;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Options.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */