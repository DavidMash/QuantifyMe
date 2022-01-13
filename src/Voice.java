import java.util.Random;

public abstract class Voice {
	
	protected Key key;
	protected Pattern chords;
	protected Pattern rhythm;
	protected double density;
	protected boolean isLead;
	protected Pattern pattern;
	protected Random random;
	
	public Voice(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
		this.key = key;
		this.chords = chords;
		this.rhythm = rhythm;
		this.density = density;
		this.isLead = isLead;
		this.random = new Random();
		this.pattern = new Pattern(this.random);
	}
	
	public void reset() {
		this.pattern = new Pattern(this.random);
	}
	
	abstract public void generatePattern();

	abstract public void setPatternOverChords();
	
	abstract public Pattern getPatternInKey();
	
	public double getDensity() {
		return this.density;
	}
	
	abstract public String toString();
}
