package gr.uom.facebooklogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


// HRYd

public class MainActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    private ImageView imageView;
    private TextView textView;

    private ShareButton sbLink;
    private ShareButton sbPhoto;

    private Button switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login_button);
        textView = findViewById(R.id.tv_name);
        imageView = findViewById(R.id.iv_story);

        sbLink = findViewById(R.id.sb_link);
        sbPhoto = findViewById(R.id.sb_photo);

        switchButton = findViewById(R.id.switch_button);


        imageView.setImageResource(R.drawable.story);

        callbackManager = CallbackManager.Factory.create();

        // define extra permissions. use the button as it wraps the LoginManager class
        loginButton.setPermissions(Arrays.asList("user_gender, user_friends"));

        // register the callback manager and handle events
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("DEMO", "Login Successful!");
                sbLink.setEnabled(true);
                sbPhoto.setEnabled(true);
            }

            @Override
            public void onCancel() {
                Log.d("DEMO", "Login Cancelled!");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("DEMO", "Login Error!");

            }
        });

        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToInstaHashtagActivity();
            }
        });

    }


    private void switchToInstaHashtagActivity(){
        Intent switchActivityIntent = new Intent(this, InstagramHashtag.class);
        startActivity(switchActivityIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DEMO", "returned");
        // prepare content to be shared when the Share Link button is pressed
        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder().setContentUrl(Uri.parse(
                "https://www.youtube.com/watch?v=GxrxV37a9YE"))
                .setShareHashtag(new ShareHashtag.Builder()
                    .setHashtag("#success").build()).build();

        sbLink.setShareContent(shareLinkContent);

        // prepare photo to be shared when the Share Photo button is pressed
        try{
            Drawable drawable = imageView.getDrawable();
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();

            SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();

            sbPhoto.setShareContent(sharePhotoContent);}
        catch (NullPointerException e){
            Log.d("DEMO", "An unexpected Error occurred while handling the image to be shared");
        }

    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null){
                LoginManager.getInstance().logOut();
                textView.setText("");
                sbLink.setEnabled(false);
                sbPhoto.setEnabled(false);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        accessTokenTracker.stopTracking();
    }
}