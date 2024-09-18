import java.util.ArrayList;

import file.MovieDB;
import movies.Actor;
import movies.Movie;

/**
 * Movie trivia class providing different methods for querying and updating a movie database.
 * @author Cheng Erxi & Jiayi Wang
 */
public class MovieTrivia {

	/**
	 * Create instance of movie database
	 */
	MovieDB movieDB = new MovieDB();


	public static void main(String[] args) {

		//create instance of movie trivia class
		MovieTrivia mt = new MovieTrivia();

		//setup movie trivia class
		mt.setUp("moviedata.txt", "movieratings.csv");
	}

	/**
	 * Sets up the Movie Trivia class
	 * @param movieData .txt file
	 * @param movieRatings .csv file
	 */
	public void setUp(String movieData, String movieRatings) {
		//load movie database files
		movieDB.setUp(movieData, movieRatings);

		//print all actors and movies
		this.printAllActors();
		this.printAllMovies();
	}

	/**
	 * Prints a list of all actors and the movies they acted in.
	 */
	public void printAllActors () {
		System.out.println(movieDB.getActorsInfo());
	}

	/**
	 * Prints a list of all movies and their ratings.
	 */
	public void printAllMovies () {
		System.out.println(movieDB.getMoviesInfo());
	}


	//five utility methods

	/**
	 * inserts given actor into and his/her movies into the database
	 * if the actor already exists in the list, update his/her movies
	 * @param actor to be inserted
	 * @param movies to be added/updated
	 * @param actorsInfo
	 */
	public void insertActor(String actor, String [] movies, ArrayList<Actor>actorsInfo) {
		//set up a condition for checking the actor in the list or not
		boolean existingActor = false;

		//remove white sapces & turns movie into lowercase
		String updatedActor = actor.trim().toLowerCase();

		//iterate through the actosInfo arraylist to locate actor
		for (Actor oldActor: actorsInfo) {

			//condition1: if the actor already exists, update the moviescasted list

			if(oldActor.getName().equals(updatedActor)) {

				//iterate through the actor's movies list and add to the moviescasted list
				for (String movie : movies) {

					//remove white spaces & turns movie into lowercase
					//movie.trim().toLowerCase();
					String updatedMovie = movie.trim().toLowerCase();
					//get the moviescasted list for the given actor and update the arraylist
					//skip the movie that already contained
					if(!oldActor.getMoviesCast().contains(updatedMovie)) {
						oldActor.getMoviesCast().add(updatedMovie);
					}
				}
				existingActor = true;
				break;
			}
		}

		if(!existingActor) {
			//condition2: if the given actor does not exist in the list, create a new one and add to it
			Actor newActor = new Actor(updatedActor);

			//iterate through the actor's movies list and add to the moviescasted list
			for (String movie : movies) {

				//remove white spaces & turns movie into lowercase
				movie.trim().toLowerCase();

				//get the moviescasted list for the given actor and update the arraylist
				newActor.getMoviesCast().add(movie);
			}

			//add the new actor to the actorsInfo list
			actorsInfo.add(newActor);
		}
	}


	/**
	 * insert given ratings for the given movie into the database
	 * @param movie to be inserted
	 * @param ratings to be inserted
	 * @param moviesInfo
	 */
	public void insertRating(String movie, int[] ratings, ArrayList<Movie>moviesInfo) {
		//check the validity of the ratings array
		if (ratings == null || ratings.length != 2) {
			return;
		}

		//store the info in the ratings array into two new variables
		int criticalRating = ratings[0];
		int audienceRating = ratings[1];

		//check if the critical and audience ratings are between 0 and 100
		if (criticalRating < 0 || criticalRating > 100 || audienceRating < 0 || audienceRating > 100) {
			return;
		}


		//set up a condition for checking the movie in the list or not
		boolean existingMovie = false;

		//remove white spaces & turns movie into lowercases
		String updatedMovie = movie.trim().toLowerCase();

		//iterate through the moviesInfo arraylist to locate the movie
		for (Movie oldMovie: moviesInfo) {

			//condition1: if the movie already exists, update the ratings of the movie
			if (oldMovie.getName().equals(updatedMovie)) {

				//set the old movie's critical rating to the first index of the ratings array
				oldMovie.setCriticRating(criticalRating);

				//set the old movie's audience rating to the second index of the ratings array
				oldMovie.setAudienceRating(audienceRating);

				existingMovie = true;
				break;
			}
		}

		if(!existingMovie) {
			//condition 2: if the given movie does not exist in the list, create a new one and add to it
			Movie newMovie = new Movie(movie, criticalRating,audienceRating);

			//add the new movie to the moviesInfo list
			moviesInfo.add(newMovie);
		}
	}


