package com.example.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyAdapter extends ArrayAdapter<TwoStrings> {

        private int layoutResource;

        public MyAdapter(Context context, int layoutResource, List<TwoStrings> twoStringsList) {
            super(context, layoutResource, twoStringsList);
            this.layoutResource = layoutResource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                view = layoutInflater.inflate(layoutResource, null);
            }

            TwoStrings twoStrings = getItem(position);

            if (twoStrings != null) {
                TextView upTextView = (TextView) view.findViewById(R.id.textView1);
                TextView downTextView = (TextView) view.findViewById(R.id.textView2);

                if (upTextView != null) {
                    upTextView.setText(twoStrings.getUp());
                }
                if (downTextView != null) {
                    downTextView.setText(twoStrings.getDown());
                }
            }

            return view;
        }
    }


/*
 List<String> listTitle;
    List<String> listTime;

    Context context;

    public MyAdapter(@NonNull Context context, List<String> title, List<String> time) {
        super(context, R.layout.clistview, R.id.textView2, time);

        this.listTitle = title;
        this.listTime = time;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.clistview, parent, false);
        TextView textV1 = view.findViewById(R.id.textView1);
        TextView textV2 = view.findViewById(R.id.textView2);
        textV1.setText(listTitle.get(position));
        textV2.setText(listTime.get(position));

        return view;
    }
 */
