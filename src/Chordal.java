public class Chordal extends PitchedVoice{
	private static double[] chances;
	
	public Chordal() {
		super();
		this.voiceName = "Chord";
	}

	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[8];
			chances[VoicedRhythm.FILL] = 0.8;
			chances[VoicedRhythm.DOWNBEAT] = 0.5;
			chances[VoicedRhythm.EVERY_BEAT] = 0.3;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0.5;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0.1;
			chances[VoicedRhythm.EVERY_NOTE] = 0;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0;
			chances[VoicedRhythm.OFFBEAT] = 0.4;
		}
		return chances;
	}


	@Override
	public String next() {
		return "[ 0 , 2 , 4 ]";
	}
}