	/**
	 * given the actor, return his or her list of movies
	 * @param actor to be examined
	 * @param actorsInfo of the actor
	 * @return the actor's list of movies
	 */
	public ArrayList<String> selectWhereActorIs(String actor, ArrayList<Actor>actorsInfo){
		//initialize an arraylist to store the return values
		ArrayList<String> returnMovies = new ArrayList<>();

		//remove white spaces & turns movie into lowercases
		String updatedActor = actor.trim().toLowerCase();

		//iterate through the actorsInfo list to locate the actor
		for (Actor existingActor: actorsInfo) {

			//check if the actor exists in the list
			if (existingActor.getName().equals(updatedActor)) {

				//return the returnMovies list with all the movies casted by the actor
				returnMovies.addAll(existingActor.getMoviesCast());
			}
		}

		//return the actor's list of movies
		return returnMovies;
	}



	/**
	 * given a movie, return the list of all actors in the movie
	 * @param movie to be examined
	 * @param actorsInfo that matches the movie
	 * @return the list of actors who are casted in the given movie
	 */
	public ArrayList<String> selectWhereMovieIs(String movie, ArrayList<Actor>actorsInfo){
		//initialize an arraylist to store all the actors casted in the given movie
		ArrayList<String> actorsInMovie = new ArrayList<>();

		//remove white spaces & turns movie into lowercases
		String updatedMovie = movie.trim().toLowerCase();

		//iterate through the actorInfo list to locate the actor
		for (Actor actorInList: actorsInfo) {

			//also iterate through the movie list of the actor
			for (String actorMovie: actorInList.getMoviesCast()){

				//check if the given movie is in the list of the actor's movie
				if (actorMovie.equals(updatedMovie)) {

					//add the name of the located actor to the actorsInMovie list
					actorsInMovie.add(actorInList.getName());
				}
			}
		}
		//return the list of all actors in the given movie
		return actorsInMovie;
	}


