 package de.czempin.chess;
 
 import java.io.PrintStream;
 
 public abstract class AbstractEngine implements Engine {
   static Thread brainThread;
   protected Brain brain;
   public static String brainMove;
   static Thread printInfoThread;
   
   public boolean isSynchronous() { return Options.synchronous; }
   
   public Brain getBrain()
   {
     return this.brain;
   }
   
   public void initializeGame() {
     this.brain.initializeGame();
   }
   
   protected void incrementTimeSpent(long l1) {}
   
   public void run()
   {
     this.c = new Chess(this);
     this.c.run();
   }
   
   public static void printPV() {
     Options.protocol.printPV();
   }
   
   public void updateMoveCount() {
     if (Options.DEBUG)
     {
       System.out.println("debug: moves to go before update: " + this.movestogo);
       System.out.println("debug: movesPerTimeControl: " + 
         this.movesPerTimeControl);
     }
     if (this.movesPerTimeControl > 0) {
       this.movestogo -= 1;
       if (this.movestogo == 0) {
         this.movestogo = this.movesPerTimeControl;
         decrementTimeSpent(getWbTime());
       }
       if (Options.DEBUG) {
         System.out.println("debug: moves to go after update: " + 
           this.movestogo);
       }
     }
   }
   
   private void decrementTimeSpent(long l) {}
   
   protected long getWbTime() {
     return this.wbtime;
   }
   
   public void execute(String command) {
     this.c.execute(command);
   }
   
   public void startBrain() {
     stopBrain();
     brainThread = new Thread()
     {
       public void run() {
         if (Options.DEBUG) {
           System.out.println("debug: value of useTT:" + 
             Options.useTranspositionTable);
           System.out.println("debug: value of ownBook:" + 
             Options.ownBook);
         }
         String move = AbstractEngine.this.brain.move();
         AbstractEngine.brainMove = move;
         String moveString = AbstractEngine.printMove(move);
         System.out.println(moveString);
 
       }
       
 
 
     };
     brainThread.start();
     printInfoThread = new Thread()
     {
       public void run() {
         while (AbstractEngine.brainThread.isAlive()) {
           Options.protocol.printInfo();
           try {
             Thread.sleep(1000L);
           } catch (InterruptedException e) {
             e.printStackTrace();
           }
           
         }
         
       }
       
 
     };
     printInfoThread.start();
   }
   
   public void stopBrain()
   {
     if ((brainThread != null) && (brainThread.isAlive())) {
       String move = this.brain.getBestMoveSoFar();
       String toPrint; if (move == null) {
         toPrint = printMove(null);
       } else
         toPrint = printMove(move);
       System.out.println(toPrint);
       brainThread.stop();
     }
     if ((printInfoThread != null) && (printInfoThread.isAlive()))
       printInfoThread.stop();
   }
   
   static String printMove(String move) {
     return Options.protocol.printMoveMade(move);
   }
   
   public void setMovesToGo(int mtg) {
     this.movestogo = mtg;
   }
   
   public void setDepth(int depth) {
     this.depth = depth;
   }
   
   public int getDepth() {
     return this.depth;
   }
   
   public void setMovesPerTimeControl(int mtg) {
     this.movesPerTimeControl = mtg;
   }
   
   public void setTimeToNextControl(int i) {
     this.timeToNextControl = i;
   }
   
   public int getMovestogo() {
     return this.movestogo;
   }
   
   public abstract java.util.Collection getMultiPvs();
   
   public void setForceMode(boolean b) {
     this.forceMode = b;
   }
   
   public boolean getForceMode() {
     return this.forceMode;
   }
   
   public void setWbInc(int i) {
     this.wbinc = i;
   }
   
   public void setWbTime(int i) {
     this.wbtime = i;
   }
   
   public long getTimeToNextControl() {
     return this.timeToNextControl;
   }
   
   public void setTimePerMove(long l) {
     this.timePerMove = l;
   }
   
   public void setTimes(long wtime, long btime, long winc, long binc) {
     setWtime(wtime);
     setBtime(btime);
     setWinc(winc);
     setBinc(binc);
   }
   
   private void setBinc(long binc) {
     this.binc = binc;
   }
   
   private void setWinc(long winc) {
     this.winc = winc;
   }
   
   private void setBtime(long btime) {
     this.btime = btime;
   }
   
   private void setWtime(long wtime) {
     this.wtime = wtime;
   }
   
   public void updateInternalClock() {
     long timeTaken = System.currentTimeMillis() - Info.searchtime;
     long timeAdded = getWbInc();
     incrementTimeSpent(timeTaken - timeAdded);
     if (Options.DEBUG) {
       System.out.println("debug: time taken: " + timeTaken);
       System.out.println("debug: time added: " + timeAdded);
       System.out.println("debug: time spent: " + getTimeSpent());
       System.out.println("debug: time left should be: " + (
         getWbTime() - getTimeSpent()));
     }
   }
   
   private long getWbInc() {
     return this.wbinc;
   }
   
   public void setTimes(int wtime, int btime, int winc, int binc) {
     setWtime(wtime);
     setBtime(btime);
     setWinc(winc);
     setBinc(binc);
   }
   
   public long getBtime() {
     return this.btime;
   }
   
   public long getBinc() {
     return this.binc;
   }
   
   public long getWtime() {
     return this.wtime;
   }
   
   public long getWinc() {
     return this.winc;
   }
   
   public long getWbinc() {
     return this.wbinc;
   }
   
 
 
 
 
   public static boolean synchronous = true;
   private Chess c;
   public int movestogo;
   public int movesPerTimeControl;
   private long wbinc;
   private long wbtime;
   private long winc;
   private long wtime;
   private long binc;
   private long btime;
   private long timeToNextControl;
   private boolean forceMode;
   protected long timePerMove;
   private int depth;
   public static final int MATE_SCORE = 90000;
   public static final int MATE_THRESHOLD = 80000;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/AbstractEngine.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */