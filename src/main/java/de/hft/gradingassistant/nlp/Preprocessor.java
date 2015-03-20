package de.hft.gradingassistant.nlp;


import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;

import static org.apache.uima.fit.util.JCasUtil.select;


import java.io.BufferedWriter;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import de.hft.gradingassistant.record.Record;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.tudarmstadt.ukp.dkpro.core.snowball.SnowballStemmer;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Stem;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.*;



/**
 * preprocesses a list of Records and enriches them with information on tokens, tags, stems, lemmas and syntax tree
 * uses dkpro to process records using the whole preprocessing pipeline at a time
 * @author kiefer
 *
 */
public class Preprocessor {
	
	public String question;
	public String answer;
	public String referenceanswer;
	
		
	public AnalysisEngine segmenter;
	public AnalysisEngine postagger;
	public AnalysisEngine lemmatizer;
	public AnalysisEngine stemmer;
	public AnalysisEngine spellchecker;
	public AnalysisEngine stopwordremover;

	
	/**
	 * uses dkpro to process records using the whole preprocessing pipeline at a time 
	 * initializes the preprocessing engines
	 * @param mode
	 * @throws ResourceInitializationException
	 */
	public Preprocessor() throws ResourceInitializationException{
						
			
		segmenter = createEngine(StanfordSegmenter.class, StanfordSegmenter.PARAM_LANGUAGE, "de");	
		postagger = createEngine(StanfordPosTagger.class,  StanfordPosTagger.PARAM_LANGUAGE, "de");		  
		lemmatizer = createEngine(StanfordLemmatizer.class);	
		stemmer = createEngine(SnowballStemmer.class, SnowballStemmer.PARAM_LANGUAGE, "de", SnowballStemmer.PARAM_LOWER_CASE, true);	
		
		
	}
	
	
	/**
	 * preprocess the question and all reference answers
	 * @param records
	 */
	public void preprocessTextWithDKProPipeline(ArrayList<Record> records){		
		
		question = records.get(1).question;			
		referenceanswer = records.get(1).referenceanswer; 	
		
		//create a new JCas and add the text
		JCas qu = createJCasAndRunDKProPipelineNoStopwordRemoval(question);	
		//then gather the preprocessing information
		HashMap<String, ArrayList<String>> preprocessingInfoQuestion = getPreprocessedTokens(qu);			
				
		
		//prepare stopword removal (we want to remove normal stopwords (snowball) and all words that were already mentioned in the 
		//question from the student answer
		
		//get all words in the question and write them to the stopword file stopwords_extracted_from_question.txt
		ArrayList<String> wordsInQuestion = preprocessingInfoQuestion.get("words");
			
		
		ArrayList<String> finalListOfStopwords = new ArrayList<String>();
		ArrayList<String> snowballStopwords = new ArrayList<String>();		
		ArrayList<String> stopwordsInQuestion = new ArrayList<String>();
		
		 for(String wordInQu : wordsInQuestion){
			 stopwordsInQuestion.add(wordInQu.toLowerCase());	    	
	     }
		
	
		snowballStopwords.add("aber");
		snowballStopwords.add("alle");
		snowballStopwords.add("allem");
		snowballStopwords.add("allen");
		snowballStopwords.add("aller");
		snowballStopwords.add("alles");
		snowballStopwords.add("als");
		snowballStopwords.add("also");
		snowballStopwords.add("am");
		snowballStopwords.add("an");
		snowballStopwords.add("ander");
		snowballStopwords.add("andere");
		snowballStopwords.add("anderem");
		snowballStopwords.add("anderen");
		snowballStopwords.add("anderer");
		snowballStopwords.add("anderes");
		snowballStopwords.add("anderm");
		snowballStopwords.add("andern");
		snowballStopwords.add("anderr");
		snowballStopwords.add("anders");
		snowballStopwords.add("auch");
		snowballStopwords.add("auf");
		snowballStopwords.add("aus");
		snowballStopwords.add("bei");
		snowballStopwords.add("bin");
		snowballStopwords.add("bis");
		snowballStopwords.add("bist");
		snowballStopwords.add("da");
		snowballStopwords.add("damit");
		snowballStopwords.add("dann");
		snowballStopwords.add("der");
		snowballStopwords.add("den");
		snowballStopwords.add("des");
		snowballStopwords.add("dem");
		snowballStopwords.add("die");
		snowballStopwords.add("das");
		snowballStopwords.add("daß");
		snowballStopwords.add("dass");
		snowballStopwords.add("derselbe");
		snowballStopwords.add("derselben");
		snowballStopwords.add("denselben");
		snowballStopwords.add("desselben");
		snowballStopwords.add("demselben");
		snowballStopwords.add("dieselbe");
		snowballStopwords.add("dieselben");
		snowballStopwords.add("dasselbe");
		snowballStopwords.add("dazu");
		snowballStopwords.add("dein");
		snowballStopwords.add("deine");
		snowballStopwords.add("deinem");
		snowballStopwords.add("deinen");
		snowballStopwords.add("deiner");
		snowballStopwords.add("deines");
		snowballStopwords.add("denn");
		snowballStopwords.add("derer");
		snowballStopwords.add("dessen");
		snowballStopwords.add("dich");
		snowballStopwords.add("dir");
		snowballStopwords.add("du");
		snowballStopwords.add("dies");
		snowballStopwords.add("diese");
		snowballStopwords.add("diesem");
		snowballStopwords.add("diesen");
		snowballStopwords.add("dieser");
		snowballStopwords.add("dieses");
		snowballStopwords.add("doch");
		snowballStopwords.add("dort");
		snowballStopwords.add("durch");
		snowballStopwords.add("ein");
		snowballStopwords.add("eine");
		snowballStopwords.add("einem");
		snowballStopwords.add("einen");
		snowballStopwords.add("einer");
		snowballStopwords.add("eines");
		snowballStopwords.add("einig");
		snowballStopwords.add("einige");
		snowballStopwords.add("einigem");
		snowballStopwords.add("einigen");
		snowballStopwords.add("einiger");
		snowballStopwords.add("einiges");
		snowballStopwords.add("einmal");
		snowballStopwords.add("er");
		snowballStopwords.add("ihn");
		snowballStopwords.add("ihm");
		snowballStopwords.add("es");
		snowballStopwords.add("etwas");
		snowballStopwords.add("euer");
		snowballStopwords.add("eure");
		snowballStopwords.add("eurem");
		snowballStopwords.add("euren");
		snowballStopwords.add("eurer");
		snowballStopwords.add("eures");
		snowballStopwords.add("für");
		snowballStopwords.add("gegen");
		snowballStopwords.add("gewesen");
		snowballStopwords.add("hab");
		snowballStopwords.add("habe");
		snowballStopwords.add("haben");
		snowballStopwords.add("hat");
		snowballStopwords.add("hatte");
		snowballStopwords.add("hatten");
		snowballStopwords.add("hier");
		snowballStopwords.add("hin");
		snowballStopwords.add("hinter");
		snowballStopwords.add("ich");
		snowballStopwords.add("mich");
		snowballStopwords.add("mir");
		snowballStopwords.add("ihr");
		snowballStopwords.add("ihre");
		snowballStopwords.add("ihrem");
		snowballStopwords.add("ihren");
		snowballStopwords.add("ihrer");
		snowballStopwords.add("ihres");
		snowballStopwords.add("euch");
		snowballStopwords.add("im");
		snowballStopwords.add("in");
		snowballStopwords.add("indem");
		snowballStopwords.add("ins");
		snowballStopwords.add("ist");
		snowballStopwords.add("jede");
		snowballStopwords.add("jedem");
		snowballStopwords.add("jeden");
		snowballStopwords.add("jeder");
		snowballStopwords.add("jedes");
		snowballStopwords.add("jene");
		snowballStopwords.add("jenem");
		snowballStopwords.add("jenen");
		snowballStopwords.add("jener");
		snowballStopwords.add("jenes");
		snowballStopwords.add("jetzt");
		snowballStopwords.add("kann");
		snowballStopwords.add("kein");
		snowballStopwords.add("keine");
		snowballStopwords.add("keinem");
		snowballStopwords.add("keinen");
		snowballStopwords.add("keiner");
		snowballStopwords.add("keines");
		snowballStopwords.add("können");
		snowballStopwords.add("könnte");
		snowballStopwords.add("machen");
		snowballStopwords.add("man");
		snowballStopwords.add("manche");
		snowballStopwords.add("manchem");
		snowballStopwords.add("manchen");
		snowballStopwords.add("mancher");
		snowballStopwords.add("manches");
		snowballStopwords.add("mein");
		snowballStopwords.add("meine");
		snowballStopwords.add("meinem");
		snowballStopwords.add("meinen");
		snowballStopwords.add("meiner");
		snowballStopwords.add("meines");
		snowballStopwords.add("mit");
		snowballStopwords.add("muss");
		snowballStopwords.add("musste");
		snowballStopwords.add("nach");
		//snowballStopwords.add("nicht");
		//snowballStopwords.add("nichts");
		snowballStopwords.add("noch");
		snowballStopwords.add("nun");
		snowballStopwords.add("nur");
		snowballStopwords.add("ob");
		snowballStopwords.add("oder");
		snowballStopwords.add("ohne");
		snowballStopwords.add("sehr");
		snowballStopwords.add("sein");
		snowballStopwords.add("seine");
		snowballStopwords.add("seinem");
		snowballStopwords.add("seinen");
		snowballStopwords.add("seiner");
		snowballStopwords.add("seines");
		snowballStopwords.add("selbst");
		snowballStopwords.add("sich");
		snowballStopwords.add("sie");
		snowballStopwords.add("ihnen");
		snowballStopwords.add("sind");
		snowballStopwords.add("so");
		snowballStopwords.add("solche");
		snowballStopwords.add("solchem");
		snowballStopwords.add("solchen");
		snowballStopwords.add("solcher");
		snowballStopwords.add("solches");
		snowballStopwords.add("soll");
		snowballStopwords.add("sollte");
		snowballStopwords.add("sondern");
		snowballStopwords.add("sonst");
		snowballStopwords.add("über");
		snowballStopwords.add("um");
		snowballStopwords.add("und");
		snowballStopwords.add("uns");
		snowballStopwords.add("unse");
		snowballStopwords.add("unsem");
		snowballStopwords.add("unsen");
		snowballStopwords.add("unser");
		snowballStopwords.add("unses");
		snowballStopwords.add("unter");
		snowballStopwords.add("viel");
		snowballStopwords.add("vom");
		snowballStopwords.add("von");
		snowballStopwords.add("vor");
		snowballStopwords.add("während");
		snowballStopwords.add("war");
		snowballStopwords.add("waren");
		snowballStopwords.add("warst");
		snowballStopwords.add("was");
		snowballStopwords.add("weg");
		snowballStopwords.add("weil");
		snowballStopwords.add("weiter");
		snowballStopwords.add("welche");
		snowballStopwords.add("welchem");
		snowballStopwords.add("welchen");
		snowballStopwords.add("welcher");
		snowballStopwords.add("welches");
		snowballStopwords.add("wenn");
		snowballStopwords.add("werde");
		snowballStopwords.add("werden");
		snowballStopwords.add("wie");
		snowballStopwords.add("wieder");
		snowballStopwords.add("will");
		snowballStopwords.add("wir");
		snowballStopwords.add("wird");
		snowballStopwords.add("wirst");
		snowballStopwords.add("wo");
		snowballStopwords.add("wollen");
		snowballStopwords.add("wollte");
		snowballStopwords.add("würde");
		snowballStopwords.add("würden");
		snowballStopwords.add("zu");
		snowballStopwords.add("zum");
		snowballStopwords.add("zur");
		snowballStopwords.add("zwar");
		snowballStopwords.add("zwischen");

		finalListOfStopwords.addAll(snowballStopwords);
		finalListOfStopwords.addAll(stopwordsInQuestion);
		
		Writer writer = null;

		try {
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream("/tmp/stopwords.txt"), "UTF-8"));
		    
