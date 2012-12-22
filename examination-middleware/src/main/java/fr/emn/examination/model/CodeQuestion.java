package fr.emn.examination.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CodeQuestion extends QuestionStudent implements Serializable {

    /**
     * 
     */
    private static final long       serialVersionUID = -1760344569100720715L;

    private HashMap<String, String> segments;

    private ArrayList<String>       displayedCode, segmentIds;

    private JavaCode                javaCode;

    public CodeQuestion(JavaCode javaCode, String id,
	    ArrayList<String> displayedCode, ArrayList<String> segmentIds,
	    HashMap<String, String> segments) {
	this.segments = segments;
	this.displayedCode = displayedCode;
	this.segmentIds = segmentIds;
	this.javaCode = javaCode;
	this.id = id;
    }

    public HashMap<String, String> getSegments() {
	return segments;
    }

    public ArrayList<String> getDisplayedCode() {
	return displayedCode;
    }

    public ArrayList<String> getSegmentIds() {
	return segmentIds;
    }

    public JavaCode getJavaCode() {
	return javaCode;
    }

    @Override
    public String toString() {
	return id + "\n" + displayedCode + "\n" + segmentIds + "\n" + segments;
    }

    @Override
    public double evaluer() {
	return 20;
    }

}