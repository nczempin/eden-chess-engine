/*     */ package de.czempin.chess;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ public abstract class AbstractEngine implements Engine {
/*     */   static Thread brainThread;
/*     */   protected Brain brain;
/*     */   public static String brainMove;
/*     */   static Thread printInfoThread;
/*     */   
/*  11 */   public boolean isSynchronous() { return Options.synchronous; }
/*     */   
/*     */   public Brain getBrain()
/*     */   {
/*  15 */     return this.brain;
/*     */   }
/*     */   
/*     */   public void initializeGame() {
/*  19 */     this.brain.initializeGame();
/*     */   }
/*     */   
/*     */   protected void incrementTimeSpent(long l1) {}
/*     */   
/*     */   public void run()
/*     */   {
/*  26 */     this.c = new Chess(this);
/*  27 */     this.c.run();
/*     */   }
/*     */   
/*     */   public static void printPV() {
/*  31 */     Options.protocol.printPV();
/*     */   }
/*     */   
/*     */   public void updateMoveCount() {
/*  35 */     if (Options.DEBUG)
/*     */     {
/*  37 */       System.out.println("debug: moves to go before update: " + this.movestogo);
/*  38 */       System.out.println("debug: movesPerTimeControl: " + 
/*  39 */         this.movesPerTimeControl);
/*     */     }
/*  41 */     if (this.movesPerTimeControl > 0) {
/*  42 */       this.movestogo -= 1;
/*  43 */       if (this.movestogo == 0) {
/*  44 */         this.movestogo = this.movesPerTimeControl;
/*  45 */         decrementTimeSpent(getWbTime());
/*     */       }
/*  47 */       if (Options.DEBUG) {
/*  48 */         System.out.println("debug: moves to go after update: " + 
/*  49 */           this.movestogo);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void decrementTimeSpent(long l) {}
/*     */   
/*     */   protected long getWbTime() {
/*  57 */     return this.wbtime;
/*     */   }
/*     */   
/*     */   public void execute(String command) {
/*  61 */     this.c.execute(command);
/*     */   }
/*     */   
/*     */   public void startBrain() {
/*  65 */     stopBrain();
/*  66 */     brainThread = new Thread()
/*     */     {
/*     */       public void run() {
/*  69 */         if (Options.DEBUG) {
/*  70 */           System.out.println("debug: value of useTT:" + 
/*  71 */             Options.useTranspositionTable);
/*  72 */           System.out.println("debug: value of ownBook:" + 
/*  73 */             Options.ownBook);
/*     */         }
/*  75 */         String move = AbstractEngine.this.brain.move();
/*  76 */         AbstractEngine.brainMove = move;
/*  77 */         String moveString = AbstractEngine.printMove(move);
/*  78 */         System.out.println(moveString);
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  84 */     };
/*  85 */     brainThread.start();
/*  86 */     printInfoThread = new Thread()
/*     */     {
/*     */       public void run() {
/*  89 */         while (AbstractEngine.brainThread.isAlive()) {
/*  90 */           Options.protocol.printInfo();
/*     */           try {
/*  92 */             Thread.sleep(1000L);
/*     */           } catch (InterruptedException e) {
/*  94 */             e.printStackTrace();
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */ 
/* 102 */     };
/* 103 */     printInfoThread.start();
/*     */   }
/*     */   
/*     */   public void stopBrain()
/*     */   {
/* 108 */     if ((brainThread != null) && (brainThread.isAlive())) {
/* 109 */       String move = this.brain.getBestMoveSoFar();
/*     */       String toPrint;
/* 111 */       String toPrint; if (move == null) {
/* 112 */         toPrint = printMove(null);
/*     */       } else
/* 114 */         toPrint = printMove(move);
/* 115 */       System.out.println(toPrint);
/* 116 */       brainThread.stop();
/*     */     }
/* 118 */     if ((printInfoThread != null) && (printInfoThread.isAlive()))
/* 119 */       printInfoThread.stop();
/*     */   }
/*     */   
/*     */   static String printMove(String move) {
/* 123 */     return Options.protocol.printMoveMade(move);
/*     */   }
/*     */   
/*     */   public void setMovesToGo(int mtg) {
/* 127 */     this.movestogo = mtg;
/*     */   }
/*     */   
/*     */   public void setDepth(int depth) {
/* 131 */     this.depth = depth;
/*     */   }
/*     */   
/*     */   public int getDepth() {
/* 135 */     return this.depth;
/*     */   }
/*     */   
/*     */   public void setMovesPerTimeControl(int mtg) {
/* 139 */     this.movesPerTimeControl = mtg;
/*     */   }
/*     */   
/*     */   public void setTimeToNextControl(int i) {
/* 143 */     this.timeToNextControl = i;
/*     */   }
/*     */   
/*     */   public int getMovestogo() {
/* 147 */     return this.movestogo;
/*     */   }
/*     */   
/*     */   public abstract java.util.Collection getMultiPvs();
/*     */   
/*     */   public void setForceMode(boolean b) {
/* 153 */     this.forceMode = b;
/*     */   }
/*     */   
/*     */   public boolean getForceMode() {
/* 157 */     return this.forceMode;
/*     */   }
/*     */   
/*     */   public void setWbInc(int i) {
/* 161 */     this.wbinc = i;
/*     */   }
/*     */   
/*     */   public void setWbTime(int i) {
/* 165 */     this.wbtime = i;
/*     */   }
/*     */   
/*     */   public long getTimeToNextControl() {
/* 169 */     return this.timeToNextControl;
/*     */   }
/*     */   
/*     */   public void setTimePerMove(long l) {
/* 173 */     this.timePerMove = l;
/*     */   }
/*     */   
/*     */   public void setTimes(long wtime, long btime, long winc, long binc) {
/* 177 */     setWtime(wtime);
/* 178 */     setBtime(btime);
/* 179 */     setWinc(winc);
/* 180 */     setBinc(binc);
/*     */   }
/*     */   
/*     */   private void setBinc(long binc) {
/* 184 */     this.binc = binc;
/*     */   }
/*     */   
/*     */   private void setWinc(long winc) {
/* 188 */     this.winc = winc;
/*     */   }
/*     */   
/*     */   private void setBtime(long btime) {
/* 192 */     this.btime = btime;
/*     */   }
/*     */   
/*     */   private void setWtime(long wtime) {
/* 196 */     this.wtime = wtime;
/*     */   }
/*     */   
/*     */   public void updateInternalClock() {
/* 200 */     long timeTaken = System.currentTimeMillis() - Info.searchtime;
/* 201 */     long timeAdded = getWbInc();
/* 202 */     incrementTimeSpent(timeTaken - timeAdded);
/* 203 */     if (Options.DEBUG) {
/* 204 */       System.out.println("debug: time taken: " + timeTaken);
/* 205 */       System.out.println("debug: time added: " + timeAdded);
/* 206 */       System.out.println("debug: time spent: " + getTimeSpent());
/* 207 */       System.out.println("debug: time left should be: " + (
/* 208 */         getWbTime() - getTimeSpent()));
/*     */     }
/*     */   }
/*     */   
/*     */   private long getWbInc() {
/* 213 */     return this.wbinc;
/*     */   }
/*     */   
/*     */   public void setTimes(int wtime, int btime, int winc, int binc) {
/* 217 */     setWtime(wtime);
/* 218 */     setBtime(btime);
/* 219 */     setWinc(winc);
/* 220 */     setBinc(binc);
/*     */   }
/*     */   
/*     */   public long getBtime() {
/* 224 */     return this.btime;
/*     */   }
/*     */   
/*     */   public long getBinc() {
/* 228 */     return this.binc;
/*     */   }
/*     */   
/*     */   public long getWtime() {
/* 232 */     return this.wtime;
/*     */   }
/*     */   
/*     */   public long getWinc() {
/* 236 */     return this.winc;
/*     */   }
/*     */   
/*     */   public long getWbinc() {
/* 240 */     return this.wbinc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 247 */   public static boolean synchronous = true;
/*     */   private Chess c;
/*     */   public int movestogo;
/*     */   public int movesPerTimeControl;
/*     */   private long wbinc;
/*     */   private long wbtime;
/*     */   private long winc;
/*     */   private long wtime;
/*     */   private long binc;
/*     */   private long btime;
/*     */   private long timeToNextControl;
/*     */   private boolean forceMode;
/*     */   protected long timePerMove;
/*     */   private int depth;
/*     */   public static final int MATE_SCORE = 90000;
/*     */   public static final int MATE_THRESHOLD = 80000;
/*     */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/AbstractEngine.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */