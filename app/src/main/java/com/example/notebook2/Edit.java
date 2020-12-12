package com.example.notebook2;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class Edit extends AppCompatActivity implements OnClickListener {
    Button button0Delete, setbutton0get, setbutton0no;
    EditText setedit0Content, setedit0Title, edittext0Author;
    ImageView seteditpicture;
    int tran = 0;
    String author = "";
    MyDataBaseHelper dbHelper = new MyDataBaseHelper(this, "Note.db", null, 6);

    private void InitNote() {       //进行数据填装
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this, "Note.db", null, 6);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Note", new String[]{}, "id=?", new String[]{tran + ""}, null, null, null);
        if (cursor.moveToNext()) {       //根据mainactivity传来的id值选择数据库中对应的行，将值返回
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                String photo = cursor.getString(cursor.getColumnIndex("photo"));
                String author = cursor.getString(cursor.getColumnIndex("author_d"));
                setedit0Content.setText(content);
                setedit0Title.setText(title);
                edittext0Author.setText(author);
                if (photo != null)
                    displayImage(photo);
            } while (cursor.moveToNext());
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setedit0Content = (EditText) findViewById(R.id.Edit0EditContent);
        setedit0Title = (EditText) findViewById(R.id.EditTextEditTitle);
        setbutton0no = (Button) findViewById(R.id.Button0cancel);
        setbutton0get = (Button) findViewById(R.id.Button0save);
        button0Delete = (Button) findViewById(R.id.Button0delete);
        edittext0Author = findViewById(R.id.EditEditAuthor);
        seteditpicture = findViewById(R.id.Edit0picture);


        setbutton0no.setOnClickListener(this);
        setbutton0get.setOnClickListener(this);
        button0Delete.setOnClickListener(this);

        Intent intent = getIntent();
        tran = intent.getIntExtra("tran", -1);       //取出mainactivity传来的id值

        InitNote();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.Button0delete:     //将对应的id行删除

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Note", "id=?", new String[]{tran + ""});
                Edit.this.setResult(RESULT_OK, getIntent());
                Edit.this.finish();
                break;
            case R.id.Button0save:       //保存该界面的数据
                SQLiteDatabase db1 = dbHelper.getWritableDatabase();
                Date date = new Date();
                ContentValues values = new ContentValues();
                String Title = String.valueOf(setedit0Title.getText());
                String Content = String.valueOf(setedit0Content.getText());
                if (Title.length() == 0) {
                    Toast.makeText(this, "请输入一个标题", Toast.LENGTH_LONG).show();
                } else {
                    values.put("title", Title);
                    values.put("content", Content);
                    db1.update("Note", values, "id=?", new String[]{tran + ""});        //对数据进行更新
                    Edit.this.setResult(RESULT_OK, getIntent());
                    Edit.this.finish();
                }


                author = String.valueOf(edittext0Author.getText());
                SharedPreferences.Editor editor = getSharedPreferences("data_1", MODE_PRIVATE).edit();
                if("".equals(edittext0Author.getText().toString())){
                    editor.putString("author","zyh");
                }
                else {
                    editor.putString("author", author);      //写入作者信息
                }
                editor.apply();

                break;


            case R.id.Button0cancel:
                Edit.this.setResult(RESULT_OK, getIntent());
                Edit.this.finish();
                break;

        }

    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            seteditpicture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "获取图片失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
}