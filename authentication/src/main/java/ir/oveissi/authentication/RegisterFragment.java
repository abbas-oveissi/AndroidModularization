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

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import ir.oveissi.authentication.databinding.FragmentAuthRegisterBinding;
import ir.oveissi.core.navigation.NavigationManager;
import ir.oveissi.core.network.ApiInterface;
import ir.oveissi.core.pojo.Register;
import ir.oveissi.core.pojo.RegisterBody;


public class RegisterFragment extends Fragment {

    @Inject
    ApiInterface apiInterface;

    @Inject
    NavigationManager navigationManager;
    ProgressDialog progressDialog;
    private FragmentAuthRegisterBinding binding;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
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
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_auth_register, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = ProgressDialog.show(getContext(), "Loading", "Please Wait...");

                RegisterBody registerBody = new RegisterBody();
                registerBody.name = binding.etName.getText().toString();
                registerBody.email = binding.etEmail.getText().toString();
                registerBody.password = binding.etPassword.getText().toString();

                DisposableSubscriber<Register> dispose = apiInterface
                        .register(registerBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSubscriber<Register>() {
                            @Override
                            public void onNext(Register token) {
                                navigationManager.getNavigator(RegisterFragment.this)
                                        .navigateBack();
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
