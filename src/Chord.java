import java.util.Random;

public class Chord extends PitchedVoice{
	static final double DOWNBEAT_CHANCE = 0.60;
	static final double FILL_CHANCE = 0.7;

	public Chord(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
		super(key, chords, rhythm, density, isLead, random);
	}

	@Override
	public void generatePattern() {
		boolean downbeat = false;
		for (int i = 0; i < rhythm.size(); i++) {
			String rhythmElement = rhythm.get(i);
			try {
				int rhythmValue = Integer.parseInt(rhythmElement);
				if (downbeat && this.random.nextDouble() < DOWNBEAT_CHANCE) {
					this.pattern.add("[ 0 , 2 , 4 , 6 ]");
				} else if (rhythmValue == 0) {
					this.pattern.addRest();
				} else if (random.nextDouble() < this.density) {
					this.pattern.add("[ 0 , 2 , 4 , 6 ]");
				} else {
					this.pattern.addRest();
				}
			} catch (NumberFormatException e) {
				this.pattern.add(rhythmElement);
			}
			downbeat = false;
			if(rhythmElement.equals("[")) {
				downbeat = true;
			}
		}
	}
	
	private Pattern generateFill() {
		Pattern fill = new Pattern(this.random);
		double downbeatChance = DOWNBEAT_CHANCE / 2;
		boolean downbeat = false;
		for (int i = 0; i < rhythm.size(); i++) {
			String rhythmElement = rhythm.get(i);
			try {
				int rhythmValue = Integer.parseInt(rhythmElement);
				if (downbeat && this.random.nextDouble() < downbeatChance) {
					fill.add("[ 0 , 2 , 4 , 6 ]");
				} else if (rhythmValue == 0) {
					fill.addRest();
				} else if (random.nextDouble() < this.density + ((1 - this.density)/2)) {
					fill.add("[ 0 , 2 , 4 , 6 ]");
				} else {
					fill.addRest();
				}
			} catch (NumberFormatException e) {
				fill.add(rhythmElement);
			}
			downbeat = false;
			if(rhythmElement.equals("[")) {
				downbeat = true;
			}
		}
		return fill;
	}

	@Override
	public void setPatternOverChords() {
		Pattern thisPattern = new Pattern(this.pattern);
		if(this.random.nextDouble() <= FILL_CHANCE) {
			this.pattern = this.transpose(thisPattern, this.chords.get(0));
			for(int i = 1; i < chords.size() - 1; i++) {
				this.pattern.add(this.transpose(thisPattern, this.chords.get(i)), false);
			}
			this.pattern.add(this.transpose(this.generateFill(), this.chords.get((chords.size() - 1))), false);
		} else {
			this.pattern = this.transpose(thisPattern, this.chords.get(0));
			for(int i = 1; i < chords.size(); i++) {
				this.pattern.add(this.transpose(thisPattern, this.chords.get(i)), false);
			}
		}
	}
	
	private Pattern transpose(Pattern pattern, String amount) {
		return pattern.transpose(Integer.parseInt(amount) - 7); //down an octave
	}

	@Override
	public String toString() {
		return "Chord";
	}

}
