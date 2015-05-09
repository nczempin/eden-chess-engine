package de.czempin.chess.eden.brain;


import de.czempin.chess.Options;

import de.czempin.chess.eden.Move;


import java.io.BufferedReader;
import java.io.File;

import java.io.FileNotFoundException;

import java.io.FileReader;

import java.io.IOException;

import java.io.PrintStream;

import java.util.HashMap;

import java.util.Map;











public class OpeningBook
{
	static void prepareOpeningBook()
	{
		if (!Options.ownBook)
		{
			if (Options.DEBUG)
				System.out.println("debug: OwnBook set to false, canceling Opening Book preparation");
			return;
			}
		if (openingsLoaded) {
			return;
		}
		try {
			final String bbFileName = "bookBlack.txt";
			File bbFile = new File(bbFileName);
			if (bbFile.exists()) {
				final FileReader bb = new FileReader(bbFile);
				BufferedReader br = new BufferedReader(bb);
				if (Options.DEBUG)
					System.out.println("debug: black openings...");
				fillBlackOpenings(br);
			} else {
				if (Options.DEBUG)
					System.out.println("warning: opening book file '" + bbFileName + "' not found");

			}
			final String wbFilename = "bookWhite.txt";
			File wbFile = new File(wbFilename);
			if (wbFile.exists()) {
				final FileReader wb = new FileReader(wbFile);
				BufferedReader wr = new BufferedReader(wb);
				if (Options.DEBUG)
					System.out.println("debug: white openings...");
				fillWhiteOpenings(wr);
			}
			int positionCount = openings.size();
			if (Options.DEBUG)
				System.out.println("debug: Opening book has " + positionCount + " positions.");
			openingsLoaded = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void fillBlackOpenings(BufferedReader br) throws IOException {
		for (;;) {
			String line = br.readLine();
			if (line == null)
				break;
			if (!line.startsWith("#")) {
				try {
					Position p = new Position();
					p.setToStartPosition();
					String[] movePairs = line.split(" ");
					for (int j = 0; j < movePairs.length; j++) {
						String[] ms = movePairs[j].split("\\.");
						if (ms.length == 2) {
							String ms1 = ms[0];
							String ms2 = ms[1];
							bookAdd(p, ms1, ms2);
						}
						}
					
					EdenBrain.threeDrawsTable.clear();
					}
				catch (ThreeRepetitionsAB e)
				{
					e.printStackTrace();
					}
				}
			}
		}

	
	private static void bookAdd(Position p, String moveString1, String moveString2) throws ThreeRepetitionsAB
	{
		if ("".equals(moveString1)) {
			p.setToStartPosition();
			} else
			p.makeMove(moveString1);
		Move m = enterIntoBook(p, moveString2);
		p.makeMove(m);
		}

	
	static void fillWhiteOpenings(BufferedReader br)
	throws IOException
	{
		for (;;)
		{
			String line = br.readLine();
			if (line == null)
				break;
			if (!line.startsWith("#")) {
				try
				{
					Position p = new Position();
					String[] movePairs = line.split("\\.");
					for (int j = 0; j < movePairs.length; j++)
					{
						String[] ms = movePairs[j].split(" ");
						
						String ms2;
						String ms1;
						if (ms.length == 1)
						{
							ms1 = "";
							ms2 = ms[0];
							} else {
							if (ms.length == 2)
							{
								ms1 = ms[0];
								ms2 = ms[1];
								}
							else {
								throw new IllegalArgumentException("Opening Book Format invalid: " + movePairs + "-->" + ms);
							}
						}
						bookAdd(p, ms1, ms2);
						}
					
					EdenBrain.threeDrawsTable.clear();
					}
				catch (ThreeRepetitionsAB e)
				{
					e.printStackTrace();
					}
				}
			}
		}

	
	static Move enterIntoBook(Position p, String moveString) {
		Long z = EdenBrain.getZobrist(p);
		Move m = Move.create(p, moveString);
		if (!openings.containsKey(z))
			openings.put(z, m);
		return m;
		}

	
	static Move checkOpeningBook(Position position)
	{
		if (Options.DEBUG)
			System.out.println("debug: EdenBrain.checkOpeningBook()");
		if (!Options.ownBook)
			return null;
		Long z = new Long(position.getZobrist());
		if (openings.containsKey(z))
		{
			Move m = (Move) openings.get(z);
			return m;
			}
		
		return null;
		}

	
	
	static Map openings = new HashMap();
	static boolean openingsLoaded;
	
}

/*
 * Location: /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/brain/OpeningBook.class Java compiler
 * version: 2 (46.0) JD-Core Version: 0.7.1-SNAPSHOT-20140817
 */