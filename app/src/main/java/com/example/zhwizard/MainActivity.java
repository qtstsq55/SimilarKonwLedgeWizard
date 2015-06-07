package com.example.zhwizard;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.animator.AnimationFactory;
import com.example.animator.AnimatorValue;
import com.example.animator.AnimatorValueImplements;
import com.example.view.FirstView;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ViewPager viewager;
    private LinearLayout layout,layout_look,layout_qq,layout_sina;
    private ArrayList<View> list_views;
    public static final  int LENGTH=6;
    private static final float SHOWFROM =0.4f ;
    private   int  temp,index,status=-1;
    private FirstView firImage;
    private View layout_bg;
    private int red=9,green=42,blue=91;
    private int redEnd=0,greenEnd=150,blueEnd=255;
    private float pageDistance;
    private ImageView im_page_press;
    private TextView tv_guide_01;
    private String guideString[]=new String[]{
            "观而思-知乎漫游指南",
            "思而动-知乎漫游指南",
            "动而问-知乎漫游指南",
            "问而答-知乎漫游指南",
            "答而得-知乎漫游指南"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initValue();
    }

    private void findViews() {
        viewager=(ViewPager)findViewById(R.id.viewpager);//ViewPager
        layout_bg=  findViewById(R.id.layout_bg);//最外层背景颜色View
        im_page_press= (ImageView) findViewById(R.id.im_pagepress_01);//移动的滑块
        tv_guide_01= (TextView) findViewById(R.id.tv_guide_01);//底部的引导文字
        pageDistance=im_page_press.getWidth()+dip2px(3);//滑块移动距离
        layout_look= (LinearLayout) findViewById(R.id.layout_look);
        layout_qq= (LinearLayout) findViewById(R.id.layout_qq);
        layout_sina= (LinearLayout) findViewById(R.id.layout_sina);
    }

    private void initValue() {
        LayoutInflater  inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        list_views=new ArrayList<View>();
        //反射加载6个引导页面布局
        for(int i=0;i<LENGTH;i++){
            View view = inflater.inflate(R.layout.wizard_image_item,null);
            list_views.add(view);
        }
        viewager.setAdapter(new MyPagerAdapter(list_views));
        viewager.setOnPageChangeListener(new MyPageChangeListener());
    }


    private void animateNormal(int position,float showFrom,int showPix,int tempPosition){
        ArrayList<View> curList=new ArrayList<View>();//当前页面的View控件
        ArrayList<View> nextList=new ArrayList<View>();//下一个页面的View控件
        ArrayList<AnimatorValue> list_ani=new ArrayList<AnimatorValue>();
        View nextView=null,curView=null;
        if(tempPosition>0){
            curView = list_views.get(index);
        }else{
            curView = list_views.get(position);
        }
        if(index+1<list_views.size()) {
            nextView = list_views.get(index +1);
        }
        //加入每个页面需要动画的控件
        if(nextView!=null) {
            nextList.add(nextView.findViewById(R.id.im_0));
            nextList.add(nextView.findViewById(R.id.im_1_1));
            nextList.add(nextView.findViewById(R.id.im_1_2));
            nextList.add(nextView.findViewById(R.id.im_1_3));
            nextList.add(nextView.findViewById(R.id.im_2_1));
            nextList.add(nextView.findViewById(R.id.im_3_1));
            nextList.add(nextView.findViewById(R.id.im_3_2));
            nextList.add(nextView.findViewById(R.id.im_3_3));
        }
        if(curView!=null) {
            curList.add(curView.findViewById(R.id.im_0));
            curList.add(curView.findViewById(R.id.im_1_1));
            curList.add(curView.findViewById(R.id.im_1_2));
            curList.add(curView.findViewById(R.id.im_1_3));
            curList.add(curView.findViewById(R.id.im_2_1));
            curList.add(curView.findViewById(R.id.im_3_1));
            curList.add(curView.findViewById(R.id.im_3_2));
            curList.add(curView.findViewById(R.id.im_3_3));
        }
        //当前页面加载动画的实现
        if(tempPosition>0){
                    if(status==2){
                        for(int i=0;i<curList.size();i++){
                            AnimatorValue a=new  AnimatorValueImplements(curList.get(i),"TranslationX",(float)((i+1)*(temp*(1-showFrom))));
                            list_ani.add(a);
                        }
                    }else {
                        for (int i = 0; i < curList.size(); i++) {
                            AnimatorValue a = new AnimatorValueImplements(curList.get(i), "TranslationX", (float) ((showPix * (i + 1) * (1 - showFrom))));
                            list_ani.add(a);
                        }
                    }
        }else{
                for (int i = 0; i < curList.size(); i++) {
                    AnimatorValue a = new AnimatorValueImplements(curList.get(i), "TranslationX", (float) (-(showPix * (curList.size() - i))));
                    list_ani.add(a);
                }
        }
        //下一个页面加载动画的实现，SHOWFROM代表当前页面划过30%，加载下一个动画，具体可以自己设置
        if(showFrom<SHOWFROM){
            if(status==2){
                for (int i = 0; i < nextList.size(); i++) {
                    AnimatorValue a = new AnimatorValueImplements(nextList.get(i), "TranslationX", (float) ((showPix * (i + 1) * (1 - showFrom))));
                    list_ani.add(a);
                }
            }else{
                for(int i=0;i<nextList.size();i++){
                    AnimatorValue a=new  AnimatorValueImplements(nextList.get(i),"TranslationX",(float)(showPix*(i+1)));
                    list_ani.add(a);
                }
                temp=showPix;
            }
        }else{
            for(int i=0;i<nextList.size();i++){
                AnimatorValue a=new  AnimatorValueImplements(nextList.get(i),"TranslationX",(float)((i+1)*(temp*(1-showFrom))));
                list_ani.add(a);
            }
        }
        //背景动画，蓝色逐渐变淡
        AnimatorValue b1=new  AnimatorValueImplements(layout_bg,"backgroundColor", Color.rgb(red,green,blue),Color.rgb( (int) (red-(redEnd-red)/list_views.size()*(position+showFrom)), (int) (green+(greenEnd-green)/list_views.size()*(position+showFrom)), (int) (blue+(blueEnd-blue)/list_views.size()*(position+showFrom))));
        list_ani.add(b1);
        //滑块动画
        AnimatorValue b2=new  AnimatorValueImplements(im_page_press,"TranslationX",(pageDistance+im_page_press.getWidth())*(position+showFrom));
        list_ani.add(b2);
        //设置执行时间为0，也就是随着滑动实时动画
        AnimationFactory.getInstance().createEngine().startTogether(0,null,list_ani);
    }


   private  class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            status=arg0;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //判断左右滑方向，这里凭借这个是不准的，需要与status配合(这里加上TouchListener会更好理解，有机会再加吧)
            int tempPosition=index-position;
            //加载动画
            animateNormal(position, positionOffset, positionOffsetPixels, tempPosition);
        }

        @Override
        public void onPageSelected(int arg0) {
            index=arg0;
            if(index>0){
                tv_guide_01.setText(guideString[index-1]);
            }
            if(index==list_views.size()-1){
                layout_look.setVisibility(View.VISIBLE);
                layout_qq.setVisibility(View.VISIBLE);
                layout_sina.setVisibility(View.VISIBLE);
            }else{
                layout_look.setVisibility(View.GONE);
                layout_qq.setVisibility(View.GONE);
                layout_sina.setVisibility(View.GONE);
            }
            int height=tv_guide_01.getHeight();
            int width=tv_guide_01.getWidth();
            //底部引导文字动画，从下到上，从上到下
            AnimatorValue c1=new  AnimatorValueImplements(tv_guide_01,"TranslationY",-height/4f,-height/2f,-height*3/4f,(float)-height);
            c1.getAnimator().setDuration(100);
            c1.getAnimator().addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (index< guideString.length) {
                        tv_guide_01.setText(guideString[index]);
                    } else {
                        tv_guide_01.setText("");
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            ArrayList<AnimatorValue> list=new ArrayList<AnimatorValue>();
            AnimatorValue c2=new  AnimatorValueImplements(tv_guide_01,"TranslationY",(float)height,height*3/4f,height/2f,height/4f,0);
            c2.before(c1);
            c2.getAnimator().setDuration(100);
            list.add(c1);
            list.add(c2);
            //最后一个页面的动画
            if(index==5){
                AnimatorValue d1=new  AnimatorValueImplements(layout_qq,"TranslationX",(float)width/2);
                AnimatorValue d2=new  AnimatorValueImplements(layout_sina,"TranslationX",(float)-width/2);
                d1.before(c2);
                d1.getAnimator().setDuration(1000);
                d2.getAnimator().setDuration(1000);
                list.add(d1);
                list.add(d2);

                ImageView im_0= (ImageView) list_views.get(index).findViewById(R.id.im_0);
                AnimatorValue e1=new  AnimatorValueImplements(im_0,"TranslationY",-55f);
                e1.before(d1);
                e1.getAnimator().setInterpolator(new AccelerateDecelerateInterpolator());
                e1.getAnimator().setDuration(1000);
                list.add(e1);

            }

            AnimationFactory.getInstance().createEngine().startTogetherByLink(null,list.toArray(new AnimatorValue[list.size()]));
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews=mListViews;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            if(getCount()>1){
                //第一个页面的逐步显示图标的View，具体就是如前两篇博文写的一样，利用了自己重写的插值器来计算显示位置，缩放率等
                if(arg1==0){
                    FirstView image = (FirstView)(mListViews.get(arg1).findViewById(R.id.item_image));
                    image.clearimages();
                }
                ((ViewPager) arg0).removeView(mListViews.get(arg1));
            }
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            layout= (LinearLayout) mListViews.get(arg1).findViewById(R.id.layout);
            firImage = (FirstView) (mListViews.get(arg1).findViewById(R.id.item_image));
            ImageView im_0= (ImageView) mListViews.get(arg1).findViewById(R.id.im_0);
            ImageView im_1_1= (ImageView) mListViews.get(arg1).findViewById(R.id.im_1_1);
            ImageView im_1_2= (ImageView) mListViews.get(arg1).findViewById(R.id.im_1_2);
            ImageView im_1_3= (ImageView) mListViews.get(arg1).findViewById(R.id.im_1_3);
            ImageView im_2_1= (ImageView) mListViews.get(arg1).findViewById(R.id.im_2_1);
            ImageView im_3_1= (ImageView) mListViews.get(arg1).findViewById(R.id.im_3_1);
            ImageView im_3_2= (ImageView) mListViews.get(arg1).findViewById(R.id.im_3_2);
            ImageView im_3_3= (ImageView) mListViews.get(arg1).findViewById(R.id.im_3_3);
            //这里推荐使用异步加载ImageLoader等,不然OOM谁也受不了
             switch (arg1){
                case 0:
                    layout.setVisibility(View.GONE);
                    firImage.startAnimation();
                    break;
                case 1:
                    layout.setVisibility(View.VISIBLE);
                    im_0.setBackgroundResource(0);
                    im_1_1.setBackgroundResource(R.drawable.ic_guide_second_01);
                    im_2_1.setBackgroundResource(R.drawable.ic_guide_second_02);
                    im_3_1.setBackgroundResource(R.drawable.ic_guide_second_03);
                    break;
                case 2:
                    layout.setVisibility(View.VISIBLE);
                    im_0.setBackgroundResource(R.drawable.ic_guide_third_01);
                    im_1_1.setBackgroundResource(R.drawable.ic_guide_third_02);
                    im_2_1.setBackgroundResource(R.drawable.ic_guide_third_03);
                    im_3_1.setBackgroundResource(R.drawable.ic_guide_third_04);
                    break;
                case 3:
                    layout.setVisibility(View.VISIBLE);
                    im_0.setBackgroundResource(R.drawable.ic_guide_fourth_01);
                    im_1_1.setBackgroundResource(R.drawable.ic_guide_fourth_02);
                    im_2_1.setBackgroundResource(R.drawable.ic_guide_fourth_03);
                    im_3_1.setBackgroundResource(R.drawable.ic_guide_fourth_04);
                    break;
                case 4:
                    layout.setVisibility(View.VISIBLE);
                    im_0.setBackgroundResource(R.drawable.ic_guide_fifth_01);
                    im_1_1.setBackgroundResource(R.drawable.ic_guide_fifth_02);
                    im_1_2.setBackgroundResource(R.drawable.ic_guide_fifth_03);
                    im_1_3.setBackgroundResource(R.drawable.ic_guide_fifth_04);
                    im_2_1.setBackgroundResource(R.drawable.ic_guide_fifth_05);
                    im_2_1.setScaleX(0.5f);
                    im_3_1.setBackgroundResource(R.drawable.ic_guide_fifth_06);
                    im_3_2.setBackgroundResource(R.drawable.ic_guide_fifth_07);
                    im_3_3.setBackgroundResource(R.drawable.ic_guide_fifth_08);
                    break;
                case 5:
                    im_0.setBackgroundResource(R.drawable.guide_logo);
                    break;
            }
            ((ViewPager)arg0).addView(mListViews.get(arg1));
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

   private int dip2px(float dpValue){
       float scale=getResources().getDisplayMetrics().density;
       return (int) (dpValue*scale+.5f);
   }

}