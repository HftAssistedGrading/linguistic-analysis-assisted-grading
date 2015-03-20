package de.hft.gradingassistant.record;

import java.util.ArrayList;
import java.util.List;
/**
 * represents a list of analyzed records
 * @author kiefer
 *
 */
public class ListOfAnalyzedRecords {
		
		    protected List<AnalyzedRecord> entry;

		   
		    public List<AnalyzedRecord> getEntry() {
		        if (entry == null) {
		            entry = new ArrayList<AnalyzedRecord>();
		        }
		        return this.entry;
		    }

		
}
