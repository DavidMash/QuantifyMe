import java.util.Random;

public class Snare extends UnpitchedVoice{
	private static final double LOWER_FIFTH_CHANCE = 0.2;
	private static double[] chances;
	
	public Snare(Random random) {
		super(random);
		this.voiceName = "Snare";
	}

	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[11];
			chances[VoicedRhythm.FILL] = 0.7;
			chances[VoicedRhythm.DOWNBEAT] = 0;
			chances[VoicedRhythm.EVERY_BEAT] = 0;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0.8;
			chances[VoicedRhythm.EVERY_NOTE] = 0;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0;
			chances[VoicedRhythm.OFFBEAT] = 0;
			chances[VoicedRhythm.PEDAL] = 0;
			chances[VoicedRhythm.P_STOP_BEAT] = 0;
			chances[VoicedRhythm.P_STOP_HALF] = 0;
		}
		return chances;
	}


	@Override
	public String next() {
		return (this.random.nextDouble() < LOWER_FIFTH_CHANCE)? "-3": "0";
	}

	@Override
	protected Voice copy() {
		return new Snare(this.random);
	}
}
