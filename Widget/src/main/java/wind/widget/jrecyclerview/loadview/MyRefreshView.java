package wind.widget.jrecyclerview.loadview;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import wind.widget.R;
import wind.widget.jrecyclerview.loadview.base.IBaseRefreshLoadView;
import wind.widget.jrecyclerview.loadview.bean.MoveInfo;


public class MyRefreshView extends IBaseRefreshLoadView {

    private RelativeLayout mLoadView;
    private View mIvMeituan;

    public MyRefreshView(Context context) {
        super(context);
    }

    public MyRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View initView(Context context) {
        this.mLoadView = (RelativeLayout) LayoutInflater.from(context)
                .inflate(R.layout.view_mai_refresh,
                        this,
                        false);

        this.mIvMeituan = this.mLoadView.findViewById(R.id.iv_mai_refresh);
        this.mIvMeituan.setBackgroundResource(R.drawable.temp_pull);

        return mLoadView;
    }

    @Override
    protected View getLoadView() {
        return mLoadView;
    }

    @Override
    protected void onPullToAction() {

    }

    @Override
    protected void onReleaseToAction() {

    }

    @Override
    protected void onExecuting() {
        this.mIvMeituan.clearAnimation();
        this.mIvMeituan.setBackgroundResource(R.drawable.meituan_refreshing_anim);
        AnimationDrawable anim = (AnimationDrawable) this.mIvMeituan.getBackground();
        anim.start();
    }

    @Override
    protected void onDone() {
        this.mIvMeituan.clearAnimation();
        this.mIvMeituan.setBackgroundResource(R.drawable.temp_pull);
    }

    @Override
    protected void onMoving(MoveInfo moveInfo) {
        this.mIvMeituan.clearAnimation();

        if (moveInfo.getPercent() < 100) {
            ViewGroup.LayoutParams layoutParams = this.mIvMeituan.getLayoutParams();
            int height = moveInfo.getViewHeight() * moveInfo.getPercent() / 100;
            layoutParams.height = height;
            layoutParams.width = height;
            this.mIvMeituan.setBackgroundResource(R.drawable.temp_pull);
        } else {
            this.mIvMeituan.clearAnimation();
            this.mIvMeituan.setBackgroundResource(R.drawable.meituan_pull_to_refresh_anim);
            AnimationDrawable anim = (AnimationDrawable) this.mIvMeituan.getBackground();
            anim.start();
        }

    }
}
