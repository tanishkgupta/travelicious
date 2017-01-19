package github.com.anurag145.srmhackathon;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

public class FirebaseActivity extends AppCompatActivity {
   int flag=0;
  EditText editText;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView textView;
    EditText editText2;
    Button button;
    static String s1;
    static String s2;
    static DataSnapshot snppy;
    ScrollView scrollView;
    StringTokenizer stringTokenizer;
    private static final String SHAREDPREFFILE = "temp";
    ArrayList<String> ob = new ArrayList<>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        editText2=(EditText)findViewById(R.id.editText2);
        button=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);
        scrollView=(ScrollView)findViewById(R.id.scrollView);
         database = FirebaseDatabase.getInstance();
        SharedPreferences prefs = getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
        myRef = database.getReference("message");
        if((prefs.getString("city",null)==null))
        {
            myRef.child("Users").child("user"+linker.getEmail());
        }


        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                   if(flag==0) {
                       stringTokenizer = new StringTokenizer((snapshot.child("Users").child("user" + linker.getEmail()).getValue()).toString(), "-");


                       Log.e("jbssfks", (stringTokenizer.nextToken()));
                       try {
                           linker.bio = stringTokenizer.nextToken();

                           snppy=snapshot;
                           temp(stringTokenizer.nextToken());


                       } catch (Exception e) {
                           Log.e("heloo", e.toString());
                       }
                   }else
                   {
                       try {

                           snppy=snapshot;
                           textView.setText((snapshot.child(s1).getValue()).toString());

                       }catch (Exception e)
                       {
                       }
                       //do nothing
                   }            }
            public void temp(String s)
            {  s1=s;
                     flag=1;
                   editText.setVisibility(View.GONE);
                (findViewById(R.id.button)).setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                editText2.setVisibility(View.VISIBLE);

                scrollView.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       s2=linker.email+" : "+editText2.getText().toString();
                        Log.e("hello",s1);
                        myRef.child(s1).setValue(snppy.child(s1).getValue()+"\n"+s2);

                    }
                });


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
        editText=(EditText)findViewById(R.id.editText1);

    }
    public void onClick(View view)
    {
        if(!editText.getText().toString().equalsIgnoreCase(""))
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message");
            SharedPreferences prefs = getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putString("city",editText.getText().toString());
            editor.commit();


            myRef.child("Users").child("user"+linker.getEmail()).setValue(linker.email+"-"+linker.bio+"-"+editText.getText().toString());

        }
    }
}
