import java.util.Random;

public class Hihat extends UnpitchedVoice{
	private static final double ROLL_CHANCE = 0.2;
	
	private static double[] chances;
	
	public Hihat(Random random) {
		super(random);
		this.random = random;
		this.voiceName = "Hihat";
	}

	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[11];
			chances[VoicedRhythm.FILL] = 0;
			chances[VoicedRhythm.DOWNBEAT] = 0;
			chances[VoicedRhythm.EVERY_BEAT] = 0.3;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0;
			chances[VoicedRhythm.EVERY_NOTE] = 0.1;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0;
			chances[VoicedRhythm.OFFBEAT] = 0.7;
			chances[VoicedRhythm.PEDAL] = 0;
			chances[VoicedRhythm.P_STOP_BEAT] = 0;
			chances[VoicedRhythm.P_STOP_HALF] = 0;
		}
		return chances;
	}


	@Override
	public String next() {
		int rollMultiplier = (this.random.nextInt(3) + 1) * (this.random.nextInt(1) + 1); 
		return (this.random.nextDouble() < ROLL_CHANCE)? "[ [ 0 ] !" + rollMultiplier + " ]": "0";
	}

	@Override
	protected Voice copy() {
		return new Hihat(this.random);
	}
}
