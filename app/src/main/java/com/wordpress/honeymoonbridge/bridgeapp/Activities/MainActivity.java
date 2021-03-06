package com.wordpress.honeymoonbridge.bridgeapp.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.wordpress.honeymoonbridge.bridgeapp.GooglePlayGames.GooglePlayServices;
import com.wordpress.honeymoonbridge.bridgeapp.R;

public class MainActivity extends AppCompatActivity {

    Button play;
    Spinner chooseNet;


    private Menu mOptionsMenu;
    private String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, GameActivity.class);
                startActivity(i);
            }
        });


        // Create the client used to sign in to Google services.
        GooglePlayServices.signedIn = false;
        GooglePlayServices.mGoogleSignInClient = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        mOptionsMenu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                return true;

            case R.id.item2:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.item3:
                onShowAchievmentPressed();
                return true;

            case R.id.item4:
                if(!GooglePlayServices.signedIn)
                    startSignInIntent();
                else
                    signOut();

                return true;

            default:
                return true;
        }
    }


    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void signInSilently() {
        GooglePlayServices.mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            onConnected(task.getResult());
                        } else {
                            onDisconnected();
                        }
                    }
                });
    }

    private void startSignInIntent() {
        startActivityForResult(GooglePlayServices.mGoogleSignInClient.getSignInIntent(), GooglePlayServices.RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        play = findViewById(R.id.play);
        // Since the state of the signed in user can change when the activity is not active
        // it is recommended to try and sign in silently from when the app resumes.
        signInSilently();

        chooseNet = findViewById(R.id.spinnerChooseNet);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String difficulty = prefs.getString("difficulty_pref", "medium");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.AIDifficultyValue, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        chooseNet.setAdapter(adapter);

        if(difficulty.equals("easy")) {
            chooseNet.setSelection(0);
        }else if(difficulty.equals("medium")){
            chooseNet.setSelection(1);
        }else{
            chooseNet.setSelection(2);

        }

        chooseNet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               prefs.edit().putString("difficulty_pref", chooseNet.getSelectedItem().toString()).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        chooseNet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                prefs.edit().putString("difficulty_pref", chooseNet.getSelectedItem().toString()).apply();
//            }
//        });

    }

    private void signOut() {
        if (!isSignedIn()) {
            return;
        }

        GooglePlayServices.mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean successful = task.isSuccessful();
                        onDisconnected();
                    }
                });
    }


    private void handleException(Exception e, String details) {
        int status = 0;

        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            status = apiException.getStatusCode();
        }

        String message = getString(R.string.status_exception_error, details, status, e);

        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null)
                .show();
    }


    /**
     * Start gameplay. This means updating some status variables and switching
     * to the "gameplay" screen (the screen where the user types the score they want).
     */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GooglePlayServices.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }

                onDisconnected();

                new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }

    }


    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        GooglePlayServices.achievementsClient = Games.getAchievementsClient(this, googleSignInAccount);


        GooglePlayServices.mPlayersClient = Games.getPlayersClient(this, googleSignInAccount);

        if (mOptionsMenu != null)
            mOptionsMenu.getItem(3).setTitle(R.string.signout);

        // Set the greeting appropriately on main menu
        GooglePlayServices.mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<Player>() {
                    @Override
                    public void onComplete(@NonNull Task<Player> task) {
                        String displayName;
                        if (task.isSuccessful()) {
                            displayName = task.getResult().getDisplayName();
                        } else {
                            Exception e = task.getException();
                            handleException(e, getString(R.string.players_exception));
                            displayName = "???";
                        }


//                        ((TextView)findViewById(R.id.greetingView)).setText("Hello, " + displayName);
                    }
                });

        GooglePlayServices.signedIn = true;
    }


    public void onShowAchievmentPressed() {
        if(GooglePlayServices.signedIn)
        GooglePlayServices.achievementsClient.getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, GooglePlayServices.RC_UNUSED);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handleException(e, getString(R.string.achievements_exception));
                    }
                });
        else
            Toast.makeText(getApplicationContext(), "You have to sign in to see achievments", Toast.LENGTH_SHORT).show();
    }

    private void onDisconnected() {
        GooglePlayServices.mPlayersClient = null;
        if (mOptionsMenu != null)
            mOptionsMenu.getItem(3).setTitle(R.string.signin);
//        ((TextView)findViewById(R.id.greetingView)).setText("Not signed in");
        GooglePlayServices.signedIn = false;
    }


    public void onSignInOutButtonClicked(View view) {
        if (GooglePlayServices.signedIn)
            signOut();
        else
            startSignInIntent();
    }

    public static class Java8Tester {

        public static void main(String args[]) {
            Java8Tester tester = new Java8Tester();

            //with type declaration
            MathOperation addition = (int a, int b) -> a + b;

            //with out type declaration
            MathOperation subtraction = (a, b) -> a - b;

            //with return statement along with curly braces
            MathOperation multiplication = (int a, int b) -> { return a * b; };

            //without return statement and without curly braces
            MathOperation division = (int a, int b) -> a / b;

            System.out.println("10 + 5 = " + tester.operate(10, 5, addition));
            System.out.println("10 - 5 = " + tester.operate(10, 5, subtraction));
            System.out.println("10 x 5 = " + tester.operate(10, 5, multiplication));
            System.out.println("10 / 5 = " + tester.operate(10, 5, division));

            //without parenthesis
            GreetingService greetService1 = message ->
                    System.out.println("Hello " + message);

            //with parenthesis
            GreetingService greetService2 = (message) ->
                    System.out.println("Hello " + message);

            greetService1.sayMessage("Mahesh");
            greetService2.sayMessage("Suresh");
        }

        interface MathOperation {
            int operation(int a, int b);
        }

        interface GreetingService {
            void sayMessage(String message);
        }

        private int operate(int a, int b, MathOperation mathOperation) {
            return mathOperation.operation(a, b);
        }
    }


}
