import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Section {
	private double HALF_TIME_CHANCE = 0.2; // should these be lowercase? probably yeah
	private double BEAT_REPEAT_CHANCE = 0.2;
	
	private List<Voice> voices; 
	private Rhythm rhythm;
	private List<String> chords;
	private Double[] voiceDensities;
	private Random random;
	private double sectionDensity;
	private double modulationChance;
	private Key key;
	private Key nextKey;
	private int switchPoint;
	private boolean buildUp;
	private boolean halfTime;
	private boolean[] silenced;
	
	public Section(List<Voice> voices, Key key, double sectionDensity, double modulationChance, Random random, boolean buildUp, boolean halfTime) {
		this.voices = new ArrayList<Voice>(voices.size());
		for (Voice otherVoice : voices) {
			this.voices.add(otherVoice.copy());
		}
		this.silenced = new boolean[voices.size()];
		for (int i = 0; i < silenced.length; i++) {
			this.silenced[i] = false;
		}
		this.random = random;
		this.key = key;
		this.nextKey = this.key;
		this.sectionDensity = sectionDensity;
		this.modulationChance = modulationChance;
		this.buildUp = buildUp;
		if (this.buildUp) {
			this.BEAT_REPEAT_CHANCE = 0.8;
		}
		this.halfTime = halfTime;
		if (this.halfTime) {
			this.HALF_TIME_CHANCE = 0.95;
		}
		this.assignVoiceDensities();
		this.setChords();
		this.setRhythm();
		this.generateSection();
	}
	
	public Section(Section other, Key key, double sectionDensity, boolean buildup, boolean halftime, boolean redoVoices, boolean redoChords) {
		this.voices = new ArrayList<Voice>(other.voices.size());
		for (Voice otherVoice : other.voices) {
			this.voices.add(otherVoice.copy());
		}
		this.silenced = Arrays.copyOf(other.silenced, other.silenced.length);
		this.random = other.random;
		this.key = key;
		this.nextKey = this.key;
		this.sectionDensity = sectionDensity;
		this.modulationChance = other.modulationChance;
		this.buildUp = buildUp || other.buildUp;
		if (this.buildUp) {
			this.BEAT_REPEAT_CHANCE = 0.8;
		}
		this.halfTime = halftime || other.halfTime;
		if (this.halfTime) {
			this.HALF_TIME_CHANCE = 0.95;
		}
		this.rhythm = other.rhythm;
		this.voices = other.voices;
		this.voiceDensities = other.voiceDensities;
		if (redoChords) {
			this.setChords();
		} else {
			this.chords = other.chords;
		}
		if (redoVoices) {
			this.assignVoiceDensities();
			this.generateSection();
		}
	}
	
	//remove the nth highest density voice
	public void removeVoice(int number) {
		Map<Double, Integer> voiceDensityToIndex = new HashMap<Double, Integer>();
		for (int i = 0; i < voices.size(); i++) {
			voiceDensityToIndex.put(this.voiceDensities[i], i);
		}
		Double[] sortedDensities = Arrays.copyOf(this.voiceDensities, this.voiceDensities.length);
		Arrays.sort(sortedDensities);
		this.silenced[voiceDensityToIndex.get(sortedDensities[sortedDensities.length - 1 - number])] = true;
	}
	
	private void assignVoiceDensities() {
		this.voiceDensities = new Double[voices.size()];
	    for(int i = 0; i < voices.size(); i++) {
	    	int voiceIndex = this.random.nextInt(voices.size());
	    	while(this.voiceDensities[voiceIndex] != null) {
	    		voiceIndex = (voiceIndex + 1) % voices.size();
	    	}
	    	// the 0.0001 makes it so no two voices have the same exact density
	    	final double WEIGHT = 0.2;
	    	this.voiceDensities[voiceIndex] = (Double) (((i * this.sectionDensity) - WEIGHT) / ((double) voices.size())) + 0.0001;
	    }
	}
		
	private void setChords() {
		int numberOfChords = 4; //TODO: Make the number of chords random
		this.chords = new ArrayList<>(); 
	    boolean modulate = true;//(this.random.nextDouble() < this.modulationChance); TODO:uncomment
	    System.out.println("Modulate? "+modulate);
		if (modulate) {
			this.nextKey = this.key.getModulationKey();
			List<String> commonChords = this.key.findCommonChords(this.nextKey);
			List<String> roots = new ArrayList<>(Arrays.asList("0", "5"));
			List<String> predominants = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "5"));
			List<String> dominants = new ArrayList<>(Arrays.asList("4"));
			this.switchPoint = numberOfChords - 1;
			
			this.chords.add(roots.get(this.random.nextInt(roots.size())));
			if (chords.get(chords.size() - 1).equals("0")) {
				predominants.add("5");
			}
			for(int i = 1; i < numberOfChords - 1; i++) {
				if (!commonChords.isEmpty() && (commonChords.size() > (numberOfChords - i - 1))) {
					this.chords.add(commonChords.get(this.random.nextInt(commonChords.size())));
				} else {
					this.chords.add(predominants.get(this.random.nextInt(predominants.size())));
				}
			}
			if (this.nextKey.getMode() == Key.MODE.MAJOR && chords.get(chords.size() - 1).equals("3")) {
				this.switchPoint = numberOfChords - 2;
				dominants.add("m3");
			}
			this.chords.add(dominants.get(this.random.nextInt(dominants.size())));
		} else {
			/*List<String> roots = new ArrayList<>(Arrays.asList("0", "5"));
			List<String> predominants = new ArrayList<>(Arrays.asList("0", "1", "2", "3"));
			List<String> dominants = new ArrayList<>(Arrays.asList("4"));

			this.chords.add(roots.get(this.random.nextInt(roots.size())));
			if (chords.get(chords.size() - 1).equals("0")) {
				predominants.add("5");
			}
			for(int i = 1; i < numberOfChords - 1; i++) {
				this.chords.add(predominants.get(this.random.nextInt(predominants.size())));
			}
			if (this.switchPoint < numberOfChords - 1 && this.nextKey.getMode() == Key.MODE.MAJOR && chords.get(chords.size() - 1).equals("3")) {
				dominants.add("m3");
			}
			this.chords.add(dominants.get(this.random.nextInt(dominants.size())));*/
			String[] options;
			if (this.key.getMode() == Key.MODE.MAJOR) {
				options = new String[] {
						//"0 2 1 4",
						//"0 0 3 4",
						//"0 4 5 4",
						//"0 3 4 3",
						"0 5 3 m3",
						"0 0 3 m3",
						"0 2 3 m3"
				};
			} else {
				options = new String[] {
						"0 0 3 4",
						"0 2 4 6",
						"0 0 0 4",
				};
			}
			this.chords = Arrays.asList(options[this.random.nextInt(options.length)].split(" "));
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
		for(int i = 0; i < voices.size(); i++) {
			boolean beatRepeat = (this.random.nextDouble() < this.BEAT_REPEAT_CHANCE);
			boolean halfTime = (this.random.nextDouble() < this.HALF_TIME_CHANCE);
	    	voices.get(i).generateVoicedRhythm(this.chords, this.key, this.nextKey, this.switchPoint, this.rhythm, voiceDensities[i], beatRepeat, halfTime);
    	}
	}
	
	public List<Voice> getVoices(){
		return this.voices;
	}
	
	public Pattern get(int voiceIndex) {
		Pattern voicedPattern = this.voices.get(voiceIndex).setOverChords(chords, key, nextKey, voiceIndex, silenced[voiceIndex]);
		//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		//System.out.println("Measures: " + voicedPattern.measures);
		return voicedPattern;
	}
	
	public Key nextKey() {
		return this.nextKey;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("\nThis section is in " + this.key.toString() + ".");
		if (this.nextKey != this.key) {
			result.append("\nIt modulates to " + this.nextKey + ".");
		}
		result.append("\nIt has the base rhythm of: " + this.rhythm);
		result.append("\nChords: ");
		Key currKey = this.key;
		int chordCount = 0;
		for (String chord : this.chords) {
			currKey = (chordCount >= switchPoint)? nextKey: key;
			char flag = '.';
			int chordInt;
			try {
				chordInt = Integer.parseInt(chord);
			} catch (NumberFormatException e) {
				flag = chord.charAt(0);
				chordInt = Integer.parseInt(chord.substring(1));
			}
			
			String chordString;
			if (currKey.getMode() == Key.MODE.MAJOR) {
				if (flag == 'm') {
					chordString = (new Key(currKey.transposeToKey(chordInt, '.'), Key.MODE.MINOR, this.random)).toString();
				} else {
					chordString = (new Key(currKey.transposeToKey(chordInt, '.'), currKey.majorChords[chordInt], this.random)).toString();
				}
			} else {
				if(chordCount == this.chords.size() - 1 && chordInt == 4) {
					chordString = (new Key(currKey.transposeToKey(chordInt, '.'), Key.MODE.MAJOR, this.random)).toString();
				} else {
					chordString = (new Key(currKey.transposeToKey(chordInt, '.'), currKey.minorChords[chordInt], this.random)).toString();
				}
			}
			if (chordCount > 0) {
				result.append(", ");
			}
			result.append(chordString);
			chordCount++;
		}
		return result.toString();
	}
}
