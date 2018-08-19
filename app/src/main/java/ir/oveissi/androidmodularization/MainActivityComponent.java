package ir.oveissi.androidmodularization;


import dagger.Component;
import ir.oveissi.core.di.AcitvityScope;
import ir.oveissi.core.di.AppComponent;
import ir.oveissi.core.di.NavigationProvider;

@AcitvityScope
@Component(dependencies = {
        AppComponent.class
}, modules = {
        NavigationProvider.class,
})
public interface MainActivityComponent {
    void inject(MainActivity __);
}
