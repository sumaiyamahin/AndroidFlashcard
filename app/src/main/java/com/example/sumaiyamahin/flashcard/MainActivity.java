package com.example.sumaiyamahin.flashcard;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.List;
//test
public class MainActivity extends AppCompatActivity {
    //define my class variables
    FlashcardDatabase flashcardDatabase; //contains ALL  of my flashcards
    List<Flashcard> allFlashcards;

    int currentCardDisplayedIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ths variable storing the flashcards that ill make
        flashcardDatabase = new com.example.sumaiyamahin.flashcard.FlashcardDatabase(this);
        //store the list of flashcards in my variable
        allFlashcards=flashcardDatabase.getAllCards();


        findViewById(R.id.flashcard_question).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View answerSideView = findViewById(R.id.flashcard_answer); //its just better to have it this way. not absolutely necessary tho
                View questionSideView = findViewById(R.id.flashcard_question);

// get the center for the clipping circle
                int cx = answerSideView.getWidth() / 2;
                int cy = answerSideView.getHeight() / 2;

// get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
                Animator anim = ViewAnimationUtils.createCircularReveal(answerSideView, cx, cy, 0f, finalRadius);

// hide the question and show the answer to prepare for playing the animation!
                questionSideView.setVisibility(View.INVISIBLE);
                answerSideView.setVisibility(View.VISIBLE);
                //Basically is doing this. but with the rename. s
                //findViewById(R.id.flashcard_question).setVisibility(View.INVISIBLE);
                //findViewById(R.id.flashcard_answer).setVisibility(View.VISIBLE);

                anim.setDuration(1000);
                anim.start();
            }
        });

        findViewById(R.id.flashcard_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
                findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);

            }
        });

        findViewById(R.id.addbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCardActivity.class);
                MainActivity.this.startActivityForResult(intent, 100);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                //right_in: takes from the rightmost corner(100%p) and moves it to the leftmost corner(0) in 500ms(500)
                //not entirely sure what left_out is doing:
                //For overridePendingTransition the first parameter is the "enter" animation for the new
                    //launched activity and the second is the "exit" animation for the current activity we're leaving.

            }
        });

        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(0).getAnswer());
        }

        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex++;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;
                }

                // set the question and answer TextViews with data from the database
                if (allFlashcards.size() > 0) { //this ensures that these lines only work if there is at least one card in the database.
                    ((TextView) findViewById(R.id.flashcard_question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion());
                    ((TextView) findViewById(R.id.flashcard_answer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());

                    //how can we make the nextbutton always take us to the question side? set visibility so it can toggle
                    findViewById(R.id.flashcard_answer).setVisibility(View.INVISIBLE);
                    findViewById(R.id.flashcard_question).setVisibility(View.VISIBLE);
                }

                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_out);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                findViewById(R.id.flashcard_question).startAnimation(leftOutAnim);
                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // this method is called when the animation is finished playing
                        findViewById(R.id.flashcard_question).startAnimation(rightInAnim);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }

                });


            }
        });



    }

    //this gets triggered when the user is adding a new card into the database
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) { // this 100 needs to match the 100 we used when we called startActivityForResult!
            String question = data.getExtras().getString("questionvalue"); // 'string1' needs to match the key we used when we put the string in the Intent
            String answer = data.getExtras().getString("answervalue");


            ((TextView) findViewById(R.id.flashcard_question)).setText(question);
            ((TextView) findViewById(R.id.flashcard_answer)).setText(answer);
            //this is where you are inserting the new flashcard values into the database
            flashcardDatabase.insertCard(new Flashcard(question, answer));
            //we need this to update the list every time we add new cards
            allFlashcards = flashcardDatabase.getAllCards();



        }
    }
}
