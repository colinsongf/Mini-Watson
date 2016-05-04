package nlpProject;

import java.util.HashMap;
import java.util.List;

import database.AccessDatabase;
import misc.StringProcessing;
import stanfordCoreNlp.StanfordParser;

public class GeographyDomain {

	private StanfordParser parser;
	private StringProcessing process;
	private HashMap<String, Integer> posTags;
	private HashMap<String, String> tagWordPairs;
	private AccessDatabase db;

	public GeographyDomain() {
		db = new AccessDatabase();

		process = new StringProcessing();

		parser = new StanfordParser();

		tagWordPairs = new HashMap<String, String>();

		posTags = new HashMap<String, Integer>();
		posTags.put("VBZ1,NNP2,DT3,NN4,IN5,NNP6", 1);
		posTags.put("VBZ1,NNP2,IN3,NNP4", 2);
		posTags.put("WP1,VBZ2,DT3,NN4,IN5,NNP6", 3);
		posTags.put("WRB1,VBZ2,NNP3", 4);		
	}

	/*
	 * SQL Query creation the Geography Domain
	 */
	public String getQueryCountryCapital(String ctry, String cptl) {
		return "SELECT count(*)"
				+ " FROM countries ctry, cities city, capitals cap"
				+ " WHERE cap.CountryId = ctry.Id AND cap.CityId = city.Id AND city.Name LIKE '%"+cptl+"%' AND ctry.Name LIKE '%"+ctry+"%'";
	}

	public String getQueryCountryInContinent(String ctry, String ctnt) {
		return "SELECT count(*) "
				+ "FROM CountryContinents ctryCtnt, Continents ctnt, Countries ctry "
				+ "WHERE ctryCtnt.CountryId = ctry.Id AND ctryCtnt.ContinentId = ctnt.Id AND "
				+ "ctry.Name LIKE '%"+ctry+"%' AND ctnt.Continent LIKE '%"+ctnt+"%'";
	}

	public String getQueryCapitalOfCountry(String ctry) {
		return "SELECT city.Name "
				+ "FROM Cities city, Countries ctry, Capitals cptl "
				+ "WHERE ctry.Id = cptl.CountryId AND city.Id = cptl.CityId AND ctry.Name LIKE '%"+ctry+"%'";
	}

	public String getQueryWhereIsCountry(String ctry) {
		return "SELECT ctnt.Continent "
				+ "FROM CountryContinents ctryCtnt, Continents ctnt, Countries ctry "
				+ "WHERE ctryCtnt.CountryId = ctry.Id AND ctryCtnt.ContinentId = ctnt.Id AND ctry.Name LIKE '%"+ctry+"%'";
	}
	
	public String getQueryWhereIsCity(String city) {
		return "SELECT ctry.Name "
				+ "FROM Cities city, Countries ctry, Capitals cptl "
				+ "WHERE ctry.Id = cptl.CountryId AND city.Id = cptl.CityId AND city.Name LIKE '%"+city+"%'";
	}

	
	/*
	 *Lambda reductions for the geography domain. The appropriate methods for SQL query creation are called here after 
	 *analyzing the terminals. 
	 */
	public void buildQuery(String question) {
		String questionPosTags = parser.getPosTags(question);
		try {
			if(!posTags.containsKey(questionPosTags)) {
				throw new Exception();
			}
			//question = question.replace("qwertys", "");
			question = question.substring(0, question.length()-1);
			int qType = posTags.get(questionPosTags);

			String[] wordsArray = question.split(" ");
			String[] posTagsArray = questionPosTags.split(",");

			HashMap<String, String> tagWordPairs = new HashMap<String, String>();
			for(int i=0;i<wordsArray.length;i++) {
				tagWordPairs.put(posTagsArray[i], wordsArray[i]);
			}
			
			String query="";
			String query2="";
			switch(qType) {
			case 1:
				String cptl = process.postProcess(tagWordPairs.get("NNP2"));
				String ctry = process.postProcess(tagWordPairs.get("NNP6"));
				query = getQueryCountryCapital(ctry, cptl);
				
				break;


			case 2:
				String cityOrCtry = process.postProcess(tagWordPairs.get("NNP2"));
				String ctryOrCtnt = process.postProcess(tagWordPairs.get("NNP4"));
				query = getQueryCountryInContinent(cityOrCtry, ctryOrCtnt);
				query2 = getQueryCountryCapital(ctryOrCtnt, cityOrCtry);
				break;


			case 3:
				String ctryForCapital = process.postProcess(tagWordPairs.get("NNP6"));
				query = getQueryCapitalOfCountry(ctryForCapital);
				break;

			case 4:
				String ctryForContinent = process.postProcess(tagWordPairs.get("NNP3"));
				query = getQueryWhereIsCountry(ctryForContinent);
				query2 = getQueryWhereIsCity(ctryForContinent);
				break;
			}
			
			System.out.println("<SQL>\n"+query);

			System.out.println("<ANSWER>\n");
			
			if(qType==1) {
				String ans = db.runGeographyYesNoQuery(query);
				int ansInt = Integer.parseInt(ans);
				if(ansInt==0) {
					System.out.println("No");
				}
				if(ansInt>=1) {
					System.out.println("Yes");
				}
			}
			else if(qType==2) {
				String ans = db.runGeographyYesNoQuery(query);
				String ans2 = db.runGeographyYesNoQuery(query2);
				if(ans.equals("1")||ans2.equals("1")) {
					System.out.println("Yes");
				}
				else if(ans.equals("0") && ans2.equals("0")) {
					System.out.println("No");
				}
				else {
					throw new Exception();
				}
			}
			else if(qType==3) {
				List<String> result = db.runGeographyWhQuery(query);
				if(result==null || result.size()==0) {
					throw new Exception();
				}
				for(int i=0;i<result.size();i++) {
					System.out.println(result.get(i));
				}
			}
			else if(qType==4) {
				List<String> result = db.runGeographyWhQuery(query);
				List<String> result2 = db.runGeographyWhQuery(query2);
				result.addAll(result2); 
				if(result==null || result.size()==0) {
					throw new Exception();
				}
				for(int i=0;i<result.size();i++) {
					System.out.println(result.get(i));
				}
			}
			else {
				throw new Exception();
			}
		}
		catch(Exception e) {
			System.out.println("I don't know!");
		}

	}
}