package github.com.anurag145.srmhackathon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.estimote.sdk.cloud.internal.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity  {
    private static final String SHAREDPREFFILE = "temp";
    EditText editText;
    EditText editText1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editText=(EditText) findViewById(R.id.editText);
        editText1=(EditText)findViewById(R.id.editText1);
        SharedPreferences prefs = getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
        if(!(prefs.getString("login",null)==null))

        {
            linker.email=prefs.getString("login",null);
            startActivity(new Intent(Login.this,FirebaseActivity.class));
            finish();
        }
    }



    public void onClick(View view)
    {
        if(!editText.getText().toString().equalsIgnoreCase(""))
        {
            SharedPreferences prefs = getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("login",editText.getText().toString());
                        editor.commit();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");
            linker.bio=editText1.getText().toString();
            linker.email=editText.getText().toString();
            Log.e("tag",linker.email);

            myRef.child("Users").child("user"+linker.getEmail()).setValue(linker.email+"-"+linker.bio);
            startActivity(new Intent(Login.this,FirebaseActivity.class));
            finish();

        }
    }
}
