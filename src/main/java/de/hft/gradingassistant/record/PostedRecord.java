package de.hft.gradingassistant.record;

/**
 * 
 * for storing records in the webservice
 * @author kiefer
 *
 */
public class PostedRecord {
	
	
	
	protected String href;
	public int id;
	public String studentId;
	public String postId;
	public String question;
	public String referenceanswer0;
	public String answer;
	public int max;
	public String referenceanswer1;
	public String referenceanswer2;
	public String referenceanswer3;
	public String referenceanswer4;
	public String referenceanswer5;
	
	public int min;
	public int sec;
	public int numAttempts;
	
	
	public PostedRecord(){
		
	}


	public PostedRecord(String href, int id, String studentId, String postId,
			String question, String referenceanswer0, String answer, int max,
			String referenceanswer1, String referenceanswer2,
			String referenceanswer3, String referenceanswer4,
			String referenceanswer5, int min, int sec, int numAttempts) {
		super();
		this.href = href;
		this.id = id;
		this.studentId = studentId;
		this.postId = postId;
		this.question = question;
		this.referenceanswer0 = referenceanswer0;
		this.answer = answer;
		this.max = max;
		this.referenceanswer1 = referenceanswer1;
		this.referenceanswer2 = referenceanswer2;
		this.referenceanswer3 = referenceanswer3;
		this.referenceanswer4 = referenceanswer4;
		this.referenceanswer5 = referenceanswer5;
		this.min = min;
		this.sec = sec;
		this.numAttempts = numAttempts;
	}


	public String getHref() {
		return href;
	}


	public void setHref(String href) {
		this.href = href;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getStudentId() {
		return studentId;
	}


	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}


	public String getPostId() {
		return postId;
	}


	public void setPostId(String postId) {
		this.postId = postId;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public String getReferenceanswer0() {
		return referenceanswer0;
	}


	public void setReferenceanswer0(String referenceanswer0) {
		this.referenceanswer0 = referenceanswer0;
	}


	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public int getMax() {
		return max;
	}


	public void setMax(int max) {
		this.max = max;
	}


	public String getReferenceanswer1() {
		return referenceanswer1;
	}


	public void setReferenceanswer1(String referenceanswer1) {
		this.referenceanswer1 = referenceanswer1;
	}


	public String getReferenceanswer2() {
		return referenceanswer2;
	}


	public void setReferenceanswer2(String referenceanswer2) {
		this.referenceanswer2 = referenceanswer2;
	}


	public String getReferenceanswer3() {
		return referenceanswer3;
	}


	public void setReferenceanswer3(String referenceanswer3) {
		this.referenceanswer3 = referenceanswer3;
	}


	public String getReferenceanswer4() {
		return referenceanswer4;
	}


	public void setReferenceanswer4(String referenceanswer4) {
		this.referenceanswer4 = referenceanswer4;
	}


	public String getReferenceanswer5() {
		return referenceanswer5;
	}


	public void setReferenceanswer5(String referenceanswer5) {
		this.referenceanswer5 = referenceanswer5;
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


	@Override
	public String toString() {
		return "PostedRecord [href=" + href + ", id=" + id + ", studentId="
				+ studentId + ", postId=" + postId + ", question=" + question
				+ ", referenceanswer0=" + referenceanswer0 + ", answer="
				+ answer + ", max=" + max + ", referenceanswer1="
				+ referenceanswer1 + ", referenceanswer2=" + referenceanswer2
				+ ", referenceanswer3=" + referenceanswer3
				+ ", referenceanswer4=" + referenceanswer4
				+ ", referenceanswer5=" + referenceanswer5 + ", min=" + min
				+ ", sec=" + sec + ", numAttempts=" + numAttempts + "]";
	}
	
	
	
	  
   
}
