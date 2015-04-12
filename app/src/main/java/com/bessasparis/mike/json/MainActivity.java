package com.bessasparis.mike.json;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends ActionBarActivity {

    public String jsonString;
    public JSONObject jsonObj;
    private int currentQuestion = 0;
    private int numberOfQuestions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            jsonString = loadJSONFromAsset();
            jsonObj = new JSONObject(jsonString);
            numberOfQuestions = jsonObj.length();
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.next).setOnClickListener(nextButtonHandler);

        displayQuestion(jsonObj, currentQuestion);

    }

    private View.OnClickListener nextButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentQuestion <= numberOfQuestions) {
                ++currentQuestion;
            }
            else {
                currentQuestion = 0;
            }
            displayQuestion(jsonObj, currentQuestion);
        }
    };

    public void displayQuestion(JSONObject mObj, int qNumber) {
        TextView questionTextView = (TextView) findViewById(R.id.question);
        Button choice1 = (Button) findViewById(R.id.answerchoice1);
        Button choice2 = (Button) findViewById(R.id.answerchoice2);
        Button choice3 = (Button) findViewById(R.id.answerchoice3);
        Button choice4 = (Button) findViewById(R.id.answerchoice4);

        TextView feedbackText = (TextView) findViewById(R.id.feedback);
        feedbackText.setText("");

        try {
            questionTextView.setText(getQuestionText(mObj, qNumber));
            choice1.setText(getChoiceText(mObj, qNumber, 1));
            choice2.setText(getChoiceText(mObj, qNumber, 2));
            choice3.setText(getChoiceText(mObj, qNumber, 3));
            choice4.setText(getChoiceText(mObj, qNumber, 4));
            setButtonHandlers(mObj, qNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setButtonHandlers(JSONObject mObj, int i) throws JSONException {
        JSONArray questionsArray = mObj.getJSONArray("questions");
        JSONObject questionObj = questionsArray.getJSONObject(i);
        int correctButton = questionObj.getInt("answer");

        findViewById(R.id.answerchoice1).setOnClickListener(feedbackWrong);
        findViewById(R.id.answerchoice2).setOnClickListener(feedbackWrong);
        findViewById(R.id.answerchoice3).setOnClickListener(feedbackWrong);
        findViewById(R.id.answerchoice4).setOnClickListener(feedbackWrong);

        if (correctButton == 1) {
            findViewById(R.id.answerchoice1).setOnClickListener(feedbackCorrect);
        }
        else if (correctButton == 2) {
            findViewById(R.id.answerchoice2).setOnClickListener(feedbackCorrect);
        }
        else if (correctButton == 3) {
            findViewById(R.id.answerchoice3).setOnClickListener(feedbackCorrect);
        }
        else if (correctButton == 4) {
            findViewById(R.id.answerchoice4).setOnClickListener(feedbackCorrect);
        }

    }

    private View.OnClickListener feedbackCorrect = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView feedbackText = (TextView) findViewById(R.id.feedback);
            feedbackText.setText("CORRECT");
            feedbackText.setTextColor(Color.GREEN);
        }
    };

    private View.OnClickListener feedbackWrong = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView feedbackText = (TextView) findViewById(R.id.feedback);
            feedbackText.setText("WRONG");
            feedbackText.setTextColor(Color.RED);
        }
    };

    public String getQuestionText(JSONObject mObj, int i) throws JSONException {
        String questionText;

        JSONArray questionsArray = mObj.getJSONArray("questions");
        JSONObject questionObj = questionsArray.getJSONObject(i);
        questionText = questionObj.getString("questiontext");
        return questionText;
    }

    // i is index to question
    // j is which answerchoice to get
    public String getChoiceText(JSONObject mObj, int i, int j) throws JSONException {
        String choiceText = null;

        JSONArray questionsArray = mObj.getJSONArray("questions");
        JSONObject questionObj = questionsArray.getJSONObject(i);

        switch(j) {
            case 1:
                choiceText = questionObj.getString("answerchoice1");
                break;
            case 2:
                choiceText = questionObj.getString("answerchoice2");
                break;
            case 3:
                choiceText = questionObj.getString("answerchoice3");
                break;
            case 4:
                choiceText = questionObj.getString("answerchoice4");
                break;
        }
        return choiceText;
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
