package com.example.efradelos.divein.divers;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.example.efradelos.divein.Constants;
import com.example.efradelos.divein.R;
import com.example.efradelos.divein.data.DatabaseManager;
import com.example.efradelos.divein.documents.Diver;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiverFragment extends Fragment {
    private static final String LOG_TAG = "EFX";
    private static final int REQUEST_TAKE_PHOTO = 100;
    private static final int REQUEST_CHOOSE_PHOTO = 101;

    private Diver mDiver;
    private String mImagePathToBeAttached;
    private EditText mFirstNameField;
    private EditText mLastNameField;
    private ImageView mAvatarField;
    private ImageView mPhotoField;
    private PopupMenu mPopup;

    public DiverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "CREATED VIEW");
        Uri diverUri = getActivity().getIntent().getData();
        mDiver = new Diver();
        if (diverUri != null) {
            mDiver = (Diver)DatabaseManager.getDocument(diverUri.getLastPathSegment(), new TypeReference<Diver>() {});
        }

        View rootView = inflater.inflate(R.layout.fragment_diver, container, false);
        mFirstNameField = ((EditText) rootView.findViewById(R.id.first_name));
        mLastNameField = ((EditText) rootView.findViewById(R.id.last_name));
        mAvatarField = (ImageView) rootView.findViewById(R.id.avatar);
        mPhotoField = (ImageView) rootView.findViewById(R.id.choose_photo_button);
        mPopup = new PopupMenu(getActivity(), mPhotoField);

        //Inflating the Popup using xml file
        mPopup.getMenuInflater().inflate(R.menu.popup_photo, mPopup.getMenu());

        //registering popup with OnMenuItemClickListener
        mPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.choose_photo) {
                    dispatchChoosePhotoIntent();
                    return true;
                }
                if (id == R.id.take_photo) {
                    dispatchTakePhotoIntent();
                    return true;
                }
                if (id == R.id.remove_photo) {
                    mDiver.setAvatar(null);
                    repopulateView();
                }

                return false;
            }
        });

        mPhotoField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.show();
            }
        });

        Button button = (Button) rootView.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });

        Spinner spinner = (Spinner) rootView.findViewById(R.id.years_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.label_years, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        repopulateView();
        return rootView;
    }

    private void dispatchChoosePhotoIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CHOOSE_PHOTO);
    }

    private void dispatchTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    private void save() {
//        mDiver.setFirstName(mFirstNameField.getText().toString());
//        mDiver.setLastName(mLastNameField.getText().toString());
//
//        if (mRef == null) {
//            mRef = Constants.FIREBASE_REF.child(Diver.FIREBASE_PATH).push();
//            mDiver.setKey(mRef.getKey());
//        }
//        mRef.setValue(mDiver);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == getActivity().RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mDiver.setAvatar(bitmap);
            repopulateView();
        }
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == getActivity().RESULT_OK) {
            try {
                Uri selectedImage = data.getData();
                InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                mDiver.setAvatar(bitmap);
                repopulateView();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Unabled to choose photo", e);
            }
        }

    }

    protected void repopulateView() {
        mFirstNameField.setText(mDiver.getFirstName());
        mLastNameField.setText(mDiver.getLastName());
//        mYearField.setText(diver.getYear());
        Bitmap avatar = mDiver.getAvatar();
        if (avatar == null)
            avatar = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.profile);
        mAvatarField.setImageBitmap(avatar);
    }
}

