package stanfordCoreNlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class StanfordParser {

	public String getPosTags(String text) {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);
		List<CoreLabel> tokens = document.get(TokensAnnotation.class);

		int num=1;
		String result="";
		for (CoreLabel token : tokens) {

			// this is the text of the token
			String pos = token.get(PartOfSpeechAnnotation.class);
			if(!pos.equals(".")) {
				if(result.length()==0)  {
					result = pos+""+num;
				}
				else {
					result = result +","+pos+""+num;
				}
				num = num + 1;
			}
		}

		return result;
	}

	public List<String> lemmatize(String text) {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);
		List<CoreLabel> tokens = document.get(TokensAnnotation.class);

		List<String> result = new ArrayList<String>();
		for (CoreLabel token : tokens) {
			// this is the text of the token
			String lemma = token.get(LemmaAnnotation.class);
			result.add(lemma);
		}
		return result;
	}


	public List<String> ner(String text) {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);
		List<CoreLabel> tokens = document.get(TokensAnnotation.class);

		List<String> result = new ArrayList<String>();
		for (CoreLabel token : tokens) {
			// this is the text of the token
			String nerTag = token.get(NamedEntityTagAnnotation.class);
			result.add(nerTag);
		}
		return result;
	}


	public List<Tree> parse(String text) {
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		List<Tree> result = new ArrayList<Tree>();
		for (CoreMap sentence : sentences) {
			Tree tree = sentence.get(TreeAnnotation.class);
			result.add(tree);
		}
		return result;
	}
}
