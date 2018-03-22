package cqc.com.refreshlistviewdemo;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private ListView3 listView;

    private Context context;
    private List<String> dataList =  new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        tv = findViewById(R.id.tv);
        tv.setPadding(0, -50, 0, 0);

        listView = findViewById(R.id.listView);

        for (int i = 0; i < 30; i++) {
            dataList.add("item"+i);
        }

        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new ListView3.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(context, "刷新成功");
                        dataList.add(0,"header  header  header");
                        adapter.notifyDataSetChanged();
                        listView.finishRefresh();
                    }
                }, 2000);
            }
        });

        listView.setOnLoadMoreListener(new ListView3.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(context, "上拉加载成功");
                        dataList.add("footer  footer  footer  ");
                        adapter.notifyDataSetChanged();
                        listView.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }



    public class MyAdapter extends BaseAdapter{

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
            TextView tv = new TextView(MainActivity.this);
            tv.setText(dataList.get(position));
            tv.setTextSize(16);
            return tv;
        }
    }
}
