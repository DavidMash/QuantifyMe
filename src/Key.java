import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Key{
	public enum MODE {
	    MAJOR,
	    MINOR
	}
	
	public final MODE[] majorChords = {MODE.MAJOR, MODE.MINOR, MODE.MINOR, MODE.MAJOR, MODE.MAJOR, MODE.MINOR, MODE.MINOR}; //should seven really be minor?
	public final MODE[] minorChords = {MODE.MINOR, MODE.MINOR, MODE.MAJOR, MODE.MINOR, MODE.MAJOR, MODE.MAJOR, MODE.MAJOR}; //should two really be minor?
	
	public static final Map<Integer, Integer> major = new HashMap<>();
	public static final Map<Integer, Integer> naturalMinor = new HashMap<>();
	public static final String[] tonics = {"C","C#","D","D#","E","F","F#","G","G#","A","A#","B"};
	
	public static int major(int note) {
		if(major.isEmpty()) {
			major.put(-6, -10);
			major.put(-5, -8);
			major.put(-4, -7);
			major.put(-3, -5);
			major.put(-2, -3);
			major.put(-1, -1);
			major.put(0, 0);
			major.put(1, 2);
			major.put(2, 4);
			major.put(3, 5);
			major.put(4, 7);
			major.put(5, 9);
			major.put(6, 11);
		}
		return major.get(note % 7);
	}
	
	public static int naturalMinor(int note) {
		if(naturalMinor.isEmpty()) {
			naturalMinor.put(-6, -10);
			naturalMinor.put(-5, -9);
			naturalMinor.put(-4, -7);
			naturalMinor.put(-3, -5);
			naturalMinor.put(-2, -4);
			naturalMinor.put(-1, -2);
			naturalMinor.put(0, 0);
			naturalMinor.put(1, 2);
			naturalMinor.put(2, 3);
			naturalMinor.put(3, 5);
			naturalMinor.put(4, 7);
			naturalMinor.put(5, 8);
			naturalMinor.put(6, 10);
		}
		return naturalMinor.get(note % 7);
	}
	
	public Random random;
	private MODE mode;
	private int tonicValue;
	
	public Key(int tonicValue, MODE mode, Random random) {
		this.tonicValue = tonicValue % 12;
		this.mode = mode;
		this.random = random;
	}
	
	public Key(Random random) {
		this.random = random;
		this.randomizeMode();
		this.randomizeKey();
	}
	
	public void randomizeMode() {
		this.mode = MODE.values()[random.nextInt(MODE.values().length)];
	}
	
	public void randomizeKey() {
		this.tonicValue = random.nextInt(tonics.length);
	}
	
	public void setMode(MODE mode) {
		this.mode = mode;
	}
	
	public MODE getMode() {
		return this.mode;
	}
	
	public int transposeToKey(int note, char flag) {
		int octave = note / 7;
		if(this.mode == MODE.MAJOR) {
			note = major(note);
		}else if (this.mode == MODE.MINOR) {
			note = naturalMinor(note);
		}
		note += (octave * 12);
		note += tonicValue;
		if (flag == '>') note += 1;
		else if (flag == '<') note -= 1;
		return note;
	}
	
	public Key getModulationKey() {
		int randomNote = this.random.nextInt(5) + 1; // 1 - 5
		int newTonic = this.transposeToKey(randomNote, '.') % 12;
		MODE newMode;
		if (this.mode == MODE.MAJOR) {
			newMode = this.majorChords[randomNote];
		} else {
			newMode = this.minorChords[randomNote];
		}
		return new Key(newTonic, newMode, this.random);
	}
	
	public List<String> findCommonChords(Key other) {
		List<String> chords = new LinkedList<>();
		MODE[] chordModes;
		if (this.mode == MODE.MAJOR) {
			chordModes = this.majorChords;
		} else {
			chordModes = this.minorChords;
		}
		System.out.println("The key of "+this+" and the key of "+other+" have the following chords in common:");
		for (int i = 1; i < chordModes.length - 1; i++) {
			for (int j = 1; j < chordModes.length - 1; j++) {
				if(this.getAbsoluteChord(i).equals(other.getAbsoluteChord(j))) {
					chords.add(String.valueOf(i));
					System.out.println(this.getAbsoluteChord(i));
				}
			}
		}
		return chords;
	}
	
	//gives a key that represents the note and quality of a chord in this key relative to 0 as C
	public Key getAbsoluteChord(int chordNumber) {
		MODE[] chordModes;
		int note = this.transposeToKey(chordNumber, '.');
		if (this.mode == MODE.MAJOR) {
			chordModes = this.majorChords;
		} else {
			chordModes = this.minorChords;
		}
		Key chord = new Key(note + this.tonicValue, chordModes[chordNumber], this.random);
		return chord;
	}
	
	public String toString() {
		String modeString = "";
		if(this.mode == MODE.MAJOR) {
			modeString = "Major";
		}else if (this.mode == MODE.MINOR) {
			modeString = "Minor";
		}
		return tonics[this.tonicValue] + " " + modeString;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Key)) return false;
		if (((Key)o).mode != this.mode || ((Key)o).tonicValue != this.tonicValue) {
			return false;
		}
		return true;
	}
}
