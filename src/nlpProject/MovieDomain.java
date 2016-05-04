package nlpProject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.AccessDatabase;
import misc.StringProcessing;
import stanfordCoreNlp.StanfordParser;

public class MovieDomain {

	private StanfordParser parser;
	private HashMap<String, Integer> posTags;
	private HashMap<String, String> tagWordPairs;
	private AccessDatabase db;
	private StringProcessing process;

	public MovieDomain(){
		db = new AccessDatabase();
		parser = new StanfordParser();
		posTags = new HashMap<String, Integer>();
		tagWordPairs = new HashMap<String, String>();
		process = new StringProcessing();

		posTags.put("VBZ1,NNP2,DT3,NN4", 1);
		posTags.put("VBZ1,NNP2,IN3,NNP4", 2);
		posTags.put("VBD1,NNP2,VBN3,IN4,NNP5", 3);
		posTags.put("VBD1,NNP2,DT3,JJS4,NN5,IN6,CD7", 4);
		posTags.put("VBD1,NNP2,VB3,IN4,NNP5", 5);
		posTags.put("VBD1,NNP2,VB3,DT4,NN5,IN6,CD7", 6);
		posTags.put("VBD1,DT2,JJ3,NN4,VB5,DT6,NN7,IN8,CD9", 7);
		posTags.put("VBD1,DT2,NN3,IN4,NNP5,VBP6,DT7,NN8,IN9,JJS10,NN11", 8);
		posTags.put("WP1,VBD2,NNP3", 9);
		posTags.put("WP1,VBD2,DT3,NN4,IN5,JJS6,NN7,IN8,CD9", 10);
		posTags.put("WP1,VBD2,DT3,JJS4,NN5,IN6,CD7", 11);
		posTags.put("WDT1,NN2,VBD3,DT4,NN5,IN6,CD7", 12);
		posTags.put("WRB1,VBD2,NNP3,VB4,DT5,NN6,IN7,JJS8,NN9", 13);
		posTags.put("WP1,VBD2,NN3", 14);
	}
	
	
	/*
	 * SQL Query creation for the movie Domain.
	 */

	public String getQueryIsDirOrAct(String role, String name){
		return "SELECT count(*) "
				+ "FROM "+role+" R, Person P "
				+ "WHERE R."+role+"_id = P.id AND P.name LIKE '%"+name+"%'";
	}

	public String getQueryDirectorName(String movieName) {
		return "SELECT count(*) "
				+"FROM Director D, Movie M "
				+ "WHERE D.movie_id = M.id AND M.name LIKE '%"+movieName+"%'";				
	}

	public String getQueryBirthPlace(String name, String pob) {
		return "SELECT count(*) "
				+"FROM Person P "
				+ "WHERE P.name LIKE '%"+name+"' AND P.pob LIKE '%"+pob+"'";
	}

	public String getQueryMovieOscar(String movieName, String year) {
		return "SELECT count(*) "
				+"FROM Oscar O, Movie M "
				+ "WHERE O.movie_id = M.id AND M.name LIKE '%"+movieName+"%' AND o.year = '"+year+"' AND o.type LIKE 'BEST-PICTURE'";
	}

	public String getQueryActorInMovie(String movieName, String actName){
		return "SELECT count(*) "
				+"FROM Actor A, Movie M, Person P "
				+ "WHERE A.actor_id = P.id AND A.movie_id = M.id AND M.name LIKE '%"+movieName+"%' AND P.name LIKE '%"+actName+"'";
	}

	public String getQueryOscarWin(String movieName, String year) {
		return "SELECT count(*) "
				+ "FROM Oscar O, Person P "
				+ "WHERE O.person_id = P.id AND P.name LIKE '%"+movieName+"' AND O.year = '"+year+"'";
	}

	public String getQueryOscarWinMovie(String movieName, String year) {
		return "SELECT count(*) "
				+ "FROM Oscar O, Movie M "
				+ "WHERE O.movie_id = M.id AND M.name LIKE '%"+movieName+"' AND O.year = '"+year+"'";
	}

	public String getQueryNationalityOscarWin(String nationality, String year, String title) {
		return "SELECT count(*) "
				+ "FROM Oscar O, Person P "
				+ "WHERE O.person_id = P.id AND O.year = "+year+" AND P.pob LIKE '%"+nationality+"%' AND O.type = 'BEST-"+title+"'";
	}

