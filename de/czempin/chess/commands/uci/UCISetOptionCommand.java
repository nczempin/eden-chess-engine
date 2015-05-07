/*    */ package de.czempin.chess.commands.uci;
/*    */ 
/*    */ import de.czempin.chess.Options;
/*    */ import de.czempin.chess.commands.AbstractCommand;
/*    */ import de.czempin.chess.commands.Command;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class UCISetOptionCommand
/*    */   extends AbstractCommand
/*    */ {
/*    */   public static Command getInstance(String parameters)
/*    */   {
/* 13 */     if (instance == null)
/* 14 */       instance = new UCISetOptionCommand();
/* 15 */     instance.setParameters(parameters);
/* 16 */     return instance;
/*    */   }
/*    */   
/*    */   public void setParameters(String s) {}
/*    */   
/*    */   public String execute(String parameters)
/*    */   {
/* 23 */     String optionName = extractStringValue(parameters, "name");
/* 24 */     String optionValue = extractStringValue(parameters, "value");
/* 25 */     if (Options.DEBUG)
/* 26 */       System.out.println("debug: name=" + optionName + ", value=" + 
/* 27 */         optionValue);
/* 28 */     Options.setOptions(optionName, optionValue);
/* 29 */     return "";
/*    */   }
/*    */   
/*    */   private String extractStringValue(String parameters, String string) {
/* 33 */     int index = parameters.indexOf(string);
/* 34 */     if (index == -1) {
/* 35 */       return null;
/*    */     }
/* 37 */     String extracted = parameters.substring(
/* 38 */       parameters.indexOf(string) + string.length() + 1)
/* 39 */       .split(" ")[0];
/* 40 */     return extracted;
/*    */   }
/*    */   
/*    */   protected String commandString()
/*    */   {
/* 45 */     return "go";
/*    */   }
/*    */   
/*    */   protected String execute() {
/* 49 */     return null;
/*    */   }
/*    */   
/* 52 */   private static Command instance = null;
/*    */ }


/* Location:              /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/commands/uci/UCISetOptionCommand.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       0.7.1-SNAPSHOT-20140817
 */