package clink.youparking;

public class StoredInformation {

    private static User loggedUser;

    public static void setLoggedUser(User incoming) {
        loggedUser = incoming;
    }

}