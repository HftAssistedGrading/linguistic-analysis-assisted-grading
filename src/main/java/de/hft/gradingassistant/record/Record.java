package de.hft.gradingassistant.record;
import java.util.ArrayList;

import org.apache.uima.UIMAException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;




/**
 * Representation of a record for the analysis. A Record holds all information on one question-answer pair in a quiz
 * records are compared using their similarity score so they can be sorted for estimated quality of student answers
 * @author kiefer
 *
 */
public class Record implements Comparable<Record>{
	
	public String predictedPointsString;
	public double predictedPoints;
	
	public double max;
	
	public int exchId;
	public String studentIdString;
	public int questionid;
	public String question;
	public String answer;
	public String referenceanswer;
	
	public int min;
	public int sec;
	public int numAttempts;
	
	
	//alternatives for full correct answers
	public String referenceanswer1;	
	public String referenceanswer2;	
	public String referenceanswer3;		
	public String referenceanswer4;	
	public String referenceanswer5;		
	
	
	public String referenceanswerOrig1;	
	public String referenceanswerOrig2;		
	public String referenceanswerOrig3;		
	public String referenceanswerOrig4;	
	public String referenceanswerOrig5;	
	
	
	public String questionOrig;
	public String answerOrig;
	public String referenceanswerOrig;
	
	public String pointsString;
	public String binaryPrediction;
	
	public String answerHighlighted;
	
	//store the final score that will be used here
	public double score;
	
	//
	public double stemsScore0;
	public double stemsScoreOfBestScoredStudentAnswerUsingStemsScore0;
	
	//similarity scores based on lemmas
	public double lemmasScore0;
	public double lemmasScore1;
	public double lemmasScore2;
	public double lemmasScore3;
	public double lemmasScore4;
	public double lemmasScore5;
	
	//similarity scores as Strings
	public String tokensScoreString;
	public String tagsScoreString;
	public String stemsScoreString;
	public String lemmasScoreString;
	public String jCasScoreString;
	public String lemmasScore1String;
	public String finalScoreString;
	
	
	//added analysis: linguistic information from single methods (opennlp)
	public String[] questionSentenceSplitted;
	public String[] answerSentenceSplitted;
	public String[] referenceanswerSentenceSplitted;
	 
	public String[] questionTokens;
	public String[] answerTokens;
	public String[] referenceanswerTokens;
	
	public String[] questionTags;
	public String[] answerTags;
	public String[] referenceanswerTags;
	
	
	
	//DKPro Processing Pipeline info	
	
	public ArrayList<String> tokenWordsAnswer; 
	public ArrayList<String> tokenPosAnswer; 
	public ArrayList<String> tokenLemmasAnswer;  
	public ArrayList<String> tokenStemsAnswer;  
	
	public ArrayList<String> tokenWordsQuestion; 
	public ArrayList<String> tokenPosQuestion; 
	public ArrayList<String> tokenLemmasQuestion;  
	public ArrayList<String> tokenStemsQuestion;  
	
	public ArrayList<String> tokenWordsReferenceAnswer; 
	public ArrayList<String> tokenPosReferenceAnswer; 
	public ArrayList<String> tokenLemmasReferenceAnswer;  
	public ArrayList<String> tokenStemsReferenceAnswer;  
	
   	//orig
	
	public ArrayList<String> tokenWordsAnswerOrig; 
	public ArrayList<String> tokenPosAnswerOrig; 
	public ArrayList<String> tokenLemmasAnswerOrig;  
	public ArrayList<String> tokenStemsAnswerOrig;  

	//JCAS
	
	public JCas jCasReferenceAnswer;
	public JCas jCasAnswer;
	public JCas jCasQuestion;
	public JCas jCasAnswerOrig;
	
	
	public ArrayList<ArrayList<String>> answerTokensHighlighted;
	
