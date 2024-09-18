/**
 @author: Cheng Erxi, Jiayi Wang
 */
import static org.junit.jupiter.api.Assertions.*;

import movies.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import file.MovieDB;

import java.util.ArrayList;

class MovieTriviaTest {

	// instance of movie trivia object to test
	MovieTrivia mt;
	// instance of movieDB object
	MovieDB movieDB;

	@BeforeEach
	void setUp() throws Exception {
		// initialize movie trivia object
		mt = new MovieTrivia();

		// set up movie trivia object
		mt.setUp("moviedata.txt", "movieratings.csv");

		// get instance of movieDB object from movie trivia object
		movieDB = mt.movieDB;
	}

	@Test
	void testSetUp() {
		assertEquals(6, movieDB.getActorsInfo().size(),
				"actorsInfo should contain 6 actors after reading moviedata.txt.");
		assertEquals(7, movieDB.getMoviesInfo().size(),
				"moviesInfo should contain 7 movies after reading movieratings.csv.");

		assertEquals("meryl streep", movieDB.getActorsInfo().get(0).getName(),
				"\"meryl streep\" should be the name of the first actor in actorsInfo.");
		assertEquals(3, movieDB.getActorsInfo().get(0).getMoviesCast().size(),
				"The first actor listed in actorsInfo should have 3 movies in their moviesCasted list.");
		assertEquals("doubt", movieDB.getActorsInfo().get(0).getMoviesCast().get(0),
				"\"doubt\" should be the name of the first movie in the moviesCasted list of the first actor listed in actorsInfo.");

		assertEquals("doubt", movieDB.getMoviesInfo().get(0).getName(),
				"\"doubt\" should be the name of the first movie in moviesInfo.");
		assertEquals(79, movieDB.getMoviesInfo().get(0).getCriticRating(),
				"The critics rating for the first movie in moviesInfo is incorrect.");
		assertEquals(78, movieDB.getMoviesInfo().get(0).getAudienceRating(),
				"The audience rating for the first movie in moviesInfo is incorrect.");
	}

	@Test
	void testInsertActor() {

		// try to insert new actor with new movies
		System.out.println("Before"+movieDB.getActorsInfo());

		mt.insertActor("test1", new String[] { "testmovie1", "testmovie2" }, movieDB.getActorsInfo());
		assertEquals(7, movieDB.getActorsInfo().size(),
				"After inserting an actor, the size of actorsInfo should have increased by 1.");
		assertEquals("test1", movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getName(),
				"After inserting actor \"test1\", the name of the last actor in actorsInfo should be \"test1\".");
		assertEquals(2, movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().size(),
				"Actor \"test1\" should have 2 movies in their moviesCasted list.");
		assertEquals("testmovie1",
				movieDB.getActorsInfo().get(movieDB.getActorsInfo().size() - 1).getMoviesCast().get(0),
				"\"testmovie1\" should be the first movie in test1's moviesCasted list.");

		// try to insert existing actor with new movies
		mt.insertActor("   Meryl STReep      ", new String[] { "   DOUBT      ", "     Something New     " },
				movieDB.getActorsInfo());
		assertEquals(7, movieDB.getActorsInfo().size(),
				"Since \"meryl streep\" is already in actorsInfo, inserting \"   Meryl STReep      \" again should not increase the size of actorsInfo.");

		// look up and inspect movies for existing actor
		// note, this requires the use of properly implemented selectWhereActorIs method
		// you can comment out these two lines until you have a selectWhereActorIs
		// method
		assertEquals(4, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"After inserting Meryl Streep again with 2 movies, only one of which is not on the list yet, the number of movies \"meryl streep\" appeared in should be 4.");
		//System.out.println(movieDB.getActorsInfo());
		assertTrue(mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).contains("something new"),
				"After inserting Meryl Streep again with a new Movie \"     Something New     \", \"somenthing new\" should appear as one of the movies she has appeared in.");
		System.out.println("After"+movieDB.getActorsInfo());
		// TODO add additional test case scenarios
		// Try to insert new movie twice(same) in one attempt
		mt.insertActor("meryl streep", new String[]{"Same movie","Same movie"},movieDB.getActorsInfo());
		assertEquals(5, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"After inserting Meryl Streep with first movies, the another one will be considered in the list already, the number of movies \"meryl streep\" appeared in should be 5, which already increase 1.");

