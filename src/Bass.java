import java.util.Random;

public class Bass extends PitchedVoice{
	private static double[] chances;
	
	public Bass(Random random) {
		super(random);
		this.voiceName = "Bass";
	}
	
	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[11];
			chances[VoicedRhythm.FILL] = 0.4;
			chances[VoicedRhythm.DOWNBEAT] = 0.8;
			chances[VoicedRhythm.EVERY_BEAT] = 0.5;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0.5;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0;
			chances[VoicedRhythm.EVERY_NOTE] = 0;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0;
			chances[VoicedRhythm.OFFBEAT] = 0.4;
			chances[VoicedRhythm.PEDAL] = 0.3;
			chances[VoicedRhythm.P_STOP_BEAT] = 0.4;
			chances[VoicedRhythm.P_STOP_HALF] = 0.4;
		}
		return chances;
	}


	@Override
	public String next() {
		return "-14";
	}

	@Override
	protected Voice copy() {
		return new Bass(this.random);
	}
}
