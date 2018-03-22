package cqc.com.refreshlistviewdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by a on 2018/3/22.
 * 下拉刷新
 * <p>
 * 根据下拉距离 设置不同的state ,再根据state执行相应的操作
 */

public class ListView2 extends ListView implements AbsListView.OnScrollListener {

    private View headerView;
    private TextView tvStateHeader;
    private int headerViewHeight;

    //headerView的3个状态
    public static final int DOWN_REFRESH = 1;
    public static final int RELEASE_REFRESH = 2;
    public static final int REFRESHING = 3;
    private int currentStateHeader = DOWN_REFRESH;

    private int downY;
    private int moveY;

    private View footerView;
    private TextView tvFooter;
    private int footerViewHeight;

    //footerView的2种状态
    public static final int LOAD_MORE = 4;
    public static final int LOAD_MORE_ING = 5;
    public int currentStateFooter = LOAD_MORE;


    public ListView2(Context context) {
        this(context, null);
    }

    public ListView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();

        setOnScrollListener(this);
    }

    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.footer_view, null);
        tvFooter = footerView.findViewById(R.id.tv_footer);

        footerView.measure(0, 0);
        footerViewHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, -footerViewHeight, 0, 0);

        addFooterView(footerView);
    }

    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.header_view, null);
        tvStateHeader = headerView.findViewById(R.id.tv_state_header);

        headerView.measure(0, 0);
        headerViewHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -headerViewHeight, 0, 0);

        addHeaderView(headerView);
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
                if (instance > 0 && getFirstVisiblePosition() == 0 && currentStateHeader != REFRESHING) {
                    int paddingTop = -headerViewHeight + instance;
                    if (paddingTop < 0) {//&& currentStateHeader != RELEASE_REFRESH
                        currentStateHeader = DOWN_REFRESH;
                        tvStateHeader.setText("下拉刷新");
                    } else if (paddingTop > 0) {//&& currentStateHeader == DOWN_REFRESH
                        currentStateHeader = RELEASE_REFRESH;
                        tvStateHeader.setText("释放刷新");
                    }
                    //对paddingTop进行限制，paddingTop<=3*headerViewHeight
                    if (paddingTop > 3 * headerViewHeight) {
                        headerView.setPadding(0, 3 * headerViewHeight, 0, 0);
                    } else {
                        headerView.setPadding(0, paddingTop, 0, 0);
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (currentStateHeader == RELEASE_REFRESH) {
                    //刷新中
                    currentStateHeader = REFRESHING;
                    headerView.setPadding(0, 0, 0, 0);
                    tvStateHeader.setText("刷新中");
                    if (onRefreshListener != null) {
                        onRefreshListener.refresh();
                    }
                } else if (currentStateHeader == DOWN_REFRESH) {
                    headerView.setPadding(0, -headerViewHeight, 0, 0);
                }
                break;
        }

        return super.onTouchEvent(ev);
    }


    /**
     * 结束下拉刷新
     */
    public void finishRefresh() {
        headerView.setPadding(0, -headerViewHeight, 0, 0);
        currentStateHeader = DOWN_REFRESH;
    }


    /**
     * 下拉刷新回调
     */
    public interface OnRefreshListener {
        void refresh();
    }

    private OnRefreshListener onRefreshListener;

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_FLING || scrollState == SCROLL_STATE_IDLE) {
            if (getLastVisiblePosition() == getCount() - 1 && currentStateFooter == LOAD_MORE) {
                currentStateFooter = LOAD_MORE_ING;
                footerView.setPadding(0, 0, 0, 0);
                setSelection(getCount());
                if (onLoadMoreListener != null) {
                    onLoadMoreListener.loadMore();
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    /**
     * 加载更多回调
     */
    public interface OnLoadMoreListener {
        void loadMore();
    }

    private OnLoadMoreListener onLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void finishLoadMore() {
        currentStateFooter = LOAD_MORE;
        footerView.setPadding(0, -footerViewHeight, 0, 0);
    }
}
