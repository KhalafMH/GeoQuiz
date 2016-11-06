package com.bignerdranch.android.geoquiz;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = QuizActivity.class.getSimpleName();
    private int mCurrentQuestion = 0;
    private Question[] mQuestions;
    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private TextView mQuestionTextView;
    private ImageButton mPrevButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate()");

        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        try {
            mQuestions = getQuestions();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            this.finish();
        }


        mTrueButton = (Button) findViewById(R.id.trueButton);
        mFalseButton = (Button) findViewById(R.id.falseButton);
        mNextButton = (ImageButton) findViewById(R.id.nextButton);
        mPrevButton = (ImageButton) findViewById(R.id.prevButton);
        mQuestionTextView = (TextView) findViewById(R.id.questionTextView);

        updateQuestion();

        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentQuestion = (mCurrentQuestion + 1) % mQuestions.length;
                updateQuestion();
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentQuestion--;
                mCurrentQuestion = mCurrentQuestion < 0 ? mQuestions.length - 1 : mCurrentQuestion;
                updateQuestion();
            }
        });

        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNextButton.callOnClick();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private void checkAnswer(boolean answerTrue) {
        int answer = answerTrue == mQuestions[mCurrentQuestion].isAnswerTrue() ? R.string.correct_toast : R.string.wrong_toast;
        Toast.makeText(QuizActivity.this, answer, Toast.LENGTH_SHORT).show();
    }

    private Question[] getQuestions() throws FileNotFoundException {
        Resources resources = getResources();
        InputStream questionsFile = resources.openRawResource(R.raw.questions);
        Scanner questionsScanner = new Scanner(questionsFile);
        List<Question> questionsList = new ArrayList<>();

        while (questionsScanner.hasNext()) {
            String questionText;
            boolean questionIsTrue;

            questionIsTrue = questionsScanner.nextBoolean();
            questionText = questionsScanner.nextLine().trim();

            questionsList.add(new Question(questionText, questionIsTrue));
        }

        questionsScanner.close();
        return questionsList.toArray(new Question[questionsList.size()]);
    }

    private void updateQuestion() {
        mQuestionTextView.setText(mQuestions[mCurrentQuestion].getQuestionText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
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
