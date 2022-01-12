import java.util.Random;

public abstract class Voice {
	
	protected Key key;
	protected Pattern chords;
	protected double density;
	protected boolean isLead;
	protected Pattern pattern;
	protected Pattern patternOverChords;
	protected Random random;
	
	public Voice(Key key, Pattern chords, Double density, boolean isLead, Random random) {
		this.key = key;
		this.chords = chords;
		this.density = density;
		this.isLead = isLead;
		this.pattern = new Pattern();
		this.random = new Random();
	}
	
	public void reset() {
		this.pattern = new Pattern();
	}
	
	abstract void generatePattern(Random random);

	abstract void setPatternOverChords();
	
	abstract Pattern getPatternInKey();
}
