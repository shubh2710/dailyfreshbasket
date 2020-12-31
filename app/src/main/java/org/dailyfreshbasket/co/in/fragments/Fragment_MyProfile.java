package org.dailyfreshbasket.co.in.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.dailyfreshbasket.co.in.R;
import org.dailyfreshbasket.co.in.myInterface.getResponce;
import org.dailyfreshbasket.co.in.networks.SendRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_LOGIN;
import static org.dailyfreshbasket.co.in.informations.GetDomain.KEYS.URL_UPDATE;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_NAME;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_PROFILE_PIC;
import static org.dailyfreshbasket.co.in.informations.SheardPrefsKeys.KEYS.PREF_KEY_UID;

public class Fragment_MyProfile extends Fragment implements getResponce {
    private EditText name,email,mobile;
    private ImageView pic;
    private Button chnage;
    private RadioGroup gender;
    private CropImageView cropImageView;
    private Uri filePath;
    private Button save;
    private ProgressDialog pd;
    private String uid;
    private Context context=getActivity();
    private Uri imageUri=null;
    private File f=null;
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_user_detail_my_profile, viewGroup, false);

            name=(EditText)view.findViewById(R.id.et_profile_name);
            email=(EditText)view.findViewById(R.id.et_profile_email);
            mobile=(EditText)view.findViewById(R.id.et_profile_mobile);
            chnage=(Button)view.findViewById(R.id.b_profile_chnage);
            save=(Button)view.findViewById(R.id.b_profile_save);
            pic=(ImageView) view.findViewById(R.id.iv_profile_pic);
            gender=(RadioGroup) view.findViewById(R.id.rg_profile_gender);
            email.setEnabled(false);
            pd = ProgressDialog.show(getActivity(), "Getting info", "wait...");
            pd.setCancelable(true);
            getUserData();
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pd = ProgressDialog.show(getActivity(), "Updating", "wait...");
                    pd.setCancelable(true);
                    if(imageUri!=null){
                        saveWithPic();
                    }else{
                        saveWithOutPic();
                    }
                }
            });
            chnage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFileChooser();
                }
            });
            return view;
        }

    private void getUserData() {
        SendRequest req=new SendRequest(URL_LOGIN,this,1);
        String uid=readPrefes(getActivity(),PREF_KEY_UID,"");
        this.uid=uid;
        if(uid.length()>1){
            Map<String,String> params = new HashMap<String, String>();
            params.put("password","");
            params.put("uid",uid);
            req.setParameters(params);
            req.send();
        }else {
            pd.dismiss();
        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    private void saveWithPic() {
        try {
            String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request
            Log.d("UID",uploadId);
            String name=this.name.getText().toString();
            String email=this.email.getText().toString();
            String mobile=this.mobile.getText().toString();
            String gen="";
            switch (this.gender.getCheckedRadioButtonId()){
                case R.id.rb_profile_female:
                    gen="female";
                    break;
                case R.id.rb_profile_male:
                    gen="male";
                    break;
            }
            Log.d("uid", "saveWithPic url: "+imageUri.getPath());
            new MultipartUploadRequest(getContext(), uploadId, URL_UPDATE)
                    .addFileToUpload(imageUri.getPath(), "file")
                    .addParameter("name",name)
                    .addParameter("mobile",mobile)
                    .addParameter("gender",gen)
                    .addParameter("uid",uid)
                    .addParameter("password","")
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(1)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(UploadInfo uploadInfo, Exception exception) {

                        }
                        @Override
                        public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                            responce(serverResponse.getBodyAsString(), 2);
                            Log.d("responce", "onCompleted: "+serverResponse.getBodyAsString());
                            pd.dismiss();
                            //VollyRequest(email,password);
                        }

                        @Override
                        public void onCancelled(UploadInfo uploadInfo) {
                            Toast.makeText(context,"File Cancelled",Toast.LENGTH_LONG).show();
                        }
                    })
                    .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void saveWithOutPic(){
        String name=this.name.getText().toString();
        String email=this.email.getText().toString();
        String mobile=this.mobile.getText().toString();
        String gen="";
        switch (this.gender.getCheckedRadioButtonId()){
            case R.id.rb_profile_female:
                gen="female";
                break;
            case R.id.rb_profile_male:
                gen="male";
                break;
        }
        Log.d("save", "saveWithOutPic: ");
        SendRequest req=new SendRequest(URL_UPDATE,this,2);
        String uid=readPrefes(getActivity(),PREF_KEY_UID,"");
        this.uid=uid;
        if(uid.length()>1){
            Map<String,String> params = new HashMap<String, String>();
            params.put("name",name);
            params.put("mobile",mobile);
            params.put("gender",gen);
            params.put("uid",uid);
            params.put("password","");
            req.setParameters(params);
            req.send();
        }else {
            pd.dismiss();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.d("File Path",filePath.toString());
            CropImage.activity(filePath)
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d("url", "onActivityResult: "+resultUri);
                Picasso.with(getActivity())
                        .load(resultUri).
                        placeholder(R.drawable.ic_019_user)
                        .noFade()
                        .into(pic, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }
                            @Override
                            public void onError() {
                            }
                        });

                imageUri=resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 123);
    }

    @Override
    public void responce(String res, int requestCode) {
        Log.d("responce ", "responce: "+res);
        pd.dismiss();
        switch (requestCode){
            case 1:
                try {
                    JSONObject responce=new JSONObject(res);
                    if(responce.getBoolean("success")){
                        if(!responce.isNull("data")){
                            JSONObject user=responce.getJSONObject("data");
                            String uid=user.getString("uid");
                            String name=user.getString("name");
                            String email=user.getString("email");
                            String mobile=user.getString("mobile");
                            String googleUid=user.getString("googleUid");
                            String gender=user.getString("gender");
                            String pic=user.getString("pic");
                            this.name.setText(name);
                            this.mobile.setText(mobile);
                            this.email.setText(email);
                            if(pic.length()>2)
                            Picasso.with(context)
                                    .load(pic)
                                    .resize(200,200)
                                    .centerInside()
                                    .placeholder(R.drawable.placeholder)
                                    .into(this.pic);
                            if(gender.equals("male")){
                                this.gender.check(R.id.rb_profile_male);
                            }else{
                                this.gender.check(R.id.rb_profile_female);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject responce=new JSONObject(res);
                    if(responce.getBoolean("success")){
                        Toast.makeText(getActivity(),"Profile Updated",Toast.LENGTH_LONG).show();
                        if(!responce.isNull("data") && responce.getString("data").length()>5)
                        saveToPref(getActivity(),PREF_KEY_PROFILE_PIC,responce.getString("data"));
                        saveToPref(getActivity(),PREF_KEY_NAME,name.getText().toString());
                    }
                    }catch (JSONException j){
                    Toast.makeText(getActivity(),"error in updating",Toast.LENGTH_LONG).show();
                    }
                break;
        }
    }
    public static void saveToPref(Context context, String preferenceName, String preferenceValue ){
        SharedPreferences sheredPreference=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sheredPreference.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
}