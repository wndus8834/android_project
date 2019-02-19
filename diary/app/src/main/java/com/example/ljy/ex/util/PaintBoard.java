package com.example.ljy.ex.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class PaintBoard extends View {
	private Bitmap mBitmap = null, mBackBitmap = null;
	private Canvas mCanvas = null;
	private Path mPath;
	private Paint mBitmapPaint;
	private Paint mPaint;
	private int mMode = 0;  // 0 : draw, 1 : clear
	private int input_color = Color.BLACK;
	private float stroke = 8;
	private int baseWidth;
	private int baseHeight;
	private ImageView backImage = null;
	
	public PaintBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		

		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        baseWidth = display.getWidth();
        baseHeight = baseWidth;
        
        mBitmap = Bitmap.createBitmap(baseWidth, baseHeight, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
        
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		
		//paint setting
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(input_color);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(stroke);
	}
	
	public void init(int x, int y)
    {
    	mBitmap.recycle();
    	mBitmap = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        
        invalidate();
    }
	
	public int getScreenWidth()
    {
    	return baseWidth;
    }

    public int getScreenHeight()
    {
    	return baseHeight;
    }
	
	//paint color
	public void setColor(int color)
    {
    	mMode = 0;
    	mPaint.setXfermode(null);
    	mPaint.setStrokeWidth(stroke);
    	mPaint.setColor(color);
    	input_color = color;
    }
	
	//paint strong
	public void setStroke(float input_stroke)
    {
    	stroke = input_stroke;
    	mPaint.setStrokeWidth(stroke);
    	mPaint.setColor(input_color);
    }
	
    public int getStroke()
    {
    	return (int) stroke;
    }
	
    //bitmap background image
	public void setBackImage(ImageView IV)
    {
    	backImage = IV;
    }
    
	//background bitmap
    public void setBackBitmap(Bitmap back)
    {
    	if(mBackBitmap != null)
    	{
    		mBackBitmap.recycle();
    		mBackBitmap = null;
    	}
    	
    	mBackBitmap = back;    	
    }
    
    //Clear paint
	public void setClearMode(){
		mMode = 1;
		mPaint.setStrokeWidth(stroke);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
	}
	
	public void StartClear()
    {
    	mPaint.setStrokeWidth(stroke + 20);
    	mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }
    
    public void EndClear()
    {
    	if(mMode == 0)
    	{
        	mPaint.setXfermode(null);
        	mPaint.setStrokeWidth(stroke);
    	}
    }
    
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(0x00000000);
		if(mBitmap != null)
			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mPaint);
		super.onDraw(canvas);
	}
	
	private float mX, mY, preX, preY;
	private static final float TOUCH_TOLERANCE = 4;
	

	private void touch_start(float x, float y){
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		preX = x;
		preY = y;
	}
	
	private void touch_move(float x, float y){
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		
		if(mMode == 0 || mMode == 1){
			mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
			mX = x;
			mY = y;
			if(mMode == 1){
				mPath.lineTo(mX, mY);
				if(mCanvas != null)
					mCanvas.drawPath(mPath, mPaint);
				mPath.reset();
				mPath.moveTo(x, y);
				mX = x;
				mY = y;
			}
		}
	}
	private void touch_up(){
		if(preX == mX && preY ==mY){
			if(mCanvas != null ){
				mCanvas.drawPoint(preX, preY, mPaint);
			}
		}
		else
		{
			mPath.lineTo(mX, mY);
			if(mCanvas != null)
				mCanvas.drawPath(mPath, mPaint);
		}
		mPath.reset();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		default:
			break;
		}
		return true;
	}
}
