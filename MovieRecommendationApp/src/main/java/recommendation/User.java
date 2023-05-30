package recommendation;

import java.util.Hashtable;

class User {
    private int user_Id;
    private Hashtable<Integer, Double> ratings;
    private double similarity;

    public User(int user_Id) {
        this.user_Id = user_Id;
        this.ratings = new Hashtable<>();
    }

    public int getUser_Id() {
        return user_Id;
    }

    public Hashtable<Integer, Double> getRatings() {
        return ratings;
    }

    public void addMovieRating(int movie_Id, double rating) {
        ratings.put(movie_Id, rating);
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
    
}
