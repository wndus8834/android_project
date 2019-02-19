package com.example.ljy.ex.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.example.ljy.ex.R;


public class PaintDiaryUtil {
	private Context mContext;
	private PaintBoard mVw;
	private ImageView mImage;
	
	public PaintDiaryUtil(Context context, PaintBoard view, ImageView image){
		mContext = context;
		mVw = view;
		mImage = image;
	}
	public void DialogDelete(final int x, final int y){
		final String[] delete = mContext.getResources().getStringArray(R.array.delete);
		
		AlertDialog.Builder del_bld = new AlertDialog.Builder(mContext);
		del_bld.setTitle("Delete");
		del_bld.setItems(delete, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				if( item == 0 ){
					mVw.setClearMode();
				}
				else if( item == 1){
					mVw.init(x, y);
				}
			}
		});
		AlertDialog al_del = del_bld.create();
		al_del.show();
	}
	
	public void DialogBkColor(final int x, final int y){
		final String[] bk_color = mContext.getResources().getStringArray(R.array.bk_color);
		
		AlertDialog.Builder bk_bld = new AlertDialog.Builder(mContext);
		bk_bld.setTitle("Background Color");
		bk_bld.setItems(bk_color, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				switch (item) {
				case 0:
					BitmapDrawable back_white = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.whitebag);
					ChangeBkColor(back_white);
					break;
				case 1:
					BitmapDrawable back_red = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.redbag);
					ChangeBkColor(back_red);
					break;
				case 2:
					BitmapDrawable back_blue = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.bluebag);
					ChangeBkColor(back_blue);
					break;
				case 3:
					BitmapDrawable back_green = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.greenbag);
					ChangeBkColor(back_green);
					break;
				default:
					break;
				}
			}
		});
		AlertDialog al_bk_color = bk_bld.create();
		al_bk_color.show();
	}
	public void ChangeBkColor(BitmapDrawable db){
		Bitmap temp = db.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
		mImage.setImageBitmap(temp);
		mVw.setBackImage(mImage);
		mVw.setBackBitmap(temp);
	}
}
