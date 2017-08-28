package com.zx.searchdemo;

import android.app.Activity;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 封装的PopupWindow,默认背景半透明
 * <p>
 * 作者： 周旭 on 2017/7/10/0010.
 * 邮箱：374952705@qq.com
 * 博客：http://www.jianshu.com/u/56db5d78044d
 */

public class CustomPopupWindow implements PopupWindow.OnDismissListener {
    private PopupWindow mPopupWindow;
    private View contentView;
    private static Activity mActivity;
    private float bgAlpha; //背景透明度

    public CustomPopupWindow(Builder builder) {
        bgAlpha = builder.backgroundAlpha;
        contentView = LayoutInflater.from(mActivity).inflate(builder.contentViewId, null);
        mPopupWindow = new PopupWindow(contentView, builder.width, builder.height);
        //设置点击外部可以取消，必须和下面这个方法配合才有效
//        mPopupWindow.setOutsideTouchable(false);
        //设置一个空背景,设置了这个背景之后，设置点击外部取消才有效
//        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        mPopupWindow.setFocusable(false);
        //Popupwindow可以点击,PopupWindow弹出后，所有的触屏和物理按键都有PopupWindows处理。
        // 其他任何事件的响应都必须发生在PopupWindow消失之后， （home  等系统层面的事件除外）。
        // 比如这样一个PopupWindow出现的时候，按back键首先是让PopupWindow消失，
        // 第二次按才是退出activity，
        //解决Pop遮挡住虚拟键盘的问题
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        //让pop自适应输入状态
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (builder.animStyle != 0) {
            mPopupWindow.setAnimationStyle(builder.animStyle); //设置pop显示的动画效果
        }
        mPopupWindow.setOnDismissListener(this); //设置pop关闭的监听事件
    }

    /**
     * popup 消失
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }


    /**
     * 相对于窗体的显示位置
     *
     * @param view    可以为Activity中的任意一个View（最终的效果一样），
     *                会通过这个View找到其父Window，也就是Activity的Window。
     * @param gravity 在窗体中的位置，默认为Gravity.NO_GRAVITY
     * @param x       表示距离Window边缘的距离，方向由Gravity决定。
     *                例如：设置了Gravity.TOP，则y表示与Window上边缘的距离；
     *                而如果设置了Gravity.BOTTOM，则y表示与下边缘的距离。
     * @param y
     * @return
     */
    public CustomPopupWindow showAtLocation(View view, int gravity, int x, int y) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(view, gravity, x, y);
            setBackgroundAlpha(bgAlpha); //设置窗体的背景透明度为半透明
        }
        return this;
    }


    /**
     * 显示在anchor控件的正下方，或者相对这个控件的位置
     *
     * @param anchor 锚点
     * @param xOff   相对这个控件x方向的偏移
     * @param yOff   相对这个控件y方向的偏移
     * @return
     */
    public CustomPopupWindow showAsDropDown(View anchor, int xOff, int yOff) {
        if (mPopupWindow != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //7.0以上系统
                //获取目标控件在屏幕中的坐标位置  
                int[] location = new int[2];
                anchor.getLocationOnScreen(location);
                mPopupWindow.showAtLocation(anchor, Gravity.NO_GRAVITY, 0, location[1] + anchor.getHeight() + yOff);
            } else {
                mPopupWindow.showAsDropDown(anchor, xOff, yOff);
            }
            setBackgroundAlpha(bgAlpha); //设置窗体的背景透明度为半透明
        }
        return this;
    }
    
    
    public boolean isShowing(){
        if (mPopupWindow.isShowing()){
            return true;
        }
        return false;
    }

    /**
     * 根据id获取view
     *
     * @param viewId
     * @return
     */
    public View getItemView(int viewId) {
        if (mPopupWindow != null) {
            return contentView.findViewById(viewId);
        }
        return null;
    }


    /**
     * 根据id设置pop内部的控件的点击事件的监听
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getItemView(viewId);
        view.setOnClickListener(listener);
    }

    /**
     * 设置Activity或者Fragment的背景透明度
     *
     * @param bgAlpha 背景的透明度
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams layoutParams = mActivity.getWindow().getAttributes();
        layoutParams.alpha = bgAlpha; //0.0-1.0  
        mActivity.getWindow().setAttributes(layoutParams);
    }

    /**
     * builder 类
     */
    public static class Builder {
        private int contentViewId; //pop的布局文件
        private int width; //pop的宽度
        private int height;  //pop的高度
        private int animStyle; //动画效果
        private float backgroundAlpha = 0.5f; //背景的透明度，默认半透明

        public Builder(Activity activity) {
            mActivity = activity;
        }

        public Builder setContentView(int contentViewId) {
            this.contentViewId = contentViewId;
            return this;
        }

        public Builder setwidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setheight(int height) {
            this.height = height;
            return this;
        }


        public Builder setAnimationStyle(int animStyle) {
            this.animStyle = animStyle;
            return this;
        }

        public Builder setBackgroundAlpha(float backgroundAlpha) {
            this.backgroundAlpha = backgroundAlpha;
            return this;
        }

        public CustomPopupWindow build() {
            return new CustomPopupWindow(this);
        }
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1f); //设置窗体的背景透明度为不透明
    }
}
