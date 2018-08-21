package com.bill.viewinjector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bill.annotation.BindView;
import com.bill.viewinjecterapi.ViewInjector;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView nameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//        Main2Activity$ViewInjector injector = new Main2Activity$ViewInjector(this);
        ViewInjector.bind(this);

        nameTv.setText("Main2");
    }
}
