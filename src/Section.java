import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Section {
	private List<Pattern> section;
	private List<Voice> voices; 
	private Rhythm rhythm;
	private List<Integer> chords;
	private Double[] voiceDensities;
	private Random random;
	private double sectionDensity;
	private double modulationChance;
	private Key key;
	private Key nextKey;
	private int switchPoint;
	
	public Section(List<Voice> voices, Key key, double sectionDensity, double modulationChance, Random random) {
		this.voices = voices;
		this.random = random;
		this.key = key;
		this.nextKey = this.key;
		this.sectionDensity = sectionDensity;
		this.modulationChance = modulationChance;
		this.assignVoiceDensities();
		this.setChords();
		this.setRhythm();
		this.generateSection();
	}
	
	private void assignVoiceDensities() {
		this.voiceDensities = new Double[voices.size()];
	    for(int i = 0; i < voices.size(); i++) {
	    	int voiceIndex = this.random.nextInt(voices.size());
	    	while(this.voiceDensities[voiceIndex] != null) {
	    		voiceIndex = (voiceIndex + 1) % voices.size();
	    	}
	    	this.voiceDensities[voiceIndex] = (Double) ((i * this.sectionDensity) / ((double) voices.size()));
	    }
	}
		
	private void setChords() {
		int numberOfChords = 4; //TODO: Make the number of chords random
		this.chords = new ArrayList<>(); 
	    boolean modulate = (this.random.nextDouble() <= this.modulationChance);
	    System.out.println("Modulate? "+modulate);
		if (modulate) {
			this.nextKey = this.key.getModulationKey();
			this.switchPoint = numberOfChords - 1;
			int[] roots = {0};
			int[] predominants = {1, 2, 3, 5};
			int[] dominants = {4};
			
			this.chords.add(roots[this.random.nextInt(roots.length)]);
			for(int i = 1; i < numberOfChords - 1; i++) {
				this.chords.add(predominants[this.random.nextInt(predominants.length)]);
			}
			this.chords.add(dominants[this.random.nextInt(dominants.length)]);
		} else {
			int[] roots = {0};
			int[] predominants = {1, 2, 3, 5};
			int[] dominants = {4};
			
			this.chords.add(roots[this.random.nextInt(roots.length)]);
			for(int i = 1; i < numberOfChords - 1; i++) {
				this.chords.add(predominants[this.random.nextInt(predominants.length)]);
			}
			this.chords.add(dominants[this.random.nextInt(dominants.length)]);
			this.switchPoint = numberOfChords;
		}
	}
	
	private void setRhythm() {
    	this.rhythm = new Rhythm(random);
    	if (QuantifyMe.debug) {
    		System.out.println("Generated Rhythm: " + this.rhythm);
    	}
	}
	
	public void generateSection() {
		System.out.println("Generating section...");
		this.section = new ArrayList<>();
		for(int i = 0; i < voices.size(); i++) {
	    	Pattern pattern = voices.get(i).generateVoicedRhythm(this.chords, this.key, this.nextKey, this.switchPoint, this.rhythm, voiceDensities[i]);
	    	this.section.add(pattern);
    	}
	}
	
	public Pattern get(int voiceIndex) {
		return this.section.get(voiceIndex);
	}
	
	public Key nextKey() {
		return this.nextKey;
	}
}
