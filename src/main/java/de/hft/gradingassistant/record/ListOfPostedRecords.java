package de.hft.gradingassistant.record;

import java.util.ArrayList;
import java.util.List;

/**
 * for storing a list of records in the webservice
 * @author kiefer
 *
 */
public class ListOfPostedRecords {

	    protected List<PostedRecord> entry;
	    protected String href;

	    public List<PostedRecord> getEntry() {
	        if (entry == null) {
	            entry = new ArrayList<PostedRecord>();
	        }
	        return this.entry;
	    }
	    
	    public String getHref() {
	        return href;
	    }
 
	    public void setHref(String value) {
	        this.href = value;
	    }
	     
		@Override
		public String toString() {
			return "ListOfPostedRecords [entry=" + entry + ", href=" + href
					+ "]";
		}
}