	public String getQueryMovieWithActorOscarWin(String celebName) {
		return "SELECT count(*) "
				+ "FROM Oscar O, Actor A, Movie M, Person P "
				+ "WHERE O.movie_id = M.id AND O.movie_id = A.movie_id AND A.actor_id = P.id AND O.type = 'BEST-PICTURE' AND P.name LIKE '%"+celebName+"'";
	}

	public String getQueryDirectorOfMovie(String movieName) {
		return "SELECT P.name "
				+ "FROM Director D, Person P, Movie M "
				+ "WHERE D.director_id = P.id AND M.id = D.movie_id AND M.name LIKE '%"+movieName+"%'";
	}

	public String getQueryWhoWonBestActorOscar(String title, String year){
		return "SELECT P.name "
				+ "FROM Oscar O, Person P "
				+ "WHERE O.type = 'BEST-"+title+"' AND O.year = "+year+" AND O.person_id = P.id";
	}

	public String getQueryDirectorOfOscarWinMovie(String year){
		return "SELECT P.name "
				+ "FROM Oscar O, Person P, Director D "
				+ "WHERE O.type = 'BEST-PICTURE' AND O.year = "+year+" AND O.movie_id = D.movie_id AND D.director_id = P.id";
	}

	public String getQueryOscarWinInYear(String year, String title){
		
		return "SELECT P.name "
				+ "FROM Oscar O, Person P "
				+ "WHERE O.type = 'BEST-"+title+"' AND O.year = "+year+" AND O.person_id = P.id";
	}

	public String getQueryMovieOscarWinInYear(String year) {
		return "SELECT M.name "
				+ "FROM Oscar O, Movie M "
				+ "WHERE O.type = 'BEST-PICTURE' AND O.year = "+year+" AND O.movie_id = M.id";
	}

	public String getQueryCelebOscarWinYear(String title, String celebName) {
		return "SELECT O.year "
				+ "FROM Oscar O, Person P "
				+ "WHERE O.person_id = P.id  AND O.type = 'BEST-"+title+"' AND P.name LIKE '%"+celebName+"'";
	}

	public String getQueryMovieOscarWinYear(String movieName){
		return "SELECT O.year "
				+ "FROM Oscar O, Movie M "
				+ "WHERE O.movie_id = M.id  AND O.type = 'BEST-PICTURE' AND M.name LIKE '%"+movieName+"'";
	}
	
	
	/*
	 *Lambda reductions for the movie domain. The appropriate methods for SQL query creation are called here after 
	 *analyzing the terminals. 
	 */

