package com.example.adduser_detail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Driver;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;

public class AddUserDetailsActivity extends AppCompatActivity {

    private static final int CAM_REQUEST=999;
    CircleImageView addUserImage;
    Button userAddbutton;
    EditText userAddName,userAddMobile,userAdddob;
    MaterialSpinner userGenderSpinner;
    int birthYear,birthMonth,birthDate,month,genderPosition=0;
    DatePickerDialog userdatepicker;
    RecyclerView userRecycler;
    AddUserAdapter usrAdapter;
    View view;
     Bitmap bmpImage;
    byte[] usrimage=null;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.adduser_detail_activity);
        addUserImage=findViewById(R.id.userimage);
        userAddbutton=findViewById(R.id.adduser_button);
        userAddName=findViewById(R.id.adduser_detail_nametext);
        userAddMobile=findViewById(R.id.adduser_dateil_phnNumber_text);
        userAdddob=findViewById(R.id.adduser_dob_text);
        userGenderSpinner=(MaterialSpinner) findViewById(R.id.userGender_spinner);

        userAddbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addUser=userAddName.getText().toString();
                String addPhnNo=userAddMobile.getText().toString();
                String userDob=userAdddob.getText().toString();


                if(addUser.equals(""))
                {
                    Snackbar snackbar=Snackbar.make(view,getString(R.string.missing_Name),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if(addPhnNo.equals("") ||(addPhnNo.length()<10))
                {
                    Snackbar snackbar=Snackbar.make(view,getString(R.string.invalid_phnNo),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if (userDob.equals(""))
                {
                    Snackbar snackbar=Snackbar.make(view,getString(R.string.dob_missing),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if (genderPosition==0)
                {
                    Snackbar snackbar=Snackbar.make(view,getString(R.string.spinner_invalid),Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else
                {
                    Intent intent=new Intent(AddUserDetailsActivity.this,MainActivity.class);
                    startActivity(intent);
                    saveUser();
                    Snackbar snackbar=Snackbar.make(view,getString(R.string.userAdded_successful),Snackbar.LENGTH_LONG);
                    snackbar.show();
                    String userGenderItem=userGenderSpinner.getSelectedItem().toString();
                    finish();

                }
            }
        });
    userAdddob.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar userCalendar = Calendar.getInstance();
            birthYear=userCalendar.get(Calendar.YEAR);
            birthMonth=userCalendar.get(Calendar.MONTH);
            birthDate=userCalendar.get(Calendar.DATE);

            userdatepicker =new DatePickerDialog(AddUserDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int date) {
//to make Jan month from 0 to 1 added month+1
                    month=month+1;
                    String seldate=date +"/" + month + "/" + year ;
                    userAdddob.setText(seldate);

                }
            },birthYear,birthMonth,birthDate);
            userdatepicker.show();
        }
    });
    //setting spinner to show value in dropdown list
        ArrayAdapter <CharSequence> adapter=ArrayAdapter.createFromResource(AddUserDetailsActivity.this,R.array.gender_dropdown_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userGenderSpinner.setAdapter(adapter);
        userGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                genderPosition=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

      if(ContextCompat.checkSelfPermission(AddUserDetailsActivity.this,Manifest.permission.CAMERA)
      !=PackageManager.PERMISSION_GRANTED)
      {
          ActivityCompat.requestPermissions(AddUserDetailsActivity.this,new String[]{
                  Manifest.permission.CAMERA
          },CAM_REQUEST);
      }
        addUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camintent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camintent,CAM_REQUEST);
            }
        });

    }


    private void saveUser(){
        String saveUsername=userAddName.getText().toString().trim();
        String saveMobile=userAddMobile.getText().toString().trim();
        String saveDob=userAdddob.getText().toString().trim();
        String saveGender=userGenderSpinner.getSelectedItem().toString().trim();
        if(bmpImage!=null){
          usrimage = DataConverter.imageToByteArray(bmpImage);
        }

        if(saveUsername.isEmpty()){
            userAddName.setError("User Name Required");
        }
        else if(saveMobile.isEmpty()){
            userAddName.setError("Mobile Number Required");
        }
        else if(saveDob.isEmpty()){
            userAddName.setError("D.O.B is Required");
        }
        else if(saveGender.isEmpty()){
            userAddName.setError("Gender Required");
        }
    else {
           SaveUser saveUser=new SaveUser(saveUsername,saveMobile,saveGender,saveDob,usrimage);
           saveUser.execute();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999){
            bmpImage= (Bitmap) data.getExtras().get("data");
            addUserImage.setImageBitmap(bmpImage);
        }
    }

    class SaveUser extends AsyncTask<Void,Void,Void>{
        String saveUsername,saveMobile,saveGender, saveDob;
        byte[] saveImage;

        public SaveUser(String saveUsername, String saveMobile, String saveGender, String saveDob,byte[] saveImage) {
            this.saveUsername=saveUsername;
            this.saveGender=saveGender;
            this.saveDob=saveDob;
            this.saveMobile=saveMobile;
            this.saveImage=saveImage;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            AddUserEntity adduser = new AddUserEntity();
            adduser.setUsername(saveUsername);
            adduser.setMobilenumber(saveMobile);
            adduser.setDob(saveDob);
            adduser.setGender(saveGender);
            adduser.setImage(saveImage);
            UserDatabaseEntity.getInstance(getApplicationContext()).getUserdatabase().userDao().insert(adduser);
            return null;
        }
    }
}