	/**
	 * return a list of movies which satisfy an inequality or equality based on the comparison argument and the targeted rating
	 * @param comparison symbol
	 * @param targetRating of rating score
	 * @param isCritic or notCritic -- audience in this case
	 * @param moviesInfo
	 * @return list of movies that satisfy the standards
	 */
	public ArrayList<String> selectWhereRatingIs(char comparison, int targetRating, boolean isCritic, ArrayList<Movie>moviesInfo){
		//initialize an arraylist to store the movies that meet the standards
		ArrayList<String> moviesSelected = new ArrayList<>();

		//check if the targetRating is valid
		if (targetRating < 0 | targetRating > 100) {
			return moviesSelected;
		}

		//check if the comparison symbol is valid
		if (comparison != '=' && comparison != '>' && comparison != '<') {
			return moviesSelected;
		}

		//iterate over the moviesInfo list to locate movies
		for (Movie movieInList : moviesInfo) {

			//condition1: compare audience rating -- isCritic == false
			if (isCritic == false) {

				//store the movie's audience rating in a variable
				int audienceRating = movieInList.getAudienceRating();

				//condition1a. movie with audience rating equaling to target rating
				if (comparison == '=') {
					if (audienceRating == targetRating) {
						moviesSelected.add(movieInList.getName());
					}

					//condition1b. movie with audience rating bigger than target rating
				} else if (comparison == '>'){
					if (audienceRating > targetRating) {
						moviesSelected.add(movieInList.getName());
					}

					//condition1c. movie with audience rating smaller than target rating
				} else if(comparison == '<'){
					if (audienceRating < targetRating) {
						moviesSelected.add(movieInList.getName());
					}
				}

				//condition2: compare critic rating -- isCritic == true
			} else {

				//store the movie's critic rating in a variable
				int criticRating = movieInList.getCriticRating();

				//condition2a. movie with critic rating equaling to target rating
				if (comparison == '=') {
					if (criticRating == targetRating) {
						moviesSelected.add(movieInList.getName());
					}

					//condition2b. movie with critic rating bigger than target rating
				} else if (comparison == '>'){
					if (criticRating > targetRating) {
						moviesSelected.add(movieInList.getName());
					}

					//condition2c. movie with critic rating smaller than target rating
				} else if(comparison == '<'){
					if (criticRating < targetRating) {
						moviesSelected.add(movieInList.getName());
					}
				}
			}

		}
		//return the list of movies that meet the requirement
		return moviesSelected;
	}

	//five more fun methods

	/**
	 * returns a list of all actors that the given actor has ever worked with in any movie except the actor herself/himself
	 * @param actor given
	 * @param actorsInfo
	 * @return a list of all other actors cocasted in his/her movie
	 */
	public ArrayList<String> getCoActors(String actor, ArrayList<Actor> actorsInfo){
		//initialize an arraylist storing all the coactors of the given actor
		ArrayList<String> coactors = new ArrayList<>();

		//remove white spaces & turns actor into lowercases
		String updatedActor = actor.trim().toLowerCase();

		//get the list of movies of the given actor
		ArrayList<String> moviesOfActor = selectWhereActorIs(updatedActor, actorsInfo);

		//iterate over the list of movies of the given actor
		for (String movie : moviesOfActor) {

			//get the list of actors casted in each movie of the given actor
			ArrayList<String> actorInMovie = selectWhereMovieIs(movie, actorsInfo);

			for(String actor_iteration : actorInMovie){
				// add the actors who are not in the list
				if(!coactors.contains(actor_iteration)){
					coactors.add(actor_iteration);
				}
			}
		}
		coactors.remove(updatedActor);
		//return the arraylist storing all other coactors of the given actors
		return coactors;
	}


	/**
	 * returns a list of movies where both actors are casted
	 * @param actor1 to be compared
	 * @param actor2 to be compared
	 * @param actorsInfo
	 * @return the list of common movies
	 */
	public ArrayList<String> getCommonMovie(String actor1, String actor2, ArrayList<Actor> actorsInfo){
		//initialize an arraylist storing all the common movies of actor1 and actor2
		ArrayList<String> commonMovies = new ArrayList<>();

		//remove white spaces & turn actor1 and actor2 into lowercases
		String updatedActorOne = actor1.trim().toLowerCase();
		String updatedActorTwo = actor2.trim().toLowerCase();

		//get the list of movies of the two actors
		ArrayList<String> moviesOfActorOne = selectWhereActorIs(updatedActorOne, actorsInfo);
		ArrayList<String> moviesOfActorTwo = selectWhereActorIs(updatedActorTwo, actorsInfo);

		//condition1: actor1 is not the same as actor2
		if (!updatedActorOne.equals(updatedActorTwo)) {

			//iterate over the movies of actor1
			for (String movie1 : moviesOfActorOne) {
				// if the movie in list 1 also appears in list 2, then add them to the common list
				if(moviesOfActorTwo.contains(movie1)){
					commonMovies.add(movie1);
				}
			}

			//condition2: actor1 is the same as actor2
		} else {

			//add all of the movies of the actor to the common movies list
			commonMovies.addAll(moviesOfActorOne);
		}

		//return the list of common movies of the two actors
		return commonMovies;
	}


