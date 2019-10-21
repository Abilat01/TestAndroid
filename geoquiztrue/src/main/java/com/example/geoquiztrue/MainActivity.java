package com.example.geoquiztrue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index";
    private static final int REQUEST_CODE_CHEAT = 0;
    private static final String QUESTION_INDEX_KEY = "Key";
    private static final String QUESTIONS_ANSWERED_KEY = "key2";

    private int correctAnswers = 0;

    Button btnTrue;
    Button btnFalse;
    Button btnNext;
    Button btnCheat;
    TextView questionTextView;

    private Question[] mQuestionBank = new Question[]{ //Масивчики, полезная вешь
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };
    private int mCurrentIndex = 0;
    private boolean mIsCheater;

    private boolean[] mQuestionsAnswered = new boolean[mQuestionBank.length];
    private String TAG;
    //логического массив, чтобы отслеживать, на какие вопросы были даны ответы:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(QUESTION_INDEX_KEY);
            mQuestionsAnswered = savedInstanceState.getBooleanArray(QUESTIONS_ANSWERED_KEY);
        }//восстановление данных внутри


        questionTextView = (TextView) findViewById(R.id.questionTextView);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        questionTextView.setText(question);

        btnTrue = (Button) findViewById(R.id.btnTrue);
        btnFalse = (Button) findViewById(R.id.btnFalse);
        btnNext = (Button) findViewById(R.id.btnNext);

        btnCheat = (Button) findViewById(R.id.btnCheat);
        btnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
            }//переход на новую активность с ответом
        });

        updateQuestion();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_CHEAT) {
            if (data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShow(data);
        }
    }

    public void ShowToastOne(View view) {
        checkAnswer(true);
    } //Не использовал в методе onCreate методы onClick, а создавал свои, стоит уточнить как лучше

    public void ShowToastTwo(View view) {

        checkAnswer(false);


    }

    public void NextQuestion(View view) {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        mIsCheater = false;
        updateQuestion();
    }

    public void BackQuestion(View view) {
        mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
        updateQuestion();
    }


    private void updateQuestion() {
        btnTrue.setEnabled(!mQuestionsAnswered[mCurrentIndex]);
        btnFalse.setEnabled(!mQuestionsAnswered[mCurrentIndex]);//проверка ответа

        int question = mQuestionBank[mCurrentIndex].getTextResId();
        questionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        mQuestionsAnswered[mCurrentIndex] = true;
        //setButtonsEnabled(false);

        btnTrue.setEnabled(false);
        btnFalse.setEnabled(false);//проверка ответа

        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            correctAnswers++;
        } else {
            messageResId = R.string.incorrect_toast;
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        calculateScore();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called");
        outState.putInt(QUESTION_INDEX_KEY, mCurrentIndex);
        outState.putBooleanArray(QUESTIONS_ANSWERED_KEY, mQuestionsAnswered);
    }

    private void calculateScore() {
        // check all questions answered
        for (boolean answered : mQuestionsAnswered) {
            if (!answered) return;
        }
        // all answered, show message
        int score = correctAnswers * 100 / mQuestionBank.length;
        String message = getString(R.string.toast_score, score);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "Выход");
        //1 пункт хз зачем, 2 это id. 3 это номер позиции

        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case 1: {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Вы уверенны?")
                        .setMessage("")
                        .setCancelable(true)
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getApplicationContext(), "Попробуем еще раз!",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .setPositiveButton("Мне пора", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();//реализация выхода


                break;
            }

        }

        return super.onOptionsItemSelected(item);
    }


}
