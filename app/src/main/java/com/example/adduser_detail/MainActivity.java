package com.example.adduser_detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity{
    FloatingActionButton addFloatingButton;
    RecyclerView userRecyclerView;
    TextView noRecordsTv;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        noRecordsTv=findViewById(R.id.norecords_textview);
        addFloatingButton=findViewById(R.id.addUser_floatButton);
        userRecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.ItemDecoration divider= new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL);
        userRecyclerView.addItemDecoration(divider);
        addFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent adduser=new Intent(MainActivity.this,AddUserDetailsActivity.class);
                startActivity(adduser);
                finish();
            }
        });

        fetchUserDetails();
    }


    private void fetchUserDetails() {

           class GetTasks extends AsyncTask<Void, Void, List<AddUserEntity>> {
                @Override
                protected List<AddUserEntity> doInBackground(Void... voids) {

                   List<AddUserEntity> taskList = UserDatabaseEntity
                            .getInstance(getApplicationContext())
                            .getUserdatabase()
                            .userDao()
                            .getall();
                    Log.v("items", taskList.toString());
                    return taskList;
                }

                @Override
                protected void onPostExecute(List<AddUserEntity> tasks) {

                    if(tasks.size()>0) {
                        noRecordsTv.setVisibility(View.GONE);
                        userRecyclerView.setVisibility(View.VISIBLE);
                        userRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        AddUserAdapter addUserAdapter = new AddUserAdapter(MainActivity.this, tasks, new UpdateUserInterface() {
                            @Override
                            public void deleteUser(String id) {
                                Bundle bundle=new Bundle();
                                Intent intent=new Intent(MainActivity.this, UserUpdateDeleteActivity.class);
                                bundle.putString("userid",id);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                        });
                        userRecyclerView.setAdapter(addUserAdapter);
                        super.onPostExecute(tasks);
                    }
                    else{
                        noRecordsTv.setVisibility(View.VISIBLE);
                        userRecyclerView.setVisibility(View.GONE);
                    }
                }
            }

            GetTasks gt = new GetTasks();
            gt.execute();
        }




}

