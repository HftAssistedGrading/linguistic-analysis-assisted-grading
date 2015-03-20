package de.hft.gradingassistant.record;

/**
 * represents an already analyzed and scored record
 * @author kiefer
 *
 */
public class AnalyzedRecord {

	private int id;
	private String answer;
	private double score;
	
	public AnalyzedRecord(){
		
	}
	
	public AnalyzedRecord(int id, String answer, double score) {
		super();
		this.id = id;
		this.answer = answer;
		this.score = score;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	@Override
	public String toString() {
		return "AnalyzedRecord [id=" + id + ", answer=" + answer + ", score="
				+ score + "]";
	}
	
	
	
}
