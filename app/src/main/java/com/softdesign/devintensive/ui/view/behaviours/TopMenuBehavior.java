package com.softdesign.devintensive.ui.view.behaviours;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.softdesign.devintensive.utils.ConstantManager;


/**
 * Created by anray on 29.06.2016.
 */
public class TopMenuBehavior extends CoordinatorLayout.Behavior<LinearLayout> {


    private Context mContext;

    public TopMenuBehavior(Context context, AttributeSet attrs) {
        this.mContext = context;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, LinearLayout child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, LinearLayout child, View dependency) {

        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        //CoordinatorLayout.LayoutParams lp2 = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();

        LinearLayout ratingMenu = (LinearLayout) parent.getChildAt(1); //rating menu

        float ratingMenuMinimumHeight = ratingMenu.getMinimumHeight();
        float toolbarHeight = ((CollapsingToolbarLayout) ((AppBarLayout) parent.getChildAt(0)).getChildAt(0)).getChildAt(1).getHeight(); //toolbar size
        float appbarHeight = parent.getChildAt(0).getHeight();
        float statusBar = 0;
        float resizeRatio;

        resizeRatio = (ratingMenuMinimumHeight) / (appbarHeight - toolbarHeight); //0.28

        //get StatusBar height
        statusBar = getStatusBarHeight();

        //Logic needed because app bar flows under status bar in API < 21
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        //if (dependency.getY() != appbarHeight) else if((appbarHeight-dependency.getY())<20) { //костыль для более плавное анимации раскрытия аппбара

        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            //for API>=21, where status bar size is considered while appbar is collapsed
            if (dependency.getY() < appbarHeight / 2) {
                lp.height = (int) (ratingMenuMinimumHeight + resizeRatio * (dependency.getY() - statusBar - toolbarHeight));
            } else {
                lp.height = (int) (ratingMenuMinimumHeight + resizeRatio * (dependency.getY() - toolbarHeight));
            }
        } else {
            //for API<21, where status bar size is NEVER considered
            lp.height = (int) (ratingMenuMinimumHeight + resizeRatio * (dependency.getY() - toolbarHeight));
        }


        if (ConstantManager.DEBUG)

        {
            Log.d(ConstantManager.TAG_PREFIX, "resizeRatio: " + resizeRatio);
            Log.d(ConstantManager.TAG_PREFIX, "ratingMenuMinimumHeight: " + ratingMenuMinimumHeight);
            Log.d(ConstantManager.TAG_PREFIX, "Plashka height: " + lp.height);
            Log.d(ConstantManager.TAG_PREFIX, "dependency.getY(): " + dependency.getY());
            Log.d(ConstantManager.TAG_PREFIX, "child.getY(): " + child.getY());
            Log.d(ConstantManager.TAG_PREFIX, "appbarHeight: " + appbarHeight);
            Log.d(ConstantManager.TAG_PREFIX, "toolbarHeight: " + toolbarHeight);
            Log.d(ConstantManager.TAG_PREFIX, "density: " + mContext.getResources().getDisplayMetrics().density);
            Log.d(ConstantManager.TAG_PREFIX, "dependency.getTranslationY(): " + dependency.getTranslationY());
        }


        //set sizes for Views
        child.setY(dependency.getY());
        child.setLayoutParams(lp);
        Log.d("onDependentViewChanged:", String.valueOf(dependency.getBottom()));

        dependency.setPadding(dependency.getPaddingLeft(), lp.height, dependency.getPaddingRight(), dependency.getPaddingBottom()); //20 нужно чтобы показывалась последняя строка в NestedScroll
        //lp2.topMargin = lp.height;
        //dependency.setLayoutParams(lp2);



        return true;

    }

    public float getStatusBarHeight() {
        float result = 0;
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
