package github.com.anurag145.srmhackathon;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.estimote.sdk.SystemRequirementsChecker;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import github.com.anurag145.srmhackathon.estimote.BeaconID;
import github.com.anurag145.srmhackathon.estimote.EstimoteCloudBeaconDetails;
import github.com.anurag145.srmhackathon.estimote.EstimoteCloudBeaconDetailsFactory;
import github.com.anurag145.srmhackathon.estimote.ProximityContentManager;

public class MainActivity extends AppCompatActivity {
    private ProximityContentManager proximityContentManager;
   private CardView cardView;
    ImageView imageView;
    TextView textView1;
    ImageView imageView1;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView =(TextView)findViewById(R.id.textView);
        cardView=(CardView) findViewById(R.id.cardview4);
        imageView=(ImageView)findViewById(R.id.photomy);
        textView1=(TextView)findViewById(R.id.delivery);
        imageView1=(ImageView)findViewById(R.id.pp);

        proximityContentManager = new ProximityContentManager(this,
                Arrays.asList(

                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 44248, 25292),
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 25373, 24529),
                        new BeaconID("B9407F30-F5F8-466E-AFF9-25556B57FE6D", 3, 3)),
                new EstimoteCloudBeaconDetailsFactory());
        proximityContentManager.setListener(new ProximityContentManager.Listener() {
            @Override
            public void onContentChanged(Object content) {


                if (content != null) {
                    EstimoteCloudBeaconDetails beaconDetails = (EstimoteCloudBeaconDetails) content;

                          textView.setText("FOUND 1 "+beaconDetails.getBeaconName());
                          imageView1.setVisibility(View.GONE);
                           textView.setVisibility(View.GONE);
                    cardView.setVisibility(View.VISIBLE);
                    if(beaconDetails.getBeaconName().equalsIgnoreCase("Aditya Chawla"))
                    {
                        imageView.setImageResource(R.mipmap.l);
                        textView1.setText("Marina Beach is a natural urban beach in the city of Chennai, India, along the Bay of Bengal.[1] The beach runs from near Fort St. George in the north to Foreshore Estate in the south, a distance of 6.5 km (4.0 mi),[2] making it the longest natural urban beach in the country and the world's first longest natural urban beach.[3][4][5] The Marina is primarily sandy, unlike the short, rocky formations that make up the Juhu Beach in Mumbai. The average width of the beach is 300 m (980 ft)[6] and the width at the widest stretch is 437 m (1,434 ft). Bathing and swimming at the Marina Beach are legally prohibited because of the dangers, as the undercurrent is very turbulent. It is one of the most crowded beaches in the country and attracts about 30,000 visitors a day during weekdays[7] and 50,000 visitors a day during the weekends and on holidays.[8][9][10] During summer months, about 15,000 to 20,000 people visit the beach daily.");
                    }else
                        imageView.setImageResource(R.mipmap.m);
                         textView1.setText("Located in the 'City of Temples', Ujjain, in the Indian state of Madhya Pradesh is the stunning Mahakaleshwar temple which is one of the most prominent temples in the country. Standing high in the state nicknamed 'The Heart of India', Mahakaleshwar temple is a multi tier temple that is considered divine and one of the ultimate destinations for Hindu pilgrims. It is also called 'Ujjain Temple' or 'Ujjain Mahakaleshwar Jyotirlinga Temple");

                } else {
                    textView.setText("No Beacon");

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            //wont be an issue (Say Amen)!
        } else {

            proximityContentManager.startContentUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        proximityContentManager.stopContentUpdates();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        proximityContentManager.destroy();
    }
    }

