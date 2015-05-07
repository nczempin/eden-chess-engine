/*     */ package de.czempin.chess.eden.brain;
/*     */ 
/*     */ import de.czempin.chess.Options;
/*     */ import de.czempin.chess.eden.Move;

/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OpeningBook
/*     */ {
/*     */   static void prepareOpeningBook()
/*     */   {
/*  26 */     if (!Options.ownBook)
/*     */     {
/*  28 */       if (Options.DEBUG)
/*  29 */         System.out.println("debug: OwnBook set to false, canceling Opening Book preparation");
/*  30 */       return;
/*     */     }
/*  32 */     if (openingsLoaded) {
/*  33 */       return;
/*     */     }
/*     */     try {
/*  36 */       BufferedReader br = new BufferedReader(new FileReader("bookBlack.txt"));
/*  37 */       BufferedReader wr = new BufferedReader(new FileReader("bookWhite.txt"));
/*  38 */       if (Options.DEBUG)
/*  39 */         System.out.println("debug: white openings...");
/*  40 */       fillWhiteOpenings(wr);
/*  41 */       if (Options.DEBUG)
/*  42 */         System.out.println("debug: black openings...");
/*  43 */       fillBlackOpenings(br);
/*  44 */       int positionCount = openings.size();
/*  45 */       if (Options.DEBUG)
/*  46 */         System.out.println("debug: Opening book has " + positionCount + " positions.");
/*  47 */       openingsLoaded = true;
/*     */     }
/*     */     catch (FileNotFoundException e)
/*     */     {
/*  51 */       e.printStackTrace();
/*  52 */       return;
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/*  56 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   static void fillBlackOpenings(BufferedReader br)
/*     */     throws IOException
/*     */   {
/*     */     for (;;)
/*     */     {
/*  65 */       String line = br.readLine();
/*  66 */       if (line == null)
/*     */         break;
/*  68 */       if (!line.startsWith("#")) {
/*     */         try
/*     */         {
/*  71 */           Position p = new Position();
/*  72 */           p.setToStartPosition();
/*  73 */           String[] movePairs = line.split(" ");
/*  74 */           for (int j = 0; j < movePairs.length; j++)
/*     */           {
/*  76 */             String[] ms = movePairs[j].split("\\.");
/*  77 */             if (ms.length == 2)
/*     */             {
/*  79 */               String ms1 = ms[0];
/*  80 */               String ms2 = ms[1];
/*  81 */               bookAdd(p, ms1, ms2);
/*     */             }
/*     */           }
/*     */           
/*  85 */           EdenBrain.threeDrawsTable.clear();
/*     */         }
/*     */         catch (ThreeRepetitionsAB e)
/*     */         {
/*  89 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void bookAdd(Position p, String moveString1, String moveString2) throws ThreeRepetitionsAB
/*     */   {
/*  97 */     if ("".equals(moveString1)) {
/*  98 */       p.setToStartPosition();
/*     */     } else
/* 100 */       p.makeMove(moveString1);
/* 101 */     Move m = enterIntoBook(p, moveString2);
/* 102 */     p.makeMove(m);
/*     */   }
/*     */   
/*     */   static void fillWhiteOpenings(BufferedReader br)
/*     */     throws IOException
/*     */   {
/*     */     for (;;)
/*     */     {
/* 110 */       String line = br.readLine();
/* 111 */       if (line == null)
/*     */         break;
/* 113 */       if (!line.startsWith("#")) {
/*     */         try
/*     */         {
/* 116 */           Position p = new Position();
/* 117 */           String[] movePairs = line.split("\\.");
/* 118 */           for (int j = 0; j < movePairs.length; j++)
/*     */           {
/* 120 */             String[] ms = movePairs[j].split(" ");
/*     */             
/*     */             String ms2;
String ms1;
/* 123 */             if (ms.length == 1)
/*     */             {
/* 125 */                ms1 = "";
/* 126 */               ms2 = ms[0];
/*     */             } else { 
/* 128 */               if (ms.length == 2)
/*     */               {
/* 130 */                 ms1 = ms[0];
/* 131 */                 ms2 = ms[1];
/*     */               }
/*     */               else {
/* 134 */                 throw new IllegalArgumentException("Opening Book Format invalid: " + movePairs + "-->" + ms); } }
/*     */              bookAdd(p, ms1, ms2);
/*     */           }
/*     */           
/* 139 */           EdenBrain.threeDrawsTable.clear();
/*     */         }
/*     */         catch (ThreeRepetitionsAB e)
/*     */         {
/* 143 */           e.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static Move enterIntoBook(Position p, String moveString) {
/* 150 */     Long z = EdenBrain.getZobrist(p);
/* 151 */     Move m = Move.create(p, moveString);
/* 152 */     if (!openings.containsKey(z))
/* 153 */       openings.put(z, m);
/* 154 */     return m;
/*     */   }
/*     */   
/*     */   static Move checkOpeningBook(Position position)
/*     */   {
/* 159 */     if (Options.DEBUG)
/* 160 */       System.out.println("debug: EdenBrain.checkOpeningBook()");
/* 161 */     if (!Options.ownBook)
/* 162 */       return null;
/* 163 */     Long z = new Long(position.getZobrist());
/* 164 */     if (openings.containsKey(z))
/*     */     {
/* 166 */       Move m = (Move)openings.get(z);
/* 167 */       return m;
/*     */     }
/*     */     
/* 170 */     return null;
/*     */   }
/*     */   
/*     */ 
/* 174 */   static Map openings = new HashMap();
/*     */   static boolean openingsLoaded;
/*     */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/OpeningBook.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */