package de.czempin.chess.commands;

import java.io.PrintStream;

public class QuitCommand implements Command
{
  public static Command getInstance()
  {
    if (instance == null)
      instance = new QuitCommand();
    return instance;
  }
  
  public String execute(String parameters) {
    Thread bt = de.czempin.chess.AbstractEngine.brainThread;
    if ((bt != null) && (bt.isAlive())) {
      try {
        bt.join(2000L);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    }
    System.out.println("Thanks for playing with " + parameters + ". Good-bye until next time!");
    System.out.flush();
    System.exit(0);
    return null;
  }
  
  private static Command instance = null;
  
  public void setParameters(String s) {}
}
