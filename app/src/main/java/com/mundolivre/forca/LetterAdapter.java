package com.mundolivre.forca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * Created by luis on 10/03/17.
 */
class LetterAdapter extends BaseAdapter {

    private String[] letters;
    private LayoutInflater letterInfl;

    public LetterAdapter(Context c) {
        letters = new String[26];
        for (int a = 0; a < letters.length; a++) {
            letters[a] = "" + (char)(a + 'A');
        }
        letterInfl = LayoutInflater.from(c);
    }
    @Override
    public int getCount() {
        return letters.length; //This represents the number of views, one for each letter.
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button letterBtn;
        if(convertView == null) {
            letterBtn = (Button) letterInfl.inflate(R.layout.letter, parent, false);
        } else {
            letterBtn = (Button) convertView;
        }
        letterBtn.setText(letters[position]);
        return letterBtn;
    }
}
