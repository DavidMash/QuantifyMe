import java.util.Random;

public class Hihat extends UnpitchedVoice{
	static final double EVERY_OTHER_CHANCE = 0.9;
	static final double FILL_CHANCE = 0.7;

	public Hihat(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
		super(key, chords, rhythm, density, isLead, random);
	}

	@Override
	public void generatePattern() {
		boolean everyOther = false;
		int otherCount = 2;
		for (int i = 0; i < rhythm.size(); i++) {
			String rhythmElement = rhythm.get(i);
			try {
				int rhythmValue = Integer.parseInt(rhythmElement);
				if (everyOther && this.random.nextDouble() < EVERY_OTHER_CHANCE) {
					this.pattern.add("0");
				} else if (rhythmValue == 0) {
					this.pattern.addRest();
				} else if (random.nextDouble() < this.density) {
					this.pattern.add("0");
				} else {
					this.pattern.addRest();
				}
			} catch (NumberFormatException e) {
				this.pattern.add(rhythmElement);
			}
			everyOther = false;
			otherCount--;
			if(rhythmElement.equals("[") || otherCount <= 0) {
				everyOther = true;
				otherCount = 2;
			}
		}
	}
	
	private Pattern generateFill() {
		Pattern fill = new Pattern(this.random);
		double everyOtherChance = EVERY_OTHER_CHANCE / 2;
		boolean everyOther = false;
		int otherCount = 2;
		for (int i = 0; i < rhythm.size(); i++) {
			String rhythmElement = rhythm.get(i);
			try {
				int rhythmValue = Integer.parseInt(rhythmElement);
				if (everyOther && this.random.nextDouble() < everyOtherChance) {
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
			everyOther = false;
			otherCount--;
			if(rhythmElement.equals("[") || otherCount <= 0) {
				everyOther = true;
				otherCount = 2;
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
		return "Hihat";
	}

}
