import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

public class VoicedRhythm extends Rhythm{
	public static int FILL = 0;
	public static int DOWNBEAT = 1;
	public static int EVERY_BEAT = 2;
	public static int EVERY_OTHER_BEAT = 3;
	public static int EVERY_SECOND_BEAT = 4;
	public static int EVERY_NOTE = 5;
	public static int EVERY_OTHER_NOTE = 6;
	public static int OFFBEAT = 7;
	public static int PEDAL = 8;
	public static int P_STOP_BEAT = 9;
	public static int P_STOP_HALF = 10;
	private double[] chances;
	private boolean[] active;
	private int[] counter;
	private boolean resetChanceOnBeat;
	private double density;
	private Rhythm base;
	private Voice voice;
	private boolean beatRepeat;
	private boolean halfTime;
	private boolean holdPedal;
	private Pattern fill = null;
	private List<Pattern> patterns;
	
	public VoicedRhythm(Voice voice, Rhythm other, double density, boolean resetChanceOnBeat, boolean beatRepeat, boolean halfTime) {
		super(other);
		this.base = other;
		this.voice = voice;
		this.resetChanceOnBeat = resetChanceOnBeat;
		if (this.voice.getChances().length < 11) { 
			throw new IndexOutOfBoundsException ("The \"chances\" array given to VoicedRhythm did not have enough!");
		}
		this.chances = this.voice.getChances();
		this.density = density;
		this.beatRepeat = beatRepeat;
		this.halfTime = halfTime;
		this.patterns = new LinkedList<>();
		this.generateVoicedRhythm(0);
		this.holdPedal = true; //TODO:make this false
		if (this.random.nextDouble() < this.chances[PEDAL]) {
			this.holdPedal = true;
		}
		this.pedal();
		this.parseNotes();
	}
	
	public VoicedRhythm(VoicedRhythm other, Pattern newPattern, boolean parse) {
		super(other);
		this.base = other.base;
		this.voice = other.voice;
		this.resetChanceOnBeat = other.resetChanceOnBeat;
		if (this.voice.getChances().length < 8) { 
			throw new IndexOutOfBoundsException ("The \"chances\" array given to VoicedRhythm did not have enough!");
		}
		this.chances = this.voice.getChances();
		this.density = other.density;
		this.beatRepeat = other.beatRepeat;
		this.halfTime = other.halfTime;
		this.pattern = newPattern.pattern;
		this.holdPedal = other.holdPedal;
		if (parse) {
			this.parseNotes();
		}
	}
	
	// get new material over each chord
	public Pattern generateOverChords(List<String> chords, Key key, Key nextKey, int switchPoint, boolean silenced) {
		//System.out.println(key+" to "+nextKey+" on chord number "+switchPoint);
		Key currKey = key;
		int chordCount = 0;
		currKey = (chordCount >= switchPoint)? nextKey: key;
		Pattern newPattern = this.generateVoicedRhythm(0);
		Pattern resultPattern = newPattern.transpose(chords.get(0), currKey).getPatternInKey(currKey);
		if(this.random.nextDouble() <= chances[FILL]) {
			for(int i = 1; i < chords.size() - 1; i++) {
				chordCount++;
				currKey = (chordCount >= switchPoint)? nextKey: key;
				newPattern = this.generateVoicedRhythm(i);
				resultPattern.add(newPattern.transpose(chords.get(i), currKey).getPatternInKey(currKey), false);
			}
			chordCount++;
			currKey = (chordCount >= switchPoint)? nextKey: key;
			Pattern fill = this.generateVoicedFill();
			resultPattern.add(fill.transpose((chords.get((chords.size() - 1))), currKey).getPatternInKey(currKey), false);
		} else {
			for(int i = 1; i < chords.size(); i++) {
				chordCount++;
				currKey = (chordCount >= switchPoint)? nextKey: key;
				newPattern = this.generateVoicedRhythm(i);
				resultPattern.add(newPattern.transpose(chords.get(i), currKey).getPatternInKey(currKey), false);
			}
		}
		if (silenced) {
			return silence(resultPattern);
		}
		//System.out.println("Generated Over Chords: "+resultPattern);
		return resultPattern;
	}
	
