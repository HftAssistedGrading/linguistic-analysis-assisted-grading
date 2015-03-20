package de.hft.gradingassistant;


import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import java.util.ArrayList;
import org.apache.uima.resource.ResourceInitializationException;
import de.hft.gradingassistant.nlp.Preprocessor;
import de.hft.gradingassistant.record.ListOfPostedRecords;
import de.hft.gradingassistant.record.PostedRecord;
import de.hft.gradingassistant.record.Record;
import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.string.GreedyStringTiling;



/**
 * this class provides methods for data exchange in json format and calculation of a text similarity measure for sorting
 * @author Cornelia Kiefer
 *
 */
public class GradingAssistantForFreeTextAnswers {
	


	public ArrayList<Record> records;

	
	/**
	 * standard constructor
	 */
	public GradingAssistantForFreeTextAnswers(){
		
		records = new ArrayList<Record>();		
	}
	
	
		
	/**
	 * use this if you want to skip analysis: returns the list of posted records as a json string, score set to 1
	 * @return json formatted string
	 */
	public String getListOfPRAsJSONString(){		
		
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonArrayBuilder b = factory.createArrayBuilder();
		
	    String finJSON = "";
	
		for(Record r : records){        
			
	            b.add(factory.createObjectBuilder()
                .add("id", r.exchId)
                .add("answer", r.answerOrig)
                .add("score", 1));             
	        
		}
		
		JsonArray a = b.build();
		finJSON = a.toString();
		
		return finJSON;
	}
	
	
	/**
	 * use this if you want to use analyzed data: returns the list of posted records as a json string, score determined by analysis
	 * @return json formatted string
	 */
	public String getAnalyzedRecordsAsJSONString(){		
		
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonArrayBuilder b = factory.createArrayBuilder();

		String finJSON = "";	
		for(Record r : records){	        
			
	            b.add(factory.createObjectBuilder()
                .add("id", r.exchId)
                .add("answer", r.answerOrig)
                .add("score", r.score));

		}
		
		JsonArray a = b.build();
		finJSON = a.toString();	
			
		return finJSON;
	}
	
	
	
	/**
	 * 	reads posted records to records
	 * @param list a list of posted records
	 */
	public void readPostedRecords(ListOfPostedRecords list){
		for(PostedRecord r : list.getEntry()){
			String[] data;
			data = new String[11];
										
			data[0] = r.studentId;
			data[1] = r.getQuestion();
			data[2] = r.getReferenceanswer0();
			data[3] = r.getAnswer();
			data[4] = "1";
			data[5] = " ";
			data[6] = " ";
			data[7] = " ";
			data[8] = " ";
			data[9] = " ";
			data[10] = r.max + "";
			
			Record record = new Record(data);
			
			record.min = r.min;
			record.sec = r.sec;
			record.numAttempts = r.numAttempts;
			record.exchId = r.id;
						
			records.add(record);
		}
		
	}
	
	
	
	
	/**
	 * calculate the score for each student answer
	 */
	public void doAnalysis(){
    	
    	//add linguistic info using dkpro library
		//german analysis!
		Preprocessor a = null;
		try {
			a = new Preprocessor();
		} catch (ResourceInitializationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		
		//the Preprocessor will tokenize, tag, stem, lemmatize and for some usages also lowercase the question, answer and all reference answers
		//the new information on tags, lemmas etc will be added to the corresponding Record object
		a.preprocessTextWithDKProPipeline(records);
		
		
		//for each record, measure the similarity of the student answer and the reference answer (=referenceanswer)
				for(Record r : records){						
								
						
					TextSimilarityMeasure measureText = null;
									
					//min num of chars is 4
					measureText = new GreedyStringTiling(4);
				
			  		double lemmasScore = 0.0;		  	
			  			  		
					try {
						
						lemmasScore = measureText.getSimilarity(r.tokenLemmasReferenceAnswer, r.tokenLemmasAnswer);				 				
									
					} catch (SimilarityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			
			  		//store the similarity score using stems in the Record object field score
			  		r.score = lemmasScore;
			  		
					
				}
				
	}
    
    

}




	
	

	
	
	
		
		
		
		
				
		
		
		
				
	
		   
       
       
	
		
	
		
				
				
		
		
		
		
		
		

		
		
		
		
		
		
		
	
		
		
	
		
		
		
		
		
	
	