		//Try to insert a new movie to a existing actor, while this movie is already contained in other existing actor
		mt.insertActor("meryl streep", new String[]{"leap year"},movieDB.getActorsInfo());
		assertEquals(6, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"After inserting Meryl Streep with the existing movies of other actor, however this movie is new for \"meryl streep\", the count will still increase by 1, become 6.");
		assertEquals("leap year",
				movieDB.getActorsInfo().get(0).getMoviesCast().get(5),
				"\"leap year\" should be the first movie in test1's moviesCasted list.");
	}


	@Test
	void testInsertRating() {

		// try to insert new ratings for new movie
		mt.insertRating("testmovie", new int[] { 79, 80 }, movieDB.getMoviesInfo());
		System.out.println("Rating Session:"+movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"After inserting ratings for a movie that is not in moviesInfo yet, the size of moviesInfo should increase by 1.");
		assertEquals("testmovie", movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getName(),
				"After inserting a rating for \"testmovie\", the name of the last movie in moviessInfo should be \"testmovie\".");
		assertEquals(79, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getCriticRating(),
				"The critics rating for \"testmovie\" is incorrect.");
		assertEquals(80, movieDB.getMoviesInfo().get(movieDB.getMoviesInfo().size() - 1).getAudienceRating(),
				"The audience rating for \"testmovie\" is incorrect.");

		// try to insert new ratings for existing movie
		mt.insertRating("doubt", new int[] { 100, 100 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Since \"doubt\" is already in moviesInfo, inserting ratings for it should not increase the size of moviesInfo.");

		// look up and inspect movies based on newly inserted ratings
		// note, this requires the use of properly implemented selectWhereRatingIs
		// method
		// you can comment out these two lines until you have a selectWhereRatingIs
		// method
		assertEquals(1, mt.selectWhereRatingIs('>', 99, true, movieDB.getMoviesInfo()).size(),
				"After inserting a critic rating of 100 for \"doubt\", there should be 1 movie in moviesInfo with a critic rating greater than 99.");
		assertTrue(mt.selectWhereRatingIs('>', 99, true, movieDB.getMoviesInfo()).contains("doubt"),
				"After inserting the rating for \"doubt\", \"doubt\" should appear as a movie with critic rating greater than 99.");

		// TODO add additional test case scenarios
		System.out.println("Before the Inserte"+movieDB.getMoviesInfo().size());
		// try to insert the rating that greater than 100 and smaller than 0
		mt.insertRating("illegal_rate", new int[] { 101, 5 }, movieDB.getMoviesInfo());
		mt.insertRating("illegal_rate", new int[] { 5, -10 }, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Since this insert is illegal as rating >100 and <0, the insert will not be updated and the size of movie info still need to be 8");
		// try to insert the rating that is missing
		mt.insertRating("missing_rate", new int[] {20}, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Since the inserted rating is partially missed, the insert will not be updated and the size of movie info still need to be 8");
		// try to insert the rating that value is null
		mt.insertRating("null_rate", new int[] {,}, movieDB.getMoviesInfo());
		assertEquals(8, movieDB.getMoviesInfo().size(),
				"Since the inserted rating contain null(s), the insert will not be updated and the size of movie info still need to be 8");

	}

	@Test
	void testSelectWhereActorIs() {
		assertEquals(3, mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).size(),
				"The number of movies \"meryl streep\" has appeared in should be 3.");
		assertEquals("doubt", mt.selectWhereActorIs("meryl streep", movieDB.getActorsInfo()).get(0),
				"\"doubt\" should show up as first in the list of movies \"meryl streep\" has appeared in.");

		// TODO add additional test case scenarios
		// try to select the Non-existent actor
		assertTrue(mt.selectWhereActorIs("non exist actor", movieDB.getActorsInfo()).isEmpty(),
				"Select request will return the empty list for non-existence actor.");
		// try to select the actor that has the same spelling but have the uppercase issue
		assertEquals(3, mt.selectWhereActorIs("MERYL STREEP", movieDB.getActorsInfo()).size(),
				"The number of movies \"MERYL STREEP\" will be considered samely as \"meryl streep\" , which has appeared in should be 3.");
		assertEquals("doubt", mt.selectWhereActorIs("MERYL STREEP", movieDB.getActorsInfo()).get(0),
				"\"doubt\" should show up as first in the list of movies \"MERYL STREEP\" has appeared in.");
		// try to select the actor names with leading or trailing whitespace
		assertEquals(3, mt.selectWhereActorIs("   meryl streep   ", movieDB.getActorsInfo()).size(),
				"The number of movies \"   meryl streep   \" will be considered samely as \"meryl streep\" , which has appeared in should be 3.");
		assertEquals("doubt", mt.selectWhereActorIs("   meryl streep   ", movieDB.getActorsInfo()).get(0),
				"\"doubt\" should show up as first in the list of movies \"   meryl streep   \" has appeared in.");
	}

	@Test
	void testSelectWhereMovieIs() {
		assertEquals(2, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).size(),
				"There should be 2 actors in \"doubt\".");
		assertEquals(true, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be an actor who appeared in \"doubt\".");
		assertEquals(true, mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" should be an actor who appeared in \"doubt\".");

		// TODO add additional test case scenarios
		// try to search a non-exist movie
		assertTrue(mt.selectWhereMovieIs("non exist movie", movieDB.getActorsInfo()).isEmpty(),
				"Select a non-exist movie will return a empty list.");
		// try to select the existing movie however has the uppercase issue
		assertEquals(2, mt.selectWhereMovieIs("DOUBT", movieDB.getActorsInfo()).size(),
				"The number of movies \"DOUBT\" will be considered samely as \"doubt\" , which has appeared in should be 2.");
		assertEquals(true, mt.selectWhereMovieIs("DOUBT", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be an actor who appeared in \"DOUBT\".");
		assertEquals(true, mt.selectWhereMovieIs("DOUBT", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" should be an actor who appeared in \"DOUBT\".");
		// try to select the movie names with leading or trailing whitespace
		assertEquals(2, mt.selectWhereMovieIs("   doubt   ", movieDB.getActorsInfo()).size(),
				"The number of movies \"   doubt   \" will be considered samely as \"doubt\" , which has appeared in should be 3.");
		assertEquals(true, mt.selectWhereMovieIs("   doubt   ", movieDB.getActorsInfo()).contains("meryl streep"),
				"\"meryl streep\" should be an actor who appeared in \"   doubt   \".");
		assertEquals(true, mt.selectWhereMovieIs("   doubt   ", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" should be an actor who appeared in \"   doubt   \".");
		// try to select the existing movie, and check if it is contained in the unmatched actor
        assertFalse(mt.selectWhereMovieIs("doubt", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" should not be an actor who appeared in \"doubt\".");
	}

	@Test
	void testSelectWhereRatingIs() {
		assertEquals(6, mt.selectWhereRatingIs('>', 0, true, movieDB.getMoviesInfo()).size(),
				"There should be 6 movies where critics rating is greater than 0.");
		assertEquals(0, mt.selectWhereRatingIs('=', 65, false, movieDB.getMoviesInfo()).size(),
				"There should be no movie where audience rating is equal to 65.");
		assertEquals(2, mt.selectWhereRatingIs('<', 30, true, movieDB.getMoviesInfo()).size(),
				"There should be 2 movies where critics rating is less than 30.");

		// TODO add additional test case scenarios
		// try when the input is greater than 100
		assertTrue(mt.selectWhereRatingIs('>', 100, true, movieDB.getMoviesInfo()).isEmpty(),
				"For a input greater than 100, it should return a empty list.");
		// try when the input is less than 0
		assertTrue(mt.selectWhereRatingIs('<', 0, true, movieDB.getMoviesInfo()).isEmpty(),
				"For a input less than 0, it should return a empty list.");
		// try the comparison is not exist
		assertTrue(mt.selectWhereRatingIs('?', 50, true, movieDB.getMoviesInfo()).isEmpty(),
				"For the situation that the comparison is not exist, it should return a empty list.");
		// try the comparison after an new insert
		mt.insertRating("testmovie", new int[] { 79, 80 }, movieDB.getMoviesInfo());
		assertEquals(7, mt.selectWhereRatingIs('>', 0, true, movieDB.getMoviesInfo()).size(),
				"There should be 7 movies that critic rating is greater than 0 after an new insert");
	}

	@Test
	void testGetCoActors() {
		assertEquals(2, mt.getCoActors("meryl streep", movieDB.getActorsInfo()).size(),
				"\"meryl streep\" should have 2 co-actors.");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" was a co-actor of \"meryl streep\".");
		assertTrue(mt.getCoActors("meryl streep", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" was a co-actor of \"meryl streep\".");

		// TODO add additional test case scenarios
		// try to select a non-existing actor
		assertTrue(mt.getCoActors("non exist actor", movieDB.getActorsInfo()).isEmpty(),
				"For the situation that the actor is not exist, it should return a empty list.");
		// try to select a actor with correct spelling but in uppercase
		assertEquals(2, mt.getCoActors("MERYL STREEP", movieDB.getActorsInfo()).size(),
				"Select \"MERYL STREEP\" will be considered samely as \"meryl streep\" , which has appeared in should be 2");
		assertTrue(mt.getCoActors("MERYL STREEP", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" was a co-actor of \"meryl streep\", the uppercase input also considered as the same.");
		assertTrue(mt.getCoActors("MERYL STREEP", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" was a co-actor of \"meryl streep\".");
		// try to select the actor with leading or trailing whitespace
		assertEquals(2, mt.getCoActors("           meryl streep          ", movieDB.getActorsInfo()).size(),
				"Select \"           meryl streep          \" will be considered samely as \"meryl streep\" , which has appeared in should be 2");
		assertTrue(mt.getCoActors("           meryl streep          ", movieDB.getActorsInfo()).contains("tom hanks"),
				"\"tom hanks\" was a co-actor of \"meryl streep\", the input with leading or trailing space also considered as the same.");
		assertTrue(mt.getCoActors("           meryl streep          ", movieDB.getActorsInfo()).contains("amy adams"),
				"\"amy adams\" was a co-actor of \"meryl streep\".");
	}

	@Test
	void testGetCommonMovie() {
		assertEquals(1, mt.getCommonMovie("meryl streep", "tom hanks", movieDB.getActorsInfo()).size(),
				"\"tom hanks\" and \"meryl streep\" should have 1 movie in common.");
		assertTrue(mt.getCommonMovie("meryl streep", "tom hanks", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"tom hanks\" and \"meryl streep\".");

		// TODO add additional test case scenarios
		// try to select while one of the actor is non-exist
		assertTrue(mt.getCommonMovie("non exist actor", "tom hanks", movieDB.getActorsInfo()).isEmpty(),
				"A empty list will be returned if one of the actor is non-exist");
		// try to select actors with correct spelling but in uppercase
		assertEquals(1, mt.getCommonMovie("MERYL STREEP", "tom hANKS", movieDB.getActorsInfo()).size(),
				"\"tom hanks\" and \"meryl streep\" should have 1 movie in common, while the uppercase will considered the same");
		assertTrue(mt.getCommonMovie("MERYL STREEP", "tom hANKS", movieDB.getActorsInfo()).contains("the post"),
				"\"the post\" should be a common movie between \"tom hanks\" and \"meryl streep\", while the uppercase will considered the same.");
		// try to select in while actor1 and actor2 are the same actor
		ArrayList<String> list = new ArrayList<>();
		list.add("doubt");
		list.add("sophie's choice");
		list.add("the post");
		assertEquals(list, mt.getCommonMovie("meryl streep", "meryl streep", movieDB.getActorsInfo()),
				"Actor1 and actor2 can be the same, in this case, return an ArrayList of movie names that this actor was cast in");
	}

	@Test
	void testGoodMovies() {
		assertEquals(3, mt.goodMovies(movieDB.getMoviesInfo()).size(),
				"There should be 3 movies that are considered good movies, movies with both critics and audience rating that are greater than or equal to 85.");
		assertTrue(mt.goodMovies(movieDB.getMoviesInfo()).contains("jaws"),
				"\"jaws\" should be considered a good movie, since it's critics and audience ratings are both greater than or equal to 85.");

		// TODO add additional test case scenarios
		// try to test while the input movie name is in uppercase
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("Rocky II"),
				"The string in goodMovies are stored in lowercase, therefore \"Rocky II\" which contain uppercase will considered as false");
		// try to test while input a movie that does not meet the requirement to be considered as good movie
		assertFalse(mt.goodMovies(movieDB.getMoviesInfo()).contains("arrival"),
				"Arrival does not meet the requirement to be considered as a good movie.");
		// try to test the number of good movie after insert a new one
		mt.insertRating("new movie", new int[] { 100, 100 }, movieDB.getMoviesInfo());
		assertEquals(4, mt.goodMovies(movieDB.getMoviesInfo()).size(),
				"After insert a new movie that meet the requirement, the NO. of good movie will increase by 1.");
	}

	@Test
	void testGetCommonActors() {
		assertEquals(1, mt.getCommonActors("doubt", "the post", movieDB.getActorsInfo()).size(),
				"There should be one actor that appeared in both \"doubt\" and \"the post\".");
		assertTrue(mt.getCommonActors("doubt", "the post", movieDB.getActorsInfo()).contains("meryl streep"),
				"The actor that appeared in both \"doubt\" and \"the post\" should be \"meryl streep\".");

		// TODO add additional test case scenarios
		// try to input the non-exist movie
		assertEquals(0, mt.getCommonActors("non exist", "the post", movieDB.getActorsInfo()).size(),
				"The output shall be 0 as there is a non exist movie in the input.");
		// try to test while the input movie name is in uppercase
		assertEquals(1, mt.getCommonActors("DoUBt", "THe PoSt", movieDB.getActorsInfo()).size(),
				"There should be one actor that appeared in both \"doubt\" and \"the post\" since the uppercase input should be considered same as lowercase.");
		assertTrue(mt.getCommonActors("DoUBt", "THe PoSt", movieDB.getActorsInfo()).contains("meryl streep"),
				"The actor that appeared in both \"doubt\" and \"the post\" should be \"meryl streep\".");
		// try to test the input with leading or trailing whitespace
		assertEquals(1, mt.getCommonActors("         doubt   ", "     the post   ", movieDB.getActorsInfo()).size(),
				"There should be one actor that appeared in both \"doubt\" and \"the post\".");
		assertTrue(mt.getCommonActors("    doubt   ", "   the post       ", movieDB.getActorsInfo()).contains("meryl streep"),
				"The actor that appeared in both \"doubt\" and \"the post\" should be \"meryl streep\".");
		// try to test while the input movie are the same
		ArrayList<String> list_2 = new ArrayList<>();
		list_2.add("meryl streep");
		list_2.add("amy adams");
		assertEquals(list_2,mt.getCommonActors("doubt", "doubt", movieDB.getActorsInfo()),
				"movie1 and movie2 can be the same. In this case, return an ArrayList of actor names that were cast in this movie.");
	}

	@Test
	void testGetMean() {
		//fail(); // This automatically causes the test to fail. Remove this line when you are ready to write your own tests.


		// TODO add ALL test case scenarios!
		// Test the case by input all of the movie in the DB (multiple input test)
		double[] mean = mt.getMean(movieDB.getMoviesInfo());
		assertArrayEquals(new double[]{67.85714, 65.71428}, mean, 0.001,
				"The average critic/audience score of all movie in DB is 65.85714 and 65.71428, the output should be matched.");

		// Test the case that the input is an empty list (empty input test)
		ArrayList<Movie> list_3 = new ArrayList<>();
		double[] mean_2 = mt.getMean(list_3);
		assertArrayEquals(new double[]{0, 0}, mean_2, 0.001,
				"For the empty input, the output should be 0.");

		// Test the case that the input is a single movie (single input test)
		list_3.add(movieDB.getMoviesInfo().get(0));
		double[] mean_3 = mt.getMean(list_3);
		assertArrayEquals(new double[]{79, 78}, mean_3, 0.001,
				"If the input is a single movie, the output should be exactly the score of the this movie.");

		// Test the case that after a new insert	(update test)
		mt.insertRating("testmovie", new int[] { 79, 80 }, movieDB.getMoviesInfo());
		double[] mean_4 = mt.getMean(movieDB.getMoviesInfo());
		assertArrayEquals(new double[]{69.25, 67.5}, mean_4, 0.001,
				"After new movie insert, the mean score are expected to update.");
	}
}
