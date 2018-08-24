package com.bill.viewinjector;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bill.annotation.BindView;
import com.bill.viewinjecterapi.ViewInjector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 2018/8/22.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<String> list = new ArrayList<>();

    public MyAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 20; i++) {
            list.add("Hello World!" + (i + 1));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview)
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ViewInjector.bind(this, itemView);
        }

        private void update(int position) {
            if (textView != null)
                textView.setText(list.get(position));
        }

    }

}