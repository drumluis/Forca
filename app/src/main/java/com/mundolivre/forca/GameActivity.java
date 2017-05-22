package com.mundolivre.forca;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by luis on 09/03/17.
 */
public class GameActivity extends Activity {

    private String[] words;
    private Random rand;
    private String currWord;
    private LinearLayout wordLayout;
    private TextView[] charViews;

    private GridView letters;
    private LetterAdapter ltrAdapt;

    private ImageView[] bodyParts;
    private int numParts = 6;
    private int currPart; //will increment when wrong answers are chosen
    private int numChars; //number of characters in current word
    private int numCorr; //number correctly guessed

    private AlertDialog helpAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Resources res = getResources();
        words = res.getStringArray(R.array.words);

        rand = new Random();
        currWord = "";

        wordLayout = (LinearLayout) findViewById(R.id.word);

        letters = (GridView) findViewById(R.id.letters);

        bodyParts = new ImageView[numParts];
        bodyParts[0] = (ImageView) findViewById(R.id.head);
        bodyParts[1] = (ImageView) findViewById(R.id.body);
        bodyParts[2] = (ImageView) findViewById(R.id.arm1);
        bodyParts[3] = (ImageView) findViewById(R.id.arm2);
        bodyParts[4] = (ImageView) findViewById(R.id.leg1);
        bodyParts[5] = (ImageView) findViewById(R.id.leg2);

     //  getActionBar().setDisplayHomeAsUpEnabled(true);

        playGame();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_help:
                showHelp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void playGame() {
        String newWord = words[rand.nextInt(words.length)];
        while (newWord.equals(currWord)) newWord = words[rand.nextInt(words.length)];
        currWord = newWord;

        charViews = new TextView[currWord.length()];

        wordLayout.removeAllViews();

        for (int c = 0; c < currWord.length(); c++) {
            charViews[c] = new TextView(this);
            charViews[c].setText(" " + currWord.charAt(c));

            charViews[c].setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            charViews[c].setGravity(Gravity.CENTER);
            charViews[c].setTextColor(Color.WHITE);
            charViews[c].setBackgroundResource(R.drawable.letter_bg);
            //add to layout
            wordLayout.addView(charViews[c]);
        }

        ltrAdapt = new LetterAdapter(this);
        letters.setAdapter(ltrAdapt);

        currPart = 0;
        numChars = currWord.length();
        numCorr = 0;

        for (int p = 0; p < numParts; p++) {
            bodyParts[p].setVisibility(View.INVISIBLE);
        }

    }

    public void letterPressed(View view) {
        String ltr = ((TextView)view).getText().toString();
        char letterChar = ltr.charAt(0);
        view.setEnabled(false);
        view.setBackgroundResource(R.drawable.letter_down);

        boolean correct = false;
        for (int k = 0; k < currWord.length(); k++) {
            if (currWord.charAt(k) == letterChar) {
                correct = true;
                numCorr++;
                charViews[k].setTextColor(Color.BLACK);
            }
        }

        if (correct) {
            if (numCorr == numChars) {
                disableButtons();

                AlertDialog.Builder winBuild = new AlertDialog.Builder(this);
                winBuild.setTitle("Uhu!!");
                winBuild.setMessage("Você acertou!\n\nA palavra era:\n\n" + currWord);
                winBuild.setPositiveButton("Jogar outra",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }
                        });
                winBuild.setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }
                        });
                     winBuild.show();
                 }
            } 

            else if (currPart < numParts) {
                bodyParts[currPart].setVisibility(View.VISIBLE);
                currPart++;
            } else {
                //user has lost
                disableButtons();

                AlertDialog.Builder loseBuild = new AlertDialog.Builder(this);
                loseBuild.setTitle("OOPS");
                loseBuild.setMessage("Você perdeu\n\nA resposta era:\n\n" + currWord);
                loseBuild.setPositiveButton("Jogar outra vez",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.playGame();
                            }
                        });

                loseBuild.setNegativeButton("Sair",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                GameActivity.this.finish();
                            }
                        });

                loseBuild.show();
            }
        }
    

    public void disableButtons() {
        int numLetters = letters.getChildCount();
        for (int l = 0; l < numLetters; l++) {
            letters.getChildAt(l).setEnabled(false);
        }
    }

    public void showHelp() {
        AlertDialog.Builder helpBuild = new AlertDialog.Builder(this);

        helpBuild.setTitle("Ajuda");
        helpBuild.setMessage("Adivinhe a palavra selecionando as letras.\n\n"
                + "Você pode errar apenas seis tentativa!");
        helpBuild.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        helpAlert.dismiss();
                    }});
                    helpAlert = helpBuild.create();

                    helpBuild.show();
                }
}
