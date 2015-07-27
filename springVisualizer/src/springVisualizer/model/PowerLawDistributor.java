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
	}
	
	private void createBins(){
		ArrayList<Proba> tmp = new ArrayList<Proba>();
		
		// Create a power law plot (linear in double logarithmic scales)
		for(int i = 0; i < numBins; i++){
			double logd = i * Math.log(size) / numBins;
			double d = Math.exp(logd);
			double logn = alpha * (Math.log(size) - logd);
			double n = Math.exp(logn);
			
			tmp.add(new Proba(d, n));
		}
		
		double minn = tmp.get(tmp.size() - 1).n,
				maxn = tmp.get(0).n,
				mind = tmp.get(0).d,
				maxd = tmp.get(tmp.size() - 2).d;
		
		// Scale to ignore infinite parts of the power law
		for(int i = 1; i<tmp.size(); i++){
			this.bins.add(new Proba(((tmp.get(i-1).d - mind) / (maxd-mind)) * size, (tmp.get(i).n - minn) / (maxn - minn)));
		}
	}
	
	public double getProbabilisticDistance(double randomN) {
		for(Proba p : this.bins){
			if(randomN >= p.n){
				return p.d;
			}
		}
		return -1;
	}
}
