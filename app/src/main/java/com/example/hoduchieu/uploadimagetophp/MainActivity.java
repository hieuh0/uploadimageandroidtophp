package com.example.hoduchieu.uploadimagetophp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btnGoi,btnChonHinh;
    ImageView imageView;
    EditText editText;
    Bitmap bitmap;
    String URL= "http://192.168.1.100/demovolley/uploadimage.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChonHinh = (Button) findViewById(R.id.button2);
        btnGoi = (Button) findViewById(R.id.button);
        editText = (EditText)findViewById(R.id.editText);
        imageView = (ImageView) findViewById(R.id.imageView);

        btnChonHinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),999);
            }
        });

        btnGoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 999 && resultCode== RESULT_OK && data !=null){
            Uri filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
public String getStringImage(Bitmap bm){
    ByteArrayOutputStream ba = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.JPEG,100,ba);
    byte[] imagebyte = ba.toByteArray();
    String encode = Base64.encodeToString(imagebyte,Base64.DEFAULT);
    return encode;
}

    private void UploadImage(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String s = response.trim();

                if(s.equalsIgnoreCase("Loi")){
                    Toast.makeText(MainActivity.this, "Loi", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Thanh COng", Toast.LENGTH_SHORT).show();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error+"", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String ten = editText.getText().toString();
                String image = getStringImage(bitmap);
                Map<String ,String> params = new HashMap<String,String>();

                params.put("TEN",ten);
                params.put("HINH",image);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
