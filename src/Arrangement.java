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
	static final int SUB_BASS = 6;
	
	static final int TOTAL_VOICE_OPTIONS = 6;
	static final double SILENT_THRESHOLD = 0.3;
	static final double LEADER_THRESHOLD = 0.9;
	static final int MAX_VOICES = 7;
	static final int MAX_SECTIONS = 5;
	
	private List<Section> sections; //sections of voice patterns
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
	    this.key = new Key(random);
	    this.key.setMode(Key.MODE.MAJOR); //only major for now
	}
	   
	private void setVoices() throws IOException {
	    int numberOfVoices = random.nextInt(MAX_VOICES);
	    if (QuantifyMe.choose) {
    		boolean done = false;
    		while(!done) {
	    		System.out.print("Enter desired number of voices: ");
    			try {
		    		numberOfVoices = Integer.parseInt(QuantifyMe.keyboard.readLine());
		    		if(numberOfVoices > 0) {
		    			done = true;
		    		}
	    		} catch (NumberFormatException e) {
	    			System.out.println("Try again.");
	    		}
    		}
	    }
    	this.voices = new LinkedList<>();
	    for (int i = 0; i < numberOfVoices; i++) {
	    	int voiceSelection = random.nextInt(TOTAL_VOICE_OPTIONS);
	    	if(QuantifyMe.choose) {
	    		boolean done = false;
	    		while(!done) {
	    		System.out.print("Choose voice (0 - " + (TOTAL_VOICE_OPTIONS - 1) + "): ");
	    			try {
			    		voiceSelection = Integer.parseInt(QuantifyMe.keyboard.readLine());
			    		if (voiceSelection >= 0 && voiceSelection < TOTAL_VOICE_OPTIONS) {
			    			done = true;
			    		}
		    		} catch (NumberFormatException e) {
		    			System.out.println("Try again.");
		    		}
	    		}
	    	}
    		if(voiceSelection == KICK) {
		    	this.voices.add(new Kick());
	    	} else if(voiceSelection == SNARE) {
	    		this.voices.add(new Snare());
	    	} else if(voiceSelection == HIHAT) {
	    		this.voices.add(new Hihat());
	    	} else if(voiceSelection == CHORD) {
	    		this.voices.add(new Chord());
	    	} else if(voiceSelection == ARPEGGIO) {
	    		this.voices.add(new Arpeggio(this.random));
	    	} else if(voiceSelection == BASS) {
	    		this.voices.add(new Bass());
	    	} else {
	    		this.voices.add(new Chord());
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
	    	sections.add(new Section(this.voices, 1, this.random)); //TODO: make the section density change
	    }
	}
	
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < voices.size(); i++) {
			Pattern voicePattern = new Pattern(true);
			for (Section section : this.sections) {
				voicePattern.add(section.get(i).getPatternInKey(this.key));
			}
			voicePattern.cutTimeToMeasure();
			string.append(voicePattern.toString());
			string.append("%");
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
