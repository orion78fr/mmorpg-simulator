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
}