package com.example.kush.tracker_final;
/**
 * Created by luv on 26/3/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;



public class CustomListAdapter extends ArrayAdapter<String> {


    Context context;
    ArrayList<String> Text;
    ArrayList<Integer> images;


    public CustomListAdapter(@NonNull Context context, @NonNull ArrayList<String> text1, @NonNull ArrayList<Integer> allContent1 ) {
        super(context, 0, text1);
        Log.i("reach", "CustomListAdapter: ");
        this.context = context;
        Text = text1;
        images = allContent1;
    }

    @Override
    public View getView(int position,View view,ViewGroup parent) {

        View rowView = View.inflate(context, R.layout.listview, null);

        ImageView IV = (ImageView) rowView.findViewById(R.id.image);
        TextView TV = (TextView) rowView.findViewById(R.id.text);
        RelativeLayout RR = (RelativeLayout) rowView.findViewById(R.id.RL);

        RR.setTag(position);

        IV.setImageResource(images.get(position));
        TV.setText(Text.get(position));

        return rowView;
    }
}
