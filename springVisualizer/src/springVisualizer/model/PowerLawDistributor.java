package springVisualizer.model;

public class PowerLawDistributor {
	private double alpha;
	private int size;
	private int numBins;
	
	public PowerLawDistributor(double alpha, int size, int numBins) {
		super();
		this.alpha = alpha;
		this.size = size;
		this.numBins = numBins;
		
		for(int i = 0; i < numBins; i++){
			double logd = i * Math.log(size) / numBins;
			double d = Math.round(Math.exp(logd));
			double logn = alpha * (Math.log(size) - Math.log(d));
			double n = Math.round(Math.exp(logn));
			
			System.out.println(d + ";" + n);
		}
	}
	
	public static void main(String[] args) {
		new PowerLawDistributor(1, 100, 50);
	}
}
