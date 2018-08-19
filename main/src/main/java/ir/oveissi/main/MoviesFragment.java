package ir.oveissi.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import ir.oveissi.core.bases.AppComponentHolder;
import ir.oveissi.core.bases.BaseFragment;
import ir.oveissi.core.di.NavigationProvider;
import ir.oveissi.core.navigation.NavigationManager;
import ir.oveissi.core.navigation.SearchEntryPoint;
import ir.oveissi.core.network.ApiInterface;
import ir.oveissi.core.pojo.Movie;
import ir.oveissi.core.pojo.Pagination;
import ir.oveissi.main.databinding.FragmentMoviesBinding;


public class MoviesFragment extends BaseFragment implements MoviesAdapter.ItemClickListener {

    public String title = "";
    public int current_page = 1;
    @Inject
    ApiInterface apiInterface;
    @Inject
    NavigationManager navigationManager;
    @Inject
    SearchEntryPoint searchEntryPoint;
    FragmentMoviesBinding binding;
    private MoviesAdapter mListAdapter;
    private EndlessLinearLayoutRecyclerview.onLoadMoreListener endlessLinearLayoutRecyclerview;

    public MoviesFragment() {
        // Required empty public constructor
    }

    public static MoviesFragment newInstance(String title) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = DataBindingUtil.inflate(inflater, ir.oveissi.main.R.layout.fragment_movies, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem search = menu.add("Search");
        search.setIcon(ir.oveissi.main.R.drawable.ic_search);
        search.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getTitle() != null && item.getTitle().equals("Search")) {
            navigationManager.getNavigator(this).open(searchEntryPoint.openMain());
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Movies");


        endlessLinearLayoutRecyclerview = new EndlessLinearLayoutRecyclerview.onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMoviesByTitle(title, current_page);
            }
        };


        if (mListAdapter == null) {
            mListAdapter = new MoviesAdapter(getActivity(), new ArrayList<Movie>());
            mListAdapter.setItemClickListener(this);
            binding.rvMovies.setAdapter(mListAdapter);

            binding.rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvMovies.setOnLoadMoreListener(endlessLinearLayoutRecyclerview);

            current_page = 1;
            onLoadMoviesByTitle(title, current_page);
        } else {
            binding.rvMovies.setAdapter(mListAdapter);

            binding.rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvMovies.setOnLoadMoreListener(endlessLinearLayoutRecyclerview);
            binding.rvMovies.setLoading(false);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        MoviesComponent component = DaggerMoviesComponent
                .builder()
                .navigationProvider(new NavigationProvider(this))
                .appComponent(((AppComponentHolder) getActivity().getApplicationContext()).getAppComponent())
                .build();
        component.inject(this);
    }

    public void showMoreMovies(Pagination<Movie> movies) {
        int lastPage = (movies.metadata.total_count / movies.metadata.per_page) + 1;
        if (movies.metadata.current_page != lastPage)
            binding.rvMovies.setLoading(false);
        current_page++;
        for (Movie p : movies.data) {
            mListAdapter.addItem(p);
        }
    }

    public void onLoadMoviesByTitle(String title, int page) {
        DisposableSubscriber<Pagination<Movie>> disposable = apiInterface
                .getMoviesByTitle(title, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Pagination<Movie>>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Pagination<Movie> movies) {
                        showMoreMovies(movies);
                    }
                });
    }

    @Override
    public void ItemClicked(int position, Movie item) {
        navigationManager.getNavigator(this)
                .open(MovieDetailFragment.newInstance(String.valueOf(item.id)));

    }
}
