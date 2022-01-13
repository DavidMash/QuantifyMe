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
	
	private void generateFill() {
		double everyOtherChance = EVERY_OTHER_CHANCE / 2;
		boolean everyOther = false;
		int otherCount = 2;
		for (int i = 0; i < rhythm.size(); i++) {
			String rhythmElement = rhythm.get(i);
			try {
				int rhythmValue = Integer.parseInt(rhythmElement);
				if (everyOther && this.random.nextDouble() < everyOtherChance) {
					this.pattern.add("0");
				} else if (rhythmValue == 0) {
					this.pattern.addRest();
				} else if (random.nextDouble() < this.density + ((1 - this.density)/2)) {
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

	@Override
	public void setPatternOverChords() {
		if(this.random.nextDouble() >= FILL_CHANCE) {
			this.pattern.repeat(2);
			this.generateFill();
		} else {
			this.pattern.repeat(3);
		}
		this.pattern.cutTimeBy(chords.size());
	}
	

	@Override
	public String toString() {
		return "Hihat";
	}

}
