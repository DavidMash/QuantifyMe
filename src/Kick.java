import java.util.Random;

public class Kick extends UnpitchedVoice{
	private static double[] chances;
	
	public Kick(Random random) {
		super(random);
		this.voiceName = "Kick";
	}

	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[11];
			chances[VoicedRhythm.FILL] = 0.6;
			chances[VoicedRhythm.DOWNBEAT] = 0.6;
			chances[VoicedRhythm.EVERY_BEAT] = 0.5;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0.4;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0;
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
		return "0";
	}

	@Override
	protected Voice copy() {
		return new Kick(this.random);
	}
}
