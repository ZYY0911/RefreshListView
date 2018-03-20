package cqc.com.refreshlistviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by cui on 2018/3/20
 * 先添加headerView footerView，
 * 在onTouchEvent中 down move up 中判断状态
 * 下拉刷新过程有3种状态： 下拉刷新  +  释放刷新 +刷新中
 * 上拉加载过程有2种状态： 上拉加载  + 加载中
 * <p>
 * 下拉刷新中：
 * move中判断是 下拉刷新 还是 释放刷新
 * up中进行刷新操作
 * <p>
 * <p>
 * <p>
 * 下拉刷新：headerView开始慢慢显示到完全显示出来
 * 释放刷新：headerView完全显示出来，但是你还在下拉，move没有停止
 * 刷新你中：headerView完全显示出来，手指离开屏幕 ACTION_UP
 */

public class RefreshListView extends ListView {

    //header
    private View headerView;
    private FrameLayout flHeader;
    private ImageView ivArrowHeader;
    private TextView tvStateHeader;
    private int headerViewHeight;


    private int downY;
    private int moveY;

    //设置3种下拉刷新状态
    public final int DOWN_REFRESH = 1;
    public final int RELEASE_REFRESH = 2;
    public final int REFRESHING = 3;

    private int currentState = DOWN_REFRESH;//当前状态是下拉刷新


    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtil.d("RefreshListView");
        initHeaderView();
        initFooterView();
    }


    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.header_view, null);
        flHeader = findViewById(R.id.fl_header);
        ivArrowHeader = findViewById(R.id.iv_arrow_header);
        tvStateHeader = findViewById(R.id.tv_state_header);

        headerView.measure(0, 0);
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        addHeaderView(headerView);
    }

    private void initFooterView() {

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = (int) ev.getY();
                int instance = moveY - downY;
                int paddingTop = -headerViewHeight + instance;
                if (instance > 0 && getFirstVisiblePosition() == 0 && currentState != REFRESHING) {
                    headerView.setPadding(0, paddingTop, 0, 0);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return super.onTouchEvent(ev);
    }
}
