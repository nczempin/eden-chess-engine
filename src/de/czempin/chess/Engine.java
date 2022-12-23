package de.czempin.chess;

import java.util.Collection;

public abstract interface Engine
{
  public abstract void run();
  
  public abstract void startBrain();
  
  public abstract boolean isSynchronous();
  
  public abstract void initializeGame();
  
  public abstract void stopBrain();
  
  public abstract Brain getBrain();
  
  public abstract void setMovesToGo(int paramInt);
  
  public abstract void setTimes(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  public abstract void setDepth(int paramInt);
  
  public abstract void setWbInc(int paramInt);
  
  public abstract void setWbTime(int paramInt);
  
  public abstract void setMovesPerTimeControl(int paramInt);
  
  public abstract void setTimeToNextControl(int paramInt);
  
  public abstract void updateMoveCount();
  
  public abstract void updateInternalClock();
  
  public abstract int getMovestogo();
  
  public abstract Collection getMultiPvs();
  
  public abstract long getBtime();
  
  public abstract long getBinc();
  
  public abstract boolean whiteToMove();
  
  public abstract long getWtime();
  
  public abstract long getWinc();
  
  public abstract long calculateTimePerMove(long paramLong1, long paramLong2);
  
  public abstract void setForceMode(boolean paramBoolean);
  
  public abstract boolean getForceMode();
  
  public abstract long getTimeToNextControl();
  
  public abstract void setTimePerMove(long paramLong);
  
  public abstract long getWbinc();
  
  public abstract long getTimeSpent();
  
  public abstract boolean timeUp();
  
  public abstract int getDepth();
}


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Engine.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */