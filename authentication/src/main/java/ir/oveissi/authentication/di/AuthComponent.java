package ir.oveissi.authentication.di;

import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;
import ir.oveissi.authentication.AuthMainActivity;
import ir.oveissi.core.di.AcitvityScope;
import ir.oveissi.core.di.AppComponent;
import ir.oveissi.core.di.NavigationProvider;

@AcitvityScope
@Component(dependencies = {
        AppComponent.class
}, modules = {
        AndroidSupportInjectionModule.class,
        FragmentSubcomponent.class,
        NavigationProvider.class
})
public interface AuthComponent {

    void inject(AuthMainActivity __);
}
