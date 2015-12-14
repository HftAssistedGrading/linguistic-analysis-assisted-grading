package de.hft.gradingassistant.record;

import java.util.HashMap;

/**
 * 
 * for storing records in the webservice
 * @author kiefer, meyer
 *
 */

public class PostedRecord {

	private HashMap<String, String> postedRecordString = new HashMap<String, String>();
	private HashMap<String, Integer> postedRecordInt = new HashMap<String, Integer>();

	public PostedRecord(){
		
	}

	@Override
	public String toString() {
		return "PostedRecord [id=" + postedRecordInt.get("id")
				+ ", studentId=" + postedRecordString.get("studentId")
				+ ", question=" + postedRecordString.get("question")
				+ ", refAnswer=" + postedRecordString.get("refAnswer") 
				+ ", answer=" + postedRecordString.get("answer")
				+ ", languageFlag=" + postedRecordString.get("languageFlag")
				+ ", max=" + postedRecordInt.get("max") 
				+ ", min=" + postedRecordInt.get("min") 
				+ ", sec=" + postedRecordInt.get("sec")
				+ ", numAttempts=" + postedRecordInt.get("numAttepts") + "]";
	}


	public HashMap<String, String> getPostedRecordString() {
		return postedRecordString;
	}

	public void setPostedRecordString(HashMap<String, String> postedRecord) {
		this.postedRecordString = postedRecord;
	}

	public HashMap<String, Integer> getPostedRecordInt() {
		return postedRecordInt;
	}

	public void setPostedRecordInt(HashMap<String, Integer> postedRecordInt) {
		this.postedRecordInt = postedRecordInt;
	}
}