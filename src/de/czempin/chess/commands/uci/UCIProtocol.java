/*     */ package de.czempin.chess.commands.uci;
/*     */ 
/*     */ import de.czempin.chess.AbstractProtocol;
/*     */ import de.czempin.chess.Engine;
/*     */ import de.czempin.chess.Info;
/*     */ import de.czempin.chess.Options;
/*     */ import de.czempin.chess.Variation;
/*     */ import de.czempin.chess.commands.Command;
/*     */ import de.czempin.chess.commands.QuitCommand;
/*     */ import de.czempin.chess.commands.StopCommand;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class UCIProtocol extends AbstractProtocol
/*     */ {
/*     */   public UCIProtocol(Engine engine)
/*     */   {
/*  21 */     super(engine);
/*     */   }
/*     */   
/*     */   public Command parse(String line) {
/*  25 */     if (line.equals("uci"))
/*  26 */       return new UCIIDCommand(this.engine);
/*  27 */     if (line.equals("isready"))
/*  28 */       return UCIIsReadyCommand.getInstance();
/*  29 */     if (line.equals("quit"))
/*  30 */       return QuitCommand.getInstance();
/*  31 */     if (line.equals("stop"))
/*  32 */       return new StopCommand(this.engine);
/*  33 */     if (line.startsWith("ucinewgame"))
/*  34 */       return new UCINewGameCommand(this.engine);
/*  35 */     if (line.startsWith("position"))
/*  36 */       return new de.czempin.chess.commands.PositionCommand(this.engine);
/*  37 */     if (line.startsWith("setoption"))
/*  38 */       return UCISetOptionCommand.getInstance(line);
/*  39 */     if (line.startsWith("go")) {
/*  40 */       return new UCIGoCommand(this.engine, line);
/*     */     }
/*  42 */     return de.czempin.chess.commands.UnknownCommand.getInstance();
/*     */   }
/*     */   
/*     */   public String printMoveMade(String move) {
/*  46 */     return "\nbestmove " + move + "\n";
/*     */   }
/*     */   
/*     */   public void printInfo() {
/*  50 */     if ((!Options.DEBUG) && (Info.idDepth < Options.minimumPlyForDisplay))
/*  51 */       return;
/*  52 */     System.out.print("info");
/*  53 */     System.out.print(" nodes " + Info.nodes);
/*  54 */     Info.time = System.currentTimeMillis() - Info.searchtime;
/*  55 */     long nps = Info.nodes * 1000L / (Info.time + 1L);
/*  56 */     if (nps < 0L)
/*  57 */       throw new IllegalStateException("nps <0, : " + nps);
/*  58 */     System.out.print(" nps " + nps);
/*  59 */     if (Options.useTranspositionTable)
/*  60 */       System.out.print(" hashfull " + Info.hashFull);
/*  61 */     System.out.println();
/*  62 */     printDebugInfo();
/*     */   }
/*     */   
/*     */   public void printPV()
/*     */   {
/*  67 */     SortedSet values = new TreeSet();
/*  68 */     values.addAll(this.engine.getMultiPvs());
/*  69 */     Iterator it = values.iterator();
/*  70 */     int i = 0;
/*  71 */     for (; it.hasNext(); System.out.println()) {
/*  72 */       i++; if (i > Options.multiPvDisplayNumber)
/*     */         break;
/*  74 */       Variation variation = (Variation)it.next();
/*  75 */       Vector v = variation.getLine();
/*  76 */       String pv = this.engine.getBrain().extractVariationString(v);
/*  77 */       System.out.print("info");
/*  78 */       int multipvRank = i;
/*  79 */       int multiPvValue = variation.getValue();
/*  80 */       assert (multiPvValue < 99999);
/*     */       
/*  82 */       assert (multiPvValue > -99999);
/*     */       
/*  84 */       int multiPvDepth = variation.getDepth();
/*  85 */       System.out.print(" depth " + multiPvDepth);
/*  86 */       if (Options.multiPvDisplayNumber > 1)
/*  87 */         System.out.print(" multipv " + multipvRank);
/*  88 */       if ((multiPvValue > 80000) && (multiPvValue < 90000)) {
/*  89 */         int mateIn = (90000 - multiPvValue) / 2 + 1;
/*  90 */         System.out.print(" score mate " + mateIn);
/*  91 */       } else if ((multiPvValue < -80000) && (multiPvValue > -90000)) {
/*  92 */         int mateIn = (-90000 - multiPvValue) / 2 - 1;
/*  93 */         System.out.print(" score mate " + mateIn);
/*     */       } else {
/*  95 */         System.out.print(" score cp " + multiPvValue);
/*     */       }
/*  97 */       System.out.print(" pv " + pv);
/*     */     }
/*     */   }
/*     */   
/*     */   public void printCurrmove(int currmovenumber, String currentmove, boolean withInfoPrefix)
/*     */   {
/* 103 */     if ((!Options.DEBUG) && (Info.idDepth < 5))
/* 104 */       return;
/* 105 */     if (currmovenumber == 0)
/* 106 */       return;
/* 107 */     if (withInfoPrefix)
/* 108 */       System.out.print("info");
/* 109 */     System.out.print(" currmove " + currentmove + " currmovenumber " + currmovenumber);
/* 110 */     System.out.print(" depth " + Info.idDepth);
/* 111 */     if (Info.seldepth > Info.idDepth)
/* 112 */       System.out.print(" seldepth " + Info.seldepth);
/* 113 */     if (withInfoPrefix) {
/* 114 */       System.out.println();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCIProtocol.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */