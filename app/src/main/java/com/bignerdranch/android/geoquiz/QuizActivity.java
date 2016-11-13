package com.bignerdranch.android.geoquiz;

import android.content.Intent;
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
    private static final String KEY_CURRENT_QUESTION = "current_question";
    private static final String KEY_IS_CHEATER = "is_cheater";
    private static final String KEY_CHEAT_STATE_ARRAY = "cheat_state_array";
    private static final int REQUEST_CODE_CHEAT = 0;

    // state fields
    private int mCurrentQuestion;
    private boolean mUserCheated;
    private boolean[] mCheatStateArray;

    // reference fields
    private Question[] mQuestions;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
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
            Log.d(TAG, "questions could not be read", e);
            finish();
        }

        mTrueButton = (Button) findViewById(R.id.trueButton);
        mFalseButton = (Button) findViewById(R.id.falseButton);
        mNextButton = (ImageButton) findViewById(R.id.nextButton);
        mPrevButton = (ImageButton) findViewById(R.id.prevButton);
        mCheatButton = (Button) findViewById(R.id.cheatButton);
        mQuestionTextView = (TextView) findViewById(R.id.questionTextView);

        if (savedInstanceState != null) {
            mCurrentQuestion = savedInstanceState.getInt(KEY_CURRENT_QUESTION, 0);
            mUserCheated = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
            if (mUserCheated) {
                mCheatStateArray = savedInstanceState.getBooleanArray(KEY_CHEAT_STATE_ARRAY);
            }
            else {
                mCheatStateArray = new boolean[mQuestions.length];
            }
        } else {
            mCurrentQuestion = 0;
            mUserCheated = false;
            mCheatStateArray = new boolean[mQuestions.length];
        }
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

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuizActivity packageContext = QuizActivity.this;
                boolean answerTrue = mQuestions[mCurrentQuestion].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(packageContext, answerTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CHEAT:
                if (resultCode == RESULT_OK && data != null) {
                    if (CheatActivity.didUserCheat(data)) {
                        mUserCheated = true;
                        mCheatStateArray[mCurrentQuestion] = true;
                    }
                }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_CURRENT_QUESTION, mCurrentQuestion);
        outState.putBoolean(KEY_IS_CHEATER, mUserCheated);
        if (mUserCheated) {
            outState.putBooleanArray(KEY_CHEAT_STATE_ARRAY, mCheatStateArray);
        }
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
        int answer = answerTrue == mQuestions[mCurrentQuestion].isAnswerTrue()
                ? R.string.correct_toast
                : R.string.wrong_toast;
        if (mCheatStateArray[mCurrentQuestion]) {
            answer = R.string.cheater_toast;
        }
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
