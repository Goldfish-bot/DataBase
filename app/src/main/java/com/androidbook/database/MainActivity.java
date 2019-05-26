package com.androidbook.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.edit);
        Button saveData = (Button)findViewById(R.id.save_data);
        Button restoreData = (Button)findViewById(R.id.restore_data);
        String inputText = load();
        if (!TextUtils.isEmpty(inputText)){
            editText.setText(inputText);
            editText.setSelection(inputText.length());
            Toast.makeText(this,"Restoring succeeded",Toast.LENGTH_SHORT).show();
        }
        saveData.setOnClickListener(savedataShare);
        restoreData.setOnClickListener(restData);
    } //Create

    View.OnClickListener savedataShare = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences.Editor editor = getSharedPreferences("shareData",MODE_PRIVATE).edit();
            editor.putString("name","admin");
            editor.putString("name","hello");
            editor.putInt("age",18);
            editor.putBoolean("married",false);
            editor.apply();
        }
    }; //end savedataShare

    View.OnClickListener restData = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences pref = getSharedPreferences("shareData",MODE_PRIVATE);
            String name = pref.getString("name","error!");
            int age = pref.getInt("age",0);
            boolean married = pref.getBoolean("married",false);
            Log.d(TAG, "name is "+name);
            Log.d(TAG, "age is  "+age);
            Log.d(TAG, "married is  "+married);
        }
    }; //end restore data
    @Override
    protected void onDestroy() {
        super.onDestroy();
        String inputText = editText.getText().toString();
        save(inputText);
    } //Destroy()

    public void save(String inputText){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    } //end save()

    public String load(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    } //end load()

}
