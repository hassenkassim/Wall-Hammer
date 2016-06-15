package net.hamoto.wallhammer.Manager;

import com.google.android.gms.games.Games;

import net.hamoto.wallhammer.MainActivity;

/**
 * Created by HassenKassim on 30.05.16.
 */
public class PlayGamesManager {

    private static final String LEADERBOARD_ID = "CgkIxPfbyu8CEAIQAA";
    private static final int REQUEST_LEADERBOARD = 123;

    public static void showLeaderboard(){
        if(MainActivity.mHelper!=null&&MainActivity.mHelper.isSignedIn()){
            MainActivity.getActivity().startActivityForResult(Games.Leaderboards.getLeaderboardIntent(MainActivity.mHelper.getApiClient(),
                    LEADERBOARD_ID), REQUEST_LEADERBOARD);
        }
    }

    public static void postHighscore(long hscore){
        if(MainActivity.mHelper!=null&&MainActivity.mHelper.isSignedIn()){
            Games.Leaderboards.submitScore(MainActivity.mHelper.getApiClient(), LEADERBOARD_ID, hscore);
        }
    }

}
