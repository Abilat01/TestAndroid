package com.example.geoquiztrue;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_INDEX = "index";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        questionTextView = (TextView) findViewById(R.id.questionTextView);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        questionTextView.setText(question);

        btnTrue = (Button) findViewById(R.id.btnTrue);
        btnFalse = (Button) findViewById(R.id.btnFalse);
        btnNext = (Button) findViewById(R.id.btnNext);

        btnCheat =(Button) findViewById(R.id.btnCheat);
        btnCheat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue);
                startActivity(intent);
            }//переход на новую активность с ответом
        });

        updateQuestion();

    }


    public void ShowToastOne(View view) {
        checkAnswer(true);
    } //Не использовал в методе onCreate методы onClick, а создавал свои, стоит уточнить как лучше

    public void ShowToastTwo(View view) {

        checkAnswer(false);


    }

    public void NextQuestion(View view) {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        updateQuestion();
    }

    public void BackQuestion(View view) {
        mCurrentIndex = (mCurrentIndex - 1 + mQuestionBank.length) % mQuestionBank.length;
        updateQuestion();
    }


    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        questionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messsageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messsageResId = R.string.correct_toast;
        } else {
            messsageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messsageResId, Toast.LENGTH_SHORT).show();
        //Проверка ответа
    }



}
