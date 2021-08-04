package com.mingrisoft.yysb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mingrisoft.yysb.util.DetailActivity;

public class HelpActivity extends AppCompatActivity {
    LinearLayout linearLayout,linearLayout2;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        linearLayout = findViewById(R.id.ll);
        linearLayout2 = new LinearLayout(HelpActivity.this);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        scrollView = new ScrollView(HelpActivity.this);
        linearLayout.addView(scrollView);
        scrollView.addView(linearLayout2);
        /*ImageView imageView = new ImageView(HelpActivity.this);
        imageView.setImageResource(R.drawable.jqr);*/
        TextView textView = new TextView(HelpActivity.this);
        textView.setText(R.string.sysm);
        /*linearLayout2.addView(imageView);*/
        linearLayout2.addView(textView);
    }
}
