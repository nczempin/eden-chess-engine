# Historical Eden releases (2004–2007)

Original release artifacts recovered from the Internet Archive's Wayback Machine in October 2026.

The 0.0.3–0.0.9 jars were compiled from the original source, before the laptop holding the
latest code was stolen in April 2007 ("Eden and my status", CCC, 2007-04-19). Everything after
0.0.11 was rebuilt from a decompilation of 0.0.11 (0.0.12, the "resurrection edition").

| Version | Built | File | Source URL (archived) |
| :--- | :--- | :--- | :--- |
| 0.0.3 | 2004-04-03 | `de.czempin.chess.eden_0.0.3.zip` | `czempin.de/nicolai/blog/archives/de.czempin.chess.eden_0.0.3.zip` (snapshot 2007-02-06) |
| 0.0.4 | 2004-04-04 | `de.czempin.chess.eden_0.0.4.zip` | same path, `_0.0.4.zip` (snapshot 2006-07-17) — the jar inside is still named `..._0.0.3.jar`, but reports `id name Eden 0.0.4` |
| 0.0.5 | 2004-04-28 | `de.czempin.chess.eden_0.0.5.zip` | same path (snapshot 2006-07-17) |
| 0.0.6 | 2004-06-24 | `de.czempin.chess.eden_0.0.6.zip` | same path (snapshot 2006-07-17) |
| 0.0.7 | 2006-06-29 | `de.czempin.chess.eden_0.0.7.zip` | same path (snapshot 2006-07-17) |
| 0.0.9 | 2006-07-17 | `de.czempin.chess.eden_0.0.9.zip` | same path (snapshot 2006-08-16) |
| 0.0.13 | 2007-09-03 | `eden_0.0.13/` | Jim Ablett's `eden-0013-ja.zip` on `kirr.homeunix.org/chess/engines/Jim Ablett/EDEN/` (snapshot 2017-12-13) |

The zips are byte-for-byte as archived. For 0.0.13 only the "Original Jar", the opening book
files, `history.txt` and `readme.txt` were taken from Jim Ablett's package; his Excelsior JET /
Launch4J executables and the bundled third-party Wb2Uci tool were left out. That jar was
repacked by Jim in 2012 and reports `id name Eden 0.0.13 JA`. It needs `bookWhite.txt` and
`bookBlack.txt` in the working directory.

Still missing: 0.0.8, 0.0.10a/b/c, 0.0.11, 0.0.12. Their file names appear in the archived
directory listing of Kirill Kryukov's mirror, but the files themselves were not captured.

All jars were verified to answer `uci` and return a `bestmove` on a current JVM.
Checksums: `SHA256SUMS`.
