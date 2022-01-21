import java.util.List;

public class VoicedRhythm extends Rhythm{
	public static int FILL = 0;
	public static int DOWNBEAT = 1;
	public static int EVERY_BEAT = 2;
	public static int EVERY_OTHER_BEAT = 3;
	public static int EVERY_SECOND_BEAT = 4;
	public static int EVERY_NOTE = 5;
	public static int EVERY_OTHER_NOTE = 6;
	public static int OFFBEAT = 7;
	private double[] chances;
	private boolean[] active;
	private int[] counter;
	private boolean resetChanceOnBeat;
	private int density;
	private Rhythm base;
	private Voice voice;
	
	public VoicedRhythm(Voice voice, Rhythm other, double density, boolean resetChanceOnBeat) {
		super(other.getQuantizedUp());
		this.base = other;
		this.voice = voice;
		this.resetChanceOnBeat = resetChanceOnBeat;
		if (this.voice.getChances().length < 8) { 
			throw new IndexOutOfBoundsException ("The \"chances\" array given to VocedRhythm did not have enough!");
		}
		this.chances = this.voice.getChances();
		this.generateVoicedRhythm();
	}
	
	public Pattern getOverChords(List<Integer> chords) {
		Pattern resultPattern = (new Pattern(this)).transpose(chords.get(0));;
		Pattern thisPattern = new Pattern(this);
		if(this.random.nextDouble() <= chances[FILL]) {
			for(int i = 1; i < chords.size() - 1; i++) {
				resultPattern.add(thisPattern.transpose(chords.get(i)), false);
			}
			resultPattern.add(this.generateVoicedFill().transpose(chords.get((chords.size() - 1))), false);
		} else {
			for(int i = 1; i < chords.size(); i++) {
				resultPattern.add(thisPattern.transpose(chords.get(i)), false);
			}
		}
		return resultPattern;
	}
	
	public Pattern getOverChordsNoTransposing(List<Integer> chords) {
		Pattern thisPattern = new Pattern(this);
		if(this.random.nextDouble() <= chances[FILL]) {
			thisPattern.repeat(chords.size() - 2);
			thisPattern.add(this.generateVoicedFill());
		} else {
			thisPattern.repeat(chords.size() - 1);
		}
		return thisPattern;
	}
	
	public void generateVoicedRhythm() {
		Pattern newPattern = new Pattern();
		this.active = new boolean[]{false, false, false, false, false, false, false, false};
		this.counter = new int[]{0, 0, 1, 0, 0, 0, 1, 0};
		this.active[DOWNBEAT] = true;
		for (int i = 0; i < base.size(); i++) {
			String rhythmElement = base.get(i);
			if (rhythmElement.equals("]")) {
				newPattern.add(rhythmElement);
				continue;
			}
			if(rhythmElement.equals("[")) {
				newPattern.add(rhythmElement);
				if (this.resetChanceOnBeat) {
					this.counter[OFFBEAT] = 0;
					this.active[OFFBEAT] = false;
					this.counter[EVERY_OTHER_NOTE] = 1;
					this.active[EVERY_OTHER_NOTE] = true;
				}
				this.active[EVERY_BEAT] = true;
				if(this.counter[EVERY_OTHER_BEAT] >= 1) {
					this.active[EVERY_OTHER_BEAT] = true;
					this.counter[EVERY_OTHER_BEAT] = 0;
				} else {
					this.active[EVERY_OTHER_BEAT] = false;
					this.counter[EVERY_OTHER_BEAT]++;
				}
				if(this.counter[EVERY_SECOND_BEAT] >= 1) {
					this.active[EVERY_SECOND_BEAT] = true;
					this.counter[EVERY_SECOND_BEAT] = 0;
				} else {
					this.active[EVERY_SECOND_BEAT] = false;
					this.counter[EVERY_SECOND_BEAT]++;
				}
			} else {
				if (this.checkChances()) {
					newPattern.add(this.voice.next());
				} else if (rhythmElement.equals("0")) {
					newPattern.addRest();
				} else if (random.nextDouble() < this.density) {
					newPattern.add(this.voice.next());
				} else {
					newPattern.addRest();
				}
				if(this.counter[EVERY_OTHER_NOTE] >= 1) {
					this.active[EVERY_OTHER_NOTE] = true;
					this.counter[EVERY_OTHER_NOTE] = 0;
				} else {
					this.active[EVERY_OTHER_NOTE] = false;
					this.counter[EVERY_OTHER_NOTE]++;
				}
				this.active[EVERY_NOTE] = true;
				if(this.counter[OFFBEAT] >= (this.highestSubdivision / ((this.triplet)? 3: 2))) {
					this.active[OFFBEAT] = true;
					this.counter[OFFBEAT] = 0;
				} else {
					this.active[OFFBEAT] = false;
					this.counter[OFFBEAT]++;
				}
				this.active[DOWNBEAT] = false;
				this.active[EVERY_BEAT] = false;
				this.active[EVERY_OTHER_BEAT] = false;
				this.active[EVERY_SECOND_BEAT] = false;
			}
		}
		System.out.println("Generated Pattern: " + newPattern);
		this.pattern = newPattern.pattern;
	}
	
	public Pattern generateVoicedFill() {
		Pattern fill = new Pattern();
		this.active = new boolean[]{this.active[FILL], true, false, false, true, false, false, false};
		this.counter = new int[]{this.counter[FILL], 0, 1, 0, 0, 0, 1, 0};
		this.active[DOWNBEAT] = true;
		for (int i = 0; i < base.size(); i++) {
			String rhythmElement = base.get(i);
			if (rhythmElement.equals("]")) {
				fill.add(rhythmElement);
				continue;
			}
			if(rhythmElement.equals("[")) {
				fill.add(rhythmElement);
				if (this.resetChanceOnBeat) {
					this.counter[OFFBEAT] = 0;
					this.active[OFFBEAT] = false;
					this.counter[EVERY_OTHER_NOTE] = 1;
					this.active[EVERY_OTHER_NOTE] = true;
				}
				this.active[EVERY_BEAT] = true;
				if(this.counter[EVERY_OTHER_BEAT] >= 1) {
					this.active[EVERY_OTHER_BEAT] = true;
					this.counter[EVERY_OTHER_BEAT] = 0;
				} else {
					this.active[EVERY_OTHER_BEAT] = false;
					this.counter[EVERY_OTHER_BEAT]++;
				}
				if(this.counter[EVERY_SECOND_BEAT] >= 1) {
					this.active[EVERY_SECOND_BEAT] = true;
					System.out.println(this.active[EVERY_SECOND_BEAT]);
					this.counter[EVERY_SECOND_BEAT] = 0;
				} else {
					this.active[EVERY_SECOND_BEAT] = false;
					System.out.println(this.active[EVERY_SECOND_BEAT]);
					this.counter[EVERY_SECOND_BEAT]++;
				}
				this.active[EVERY_BEAT] = true;
			} else {
				if (this.checkChances() && this.checkChances()) {
					fill.add(this.voice.next());
				} else if (rhythmElement.equals("0")) {
					fill.addRest();
				} else if (random.nextDouble() < this.density + ((1 - this.density) / 2)) {
					fill.add(this.voice.next());
				} else {
					fill.addRest();
				}
				if(this.counter[EVERY_OTHER_NOTE] >= 1) {
					this.active[EVERY_OTHER_NOTE] = true;
					this.counter[EVERY_OTHER_NOTE] = 0;
				} else {
					this.active[EVERY_OTHER_NOTE] = false;
					this.counter[EVERY_OTHER_NOTE]++;
				}
				this.active[EVERY_NOTE] = true;
				if(this.counter[OFFBEAT] >= (this.highestSubdivision / ((this.triplet)?3:2))) {
					this.active[OFFBEAT] = true;
					this.counter[OFFBEAT] = 0;
				} else {
					this.active[OFFBEAT] = false;
					this.counter[OFFBEAT]++;
				}
				this.active[DOWNBEAT] = false;
				this.active[EVERY_BEAT] = false;
				this.active[EVERY_OTHER_BEAT] = false;
				this.active[EVERY_SECOND_BEAT] = false;
			}
		}
		return fill;
	}
	
	private boolean checkChances() {
		if(active[DOWNBEAT] && random.nextDouble() < chances[DOWNBEAT]) {
			return true;
		}
		if(active[EVERY_BEAT] && random.nextDouble() < chances[EVERY_BEAT]) {
			return true;
		}
		if(active[EVERY_OTHER_BEAT] && random.nextDouble() < chances[EVERY_OTHER_BEAT]) {
			return true;
		}
		if(active[EVERY_SECOND_BEAT] && random.nextDouble() < chances[EVERY_SECOND_BEAT]) {
			return true;
		}
		if(active[EVERY_NOTE] && random.nextDouble() < chances[EVERY_NOTE]) {
			return true;
		}
		if(active[EVERY_OTHER_NOTE] && random.nextDouble() < chances[EVERY_OTHER_NOTE]) {
			return true;
		}
		if(active[OFFBEAT] && random.nextDouble() < chances[OFFBEAT]) {
			return true;
		}
		return false;
	}
}
