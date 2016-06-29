package com.softdesign.devintensive.ui.view.behaviours;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;


/**
 * Created by anray on 29.06.2016.
 */
public class TopMenuBehavior extends CoordinatorLayout.Behavior<LinearLayout> {


    public TopMenuBehavior(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        int pxindp = TypedValue.COMPLEX_UNIT_DIP;


        if (dependency.getY() >= (72 + 64 + 24) * pxindp) {
            lp.height = (int) (dependency.getY()) * 72 / (160) * pxindp;
        }

        dependency.setPadding(dependency.getPaddingLeft(), lp.height, dependency.getPaddingRight(), dependency.getPaddingBottom());


        child.setY(dependency.getY());
        child.setLayoutParams(lp);
        return true;

    }

}
