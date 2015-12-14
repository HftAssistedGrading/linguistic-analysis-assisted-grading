package de.hft.gradingassistant;

import java.util.ArrayList;

import de.hft.gradingassistant.record.Record;
import dkpro.similarity.algorithms.api.SimilarityException;
import dkpro.similarity.algorithms.api.TextSimilarityMeasure;
import dkpro.similarity.algorithms.lexical.string.GreedyStringTiling;

/**
 * Calculate the similarity between all student answers 
 * to deliver the basics for the sanity check.
 * (Sanity Check = A test if similar answers get similar grades) 
 * 
 *  @author	Cornelia Kiefer, Verena Meyer
 */
public class SimilarityCalculator {

	//INFO: If you want to raise or lower the bar for similar answers just alter the score of 0.29 (higher number: get fewer similar answers, 
	// lower number: get more similar answers)
	//e.g. 1.0 -> you only get answers which are absolutely identical, 0.0 -> all answers are similar to each other.
	private static final double THRESHOLD = 0.34;
	
	public ArrayList<Record> calculateSimilarity(ArrayList<Record> records){

		//Iterate over all records
		for(int counter_out = 0; counter_out < records.size(); counter_out++){
			ArrayList<Integer> similarAnswers_out = new ArrayList<Integer>();
			ArrayList<Integer> similarAnswers_in = new ArrayList<Integer>();
			TextSimilarityMeasure measureText = null;		
			//min num of chars is 4
			//pick the algorithm you want to use to calculate the similarity
			measureText = new GreedyStringTiling(4);
//			measureText = new CosineSimilarity();
			double lemmasScoreInter1 = 0.0;
			//Iterate over all records to be able to compare every answer with every answer
			for(int counter_in = counter_out + 1; counter_in < records.size(); counter_in++){

				double lemmasScoreInter2 = 0.0;
				//Make sure the answers that should be compared have reasonable size
				if(records.get(counter_out).getRecord().get("studentId") != records.get(counter_in).getRecord().get("studentId") &&
						records.get(counter_out).getTokenLemmasAnswer().size() > 0 &&
						records.get(counter_out).getRecord().get("answer").length() > 10 &&
						records.get(counter_in).getTokenLemmasAnswer().size() > 0 &&
						records.get(counter_in).getRecord().get("answer").length() > 10){
					try {
						//calculate similarity
						lemmasScoreInter1 = measureText.getSimilarity(records.get(counter_out).getTokenLemmasAnswer(), records.get(counter_in).getTokenLemmasAnswer());	
						lemmasScoreInter2 = measureText.getSimilarity(records.get(counter_in).getTokenLemmasAnswer(), records.get(counter_out).getTokenLemmasAnswer());
					} catch (SimilarityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
					/*store the similar
					&& is more restrictive than || if you want to use all results if either lemmasScoreInter1 or lemmasScoreInter2 change back to || 
					(depends on algorithm to calculate similarity) */
					if(lemmasScoreInter1 >= THRESHOLD && lemmasScoreInter2 >= THRESHOLD){						
						records.get(counter_out).setScoreInter(lemmasScoreInter1);
						similarAnswers_out.add(records.get(counter_in).getExchId());
						
						records.get(counter_in).setScoreInter(lemmasScoreInter2);
						similarAnswers_in.add(records.get(counter_out).getExchId());
					}
					 
				} 
				records.get(counter_in).setSimilarAnswers(similarAnswers_in);
				similarAnswers_in.clear();
			}
			records.get(counter_out).setSimilarAnswers(similarAnswers_out);
		}
		return records;
	}
}
