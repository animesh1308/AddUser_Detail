package com.example.adduser_detail;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUserAdapter extends RecyclerView.Adapter<AddUserAdapter.UserAddViewHolder> {
    private Context ucontext;
    private List<AddUserEntity> userList;
    UpdateUserInterface userInterface;

  public AddUserAdapter(Context ucontext,List<AddUserEntity> userList,UpdateUserInterface userInterface){
      this.ucontext=ucontext;
      this.userList=userList;
      this.userInterface=userInterface;
  }


    @Override
    public UserAddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userrecyclercontainer,parent,false);
        return new UserAddViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddUserAdapter.UserAddViewHolder holder, int position) {
        AddUserEntity list=userList.get(position);
        holder.userName.setText(list.getUsername());
        holder.mobileNumber.setText(list.getMobilenumber());
        if(list.getImage()!=null) {
            holder.usriImage.setImageBitmap(DataConverter.byteArrayToImage(list.getImage()));
        }
        else {

        }
        holder.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              userInterface.deleteUser(String.valueOf(list.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class UserAddViewHolder extends RecyclerView.ViewHolder{
        TextView userName,mobileNumber,dob,gender;
        ImageButton delButton;
        CircleImageView usriImage;
        public UserAddViewHolder(View itemview){
            super(itemview);
            userName=itemview.findViewById(R.id.rccontainer_name);
            mobileNumber=itemview.findViewById(R.id.rccontainer_mobile);
            usriImage=itemview.findViewById(R.id.recuser_image);

        }
    }


}


