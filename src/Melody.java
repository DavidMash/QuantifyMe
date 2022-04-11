import java.util.List;
import java.util.Random;

public class Melody extends PitchedVoice{
	private static final double EXTENDED_MELODY_CHANCE = 0.4;
	private static final double HARMONY_CHANCE = 0.2;
	private static double[] chances;
	private boolean harmony;
	
	public Melody(Random random) {
		super(random);
		this.voiceName = "Melody";
		this.harmony = this.random.nextDouble() < HARMONY_CHANCE;
	}

	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[11];
			chances[VoicedRhythm.FILL] = 0.2;
			chances[VoicedRhythm.DOWNBEAT] = 0.5;
			chances[VoicedRhythm.EVERY_BEAT] = 0;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0.5;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0.1;
			chances[VoicedRhythm.EVERY_NOTE] = 0;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0;
			chances[VoicedRhythm.OFFBEAT] = 0.4;
			chances[VoicedRhythm.PEDAL] = 0.3;
			chances[VoicedRhythm.P_STOP_BEAT] = 0.3;
			chances[VoicedRhythm.P_STOP_HALF] = 0.3;
		}
		return chances;
	}
	
	private static final double LEAP_CHANCE = 0.3;
	private int prev = -1;
	@Override
	public String next() {
		if (this.prev == -1) {
			this.prev = this.random.nextInt(4) * 2;
		} else {
			if (this.random.nextDouble() < LEAP_CHANCE) {
				this.prev = this.random.nextInt(4) * 2 - (7 * this.random.nextInt(1)) + (7 * this.random.nextInt(1));
			} else {
				this.prev = this.prev + (this.random.nextInt(3) - 1);
			}
		}
		String returnValue = String.valueOf(prev);
		if (this.harmony) {
			returnValue = "[ " + returnValue + " , " + (prev + 2) + " ]";
		}
		return returnValue;
	}
	
	@Override
	public Pattern setOverChords(List<String> chords, Key key, Key nextKey, int switchPoint, boolean silenced) {
		Pattern p;
		if (this.random.nextDouble() < EXTENDED_MELODY_CHANCE) {
			p = this.vr.generateOverChords(chords, key, nextKey, switchPoint, silenced);
		} else {
			p = this.vr.getOverChords(chords, key, nextKey, switchPoint, silenced);
		}
		return p;
	}

	@Override
	protected Voice copy() {
		return new Melody(this.random);
	}
}