	private Pattern silence(Pattern pattern) {
		for (int i = 0; i < pattern.size(); i++) {
			String element = pattern.get(i);
			if (element.equals("n")) {
				throw new NumberFormatException("Pattern should be parsed first, but an 'n' was found in the pattern: " + pattern);
			}
			try {
				Integer.parseInt(element);
				pattern.add(i, "~");
				pattern.remove(i + 1);
			} catch (NumberFormatException e) {
				
			}
		}
		return pattern;
	}
	
	// repeat the same content over each chord
	public Pattern getOverChords(List<String> chords, Key key, Key nextKey, int switchPoint, boolean silenced) {
		//System.out.println(key+" to "+nextKey+" on chord number "+switchPoint);
		Key currKey = key;
		int chordCount = 0;
		currKey = (chordCount >= switchPoint)? nextKey: key;
		Pattern newPattern = this.generateVoicedRhythm(0);
		Pattern resultPattern =  newPattern.transpose(chords.get(0), currKey).getPatternInKey(currKey);
		if(this.random.nextDouble() <= chances[FILL]) {
			for(int i = 1; i < chords.size() - 1; i++) {
				chordCount++;
				currKey = (chordCount >= switchPoint)? nextKey: key;
				resultPattern.add((newPattern.transpose(chords.get(i), currKey)).getPatternInKey(currKey), false);
			}
			chordCount++;
			currKey = (chordCount >= switchPoint)? nextKey: key;
			Pattern fill = this.generateVoicedFill();
			resultPattern.add(fill.transpose((chords.get((chords.size() - 1))), currKey).getPatternInKey(currKey), false);
		} else {
			for(int i = 1; i < chords.size(); i++) {
				chordCount++;
				currKey = (chordCount >= switchPoint)? nextKey: key;
				resultPattern.add((newPattern.transpose(chords.get(i), currKey)).getPatternInKey(currKey), false);
			}
		}
		if (silenced) {
			return silence(resultPattern);
		}
		//System.out.println("Pattern Over Chords: "+resultPattern);
		return resultPattern;
	}
	
	public Pattern getOverChordsNoTransposing(List<String> chords, boolean silenced) {
		Pattern newPattern = new Pattern(this.generateVoicedRhythm(0));
		if(this.random.nextDouble() <= chances[FILL]) {
			newPattern.repeat(chords.size() - 2);
			Pattern fill = this.generateVoicedFill();
			newPattern.add(fill);
		} else {
			newPattern.repeat(chords.size() - 1);
		}
		if (silenced) {
			return silence(newPattern);
		}
		return newPattern;
	}
	
	public void parseNotes() {
		Pattern newPattern = new Pattern();
		for (int i = 0; i < this.pattern.size(); i++) {
			String element = this.pattern.get(i);
			if (element.equals("n")) {
				newPattern.add(this.voice.next());
			} else {
				newPattern.add(element);
			}
		}
		this.pattern = newPattern.pattern;
		System.out.println("Parsed note pattern: " + this);
	}
	
	public void pedal() {
		//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		//System.out.println("Original pattern: " + this);
		if (!this.holdPedal) return;
		int count = 1;
		boolean start = false;
		for (int i = 0; i < this.pattern.size(); i++) {
			String element = this.pattern.get(i);
			if (element.equals("[") || element.equals("]")) {
				this.remove(i);
				i--;
			} else { 
				if (start && element.equals("~")) {
					this.remove(i);
					count++;
					i--;
				} else if (element.equals("n")) {
					start = true;
					if (count > 1) {
						this.elongate(i - 1, count);
						i += 2;
						count = 1;
					}
				}
			}
		}
		if (count > 1) {
			this.elongate(this.pattern.size() - 1, count);
		}
		this.add(0, "[");
		this.add("]");
		//System.out.println("Pedal pattern: " + this);
	}
	
