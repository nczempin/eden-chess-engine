 package de.czempin.chess.commands.wb;
 
 import de.czempin.chess.Brain;
 import de.czempin.chess.commands.AbstractCommand;
 import de.czempin.chess.commands.Command;
 
 public class WBSetboardCommand
   extends AbstractCommand
 {
   public static Command getInstance()
   {
     if (instance == null)
       instance = new WBSetboardCommand();
     return instance;
   }
   
   protected String commandString() {
     return "setboard";
   }
   
   protected String execute() {
     this.brain.initializeGame();
     setPosition();
     handleMoves();
     return "";
   }
   
   private void handleMoves() {
     String movesString = extractMoves(this.actualParams);
     String[] moves = movesString.split(" ");
     this.brain.clearThreeDraws();
     if ((movesString != "") && (moves.length != 0)) {
       for (int i = 0; i < moves.length; i++) {
         this.brain.makeMove(moves[i]);
       }
     }
   }
   
   private void setPosition() {}
   
   private static String extractMoves(String parameters)
   {
     String pattern = " moves ";
     int index = parameters.indexOf(pattern);
     if (index == -1) {
       return "";
     }
     String moves = parameters.substring(index + pattern.length());
     return moves;
   }
   
 
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WBSetboardCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */