package de.hft.gradingassistant.nlp;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.util.JCasUtil.select;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import de.hft.gradingassistant.record.Record;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Stem;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.snowball.SnowballStemmer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import de.tudarmstadt.ukp.dkpro.core.stopwordremover.StopWordRemover;

/**
 * preprocesses a list of Records and enriches them with information on tokens, tags, stems, lemmas and syntax tree
 * uses dkpro to process records using the whole preprocessing pipeline at a time
 * @author kiefer, Verena Meyer
 *
 */

public class Preprocessor {

	private String question;
	private String referenceAnswer;

	private AnalysisEngine segmenter;
	private AnalysisEngine postagger;
	private AnalysisEngine lemmatizer;
	private AnalysisEngine stemmer;

	InputStream stopwordsSource;
	Path stopwordsDestination;
	//private AnalysisEngine spellChecker; //NOTE: unused
	//JCAS

	/**
	 * Constructor
	 * Uses DKPro to process records using the whole preprocessing pipeline at a time.
	 * Initializes the preprocessing engines.
	 * @param languageFlag
	 * @throws ResourceInitializationException
	 */
	public Preprocessor(String languageFlag) throws ResourceInitializationException{
		segmenter = createEngine(StanfordSegmenter.class, StanfordSegmenter.PARAM_LANGUAGE, languageFlag);	
		postagger = createEngine(StanfordPosTagger.class,  StanfordPosTagger.PARAM_LANGUAGE, languageFlag);		  
		lemmatizer = createEngine(StanfordLemmatizer.class);	//NOTE: Wieso nimmst du kein language_flag?
		stemmer = createEngine(SnowballStemmer.class, SnowballStemmer.PARAM_LANGUAGE, languageFlag, SnowballStemmer.PARAM_LOWER_CASE, true);	
	}

