package springCommon.QTree;

import springCommon.Parameters;
import springCommon.Point2d;

public class TableUtils {
	public static <T> T tab_get(T[] tab, Point2d p){
		return tab_get(tab, p.getX(), p.getY());
	}
	public static double tab_get(double[] tab, Point2d p){
		return tab_get(tab, p.getX(), p.getY());
	}
	public static boolean tab_get(boolean[] tab, Point2d p){
		return tab_get(tab, p.getX(), p.getY());
	}
	
	public static <T> T tab_get(T[] tab, double x, double y){
		return tab_get(tab, (int)x * Parameters.sizey + (int) y);
	}
	public static double tab_get(double[] tab, double x, double y){
		return tab_get(tab, (int)x * Parameters.sizey + (int) y);
	}
	public static boolean tab_get(boolean[] tab, double x, double y){
		return tab_get(tab, (int)x * Parameters.sizey + (int) y);
	}
	
	public static <T> T tab_get(T[] tab, int p){
		return tab[p];
	}
	public static double tab_get(double[] tab, int p){
		return tab[p];
	}
	public static boolean tab_get(boolean[] tab, int p){
		return tab[p];
	}
	
	public static <T> void tab_set(T[] tab, Point2d p, T value){
		tab_set(tab, p.getX(), p.getY(), value);
	}
	public static void tab_set(double[] tab, Point2d p, double value){
		tab_set(tab, p.getX(), p.getY(), value);
	}
	public static void tab_set(boolean[] tab, Point2d p, boolean value){
		tab_set(tab, p.getX(), p.getY(), value);
	}
	
	public static <T> void tab_set(T[] tab, double x, double y, T value){
		tab_set(tab, (int)x * Parameters.sizey + (int) y, value);
	}
	public static void tab_set(double[] tab, double x, double y, double value){
		tab_set(tab, (int)x * Parameters.sizey + (int) y, value);
	}
	public static void tab_set(boolean[] tab, double x, double y, boolean value){
		tab_set(tab, (int)x * Parameters.sizey+ (int) y, value);
	}
	
	public static <T> void tab_set(T[] tab, int p, T value){
		tab[p] = value;
	}
	public static void tab_set(double[] tab, int p, double value){
		tab[p] = value;
	}
	public static void tab_set(boolean[] tab, int p, boolean value){
		tab[p] = value;
	}
}
