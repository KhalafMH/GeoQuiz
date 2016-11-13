package com.bignerdranch.android.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    // constants
    private static final String TAG = CheatActivity.class.getSimpleName();
    private static final String EXTRA_USER_CHEATED = CheatActivity.class.getPackage().toString() + ".cheater";
    private static final String EXTRA_ANSWER_TRUE = CheatActivity.class.getPackage().getName() + ".answer";
    private static final String KEY_ANSWER = "answer";
    private static final String KEY_IS_CHEATER = "is_cheater";

    // state fields
    private boolean mIsAnswerTrue;
    private boolean mIsCheater;

    // reference fields
    private Button mCheatButton;
    private TextView mAnswerTextView;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_ANSWER, mIsAnswerTrue);
        outState.putBoolean(KEY_IS_CHEATER, mIsCheater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            mIsAnswerTrue = savedInstanceState.getBoolean(KEY_ANSWER, false);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
        }
        else {
            mIsAnswerTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_TRUE, false);
            mIsCheater = false;
        }

        mCheatButton = (Button) findViewById(R.id.cheatButton);
        mAnswerTextView = (TextView) findViewById(R.id.cheatAnswerTextView);

        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsCheater = true;

                int answerStringId;
                answerStringId = mIsAnswerTrue ? R.string.answer_true : R.string.answer_false;
                mAnswerTextView.setText(answerStringId);

                Intent intent = new Intent(CheatActivity.this, QuizActivity.class);
                intent.putExtra(EXTRA_USER_CHEATED, true);
                setResult(RESULT_OK, intent);
            }
        });
    }

    public static boolean didUserCheat(Intent data) {
        return data.getBooleanExtra(EXTRA_USER_CHEATED, false);
    }

    @NonNull
    public static Intent newIntent(Context context, boolean answerTrue) {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_TRUE, answerTrue);
        return intent;
    }

}
