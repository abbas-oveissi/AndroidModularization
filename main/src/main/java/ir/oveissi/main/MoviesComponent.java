package ir.oveissi.main;

import dagger.Component;
import ir.oveissi.core.di.AppComponent;
import ir.oveissi.core.di.FragmentScope;
import ir.oveissi.core.di.NavigationProvider;

@FragmentScope
@Component(dependencies = {
        AppComponent.class
}, modules = {
        NavigationProvider.class
})
public interface MoviesComponent {

    void inject(MoviesFragment __);
}
