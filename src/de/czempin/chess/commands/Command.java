package de.czempin.chess.commands;

public abstract interface Command
{
  public abstract String execute(String paramString);
  
  public abstract void setParameters(String paramString);
}


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/Command.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */