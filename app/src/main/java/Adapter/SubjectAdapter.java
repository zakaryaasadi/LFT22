package Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shahbaapp.lft.NewsActivity;
import com.shahbaapp.lft.R;
import com.shahbaapp.lft.SessionActivity;

import java.util.List;

import javax.security.auth.Subject;

import Controller.Common;
import Models.CategoryClass;
import Models.ClassSubjectClass;
import Models.SubjectClass;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder>{

    Context context;


    private List<SubjectClass> OfferList;




    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView txt;
        ImageView image;

        public MyViewHolder(View view) {
            super(view);

            txt=(TextView)view.findViewById(R.id.title);


            image=(ImageView)view.findViewById(R.id.image);

        }

    }


    public SubjectAdapter(Context context, List<SubjectClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public SubjectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subject, parent, false);


        return new SubjectAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final SubjectAdapter.MyViewHolder holder, int position) {
        final SubjectClass subjectClass = OfferList.get(position);
        String title =  subjectClass.name;
        holder.txt.setText(title);


        title = title.toLowerCase();
        if(title.contains("anglais")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.anglais));
        }else if(title.contains("arabe")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.arabe));
        }
        else if(title.contains("art")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.art));
        }
        else if(title.contains("emc")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.emc));
        }
        else if(title.contains("eps")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.eps));
        }
        else if(title.contains("fran")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.francais));
        }
        else if(title.contains("histoire")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.histoire));
        }
        else if(title.contains("math")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.math));
        }
        else if(title.contains("musique")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.musique));
        }
        else if(title.contains("physic")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.physic));
        }
        else if(title.contains("science")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.sciences));
        }
        else if(title.contains("tech")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.technology));
        }
        else if(title.contains("ses")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.ses));
        }
        else if(title.contains("svt")){
            holder.image.setImageDrawable(context.getResources().getDrawable(R.drawable.svt));
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SessionActivity.class);
                i.putExtra("id",subjectClass.id);
                context.startActivity(i);
            }
        });

    }



    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


