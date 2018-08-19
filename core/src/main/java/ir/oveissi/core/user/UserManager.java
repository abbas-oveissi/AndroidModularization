package ir.oveissi.core.user;


import ir.oveissi.core.local.SettingsManager;

public class UserManager {

    private User currentUser;
    private SettingsManager settingsManager;

    public UserManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }

    public boolean isUserLogged() {
        if (currentUser != null)
            return true;
        return tryLogin();
    }

    public boolean tryLogin() {

        if (!settingsManager.getAccessToken().isEmpty()) {
            User user = new User(settingsManager.getName(),
                    settingsManager.getUsername(),
                    settingsManager.getAccessToken(),
                    settingsManager.getRefreshToken());
            this.currentUser = user;
            return true;
        }
        return false;
    }

    public void login(User user) {
        settingsManager.setName(user.getName());
        settingsManager.setAccessToken(user.getAccessToken());
        settingsManager.setRefreshToken(user.getRefreshToken());
        settingsManager.setUsername(user.getUsername());
        this.currentUser = user;
    }

    public void logout() {
        settingsManager.setName("");
        settingsManager.setAccessToken("");
        settingsManager.setRefreshToken("");
        settingsManager.setUsername("");
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

}
