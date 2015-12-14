package de.hft.gradingassistant.record;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Representation of a record for the analysis. A Record holds all information on one question-answer pair in a quiz
 * records are compared using their similarity score so they can be sorted for estimated quality of student answers
 * @author Cornelia Kiefer, Verena Meyer
 *
 */

public class Record implements Comparable<Record>{

	private HashMap<String, String> record = new HashMap<String, String>();
	private double max = 0.0;
	private int exchId = 0;
	
	private int min = 0;
	private int sec = 0;
	private int numAttempts = 0;
	
	//store the final score that will be used here
	private double score = 0.0;
	private double scoreInter = 0.0;
		
	//DKPro Processing Pipeline info	
	private ArrayList<String> tokenWordsAnswer = new ArrayList<String>();
	private ArrayList<String> tokenPosAnswer = new ArrayList<String>();
	private ArrayList<String> tokenLemmasAnswer = new ArrayList<String>();
	private ArrayList<String> tokenStemsAnswer = new ArrayList<String>();
	
	private ArrayList<String> tokenWordsQuestion = new ArrayList<String>(); 
	private ArrayList<String> tokenPosQuestion = new ArrayList<String>();
	private ArrayList<String> tokenLemmasQuestion = new ArrayList<String>();
	private ArrayList<String> tokenStemsQuestion = new ArrayList<String>();
	
	private ArrayList<String> tokenWordsReferenceAnswer = new ArrayList<String>(); 
	private ArrayList<String> tokenPosReferenceAnswer = new ArrayList<String>();
	private ArrayList<String> tokenLemmasReferenceAnswer = new ArrayList<String>();
	private ArrayList<String> tokenStemsReferenceAnswer = new ArrayList<String>();
	
   	//orig
	private ArrayList<String> tokenWordsAnswerOrig = new ArrayList<String>();
	private ArrayList<String> tokenPosAnswerOrig = new ArrayList<String>();
	private ArrayList<String> tokenLemmasAnswerOrig = new ArrayList<String>();
	private ArrayList<String> tokenStemsAnswerOrig = new ArrayList<String>();

	private ArrayList<Integer> similarAnswers = new ArrayList<Integer>();

	//default
	public Record(){
	}

	//fill a record
    public Record(PostedRecord r) {
		record.putAll(r.getPostedRecordString());

		record.put("question", record.get("question").replaceAll("\\n","").replaceAll("\\r","").toLowerCase());	//delete all newline and return characters		record.put("refAnswer", postedRecord.getReferenceanswer0().replaceAll("\\n","").replaceAll("\\r","").toLowerCase());	
		record.put("answer", record.get("answer").replaceAll("\\n","").replaceAll("\\r","").toLowerCase()); //TODO: ist das die Antwort die gegeben wurde?	
		record.put("question", record.get("question").replaceAll("\\n","").replaceAll("\\r",""));	//delete all newline and return characters
		record.put("refAnswer", record.get("refAnswer").replaceAll("\\n","").replaceAll("\\r",""));	
		record.put("answer", record.get("answer").replaceAll("\\n","").replaceAll("\\r",""));		
		record.put("pointsString", "1");
		
		if(r.getPostedRecordInt().get("max") > 0){
		    this.max = r.getPostedRecordInt().get("max");	
		}  
		this.exchId = r.getPostedRecordInt().get("id");
		this.min = r.getPostedRecordInt().get("min");
	}

	public int compareTo(Record argument) {    
        if(Double.isNaN(this.score)){
        	this.score = 0;
        }
        if(Double.isNaN(argument.score)){
        	argument.score = 0;
        }
        if (this.score < argument.score){
            return -1;
        } else if (this.score == argument.score){
            return 0;
        } else {
            return 1;
        }
	}	

	//Getters and Setters
    public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public int getExchId() {
		return exchId;
	}

