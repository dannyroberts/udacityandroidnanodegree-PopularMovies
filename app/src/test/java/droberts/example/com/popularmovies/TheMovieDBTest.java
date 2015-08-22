package droberts.example.com.popularmovies;

import junit.framework.TestCase;

public class TheMovieDBTest extends TestCase {
    public void testGetImageUrl() {
        assertEquals(TheMovieDB.getImageUrl("/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg", TheMovieDB.ImageSize.w185),
                "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg");
    }
}
