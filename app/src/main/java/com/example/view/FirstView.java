package com.example.view;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.example.animator.AnimationFactory;
import com.example.animator.AnimatorValue;
import com.example.animator.AnimatorValueImplements;
import com.example.zhwizard.R;
import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.animation.ValueAnimator;

public class FirstView extends View implements ValueAnimator.AnimatorUpdateListener{
	 
	 private ArrayList<ShapeHolder> images = new ArrayList<ShapeHolder>();
	 private ArrayList<AnimatorValue> animatorValues = new ArrayList<AnimatorValue>();
	 private AnimatorValue bounceAnim = null;
	 private ZHImage imageHolder = null;
     private int width,height;
     public static  final float SCAL=0.3f;
     private int imageids[]=new int[]{
             R.drawable.ic_guide_first_01,
             R.drawable.ic_guide_first_02,
             R.drawable.ic_guide_first_03,
             R.drawable.ic_guide_first_04,
             R.drawable.ic_guide_first_05,
             R.drawable.ic_guide_first_06,
             R.drawable.ic_guide_first_07,
             R.drawable.ic_guide_first_08,
             R.drawable.ic_guide_first_09,
             R.drawable.ic_guide_first_10,
             R.drawable.ic_guide_first_11,
             R.drawable.ic_guide_first_12,
             R.drawable.ic_guide_first_13
     };
    private Point[] points=new Point[13];

 	 public FirstView(Context context) {
		super(context);
        initDisplay();
	 }
 	 public FirstView(Context context, AttributeSet set) {
		super(context,set);
        initDisplay();
	 }

    private void initDisplay(){
        WindowManager manager = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        width=display.getWidth();
        height=display.getHeight();
    }

	private void createAnimation() {
        for (int i = 0;i < images.size();i++) {
            float X = points[i].x;
            float Y = points[i].y;
            float deptAlpha;
            Object holder[] = new Object[10];
            deptAlpha = 255 / holder.length+.5f;
            for (int k = 0; k < holder.length; k++) {
                holder[k] = new XYAHolder(X, Y, (k + 1) * deptAlpha, (k + 1) * (0.5f / holder.length), (k + 1) * (0.5f / holder.length));
            }
            imageHolder = new ZHImage(images.get(i));
            bounceAnim = new AnimatorValueImplements(imageHolder, new XYAvaluator(), "XYA", holder);
            animatorValues.add(bounceAnim);
        }
   }

     public void startAnimation() {
         ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
         for(int i=0;i<imageids.length;i++){
             bitmaps.add(BitmapFactory.decodeResource(getResources(),imageids[i]));
         }
         points[0]=new Point(width/2, (int) (height*0.02));
         points[1]=new Point(width/6, points[0].y+bitmaps.get(0).getHeight()+30);
         for(int i=2;i<points.length;i++){
             if(i%4==1){
                 points[i]=new Point(points[i-4].x, (int) (height*0.15+points[i-1].y));
             }else{
                 points[i]=new Point(points[i-1].x+bitmaps.get(i-1).getWidth()*2/3,points[i-1].y);
             }
         }
         for(int i=0;i<bitmaps.size();i++){
        	images.add(createImage(bitmaps.get(i),points[i].x, points[i].y,0,0,0));
         }
         createAnimation();
         for(int i=0;i<animatorValues.size();i++){
             animatorValues.get(i).getAnimator().setDuration(300);
             if(i%2==0){
                 animatorValues.get(i).getAnimator().addUpdateListener(this);
                 if(i>0){
                     animatorValues.get(i).before(animatorValues.get(i-1));
                 }
             }
         }
         AnimatorValue values[]=new AnimatorValue[animatorValues.size()];
         animatorValues.toArray(values);
         AnimationFactory.getInstance().createEngine().startTogetherByLink(null,values);
     }


     private ShapeHolder createImage(Bitmap bitmap,float x, float y,float alpha,float scalex, float scaley) {
         ShapeHolder shapeHolder = new ShapeHolder();
         shapeHolder.setX(x);
         shapeHolder.setY(y);
         shapeHolder.setAlpha(alpha);
         shapeHolder.setScalex(scalex);
         shapeHolder.setScaley(scaley);
         shapeHolder.setBitmap(bitmap);
         return shapeHolder;
     }

    private  Bitmap zoomBitmap(Bitmap bitmap, float scalex,float scaley) throws Exception{
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(scalex, scaley);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
        }
        return newbmp;
    }

    @Override
     protected void onDraw(Canvas canvas) {
        try {
             for (int i = 0; i < images.size(); ++i) {
                 Paint p=new Paint();
                 ShapeHolder shapeHolder = images.get(i);
                 p.setAlpha((int) shapeHolder.getAlpha());
                 canvas.save();
                 Bitmap bitmap=zoomBitmap(shapeHolder.getBitmap(), shapeHolder.getScalex(), shapeHolder.getScaley());
                 canvas.drawBitmap(bitmap,shapeHolder.getX()-bitmap.getWidth()/2,shapeHolder.getY()-bitmap.getHeight()/2,p);
                 canvas.restore();
               }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     public void onAnimationUpdate(ValueAnimator animation) {
         invalidate();
     }
     
     public void clearimages(){
    	 images.clear();
    	 animatorValues.clear();
     }
     
     public class XYAvaluator implements TypeEvaluator {
         public Object evaluate(float fraction, Object startValue, Object endValue) {
        	 XYAHolder startXYA = (XYAHolder) startValue;
        	 XYAHolder endXYA = (XYAHolder) endValue;
             return new XYAHolder(
                     startXYA.getX() + fraction * (endXYA.getX() - startXYA.getX()),
            		 startXYA.getY() + fraction * (endXYA.getY() - startXYA.getY()),
                     startXYA.getAlpha() + fraction * (endXYA.getAlpha() - startXYA.getAlpha()),
                     startXYA.getScalex() + fraction * (endXYA.getScalex() - startXYA.getScalex()),
                     startXYA.getScaley() + fraction * (endXYA.getScaley() - startXYA.getScaley())
                     );
         }
     }
     
     public class XYAHolder {
         private float mX;
         private float mY;
         private float  mAlpha,scalex,scaley;
         public XYAHolder(float x, float y,float alpha,float scalex,float scaley) {
             mX = x;
             mY = y;
             mAlpha=alpha;
             this.scalex=scalex;
             this.scaley=scaley;
         }

         public float getX() {
             return mX;
         }

         public void setX(float x) {
             mX = x;
         }

         public float getY() {
             return mY;
         }

         public void setY(float y) {
             mY = y;
         }
         
         public float getAlpha() {
             return mAlpha;
         }

         public void setAlpha(int alpha) {
             mAlpha=alpha;
         }

         public float getScalex() {
             return scalex;
         }

         public void setScalex(float scalex) {
             this.scalex = scalex;
         }

         public float getScaley() {
             return scaley;
         }

         public void setScaley(float scaley) {
             this.scaley = scaley;
         }
     }
     
     public class ZHImage{

         private ShapeHolder image;

         public ZHImage(ShapeHolder image) {
             this.image = image;
         }

         public void setXYA(XYAHolder xyaHolder) {
             image.setX(xyaHolder.getX());
             image.setY(xyaHolder.getY());
             image.setAlpha(xyaHolder.getAlpha());
             image.setScalex(xyaHolder.getScalex());
             image.setScaley(xyaHolder.getScaley());
         }

         public XYAHolder getXYA() {
             return new XYAHolder(image.getX(), image.getY(),image.getAlpha(),image.getScalex(),image.getScaley());
         }
         
     }

}
