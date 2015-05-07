/*     */ package de.czempin.chess.commands.wb;
/*     */ 
/*     */ import de.czempin.chess.Engine;
/*     */ import de.czempin.chess.Info;
/*     */ import de.czempin.chess.Variation;
/*     */ import de.czempin.chess.commands.Command;
/*     */ import de.czempin.chess.commands.IgnoredCommand;
/*     */ import de.czempin.chess.commands.UnknownCommand;
/*     */ import java.io.PrintStream;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class WinBoardProtocol implements de.czempin.chess.Protocol
/*     */ {
/*     */   Engine engine;
/*     */   
/*     */   public WinBoardProtocol(Engine engine)
/*     */   {
/*  20 */     this.engine = engine;
/*     */   }
/*     */   
/*     */   public Command parse(String line) {
/*  24 */     if (line.equals("xboard"))
/*  25 */       return IgnoredCommand.getInstance();
/*  26 */     if (line.equals("quit"))
/*  27 */       return de.czempin.chess.commands.QuitCommand.getInstance();
/*  28 */     if (line.equals("force"))
/*  29 */       return new WBForceCommand(this.engine);
/*  30 */     if (line.equals("?"))
/*  31 */       return new de.czempin.chess.commands.StopCommand(this.engine);
/*  32 */     if (line.startsWith("new"))
/*  33 */       return new WBNewGameCommand(this.engine);
/*  34 */     if (line.startsWith("level"))
/*  35 */       return new WBLevelCommand(this.engine);
/*  36 */     if (line.startsWith("time"))
/*  37 */       return new WBTimeCommand(this.engine);
/*  38 */     if (line.startsWith("otim"))
/*  39 */       return IgnoredCommand.getInstance();
/*  40 */     if (line.startsWith("random"))
/*  41 */       return IgnoredCommand.getInstance();
/*  42 */     if (line.startsWith("post"))
/*  43 */       return IgnoredCommand.getInstance();
/*  44 */     if (line.startsWith("hard"))
/*  45 */       return IgnoredCommand.getInstance();
/*  46 */     if (line.startsWith("easy"))
/*  47 */       return IgnoredCommand.getInstance();
/*  48 */     if (line.startsWith("computer"))
/*  49 */       return IgnoredCommand.getInstance();
/*  50 */     if (line.startsWith("name"))
/*  51 */       return IgnoredCommand.getInstance();
/*  52 */     if (line.startsWith("setboard"))
/*  53 */       return WBSetboardCommand.getInstance();
/*  54 */     if (line.startsWith("accepted"))
/*  55 */       return IgnoredCommand.getInstance();
/*  56 */     if (line.startsWith("exit"))
/*  57 */       return IgnoredCommand.getInstance();
/*  58 */     if (line.startsWith("."))
/*  59 */       return IgnoredCommand.getInstance();
/*  60 */     if (line.startsWith("bk"))
/*  61 */       return IgnoredCommand.getInstance();
/*  62 */     if (line.startsWith("hint"))
/*  63 */       return IgnoredCommand.getInstance();
/*  64 */     if (line.startsWith("undo"))
/*  65 */       return UnknownCommand.getInstance();
/*  66 */     if (line.startsWith("sd "))
/*  67 */       return UnknownCommand.getInstance();
/*  68 */     if (line.startsWith("st "))
/*  69 */       return UnknownCommand.getInstance();
/*  70 */     if (line.startsWith("result"))
/*  71 */       return IgnoredCommand.getInstance();
/*  72 */     if (line.startsWith("go"))
/*  73 */       return new WBGoCommand(this.engine);
/*  74 */     if (line.startsWith("protover"))
/*  75 */       return new WBIDCommand(this.engine);
/*  76 */     String command = line.split(" ")[0];
/*  77 */     int length = command.length();
/*  78 */     if ((length == 4) || (length == 5)) {
/*  79 */       return new WBMoveCommand(this.engine, line);
/*     */     }
/*  81 */     return UnknownCommand.getInstance();
/*     */   }
/*     */   
/*     */   public String printMoveMade(String move) {
/*  85 */     this.engine.updateMoveCount();
/*  86 */     this.engine.updateInternalClock();
/*  87 */     return "move " + move + "\n";
/*     */   }
/*     */   
/*     */ 
/*     */   public void printInfo() {}
/*     */   
/*     */   public void printPV()
/*     */   {
/*  95 */     SortedSet values = new TreeSet();
/*  96 */     values.addAll(this.engine.getMultiPvs());
/*  97 */     Variation variation = (Variation)values.first();
/*  98 */     Vector v = variation.getLine();
/*  99 */     String pv = this.engine.getBrain().extractVariationString(v);
/* 100 */     int multiPvValue = variation.getValue();
/* 101 */     int multiPvDepth = variation.getDepth();
/* 102 */     System.out.print(multiPvDepth + "\t");
/* 103 */     System.out.print(multiPvValue + "\t");
/* 104 */     int time = (int)(Info.time / 10L);
/* 105 */     System.out.print(time + "\t");
/* 106 */     System.out.print(Info.nodes + "\t");
/* 107 */     System.out.print(pv);
/* 108 */     System.out.println();
/*     */   }
/*     */   
/*     */ 
/*     */   public static void printWBdata() {}
/*     */   
/*     */   public void calculateTimePerMove()
/*     */   {
/* 116 */     long timeLeft = this.engine.getTimeToNextControl();
/* 117 */     if (de.czempin.chess.Options.DEBUG)
/* 118 */       System.out.println("debug: base time left: " + timeLeft);
/* 119 */     long wbinc = this.engine.getWbinc();
/* 120 */     long tpm = this.engine.calculateTimePerMove(timeLeft, wbinc);
/* 121 */     this.engine.setTimePerMove(tpm);
/*     */   }
/*     */   
/*     */   public void printCurrmove(int i, String s, boolean flag) {}
/*     */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/wb/WinBoardProtocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */