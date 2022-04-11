import java.util.Random;

public class Rhythm extends Pattern{
	protected static final double UNCOMMON_TIME_CHANCE = 0.0; // this spuriously breaks the physical repeat for some reason when it is not 0
	protected static final double OFFBEAT_CHANCE = 0.5;
	protected static final double SUBDIVISION_CHANCE = 0.2;
	protected static final double DOWNBEAT_CHANCE = 0.6;
	protected static final double TRIPLET_CHANCE = 0.2;
	protected static final double SWITCH_DUPLE_CHANCE = 0.0; //this spuriously breaks the pedal function when higher than 0
	protected static final int COMMON_TIME = 4;
	
	protected Rhythm quantizedUp;
	protected Rhythm quantizedDown;
	protected int beats;
	protected int highestSubdivision;
	protected int lowestSubdivision;
	protected boolean triplet;
	protected Random random;
	
	public Rhythm(Random random){
		super();
		this.random = random;
		this.highestSubdivision = 0;
		this.lowestSubdivision = Integer.MAX_VALUE;
		this.rerollChances();
		this.generateRhythm();
	}
	
	public Rhythm(Rhythm other) {
		super(other);
		this.random = other.random;
		this.quantizedUp = other.quantizedUp;
		this.beats = other.beats;
		this.triplet = other.triplet;
	}
	
	public void rerollChances() {
		this.triplet = (this.random.nextDouble() >= TRIPLET_CHANCE);
		this.beats = (this.random.nextDouble() < UNCOMMON_TIME_CHANCE)? COMMON_TIME + (this.random.nextInt(6) - 3): COMMON_TIME;
	}
	
	private void generateRhythm() {
		this.reset();
		for (int i = 0; i < beats; i++) {
			this.openBracket(); //start of beat
			double chance = DOWNBEAT_CHANCE;
			int subdivisions;
			if (this.random.nextDouble() < SWITCH_DUPLE_CHANCE) {
				subdivisions = ((!triplet)? 3 : 2) * 4;
			} else {
				subdivisions = ((triplet)? 3 : 2) * 4;
			}
			boolean subdivision = false;
			for (int j = 0; j < subdivisions; j++) {
				if (subdivision) {
					chance = SUBDIVISION_CHANCE;
				} else if (j > 0) {
					chance = OFFBEAT_CHANCE;
				}
				if (random.nextDouble() < chance) {
					if (subdivision) {
						this.remove(this.size() - 1);
						this.add("1");
					}
					this.add("1");
				} else {
					this.add("0");
				}
				subdivision = !subdivision;
			}
			this.closeBracket();
		}
	}
}