	/**
	 * return a list of movie that both critical and audience ratings are >= 85
	 * @param moviesInfo
	 * @return list of good movies
	 */
	public ArrayList<String> goodMovies(ArrayList<Movie> moviesInfo){

		//initialize an arraylist of movies which are good
		ArrayList<String> goodMovies = new ArrayList<>();

		//find the movie that critic score equal or greater than 85 then store them to the list
		ArrayList<String> criticGoodMovie = selectWhereRatingIs('=', 85, true, moviesInfo);
		criticGoodMovie.addAll(selectWhereRatingIs('>', 85, true, moviesInfo));

		//initialize an arraylist of movies which have audience ratings equal to or larger than 85
		ArrayList<String> audienceGoodMovie = selectWhereRatingIs('=', 85, false, moviesInfo);
		audienceGoodMovie.addAll(selectWhereRatingIs('>', 85, false, moviesInfo));

		//iterate over the movies which have critic ratings that fit the standards
		for (String criticGood: criticGoodMovie) {

			//iterate over the movies which have audience ratings that fit the standards
			for (String audienceGood: audienceGoodMovie) {

				//check if the two movies are the same
				if (criticGood.equals(audienceGood)) {

					//if the same, add it to good movies list
					goodMovies.add(criticGood);

				}
			}
		}
		//return the list of good movies
		return goodMovies;
	}


	/**
	 * given a pair of movies, return a list of actors that acted in both
	 * @param movie1 to be compared
	 * @param movie2 to be compared
	 * @param actorsInfo
	 * @return the list of common actors of the two movies
	 */
	public ArrayList<String> getCommonActors (String movie1, String movie2, ArrayList<Actor>actorsInfo){
		//initialize an arraylist storing all the common actors of movie1 and movie2
		ArrayList<String> commonActors = new ArrayList<>();

		//remove white spaces & turn movie1 and movie2 into lowercases
		String updatedMovie1 = movie1.trim().toLowerCase();
		String updatedMovie2 = movie2.trim().toLowerCase();

		//get the list of actors of the two movies
		ArrayList<String> actorsInMovie1 = selectWhereMovieIs(updatedMovie1, actorsInfo);
		ArrayList<String> actorsInMovie2 = selectWhereMovieIs(updatedMovie2, actorsInfo);

		//condition1: movie1 is not the same as movie2
		if (!updatedMovie1.equals(updatedMovie2)) {

			//iterate over the actors of movie1
			for (String actor1 : actorsInMovie1) {
				// add to the common list if the actor in list 1 also appears in list 2
				if(actorsInMovie2.contains(actor1)){
					commonActors.add(actor1);
				}
			}

			//condition2: movie1 is the same as movie2
		} else {

			//add all of the actors of the movie to the common actors list
			commonActors.addAll(actorsInMovie1);
		}

		//return the list of common actors of the two movies
		return commonActors;
	}



	public static double [] getMean(ArrayList<Movie> moviesInfo) {
		//initialize an array that contains double variables
		double [] mean = new double[2];

		//initialize two double variables for the sum of critic and audience ratings
		double criticSum = 0;
		double audienceSum = 0;

		//iterate over the moviesInfo list to get ratings for each movie
		for (Movie movieToCheck : moviesInfo) {

			//add the critic & audience ratings of the movie to the sum
			criticSum += movieToCheck.getCriticRating();
			audienceSum += movieToCheck.getAudienceRating();
		}

		//calculate the mean for the two ratings
		double criticMean = (criticSum/moviesInfo.size());
		double audienceMean = (audienceSum/moviesInfo.size());

		//store the mean of the two ratings into the mean array
		mean[0] = criticMean;
		mean[1] = audienceMean;

		if(moviesInfo.size()==0)
		{
			mean[0]=0;
			mean[1]=0;
		}
		//return the array
		return mean;

	}




}
