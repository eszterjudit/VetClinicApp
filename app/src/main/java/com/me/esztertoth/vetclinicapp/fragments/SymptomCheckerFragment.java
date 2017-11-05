package com.me.esztertoth.vetclinicapp.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.me.esztertoth.vetclinicapp.R;
import com.me.esztertoth.vetclinicapp.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SymptomCheckerFragment extends Fragment {

    @BindView(R.id.question) TextView question;
    @BindView(R.id.answer) TextView answer;
    @BindView(R.id.yes_no_buttons_container) LinearLayout yesNoButtonsContainer;
    @BindView(R.id.mood_image) ImageView moodImage;

    private List<Question> questions;
    private int currentQuestion;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_symptom_checker, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Symptom checker");

        questions = createQuestionsList();
        currentQuestion = 0;

        initUI();

        return view;
    }

    private void initUI() {
        if(questions.isEmpty()) {
            yesNoButtonsContainer.setVisibility(View.VISIBLE);
            answer.setVisibility(View.GONE);
            moodImage.setVisibility(View.GONE);
        }
        showNextQuestion();
    }

    private void showNextQuestion() {
        question.setText(questions.get(currentQuestion).getSymptom());
        answer.setText(questions.get(currentQuestion).getCause());
        currentQuestion = currentQuestion + 1;
    }

    private List<Question> createQuestionsList() {
        List<Question> questionList = new ArrayList<>();
        Resources res = getResources();
        String[] arrayOfQuestions = res.getStringArray(R.array.symptom_checker_questions);
        String[] arrayOfAnswers = res.getStringArray(R.array.symptom_checker_answers);

        for(int i=0; i<arrayOfQuestions.length; i++) {
            Question question = new Question();
            question.setSymptom(arrayOfQuestions[i]);
            question.setCause(arrayOfAnswers[i]);
            questionList.add(question);
        }

        return questionList;
    }

    @OnClick(R.id.no_button)
    void noClicked() {
        if(currentQuestion != questions.size()-1) {
            showNextQuestion();
        } else {
            yesNoButtonsContainer.setVisibility(View.GONE);
            answer.setVisibility(View.VISIBLE);
            moodImage.setVisibility(View.VISIBLE);
            moodImage.setImageResource(R.drawable.ic_good);
            question.setText(getString(R.string.symptom_checker_no_symptoms));
            answer.setText(getString(R.string.symptom_checker_no_symptoms_desc));
        }
    }

    @OnClick(R.id.yes_button)
    void yesClicked() {
        yesNoButtonsContainer.setVisibility(View.GONE);
        moodImage.setVisibility(View.VISIBLE);
        answer.setVisibility(View.VISIBLE);
        moodImage.setImageResource(R.drawable.ic_bad);
    }

}
