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
	private static BufferedReader openBookReader(String fileName) throws FileNotFoundException {
		File f = new File(fileName);
		if (f.exists()) {
			return new BufferedReader(new FileReader(f));
		}
		try {
			java.security.CodeSource cs = OpeningBook.class.getProtectionDomain().getCodeSource();
			if (cs != null && cs.getLocation() != null) {
				File jarFile = new File(cs.getLocation().toURI());
				File dir = jarFile.isDirectory() ? jarFile : jarFile.getParentFile();
				if (dir != null) {
					File sibling = new File(dir, fileName);
					if (sibling.exists()) {
						return new BufferedReader(new FileReader(sibling));
					}
				}
			}
		} catch (Exception ignored) {
		}
		java.io.InputStream is = OpeningBook.class.getResourceAsStream("/" + fileName);
		if (is != null) {
			return new BufferedReader(new java.io.InputStreamReader(is));
		}
		return null;
	}

	public static void prepareOpeningBook()
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
			BufferedReader br = openBookReader(bbFileName);
			if (br != null) {
				if (Options.DEBUG)
					System.out.println("debug: black openings...");
				fillBlackOpenings(br);
				br.close();
			} else {
				if (Options.DEBUG)
					System.out.println("warning: opening book file '" + bbFileName + "' not found");

			}
			final String wbFilename = "bookWhite.txt";
			BufferedReader wr = openBookReader(wbFilename);
			if (wr != null) {
				if (Options.DEBUG)
					System.out.println("debug: white openings...");
				fillWhiteOpenings(wr);
				wr.close();
			}
			int positionCount = openings.size();
			if (Options.DEBUG)
				System.out.println("debug: Opening book has " + positionCount + " positions.");
			openingsLoaded = true;
		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			if (Options.DEBUG)
				e.printStackTrace();
		}
	}

	static void fillBlackOpenings(BufferedReader br) throws IOException {
		for (;;) {
			String line = br.readLine();
			if (line == null)
				break;
			line = line.trim();
			if (!line.isEmpty() && !line.startsWith("#")) {
				try {
					Position p = new Position();
					p.setToStartPosition();
					String[] movePairs = line.split("\\s+");
					for (int j = 0; j < movePairs.length; j++) {
						String[] ms = movePairs[j].split("\\.");
						if (ms.length == 2) {
							String ms1 = ms[0].trim();
							String ms2 = ms[1].trim();
							if (!bookAdd(p, ms1, ms2)) {
								break;
							}
						}
						}
					
					EdenBrain.threeDrawsTable.clear();
					}
				catch (ThreeRepetitionsAB e)
				{
					EdenBrain.threeDrawsTable.clear();
					}
				}
			}
		}

	
	private static boolean bookAdd(Position p, String moveString1, String moveString2) throws ThreeRepetitionsAB
	{
		if ("".equals(moveString1)) {
			p.setToStartPosition();
		} else {
			Move m1 = Move.create(p, moveString1);
			if (m1 == null || !p.isLegal(m1.from, m1.to, m1.promotedTo)) {
				return false;
			}
			p.makeMove(m1);
		}
		Move m = Move.create(p, moveString2);
		if (m == null || !p.isLegal(m.from, m.to, m.promotedTo)) {
			return false;
		}
		enterIntoBook(p, moveString2);
		p.makeMove(m);
		return true;
	}

	
	static void fillWhiteOpenings(BufferedReader br)
	throws IOException
	{
		for (;;)
		{
			String line = br.readLine();
			if (line == null)
				break;
			line = line.trim();
			if (!line.isEmpty() && !line.startsWith("#")) {
				try
				{
					Position p = new Position();
					p.setToStartPosition();
					String[] movePairs = line.split("\\.");
					for (int j = 0; j < movePairs.length; j++)
					{
						String pair = movePairs[j].trim();
						if (pair.isEmpty())
							continue;
						String[] ms = pair.split("\\s+");
						
						String ms2;
						String ms1;
						if (ms.length == 1)
						{
							ms1 = "";
							ms2 = ms[0].trim();
							} else {
							if (ms.length == 2)
							{
								ms1 = ms[0].trim();
								ms2 = ms[1].trim();
								}
							else {
								break;
							}
						}
						if (!bookAdd(p, ms1, ms2)) {
							break;
						}
						}
					
					EdenBrain.threeDrawsTable.clear();
					}
				catch (ThreeRepetitionsAB e)
				{
					EdenBrain.threeDrawsTable.clear();
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
		Long z = Long.valueOf(position.getZobrist());
		if (openings.containsKey(z))
		{
			Move m = (Move) openings.get(z);
			if (m != null && position.isLegal(m.from, m.to, m.promotedTo)) {
				return new Move(position, m.from, m.to, m.promotedTo);
			}
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