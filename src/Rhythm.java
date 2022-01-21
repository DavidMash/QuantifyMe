import java.util.Random;

public class Rhythm extends Pattern{
	protected static final int BEAT_DICE_SIDES = 20;
	protected static final int ODD_BEAT_THRESHOLD = 20;
	protected static final int DOWNBEAT_DICE_SIDES = 4;
	protected static final int DOWNBEAT_THRESHOLD = 1;
	protected static final int OFFBEAT_DICE_SIDES = 4;
	protected static final int OFFBEAT_THRESHOLD = 3;
	protected static final int SUBDIVISION_DICE_SIDES = 10;
	protected static final int TRIPLET_THRESHOLD = 9;
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
		this.generateRhythm();
	}
	
	public Rhythm(Rhythm other) {
		super(other);
		this.random = other.random;
		this.quantizedUp = other.quantizedUp;
		this.beats = other.beats;
		this.highestSubdivision = other.highestSubdivision;
		this.lowestSubdivision = other.lowestSubdivision;
		this.triplet = other.triplet;
	}
	
	private Rhythm(Pattern otherPattern, int beats, int highestSubdivision, int lowestSubdivision, boolean triplet, Random random) {
		super(otherPattern);
		this.beats = beats;
		this.highestSubdivision = highestSubdivision;
		this.lowestSubdivision = lowestSubdivision;
		this.triplet = triplet;
		this.random = random;
	}
	
	private void generateRhythm() {
		this.reset();
		this.triplet = (this.random.nextInt(SUBDIVISION_DICE_SIDES) >= TRIPLET_THRESHOLD);
		this.beats = (this.random.nextInt(BEAT_DICE_SIDES) >= ODD_BEAT_THRESHOLD)? COMMON_TIME + (this.random.nextInt(6) - 3): COMMON_TIME;
		for (int i = 0; i < beats; i++) {
			this.openBracket(); //start of beat
			int dice = DOWNBEAT_DICE_SIDES;
			int threshold = DOWNBEAT_THRESHOLD;
			int subdivisions = ((triplet)? 3 : 2) * (random.nextInt(2) + 1);
			if (subdivisions > highestSubdivision) {
				this.highestSubdivision = subdivisions;
			}
			if (subdivisions < lowestSubdivision) {
				this.lowestSubdivision = subdivisions;
			}
			for (int j = 0; j < subdivisions; j++) {
				if (j > 0) {
					dice = OFFBEAT_DICE_SIDES;
					threshold = OFFBEAT_THRESHOLD;
				}
				if (random.nextInt(dice) >= threshold) {
					this.add("1");
				} else {
					this.add("0");
				}
			}
			this.closeBracket();
		}
	}
	
	public Rhythm getQuantizedUp() {
		if (this.quantizedUp != null) {
			return this.quantizedUp;
		}
		Pattern quantizedPattern = new Pattern(this);

		int index = 0;
		for (int beat = 0; beat < beats; beat++) {
			int beatIndex = index;
			if (index < quantizedPattern.size() && quantizedPattern.get(index).equals("[")) {
				index++;
				beat--;
				continue;
			}
			 
			int missingSubdivisions = 0;
			int presentSubdivisions = 0;
			for (int subdivision = 0; subdivision < highestSubdivision; subdivision++) {
				System.out.println(index+" "+quantizedPattern.size()+" "+quantizedPattern.get(index));
				if (index > quantizedPattern.size() || quantizedPattern.get(index).equals("]")) {
					missingSubdivisions++;
				} else {
					presentSubdivisions++;
					index++;
				}
			}
			if (missingSubdivisions > 0) {
				int distribution = missingSubdivisions / presentSubdivisions;
				int subdivisionAdded = 0;
				for (int subdivisionIndex = beatIndex; subdivisionIndex < index; subdivisionIndex++) {
					for(int added = 0; added < distribution; added++) {
						quantizedPattern.add(subdivisionIndex + (distribution * subdivisionAdded) + 1, "0");
					}
					subdivisionAdded++;
				}
				index += missingSubdivisions;
			}
			index++;
		}
		
		System.out.println("Original Rhythm: " + this);
		this.quantizedUp = new Rhythm(quantizedPattern, this.beats, this.highestSubdivision, this.highestSubdivision, this.triplet, this.random);
		System.out.println("Quantized Rhythm: " + this.quantizedUp);
		return quantizedUp;
	}
	
	public Rhythm getQuantizedDown() {
		if (this.quantizedDown != null) {
			return this.quantizedDown;
		}
		Pattern quantizedPattern = new Pattern(this);

		int index = 0;
		for (int beat = 0; beat < beats; beat++) {
			int beatIndex = index;
			if (index < quantizedPattern.size() && quantizedPattern.get(index).equals("[")) {
				index++;
				beat--;
				continue;
			}
			System.out.println("Beat: "+beat);
			
			int presentSubdivisions = 0;
			for (int subdivision = 0; subdivision < highestSubdivision; subdivision++) {
				if (index > quantizedPattern.size() || quantizedPattern.get(index).equals("]")) {
					break;
				} else {
					presentSubdivisions++;
					index++;
				}
			}
			int excessSubdivisions = presentSubdivisions - this.lowestSubdivision;
			System.out.println("Excess: "+excessSubdivisions);
			if (excessSubdivisions > 0) {
				System.out.println("Original: "+this);
				int distribution = excessSubdivisions / this.lowestSubdivision;
				System.out.println("Distribution: "+distribution);
				for (int subdivisionIndex = beatIndex; subdivisionIndex < index; subdivisionIndex++) {
					System.out.println("Subdivision Index: "+subdivisionIndex);
					for(int removed = 0; removed < distribution; removed++) {
						System.out.println("Removing at: "+(subdivisionIndex + distribution - removed));
						quantizedPattern.remove(subdivisionIndex + distribution - removed);
						index--;
						System.out.println("Current: "+quantizedPattern);
					}
				}
			}
			index++;
		}
		
		this.quantizedDown = new Rhythm(quantizedPattern, this.beats, this.lowestSubdivision, this.lowestSubdivision, this.triplet, this.random);
		
		System.out.println("Original: "+this);
		System.out.println("Quantized: "+this.quantizedDown);
		
		return quantizedDown;
	}
}
