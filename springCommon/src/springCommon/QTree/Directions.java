package springCommon.QTree;

public enum Directions{
	NE, N, NW, W, SW, S, SE, E;
	
	public static Directions getOpposite(Directions d){
		if(d == null){
			return null;
		}
		switch(d){
			case E:	return W;
			case N: return S;
			case NE: return SW;
			case NW: return SE;
			case S: return N;
			case SE: return NW;
			case SW: return NE;
			case W: return E;
			default: return null;
		}
	}
	
	public static Directions getPerpendicular(Directions d, boolean clockwise){
		if(d == null){
			return null;
		}
		switch(d){
			case E:	return clockwise ? S : N;
			case N: return clockwise ? E : W;
			case NE: return clockwise ? SE : NW;
			case NW: return clockwise ? NE : SW;
			case S: return clockwise ? W : E;
			case SE: return clockwise ? SW : NE;
			case SW: return clockwise ? NW : SE;
			case W: return clockwise ? N : S;
			default: return null;
		}
	}
	
	public static Directions get45deg(Directions d, boolean clockwise){
		if(d == null){
			return null;
		}
		switch(d){
			case E:	return clockwise ? SE : NE;
			case N: return clockwise ? NE : NW;
			case NE: return clockwise ? E : N;
			case NW: return clockwise ? N : W;
			case S: return clockwise ? SW : SE;
			case SE: return clockwise ? S : E;
			case SW: return clockwise ? W : S;
			case W: return clockwise ? NW : SW;
			default: return null;
		}
	}
	
	public static Directions get135deg(Directions d, boolean clockwise){
		return Directions.get45deg(Directions.getOpposite(d), !clockwise);
	}
	
	public static boolean isDiagonal(Directions d){
		if(d == null){
			return false;
		}
		switch(d){
			case E:	return false;
			case N: return false;
			case NE: return true;
			case NW: return true;
			case S: return false;
			case SE: return true;
			case SW: return true;
			case W: return false;
			default: return false;
		}
	}
}