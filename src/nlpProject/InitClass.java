/*
 * This is the initial class. The main method for the project exists here.
 */

package nlpProject;

import java.util.Scanner;

import misc.StringProcessing;

public class InitClass {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome! This is Mini Watson.\n\nPlease ask a question. Type 'q' when finished.");

		DomainInference infer = new DomainInference();

		StringProcessing process = new StringProcessing();

		MovieDomain movie = new MovieDomain();
		MusicDomain music = new MusicDomain();
		GeographyDomain geography = new GeographyDomain();

		while(true) {

			System.out.println("<QUERY>\n");
			String question = sc.nextLine();
			if(question.equals("q")) {
				return;
			}
			
			/*
			 * Infer the question domain and take appropriate action.
			 */

			String domain = infer.getDomain(question);
			switch (domain) {
			case "MUSIC":
				question = process.replaceAlbumNameInQuestion(question);
				question = process.replaceTrackNameInQuestion(question);
				question = process.preProcess(question);
				music.buildQuery(question);
				break;
			case "MOVIE":
				question = process.replaceMovieNameInQuestion(question);
				question = process.preProcess(question);
				movie.buildQuery(question);
				break;
			case "GEOGRAPHY":
				question = process.preProcess(question);
				geography.buildQuery(question);
				break;
			}
		}
	}
}