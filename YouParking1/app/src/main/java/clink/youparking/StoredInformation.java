package clink.youparking;

/**
 * Created by acous on 8/30/2016.
 */
public class StoredInformation {

    private static User loggedUser;

    public static void setLoggedUser(User incoming) {
        loggedUser = incoming;
    }

}