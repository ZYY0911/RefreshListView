package cqc.com.refreshlistviewdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    private ListView2 listView;
    private List<String> dataList;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();

        listView = findViewById(R.id.listView);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);


        listView.setOnRefreshListener(new ListView2.OnRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataList.add(0,"header");
                        adapter.notifyDataSetChanged();
                        listView.finishRefresh();
                    }
                },2000);
            }
        });

        listView.setOnLoadMoreListener(new ListView2.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataList.add("footer");
                        adapter.notifyDataSetChanged();
                        listView.finishLoadMore();
                    }
                },2000);
            }
        });
    }

    private void initData() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add("item" + i);
        }
    }


    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = new TextView(Main2Activity.this);
            tv.setText(dataList.get(position));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
            return tv;
        }
    }
}
