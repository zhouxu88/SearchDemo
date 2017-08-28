package com.zx.searchdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * 展示搜索结果
 */
public class SearchResultActivity extends AppCompatActivity {

    private TextView resultTv; //搜索结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        resultTv = (TextView) findViewById(R.id.search_result_tv);
        String result = getIntent().getStringExtra("result");
        resultTv.setText(result);
    }
}
