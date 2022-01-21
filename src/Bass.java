public class Bass extends PitchedVoice{
	private static double[] chances;
	
	public Bass() {
		super();
		this.voiceName = "Bass";
	}
	
	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[8];
			chances[VoicedRhythm.FILL] = 0.4;
			chances[VoicedRhythm.DOWNBEAT] = 0.8;
			chances[VoicedRhythm.EVERY_BEAT] = 0.4;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0.5;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0;
			chances[VoicedRhythm.EVERY_NOTE] = 0;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0;
			chances[VoicedRhythm.OFFBEAT] = 0.6;
		}
		return chances;
	}


	@Override
	public String next() {
		return "-24";
	}
}
