package com.example.ljy.ex;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView myListView;
    DBHelper mydb;
    ArrayAdapter mAdapter;
    public static final int NOTIFICATION_ID=1;
    //private Button btnStart, btnEnd;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Intent intent;
        switch (item.getItemId()){
            case R.id.btn_Start:
                Toast.makeText(getApplicationContext(),"알림 시작",Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
                return true;
            case R.id.btn_End:
                Toast.makeText(getApplicationContext(),"알림 끝",Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this,MyService.class);
                stopService(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllMovies();



      /*  btnStart = (Button)findViewById(R.id.btn_Start);
        btnEnd = (Button)findViewById(R.id.btn_End);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"알림 시작",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MyService.class);
                startService(intent);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"알림 끝",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,MyService.class);
                stopService(intent);
            }
        });*/



        mAdapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_list);

        myListView = (ListView) findViewById(R.id.listview);
        myListView.setAdapter(mAdapter);

        myListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                int id = arg2 + 1;
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id);
                Intent intent = new Intent(getApplicationContext(), com.example.ljy.ex.DisplayMovie.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.clear();
        mAdapter.addAll(mydb.getAllMovies());
        mAdapter.notifyDataSetChanged();
    }

    public void onClick(View target) {
        Bundle bundle = new Bundle();
        bundle.putInt("id", 0);
        Intent intent = new Intent(getApplicationContext(), com.example.ljy.ex.DisplayMovie.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

  /*  public void sendNotification(View view) {

        // NotificationCompat.Builder를 사용하여서 알림을 설정한다.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        // 알림에 나타나는 아이콘
        builder.setSmallIcon(R.drawable.ic_launcher);

        // 알림이 클릭되면 이 인텐트가 보내진다.
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 사용자가 알림을 터치하면 인 인텐트가 보내진다.
        builder.setContentIntent(pendingIntent);

        // 알림에 표시되는 큰 아이콘
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

        // 알림 제목
        builder.setContentTitle("알려드립니다.");

        // 알림 콘텐츠
        builder.setContentText("이것은 시험적인 알림입니다.");

        // 4.2 이상인 경우에 보여지는 서브 텍스트
        builder.setSubText("클릭하면 해당 앱으로 이동합니다.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // 알림바에 알림을 표시한다.
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }*/

}