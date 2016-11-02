package com.bignerdranch.android.geoquiz;

/**
 * Representation of a question and whether the answer is true or false.
 */
public class Question {
    private String mQuestionText;
    private boolean mIsAnswerTrue;

    public boolean isAnswerTrue() {
        return mIsAnswerTrue;
    }

    public void setIsAnswerTrue(boolean isAnswerTrue) {
        mIsAnswerTrue = isAnswerTrue;
    }

    public String getQuestionText() {
        return mQuestionText;
    }

    public void setQuestionText(String questionText) {
        mQuestionText = questionText;
    }

    public Question(String text, boolean isTrue) {
        this.mQuestionText = text;
        this.mIsAnswerTrue = isTrue;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Question)) {
            return false;
        }

        Question that = (Question) o;
        return mIsAnswerTrue == that.isAnswerTrue() && mQuestionText.equals(that.getQuestionText());
    }
}
