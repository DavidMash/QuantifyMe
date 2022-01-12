import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Key {
	public enum MODE {
	    MAJOR,
	    NATURAL_MINOR,
	    HARMONIC_MINOR
	}
	
	public static final Map<Integer, Integer> major = new HashMap<>();
	public static final Map<Integer, Integer> naturalMinor = new HashMap<>();
	public static final Map<Integer, Integer> harmonicMinor = new HashMap<>();
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
		return major.get(note);
	}
	
	public static int naturalMinor(int note) {
		if(naturalMinor.isEmpty()) {
			naturalMinor.put(-6, -9);
			naturalMinor.put(-5, -8);
			naturalMinor.put(-4, -6);
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
		return naturalMinor.get(note);
	}
	
	public static int harmonicMinor(int note) {
		if(harmonicMinor.isEmpty()) {
			harmonicMinor.put(-6, -9);
			harmonicMinor.put(-5, -8);
			harmonicMinor.put(-4, -6);
			harmonicMinor.put(-3, -5);
			harmonicMinor.put(-2, -4);
			harmonicMinor.put(-1, -1);
			harmonicMinor.put(0, 0);
			harmonicMinor.put(1, 2);
			harmonicMinor.put(2, 3);
			harmonicMinor.put(3, 5);
			harmonicMinor.put(4, 7);
			harmonicMinor.put(5, 8);
			harmonicMinor.put(6, 11);
		}
		return harmonicMinor.get(note);
	}
	
	public Random random;
	private MODE mode;
	private int tonicValue;
	private Map<Integer,Integer> modeMap;
	
	public Key(Random random) {
		this.random = random;
		this.randomizeMode();
		this.randomizeKey();
	}
	
	public void randomizeMode() {
		this.mode = MODE.values()[random.nextInt(MODE.values().length)];
		modeMap = null;
	}
	
	public void randomizeKey() {
		this.key = random.nextInt(tonics.length);
	}
	
	public int transposeToKey(int note) {
		if(this.mode == MODE.MAJOR) {
			note = major(note);
		}else if (this.mode == MODE.NATURAL_MINOR) {
			note = naturalMinor(note);
		}else if (this.mode == MODE.HARMONIC_MINOR) {
			note = harmonicMinor(note);
		}
		note += tonicValue;
		return note;
	}
	
	public String toString() {
		String modeString = "";
		if(this.mode == MODE.MAJOR) {
			modeString = "Major";
		}else if (this.mode == MODE.NATURAL_MINOR) {
			modeString = "Natural Minor";
		}else if (this.mode == MODE.HARMONIC_MINOR) {
			modeString = "Harmonic Minor";
		}
		return tonics[this.tonicValue] + " " + modeString;
	}
}
