package droberts.example.com.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final TheMovieDB.SortOption[] sortOptions = {
            TheMovieDB.SortOption.MOST_POPULAR, TheMovieDB.SortOption.HIGHEST_RATED};
    private TheMovieDB.SortOption mSelectedSortOption;
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
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TheMovieDB.MovieInfo movieInfo = mMoviesAdapter.getItem(position);
                Intent intent = new Intent(parent.getContext(), DetailActivity.class);
                intent.putExtra("movie_info", movieInfo);
                startActivity(intent);
            }
        });

        // https://developer.android.com/guide/topics/ui/controls/spinner.html
        Spinner sortSpinner = (Spinner) rootView.findViewById(R.id.spinner_sort);
        ArrayAdapter<CharSequence> sortAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.sort_options, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mSelectedSortOption != sortOptions[position]) {
                    mSelectedSortOption = sortOptions[position];
                    populateMovies();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;
    }

    public void populateMovies() {
        new LoadMoviesTask().execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        populateMovies();
    }

    private class LoadMoviesTask extends AsyncTask<Void, Void, ArrayList<TheMovieDB.MovieInfo>> {
        @Override
        protected ArrayList<TheMovieDB.MovieInfo> doInBackground(Void... params) {
            return ((MainActivity) getActivity()).getTheMovieDB().discoverMovies(
                    mSelectedSortOption != null ? mSelectedSortOption : TheMovieDB.SortOption.MOST_POPULAR);
        }

        @Override
        protected void onPostExecute(ArrayList<TheMovieDB.MovieInfo> movieInfos) {
            mMoviesAdapter.clear();
            if (movieInfos != null) {
                for (TheMovieDB.MovieInfo movieInfo : movieInfos) {
                    mMoviesAdapter.add(movieInfo);
                }
            }
        }
    }
}
