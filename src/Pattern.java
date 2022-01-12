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
	
	private StringBuilder pattern;
	private Random random;
	
	public Pattern(String givenString, Random random) {
		this.pattern = new StringBuilder();
		this.add(givenString);
		this.random = random;
	}
	
	public Pattern(Random random) {
		this("", random);
	}
	
	public Pattern(Pattern givenPattern, Random random) {
		this(givenPattern.toString(), random);
	}
	
	public void reset() {
		pattern = new StringBuilder();
	}
	
	public void add(String newVal) {
		if(newVal.isEmpty()) return;
		if(!pattern.isEmpty()) pattern.append(" ");
		pattern.append(newVal);
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
	
	public void cutTimeBy(int divisor) {
		this.pattern.insert(0, "[ ");
		this.pattern.append(" ] / "+divisor);
	}
	
	public void generateRhythm() {
		this.reset();
		int roll = random.nextInt(SUBDIVISION_DICE_SIDES);
		boolean triplet = (roll >= TRIPLET_THRESHOLD);
		roll = random.nextInt(BEAT_DICE_SIDES);
		int beats = (roll >= ODD_BEAT_THRESHOLD)? COMMON_TIME + (random.nextInt(6) - 3): COMMON_TIME;
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
				roll = random.nextInt(dice);
				if(roll >= threshold) this.add("1");
				else this.add("0");
			}
			this.closeBracket();
		}
	}
	
	public String toString() {
		return pattern.toString();
	}
}
