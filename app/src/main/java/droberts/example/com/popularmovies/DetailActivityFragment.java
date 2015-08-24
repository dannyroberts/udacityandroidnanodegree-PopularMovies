package droberts.example.com.popularmovies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private TheMovieDB.MovieInfo mMovieInfo;
    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mMovieInfo = (TheMovieDB.MovieInfo) getActivity().getIntent().getExtras().getSerializable("movie_info");
        ((TextView) rootView.findViewById(R.id.detail_movie_title)).setText(mMovieInfo.original_title);
        Picasso.with(getActivity())
                .load(TheMovieDB.getImageUrl(mMovieInfo.poster_path, TheMovieDB.ImageSize.w500))
                .placeholder(R.drawable.transparent_poster)
                .into((ImageView) rootView.findViewById(R.id.detail_movie_poster));
        ((TextView) rootView.findViewById(R.id.detail_movie_rating)).setText("Rating: " + mMovieInfo.vote_average);
        ((TextView) rootView.findViewById(R.id.detail_movie_release_date)).setText("Release Date: " + mMovieInfo.release_date);
        ((TextView) rootView.findViewById(R.id.detail_movie_description)).setText(mMovieInfo.overview);
        return rootView;
    }
}
