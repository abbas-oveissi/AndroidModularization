package ir.oveissi.authentication.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ir.oveissi.authentication.LoginFragment;
import ir.oveissi.authentication.RegisterFragment;

@Module
public abstract class FragmentSubcomponent {

    @ContributesAndroidInjector
    abstract LoginFragment provideSub1();

    @ContributesAndroidInjector
    abstract RegisterFragment provideSub2();
}
