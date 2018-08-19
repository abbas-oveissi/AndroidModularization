package ir.oveissi.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import ir.oveissi.core.bases.AppComponentHolder;
import ir.oveissi.core.bases.BaseFragment;
import ir.oveissi.core.network.ApiInterface;
import ir.oveissi.core.pojo.Movie;
import ir.oveissi.main.databinding.FragmentMovieDetailBinding;

public class MovieDetailFragment extends BaseFragment {


    @Inject
    ApiInterface apiInterface;
    FragmentMovieDetailBinding binding;
    private String movie_id;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment newInstance(String movie_id) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString("movie_id", movie_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie_id = getArguments().getString("movie_id");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        MovieDetailComponent component = DaggerMovieDetailComponent
                .builder()
                .appComponent(((AppComponentHolder) getActivity().getApplicationContext()).getAppComponent())
                .build();
        component.inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, ir.oveissi.main.R.layout.fragment_movie_detail, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        onLoadMovieDetail(movie_id);
    }


    public void showMovieDetail(Movie movie) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(movie.title);

        binding.tvOverview.setText(movie.plot);

        Picasso.with(getContext())
                .load(movie.poster)
                .noFade()
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(binding.imPoster);
    }


    public void onLoadMovieDetail(String id) {
        DisposableSubscriber<Movie> disposable = apiInterface
                .getMovieById(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Movie>() {
                    @Override
                    public void onComplete() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Movie movie) {
                        showMovieDetail(movie);
                    }
                });
    }


}
