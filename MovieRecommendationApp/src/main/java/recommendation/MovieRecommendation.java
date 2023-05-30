package recommendation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

public class MovieRecommendation<T> {

    private Hashtable<Integer, User> users;
    private Hashtable<Integer, User> targetUsers;
    private Hashtable<Integer, String> movieNames;

    public MovieRecommendation() {
        users = new Hashtable<>();
        targetUsers = new Hashtable<>();
        movieNames = new Hashtable<>();
    }

    public Hashtable<Integer, User> getUsers() {
        return users;
    }

    public Hashtable<Integer, User> getTargetUsers() {
        return targetUsers;
    }

    public Hashtable<Integer, String> getMovieNames() {
        return movieNames;
    }

    public void addUserToTable(int user_Id, Hashtable<Integer, User> table) {
        table.put(user_Id, new User(user_Id));
    }

    public void addMovieRating(int user_Id, int movie_Id, double rating, Hashtable<Integer, User> table) {
        User user = table.get(user_Id);
        if (user != null) {
            user.addMovieRating(movie_Id, rating);
        }
        else{
            System.out.println("user not found");
        }
        
    }

    public void loadMovieNames(File f) {
        //load the movies names and ids from the movies file and store them into the movieNames table 
        try (Scanner scanner = new Scanner(f)) {
            int line_Num = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (line_Num != 0) {
                    if (parts.length >= 2) {
                        int movie_Id = Integer.parseInt(parts[0]);
                        String movie_Name = parts[1];
                        movieNames.put(movie_Id, movie_Name);
                        line_Num++;
                    }

                } else {
                    line_Num++;
                }

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

    }

    public void LoadRatingsToTable(File f) {
        //load the main data file to the user tables
        try (Scanner scanner = new Scanner(f)) {
            int line_Num = 0;
            List<Integer> movie_Ids = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line_Num == 0) {
                    // get the movie ids from the first row and starts from index 1
                    String[] movieIdValues = line.split(",");
                    for (int i = 1; i < movieIdValues.length; i++) {
                        int movie_Id = Integer.parseInt(movieIdValues[i]);
                        movie_Ids.add(movie_Id);
                    }
                } else {
                    // if the index =0 that equal to the user id else movie rating
                    String[] values = line.split(",");
                    int user_Id = Integer.parseInt(values[0]);
                    addUserToTable(user_Id, users);

                    for (int i = 1; i < values.length; i++) {
                        int movie_Id = movie_Ids.get(i - 1);
                        double rating = Double.parseDouble(values[i]);
                        addMovieRating(user_Id, movie_Id, rating, users);
                    }
                }

                line_Num++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    public void LoadTargetsToTable(File f) {
        //load the main data file to the user tables
        try (Scanner scanner = new Scanner(f)) {
            int line_Num = 0;
            List<Integer> movie_Ids = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line_Num == 0) {
                    // get the movie ids from the first row and starts from index 1
                    String[] movieIdValues = line.split(",");
                    for (int i = 1; i < movieIdValues.length; i++) {
                        int movie_Id = Integer.parseInt(movieIdValues[i]);
                        movie_Ids.add(movie_Id);
                    }
                } else {
                    // if the index =0 that equal to the user id else movie rating
                    String[] values = line.split(",");
                    int user_Id = Integer.parseInt(values[0]);
                    addUserToTable(user_Id, targetUsers);

                    for (int i = 1; i < values.length; i++) {
                        int movie_Id = movie_Ids.get(i - 1);
                        double rating = Double.parseDouble(values[i]);
                        addMovieRating(user_Id, movie_Id, rating, targetUsers);
                    }
                }

                line_Num++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

    }

    public int getDummyTargetUser() {
        int user_Id = generateUserId(targetUsers);
        addUserToTable(user_Id, targetUsers);
        Object[] moviesId = movieNames.keySet().toArray();
        for (int i = 0; i < moviesId.length; i++) {
            int movie_Id = Integer.parseInt(moviesId[i].toString());
            addMovieRating(user_Id, movie_Id, 0, targetUsers);
        }

        return user_Id;
    }

    public ArrayList<String[]> getRandomMovieList(int j, int n) {
        ArrayList<Integer> id = new ArrayList<>();
        ArrayList<String[]> list = new ArrayList<>();

        for (int i = 0; i < j; i++) {
            String[] filmList = new String[n];
            for (int k = 0; k < n; k++) {
                boolean state = false;
                int randId = getRandomFilmID();
                while (state == false) {
                    if (id.size() == 0) {
                        state = true;
                        break;
                    }
                    for (int l = 0; l < id.size(); l++) {
                        if (randId == id.get(l)) {
                            state = false;
                            randId = getRandomFilmID();
                            break;
                        } else {
                            state = true;
                        }

                    }

                }
                id.add(randId);
                String movieName = null;

                for (Map.Entry<Integer, String> entry : movieNames.entrySet()) {
                    int key = entry.getKey();

                    int randIdS = randId;
                    if (key == randIdS) {
                        movieName = entry.getValue();
                        filmList[k] = movieName;
                        break;
                    }

                }

            }

            list.add(filmList);
        }
        return list;
    }

    public int getIdFromMovieName(String movieName) {

        int movieId = 0;

        for (Map.Entry<Integer, String> entry : movieNames.entrySet()) {
            String value = entry.getValue();
            if (value != null && value.equals(movieName)) {
                movieId = entry.getKey();
                break;
            }
        }
        return movieId;
    }

    public int getRandomFilmID() {
        Random r = new Random();
        int low = 1;
        int high = 100;
        int result = r.nextInt((int) (high - low)) + low;
        int id = 0;
        Object[] ids = movieNames.keySet().toArray();
        for (int i = 0; i < result; i++) {
            id = Integer.parseInt(ids[i].toString());
        }

        return id;

    }

    public ArrayList<String> getRecommendations(int targetUserId, int numSimilarUsers, int numRecommendedMovies) {
        User targetUser = null;
        for (User user : targetUsers.values()) {
            if (user.getUser_Id() == targetUserId) {
                targetUser = user;
            }
        }

        if (targetUser == null) {
            System.out.println("Target user not found.");
            return new ArrayList<>(); // Return an empty list if target user not found
        }

        // Step 1: Compute similarity between each pair of users
        Heap<User> similarityHeap = new Heap<>();
        for (User user : users.values()) {
            double similarity = computeSimilarity(user, targetUser);
            user.setSimilarity(similarity);
            similarityHeap.add(user);
        }

        // Step 2: Retrieve the most similar users from the heap
        ArrayList<User> mostSimilarUsers = new ArrayList<>();
        int count = Math.min(numSimilarUsers, similarityHeap.size());
        for (int i = 0; i < count; i++) {
            User user = similarityHeap.removeTop();
            mostSimilarUsers.add(user);
        }

        // Step 3: Get the highest rated movies for the most similar users
        ArrayList<String> recommendedMovies = new ArrayList<>();
        for (User user : mostSimilarUsers) {
            Hashtable<Integer, Double> ratings = user.getRatings();
            ArrayList<Integer> topRatedMovies = getTopRatedMovies(ratings, numRecommendedMovies);
            for (int movieId : topRatedMovies) {
                String movieName = movieNames.get(movieId);
                recommendedMovies.add(movieName);
            }
        }

        return recommendedMovies;
    }

    private ArrayList<Integer> getTopRatedMovies(Hashtable<Integer, Double> ratings, int count) {
        ArrayList<Integer> topRatedMovies = new ArrayList<>();
        ArrayList<Entry<Integer, Double>> ratingEntries = new ArrayList<>(ratings.entrySet());
        ratingEntries.sort(Entry.comparingByValue(Comparator.reverseOrder()));

        int numMovies = Math.min(count, ratingEntries.size());
        for (int i = 0; i < numMovies; i++) {
            Entry<Integer, Double> entry = ratingEntries.get(i);
            topRatedMovies.add(entry.getKey());
        }
 
        return topRatedMovies;
    }

    private double computeSimilarity(User user1, User user2) {
        Hashtable<Integer, Double> ratings1 = user1.getRatings();
        Hashtable<Integer, Double> ratings2 = user2.getRatings();

        Set<Integer> commonMovies = new HashSet<>(ratings1.keySet());
        commonMovies.retainAll(ratings2.keySet());

        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int movieId : commonMovies) {
            double rating1 = ratings1.get(movieId);
            double rating2 = ratings2.get(movieId);

            dotProduct += rating1 * rating2;
            norm1 += Math.pow(rating1, 2);
            norm2 += Math.pow(rating2, 2);
        }

        if (norm1 == 0.0 || norm2 == 0.0) {
            return 0.0; // No similarity if one of the users has no ratings in common movies.
        }

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    private int generateUserId(Hashtable<Integer, User> users) {

        int maxId = 0;
        Object[] list = users.keySet().toArray();
        for (int i = 0; i < list.length; i++) {
            int key = Integer.parseInt(list[i].toString());
            if (i == 0) {
                maxId = key;

            } else {
                if (key > maxId) {
                    maxId = key;
                }
            }

        }
        maxId++;

        int id = maxId;
        return id;

    }


}
