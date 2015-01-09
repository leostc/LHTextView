package com.lihao.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by lihao-xy on 2015/1/5.
 */
public class LHTextView extends RelativeLayout {
    private Context mContext;
    private TextView mContentTextView;
    private Button rightBtn, leftBtn;
    private int statusBarHeight = 0;
    final private String text = "1, 把西点军校学员学雷锋'的谎言引进中国，是我一生中所犯的最大错误之一。本人对自已一切言论负全责，特承认错误、道歉。”昨日，网友“老李头06”在微博上说。这位实名为李竹润的已退休媒体人向南都记者回忆，在上世纪80年代初编写新闻写作教材时，他误将西方媒体上的这则“愚人节新闻”引入教材中，后来撰写其他稿件时也多次使用，以致广为流传。" +
            "从写作技巧出发选错案例 “我不假思索，便把它写进文章(署笔名黎信)。”注意到日前再度有网友讨论“美国西点军校学雷锋”，李竹润昨日一大早转发相关微博并称，他于1981年愚人节将外电播发的西点军校学员学雷锋的假消息，误当成真事写到了文章里，直到十几年后在《读书》杂志看到一篇澄清的文章，才知道自己上了当。" +
            "检索显示，从被媒体报道到引发网络热议，“西点军校学雷锋”的话题不时出现，经久不衰。这个据称于20世纪80年代末、90年代初开始流传的说法，其主要内容是：“走进西点军校，人们首先发现校园内一尊雷锋的半身塑像，会议大厅挂着5位他们所仰慕的英雄像，排在首位的是我国伟大的战士楷模雷锋，学校还把雷锋日记中一些名言印在学员学习手册扉页上……" +
            "不过，按照李竹润的说法，最早引入这个说法的是他，而且是在其编写的新闻写作教材里。李竹润向南都记者回忆，1981年前后，自己一方面在媒体工作，一方面任教于成立不久的中国社科院研究生院新闻系，后在编写函授教材时注意到这个消息，“我印象里是在U PI(注：合众国际社)上看到一篇通讯，写的是西点军校的学员学雷锋，他们不 1";
    private String contentText;
    private ArrayList<Integer> offsetArrayList;

    public LHTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflateView();
        initListener();
    }

    private void inflateView() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_lh_textview, this);
        mContentTextView = (TextView) findViewById(R.id.textview_content);
        rightBtn = (Button) findViewById(R.id.button_right);
        leftBtn = (Button) findViewById(R.id.button_left);
        contentText = text;
        offsetArrayList = new ArrayList<Integer>();
        mContentTextView.setText(contentText);

        mContentTextView.setPadding(mContentTextView.getPaddingLeft(), mContentTextView.getPaddingTop(),
                mContentTextView.getPaddingRight(), getPaddingBottom(mContentTextView));
    }

    private void initListener() {
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMetrics display = mContext.getResources().getDisplayMetrics();
                int height = display.heightPixels;
                int width = display.widthPixels;
                int offset = TextViewUtils.getOffsetForPosition(mContentTextView, width - mContentTextView.getPaddingRight(),
                        height - getStatusBarHeight() - mContentTextView.getPaddingBottom());
                if (offset != -1 && offset < contentText.length()) {
                    contentText = contentText.substring(offset + 1);
                    mContentTextView.setText(contentText);
                    offsetArrayList.add(offset);
                }
            }
        });
        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (offsetArrayList.size() > 0) {
                    int lastOffset = offsetArrayList.get(offsetArrayList.size() - 1);
                    int offset = text.length() - contentText.length() - lastOffset - 1;
                    contentText = text.substring(offset);
                    mContentTextView.setText(contentText);
                    offsetArrayList.remove(offsetArrayList.size() - 1);
                }
            }
        });
    }

    /**
     * 获取重新规划后的文字排版
     *
     * @param textView
     * @return
     */
    private int getPaddingBottom(TextView textView) {
        int bottom = 0;
        DisplayMetrics display = mContext.getResources().getDisplayMetrics();
        int height = display.heightPixels;
        int textHeight = height - getStatusBarHeight() - textView.getPaddingBottom() - textView.getPaddingTop();
        double lineFloat = textHeight * 1.0 / textView.getLineHeight();
        int line = (int) lineFloat;
        double lineRemainder = lineFloat - line;
        if (lineRemainder < 0.9) {
            lineRemainder = lineRemainder + 0.1;
        }
        bottom = textView.getPaddingBottom() + (int) (lineRemainder * textView.getLineHeight());
        return bottom;
    }

    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}
