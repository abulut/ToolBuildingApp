package com.example.serkan.toolbuilding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Serkan on 15.06.2015.
 */
public class HashtagActivity extends Activity implements View.OnClickListener,DialogInterface.OnClickListener{
    private String date;
    private String dir;
    private AlertDialog ad;

    private EditText hashtag1;
    private EditText hashtag2;
    private EditText notes;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_hashtag);

        hashtag1 = (EditText) findViewById(R.id.hashtag1);
        hashtag2 = (EditText) findViewById(R.id.hashtag2);
        notes = (EditText) findViewById(R.id.notes);
        ImageView image = (ImageView) findViewById(R.id.imageView);
        Button back = (Button) findViewById(R.id.back);
        Button save = (Button) findViewById(R.id.save);

        Intent i = getIntent();
        // Receiving the Data
        date = i.getStringExtra("date");

        dir = Environment.getExternalStorageDirectory() + "/camtest";

        Log.d("e-----------", dir);

        Bitmap bitmap = BitmapFactory.decodeFile(dir + "/" + date + ".jpg");
        image.setImageBitmap(bitmap);

        back.setOnClickListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("image", date);
                    jsonObject.put("hashtag1", hashtag1.getText().toString());
                    jsonObject.put("hashtag2", hashtag2.getText().toString());
                    jsonObject.put("notes", notes.getText().toString());


                    FileOutputStream outStream = null;
                    File outFile = new File(dir, date + ".json");

                    outStream = new FileOutputStream(outFile);
                    outStream.write(jsonObject.toString().getBytes());
                    outStream.flush();
                    outStream.close();

                    Intent nextScreen = new Intent(getApplicationContext(), CamTestActivity.class);

                    Log.e("n", "wechsel zu CameraTest");

                    startActivity(nextScreen);
                    Toast.makeText(getApplicationContext(), "Das Foto wurde gespeichert!", Toast.LENGTH_LONG).show();
                    finish();
                }
                catch(JSONException ex) {
                    ex.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        ad = new AlertDialog.Builder(this)
                .setMessage("M�chten Sie das Foto wirklich l�schen?")
                .setTitle("ToolBuilding")
                .setPositiveButton("Ja", this)
                .setNegativeButton("Nein", this)
                .setCancelable(false)
                .create();

        ad.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        switch(which){
            case DialogInterface.BUTTON_POSITIVE: // yes
                File file = new File(dir);
                file.delete();
                Intent nextScreen = new Intent(getApplicationContext(), CamTestActivity.class);

                Log.e("n", "wechsel zu CameraTest");

                startActivity(nextScreen);
                finish();
                break;
            case DialogInterface.BUTTON_NEGATIVE: // no
                ad.dismiss();
                break;
            default:
                // nothing
                break;
        }
    }
}