	/**
	 * Preprocesses the question and reference answers
	 * @param records
	 */
	public void preprocessTextWithDKProPipeline(ArrayList<Record> records){
		String oldQuestion = null;
		JCas questionInJCas = null;
		JCas ra = null;
		JCas jcas = null;

		stopwordsSource = this.getClass().getResourceAsStream("stopwords_basic_" + records.get(1).getRecord().get("languageFlag") + ".txt");

		File temp = null;
		try {
			temp = File.createTempFile("stopwords", ".txt");
			//TODO: fix encoding
			stopwordsDestination = Paths.get(temp.getAbsolutePath());


		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		try {
			jcas = JCasFactory.createJCas();

			HashMap<String, ArrayList<String>> preprocessingInfoQuestion = null;
			HashMap<String, ArrayList<String>> preprocessingInfoReferenceAnswer = null;
			ArrayList<String> wordsInQuestion = null;
			ArrayList<String> posInQuestion	= null;
			ArrayList<String> lemmasInQuestion = null;
			ArrayList<String> stemsInQuestion = null;

			ArrayList<String> wordsInRefAnswer = null;
			ArrayList<String> posInRefAnswer = null;
			ArrayList<String> lemmasInRefAnswer = null;
			ArrayList<String> stemsInRefAnswer = null;
			AnalysisEngine stopwordRemover = null;


			for(Record r : records){
				question = r.getRecord().get("question");

				//To not do the same stuff over and over again without any differences in the result
				if(!question.equals(oldQuestion) || oldQuestion == null){
					stopwordRemover = null;

					referenceAnswer = r.getRecord().get("refAnswer");
					jcas.reset();
					//create a new JCas and add the question
					questionInJCas = createJCasAndRunDKProPipeline(jcas, question, records.get(1).getRecord().get("languageFlag"), null);

					//gather preprocessing information
					//prepare stopword removal (remove normal stopwords (snowball) and all words, already mentioned in the question
					//get all words in the question and write them to finalListOfStopwords
					preprocessingInfoQuestion = getPreprocessedTokens(questionInJCas);
					wordsInQuestion = preprocessingInfoQuestion.get("words");
					posInQuestion	= preprocessingInfoQuestion.get("pos");
					lemmasInQuestion = preprocessingInfoQuestion.get("lemmas");
					stemsInQuestion = preprocessingInfoQuestion.get("stems");

					//Copy one file and replace if the file already exists
					try {
						Files.copy(stopwordsSource, stopwordsDestination, StandardCopyOption.REPLACE_EXISTING);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					//Method to build stopword list
					buildStopwordList(wordsInQuestion, records, temp);

					//then create the StopWordRemover engine
					try {
						stopwordRemover = createEngine(StopWordRemover.class, StopWordRemover.PARAM_MODEL_LOCATION, temp);
					} catch (ResourceInitializationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("Problem creating the stopword remover.");
					}

					jcas.reset();
					//gather preprocessing info for all reference answers
					ra = createJCasAndRunDKProPipeline(jcas, referenceAnswer.replaceAll("[^a-zA-Zäüö0123456789ß ]", ""), records.get(1).getRecord().get("languageFlag"), stopwordRemover);			
					preprocessingInfoReferenceAnswer = getPreprocessedTokens(ra);

					wordsInRefAnswer = preprocessingInfoReferenceAnswer.get("words"); 
					posInRefAnswer	= preprocessingInfoReferenceAnswer.get("pos");
					lemmasInRefAnswer = preprocessingInfoReferenceAnswer.get("lemmas");
					stemsInRefAnswer = preprocessingInfoReferenceAnswer.get("stems");	
				}
				oldQuestion = question;
				//enrich each record with the new representations of question and reference answers					
				r.setTokenWordsQuestion(wordsInQuestion);
				r.setTokenPosQuestion(posInQuestion);
				r.setTokenLemmasQuestion(lemmasInQuestion);
				r.setTokenStemsQuestion(stemsInQuestion);

				r.setTokenWordsReferenceAnswer(wordsInRefAnswer);
				r.setTokenPosReferenceAnswer(posInRefAnswer);
				r.setTokenLemmasReferenceAnswer(lemmasInRefAnswer);
				r.setTokenStemsReferenceAnswer(stemsInRefAnswer);

				jcas.reset();
				//enrich each record with the new representations of the student answer, with and without stopword removal
				JCas aOrig = createJCasAndRunDKProPipeline(jcas, r.getRecord().get("answer"), records.get(1).getRecord().get("languageFlag"), null);				
				HashMap<String, ArrayList<String>> preprocessingInfoAnswerOrig = getPreprocessedTokens(aOrig);			

				r.setTokenWordsAnswerOrig(preprocessingInfoAnswerOrig.get("words")); //The highlighted answer in the end shall contain case information

				jcas.reset();
				//lowercased version of the student answer
				JCas aOrigLowercased = createJCasAndRunDKProPipeline(jcas, r.getRecord().get("answer"), records.get(1).getRecord().get("languageFlag"), null);				
				HashMap<String, ArrayList<String>> preprocessingInfoAnswerOrigLowercased = getPreprocessedTokens(aOrigLowercased);			

				r.setTokenPosAnswerOrig(preprocessingInfoAnswerOrigLowercased.get("pos"));
				r.setTokenLemmasAnswerOrig(preprocessingInfoAnswerOrigLowercased.get("lemmas"));
				r.setTokenStemsAnswerOrig(preprocessingInfoAnswerOrigLowercased.get("stems"));

				jcas.reset();
				//now with stopword removal, lowercased
				JCas a = createJCasAndRunDKProPipeline(jcas, r.getRecord().get("answer"), records.get(1).getRecord().get("languageFlag"), stopwordRemover);			
				HashMap<String, ArrayList<String>> preprocessingInfoAnswer = getPreprocessedTokens(a);

				r.setTokenWordsAnswer(preprocessingInfoAnswer.get("words"));
				r.setTokenPosAnswer(preprocessingInfoAnswer.get("pos"));
				r.setTokenLemmasAnswer(preprocessingInfoAnswer.get("lemmas"));
				r.setTokenStemsAnswer(preprocessingInfoAnswer.get("stems"));
			
				temp.delete();
			}
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * To build a stopword list from file and the words which occur in the question
	 * @param wordsInQuestion
	 * @param records
	 * @author Verena Meyer
	 */
	private void buildStopwordList(ArrayList<String> wordsInQuestion, ArrayList<Record> records, File temp) {

		try {
			FileWriter writer = new FileWriter(temp, true);

			//Appends words used in question to stopword file
			for(String word : wordsInQuestion){
				writer.append(word.toLowerCase() + "\n");
				writer.flush();
			}
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * returns a jcas build from the text and with the whole pipeline run on it with or without stopword removal
	 * @param text
	 * @return
	 * @author Verena Meyer
	 * @param jcas 
	 */
	private JCas createJCasAndRunDKProPipeline(JCas jcas, String text, String languageFlag, AnalysisEngine stopwordRemover){
		jcas.setDocumentLanguage(languageFlag);			
		jcas.setDocumentText(text);

		try {
			if(stopwordRemover != null){
				SimplePipeline.runPipeline(jcas, segmenter, postagger, lemmatizer, stemmer, stopwordRemover);
			} else {
				SimplePipeline.runPipeline(jcas, segmenter, postagger, lemmatizer, stemmer);
			}
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
			try{
			tokenWords.add(t.getCoveredText());

			POS pos = t.getPos();			
			tokenPos.add(pos.getPosValue());

			Lemma lemma = t.getLemma();
			tokenLemmas.add(lemma.getValue());
			//	}

			Stem stem = t.getStem();
			tokenStems.add(stem.getValue());
			//	}
			} catch(NullPointerException ne){
				//do nothing
			}
		}

		preprocessingInfo.put("words", tokenWords);
		preprocessingInfo.put("pos", tokenPos);
		preprocessingInfo.put("lemmas", tokenLemmas);
		preprocessingInfo.put("stems", tokenStems);

		return preprocessingInfo;
	}
}