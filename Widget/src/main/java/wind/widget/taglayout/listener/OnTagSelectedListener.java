package wind.widget.taglayout.listener;

import android.view.View;

import java.util.List;

import wind.widget.taglayout.TagBean;

/**
 * Author   :hymane
 * Email    :hymanme@163.com
 * Create at 2017-06-13
 * Description: 标签被选中或取消选中监听
 */
public interface OnTagSelectedListener {
    void selected(View view, int position, List<TagBean> selected);

    void unSelected(View view, int position, List<TagBean> selected);
}
