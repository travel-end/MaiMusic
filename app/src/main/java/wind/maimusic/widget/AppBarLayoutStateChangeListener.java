package wind.maimusic.widget;


import com.google.android.material.appbar.AppBarLayout;

public abstract class AppBarLayoutStateChangeListener implements AppBarLayout.OnOffsetChangedListener{

    public enum BarState{
        EXPANDED,//展开
        COLLAPSED,//折叠
        INTERMEDIATE//中间状态
    }
    private BarState mCurrentState = BarState.INTERMEDIATE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (mCurrentState != BarState.EXPANDED) {
                onStateChanged(appBarLayout, BarState.EXPANDED);
            }
            mCurrentState = BarState.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != BarState.COLLAPSED) {
                onStateChanged(appBarLayout, BarState.COLLAPSED);
            }
            mCurrentState = BarState.COLLAPSED;
        } else {
            if (mCurrentState != BarState.INTERMEDIATE) {
                onStateChanged(appBarLayout, BarState.INTERMEDIATE);
            }
            mCurrentState = BarState.INTERMEDIATE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, BarState state);
}