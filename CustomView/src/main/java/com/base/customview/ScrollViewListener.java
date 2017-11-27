package com.base.customview;

/**
 * @author 56896
 * @date 2016/12/20
 */

public interface ScrollViewListener {
    /**
     * 当ScrollView滑动的时候触发
     *
     * @param scrollView
     * @param x
     * @param y
     * @param oldx
     * @param oldy
     */
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);
}
