package wind.widget.jrecyclerview.config;


import wind.widget.jrecyclerview.anim.IBaseAnimation;
import wind.widget.jrecyclerview.anim.SlideInBottomAnimation;
import wind.widget.jrecyclerview.loadview.base.IBaseLoadMoreView;
import wind.widget.jrecyclerview.loadview.base.IBaseRefreshLoadView;

/**
 * author       : Jiang Pengyong
 * time         : 2019-04-17 12:31
 * email        : 56002982@qq.com
 * desc         : JRecycleView 的配置管理
 * version      : 1.0.0
 */

public class JRecycleViewManager {

    private static final JRecycleViewManager INSTANCE = new JRecycleViewManager();

    // 下拉刷新视图
    private IBaseRefreshLoadView mRefreshLoadView;
    // 上拉更多视图
    private IBaseLoadMoreView mLoadMoreView;

    // 动画
    private IBaseAnimation[] mItemAnimations;

    // 是否处于 debug
    private boolean mIsDebug;

    public static JRecycleViewManager getInstance() {
        return INSTANCE;
    }

    private JRecycleViewManager() {
        mItemAnimations = new IBaseAnimation[]{new SlideInBottomAnimation()};
        mIsDebug = false;
    }

    public IBaseRefreshLoadView getRefreshLoadView() {
        return mRefreshLoadView;
    }

    public JRecycleViewManager setRefreshLoadView(IBaseRefreshLoadView refreshLoadView) {
        this.mRefreshLoadView = refreshLoadView;
        return this;
    }

    public IBaseLoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    public JRecycleViewManager setLoadMoreView(IBaseLoadMoreView loadMoreView) {
        this.mLoadMoreView = loadMoreView;
        return this;
    }

    public IBaseAnimation[] getItemAnimations() {
        return mItemAnimations;
    }

    public JRecycleViewManager setItemAnimations(IBaseAnimation[] itemAnimations) {
        this.mItemAnimations = itemAnimations;
        return this;
    }

    public boolean isDebug() {
        return mIsDebug;
    }

    public JRecycleViewManager setIsDebug(boolean isDebug) {
        this.mIsDebug = isDebug;
        return this;
    }
}
