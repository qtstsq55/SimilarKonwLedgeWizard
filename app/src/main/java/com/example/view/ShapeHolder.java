package com.example.view;

import android.graphics.Bitmap;

public class ShapeHolder {
	private float x = 0, y = 0;
    private float alpha = 255f;
    private float scalex,scaley;
    private Bitmap bitmap;
    public ShapeHolder() {
    }

    public void setX(float value) {
        x = value;
    }
    public float getX() {
        return x;
    }
    public void setY(float value) {
        y = value;
    }
    public float getY() {
        return y;
    }
	public float getAlpha() {
		return alpha;
	}
    public void setAlpha(float alpha) {
        this.alpha = alpha;
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
