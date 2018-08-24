package com.bill.viewinjector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bill.annotation.BindView;
import com.bill.annotation.OnClick;
import com.bill.viewinjecterapi.ViewInjector;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView nameTv;

    @BindView(R.id.tv_sex)
    TextView sexTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.bind(this);

        nameTv.setText("Bill");
        sexTv.setText("Male");

    }

    @OnClick(R.id.btn)
    public void myClick() {
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
        startActivity(intent);
    }

}
