package org.dailyfreshbasket.co.in.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import org.dailyfreshbasket.co.in.R;

public class SortActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup rg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        rg=(RadioGroup)findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener(this);
    }
    public void close(int number){
        Intent returnIntent = new Intent();
        returnIntent.putExtra("type",number);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.d("Sort by",checkedId+"");
        switch (checkedId){
            case R.id.radio3:
                close(1);
                break;
            case R.id.radio4:
                close(2);
                break;
            case R.id.radio5:
                close(3);
                break;
            case R.id.radio6:
                close(4);
                break;
        }
    }
}
