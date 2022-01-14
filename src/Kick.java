import java.util.Random;

public class Kick extends UnpitchedVoice{
	static final double DOWNBEAT_CHANCE = 0.8;
	static final double FILL_CHANCE = 1;

	public Kick(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
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
					this.pattern.add("0");
				} else if (rhythmValue == 0) {
					this.pattern.addRest();
				} else if (random.nextDouble() < this.density / 2) {
					this.pattern.add("0");
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
					fill.add("0");
				} else if (rhythmValue == 0) {
					fill.addRest();
				} else if (random.nextDouble() < this.density + ((1 - this.density)/2)) {
					fill.add("0");
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
		if(this.random.nextDouble() <= FILL_CHANCE) {
			this.pattern.repeat(2);
			this.pattern.add(generateFill());
		} else {
			this.pattern.repeat(3);
		}
	}
	

	@Override
	public String toString() {
		return "Kick";
	}

}
