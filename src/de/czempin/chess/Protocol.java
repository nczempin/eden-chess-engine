package de.czempin.chess;

import de.czempin.chess.commands.Command;

public abstract interface Protocol
{
  public abstract Command parse(String paramString);
  
  public abstract String printMoveMade(String paramString);
  
  public abstract void printInfo();
  
  public abstract void printPV();
  
  public abstract void printCurrmove(int paramInt, String paramString, boolean paramBoolean);
  
  public abstract void calculateTimePerMove();
}


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Protocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */