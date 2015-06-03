package com.example.zhwizard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.animator.AnimationFactory;
import com.example.animator.AnimatorValue;
import com.example.animator.AnimatorValueImplements;
import com.example.view.FirstView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ViewPager viewager;
    private LinearLayout layout;
    private ArrayList<View> list_views;
    public static final  int LENGTH=5;
    private static final float SHOWFROM =0.4f ;
    private   int  temp,index,status=-1;
    private FirstView firImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initValue();
    }

    private void findViews() {
        viewager=(ViewPager)findViewById(R.id.viewpager);
    }

    private void initValue() {
        LayoutInflater  inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        list_views=new ArrayList<View>();
        for(int i=0;i<LENGTH;i++){
            View view = inflater.inflate(R.layout.wizard_image_item,null);
            list_views.add(view);
        }
        viewager.setAdapter(new MyPagerAdapter(list_views));
        viewager.setOnPageChangeListener(new MyPageChangeListener());
    }


    private void animateNormal(int position,float showFrom,int showPix,int tempPosition){
        ArrayList<View> curList=new ArrayList<View>();
        ArrayList<View> nextList=new ArrayList<View>();
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
        AnimationFactory.getInstance().createEngine().startTogether(0,null,list_ani);
    }


   private  class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            status=arg0;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tempPosition=index-position;
            animateNormal(position,positionOffset,positionOffsetPixels,tempPosition);
        }

        @Override
        public void onPageSelected(int arg0) {
            index=arg0;
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


}