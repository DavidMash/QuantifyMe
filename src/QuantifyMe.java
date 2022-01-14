import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuantifyMe {
	static final int KICK = 0;
	static final int SNARE = 1;
	static final int HIHAT = 2;
	static final int CHORD = 3;
	static final int ARPEGGIO = 4;
	static final int BASS = 5;
	static final int SUB_BASS = 6;
	
	
	static final int TOTAL_VOICE_OPTIONS = 5;
	static final double SILENT_THRESHOLD = 0.3;
	static final double LEADER_THRESHOLD = 0.9;
	static final int MAX_VOICES = 7;
	static final int MAX_SECTIONS = 5;
	static final Random random = new Random(); 

	private static boolean debug = false;
	private static boolean choose = false;
	
	public static void main(String[] args) throws IOException {
		if(args.length > 0 && args[0].equals("debug")) { 
			debug = true;
			System.out.println("Starting in debug mode...");
		}
		
		if(args.length > 1 && args[1].equals("choose")) {
			choose = true;
			System.out.println("with choosing on...");
		}
		
		BufferedReader keyboard = new BufferedReader (new InputStreamReader(System.in));
		
	    System.out.println("Setting up output file...");
	    
		File file = new File("generatedPattern.txt");
	    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	    
	    System.out.println("Generating chords, rhythms, and voices...");
	    
	    Key key = new Key(random);
	    key.setMode(Key.MODE.MAJOR);
	    
	    Pattern chords = generateChords();
	    
	    int numberOfVoices = random.nextInt(MAX_VOICES);
	    int numberOfSections = random.nextInt(MAX_VOICES);
	    
	    if(choose) {
    		boolean done = false;
    		while(!done) {
	    		System.out.print("Enter desired number of voices: ");
    			try {
		    		numberOfVoices = Integer.parseInt(keyboard.readLine());
		    		if(numberOfVoices > 0) {
		    			done = true;
		    		}
	    		} catch (NumberFormatException e) {
	    			System.out.println("Try again.");
	    		}
    		}
    		done = false;
    		while(!done) {
	    		System.out.print("Enter desired number of sections: ");
    			try {
		    		numberOfSections = Integer.parseInt(keyboard.readLine());
		    		if(numberOfSections > 0) {
		    			done = true;
		    		}
	    		} catch (NumberFormatException e) {
	    			System.out.println("Try again.");
	    		}
    		}
    	}
	    
	    Voice[] voices = new Voice[numberOfVoices];
	    
	    int numberOfRhythms = numberOfSections;
	    List<Pattern> rhythms = new ArrayList<>(numberOfRhythms);
	    for(int i = 0; i < numberOfRhythms; i++) {
	    	Pattern rhythm = new Pattern(random);
	    	rhythm.generateRhythm();
	    	if(debug) System.out.println("Generated Rhythm: "+rhythm);
	    	rhythms.add(rhythm);
	    }
	    
	    System.out.println("Generating patterns...");
	    
	    Double[][] densities = new Double[numberOfSections][numberOfVoices];
	    for(int sectionIndex = 0; sectionIndex < numberOfSections; sectionIndex++) {
		    for(int i = 0; i < numberOfVoices; i++) {
		    	int voiceIndex = random.nextInt(numberOfVoices);
		    	while(densities[sectionIndex][voiceIndex] != null) {
		    		voiceIndex = (voiceIndex + 1) % numberOfVoices;
		    	}
		    	densities[sectionIndex][voiceIndex] = (Double) (i / ((double) numberOfVoices));
		    	//densities[voiceIndex] = densities[voiceIndex] + ((1 - densities[voiceIndex]) / 2); // skew up
		    }
	    }
	    
	    for(int i = 0; i < numberOfVoices; i++) {
	    	int voiceSelection = random.nextInt(TOTAL_VOICE_OPTIONS);
	    	
	    	if(choose) {
	    		boolean done = false;
	    		while(!done) {
	    		System.out.print("Choose voice (0 - " + (TOTAL_VOICE_OPTIONS - 1) + "): ");
	    			try {
			    		voiceSelection = Integer.parseInt(keyboard.readLine());
			    		if (voiceSelection >= 0 && voiceSelection < TOTAL_VOICE_OPTIONS) {
			    			done = true;
			    		}
		    		} catch (NumberFormatException e) {
		    			System.out.println("Try again.");
		    		}
	    		}
	    	}
	    	
	    	Pattern voicePattern = new Pattern();
	    	for(int section = 0; section < numberOfSections; section++) {
		    	double density = densities[section][i];
		    	
		    	boolean isLead = density >= LEADER_THRESHOLD;
		    	System.out.println("Creating voice...");
		    	
		    	if(voiceSelection == KICK) {
			    	voices[i] = new Kick(key, chords, rhythms.get(section), density, isLead, random);
		    	} else if(voiceSelection == SNARE) {
			    	voices[i] = new Snare(key, chords, rhythms.get(section), density, isLead, random);
		    	} else if(voiceSelection == HIHAT) {
			    	voices[i] = new Hihat(key, chords, rhythms.get(section), density, isLead, random);
		    	} else if(voiceSelection == CHORD) {
			    	voices[i] = new Chord(key, chords, rhythms.get(section), density, isLead, random);
		    	} else if(voiceSelection == ARPEGGIO) {
			    	voices[i] = new Arpeggio(key, chords, rhythms.get(section), density, isLead, random);
		    	} else if(voiceSelection == BASS) {
			    	voices[i] = new Arpeggio(key, chords, rhythms.get(section), density, isLead, random);
		    	} else {
		    		voices[i] = new Chord(key, chords, rhythms.get(section), density, isLead, random);
		    	}

		    	voices[i].generatePattern();
		    	voices[i].setPatternOverChords();
		    	Pattern pattern = voices[i].getPatternInKey();
		    	//pattern.repeat(3);
		    	voicePattern.add(pattern);
	    	}
	    	voicePattern.cutTimeToMeasure();
		    writer.append(voicePattern.toString());
		    writer.append("%");
	    }
	    
	    System.out.println("Adding note of voice order in file...");
	    
	    writer.append("\nVoices in order:");
	    for (int i = 0; i < numberOfVoices; i++) {
	    	writer.append("\n"+i+") "+voices[i]);
	    	if (debug) {
	    		writer.append(" (with density: " + voices[i].getDensity() + ")");
	    	}
	    }
	    writer.append("\nin the key of "+key+".");
	    
	    System.out.println("Saving to File: "+file.getAbsolutePath());
	    writer.close();
	    System.out.println("Done!");
	}
	
	private static Pattern generateChords(){
		String[] roots = {"0"};
		String[] predominants = {"1", "2", "3", "5"};
		String[] dominants = {"4", "6"};
		
		Pattern prog = new Pattern(random);
		prog.add(roots[random.nextInt(roots.length)]);
		prog.add(predominants[random.nextInt(predominants.length)]);
		prog.add(predominants[random.nextInt(predominants.length)]);
		prog.add(dominants[random.nextInt(dominants.length)]);		
		return prog;
	}
}
