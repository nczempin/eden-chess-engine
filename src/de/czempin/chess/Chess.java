/*    */ package de.czempin.chess;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class Chess
/*    */ {
/*    */   private String engineName;
/*    */   private String authorName;
/*    */   private String engineVersion;
/*    */   public Brain brain;
/*    */   private Engine engine;
/*    */   
/*    */   public Chess(Engine engine) {
/* 14 */     this.engine = engine;
/* 15 */     Brain b = engine.getBrain();
/* 16 */     this.engineName = b.getName();
/* 17 */     this.authorName = b.getAuthor();
/* 18 */     this.engineVersion = b.getVersion();
/* 19 */     this.brain = b;
/*    */   }
/*    */   
/*    */   public void run() {
/* 23 */     Options.synchronous = false;
/* 24 */     System.out.println("Welcome to " + this.engineName + ", a chess engine by " + 
/* 25 */       this.authorName + ". This is version " + this.engineVersion);
/* 26 */     System.out
/* 27 */       .println("This engine supports the UCI (default) and WB protocols.");
/* 28 */     java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
/*    */     try {
/*    */       for (;;) {
/* 31 */         String line = br.readLine();
/* 32 */         String result = execute(line);
/* 33 */         System.out.print(result);
/*    */       }
/* 35 */     } catch (java.io.IOException e) { e.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public String execute(String line)
/*    */   {
/* 41 */     line.trim();
/* 42 */     if ("uci".equals(line)) {
/* 43 */       Options.protocol = new de.czempin.chess.commands.uci.UCIProtocol(this.engine);
/* 44 */     } else if ("xboard".equals(line)) {
/* 45 */       if (Options.DEBUG)
/* 46 */         System.out.println("debug: protocol set to WB");
/* 47 */       Options.protocol = new de.czempin.chess.commands.wb.WinBoardProtocol(this.engine);
/* 48 */     } else if (Options.protocol == null) {
/* 49 */       Options.protocol = new de.czempin.chess.commands.uci.UCIProtocol(this.engine);
/*    */     }
/* 51 */     if (Options.DEBUG)
/* 52 */       System.out.println("debug: command received: " + line);
/* 53 */     de.czempin.chess.commands.Command c = parse(line);
/* 54 */     String output = c.execute(line);
/* 55 */     return output;
/*    */   }
/*    */   
/*    */   private static de.czempin.chess.commands.Command parse(String line) {
/* 59 */     return Options.protocol.parse(line);
/*    */   }
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/Chess.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */