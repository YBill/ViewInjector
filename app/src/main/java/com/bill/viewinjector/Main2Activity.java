package com.bill.viewinjector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bill.annotation.BindView;
import com.bill.viewinjecterapi.ViewInjector;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView nameTv;

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ViewInjector.bind(this);

        nameTv.setText("Main2");

        myAdapter = new MyAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(myAdapter);
    }
}
