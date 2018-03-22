package cqc.com.refreshlistviewdemo;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private RefreshListView listView;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        tv = findViewById(R.id.tv);
        tv.setPadding(0, -50, 0, 0);

        listView = findViewById(R.id.listView);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.province, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(context, "刷新成功");
                        listView.finishRefresh();
                    }
                }, 2000);
            }
        });

        listView.setOnLoadMoreListener(new RefreshListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showShortToast(context, "上拉加载成功");
                        listView.finishLoadMore();
                    }
                }, 2000);
            }
        });
    }
}