		    for(String s : finalListOfStopwords){
		    	 writer.write(s.toLowerCase());
		    	 ((BufferedWriter) writer).newLine();
		    }
		    writer.flush();
            writer.close();
		   
		} catch (IOException ex) {
		  // report
		} finally {
		   try {writer.close();} catch (Exception ex) {}
		}
		
		
		//then create the stopwordremover engine
		try {
			stopwordremover = createEngine(StopWordRemover.class, StopWordRemover.PARAM_STOP_WORD_LIST_FILE_NAMES, "/tmp/stopwords.txt");
		} catch (ResourceInitializationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Problem creating the Stopwordremover.");
			
		}
			
				
		//gather preprocessing info for all reference answers
		JCas ra = createJCasAndRunDKProPipelineWithStopwordRemoval(referenceanswer.replaceAll("[^a-zA-Zäüö0123456789ß ]", ""));			
		HashMap<String, ArrayList<String>> preprocessingInfoReferenceAnswer = getPreprocessedTokens(ra);	
		
		
		for(Record r : records){
			
					
			//enrich each record with the new representations of question and reference answers
						
			r.jCasQuestion = qu;
			r.jCasReferenceAnswer = ra;						
		
			r.tokenWordsQuestion = preprocessingInfoQuestion.get("words");
			r.tokenPosQuestion = preprocessingInfoQuestion.get("pos");
			r.tokenLemmasQuestion = preprocessingInfoQuestion.get("lemmas");
			r.tokenStemsQuestion = preprocessingInfoQuestion.get("stems");
			
			r.tokenWordsReferenceAnswer = preprocessingInfoReferenceAnswer.get("words");
			r.tokenPosReferenceAnswer = preprocessingInfoReferenceAnswer.get("pos");
			r.tokenLemmasReferenceAnswer = preprocessingInfoReferenceAnswer.get("lemmas");
			r.tokenStemsReferenceAnswer = preprocessingInfoReferenceAnswer.get("stems");
			
						
			//enrich each record with the new representations of the student answer, with and without stopword removal
            JCas aOrig = createJCasAndRunDKProPipelineNoStopwordRemoval(r.answerOrig);				
			HashMap<String, ArrayList<String>> preprocessingInfoAnswerOrig = getPreprocessedTokens(aOrig);			
			r.jCasAnswerOrig = aOrig;
					
			r.tokenWordsAnswerOrig = preprocessingInfoAnswerOrig.get("words"); //The highlighted answer in the end shall contain case information
			
			//lowercased version of the student answer
			JCas aOrigLowercased = createJCasAndRunDKProPipelineNoStopwordRemoval(r.answer);				
			HashMap<String, ArrayList<String>> preprocessingInfoAnswerOrigLowercased = getPreprocessedTokens(aOrigLowercased);			
			r.jCasAnswerOrig = aOrigLowercased;			
			
			//r.tokenWordsAnswerOrig = preprocessingInfoAnswerOrigLowercased.get("words");
			r.tokenPosAnswerOrig = preprocessingInfoAnswerOrigLowercased.get("pos");
			r.tokenLemmasAnswerOrig = preprocessingInfoAnswerOrigLowercased.get("lemmas");
			r.tokenStemsAnswerOrig = preprocessingInfoAnswerOrigLowercased.get("stems");
			
			//now with stopword removal, lowercased
			JCas a = createJCasAndRunDKProPipelineWithStopwordRemoval(r.answer);			
			HashMap<String, ArrayList<String>> preprocessingInfoAnswer = getPreprocessedTokens(a);
						
			r.jCasAnswer = a;			
			
			r.tokenWordsAnswer = preprocessingInfoAnswer.get("words");
			r.tokenPosAnswer = preprocessingInfoAnswer.get("pos");
			r.tokenLemmasAnswer = preprocessingInfoAnswer.get("lemmas");
			r.tokenStemsAnswer = preprocessingInfoAnswer.get("stems");
			
						
		}
			
	}
	
	
	
	/**
	 * returns a jcas build from the text and with the whole pipeline run on it
	 * @param text
	 * @return
	 */
	 private JCas createJCasAndRunDKProPipelineWithStopwordRemoval(String text){
		 
		    JCas jcas = null;
			try {
				jcas = JCasFactory.createJCas();
			} catch (UIMAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			jcas.setDocumentLanguage("de");			
	       
	        jcas.setDocumentText(text);
	        		
			 		
			try {
				//SimplePipeline.runPipeline(jcas, segmenter, postagger, lemmatizer, stemmer, spellchecker);
				SimplePipeline.runPipeline(jcas, segmenter, postagger, lemmatizer, stemmer, stopwordremover);
				
				
			} catch (AnalysisEngineProcessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return jcas;

			
		}
	/**
	 * returns a jcas build from the text and with the whole pipeline run on it besides stopword removal
	 * @param text
	 * @return
	 */
	 private JCas createJCasAndRunDKProPipelineNoStopwordRemoval(String text){
		 
		    JCas jcas = null;
			try {
				jcas = JCasFactory.createJCas();
			} catch (UIMAException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			jcas.setDocumentLanguage("de");			
	       
	        jcas.setDocumentText(text);
	        		
			 		
			try {
				//SimplePipeline.runPipeline(jcas, segmenter, postagger, lemmatizer, stemmer, spellchecker);
				SimplePipeline.runPipeline(jcas, segmenter, postagger, lemmatizer, stemmer);
				
				
			} catch (AnalysisEngineProcessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return jcas;

			
		}
	
	/**
	 * takes the jcas and returns a HashMap containing preprocessing info for the keys words, stems, lemmas and pos
	 * @param jcas
	 * @return
	 */
	private HashMap<String, ArrayList<String>> getPreprocessedTokens(JCas jcas){
		
		HashMap<String, ArrayList<String>> preprocessingInfo = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> tokenWords = new ArrayList<String>();
		ArrayList<String> tokenPos = new ArrayList<String>();
		ArrayList<String> tokenLemmas = new ArrayList<String>();
		ArrayList<String> tokenStems = new ArrayList<String>();
		
		
		
		Collection<Token> tokens = select(jcas, Token.class);
		
				
		for(Token t : tokens){		
							
			tokenWords.add(t.getCoveredText());
					
			POS pos = t.getPos();			
			tokenPos.add(pos.getPosValue());
			//System.out.println(t.getCoveredText() + "\t" + pos.getPosValue());			
			
			Lemma lemma = t.getLemma();
		//	if((!pos.getPosValue().startsWith("$"))||(!t.getCoveredText().equalsIgnoreCase("="))||(!t.getCoveredText().equalsIgnoreCase("-"))||(!t.getCoveredText().equalsIgnoreCase("}"))||(!t.getCoveredText().equalsIgnoreCase("{"))){ //omit punctuation and =
			tokenLemmas.add(lemma.getValue());
		//	}
			
			Stem stem = t.getStem();
		//if((!pos.getPosValue().startsWith("$"))||(!t.getCoveredText().equalsIgnoreCase("="))){ //omit punctuation and =
			tokenStems.add(stem.getValue());
		//	}
		}
		
		
		preprocessingInfo.put("words", tokenWords);
		preprocessingInfo.put("pos", tokenPos);
		preprocessingInfo.put("lemmas", tokenLemmas);
		preprocessingInfo.put("stems", tokenStems);
		
		
		return preprocessingInfo;
		

		
	}
	
   
	
		
	
	
}