package ir.oveissi.core.di;


import javax.inject.Singleton;

import dagger.Component;
import ir.oveissi.core.network.ApiInterface;
import ir.oveissi.core.network.ApiModule;
import ir.oveissi.core.network.ClientModule;
import ir.oveissi.core.user.UserManager;

@Singleton
@Component(modules = {
        ApiModule.class,
        AndroidModule.class,
        ClientModule.class,
        UserManagerProvider.class
})
public interface AppComponent {

    UserManager getUserManager();

    ApiInterface wqegetApiInterface();

}
