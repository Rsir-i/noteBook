package com.example.notebook2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Integer> IDList = new ArrayList<>();
    private List<String> TADList = new ArrayList<>();
    ArrayAdapter simple0adapter;
    Button sgetbutton0Seek;
    EditText getedit0TextSeek;
    EditText set0DefaultsName;
    String setSeektitle;
    Button setAuthorsName;
    public void RefreshTADList(){       //返回该界面时刷新的方法
        int size = TADList.size();
        //if(size>0){
        TADList.removeAll(TADList);
        IDList.removeAll(IDList);
        simple0adapter.notifyDataSetChanged();       //清空两个list中的值
        //}
        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this,"Note.db",null,6);
        SQLiteDatabase db = dbHelper.getWritableDatabase();         //实例化SQLitedatabase
        Cursor cursor  = db.rawQuery("select * from Note",null);
        while(cursor.moveToNext()){         //对两个list重新赋予值
            int id=cursor.getInt(cursor.getColumnIndex("id"));

            String title = cursor.getString(cursor.getColumnIndex("title"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            IDList.add(id);
            TADList.add(title+"\n"+ date);      //将title和时间分开显示
        }
        /*SharedPreferences.Editor editor=getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("author", " ");
        editor.apply();*/
        SharedPreferences sharedPreferences=getSharedPreferences("data_1",MODE_PRIVATE);
        String name=sharedPreferences.getString("author","gyh");
        set0DefaultsName.setText(name);

    }



    @Override
    protected void onStart() {
        super.onStart();
        RefreshTADList();       //调用刷新方法
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MyDataBaseHelper dbHelper = new MyDataBaseHelper(this,"Note.db",null,6);
        SQLiteDatabase db = dbHelper.getWritableDatabase();     //通过dbhelper获得可写文件
        Cursor cursor  = db.rawQuery("select * from Note",null);
        IDList.clear();
        TADList.clear();        //清空两个list
        while(cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            IDList.add(id);
            TADList.add(title+"\n"+ date);      //对两个list填充数据
        }

        simple0adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,TADList);       //配置适配器
        ListView ListView = (ListView)findViewById(R.id.ListView);
        ListView.setAdapter(simple0adapter);                 //将两个list中的值通过ArrayList显示出来

        Button ButtonAdd;
        ButtonAdd = (Button)findViewById(R.id.Button0Add);
        ButtonAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, com.example.notebook2.Add.class);
                startActivity(intent);
            }
        });

        sgetbutton0Seek = findViewById(R.id.Button0Seek);
        getedit0TextSeek = findViewById(R.id.EditTextSeek);
        sgetbutton0Seek.setOnClickListener(new View.OnClickListener(){       //点击跳转查询界面
            @Override
            public void onClick(View v){
                setSeektitle ="";
                setSeektitle = String.valueOf(getedit0TextSeek.getText());
                //Log.d("title is ",EditTextSeekString);
                if(setSeektitle.length()==0){             //查询为空，给出提示信息
                    RefreshTADList();
                    Toast.makeText(MainActivity.this,"查询值不能为空",Toast.LENGTH_LONG).show();
                }
                else{           //否则通过intent给查询界面传入查询的title
                    Intent intent = new Intent(MainActivity.this, Researchtitle.class);
                    //intent.putExtra("tranTitle",EditTextSeekString);
                    intent.putExtra("tranTitletoRE", setSeektitle);
                    startActivity(intent);

                }
            }
        });

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){      //配置ArrayList点击按钮
            @Override
            public void  onItemClick(AdapterView<?> parent, View view , int position , long id){
                int tran = IDList.get(position);        //点击不同的行，返回不同的id
                Intent intent = new Intent(MainActivity.this, com.example.notebook2.Edit.class);
                intent.putExtra("tran",tran);
                startActivity(intent);      //通过intent传输
            }
        });

        set0DefaultsName =findViewById(R.id.default_author_name);
        setAuthorsName =findViewById(R.id.Button0DefaultAuthor);
        setAuthorsName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= set0DefaultsName.getText().toString();
                SharedPreferences.Editor editor=getSharedPreferences("data_1",MODE_PRIVATE).edit();

                    editor.putString("author", name);
                    Toast.makeText(MainActivity.this, "已保存", Toast.LENGTH_SHORT).show();

                editor.apply();
            }
        });
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){

        }

    }
    
}