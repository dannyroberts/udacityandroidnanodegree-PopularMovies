package droberts.example.com.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private ArrayAdapter<TheMovieDB.MovieInfo> mMoviesAdapter;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        // thanks to http://sudarmuthu.com/blog/using-arrayadapter-and-listview-in-android-applications/
        // for this pattern
        mMoviesAdapter = new ArrayAdapter<TheMovieDB.MovieInfo>(
                getActivity(), R.layout.grid_item_movie, new ArrayList<TheMovieDB.MovieInfo>()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                TheMovieDB.MovieInfo movieInfo = getItem(position);
                if (null == convertView) {
                    view = inflater.inflate(R.layout.grid_item_movie, container);
                } else {
                    view = convertView;
                }
                ImageView posterView = (ImageView) view;
                Picasso.with(getActivity())
                        .load(TheMovieDB.getImageUrl(movieInfo.poster_path, TheMovieDB.ImageSize.w500))
                        .placeholder(R.drawable.transparent_poster)
                        .into(posterView);
                return view;
            }
        };
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(mMoviesAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        new LoadMoviesTask().execute();
    }

    private class LoadMoviesTask extends AsyncTask<Void, Void, ArrayList<TheMovieDB.MovieInfo>> {
        @Override
        protected ArrayList<TheMovieDB.MovieInfo> doInBackground(Void... params) {
            return ((MainActivity) getActivity()).getTheMovieDB().discoverMovies();
        }

        @Override
        protected void onPostExecute(ArrayList<TheMovieDB.MovieInfo> movieInfos) {
            if (movieInfos != null) {
                for (TheMovieDB.MovieInfo movieInfo : movieInfos) {
                    mMoviesAdapter.add(movieInfo);
                }
            }
        }
    }
}
