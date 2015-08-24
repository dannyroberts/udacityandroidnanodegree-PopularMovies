package droberts.example.com.popularmovies;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TheMovieDB {
    private static final String LOG_TAG = TheMovieDB.class.getSimpleName();
    private static final String IMAGE_URL_BASE = "http://image.tmdb.org/t/p/";
    private static final String API_URL_BASE = "http://api.themoviedb.org/3/";
    private static final String ENDPOINT_DISCOVER_MOVIE = "discover/movie";

    enum SortOption {
        MOST_POPULAR, HIGHEST_RATED
    }

    public enum ImageSize {
        w92 ("w92"),
        w154 ("w154"),
        w185 ("w185"),
        w342 ("w342"),
        w500 ("w500"),
        w780 ("w780"),
        original ("original");
        private String mString;
        ImageSize (String string) {
            mString = string;
        }
        public String toString() {
            return mString;
        }
    }
    private String mApiKey;
    /*
     * imageName should begin with a '/'
     */
    protected static String getImageUrl(String imageName, ImageSize imageSize) {
        return IMAGE_URL_BASE + imageSize.toString() + imageName;
    }

    public TheMovieDB(String apiKey) {
        mApiKey = apiKey;
    }


    public static class MovieInfo {
        public final String poster_path;
        public final String original_title;
        public MovieInfo(String poster_path, String original_title) {
            this.poster_path = poster_path;
            this.original_title = original_title;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj.getClass() != getClass()) {
                return false;
            }
            MovieInfo rhs = (MovieInfo) obj;
            return poster_path.equals(rhs.poster_path) && original_title.equals(rhs.original_title);
        }
        public String toString() {
            return "new MovieInfo(\"" + poster_path + "\", \"" + original_title + "\")";
        }
    }
    public static ArrayList<MovieInfo> parseDiscoverMoviesJson(JSONObject discoverMoviesJson) {
        ArrayList<MovieInfo> movieInfos = new ArrayList<>();
        try {
            JSONArray results = discoverMoviesJson.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject movieInfoJson = results.getJSONObject(i);
                movieInfos.add(new MovieInfo(movieInfoJson.getString("poster_path"),
                        movieInfoJson.getString("original_title")));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, ENDPOINT_DISCOVER_MOVIE + " discoverMoviesJson not expected format: \n" + discoverMoviesJson.toString());
            return null;
        }
        return movieInfos;
    }

    public ArrayList<MovieInfo> discoverMovies(SortOption sort) {
        String sortByParam;
        switch (sort) {
            case HIGHEST_RATED:
                sortByParam = "vote_average.desc";
                break;
            case MOST_POPULAR:
                sortByParam = "popularity.desc";
                break;
            default:
                throw new AssertionError("This is impossible");
        }
        Uri uri = Uri.parse(API_URL_BASE + ENDPOINT_DISCOVER_MOVIE).buildUpon()
                .appendQueryParameter("api_key", mApiKey)
                .appendQueryParameter("sort_by", sortByParam)
                .build();
        Requests.Response response = Requests.get(uri.toString());
        if (response == null) {
            return null;
        }
        JSONObject discoverMoviesJson;
        try {
            discoverMoviesJson = response.json();
        } catch (JSONException e) {
            Log.e(LOG_TAG, ENDPOINT_DISCOVER_MOVIE + " came back with invalid JSON: \n"
                    + response.text);
            return null;
        }

        return parseDiscoverMoviesJson(discoverMoviesJson);
    }
}
