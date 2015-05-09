 package de.czempin.chess.commands;
 
 import de.czempin.chess.Brain;
 import de.czempin.chess.Engine;
 
 public class PositionCommand extends AbstractCommand
 {
   public PositionCommand(Engine engine)
   {
     super(engine);
   }
   
   public static Command getInstance()
   {
     if (instance == null)
       instance = new PositionCommand(null);
     return instance;
   }
   
   protected String commandString()
   {
     return "position";
   }
   
   protected String execute()
   {
     this.engine.initializeGame();
     setPosition();
     handleMoves();
     return "";
   }
   
   private void handleMoves()
   {
     String movesString = extractMoves(this.actualParams);
     String[] moves = movesString.split(" ");
     this.brain.clearThreeDraws();
     if ((movesString != "") && (moves.length != 0))
     {
       for (int i = 0; i < moves.length; i++) {
         this.brain.makeMove(moves[i]);
       }
     }
   }
   
   private void setPosition()
   {
     this.brain.SetEmptyPosition();
     String positionString = extractPosition(this.actualParams);
     if (positionString.equals("startpos")) {
       this.brain.setToStartPosition();
     }
     else if (positionString.startsWith("fen"))
     {
       String positionFen = extractFen(positionString);
       this.brain.setFENPosition(positionFen);
     }
   }
   
   private String extractFen(String positionString)
   {
     String retValue = positionString.replaceFirst("fen ", "");
     if (retValue.indexOf("moves ") != -1)
       retValue = retValue.split("moves")[0];
     return retValue;
   }
   
   private static String extractMoves(String parameters)
   {
     String pattern = " moves ";
     int index = parameters.indexOf(pattern);
     if (index == -1)
     {
       return "";
     }
     
     String moves = parameters.substring(index + pattern.length());
     return moves;
   }
   
 
   private String extractPosition(String parameters)
   {
     String pattern = " moves ";
     int index = parameters.indexOf(pattern);
     if (index == -1)
     {
       return parameters;
     }
     
     String position = parameters.substring(0, index);
     return position;
   }
   
 
   private static Command instance = null;
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/PositionCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */