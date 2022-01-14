import java.util.Random;

public class Arpeggio extends PitchedVoice{
	static final double EVERY_CHANCE = 0.50;
	static final double FILL_CHANCE = 0.7;
	
	private static int[][] arps = {{0, 2, 4},
								{0, 2, 4, 6},
								{0, 2, 4, 6, 7},
								{0, 2, 4, 7},
								{0, 2, 4, 2},
								{0, 2, 4, 6, 4, 2},
								{0, 2, 4, 6, 7, 6, 4, 2},
								{0, 2, 4, 7, 4, 2},
								{0, 2, 4, 2, 4, 6, 4, 6}
	};
	
	private int[] arp;
	private int arpStep;

	public Arpeggio(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
		super(key, chords, rhythm, density, isLead, random);
		this.arp = arps[this.random.nextInt(arps.length)];
		arpStep = 0;
	}

	@Override
	public void generatePattern() {
		boolean downbeat = false;
		for (int i = 0; i < rhythm.size(); i++) {
			String rhythmElement = rhythm.get(i);
			try {
				int rhythmValue = Integer.parseInt(rhythmElement);
				if (downbeat && this.random.nextDouble() < EVERY_CHANCE) {
					this.pattern.add(this.next());
				} else if (rhythmValue == 0) {
					this.pattern.addRest();
				} else if (random.nextDouble() < this.density + ((1 - this.density)/2)) {
					this.pattern.add(this.next());
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
		double downbeatChance = EVERY_CHANCE / 2;
		boolean downbeat = false;
		for (int i = 0; i < rhythm.size(); i++) {
			String rhythmElement = rhythm.get(i);
			try {
				int rhythmValue = Integer.parseInt(rhythmElement);
				if (downbeat && this.random.nextDouble() < downbeatChance) {
					fill.add(this.next());
				} else if (rhythmValue == 0) {
					fill.addRest();
				} else if (random.nextDouble() < this.density + ((1 - this.density)/2)) {
					fill.add(this.next());
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
			this.pattern = thisPattern.transpose(this.chords.get(0));
			for(int i = 1; i < chords.size() - 1; i++) {
				this.pattern.add(thisPattern.transpose(chords.get(i)), false);
			}
			this.pattern.add(this.generateFill().transpose(chords.get(chords.size() - 1)), false);
		} else {
			this.pattern = thisPattern.transpose(this.chords.get(0));
			for(int i = 1; i < chords.size(); i++) {
				this.pattern.add(thisPattern.transpose(chords.get(i)), false);
			}
		}
	}
	
	private int next() {
		int currStep = arpStep;
		arpStep = (arpStep + 1) % arp.length;
		return arp[currStep];
	}

	@Override
	public String toString() {
		return "Arpeggio";
	}

}
