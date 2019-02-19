package com.example.ljy.ex;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ljy.ex.util.PaintBoard;
import com.example.ljy.ex.util.PaintDiaryUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DisplayMovie extends ActionBarActivity {

    private DBHelper mydb;
    TextView name;
    TextView director;
    int id = 0;

    private int baseWidth;
    private int baseHeight;
    private EditText mInputText;
    private PaintBoard vw;
    private PaintDiaryUtil mPaintUtil;
    final int REQUEST = 1;
    final int CROP_REQUEST = 2;

    private String mName;
    private int mTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display);

        name = (TextView) findViewById(R.id.title_txt);
        director = (TextView) findViewById(R.id.txt);

        mydb = new DBHelper(this);


        TextView date_txt = (TextView)findViewById(R.id.date_view);

        date_txt.setText(getCurrentTime());


        vw = (PaintBoard)findViewById(R.id.draw_board);

        baseWidth = vw.getScreenWidth();
        baseHeight = vw.getScreenHeight();

        ImageView back = (ImageView)findViewById(R.id.backimage);

        BitmapDrawable back_drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.whitebag);
        Bitmap temp = back_drawable.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        back_drawable = null;
        back.setImageBitmap(temp);
        vw.setBackImage(back);
        vw.setBackBitmap(temp);

        mPaintUtil = new PaintDiaryUtil(this, vw, back);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                Cursor rs = mydb.getData(Value);
                id = Value;
                rs.moveToFirst();
                String n = rs.getString(rs.getColumnIndex(DBHelper.MOVIES_COLUMN_NAME));
                String d = rs.getString(rs.getColumnIndex(DBHelper.MOVIES_COLUMN_DIRECTOR));
                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button) findViewById(R.id.save_btn);
                b.setVisibility(View.INVISIBLE);

                name.setText((CharSequence) n);
                director.setText((CharSequence) d);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST:
            {
                if(data != null){

                    Uri Data_uri = data.getData();

                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    cropIntent.setDataAndType(Data_uri, "image/*");
                    cropIntent.putExtra("noFaceDetection", true);
                    cropIntent.putExtra("faceDetection", false);
                    cropIntent.putExtra("outputX", baseWidth);
                    cropIntent.putExtra("outputY", baseHeight);
                    cropIntent.putExtra("aspectX", baseWidth);
                    cropIntent.putExtra("aspectX", baseHeight);
                    cropIntent.putExtra("scale", true);
                    cropIntent.putExtra("return-data", true);

                    startActivityForResult(cropIntent, CROP_REQUEST);
                }
            }

            break;
            case CROP_REQUEST:
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = (Bitmap)data.getParcelableExtra("data");
                    vw.setBackBitmap(bitmap);
                    RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams((int)vw.getScreenWidth(), (int)vw.getScreenHeight());
                    param.addRule(RelativeLayout.CENTER_IN_PARENT);

                    ImageView back = (ImageView)findViewById(R.id.backimage);
                    back.setLayoutParams(param);
                    back.setScaleType(ImageView.ScaleType.FIT_XY);
                    back.setImageBitmap(bitmap);
                    back.setVisibility(View.VISIBLE);

                }
            default:
                break;
        }
    }

    public  void onClick(View view){
        switch (view.getId()) {
            case R.id.background_color_btn:
                mPaintUtil.DialogBkColor(baseWidth, baseHeight);
                break;
            case R.id.background_image_btn:
                LoadImage();
                break;
            case R.id.erase_btn:
                mPaintUtil.DialogDelete(baseWidth, baseHeight);
                break;
            default:
                break;
        }

    }

    private void LoadImage() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST);

    }

    public void insert(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (mydb.updateMovie(id, name.getText().toString(), director.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "수정되었음", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), com.example.ljy.ex.MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "수정되지 않았음", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (mydb.insertMovie(name.getText().toString(), director.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "추가되었음", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "추가되지 않았음", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        }
    }

    public void delete(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                mydb.deleteMovie(id);
                Toast.makeText(getApplicationContext(), "삭제되었음", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "삭제되지 않았음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void edit(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("id");
            if (value > 0) {
                if (mydb.updateMovie(id, name.getText().toString(), director.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "수정되었음", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "수정되지 않았음", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String getCurrentTime() {
        String strTime = "";
        SimpleDateFormat sdf = null;
        long time = System.currentTimeMillis();
        if(mTime == 0){
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        else if(mTime == 1)
        {
            sdf = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        }
        Date dd = new Date(time);
        strTime = sdf.format(dd);

        return strTime;
    }

    //save bitmap and texst
    public void DataSaved()
    {
        RelativeLayout r = (RelativeLayout)findViewById(R.id.drawimage1);
        RayoutToBitmap(r);
    }

    //save bitmap and texst
    public void RayoutToBitmap(RelativeLayout rl)
    {
       /* rl.invalidate();
        rl.setDrawingCacheEnabled(true);

        Bitmap b = null;
        String description;
        b = rl.getDrawingCache();
        mTime = 1;
        mName = getCurrentTime() + ".jpg";
        String dir = Environment.getExternalStorageDirectory()+"/paintdiary/";

        File folder = new File(dir);

        if( !folder.exists() )
        {
            folder.mkdirs();
        }

        folder = null;

        File nomediaFile = new File(dir + "/.nomedia");

        if(!nomediaFile.exists()){
            nomediaFile.mkdir();
        }
        nomediaFile = null;

        File file = new File(dir, mName);

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            description = mInputText.getText().toString();
            DBHelper.insertFaMathematic(mName, dir + mName, description);


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        b = null;

        file = null;
        outputStream = null;*/
    }

}