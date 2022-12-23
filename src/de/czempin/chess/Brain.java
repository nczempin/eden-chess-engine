package de.czempin.chess;

import java.util.Vector;

public abstract interface Brain
{
  public abstract String getAuthor();
  
  public abstract String getName();
  
  public abstract String getVersion();
  
  public abstract String move();
  
  public abstract void setToStartPosition();
  
  public abstract void setFENPosition(String paramString);
  
  public abstract void clearThreeDraws();
  
  public abstract void makeMove(String paramString);
  
  public abstract void initializeGame();
  
  public abstract Boolean onMove();
  
  public abstract boolean isStartPosition();
  
  public abstract void SetEmptyPosition();
  
  public abstract String extractVariationString(Vector paramVector);
  
  public abstract String getBestMoveSoFar();
  
  public abstract boolean whiteToMove();
  
  public abstract int getStaticValue();
}


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Brain.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */