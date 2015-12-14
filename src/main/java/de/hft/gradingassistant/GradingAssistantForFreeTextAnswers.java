package de.hft.gradingassistant;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;

import java.util.ArrayList;
import java.util.HashMap;

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
 * @author Cornelia Kiefer, Verena Meyer
 *
 */
public class GradingAssistantForFreeTextAnswers {

	//To hold all question-answer-pairs in one location
	private ArrayList<Record> records = new ArrayList<Record>();

	/**
	 * standard constructor
	 */
	public GradingAssistantForFreeTextAnswers(){
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
					.add("id", r.getExchId())
					.add("answer", r.getRecord().get("answerOrig"))
					.add("score", 1));
			System.out.println("GAFFTA " + r.getExchId() + "\t" + r.getRecord().get("answer"));
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
		JsonArrayBuilder builder = Json.createArrayBuilder();
		
		String finJSON = "";
		for(Record r : records){
			for(int id : r.getSimilarAnswers()){
				builder.add(id);
			}
			JsonArray array = builder.build();
			
			b.add(factory.createObjectBuilder()
					.add("id", r.getExchId())
					.add("answer", r.getRecord().get("answer"))
					.add("score", r.getScore())
					.add("sanity_check", array));
		}
			
		JsonArray a = b.build();
		finJSON = a.toString();
		System.out.println("finJSON " + finJSON.toString());
		
		return finJSON;
	}

	/**
	 * 	reads posted records to records
	 * @param list a list of posted records
	 */
	public void readPostedRecords(ListOfPostedRecords list){

		for(PostedRecord r : list.getEntry()){

			HashMap<String, String> test = new HashMap<String, String>();
			test.putAll(r.getPostedRecordString());

			Record record = new Record(r);
			
			//Set time needed for review
//			record.setMin(r.getPostedRecordInt().get("min"));
//			record.setSec(r.getPostedRecordInt().get("sec"));
//			record.setNumAttempts(r.getPostedRecordInt().get("numAttempts"));
//			record.setExchId(r.getPostedRecordInt().get("id"));

			records.add(record);
		}
	}

	/**
	 * calculate the score for each student answer
	 */
	public void doAnalysis(){

		//add linguistic info using dkpro library
		Preprocessor a = null;
		try {
			a = new Preprocessor(records.get(0).getRecord().get("languageFlag"));
		} catch (ResourceInitializationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		

		//the Preprocessor will tokenize, tag, stem, lemmatize and for some usages also lowercase the question, answer and all reference answers
		//the new information on tags, lemmas etc will be added to the corresponding Record object
		a.preprocessTextWithDKProPipeline(records);
		
		//Calculate similarity between the student answers to give feedback in the frontend (if given points
		//by a human grader are inconsistent for similar student answers)
		SimilarityCalculator c = new SimilarityCalculator();
		c.calculateSimilarity(records);
		
		//for each record, measure the similarity of the student answer and the reference answer (=referenceanswer)
		for(Record r : records){
			TextSimilarityMeasure measureText = null;		
			//min num of chars is 4
			measureText = new GreedyStringTiling(4);
			double lemmasScore = 0.0;

			try {
				lemmasScore = measureText.getSimilarity(r.getTokenLemmasReferenceAnswer(), r.getTokenLemmasAnswer());				 							
			} catch (SimilarityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			//store the similarity score using stems in the Record object field score
			r.setScore(lemmasScore);
		}		
	}

	public ArrayList<Record> getRecords() {
		return records;
	}
}