package com.wordpress.honeymoonbridge.bridgeapp.GooglePlayGames;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.PlayersClient;

public class GooglePlayServices {

    public static AchievementsClient achievementsClient;
    public static boolean signedIn;
    // request codes we use when invoking an external activity
    public static final int RC_SIGN_IN = 9001;
    public static final int RC_UNUSED = 5001;
    // tag for debug logging
    // Client used to sign in with Google APIs
    public static GoogleSignInClient mGoogleSignInClient;
    // Client variables
    public static PlayersClient mPlayersClient;


}