	public void setExchId(int exchId) {
		this.exchId = exchId;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getSec() {
		return sec;
	}

	public void setSec(int sec) {
		this.sec = sec;
	}

	public int getNumAttempts() {
		return numAttempts;
	}

	public void setNumAttempts(int numAttempts) {
		this.numAttempts = numAttempts;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public ArrayList<String> getTokenWordsAnswer() {
		return tokenWordsAnswer;
	}

	public void setTokenWordsAnswer(ArrayList<String> tokenWordsAnswer) {
		this.tokenWordsAnswer = tokenWordsAnswer;
	}

	public ArrayList<String> getTokenPosAnswer() {
		return tokenPosAnswer;
	}

	public void setTokenPosAnswer(ArrayList<String> tokenPosAnswer) {
		this.tokenPosAnswer = tokenPosAnswer;
	}

	public ArrayList<String> getTokenLemmasAnswer() {
		return tokenLemmasAnswer;
	}

	public void setTokenLemmasAnswer(ArrayList<String> tokenLemmasAnswer) {
		this.tokenLemmasAnswer = tokenLemmasAnswer;
	}

	public ArrayList<String> getTokenStemsAnswer() {
		return tokenStemsAnswer;
	}

	public void setTokenStemsAnswer(ArrayList<String> tokenStemsAnswer) {
		this.tokenStemsAnswer = tokenStemsAnswer;
	}

	public ArrayList<String> getTokenWordsQuestion() {
		return tokenWordsQuestion;
	}

	public void setTokenWordsQuestion(ArrayList<String> tokenWordsQuestion) {
		this.tokenWordsQuestion = tokenWordsQuestion;
	}

	public ArrayList<String> getTokenPosQuestion() {
		return tokenPosQuestion;
	}

	public void setTokenPosQuestion(ArrayList<String> tokenPosQuestion) {
		this.tokenPosQuestion = tokenPosQuestion;
	}

	public ArrayList<String> getTokenLemmasQuestion() {
		return tokenLemmasQuestion;
	}

	public void setTokenLemmasQuestion(ArrayList<String> tokenLemmasQuestion) {
		this.tokenLemmasQuestion = tokenLemmasQuestion;
	}

	public ArrayList<String> getTokenStemsQuestion() {
		return tokenStemsQuestion;
	}

	public void setTokenStemsQuestion(ArrayList<String> tokenStemsQuestion) {
		this.tokenStemsQuestion = tokenStemsQuestion;
	}

	public ArrayList<String> getTokenWordsReferenceAnswer() {
		return tokenWordsReferenceAnswer;
	}

	public void setTokenWordsReferenceAnswer(
			ArrayList<String> tokenWordsReferenceAnswer) {
		this.tokenWordsReferenceAnswer = tokenWordsReferenceAnswer;
	}

	public ArrayList<String> getTokenPosReferenceAnswer() {
		return tokenPosReferenceAnswer;
	}

	public void setTokenPosReferenceAnswer(ArrayList<String> tokenPosReferenceAnswer) {
		this.tokenPosReferenceAnswer = tokenPosReferenceAnswer;
	}

	public ArrayList<String> getTokenLemmasReferenceAnswer() {
		return tokenLemmasReferenceAnswer;
	}

	public void setTokenLemmasReferenceAnswer(
			ArrayList<String> tokenLemmasReferenceAnswer) {
		this.tokenLemmasReferenceAnswer = tokenLemmasReferenceAnswer;
	}

	public ArrayList<String> getTokenStemsReferenceAnswer() {
		return tokenStemsReferenceAnswer;
	}

	public void setTokenStemsReferenceAnswer(
			ArrayList<String> tokenStemsReferenceAnswer) {
		this.tokenStemsReferenceAnswer = tokenStemsReferenceAnswer;
	}

	public ArrayList<String> getTokenWordsAnswerOrig() {
		return tokenWordsAnswerOrig;
	}

	public void setTokenWordsAnswerOrig(ArrayList<String> tokenWordsAnswerOrig) {
		this.tokenWordsAnswerOrig = tokenWordsAnswerOrig;
	}

	public ArrayList<String> getTokenPosAnswerOrig() {
		return tokenPosAnswerOrig;
	}

	public void setTokenPosAnswerOrig(ArrayList<String> tokenPosAnswerOrig) {
		this.tokenPosAnswerOrig = tokenPosAnswerOrig;
	}

	public ArrayList<String> getTokenLemmasAnswerOrig() {
		return tokenLemmasAnswerOrig;
	}

	public void setTokenLemmasAnswerOrig(ArrayList<String> tokenLemmasAnswerOrig) {
		this.tokenLemmasAnswerOrig = tokenLemmasAnswerOrig;
	}

	public ArrayList<String> getTokenStemsAnswerOrig() {
		return tokenStemsAnswerOrig;
	}

	public void setTokenStemsAnswerOrig(ArrayList<String> tokenStemsAnswerOrig) {
		this.tokenStemsAnswerOrig = tokenStemsAnswerOrig;
	}

	public HashMap<String, String> getRecord() {
		return record;
	}

	public void setRecord(HashMap<String, String> record) {
		this.record = record;
	}

	public void setScoreInter(double scoreInter) {
		this.scoreInter = scoreInter;
	}
	public double getScoreInter() {
		return scoreInter;
	}

	public ArrayList<Integer> getSimilarAnswers() {
		return similarAnswers;
	}

	public void setSimilarAnswers(ArrayList<Integer> similarAnswers) {
		this.similarAnswers.addAll(similarAnswers);
	}
}
