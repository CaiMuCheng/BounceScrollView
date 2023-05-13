package com.mucheng.wonder.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

/**
 * 弹性水平滑动控件
 * 在滑动到左端 / 右端超出边缘后松手会有一个回弹效果
 * 我发现有人用 Translation 实现的，但是这样放在约束布局里面会出现穿透父布局的情况
 * <p>
 * Author: SuMuCheng
 * QQ: 3578557729
 * */
public class BounceHorizontalScrollView extends HorizontalScrollView {

    private float x;

    private final Rect normal = new Rect();

    private boolean isCount = false;
    private float lastX = 0;
    private float lastY = 0;
    private float distanceX = 0;
    private float distanceY = 0;
    private boolean leftRightSlide = false;

    public BounceHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    private View firstChild() {
        return getChildAt(0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            distanceX = currentX - lastX;
            distanceY = currentY - lastY;
            if (Math.abs(distanceX) >= Math.abs(distanceY) && Math.abs(distanceX) > 12) {
                leftRightSlide = true;
            }
        }
        lastX = currentX;
        lastY = currentY;
        if (leftRightSlide && getChildCount() > 0) commOnTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }


    public void commOnTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()) {
                    animation();
                    isCount = false;
                }
                clear0();
                break;
            case MotionEvent.ACTION_MOVE:
                final float preX = x;
                float nowX = ev.getX();
                int deltaX = (int) (preX - nowX);
                if (!isCount) {
                    deltaX = 0;
                }

                x = nowX;
                if (isNeedMove()) {
                    View inner = firstChild();
                    if (normal.isEmpty()) {
                        normal.set(inner.getLeft(), inner.getTop(),
                                inner.getRight(), inner.getBottom());
                    }
                    inner.layout(inner.getLeft() - deltaX / 2, inner.getTop(),
                            inner.getRight() - deltaX / 2, inner.getBottom());
                }
                isCount = true;
                break;

            default:
                break;
        }
    }


    public void animation() {
        View inner = firstChild();
        TranslateAnimation ta = new TranslateAnimation(inner.getLeft(), normal.left, 0,
                0);
        ta.setDuration(200);
        inner.startAnimation(ta);
        inner.layout(normal.left, normal.top, normal.right, normal.bottom);
        normal.setEmpty();

    }


    public boolean isNeedAnimation() {
        return !normal.isEmpty();
    }


    public boolean isNeedMove() {
        int offset = firstChild().getMeasuredWidth() - getWidth();
        int scrollX = getScrollX();
        return scrollX == 0 || scrollX == offset;
    }

    private void clear0() {
        lastX = 0;
        lastY = 0;
        distanceX = 0;
        distanceY = 0;
        leftRightSlide = false;
    }

}
