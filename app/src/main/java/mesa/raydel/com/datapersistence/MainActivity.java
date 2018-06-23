package mesa.raydel.com.datapersistence;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import mesa.raydel.com.datapersistence.model.User;
import mesa.raydel.com.datapersistence.util.AppDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String prefs = "userPrefs";
    private static final String FILENAME = "nameStorage";
    private static final int ID = 1;
    private boolean isSavedChecked = true;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        TextView saveOrRetrieveTextView = findViewById(R.id.saveOrRetrieveTextView);

                        if (checkedId == R.id.saveRadioButton) {
                            isSavedChecked=true;
                            findViewById(R.id.nameEditText).setVisibility(View.VISIBLE);
                            saveOrRetrieveTextView.setText("Save to:");


                        } else if (checkedId == R.id.retrieveRadioButton) {
                            isSavedChecked=false;
                            findViewById(R.id.nameEditText).setVisibility(View.GONE);
                            saveOrRetrieveTextView.setText("Retrieve From:");
                        }
                    }
                });
    }

    public void sharedPreferencesClicked(View view) {
        if(isSavedChecked){

            EditText nameEditText = findViewById(R.id.nameEditText);
            String name = nameEditText.getText().toString();

            //Writing to shared preferences
            SharedPreferences sharedPref = this.getSharedPreferences(prefs, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name", name);
            editor.commit();

            Toast.makeText(this, "Your name was saved to Shared Preferences", Toast.LENGTH_LONG).show();
        }else {
            //Reading from shared preferences
            SharedPreferences sharedPref = this.getSharedPreferences(prefs, Context.MODE_PRIVATE);

            //Reading the name from the shared preferences
            String  savedName = sharedPref.getString("name", null);

            if(savedName == null){
                Toast.makeText(this, "Sorry we couldn't find your name. Please save it again", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Hi, " + savedName + ". We retrieved your name from Shared Preferences", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void fileSystemClicked(View view) {
        if(isSavedChecked){

            EditText nameEditText = findViewById(R.id.nameEditText);
            String name = nameEditText.getText().toString();

            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
                outputStream.write(name.getBytes());
                outputStream.close();

            } catch (Exception e) {
                Toast.makeText(this, "Sorry, something went wrong. Please try again", Toast.LENGTH_LONG).show();
            }

            Toast.makeText(this, "Your name was saved to File System", Toast.LENGTH_LONG).show();
        }else {
            //Reading from file
            FileInputStream fin = null;
            BufferedReader bufferedReader = null;
            StringBuilder name = new StringBuilder();
            try {
                fin = openFileInput(FILENAME);
                bufferedReader = new BufferedReader(new InputStreamReader(fin));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    name.append(line);
                }
                bufferedReader.close();
                fin.close();

                Toast.makeText(this, "Hi, " + name.toString() + ". We retrieved your name from File System", Toast.LENGTH_LONG).show();

            } catch (IOException e) {
                Toast.makeText(this, "Sorry we couldn't find your name. Please save it again", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    public void sqliteClicked(View view) {
        final AppDatabase mDb = AppDatabase.getInstance(MainActivity.this);
        final Activity activity = this;

        if(isSavedChecked){
            EditText nameEditText = findViewById(R.id.nameEditText);
            final String name = nameEditText.getText().toString();

            @SuppressLint("StaticFieldLeak") final AsyncTask<Void, Void, Void> execute = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    mDb.userModel().insert(new User(ID, name));
                    return null;
                }

                @Override
                protected void onPostExecute(final Void result) {
                    Toast.makeText(activity, "Your name was saved to SQLite", Toast.LENGTH_LONG).show();
                }
            }.execute();

        }else {

            @SuppressLint("StaticFieldLeak") final AsyncTask<Void, Void, Void> execute = new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                     user = mDb.userModel().findByID(ID);
                    return null;
                }

                @Override
                protected void onPostExecute(final Void result) {
                    if(user==null || user.getName() == null)
                        Toast.makeText(activity, "Sorry we couldn't find your name. Please save it again", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(activity, "Hi, " + user.getName() + ". We retrieved your name from SQLite", Toast.LENGTH_LONG).show();
                }
            }.execute();
        }
    }
}
