package ir.oveissi.search;

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
public interface MovieSearchComponent {

    void inject(MovieSearchFragment __);
}
