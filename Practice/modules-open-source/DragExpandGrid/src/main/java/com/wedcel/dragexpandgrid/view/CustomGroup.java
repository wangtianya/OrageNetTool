/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
package com.wedcel.dragexpandgrid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.wangtianya.practice.lib.draggrid.R;

import java.util.ArrayList;

public class CustomGroup extends ViewGroup {

    private CustomAboveView mCustomAboveView;
    private CustomBehindParent mCustomBehindParent;
    private boolean isEditModel = false;
    public static final int COLUMNUM = 4;
    private Context mContext;
    private ArrayList<DragIconInfo> allInfoList = new ArrayList<>();
    private InfoEditModelListener editModelListener;

    public interface InfoEditModelListener {
        void onModleChanged(boolean isEditModel);
    }

    public CustomGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        LayoutParams upParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mCustomAboveView = new CustomAboveView(context, this);
        addView(mCustomAboveView, upParams);
        LayoutParams downParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mCustomBehindParent = new CustomBehindParent(mContext, this);
        addView(mCustomBehindParent, downParams);

        demo();
    }


    public CustomGroup(Context context) {
        this(context, null);
    }

    public InfoEditModelListener getEditModelListener() {
        return editModelListener;
    }

    public void setEditModelListener(InfoEditModelListener editModelListener) {
        this.editModelListener = editModelListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasure;
        int heightMeasure;
        if (isEditModel) {
            mCustomBehindParent.measure(widthMeasureSpec, heightMeasureSpec);
            widthMeasure = mCustomBehindParent.getMeasuredWidth();
            heightMeasure = mCustomBehindParent.getMeasuredHeight();
        } else {
            mCustomAboveView.measure(widthMeasureSpec, heightMeasureSpec);
            widthMeasure = mCustomAboveView.getMeasuredWidth();
            heightMeasure = mCustomAboveView.getMeasuredHeight();
        }
        setMeasuredDimension(widthMeasure, heightMeasure);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isEditModel) {
            int behindHeight = mCustomBehindParent.getMeasuredHeight();
            mCustomBehindParent.layout(l, 0, r, behindHeight + t);
        } else {
            int aboveHeight = mCustomAboveView.getMeasuredHeight();
            mCustomAboveView.layout(l, 0, r, aboveHeight + t);
        }
    }

    private ArrayList<DragIconInfo> initAllOriginalInfo() {
        ArrayList<DragIconInfo> iconInfoList = new ArrayList<>();
        iconInfoList.add(new DragIconInfo("1", R.drawable.ic_launcher));
        iconInfoList.add(new DragIconInfo("2", R.drawable.ic_launcher));
        iconInfoList.add(new DragIconInfo("3", R.drawable.ic_launcher));
        iconInfoList.add(new DragIconInfo("4", R.drawable.ic_launcher));
        iconInfoList.add(new DragIconInfo("5", R.drawable.ic_launcher));
        iconInfoList.add(new DragIconInfo("6", R.drawable.ic_launcher));
        return iconInfoList;
    }

    public boolean isEditModel() {
        return isEditModel;
    }

    public void cancleEidtModel() {
        setEditModel(false, 0);
    }

    public void setEditModel(boolean isEditModel, int position) {
        this.isEditModel = isEditModel;
        if (isEditModel) {
            mCustomAboveView.setViewCollaps();
            mCustomAboveView.setVisibility(View.GONE);
            mCustomBehindParent.notifyDataSetChange(mCustomAboveView.getIconInfoList());
            mCustomBehindParent.setVisibility(View.VISIBLE);
            mCustomBehindParent.drawWindowView(position, mCustomAboveView.getFirstEvent());
        } else {
            mCustomAboveView.refreshIconInfoList(allInfoList);
            mCustomAboveView.setVisibility(View.VISIBLE);
            mCustomBehindParent.setVisibility(View.GONE);
            if (mCustomBehindParent.isModifyedOrder()) {
                mCustomBehindParent.cancleModifyOrderState();
            }
            mCustomBehindParent.resetHidePosition();
        }
        if (editModelListener != null) {
            editModelListener.onModleChanged(isEditModel);
        }
    }

    public void sendEventBehind(MotionEvent ev) {
        mCustomBehindParent.childDispatchTouchEvent(ev);
    }

    public void dispatchSingle(DragIconInfo dragInfo) {
        if (dragInfo == null) {
            return;
        }
        Toast.makeText(mContext, "点击了icon" + dragInfo.name, Toast.LENGTH_SHORT).show();

    }

    public boolean isValideEvent(MotionEvent ev, int scrolly) {
        return mCustomBehindParent.isValideEvent(ev, scrolly);
    }

    public void clearEditDragView() {
        mCustomBehindParent.clearDragView();
    }



    public void setData(ArrayList<DragIconInfo> datas) {
        allInfoList.clear();
        allInfoList.addAll(datas);
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).index = i;
        }

        mCustomAboveView.refreshIconInfoList(allInfoList);
        mCustomBehindParent.refreshIconInfoList(allInfoList);
    }

    public void setItemClickListener(CustomAboveView.ItemClickListener gridViewClickListener) {
        mCustomAboveView.setGridViewClickListener(gridViewClickListener);
    }
    public ArrayList<DragIconInfo> getCurrentDatas() {
        return new ArrayList<>(allInfoList);
    }


    public void demo() {
        setItemClickListener(new CustomAboveView.ItemClickListener() {
            @Override
            public void onSingleClicked(DragIconInfo iconInfo) {
                dispatchSingle(iconInfo);
            }
        });
        setData(initAllOriginalInfo());
    }

}
