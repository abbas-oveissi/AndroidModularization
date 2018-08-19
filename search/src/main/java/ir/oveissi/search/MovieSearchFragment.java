package ir.oveissi.search;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import ir.oveissi.core.bases.AppComponentHolder;
import ir.oveissi.core.bases.BaseFragment;
import ir.oveissi.core.di.NavigationProvider;
import ir.oveissi.core.navigation.MainEntryPoint;
import ir.oveissi.core.navigation.NavigationManager;
import ir.oveissi.search.databinding.FragmentMovieSearchBinding;
import ir.oveissi.search.db.AppDatabaseSingleton;
import ir.oveissi.search.db.Suggestion;


public class MovieSearchFragment extends BaseFragment implements MovieSearchAdapter.ItemClickListener {

    FragmentMovieSearchBinding binding;
    @Inject
    NavigationManager navigationManager;
    @Inject
    MainEntryPoint mainEntryPoint;
    private MovieSearchAdapter mListAdapter;

    public MovieSearchFragment() {
        // Required empty public constructor
    }

    public static MovieSearchFragment newInstance() {
        MovieSearchFragment fragment = new MovieSearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, ir.oveissi.search.R.layout.fragment_movie_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search Movies");


        binding.rvSuggestion.setAdapter(new MovieSearchAdapter(getContext(), new ArrayList<Suggestion>()));
        binding.rvSuggestion.setLayoutManager(new LinearLayoutManager(getContext()));

        getSuggestionFromDB("");


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getSuggestionFromDB(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
    }

    private void getSuggestionFromDB(String query) {

        Flowable<List<Suggestion>> flowable = null;
        if (query.isEmpty()) {
            flowable = AppDatabaseSingleton.getIntance(getContext().getApplicationContext())
                    .suggestionDAO()
                    .getAll();
        } else {
            flowable = AppDatabaseSingleton.getIntance(getContext().getApplicationContext())
                    .suggestionDAO()
                    .getAllOrderByCount(query);
        }
        DisposableSubscriber<List<Suggestion>> dispose = flowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<List<Suggestion>>() {
                    @Override
                    public void onNext(List<Suggestion> suggestions) {
                        mListAdapter = new MovieSearchAdapter(getActivity(), suggestions);
                        binding.rvSuggestion.setAdapter(mListAdapter);
                        mListAdapter.setItemClickListener(MovieSearchFragment.this);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void search() {
        Flowable<Boolean> observable = Flowable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Suggestion suggestion = new Suggestion();
                suggestion.setTitle(binding.etSearch.getText().toString());
                suggestion.setCount(0);

                AppDatabaseSingleton.getIntance(getContext().getApplicationContext())
                        .suggestionDAO()
                        .insertAll(suggestion);
                return true;
            }
        });
        DisposableSubscriber<Boolean> dispose = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aVoid) {
                        navigationManager.getNavigator(MovieSearchFragment.this)
                                .open(mainEntryPoint.openMain(binding.etSearch.getText().toString()));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        MovieSearchComponent component = DaggerMovieSearchComponent
                .builder()
                .navigationProvider(new NavigationProvider(this))
                .appComponent(((AppComponentHolder) getActivity().getApplicationContext()).getAppComponent())
                .build();
        component.inject(this);

    }

    @Override
    public void ItemClicked(int position, Suggestion item) {
        navigationManager.getNavigator(MovieSearchFragment.this)
                .open(mainEntryPoint.openMain(item.getTitle()));
    }

}
