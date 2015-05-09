package de.czempin.chess.eden;


import de.czempin.chess.eden.brain.Position;


public class Move implements Comparable {
	public int from;
	public int promotedTo;
	public int to;

	
	public Move(Move m) {
		this.value = 0;
		if (m != null) {
			this.from = m.from;
			this.to = m.to;
			this.promotedTo = m.promotedTo;
			this.capturedPiece = m.capturedPiece;
			this.movingPiece = m.movingPiece;
			this.value = m.value;
		}
	}

	
	public int value;
	public int capturedPiece;
	public int movingPiece;

	public Move(Position positionBefore, int from, int to) {
		this(positionBefore, from, to, 0);
	}

	
	public Move(Position positionBefore, int from, int to, de.czempin.chess.eden.brain.Piece promotedTo)
	{
		this(positionBefore, from, to, promotedTo.getType());
		}

	
	public Move(Position positionBefore, int from, int to, int promotedTo) {
		this.value = 0;
		this.from = from;
		this.to = to;
		this.promotedTo = promotedTo;
		this.movingPiece = positionBefore.board[from];
		}

	
	public Move(Position position, int from2, int next, de.czempin.chess.eden.brain.Piece promotedTo, int capturedPiece) {
		this(position, from2, next, promotedTo);
		this.capturedPiece = capturedPiece;
		this.movingPiece = position.board[from2];
		}

	
	public Move(Position position, int from2, int next, int promotedTo, int capturedPiece) {
		this(position, from2, next, promotedTo);
		this.capturedPiece = capturedPiece;
		this.movingPiece = position.board[from2];
		}

	
	private Move(Position pos, String move) {
		this.value = 0;
		int from = Position.decodeSquare(move.substring(0, 2));
		int to = new Square(move.substring(2, 4)).getIndex();
		String promotedTo = move.substring(4);
		int newPiece = 0;
		if (!promotedTo.equals("")) {
			newPiece = Position.decodePiece(promotedTo);
			this.capturedPiece = newPiece;
			}
		this.movingPiece = pos.board[from];
		this.from = from;
		this.to = to;
		}

	
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if (!(other instanceof Move)) {
			return false;
			}
		Move otherMove = (Move) other;
		return getText().equals(otherMove.getText());
		}

	
	public int hashCode()
	{
		return getText().hashCode();
		}

	
	public String toString() {
		String retVal = getText();
		return retVal;
		}

	
	public int compareTo(Object o) {
		if (this == o)
			return 0;
		Move other = (Move) o;
		int thisCaptureValue = Math.abs(this.capturedPiece);
		int otherCaptureValue = Math.abs(other.capturedPiece);
		if (thisCaptureValue == 6)
			return -1;
		if (otherCaptureValue == 6)
			return 1;
		if (this.promotedTo == 5)
			return -1;
		if (other.promotedTo == 5)
			return 1;
		if ((this.capturedPiece != 0) && (other.capturedPiece == 0))
			return -1;
		if ((this.capturedPiece == 0) && (other.capturedPiece != 0))
			return 1;
		if ((this.capturedPiece != 0) && (other.capturedPiece != 0)) {
			int thisMovingAbs = Math.abs(this.movingPiece);
			int otherMovingAbs = Math.abs(other.movingPiece);
			if ((thisMovingAbs == 1) && (otherMovingAbs != 1))
				return -1;
			if ((otherMovingAbs == 1) && (thisMovingAbs != 1))
				return 1;
			if ((thisCaptureValue == 5) && (otherCaptureValue != 5))
				return -1;
			if ((otherCaptureValue == 5) && (thisCaptureValue != 5))
				return 1;
			if ((thisCaptureValue == 1) && (otherCaptureValue != 1))
				return 1;
			if ((otherCaptureValue == 1) && (thisCaptureValue != 1))
				return -1;
			if (thisCaptureValue < otherCaptureValue)
				return 1;
			if (otherCaptureValue < thisCaptureValue)
				return -1;
			if (thisMovingAbs < otherMovingAbs)
				return -1;
			if (otherMovingAbs < thisMovingAbs)
				return 1;
			int thisPieceSquareValue = getPieceSquareValue(this.from, this.to, this.movingPiece);
			int otherPieceSquareValue = getPieceSquareValue(other.from, other.to, other.movingPiece);
			if (thisPieceSquareValue != otherPieceSquareValue)
				return otherPieceSquareValue - thisPieceSquareValue;
			if (this.promotedTo != 0)
				return 1;
			if (other.promotedTo != 0)
				return -1;
			int fromDiff = this.from - other.from;
			int toDiff = this.to - other.to;
			if (this.movingPiece > 0) {
				if (toDiff != 0)
					return -toDiff;
				if (fromDiff != 0)
					return -fromDiff;
				} else {
				if (toDiff != 0)
					return toDiff;
				if (fromDiff != 0)
					return fromDiff;
				}
			return 1;
			}
		if ((this.movingPiece == 6) || (this.movingPiece == -6)) {
			if (Math.abs(this.from - this.to) == 2)
				return -1;
			} else if (((other.movingPiece == 6) || (other.movingPiece == -6)) && (Math.abs(this.from - this.to) == 2))
			return 1;
		if ((this.promotedTo != 0) && (other.promotedTo == 0))
			return 1;
		if ((other.promotedTo != 0) && (this.promotedTo == 0))
			return -1;
		int promoteDiff = other.promotedTo - this.promotedTo;
		if (promoteDiff != 0)
			return promoteDiff;
		int thisPieceSquareValue = getPieceSquareValue(this.from, this.to, this.movingPiece);
		int otherPieceSquareValue = getPieceSquareValue(other.from, other.to, other.movingPiece);
		if (thisPieceSquareValue != otherPieceSquareValue)
			return otherPieceSquareValue - thisPieceSquareValue;
		int thisMovingAbs = Math.abs(this.movingPiece);
		int otherMovingAbs = Math.abs(other.movingPiece);
		if (thisMovingAbs < otherMovingAbs)
			return -1;
		if (otherMovingAbs < thisMovingAbs)
			return 1;
		int fromDiff = this.from - other.from;
		int toDiff = this.to - other.to;
		if (this.movingPiece > 0) {
			if (toDiff != 0)
				return -toDiff;
			if (fromDiff != 0)
				return -fromDiff;
			} else {
			if (toDiff != 0)
				return toDiff;
			if (fromDiff != 0)
				return fromDiff;
			}
		return 1;
		}

	
	private int getPieceSquareValue(int from, int to, int piece) {
		if (piece == 1) {
			if ((to > 80) || (to < 20)) {
				return 300;
				}
			return Position.whitePawnSquareValues[(79 - to)] - Position.whitePawnSquareValues[(79 - from)];
		}
		if (piece == -1) {
			if ((to < 20) || (to > 80)) {
				return 300;
				}
			return Position.blackPawnSquareValues[(to - 20)] - Position.blackPawnSquareValues[(from - 20)];
		}
		if ((piece == 2) || (piece == -2))
			return Position.getKnightPcSqValue(to) - Position.getKnightPcSqValue(from);
		if ((piece == 3) || (piece == -3))
			return Position.getBishopValue(to) - Position.getBishopValue(from);
		if ((piece == 5) || (piece == -5)) {
			int retVal = Position.getBishopValue(to) - Position.getBishopValue(from);
			return retVal;
			}
		return 0;
		}

	
	public String getText()
	{
		String promotedPiece = "";
		final int abs = Math.abs(this.promotedTo);
		switch (abs) {
		case 2:
			promotedPiece = "n";
			break;
		
		case 3:
			promotedPiece = "b";
			break;
		
		case 4:
			promotedPiece = "r";
			break;
		
		case 5:
			promotedPiece = "q";
			break;
		
		case 0:
			break;
		case 1:
			
		default:
			throw new AssertionError("should never happen");
			}
		
		
		
		return Position.encodeSquare(this.from) + Position.encodeSquare(this.to) + promotedPiece;
		}

	
	public boolean isCastling(int piece) {
		return (piece == 6) && (Math.abs(this.from - this.to) == 2);
		}

	
	public static Move create(Position p, String moveString) {
		if ((moveString.length() < 3) || (moveString.length() > 5))
			throw new IllegalArgumentException("move not recognized: " + moveString);
		if ("...".equals(moveString)) {
			System.out.println("debug: nullmove not supported");
			return null;
			}
		return new Move(p, moveString);
		}
	
}

/*
 * Location: /Users/nczempin/Desktop/eden-0013-ja/Original Jar/de.czempin.chess.eden-0013.jar!/de/czempin/chess/eden/Move.class Java compiler version: 2 (46.0)
 * JD-Core Version: 0.7.1-SNAPSHOT-20140817
 */