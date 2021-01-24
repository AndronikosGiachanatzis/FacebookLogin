package gr.uom.facebooklogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class InstagramHashtag extends AppCompatActivity {

    private Button returnButton;
    private Button searchButton;
    private AccessToken accessToken;
//    private CallbackManager callbackManager;


    private EditText hashtagText;
    private String hashtagId;
    private String hashtag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_hashtag);

        returnButton = findViewById(R.id.bt_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        searchButton = findViewById(R.id.search_bt);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hashtag = hashtagText.getText().toString();

                // get the hashtag id
                GraphRequest hashtagIdRequest = GraphRequest.newGraphPathRequest(
                        accessToken,
                        "/ig_hashtag_search",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                Log.d("DEMO", "Fetched Hashtag ID: " + response.getJSONObject().toString());

                                try {
                                    JSONObject hashtagJson = response.getJSONObject();

                                    hashtagId = ((JSONArray) response.getJSONObject().get("data"))
                                            .getJSONObject(0)
                                            .getString("id");


                                    Log.d("DEMO", hashtagId);
                                } catch (JSONException e){
                                    Log.d("DEMO", "Error during the reading of the hashtag id");
                                }

                                GraphRequest topHashtagsRequests = GraphRequest.newGraphPathRequest(
                                        accessToken,
                                        "/" + hashtagId + "/top_media",
                                        new GraphRequest.Callback() {
                                            @Override
                                            public void onCompleted(GraphResponse response) {
                                                Log.d("DEMO", response.toString());
                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString("user_id", "17841444624927306");
                                topHashtagsRequests.setParameters(parameters);
                                topHashtagsRequests.executeAsync();
                            }

                        });

                Bundle parameters = new Bundle();
                parameters.putString("user_id", "17841444624927306");
                parameters.putString("q", hashtag);
                hashtagIdRequest.setParameters(parameters);
                hashtagIdRequest.executeAsync();
            }
        });

        hashtagText = (EditText)findViewById(R.id.pt_hashtag);



        accessToken = AccessToken.getCurrentAccessToken();


//        callbackManager = CallbackManager.Factory.create();







    }
}