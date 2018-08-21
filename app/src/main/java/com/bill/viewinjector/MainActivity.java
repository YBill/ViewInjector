package com.bill.viewinjector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bill.annotation.BindView;
import com.bill.viewinjecterapi.ViewInjector;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView nameTv;

    @BindView(R.id.tv_sex)
    TextView sexTv;

    @BindView(R.id.btn)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        MainActivity$ViewInjector injector = new MainActivity$ViewInjector(this);
        ViewInjector.bind(this);

        nameTv.setText("Bill");
        sexTv.setText("Male");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
