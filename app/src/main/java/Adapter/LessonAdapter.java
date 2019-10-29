package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shahbaapp.lft.LessonFileActivity;
import com.shahbaapp.lft.SubjectActivity;
import com.shahbaapp.lft.R;

import java.util.List;

import Controller.Common;
import Models.LessonClass;
import Models.UserTypeEnum;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.MyViewHolder>{

    Context context;


    private List<LessonClass> OfferList;




    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView txt, duration;

        public MyViewHolder(View view) {
            super(view);

            txt=(TextView)view.findViewById(R.id.title);
            duration=(TextView)view.findViewById(R.id.duration);

        }

    }


    public LessonAdapter(Context context, List<LessonClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public LessonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sessions, parent, false);


        return new LessonAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final LessonAdapter.MyViewHolder holder, final int position) {
        final LessonClass doc = OfferList.get(position);
        holder.txt.setText(doc.title);

        if(Common.getUser().getType().userType == 1 || Common.getUser().getType().userType == 0){
            holder.duration.setVisibility(View.VISIBLE);
            holder.duration.setText(String.valueOf(doc.duration));
        }else{
            holder.duration.setVisibility(View.GONE);
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LessonFileActivity.class);
                i.putExtra("title", doc.title);
                i.putExtra("files", new Gson().toJson(doc.files) ) ;
                context.startActivity(i);
            }
        });

    }



    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


