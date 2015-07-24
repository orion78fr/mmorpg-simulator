package springVisualizer.model;

import java.util.ArrayList;

public class PowerLawDistributor {
	private double alpha;
	private double size;
	private int numBins;
	
	private ArrayList<Proba> bins = new ArrayList<Proba>();
	
	private static class Proba{
		double d; double n;
		public Proba(double d, double n) {
			super();
			this.d = d;
			this.n = n;
		}
	}
	
	public PowerLawDistributor(double alpha, double size, int numBins) {
		super();
		this.alpha = alpha;
		this.size = size;
		this.numBins = numBins;
		
		createBins();
		compensateExtremes();
	}
	
	private void createBins(){
		ArrayList<Proba> tmp = new ArrayList<Proba>();
		
		// Create a power law plot (linear in double logarithmic scales)
		for(int i = 0; i < numBins; i++){
			double logd = i * Math.log(size) / numBins;
			double d = Math.exp(logd);
			double logn = alpha * (Math.log(size) - logd);
			double n = Math.exp(logn) / Math.pow(size, alpha);
			
			tmp.add(new Proba(d, n));
		}
		
		// Diff the result to get probability of interval
		for(int i = 1; i<tmp.size(); i++){
			this.bins.add(new Proba(tmp.get(i-1).d, tmp.get(i-1).n - tmp.get(i).n));
		}
	}
	
	public void compensateExtremes(){
		double error = getExtremesError();
		
		// Correct extreme part of the plot not calculates (infinite parts), can have some floating point approximation errors
		this.bins.get(0).n += error;
	}
	
	public double getExtremesError(){
		double result = 0;
		for(Proba p : this.bins){
			result += p.n;
			
			System.out.println(p.d + ";" + p.n);
		}
		return 1-result;
	}
	
	public Object getProbabilisticDistance(double randomN) {
		// TODO Auto-generated method stub
		return null;
	}
}
