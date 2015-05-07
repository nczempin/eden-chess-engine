/*     */ package de.czempin.chess.eden;
/*     */ 
/*     */ import de.czempin.chess.AbstractEngine;
/*     */ import de.czempin.chess.Chess;
/*     */ import de.czempin.chess.Engine;
/*     */ import de.czempin.chess.Info;
/*     */ import de.czempin.chess.Options;
/*     */ import de.czempin.chess.Protocol;
/*     */ import de.czempin.chess.eden.brain.EdenBrain;
/*     */ import java.util.Collection;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ 
/*     */ public class Eden extends AbstractEngine
/*     */ {
/*     */   public static final String VERSION = "0.0.13";
/*     */   
/*     */   public Eden()
/*     */   {
/*  20 */     this.brain = new EdenBrain(this);
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*  25 */     Engine e = new Eden();
/*  26 */     e.run();
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  31 */     this.c = new Chess(this);
/*  32 */     this.c.run();
/*     */   }
/*     */   
/*     */   public void setMovesToGo(int mtg)
/*     */   {
/*  37 */     this.movestogo = mtg;
/*     */   }
/*     */   
/*     */   public static void printCurrmove(int currmovenumber, String currentmove, boolean withInfo)
/*     */   {
/*  42 */     Options.protocol.printCurrmove(currmovenumber, currentmove, withInfo);
/*     */   }
/*     */   
/*     */   public static void printInfo()
/*     */   {
/*  47 */     Options.protocol.printInfo();
/*     */   }
/*     */   
/*     */   public static void calculateTimePerMove()
/*     */   {
/*  52 */     Options.protocol.calculateTimePerMove();
/*     */   }
/*     */   
/*     */   public static void printPV()
/*     */   {
/*  57 */     Options.protocol.printPV();
/*     */   }
/*     */   
/*     */   public Collection getMultiPvs()
/*     */   {
/*  62 */     return multiPvs.values();
/*     */   }
/*     */   
/*     */   public boolean whiteToMove()
/*     */   {
/*  67 */     return this.brain.whiteToMove();
/*     */   }
/*     */   
/*     */   public long calculateTimePerMove(long t, long inc)
/*     */   {
/*  72 */     if ((t == 0L) && (inc == 0L))
/*  73 */       return -1L;
/*  74 */     int slice = getMovestogo() + 1;
/*  75 */     long retVal = (t - inc) / slice + inc - 500L;
/*  76 */     if (retVal < 100L)
/*  77 */       retVal = 100L;
/*  78 */     return retVal;
/*     */   }
/*     */   
/*     */   public long getTimeSpent()
/*     */   {
/*  83 */     return 0L;
/*     */   }
/*     */   
/*     */   public boolean timeUp()
/*     */   {
/*  88 */     if (this.timePerMove <= 0L)
/*  89 */       return false;
/*  90 */     Info.time = System.currentTimeMillis() - Info.searchtime;
/*  91 */     return Info.time >= this.timePerMove;
/*     */   }
/*     */   
/*     */ 
/*  95 */   public static boolean DEBUG = false;
/*     */   private Chess c;
/*     */   public static long btime;
/*     */   public static long binc;
/*     */   public static long timeSpent;
/*     */   public static int timeToNextControl;
/*     */   public static int currmovenumber;
/*     */   public static Move currentmove;
/* 103 */   public static SortedMap multiPvs = new TreeMap();
/*     */   static Thread brainThread;
/*     */   static Thread printInfoThread;
/* 106 */   public static boolean useQuiescence = true;
/*     */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/Eden.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */