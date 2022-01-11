import java.util.Map;
import java.util.Random;

public class Pattern {
	private StringBuilder pattern;
	private Random random;
	
	public Pattern(String givenString) {
		this.pattern = new StringBuilder();
		this.add(givenString);
		random = new Random();
	}
	
	public Pattern() {
		this("");
	}
	
	public Pattern(Pattern givenPattern) {
		this(givenPattern.toString());
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
	
	public void repeatOverChords(Pattern chords) {
		String[] roots = chords.toString().split(" ");
		String[] oldPattern = this.pattern.toString().split(" ");
		this.reset();
		for(String root : roots) {
			int rootAsNum = Integer.parseInt(root);
			boolean ignoreNext = false;
			for(String note : oldPattern) {
				if(ignoreNext) {
					ignoreNext = false;
					this.add(note);
					continue;
				}
				try {
					int noteAsNum = Integer.parseInt(note);
					this.add(rootAsNum+noteAsNum);
				}catch (NumberFormatException e) {
					this.add(note);
					if(note.equals("/") || note.equals("*")) {
						ignoreNext = true;
					}
				}
			}
		}
		this.cutTimeBy(roots.length);
	}
	
	public void cutTimeBy(int divisor) {
		this.pattern.insert(0, "[ ");
		this.pattern.append(" ] / "+divisor);
	}
	
	public Pattern inKey() {
		Pattern result = new Pattern();
		Map<Integer, Integer> scale = Scale.major();
		String[] patternArray = this.pattern.toString().split(" ");
		boolean ignoreNext = false;
		for(String note : patternArray) {
			if(ignoreNext) {
				ignoreNext = false;
				result.add(note);
				continue;
			}
			try {
				int noteAsNum = Integer.parseInt(note);
				int scaleDegree = noteAsNum % 7;
				int octave = (noteAsNum / 7) * 12;
				result.add(scale.get(scaleDegree)+octave);
			}catch (NumberFormatException e) {
				result.add(note);
				if(note.equals("/") || note.equals("*")) {
					ignoreNext = true;
				}
			}
		}
		return result;
	}
	
	public String toString() {
		return pattern.toString();
	}
}
