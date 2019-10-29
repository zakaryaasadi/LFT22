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
import com.shahbaapp.lft.LessonActivity;
import com.shahbaapp.lft.SubjectActivity;
import com.shahbaapp.lft.R;

import java.util.List;

import Models.SessionClass;

public class SessionAdapter extends RecyclerView.Adapter<SessionAdapter.MyViewHolder>{

    Context context;


    private List<SessionClass> OfferList;




    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView txt;

        public MyViewHolder(View view) {
            super(view);

            txt=(TextView)view.findViewById(R.id.title);

        }

    }


    public SessionAdapter(Context context, List<SessionClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public SessionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sessions, parent, false);


        return new SessionAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final SessionAdapter.MyViewHolder holder, final int position) {
        final SessionClass doc = OfferList.get(position);
        holder.txt.setText(doc.title);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, LessonActivity.class);
                i.putExtra("id", doc.id);
                i.putExtra("title", doc.title);
                i.putExtra("lessons", new Gson().toJson(doc.lessons) ) ;
                context.startActivity(i);
            }
        });

    }



    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


