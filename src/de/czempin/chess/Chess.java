 package de.czempin.chess;
 
 import java.io.PrintStream;
 
 public class Chess
 {
   private String engineName;
   private String authorName;
   private String engineVersion;
   public Brain brain;
   private Engine engine;
   
   public Chess(Engine engine) {
     this.engine = engine;
     Brain b = engine.getBrain();
     this.engineName = b.getName();
     this.authorName = b.getAuthor();
     this.engineVersion = b.getVersion();
     this.brain = b;
   }
   
   public void run() {
     Options.synchronous = false;
     System.out.println("Welcome to " + this.engineName + ", a chess engine by " + 
       this.authorName + ". This is version " + this.engineVersion);
     System.out
       .println("This engine supports the UCI (default) and WB protocols.");
     java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
     try {
       for (;;) {
         String line = br.readLine();
         String result = execute(line);
         System.out.print(result);
       }
     } catch (java.io.IOException e) { e.printStackTrace();
     }
   }
   
   public String execute(String line)
   {
     line.trim();
     if ("uci".equals(line)) {
       Options.protocol = new de.czempin.chess.commands.uci.UCIProtocol(this.engine);
     } else if ("xboard".equals(line)) {
       if (Options.DEBUG)
         System.out.println("debug: protocol set to WB");
       Options.protocol = new de.czempin.chess.commands.wb.WinBoardProtocol(this.engine);
     } else if (Options.protocol == null) {
       Options.protocol = new de.czempin.chess.commands.uci.UCIProtocol(this.engine);
     }
     if (Options.DEBUG)
       System.out.println("debug: command received: " + line);
     de.czempin.chess.commands.Command c = parse(line);
     String output = c.execute(line);
     return output;
   }
   
   private static de.czempin.chess.commands.Command parse(String line) {
     return Options.protocol.parse(line);
   }
 }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Chess.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */