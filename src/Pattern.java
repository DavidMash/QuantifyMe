import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Pattern {
	private static final int BEAT_DICE_SIDES = 20;
	private static final int ODD_BEAT_THRESHOLD = 19;
	private static final int DOWNBEAT_DICE_SIDES = 4;
	private static final int DOWNBEAT_THRESHOLD = 1;
	private static final int OFFBEAT_DICE_SIDES = 4;
	private static final int OFFBEAT_THRESHOLD = 3;
	private static final int SUBDIVISION_DICE_SIDES = 10;
	private static final int TRIPLET_THRESHOLD = 9;
	private static final int COMMON_TIME = 4;
	
	private List<String> pattern;
	private Random random;
	private int measures;
	private boolean alreadyCut;
	
	public Pattern(String givenString, Random random) {
		this.pattern = new LinkedList<String>();
		this.add(givenString.split(" "));
		this.random = random;
		this.measures = 1;
		this.alreadyCut = false;
	}

	public Pattern(Random random) {
		this("", random);
	}
	
	public Pattern() { // we need different kinds of patterns
		this.pattern = new LinkedList<String>();
		this.measures = 0;
		this.alreadyCut = false;
	}
	
	public Pattern(Pattern givenPattern) {
		this(givenPattern.toString(), givenPattern.random);
		this.measures = givenPattern.getMeasures();
	}
	
	public Pattern(Pattern givenPattern, int repeats) {
		this(givenPattern);
		this.repeat(repeats);
	}
	
	public void repeat(int repeats) {
		if(repeats < 1) throw new IndexOutOfBoundsException();
		this.add(0, "[");
		this.add("]");
		this.add("!" + (repeats + 1));
		this.measures *= repeats + 1;
		System.out.println("Repeat: measures = "+measures);
	}

	public void reset() {
		pattern = new LinkedList<String>();
	}
	
	public void add(String newVal) {
		if (newVal.isEmpty()) return;
		String[] splitList = newVal.split(" ");
		if (splitList.length > 1) {
			this.add(splitList);
		} else {
			this.pattern.add(newVal);
		}
	}
	
	private void add(String[] strings) {
		for (String string : strings) {
			if(!string.isEmpty()) {
				this.add(string);
			}
		}
	}
	
	public void add(int index, String newVal) {
		if (newVal.isEmpty()) return;
		if(index < 0 || index >= pattern.size()) throw new IndexOutOfBoundsException();
		String[] splitList = newVal.split(" ");
		if (splitList.length > 1) {
			this.add(index, splitList);
		} else {
			this.pattern.add(index, newVal);
		}
	}
	
	private void add(int index, String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			String string = strings[i];
			if(!string.isEmpty()) {
				this.add(index + i, string);
			}
		}
	}
	
	public void add(Pattern other, boolean wrap) {
		this.measures += other.measures;
		System.out.println("Add: measures = "+measures);
		if(wrap) {
			this.add(other.wrapped().getPatternArray());
		} else {
			this.add(other.getPatternArray());
		}
	}
	
	public void add(Pattern other) {
		add(other, true);
	}
	
	public void add(int integer) {
		this.add("" + integer);
	}
	
	public void addRest() {
		this.add("~");
	}
	
	public void openBracket() {
		this.add("[");
	}
	
	public void closeBracket() {
		this.add("]");
	}
	
	public Pattern wrapped() {
		Pattern wrapped = new Pattern(this);
		wrapped.add(0, "[");
		wrapped.add("]");
		return wrapped;
	}
	
	private void setMeasures(int measures) {
		this.measures = measures;
	}
	
	public int getMeasures() {
		return this.measures;
	}
	
	public Pattern transpose(int amount) {
		Pattern transposed = new Pattern(this.random);
		for (String element : this.pattern) {
			try {
				transposed.add((Integer.parseInt(element) + amount));
			} catch (NumberFormatException e){
				transposed.add(element);
			}
		}
		transposed.setMeasures(this.measures);
		return transposed;
	}
	
	public Pattern transpose(String amountString) {
		return this.transpose(Integer.parseInt(amountString));
	}
	
	public String get(int i) {
		if(i < 0 || i >= pattern.size()) throw new IndexOutOfBoundsException();
		return pattern.get(i);
	}
	
	public String[] getPatternArray() {
		String[] array = new String[this.size()];
		for(int i = 0; i < this.pattern.size(); i++) {
			array[i] = this.pattern.get(i);
		}
		return array;
	}
	
	public void cutTimeToMeasure() {
		if(alreadyCut) {
			for(String element : this.pattern) {
				if(element.contains("/")) {
					this.pattern.remove(element);
					break;
				}
			}
		}

		this.pattern.add(0, "[");
		this.pattern.add("]");
		this.pattern.add("/" + this.measures);
		
		this.alreadyCut = true;
	}
	
	public int size() {
		return pattern.size();
	}
	
	public void generateRhythm() {
		this.reset();
		boolean triplet = (this.random.nextInt(SUBDIVISION_DICE_SIDES) >= TRIPLET_THRESHOLD);
		int beats = (this.random.nextInt(BEAT_DICE_SIDES) >= ODD_BEAT_THRESHOLD)? COMMON_TIME + (this.random.nextInt(6) - 3): COMMON_TIME;
		for(int i = 0; i < beats; i++) {
			this.openBracket(); //start of beat
			int dice = DOWNBEAT_DICE_SIDES;
			int threshold = DOWNBEAT_THRESHOLD;
			int subdivisions = ((triplet)? 3 : 2) * (random.nextInt(2) + 1);
			for(int j = 0; j < subdivisions; j++) {
				if (j > 0) {
					dice = OFFBEAT_DICE_SIDES;
					threshold = OFFBEAT_THRESHOLD;
				}
				if(random.nextInt(dice) >= threshold) this.add("1");
				else this.add("0");
			}
			this.closeBracket();
		}
	}
	
	public Pattern getPatternInKey(Pattern chords, Key key) {
		Pattern result = new Pattern(this.random);
		String[] patternArray = this.getPatternArray();
		boolean ignoreNext = false;
		for(String note : patternArray) {
			if(ignoreNext) {
				ignoreNext = false;
				result.add(note);
				continue;
			}
			try {
				int noteAsNum = Integer.parseInt(note);
				result.add(key.transposeToKey(noteAsNum));
			}catch (NumberFormatException e) {
				result.add(note);
			}
		}
		result.setMeasures(this.measures);
		
		return result;
	}
	
	public String toString() {
		StringBuilder patternString = new StringBuilder();
		for (int i = 0; i < pattern.size(); i++) {
			if(i > 0) patternString.append(" ");
			patternString.append(this.pattern.get(i));
		}
		return patternString.toString();
	}
}
