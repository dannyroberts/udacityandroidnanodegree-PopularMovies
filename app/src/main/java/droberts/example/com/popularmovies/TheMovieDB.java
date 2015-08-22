package droberts.example.com.popularmovies;

public class TheMovieDB {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/";
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
    /*
     * imageName should begin with a '/'
     */
    public static String getImageUrl(String imageName, ImageSize imageSize) {
        return BASE_URL + imageSize.toString() + imageName;
    }
}