	public Pattern generateVoicedRhythm(int patternIndex) {
		if (this.patterns.size() > patternIndex) {
			return this.patterns.get(patternIndex);
		}
		Pattern newPattern = new Pattern();
		this.active = new boolean[]{false, false, false, false, false, false, false, false};
		this.counter = new int[]{0, 0, 1, 0, 0, 0, 1, 0};
		this.active[DOWNBEAT] = true;
		int halfTimeCounter = 0;
		int skipSubdivisions = 4;
		int subdivisionCounter = skipSubdivisions;
		for (int i = 0; i < base.size(); i++) {
			String rhythmElement = base.get(i);
			if (rhythmElement.equals("]")) {
				newPattern.add(rhythmElement);
				if (beatRepeat) {
					break;
				}
				continue;
			}
			if(rhythmElement.equals("[")) {
				newPattern.add(rhythmElement);
				
				subdivisionCounter = skipSubdivisions;
				
				halfTimeCounter++;
				if (halfTime && halfTimeCounter % 2 == 0) {
					continue;
				}
				
				if (this.resetChanceOnBeat) {
					this.counter[OFFBEAT] = 0;
					this.active[OFFBEAT] = false;
					this.counter[EVERY_OTHER_NOTE] = 1;
					this.active[EVERY_OTHER_NOTE] = true;
				}
				this.active[EVERY_BEAT] = true;
				if(this.counter[EVERY_OTHER_BEAT] >= 1) {
					this.active[EVERY_OTHER_BEAT] = true;
					this.counter[EVERY_OTHER_BEAT] = 0;
				} else {
					this.active[EVERY_OTHER_BEAT] = false;
					this.counter[EVERY_OTHER_BEAT]++;
				}
				if(this.counter[EVERY_SECOND_BEAT] >= 1) {
					this.active[EVERY_SECOND_BEAT] = true;
					this.counter[EVERY_SECOND_BEAT] = 0;
				} else {
					this.active[EVERY_SECOND_BEAT] = false;
					this.counter[EVERY_SECOND_BEAT]++;
				}
			} else {
				subdivisionCounter++;
				if (subdivisionCounter < skipSubdivisions) {
					continue;
				}else {
					subdivisionCounter = 0;
				}
				if (this.checkChances()) {
					newPattern.add("n");
				} else if (rhythmElement.equals("0")) {
					newPattern.addRest();
				} else if (random.nextDouble() < this.density) {
					newPattern.add("n");
				} else {
					newPattern.addRest();
				}
				if(this.counter[EVERY_OTHER_NOTE] >= 1) {
					this.active[EVERY_OTHER_NOTE] = true;
					this.counter[EVERY_OTHER_NOTE] = 0;
				} else {
					this.active[EVERY_OTHER_NOTE] = false;
					this.counter[EVERY_OTHER_NOTE]++;
				}
				this.active[EVERY_NOTE] = true;
				if(this.counter[OFFBEAT] >= (this.highestSubdivision / ((this.triplet)? 3: 2)) - 1) {
					this.active[OFFBEAT] = true;
					this.counter[OFFBEAT] = 0;
				} else {
					this.active[OFFBEAT] = false;
					this.counter[OFFBEAT]++;
				}
				this.active[DOWNBEAT] = false;
				this.active[EVERY_BEAT] = false;
				this.active[EVERY_OTHER_BEAT] = false;
				this.active[EVERY_SECOND_BEAT] = false;
			}
		}
		if (beatRepeat) {
			newPattern.physicalRepeat(base.beats - 1);
		}
		VoicedRhythm newVR = new VoicedRhythm(this, newPattern, false);
		newVR.pedal();
		newVR.parseNotes();
		this.patterns.add(newVR);
		return newVR;
	}
	
