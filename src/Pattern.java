import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Pattern {
	
	protected List<String> pattern;
	protected int measures;
	private boolean alreadyCut;
	
	public Pattern(String givenString) {
		this.pattern = new LinkedList<String>();
		this.add(givenString.split(" "));
		this.measures = 1;
		this.alreadyCut = false;
	}
	
	public Pattern(boolean noMeasures) { //TODO: Make a different way to do no measures
		this();
		if (noMeasures) {
			this.measures = 0;
		}
	}
	
	public Pattern() {
		this("");
	}
	
	public Pattern(Pattern givenPattern) {
		this(givenPattern.toString());
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
		//System.out.println("*****************************((((((((((((((()))))))))))))))");
		System.out.println("Repeat completed, measures = "+this.measures);
	}

	public void physicalRepeat(int repeats) {
		if(repeats < 1) throw new IndexOutOfBoundsException();
		Pattern copy = new Pattern(this);
		for (int i = 0; i < repeats; i++) {
			this.add(copy);
		}
	}
	
	public void elongate(int i, int multiple) {
		if(multiple < 1) throw new IndexOutOfBoundsException();
		this.add(i + 1, "_" + multiple);
		this.add(i + 1, "]");
		this.add(i, "[");
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
		if(index < 0 || index > pattern.size()) throw new IndexOutOfBoundsException();
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
		if(wrap) {
			this.add(other.wrapped().getPatternArray());
		} else {
			this.add(other.getPatternArray());
		}
		//System.out.println("*****************************((((((((((((((()))))))))))))))");
		//System.out.println("ADD completed, measures = "+this.measures);
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
	
	public void remove(int i) {
		if(i < 0 || i >= pattern.size()) throw new IndexOutOfBoundsException();
		this.pattern.remove(i);
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
	
	public Pattern transpose(String string, Key key) {
		Pattern transposed = new Pattern();
		for (String element : this.pattern) {
			try {
				String flag = ".";
				if (key.getMode() == Key.MODE.MINOR && string.equals("4") && element.equals("2")) {
					flag = ">"; //raise the third of the five chord in minor
				} else if (key.getMode() == Key.MODE.MAJOR && string.charAt(0) == 'm' && element.equals("2")) {
					flag = "<"; //lower the third of an explicit minor chord
					string = string.substring(1);
				}
				transposed.add(flag + (Integer.parseInt(element) + Integer.parseInt(string)));
			} catch (NumberFormatException e){
				transposed.add(element);
			}
		}
		transposed.setMeasures(this.measures);
		return transposed;
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
	
	public Pattern getPatternInKey(Key key) {
		//System.out.println("Getting Pattern:"+this);
		//System.out.println("Getting Key: "+key);
		Pattern result = new Pattern();
		String[] patternArray = this.getPatternArray();
		boolean ignoreNext = false;
		for(String note : patternArray) {
			if(ignoreNext) {
				ignoreNext = false;
				result.add(note);
				continue;
			}
			if (note.charAt(0) == '_') {
				result.add(note);
				continue;
			}
			try {
				int noteAsNum = Integer.parseInt(note.substring(1));
				result.add(key.transposeToKey(noteAsNum, note.charAt(0)));
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
