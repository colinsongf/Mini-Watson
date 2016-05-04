/*
 * This file contains the logic to infer question domain.
 */

package nlpProject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.trees.Tree;
import stanfordCoreNlp.StanfordParser;

public class DomainInference {

	private HashMap<String, String> verbDomainPairs;
	private HashMap<String, String> nnDomainPairs;
	private HashMap<String, String> adjDomainPairs;
	private StanfordParser parser;
	private List<String> musicArtists;
	
	public DomainInference() {
		parser = new StanfordParser();
		verbDomainPairs = new HashMap<String, String>();
		nnDomainPairs = new HashMap<String, String>();
		adjDomainPairs = new HashMap<String, String>();
		musicArtists = new ArrayList<String>();
		musicArtists.add("Beyonce");
		musicArtists.add("Madonna");
		musicArtists.add("Gaga");
		musicArtists.add("Jackson");
		musicArtists.add("Swift");
		getKeywordsFromFile();
	}

	public void getKeywordsFromFile() {
		try {
			InputStream ir = DomainInference.class.getResourceAsStream("/resources/keywords.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(ir));
			String line;
			while((line=br.readLine())!=null) {
				String[] lineContent = line.split(",");
				if(lineContent[0].equals("Verb")) {
					verbDomainPairs.put(lineContent[1], lineContent[2]);
				}
				else if (lineContent[0].equals("CommonNoun")) {
					nnDomainPairs.put(lineContent[1], lineContent[2]);
				}
				else if (lineContent[0].equals("Adjective")) {
					adjDomainPairs.put(lineContent[1], lineContent[2]);
				}
			}
		}
		catch(FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}


	public String checkParseTree(String text, Tree parseTree) {
		
		List<String> verbs = new ArrayList<String>();
		List<String> commonNouns = new ArrayList<String>();
		List<String> adjectives = new ArrayList<String>();

		List<Tree> treeList = parseTree.subTreeList();
		for(Tree t:treeList) {
			if(t.isPreTerminal()) {
				List<TaggedWord> taggedWord= t.taggedYield();
				for(TaggedWord tw:taggedWord) {
					if(tw.tag().contains("VB")) {
						verbs.add(parser.lemmatize(tw.word()).get(0));
					}
					else if(tw.tag().equals("NN")) {
						commonNouns.add(parser.lemmatize(tw.word()).get(0));
					}
					else if(tw.tag().contains("JJ")) {
						adjectives.add(parser.lemmatize(tw.word()).get(0));
					}
				}
			}
		}
		
		for(int i=0;i<verbs.size();i++) {
			String verb = verbs.get(i);
			if(verbDomainPairs.containsKey(verb)) {
				return verbDomainPairs.get(verb);
			}
		}

		for(int i=0;i<commonNouns.size();i++) {
			String commonNoun = commonNouns.get(i);
			if(nnDomainPairs.containsKey(commonNoun)) {
				return nnDomainPairs.get(commonNoun);
			}
		}


		for(int i=0;i<adjectives.size();i++) {
			String adjective = adjectives.get(i);
			if(adjDomainPairs.containsKey(adjective)) {
				return adjDomainPairs.get(adjective);
			}
		}	

		if(parseTree.toString().contains("NNP")) {
			for(int i=0;i<musicArtists.size();i++) {
				if(text.contains(musicArtists.get(i))) {
					return "MUSIC";
				}
			}
			return "MOVIE";
		}
		else {
			return "GEOGRAPHY";
		}
	}

	public String getDomain(String text) {
		int musicScore=0, movieScore=0, geographyScore=0;
		Tree parseTree = parser.parse(text).get(0);
		List<String> nerTags = parser.ner(text);

		for(String s:nerTags) {
			if(s.equals("PERSON")||s.equals("DATE")) {
				movieScore++;
				musicScore++;
			}

			if(s.equals("LOCATION") && !nerTags.contains("PERSON")) {
				geographyScore++;
			}
		}

		if(geographyScore>musicScore && geographyScore>movieScore) {
			return "GEOGRAPHY";
		}

		else {
			return checkParseTree(text, parseTree);
		}
	}
}