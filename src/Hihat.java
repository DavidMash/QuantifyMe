public class Hihat extends UnpitchedVoice{
	private static double[] chances;
	
	public Hihat() {
		super();
		this.voiceName = "Hihat";
	}

	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[8];
			chances[VoicedRhythm.FILL] = 0;
			chances[VoicedRhythm.DOWNBEAT] = 0;
			chances[VoicedRhythm.EVERY_BEAT] = 0;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0;
			chances[VoicedRhythm.EVERY_NOTE] = 0;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0;
			chances[VoicedRhythm.OFFBEAT] = 1;
		}
		return chances;
	}


	@Override
	public String next() {
		return "0";
	}
}
