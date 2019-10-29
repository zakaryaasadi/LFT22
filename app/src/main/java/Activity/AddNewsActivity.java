package Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.snatik.storage.Storage;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import org.angmarch.views.NiceSpinner;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import Adapter.AddNewsImageAdapter;
import Controller.Common;
import Controller.Api;
import Controller.DataFromApi;
import Models.NewsClass;
import Models.NewsResult;
import Models.SubcategoryClass;
import Models.SubcategoryResult;
import Utils.FileProcessing;
import Utils.ImageProcessing;
import Utils.NotifyMe;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.shahbaapp.lft.R;
import com.vincent.videocompressor.VideoCompress;

import static android.app.Activity.RESULT_OK;

public class AddNewsActivity extends Fragment {

    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    Integer notificationID = 69863;
    private ImageView btnGallery, btnCamera, myProfileImage;
    private EditText title, post;
    private TextView btnPost, myPersonName;
    private NewsClass news;
    private Api api;
    private NiceSpinner sprCategory;
    private List<SubcategoryClass> subcategory;
    private Intent intentCamera;
    private AlertDialog dialog;
    private RecyclerView recyclerview;
    private List<Uri> images;
    private LinearLayout form;
    private NotificationManager mNotifyManager;
    private Notification.Builder mBuilder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_news, container, false);
        news = new NewsClass();
        subcategory = new ArrayList<>();
        api = DataFromApi.getApi();



        btnGallery = view.findViewById(R.id.btn_grallery);
        btnCamera = view.findViewById(R.id.btn_camera);
        title = view.findViewById(R.id.title);
        post = view.findViewById(R.id.post);
        btnPost = view.findViewById(R.id.btn_post);
        myPersonName = view.findViewById(R.id.person_name);
        myProfileImage = view.findViewById(R.id.profile_image);
        sprCategory = view.findViewById(R.id.spr_category);
        recyclerview = view.findViewById(R.id.recyclerView);
        form = view.findViewById(R.id.form);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        images = new ArrayList<>();
        AddNewsImageAdapter adapter = new AddNewsImageAdapter(getActivity(), images);
        recyclerview.setAdapter(adapter);


        dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.dialog)
                .setCancelable(false).build();

        fetchSubCategoris();

        intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(getContext().getExternalCacheDir(), "temp.jpg");
        Uri photoURI = FileProcessing.getFileUri(f.getPath());
        intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        if (Common.getUser().profileImage != null) {
            myProfileImage.setImageBitmap(ImageProcessing.base64ToBitmap(Common.getUser().profileImage));
        }

        myPersonName.setText(Common.getUser().fullName);


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureActionIntent = null;

                pictureActionIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(
                        pictureActionIntent,
                        GALLERY_PICTURE);
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PermissionChecker.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_REQUEST_CODE);
                } else
                    startActivityForResult(intentCamera, CAMERA_REQUEST);
            }
        });


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (title.getText().toString().equals(""))
                    Toast.makeText(getContext(), "Please enter title for post", Toast.LENGTH_LONG).show();

                else if (post.getText().toString().equals(""))
                    Toast.makeText(getContext(), "Please enter your post", Toast.LENGTH_LONG).show();

                else if (images.size() == 0)
                    Toast.makeText(getContext(), "Please put image for news", Toast.LENGTH_LONG).show();

                else {
                    startDialog();
                }
            }
        });


        return view;

    }


    public void fetchSubCategoris() {
        Call<SubcategoryResult> call = api.GetPermission(Common.categoryClass.getId(), Common.getUser().id);
        dialog.setMessage("Please wait...");
        dialog.show();

        call.enqueue(new Callback<SubcategoryResult>() {
            @Override
            public void onResponse(Call<SubcategoryResult> call, Response<SubcategoryResult> response) {
                dialog.dismiss();
                SubcategoryResult subcategoryResult = response.body();
                if (subcategoryResult != null) {
                    if (subcategoryResult.results != null && subcategoryResult.results.size() > 0) {
                        form.setVisibility(View.VISIBLE);
                        subcategory.addAll(subcategoryResult.results);
                        List<String> data = new ArrayList<>();
                        for (SubcategoryClass item : subcategory)
                            data.add(item.getTitle());

                        sprCategory.attachDataSource(new LinkedList<>(data));

                    } else {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ExploreActivity()).commit();
                        Toast.makeText(getContext(), "You do not have permission to add news to this subcategory, " +
                                "please request permission from the administrator", Toast.LENGTH_LONG).show();
                    }
                }
            }


            @Override
            public void onFailure(Call<SubcategoryResult> call, Throwable t) {
                dialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ExploreActivity()).commit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            File f = new File(getContext().getExternalCacheDir()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }
            images.add(Uri.fromFile(f));
            recyclerview.getAdapter().notifyDataSetChanged();


        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                images.add(selectedImage);
                recyclerview.getAdapter().notifyDataSetChanged();

            } else {
                Toast.makeText(getContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                startActivityForResult(intentCamera, CAMERA_REQUEST);

            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void uploadMultipart(long newsId) {
        Storage storage = new Storage(getContext());
        for (int i = 0; i < images.size(); i++)

            if (isMediaImage(images.get(i))) {
                try {
                    String fileName = String.valueOf(i) + ".jpg";
                    String path = storage.getInternalCacheDirectory() + File.separator + fileName;
                    storage.createFile(path, ImageProcessing
                            .resizeToBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), images.get(i)),
                                    600));
                    new MultipartUploadRequest(getContext(), Api.HOST + "news/addimage")
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addHeader("news_id", String.valueOf(newsId))
                            .addFileToUpload(path, fileName)
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
                } catch (Exception exc) {
                    Log.e("AndroidUploadService", exc.getMessage(), exc);
                }

            } else {

                String fileName = String.valueOf(i) + ".mp4";
                String videoPath = getPathVideoFromUri(images.get(i));
                String destPath = storage.getInternalCacheDirectory() + File.separator + fileName;


                NotifyMe.MyNotify myNotify = NotifyMe.with(getActivity(), "x", i)
                        .progress(0, 100)
                        .autoCancel(false)
                        .lockscreenVisibility(true)
                        .build();
                VideoCompress.VideoCompressTask task = VideoCompress.compressVideoLow(videoPath, destPath, new VideoCompress.CompressListener() {
                    @Override
                    public void onStart() {
                        myNotify.title("Compressing your video").show();
                    }

                    @Override
                    public void onSuccess() {
                        myNotify.message("Compress is complete")
                                .hideProgressbar()
                                .show();
                        try {
                            new MultipartUploadRequest(getContext(), Api.HOST + "news/AddVideo")

                                    .addHeader("news_id", String.valueOf(newsId))
                                    .addFileToUpload(destPath, fileName)
                                    .setNotificationConfig(new UploadNotificationConfig())
                                    .setMaxRetries(2)
                                    .startUpload();
                        } catch (Exception exc) {
                            Log.e("AndroidUploadService", exc.getMessage(), exc);
                        }
                    }

                    @Override
                    public void onFail() {
                        myNotify.message("Compress is fail")
                                .hideProgressbar()
                                .show();

                    }

                    @Override
                    public void onProgress(float percent) {
                        myNotify.onNext((int) percent).show();
                    }
                });


            }


    }


    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getContext());

        myAlertDialog.setTitle("Confirm message");
        myAlertDialog.setMessage("Are you sure want to add this news?");

        AlertDialog stopAlert = myAlertDialog.create();
        myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                stopAlert.cancel();
            }
        });

        myAlertDialog.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String[] words = post.getText().toString().split(" ");
                        String headline = "";
                        for (int i = 0; i < words.length; i++) {
                            if (i == 9)
                                break;
                            headline += words[i] + " ";
                        }


                        news.setUserId((int) Common.getUser().id);
                        news.setType(2);
                        news.setSubcategory(subcategory.get(sprCategory.getSelectedIndex()));
                        news.setTitle(title.getText().toString());
                        news.setHeadLine(headline);
                        news.setBody(post.getText().toString());
                        news.setSharable(true);
                        news.setPrivateNewsType(0);
                        dialog.setMessage("Sending...");
                        dialog.show();
                        Call<NewsResult> call = api.AddNews(news);

                        call.enqueue(new Callback<NewsResult>() {
                            @Override
                            public void onResponse(Call<NewsResult> call, Response<NewsResult> response) {
                                dialog.dismiss();
                                NewsResult newsResult = response.body();
                                if (newsResult != null) {
                                    if (newsResult.results != null) {
                                        uploadMultipart(newsResult.results.get(0).getId());
//                                        NewsActivity.toolbar.setTitle("Explore");
//                                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                                new ExploreActivity()).commit();

                                    } else {
                                        Toast.makeText(getContext(), newsResult.status, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }


                            @Override
                            public void onFailure(Call<NewsResult> call, Throwable t) {
                                dialog.dismiss();
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        myAlertDialog.show();
    }


    private boolean isMediaImage(Uri uri) {
        String b = getActivity().getContentResolver().getType(uri);
        return b.contains("image");
    }


    private String getPathVideoFromUri(Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return picturePath;
    }


}