	public Pattern generateVoicedFill() {
		if (this.fill != null) {
			return this.fill;
		}
		Pattern fill = new Pattern();
		this.active = new boolean[]{false, true, false, false, true, false, false, false};
		this.counter = new int[]{0, 0, 1, 0, 0, 0, 1, 0};
		this.active[DOWNBEAT] = true;
		int skipSubdivisions = 4;
		int subdivisionCounter = skipSubdivisions;
		for (int i = 0; i < base.size(); i++) {
			String rhythmElement = base.get(i);
			if (rhythmElement.equals("]")) {
				fill.add(rhythmElement);
				continue;
			}
			if(rhythmElement.equals("[")) {
				fill.add(rhythmElement);
				
				subdivisionCounter = skipSubdivisions;
				
				if (this.resetChanceOnBeat) {
					this.counter[OFFBEAT] = 0;
					this.active[OFFBEAT] = false;
					this.counter[EVERY_OTHER_NOTE] = 1;
					this.active[EVERY_OTHER_NOTE] = true;
				}
				this.active[EVERY_BEAT] = true;
				if(this.counter[EVERY_OTHER_BEAT] >= 1) {
					this.active[EVERY_OTHER_BEAT] = true;
					this.counter[EVERY_OTHER_BEAT] = 0;
				} else {
					this.active[EVERY_OTHER_BEAT] = false;
					this.counter[EVERY_OTHER_BEAT]++;
				}
				if(this.counter[EVERY_SECOND_BEAT] >= 1) {
					this.active[EVERY_SECOND_BEAT] = true;
					this.counter[EVERY_SECOND_BEAT] = 0;
				} else {
					this.active[EVERY_SECOND_BEAT] = false;
					this.counter[EVERY_SECOND_BEAT]++;
				}
				this.active[EVERY_BEAT] = true;
			} else {
				subdivisionCounter++;
				if (subdivisionCounter < skipSubdivisions) {
					continue;
				}else {
					subdivisionCounter = 0;
				}
				if (this.checkChances() && this.checkChances()) {
					fill.add("n");
				} else if (rhythmElement.equals("0")) {
					fill.addRest();
				} else if (random.nextDouble() < this.density + ((1 - this.density) / 2)) {
					fill.add("n");
				} else {
					fill.addRest();
				}
				if(this.counter[EVERY_OTHER_NOTE] >= 1) {
					this.active[EVERY_OTHER_NOTE] = true;
					this.counter[EVERY_OTHER_NOTE] = 0;
				} else {
					this.active[EVERY_OTHER_NOTE] = false;
					this.counter[EVERY_OTHER_NOTE]++;
				}
				this.active[EVERY_NOTE] = true;
				if(this.counter[OFFBEAT] >= (this.highestSubdivision / ((this.triplet)?3:2))) {
					this.active[OFFBEAT] = true;
					this.counter[OFFBEAT] = 0;
				} else {
					this.active[OFFBEAT] = false;
					this.counter[OFFBEAT]++;
				}
				this.active[DOWNBEAT] = false;
				this.active[EVERY_BEAT] = false;
				this.active[EVERY_OTHER_BEAT] = false;
				this.active[EVERY_SECOND_BEAT] = false;
			}
		}
		VoicedRhythm newVR = new VoicedRhythm(this, fill, false);
		newVR.pedal();
		newVR.parseNotes();
		this.fill = newVR;
		return newVR;
	}
	
	private boolean checkChances() {
		if(active[DOWNBEAT] && random.nextDouble() < chances[DOWNBEAT]) {
			return true;
		}
		if(active[EVERY_BEAT] && random.nextDouble() < chances[EVERY_BEAT]) {
			return true;
		}
		if(active[EVERY_OTHER_BEAT] && random.nextDouble() < chances[EVERY_OTHER_BEAT]) {
			return true;
		}
		if(active[EVERY_SECOND_BEAT] && random.nextDouble() < chances[EVERY_SECOND_BEAT]) {
			return true;
		}
		if(active[EVERY_NOTE] && random.nextDouble() < chances[EVERY_NOTE]) {
			return true;
		}
		if(active[EVERY_OTHER_NOTE] && random.nextDouble() < chances[EVERY_OTHER_NOTE]) {
			return true;
		}
		if(active[OFFBEAT] && random.nextDouble() < chances[OFFBEAT]) {
			return true;
		}
		return false;
	}
}
