package Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;

import Utils.ImageProcessing;
import com.shahbaapp.lft.R;

public class AddNewsImageAdapter extends RecyclerView.Adapter<AddNewsImageAdapter.MyViewHolder> {

    Context context;
    private List<Uri> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image, cancel;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            cancel = view.findViewById(R.id.cancel);
        }
    }


    public AddNewsImageAdapter(Context context, List<Uri> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public AddNewsImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_image, parent, false);


        return new AddNewsImageAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        Uri i = OfferList.get(position);

        Bitmap bitmap = null;
        if(isMediaImage(i)) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), i);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            bitmap = ThumbnailUtils.createVideoThumbnail(getPathVideoFromUri(i), MediaStore.Images.Thumbnails.MINI_KIND);
        }

        holder.image.setImageBitmap(ImageProcessing.resizeToBitmap(bitmap, 200));

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferList.remove(position);
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return OfferList.size();
    }



    private boolean isMediaImage(Uri uri){
        String b = context.getContentResolver().getType(uri);
        return b.contains("image");
    }

    private String getPathVideoFromUri(Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return  picturePath;
    }

}


