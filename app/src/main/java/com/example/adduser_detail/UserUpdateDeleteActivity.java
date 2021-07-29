package com.example.adduser_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActionBarContextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;

import static com.example.adduser_detail.R.id.btn_del_user;
import static com.example.adduser_detail.R.id.tv_gender;
import static com.example.adduser_detail.R.id.updateimage;

public class UserUpdateDeleteActivity extends AppCompatActivity {

    EditText tvdelupdtname,tvdelupdtmobile,tvdelupdtdob;
    MaterialSpinner spdelupdtgender;
    Button btnUpdate,btnDelete;
    String updtmobile,updtname,updtdob,updtgender;
    CircleImageView imageupdt;
    Bitmap bmImage;
    private static final int UPDTCAM_REQUEST=111;
    byte[] imageUpdate=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userupdatedelete);
        getSupportActionBar().hide();
        tvdelupdtname=findViewById(R.id.tv_updt_del_name);
        tvdelupdtmobile=findViewById(R.id.tv_updt_mobile);
        tvdelupdtdob=findViewById(R.id.tv_updt_del_dob);
        spdelupdtgender=(MaterialSpinner) findViewById(R.id.sp_updt_gender);
        btnDelete=findViewById(btn_del_user);
        btnUpdate=findViewById(R.id.btn_updt_user);
        imageupdt=findViewById(R.id.updateimage);
        Bundle bundle=getIntent().getExtras();
        String id=bundle.getString("userid");
        UserDetail detail=new UserDetail(id,"fetch");
        detail.execute();
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(UserUpdateDeleteActivity.this,R.array.gender_dropdown_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdelupdtgender.setAdapter(adapter);
        spdelupdtgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
       btnDelete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Are you sure Want to delete", Snackbar.LENGTH_LONG)
                       .setAction("YES", new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       UserDetail detail=new UserDetail(id,"delete");
                       detail.execute();
                   }
               });
               snackbar.show();
           }
       });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserDetail detail=new UserDetail(id,"update");
                AddUserEntity adduser = new AddUserEntity();
                updtname=tvdelupdtname.getText().toString().trim();
                updtmobile=tvdelupdtmobile.getText().toString().trim();
                updtdob=tvdelupdtdob.getText().toString().trim();
                updtgender=spdelupdtgender.getSelectedItem().toString().trim();
                if(bmImage!=null) {
                    imageUpdate = DataConverter.imageToByteArray(bmImage);
                }

                if(!updtname.equals("") && (!updtmobile.equals("")) && (!updtdob.equals("")) && (!updtgender.equals("Select Gender"))) {
                    detail.execute();
                }
                else {
                    Snackbar.make(findViewById(android.R.id.content),"One of the item is blank!!",Snackbar.LENGTH_LONG).show();
                }
            }
        });

        imageupdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camIntent,UPDTCAM_REQUEST);
            }
        });

    }


        class UserDetail extends AsyncTask<Void, Void, List<AddUserEntity>>{

            private final String id;
            private String flag;

            public UserDetail(String id, String flag) {
                this.id=id;
                this.flag=flag;
            }

            @Override
            protected List<AddUserEntity> doInBackground(Void... voids) {
                List<AddUserEntity> userlist=UserDatabaseEntity
                        .getInstance(getApplicationContext())
                        .getUserdatabase()
                        .userDao().selectuser(Integer.parseInt(id));

              if(flag.equals("delete")){
                  UserDatabaseEntity.getInstance(getApplicationContext()).
                          getUserdatabase().userDao()
                          .deleteuser(Integer.parseInt(id));
               }
              else if (flag.equals("update")){
                  UserDatabaseEntity.getInstance(getApplicationContext()).getUserdatabase().userDao()
                          .updaterecord(updtname, updtmobile, updtdob, updtgender, Integer.parseInt(id),imageUpdate);
              }

                return userlist;
            }

            @Override
            protected void onPostExecute(List<AddUserEntity> addUserEntities) {
                if(flag.equals("fetch")){
                    AddUserEntity list=addUserEntities.get(0);
                    tvdelupdtname.setText(list.getUsername());
                    tvdelupdtmobile.setText(list.getMobilenumber());
                    tvdelupdtdob.setText(list.getDob());
                    if(list.getImage()!=null) {
                        imageupdt.setImageBitmap(DataConverter.byteArrayToImage(list.getImage()));
                    }
                    else {
                        imageupdt.setImageResource(R.drawable.ic_baseline_person_24);
                    }
                    String gender=list.getGender();
                    String[] stringArray = getResources().getStringArray(R.array.gender_dropdown_array);
                    int position=Arrays.asList(stringArray).indexOf(gender);
                    spdelupdtgender.setSelection(position);
                }
                else if(flag.equals("delete")){
                    Intent newintent=new Intent(UserUpdateDeleteActivity.this,MainActivity.class);
                    startActivity(newintent);
                    finish();
                }
                else if(flag.equals("update")){

                    Intent newintent=new Intent(UserUpdateDeleteActivity.this,MainActivity.class);
                    startActivity(newintent);
                    finish();
                }
                else {
                    Toast.makeText(UserUpdateDeleteActivity.this,"not a Valid choice",Toast.LENGTH_LONG);
                }

                super.onPostExecute(addUserEntities);
            }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(UPDTCAM_REQUEST==111) {
            bmImage = (Bitmap) data.getExtras().get("data");
            imageupdt.setImageBitmap(bmImage);
        }
    }
}
