import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	
	public Pattern(String givenString, Random random) {
		this.pattern = new LinkedList<String>();
		this.add(givenString.split(" "));
		this.random = random;
	}

	public Pattern(Random random) {
		this("", random);
	}
	
	public Pattern(Pattern givenPattern) {
		this(givenPattern.toString(), givenPattern.random);
	}
	
	public Pattern(Pattern givenPattern, int repeats) {
		this(givenPattern.toString(), givenPattern.random);
		this.repeat(repeats);
	}
	
	public void repeat(int repeats) {
		this.repeat(repeats, this.size());
	}
	
	public void repeat(int repeats, int end) {
		this.repeat(repeats, 0, end);
	}
	
	public void repeat(int repeats, int start, int end) {
		if(start < 0 || end > this.size()) throw new IndexOutOfBoundsException();
		int copyPoint = end;
		Pattern subpattern = this.getSubpattern(start, end);
		for(int r = 0; r < repeats; r++) {
			for(int i = start; i < end; i++) {
				this.pattern.add(copyPoint++, subpattern.get(i));
			}
		}
	}
	
	public Pattern getSubpattern(int start, int end) {
		Pattern subpattern = new Pattern(this.random);
		for(int i = start; i < end; i++) {
			subpattern.add(this.get(i));
		}
		return subpattern;
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
	
	public void add(String[] strings) {
		for (String string : strings) {
			if(!string.isEmpty()) {
				this.add(string);
			}
		}
	}
	
	public void add(String newVal, int percentChance) {
		int roll = this.random.nextInt(101); //0 to 100
		if (roll >= percentChance) {
			this.add(newVal);
		} else {
			this.addRest();
		}
	}
	
	public void add(Pattern other) {
		this.add(other.getPatternArray());
	}
	
	public void add(int integer, int percentChance) {
		this.add(""+integer, percentChance);
	}
	
	public void add(int integer) {
		this.add(""+integer);
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
	
	public void addRandomNotes(int numberCount, int lowerRange, int upperRange) {
		for (int i =  0; i < numberCount; i++) {
			this.add(random.nextInt(upperRange - lowerRange) + lowerRange);
		}
	}
	
	public void addRandomBinary(int numberCount) {
		for (int i =  0; i < numberCount; i++) {
			this.add(random.nextInt(2));
		}
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
	
	public void cutTimeBy(int divisor) {
		this.pattern.add(0, "[ ");
		this.pattern.add(" ] / "+divisor);
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
	
	public String toString() {
		StringBuilder patternString = new StringBuilder();
		for (int i = 0; i < pattern.size(); i++) {
			if(i > 0) patternString.append(" ");
			patternString.append(this.pattern.get(i));
		}
		return patternString.toString();
	}
}
