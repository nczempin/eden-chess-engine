Eden 0.0.13 (the "lucky edition")


Hello there!
Have fun with my chess engine "Eden"!

Why is it called Eden? It's a tribute to a program called PARADISE. More on that perhaps in a later edition of 
this readme.txt
How to use it:
Use a program like "Arena" (www.playwitharena.com) to play against it, or let other engines play against it, 
even have tournaments, or let it solve problem test suites (usually contained in ".epd" files. I'd even say 
analyze your games, but Eden is far too weak for that for the moment.

Logos:
Three jpeg-Images are included if you want to give Eden a face to the name. The first one was done by David 
Dahlem, author of Pooky, the second one by Jim Ablett, chess engine compile-master extraordinaire (and art 
history connoisseur?), and the last one by myself. It's what you get when a programmer does the job of a
graphic designer; but hopefully it's better than it would be the other way round. So consider my own black-
and-white version to be the official logo, to be used if you want to display a logo on the Web etc. It is based 
on work by Albrecht Duerer, a German Renaissance artist. The Font used is called PRETEXT.


You can freely use it, but in the unlikely event that you make money off it you have to ask me first; I might 
even say yes.


Start it using "eden_0.0.13.bat" or configuring Arena to use that batch script.
Note that the script merely calls "java" to start the .jar file, so you need a Java Virtual Machine (JVM) 
installed (tested with version 6) and in your path, for more info see http://java.sun.com.
If you know what you're doing, you can get a significant speedup by using a "server" JVM (add the option
 -server to the java call in the .bat). I haven't enabled it by default myself because it won't work for everyone. For the Server hotspot JVM I think you need the JDK version, but I haven't checked otherwise, as I always have a JDK on my machine. It would be nice if you would mention this setting if you use it in a published tournament, as the results will probably differ from using a regular version.



This is version 0.0.13, released 2007-09-03.

It has the following features:
- supports the very basics of the UCI protocol (for chess engines)
- supports the even more basic basics of the WB protocol (for chess engines)

- knows all rules of chess, including castling and en passant
- can sub-promote if necessary, i. e. take a knight instead of a queen on promotion
- knows about stalemate
- three-fold repetition rule recognized
- has an opening repertoire of over 880 positions


Some implementation notes:
- straightforward alpha-beta
- Iterative Deepening
- Simple time control: When the time limit is reached, it returns the best-so-far move.
- Quiescence Search, with "good or equal captures" examined "all the way"
- Basic Shannon-type (one-dimensional array) 10 x 8 board
- Three Zobrist-based Hashtables: Transpositions, Pawn Strucure, Opening Book

The playing strength:
Version 0.0.13 is a significant improvement over version 0.0.12.
- It plays similarly to a "weak club player", around 1500-1650 Elo/DWZ/USCF. Tactically, it is much stronger 
 (but among the engines at this strength level, its eval is relatively better than its search). I haven't 
  tested it against humans, however.

Test suites I ran (Pentium 4 Mobile 1.76 GHz):
WAC:
0.0.11: 178,
0.0.12: 180,
0.0.13: 181.

UCI protocol features:
- go depth
- go wtime x winc y ...
- go movestogo
- go infinite (actually this is used by default whenever the "go" params are not understood)
- stop
- OwnBook option
- Hash option. Please note that by default, a Java Virtual Machine (and therefore Eden) is limited to a total 
  of 64 MB. It is possible to increase the maximum size by the -Xmx option, e. g. java -server -Xmx256M -jar 
  de.czempin.... if you know what you're doing. If you try to give it more hash size than is available, it 
  will only use what is available, minus a safety margin. ****Note**** I didn't have the time to code the 
  resizing part, so the previous comment is invalid. I'm leaving it there in preparation for version 0.1.0, 
  where the comment will be valid.
-- only "id", "uciok", "readyok" and "bestmove" (without "ponder") is sent back
-- no "debug" command
-- no "ponder" or "ponderhit" command
-- no "go mate n"
-- no "go nodes n"


WinBoard (WB) protocol features:
- the basics of WB-2. Should be sufficient to play games.
-- UCI should be the default (it is much more stable and less bug-prone). I need feedback if you find any 
   problems!
-- "level" command only recognizes integers, e.g. "level 0 2 6" or "level 0 120 0", but not "level 40 0:30 0". 
   In fact, the second example will probably crash (or perhaps set the time to 0?)
-- no extended "level" command
-- "random" command is a no-op
-- "undo" and remove commands not supported
-- "nopost" command not supported; thinking output will always be displayed (hey, it's scarce enough as it is!)
-- "hard" & "easy" commands ignored (no pondering)
-- "bk" command ignored
-- "hint" command ignored
-- "st" command not supported
-- "sn" command not supported
-- "sd" command not supported
-- "rejected" command not supported, "accepted" simply ignored (whatever they are supposed to be for anyway)
-- which of the commands that can be deactivated are supported or not (analyze etc.) Eden will tell you when 
   receiving "protover" in xboard mode


TODO LIST = Limitations/Known Bugs/Future Features (in no particular order):
- the hashtable size still can't be changed. Total memory usage is < 4MB, so consider that to be the hashtable 
  size.
- 50-moves draw
- KBNK
- basic pawn endings
- backward pawns, as well as many other important positional factors, still not implemented
- null-move pruning
- pvs search
- pawn-to-seventh extension
- recapture extension
- pesky-attack extension
- mobility
- trapped pieces
- good bishop/bad bishop
- good knight/bad knight
- connected passers
- promotion in QS
- contempt factor
- enhance eval
- local optimizations
- option setting for WB (traditionally, .ini file)

Probable focus for the next version:
- more raw speed
- WB protocol compliance: undo/remove (WB) st (WB) sd
- UCI protocol compliance:  movetime (UCI)
- finish TT implementation (resize)
- insufficient material (done when true for both sides. TODO. One side insufficient->0 is upper bound for score)
- basic endgames: KPK
- whatever else it takes to beat this version!

A few days after I release my engine, usually a compiled version (an .exe) appears on Jim Ablett's site: 
http://homepages.tesco.net/henry.ablett/jims.html

Not only does that .exe version usually play significantly stronger than both the server and the regular 
version, it also makes it easier to use with some GUIs such as the Fritz GUI.

If you test this, please let me know your results. It does help if you prefer an .exe to a .bat, but it is 
much bigger than the pure Java bytecode. An alternative if you are just concered with having an .exe would be 
to use jlaunch.exe, which you can find using your favourite search engine.
*Please note this when/if setting up the compiled version in tournaments, by appending "(compiled)" (or 
whatever you prefer that will make the distinction clear) to the name in results. The noncompiled version is 
intended to be the official version. *


I love feedback (criticism, questions, tournament reports, games played by Eden, ...)! 
n.*mylastname*@gmx.de, replace *mylastname* by, well, ...)
I usually lurk in the "CCC" forums: www.talkchess.com. My username is nczempin.
 

Changes from 0.0.12->0.0.13
- optimizations and bugfixes
- doubled the (fixed) size of the Transposition Table

Changes in older versions:
See separate file "history.txt"

Other notes:
I'm not releasing the source code yet despite me normally being a proponent of open-source. I'm under the 
delusion that I have a professional reputation to lose :-). In addition, despite all the friendly help you 
get (and despite the fact that my engine is nowhere near competitive), chess engine programming is a bit like 
a competitive sport; let me keep my illusion that I have secrets to hide!

Nicolai Czempin

P. S. This chess engine is dedicated to Pavel and Janik.
