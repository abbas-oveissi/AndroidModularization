package ir.oveissi.authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import ir.oveissi.authentication.databinding.FragmentAuthLoginBinding;
import ir.oveissi.core.navigation.NavigationManager;
import ir.oveissi.core.network.ApiInterface;
import ir.oveissi.core.pojo.Token;
import ir.oveissi.core.user.User;
import ir.oveissi.core.user.UserManager;


public class LoginFragment extends Fragment {

    @Inject
    ApiInterface apiInterface;
    @Inject
    UserManager userManager;
    @Inject
    NavigationManager navigationManager;
    ProgressDialog progressDialog;
    private FragmentAuthLoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationManager.getNavigator(LoginFragment.this)
                        .open(RegisterFragment.newInstance());
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait...");

                Map<String, String> map = new HashMap<>();
                map.put("grant_type", "password");
                map.put("username", binding.etEmail.getText().toString());
                map.put("password", binding.etPassword.getText().toString());
                DisposableSubscriber<Token> dispose = apiInterface.getToken(map)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<Token>() {
                            @Override
                            public void onNext(Token token) {
                                User user = new User("",
                                        binding.etEmail.getText().toString(),
                                        token.access_token,
                                        token.refresh_token);
                                userManager.login(user);
                                getActivity().finish();
                            }

                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                            }

                            @Override
                            public void onComplete() {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                            }
                        });

            }
        });
    }
}
