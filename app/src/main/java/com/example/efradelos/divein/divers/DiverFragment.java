package com.example.efradelos.divein.divers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.efradelos.divein.Constants;
import com.example.efradelos.divein.R;
import com.example.efradelos.divein.documents.Diver;
import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiverFragment extends Fragment {
    private static final int REQUEST_TAKE_PHOTO = 100;

    private Diver mDiver;
    private String mImagePathToBeAttached;

    public DiverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Uri diverUri = getActivity().getIntent().getData();
        if(diverUri != null) {
            Log.i("EFX", diverUri.getLastPathSegment());
        } else {
            mDiver = new Diver();
            mDiver.setLastName("Peterson");
            mDiver.setFirstName("Tom");
            mDiver.setYear("Sophormore");
        }

        View rootView = inflater.inflate(R.layout.fragment_diver, container, false);
        ImageView avatar = (ImageView)rootView.findViewById(R.id.avatar);

        avatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dispatchTakePhotoIntent();
                return true;
            }
        });

        Button button = (Button)rootView.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        Spinner spinner = (Spinner)rootView.findViewById(R.id.years_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.years_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        populate(mDiver);
    }

    private void dispatchTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                com.couchbase.lite.util.Log.e("EFX", "Cannot create a temp image file", e);
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException e) {
//                Log.e(Application.TAG, "Cannot create a temp image file", e);
//            }
//
//            if (photoFile != null) {
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
    }

    private void save() {

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "DIVE_IN_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg", storageDir);
        mImagePathToBeAttached = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mImagePathToBeAttached);
            mDiver.setAvatar(bitmap);
            populate(mDiver);

        }
    }

    protected void populate(Diver diver) {
        View view = getView();
        ((EditText)view.findViewById(R.id.first_name)).setText(diver.getFirstName());
        ((EditText)view.findViewById(R.id.last_name)).setText(diver.getLastName());
        Bitmap avatar = diver.getAvatar();
        if(avatar != null) {
            ((ImageView) view.findViewById(R.id.avatar)).setImageBitmap(avatar);
        }
    }
}