	public void buildQuery(String question){
		String quesPostTags = parser.getPosTags(question);
		try{
			if(!posTags.containsKey(quesPostTags)){
				throw new Exception();
			}
			question = question.substring(0, question.length()-1);
			int qType = posTags.get(quesPostTags);
			String[] wordsArray = question.split(" ");
			String[] posTagsArray = quesPostTags.split(",");

			for(int i=0;i<wordsArray.length;i++) {
				tagWordPairs.put(posTagsArray[i], wordsArray[i]);
			}
			Map<String, String> countryMap = new HashMap<String, String>();
			countryMap.put("French", "France");
			countryMap.put("German", "Germany");
			countryMap.put("British", "UK");
			countryMap.put("Italian", "Italy");
			countryMap.put("American", "USA");

			Map<String, String> titleMap = new HashMap<String, String>();
			titleMap.put("actor", "ACTOR");
			titleMap.put("actress", "ACTRESS");
			titleMap.put("director", "DIRECTOR");

			String query="";
			String query2="";
			String role = "";
			String celebName = "";
			String movieName = "";
			String pob = "";
			String year = "";
			String title = "";
			
			switch(qType) {
			case 1:				
				role = tagWordPairs.get("NN4");
				celebName = process.postProcess(tagWordPairs.get("NNP2"));
				query = getQueryIsDirOrAct(role, celebName); 
				break;
			case 2:
				movieName = process.movieNamesProcessedReverse.get(tagWordPairs.get("NNP2"));
				movieName = movieName.replaceAll("['!?.,:-]", "%");
				query = getQueryDirectorName(movieName);
				break;
			case 3:
				celebName = process.postProcess(tagWordPairs.get("NNP2"));
				pob = tagWordPairs.get("NNP5");
				query = getQueryBirthPlace(celebName, pob);
				break;
			case 4:
				movieName = process.movieNamesProcessedReverse.get(tagWordPairs.get("NNP2"));
				System.out.println(movieName);
				movieName = movieName.replaceAll("['!?.,:-]", "%");
				year = tagWordPairs.get("CD7");
				query = getQueryMovieOscar(movieName, year);
				break;
			case 5:
				movieName = process.movieNamesProcessedReverse.get(tagWordPairs.get("NNP5"));
				movieName = movieName.replaceAll("['!?.,:-]", "%");
				celebName = process.postProcess(tagWordPairs.get("NNP2"));
				query = getQueryActorInMovie(movieName, celebName);
				break;
			case 6:
				String name = process.postProcess(tagWordPairs.get("NNP2"));
				year = tagWordPairs.get("CD7");
				query = getQueryOscarWin(name, year);
				break;
			case 7:
				year = tagWordPairs.get("CD9");
				pob = countryMap.get(tagWordPairs.get("JJ3"));
				title = titleMap.get(tagWordPairs.get("NN4"));
				query = getQueryNationalityOscarWin(pob, year, title);
				break;
			case 8:
				celebName = process.postProcess(tagWordPairs.get("NNP5"));
				query = getQueryMovieWithActorOscarWin(celebName);
				break;
			case 9:
				movieName = process.movieNamesProcessedReverse.get(tagWordPairs.get("NNP3"));
				movieName = movieName.replaceAll("['!?.,:-]", "%");
				query = getQueryDirectorOfMovie(movieName);
				System.out.println(query);
				break;
			case 10:
				title = titleMap.get(tagWordPairs.get("NN7"));
				year = tagWordPairs.get("CD9");
				query = getQueryWhoWonBestActorOscar(title, year);
				break;
			case 11:
				year = tagWordPairs.get("CD7");
				query = getQueryDirectorOfOscarWinMovie(year);
				break;
			case 12:
				year = tagWordPairs.get("CD7");
				title = titleMap.get(tagWordPairs.get("NN2"));
				if(tagWordPairs.get("NN2").equals("movie")){
					query = getQueryMovieOscarWinInYear(year);
				}else{
					query = getQueryOscarWinInYear(year, title);
				}
				break;
			case 13:
				title = titleMap.get(tagWordPairs.get("NN9"));
				celebName = process.postProcess(tagWordPairs.get("NNP3"));
				query = getQueryCelebOscarWinYear(title, celebName);
				query2 = getQueryMovieOscarWinYear(celebName);
				break;
			case 14:
				movieName = process.movieNamesProcessedReverse.get(tagWordPairs.get("NN3"));
				movieName = movieName.replaceAll("['!?.,:-]", "%");
				query = getQueryDirectorOfMovie(movieName);
				break;
			}
			
			
			System.out.println("<SQL>\n"+query);
			
			System.out.println("<ANSWER>\n");
			
			if(qType <= 5){
				String ans = db.runMovieYesNoQuery(query);

				if(Integer.parseInt(ans) == 0){
					System.out.println("No");
				}
				if(Integer.parseInt(ans) >= 1){
					System.out.println("Yes");
				}
			}
			else if(qType == 6){
				String ans1 = db.runMovieYesNoQuery(query);
				if(Integer.parseInt(ans1) == 0){
					System.out.println("No");
				}
				if(Integer.parseInt(ans1) >= 1){
					System.out.println("Yes");
				}
			}
			else if(qType == 7 || qType == 8){
				String ans = db.runMovieYesNoQuery(query);
				if(Integer.parseInt(ans) == 0){
					System.out.println("No");
				}
				if(Integer.parseInt(ans) >= 1){
					System.out.println("Yes");
				}
			}
			else if(qType == 9 || qType == 10 || qType == 11 || qType == 14){
				List<String> result = db.runMovieWhQuery(query);
				if(result==null || result.size()==0) {
					throw new Exception();
				}
				for(int i=0;i<result.size();i++) {
					System.out.println(result.get(i));
				} 
			}
			else if(qType==12) {
				List<String> result = db.runMovieWhQuery(query);
				if(result != null){
					for(int i = 0; i < result.size(); i++){
						System.out.println(result.get(i));
					}
				}
			}
			else if(qType == 13){
				List<String> result = db.runMovieWhQuery(query);
				List<String> result2 = db.runMovieWhQuery(query2);
				if(result != null){
					for(int i = 0; i < result.size(); i++){
						System.out.println(result.get(i));
					}
				}
				if(result2 != null){
					for(int i = 0; i < result2.size(); i++){
						System.out.println(result2.get(i));
					}
				}
				
			}
			else {
				
				throw new Exception();
			}
		}

		catch(Exception e){
			System.out.println("I dont know");
		}
	}
}