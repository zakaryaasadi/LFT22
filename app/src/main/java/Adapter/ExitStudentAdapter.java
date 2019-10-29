package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shahbaapp.lft.LessonActivity;
import com.shahbaapp.lft.R;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.ExistStudentClass;
import Models.SessionClass;

public class ExitStudentAdapter extends RecyclerView.Adapter<ExitStudentAdapter.MyViewHolder> {

    Context context;


    private List<ExistStudentClass> OfferList;
    private HashMap<Integer, EditText> editTexts = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(){
        for(Integer i : editTexts.keySet()){
            ExistStudentClass e = OfferList.stream().filter(o -> o.id == i).findFirst().orElse(null);
            e.note = editTexts.get(i).getText().toString();
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView name;
        CheckBox exist, later;
        EditText note;

        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.person_name);
            exist = view.findViewById(R.id.exist);
            later = view.findViewById(R.id.later);
            note = view.findViewById(R.id.note);

        }

    }


    public ExitStudentAdapter(Context context, List<ExistStudentClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public ExitStudentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exit_student, parent, false);


        return new ExitStudentAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final ExitStudentAdapter.MyViewHolder holder, final int position) {
        final ExistStudentClass doc = OfferList.get(position);
        holder.name.setText(doc.name);
        holder.exist.setChecked(doc.exist == 1);
        holder.later.setChecked(doc.later == 1);
        holder.note.setText(doc.note);

        editTexts.put(doc.id, holder.note);


        holder.exist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doc.exist = isChecked ? (short)1 : 0;
                if(!isChecked &&  holder.later.isChecked())
                    holder.later.setChecked(false);
            }
        });

        holder.later.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                doc.later = isChecked ? (short)1 : 0;
                if(isChecked &&  !holder.exist.isChecked())
                    holder.exist.setChecked(true);
            }
        });
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


