package ir.oveissi.core.di;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ir.oveissi.core.local.SettingsManager;
import ir.oveissi.core.user.UserManager;

@Module
public class UserManagerProvider {

    @Singleton
    @Provides
    public UserManager provideUserManager(SettingsManager settingsManager) {
        return new UserManager(settingsManager);
    }
}
