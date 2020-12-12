package com.example.notebook2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Researchtitle extends AppCompatActivity implements OnClickListener{
    Button set0Delete, buttonget0Save, buttongetCancel;
    EditText setedit0Content, setTextTitle, setedit0author;
    String titlrrr;
    String authorrr;
    MyDataBaseHelper dbHelper = new MyDataBaseHelper(this,"Note.db",null,6);

    private void InitNote() {
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this,"Note.db",null,6);
        SQLiteDatabase db = dbHelper.getWritableDatabase();     //同上，获得可写文件
        Cursor cursor  = db.query("Note",new String[]{"id","title","content"},"title=?",new String[]{titlrrr +""},null,null,null);

        if(cursor.moveToNext()) {       //逐行查找，得到匹配信息
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                setedit0Content.setText(content);
                setTextTitle.setText(title);
            } while (cursor.moveToNext());
        }

        SharedPreferences pref = getSharedPreferences("data_1",MODE_PRIVATE);     //向sharedpreferences文件读取作者信息
        String name = pref.getString("author","gyh");
        Log.d("MainActivity","name is " + name);
        setedit0author.setText(name);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);
        setedit0Content = (EditText)findViewById(R.id.EditText0Content);
        setTextTitle = (EditText)findViewById(R.id.EditText0Title) ;
        buttongetCancel = (Button)findViewById(R.id.ButtontheCancel);
        buttonget0Save = (Button)findViewById(R.id.Button1Save);
        set0Delete = (Button)findViewById(R.id.ButtonRDelete);
        setedit0author = findViewById(R.id.EditREAuthor);


        buttongetCancel.setOnClickListener(this);
        buttonget0Save.setOnClickListener(this);
        set0Delete.setOnClickListener(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        titlrrr = extras.getString("tranTitletoRE");      //接受主界面传来的title值

        InitNote();


    }
    @Override
    public void onClick(View v){
        switch (v.getId()){

            case R.id.ButtonRDelete:       //删除该title的日志
                Log.d("title is ", titlrrr);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Note","title=?",new String[]{titlrrr +""});     //进行字符串匹配
                Researchtitle.this.setResult(RESULT_OK,getIntent());
                Researchtitle.this.finish();
                break;
            case R.id.Button1Save:         //将文界面内容保存
                SQLiteDatabase db1 = dbHelper.getWritableDatabase();        //获取可写文件
                Date date = new Date();
                ContentValues values = new ContentValues();         //获取信息
                String Title = String.valueOf(setTextTitle.getText());
                String Content = String.valueOf(setedit0Content.getText());
                if(Title.length()==0){
                    Toast.makeText(this, "请输入一个标题", Toast.LENGTH_LONG).show();
                }else {
                    values.put("title", Title);         //填装信息
                    values.put("content", Content);
                    db1.update("Note", values, "title=?", new String[]{titlrrr + ""});        //字符串匹配
                    Researchtitle.this.setResult(RESULT_OK, getIntent());        //返回主界面
                    Researchtitle.this.finish();
                }

                authorrr = String.valueOf(setedit0author.getText());
                SharedPreferences.Editor editor = getSharedPreferences("data_1",MODE_PRIVATE).edit();
                editor.putString("author", authorrr);
                editor.apply();     //写入作者信息

                break;


            case R.id.ButtontheCancel:
                Researchtitle.this.setResult(RESULT_OK,getIntent());
                Researchtitle.this.finish();
                break;

        }

    }
}