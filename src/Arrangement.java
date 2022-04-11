import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Arrangement{
	static final int KICK = 0;
	static final int SNARE = 1;
	static final int HIHAT = 2;
	static final int CHORD = 3;
	static final int ARPEGGIO = 4;
	static final int BASS = 5;
	static final int MELODY = 6;
	
	static final int TOTAL_VOICE_OPTIONS = 7;
	static final double SILENT_THRESHOLD = 0.3;
	static final double LEADER_THRESHOLD = 0.9;
	static final int MAX_VOICES = 7;
	static final int MAX_SECTIONS = 5;
	static final double REPEAT_CHANCE = 0.5;
	private static final double BUILD_UP_CHANCE = 0.2;
	private static final double HALF_TIME_CHANCE = 0.3;
	private static final double REDO_CHORDS_CHANCE = 0.3;
	
	private List<List<Section>> sections; //sections of voice patterns
	private List<Voice> voices;
	private Random random;
	private Key key;
	
	public Arrangement(Random random) throws IOException {
		this.random = random;
		this.sections = new LinkedList<>();
		this.setKey();
		this.setVoices();
		this.setSections();
	}
	
	private void setKey() {
		System.out.println("Setting Key Signature");
		//this.key = new Key(0, Key.MODE.MAJOR, random);
	    this.key = new Key(random);
	}
	   
	private void setVoices() throws IOException {
	    int numberOfVoices = TOTAL_VOICE_OPTIONS;
    	this.voices = new LinkedList<>();
	    for (int i = 0; i < numberOfVoices; i++) {
    		if(i == KICK) {
		    	this.voices.add(new Kick(this.random));
	    	} else if(i == SNARE) {
	    		this.voices.add(new Snare(this.random));
	    	} else if(i == HIHAT) {
	    		this.voices.add(new Hihat(this.random));
	    	} else if(i == CHORD) {
	    		this.voices.add(new Chordal(this.random));
	    	} else if(i == ARPEGGIO) {
	    		this.voices.add(new Arpeggio(this.random));
	    	} else if(i == BASS) {
	    		this.voices.add(new Bass(this.random));
	    	} else if(i == MELODY) {
	    		this.voices.add(new Melody(this.random));
	    	} else {
	    		this.voices.add(new Chordal(this.random));
	    	}
	    }
	}
	
	private void setSections() throws IOException {
	    int numberOfSections = random.nextInt(MAX_VOICES);
	 
	    if (QuantifyMe.choose) {
    		boolean done = false;
    		while(!done) {
	    		System.out.print("Enter desired number of sections: ");
    			try {
		    		numberOfSections = Integer.parseInt(QuantifyMe.keyboard.readLine());
		    		if(numberOfSections > 0) {
		    			done = true;
		    		}
	    		} catch (NumberFormatException e) {
	    			System.out.println("Try again.");
	    		}
    		}
    	}
	    

	    for(int i = 0; i < numberOfSections; i++) {
		    Key currKey = this.key;
	    	List<Section> currMetaSection = new LinkedList<>(); 
	    	boolean redoChords = (this.random.nextDouble() < REDO_CHORDS_CHANCE);
		    if (i == 0) { //TODO: put this back to 0 to have intro section
		    	int form = 1;
	    		boolean halfTime = (this.random.nextDouble() < HALF_TIME_CHANCE);
			    if(form == 0) { //buildup and new section
			    	Section lastSection = new Section(this.voices, currKey, 1, 0, this.random, false, halfTime);
		    		currMetaSection.add(lastSection);
			    	for (int j = 0; j < currMetaSection.get(0).getVoices().size() - 1; j++) {
			    		currKey = lastSection.nextKey();
			    		lastSection = new Section(lastSection, currKey, 0, false, halfTime, false, false);
			    		lastSection.removeVoice(j);
			    		currMetaSection.add(0, lastSection);
			    		currKey = lastSection.nextKey();
			    	}
			    } else {
			    	Section lastSection = new Section(this.voices, currKey, 1, 0, this.random, false, halfTime);
		    		currKey = lastSection.nextKey();
		    		Section reducedSection = new Section(lastSection, currKey, 1, false, false, true, false);
		    		int voicesToRemove = ((this.random.nextInt(this.voices.size()) / 4)) + 1 * 3;
		    		for	(int j = 0; j < voicesToRemove; j++) {
			    		reducedSection.removeVoice(j);
		    		}
		    		currMetaSection.add(reducedSection);
		    		currMetaSection.add(lastSection);
		    		currKey = currMetaSection.get(1).nextKey();
			    }
		    } else if ( i == numberOfSections - 1) {
		    	boolean halfTime = (this.random.nextDouble() < HALF_TIME_CHANCE);
		    	List<Section> meta = this.sections.get(this.random.nextInt(this.sections.size()));
	    		Section lastSection = new Section(meta.get(meta.size() - 1), currKey, 0, false, halfTime, false, redoChords);
	    		currMetaSection.add(lastSection);
		    	for (int j = 0; j < currMetaSection.get(0).getVoices().size() - 1; j++) {
		    		currKey = lastSection.nextKey();
		    		lastSection = new Section(lastSection, currKey, 0, false, halfTime, false, redoChords);
		    		lastSection.removeVoice(j);
		    		currMetaSection.add(lastSection);
		    	}
	    		currKey = lastSection.nextKey();
		    } else {
			    int form = i;//this.random.nextInt(6); //make this look better
			    if(form == 0) { //buildup and new section
		    		boolean halfTime = (this.random.nextDouble() < HALF_TIME_CHANCE);
			    	List<Section> meta = this.sections.get(this.random.nextInt(this.sections.size()));
		    		Section lastSection = new Section(meta.get(meta.size() - 1), currKey, 1, false, halfTime, true, redoChords);
		    		currMetaSection.add(lastSection); 
		    		currKey = lastSection.nextKey();
		    		currMetaSection.add(0, new Section(lastSection, currKey, 1, true, true, true, false));
		    		currKey = currMetaSection.get(1).nextKey();
			    } else if (form == 1) { // remove high density voices
			    	List<Section> meta = this.sections.get(this.random.nextInt(this.sections.size()));
		    		Section lastSection = new Section(meta.get(meta.size() - 1), currKey, 1, false, false, true, redoChords);
		    		currKey = lastSection.nextKey();
		    		Section reducedSection = new Section(lastSection, currKey, 1, false, false, true, false);
		    		int voicesToRemove = (this.random.nextInt(this.voices.size()) / 4) * 3;
		    		for	(int j = 0; j < voicesToRemove; j++) {
			    		reducedSection.removeVoice(j);
		    		}
		    		currMetaSection.add(lastSection);
		    		currMetaSection.add(reducedSection);
		    		currKey = currMetaSection.get(1).nextKey();
			    } else if (form == 2) { // remove low density voices
			    	List<Section> meta = this.sections.get(this.random.nextInt(this.sections.size()));
		    		Section lastSection = new Section(meta.get(meta.size() - 1), currKey, 1, false, false, true, redoChords);
		    		currKey = lastSection.nextKey();
		    		Section reducedSection = new Section(lastSection, currKey, 1, false, false, true, false);
		    		int voicesToRemove = (this.random.nextInt(this.voices.size()) / 4) * 3;
		    		for	(int j = 0; j < voicesToRemove; j++) {
			    		reducedSection.removeVoice(this.voices.size() - 1 - j);
		    		}
		    		currMetaSection.add(lastSection);
		    		currMetaSection.add(reducedSection);
		    		currKey = currMetaSection.get(1).nextKey();
			    } else {
		    		boolean halfTime = (this.random.nextDouble() < HALF_TIME_CHANCE);
		    		Section section = new Section(this.voices, currKey, 1, 1, this.random, true, halfTime);
		    		currMetaSection.add(section); 
			    }
		    }
		    this.sections.add(currMetaSection);
	    }
	}
	
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < voices.size(); i++) {
			Pattern voicePattern = new Pattern(true);
			for(List<Section> metaSection : sections) {
				for (Section section : metaSection) {
					voicePattern.add(section.get(i));
				}
			}
			voicePattern.cutTimeToMeasure();
			string.append(voicePattern.toString());
			string.append("%");
		}
		string.append("\nSection Details:");
		for (List<Section> section : this.sections) {
			string.append(section.get(section.size() - 1).toString() + "\n------------------------------------------------");
		}
    	
		return string.toString();
	}

	public List<Voice> getVoices() {
		return voices;
	}

	public Key getKey() {
		return this.key;
	}
}
