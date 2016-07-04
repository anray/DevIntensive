package com.softdesign.devintensive.ui.view.behaviours;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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

    float oldY = 0;

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
        int toolbarHeight = ((CollapsingToolbarLayout) ((AppBarLayout) parent.getChildAt(0)).getChildAt(0)).getChildAt(1).getHeight();
        int heightChange = (int) (dependency.getY()) * 72 / (160) * pxindp;


        if (dependency.getY() >= (72 + 64 + 24) * pxindp) {
            if (heightChange >= toolbarHeight) {
                lp.height = heightChange;
            }
        }

        child.setY(dependency.getY());
        child.setLayoutParams(lp);
        dependency.setPadding(dependency.getPaddingLeft(), lp.height, dependency.getPaddingRight(), dependency.getPaddingBottom());


/*
        //START another logic of header


        //get resizing step(scale). To set scaling proportion between app bar and rating menu. Usage: height of rating menu.Height() = scale*get(Y)
        float resizeScale;

        LinearLayout ratingMenu = (LinearLayout) parent.getChildAt(1); //rating menu
        int ratingMenuHeight = ratingMenu.getMinimumHeight();
        int toolbarHeight = ((CollapsingToolbarLayout) ((AppBarLayout) parent.getChildAt(0)).getChildAt(0)).getChildAt(1).getHeight(); //toolbar size
        int appbarHeight = parent.getChildAt(0).getHeight();


        resizeScale = ratingMenuHeight / ((float) (appbarHeight - toolbarHeight));


        //set Y position of rating menu
        child.setY(dependency.getY());

        //calculate and set height of rating menu
        int upDelta = (int) (ratingMenu.getHeight() - (oldY - dependency.getY()) * resizeScale); //the
        int downDelta = (int) (ratingMenu.getHeight() + (dependency.getY() - oldY) * resizeScale);

        if (oldY != 0) {
            if ((dependency.getY() - oldY) < 0) { //scroll up
                if (upDelta >= ratingMenu.getMinimumHeight()) {
                    lp.height = upDelta;
                } else {
                    lp.height = ratingMenu.getMinimumHeight();
                }
            } else { //scroll down
                if (upDelta <= ratingMenu.getMinimumHeight() * 2) {
                    lp.height = downDelta;
                } else {
                    lp.height = ratingMenu.getMinimumHeight()*2;
                }
            }
        }

        //
        child.setLayoutParams(lp);

        //set top padding of Nested Scroll
        dependency.setPadding(dependency.getPaddingLeft(), lp.height, dependency.getPaddingRight(), dependency.getPaddingBottom());

        //Log.d(ConstantManager.TAG_PREFIX, "dependency.getY()+oldY: " + dependency.getY() + " " + oldY);
        //Log.d(ConstantManager.TAG_PREFIX, "onDependentViewChanged,dependency.getY(): " + dependency.getY());
        //Log.d(ConstantManager.TAG_PREFIX, "onDependentViewChanged,dependency.getY(): " + dependency.getY());

        Log.d(ConstantManager.TAG_PREFIX, "ratingMenuHeight: " + ratingMenuHeight);
        Log.d(ConstantManager.TAG_PREFIX, "ratingMenu.getHeight(): " + ratingMenu.getHeight());
        Log.d(ConstantManager.TAG_PREFIX, "ratingMenu.getMinimumHeight(): " + ratingMenu.getMinimumHeight());
        //Log.d(ConstantManager.TAG_PREFIX, "appbarHeight: " + appbarHeight);
        //Log.d(ConstantManager.TAG_PREFIX, "toolbarHeight: " + toolbarHeight);
        //Log.d(ConstantManager.TAG_PREFIX, "LL sizes: " + parent.getChildAt(1).getMinimumHeight() + " " + parent.getChildAt(1).getHeight());
        Log.d(ConstantManager.TAG_PREFIX, "resizeScale: " + resizeScale);

        Log.d(ConstantManager.TAG_PREFIX, "onDependentViewChanged,dependency.getTranslationY(): " + dependency.getTranslationY());
        Log.d(ConstantManager.TAG_PREFIX, "onDependentViewChanged,dependency.getScrollY(): " + dependency.getScrollY());

        oldY = dependency.getY();
        //END another logic of header
*/
        return true;

    }


}