	//default
	public Record(){	
		exchId = 0;
		min = 0;
		sec = 0;
		numAttempts = 0;
		
		predictedPoints = 0.0;
		score = 0.0;
		stemsScore0 = 0.0;
		stemsScoreOfBestScoredStudentAnswerUsingStemsScore0 = 0.0;
		max = 0.0;
		
		lemmasScore0 = 0.0;
		lemmasScore1 = 0.0;
		lemmasScore2 = 0.0;
		lemmasScore3 = 0.0;
		lemmasScore4 = 0.0;
		lemmasScore5 = 0.0;
		
		studentIdString = null;		
		question = null;		
		referenceanswer = null;			
		answer = null;		
		
		questionOrig = null;		
		referenceanswerOrig = null;			
		answerOrig = null;		
		
		pointsString = null;		
		binaryPrediction = "";
		
		//reference answer, up to 5 parts
		referenceanswer1 = null;	
		referenceanswer2= null;	
		referenceanswer3 = null;		
		referenceanswer4 = null;	
		referenceanswer5 = null;		
		
		referenceanswerOrig1 = null;	
		referenceanswerOrig2 = null;		
		referenceanswerOrig3 = null;		
		referenceanswerOrig4 = null;	
		referenceanswerOrig5 = null;	
		
		questionSentenceSplitted = null;
		answerSentenceSplitted = null;
		questionTokens= null;
		answerTokens= null;
		
		answerHighlighted = null;
		
		tokenWordsAnswer = new ArrayList<String>(); 
		tokenPosAnswer= new ArrayList<String>(); 
		tokenLemmasAnswer= new ArrayList<String>();  
		tokenStemsAnswer= new ArrayList<String>();   
		
		tokenWordsAnswerOrig = new ArrayList<String>(); 
		tokenPosAnswerOrig= new ArrayList<String>(); 
		tokenLemmasAnswerOrig= new ArrayList<String>();  
		tokenStemsAnswerOrig= new ArrayList<String>();   
		
		tokenWordsQuestion= new ArrayList<String>();  
		tokenPosQuestion= new ArrayList<String>();  
		tokenLemmasQuestion= new ArrayList<String>();   
		tokenStemsQuestion= new ArrayList<String>();   
		
		tokenWordsReferenceAnswer= new ArrayList<String>();  
		tokenPosReferenceAnswer= new ArrayList<String>(); 
		tokenLemmasReferenceAnswer= new ArrayList<String>();  
		tokenStemsReferenceAnswer= new ArrayList<String>();  
		
		answerTokensHighlighted = new ArrayList<ArrayList<String>>();
		
		tokensScoreString = "";
		tagsScoreString = "";
		stemsScoreString = "";
		lemmasScoreString = "";
		jCasScoreString = "";
		lemmasScore1String = "";
		finalScoreString = "";
		
		try {
			
			jCasReferenceAnswer = JCasFactory.createJCas();
			jCasAnswer = JCasFactory.createJCas();
			jCasAnswerOrig = JCasFactory.createJCas();
			jCasQuestion = JCasFactory.createJCas();
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		jCasReferenceAnswer.reset();
		jCasReferenceAnswer.setDocumentLanguage("de");
		jCasAnswer.reset();
		jCasAnswer.setDocumentLanguage("de");
		jCasQuestion.reset();
		jCasQuestion.setDocumentLanguage("de");
		jCasAnswerOrig.reset();
		jCasAnswerOrig.setDocumentLanguage("de");
		
	}
	
	
	
	
	//fill a record using an Array of Strings
    public Record(String[] s){		
    	exchId = 0;
    	min = 0;
		sec = 0;
		numAttempts = 0;
		
    	score = 0.0;	
		stemsScore0 = 0.0;
		stemsScoreOfBestScoredStudentAnswerUsingStemsScore0 = 0.0;
		predictedPoints = 0.0;
		
		lemmasScore0 = 0.0;
		lemmasScore1 = 0.0;
		lemmasScore2 = 0.0;
		lemmasScore3 = 0.0;
		lemmasScore4 = 0.0;
		lemmasScore5 = 0.0;
		
    	studentIdString = s[0];				
		question = s[1].replaceAll("\\n","").replaceAll("\\r","").toLowerCase();	//delete all newline and return characters
		referenceanswer = s[2].replaceAll("\\n","").replaceAll("\\r","").toLowerCase();	
		answer = s[3].replaceAll("\\n","").replaceAll("\\r","").toLowerCase();	
		
		questionOrig = s[1].replaceAll("\\n","").replaceAll("\\r","");	//delete all newline and return characters
		referenceanswerOrig = s[2].replaceAll("\\n","").replaceAll("\\r","");	
		answerOrig = s[3].replaceAll("\\n","").replaceAll("\\r","");		
		
		pointsString = s[4];
			
			
		//reference answer, up to 5 parts		
		referenceanswer1 = s[5].replaceAll("\\n","").replaceAll("\\r","").toLowerCase();	
		referenceanswer2 = s[6].replaceAll("\\n","").replaceAll("\\r","").toLowerCase();	
		referenceanswer3 = s[7].replaceAll("\\n","").replaceAll("\\r","").toLowerCase();	
		referenceanswer4 = s[8].replaceAll("\\n","").replaceAll("\\r","").toLowerCase();	
		referenceanswer5 = s[9].replaceAll("\\n","").replaceAll("\\r","").toLowerCase();	
		
		
		
		referenceanswerOrig1 = s[5].replaceAll("\\n","").replaceAll("\\r","");	
		referenceanswerOrig2 = s[6].replaceAll("\\n","").replaceAll("\\r","");	
		referenceanswerOrig3 = s[7].replaceAll("\\n","").replaceAll("\\r","");	
		referenceanswerOrig4 = s[8].replaceAll("\\n","").replaceAll("\\r","");	
		referenceanswerOrig5 = s[9].replaceAll("\\n","").replaceAll("\\r","");	
		
		if(s[10] != ""){
		    max = Double.parseDouble(s[10]);	
		}
		
		questionSentenceSplitted = null;
		answerSentenceSplitted = null;
		questionTokens= null;
		answerTokens= null;
		
		
		tokenWordsAnswer = new ArrayList<String>(); 
		tokenPosAnswer= new ArrayList<String>(); 
		tokenLemmasAnswer= new ArrayList<String>();  
		tokenStemsAnswer= new ArrayList<String>();   
		
		tokenWordsQuestion= new ArrayList<String>();  
		tokenPosQuestion= new ArrayList<String>();  
		tokenLemmasQuestion= new ArrayList<String>();   
		tokenStemsQuestion= new ArrayList<String>();   
		
		tokenWordsReferenceAnswer= new ArrayList<String>();  
		tokenPosReferenceAnswer= new ArrayList<String>(); 
		tokenLemmasReferenceAnswer= new ArrayList<String>();  
		tokenStemsReferenceAnswer= new ArrayList<String>();  
		
		jCasReferenceAnswer = null; 
		jCasAnswer = null;
		jCasQuestion = null;
		
		tokensScoreString = "";
		tagsScoreString = "";
		stemsScoreString = "";
		lemmasScoreString = "";
		jCasScoreString = "";
		lemmasScore1String = "";
		finalScoreString = "";
		
		answerTokensHighlighted = new ArrayList<ArrayList<String>>();
		
		try {
			
			jCasReferenceAnswer = JCasFactory.createJCas();
			jCasAnswer = JCasFactory.createJCas();
			jCasQuestion = JCasFactory.createJCas();
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		jCasReferenceAnswer.reset();
		jCasReferenceAnswer.setDocumentLanguage("de");
		jCasAnswer.reset();
		jCasAnswer.setDocumentLanguage("de");
		jCasQuestion.reset();
		jCasQuestion.setDocumentLanguage("de");
	}
    
    
    public int compareTo(Record argument) {    
    	
        if(Double.isNaN(this.score)){
        	this.score = 0;
        }
        if(Double.isNaN(argument.score)){
        	argument.score = 0;
        }
    	
    	
        if ( this.score < argument.score ){
            return -1;
        } else if ( this.score == argument.score ){
            return 0;
        } else {
            return 1;
        }
           
    }
    
    
	
}
