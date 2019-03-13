package wdx.musgig.venue_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.makeramen.roundedimageview.RoundedImageView;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import wdx.musgig.R;

import static com.vk.sdk.VKAccessToken.currentToken;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    VKAccessToken vkAccessToken;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public void vk_login(View view) {
        VKSdk.login(this);
    }

    public void google_login(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void logout(View view) {
        VKSdk.logout();
        mGoogleSignInClient.signOut();
        updateUI(null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                getVkInfo();
                Toast.makeText(LoginActivity.this, "Вы успешно авторизировались", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VKError error) {
                Toast.makeText(LoginActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        vkAccessToken = currentToken();
        updateUI(account, vkAccessToken);
    }

    public void setAccountData(String name, String id, String photo) {
        if ((name != null) && (id != null) && (photo != null)) {
            RoundedImageView acc_photo = findViewById(R.id.acc_photo);
            TextView acc_name = findViewById(R.id.acc_name);
            TextView acc_position = findViewById(R.id.acc_position);
            acc_name.setText(name);
            acc_position.setText(id);
            Glide.with(this).load(photo).into(acc_photo);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account, VKAccessToken vkAccessToken) {
        if ((account != null) || (vkAccessToken != null)) {
            if (account != null)
                setAccountData(account.getDisplayName(), account.getId(), String.valueOf(account.getPhotoUrl()));
            if (vkAccessToken != null) getVkInfo();
            findViewById(R.id.google).setVisibility(View.GONE);
            findViewById(R.id.vk).setVisibility(View.GONE);
            findViewById(R.id.logout).setVisibility(View.VISIBLE);
        } else {
            setAccountData("Залогиньтесь", "плизз", "https://vk.com/images/camera_200.png?ava=1");
            findViewById(R.id.google).setVisibility(View.VISIBLE);
            findViewById(R.id.vk).setVisibility(View.VISIBLE);
            findViewById(R.id.logout).setVisibility(View.GONE);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "Вы успешно авторизировались", Toast.LENGTH_SHORT).show();
            updateUI(account, vkAccessToken);
        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
            updateUI(null, vkAccessToken);
        }
    }

    public void getVkInfo() {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200_orig", "first_name", "last_name"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONArray resp = response.json.getJSONArray("response");
                    JSONObject jsonObject = resp.getJSONObject(0);
                    String photo = jsonObject.getString("photo_200_orig");
                    String name = jsonObject.getString("first_name") + " " + jsonObject.getString("last_name");
                    MainActivity MainActivity = new MainActivity();
                    VKAccessToken vkAccessToken = currentToken();
                    int id = vkAccessToken != null ? Integer.parseInt(vkAccessToken.userId) : 0;
                    setAccountData(name, String.valueOf(id), photo);
                    //Пишем в базу и вызываем интент
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
