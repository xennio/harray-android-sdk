package io.xenn.demo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.xenn.android.Xennio;
import io.xenn.android.common.ResultConsumer;
import io.xenn.android.utils.XennioLogger;
import io.xenn.fcmkit.FcmKitPlugin;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("Xennio", "Source:" + intent.getStringExtra("source"));
        Log.d("Xennio", "Realty List:" + intent.getStringExtra("realty_list"));
        Xennio.plugins().get(FcmKitPlugin.class).pushMessageOpened(intent);
        Xennio.plugins().get(FcmKitPlugin.class).resetBadgeCounts(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Xennio.login("2398336");
        Xennio.pushMessagesHistory().getPushMessagesHistory(10, new ResultConsumer<List<Map<String, String>>>() {
            @Override
            public void consume(List<Map<String, String>> data) {
                System.out.println("push notifications count: " + data.size());
            }
        });
        Xennio.eventing().pageView("homePage");
        Xennio.eventing().actionResult("test Action");
        Xennio.eventing().impression("productdetail");
        Xennio.eventing().custom("customEvent", new HashMap<String, Object>());

        Xennio.ecommerce().productView("1003", "small", 200d, 180d, "USD", null, "https://commercedemo.xenn.io/proteus-fitness-jackshirt.html");
        Xennio.memberSummary().getDetails("cf07c143-76fb-45b4-9d34-ff450eb4bc08", new ResultConsumer<JSONObject>() {
            @Override
            public void consume(JSONObject data) {
                XennioLogger.log("Reco data is here! : " + data.toString());
            }
        });

        Xennio.recommendations().getRecommendations("3b1d5ae7-3c95-4aa0-9ddc-a26765b1ad55",50,"city:Konya|Mardin,roomInfo:5+1","", new ResultConsumer<List<Map<String, String>>>() {
            @Override
            public void consume(List<Map<String, String>> data) {
                System.out.println("push notifications count: " + data.size());
            }
        });


        Xennio.browsingHistory().getBrowsingHistory("listings", 10, new ResultConsumer<List<Map<String, String>>>() {
            @Override
            public void consume(List<Map<String, String>> data) {
                XennioLogger.log("BrowsingHistory data is here! : " + data);
            }
        });
        Xennio.pushMessagesHistory().getPushMessagesHistory(10, new ResultConsumer<List<Map<String, String>>>() {
            @Override
            public void consume(List<Map<String, String>> data) {
                XennioLogger.log("PushMessagesHistory data is here! : " + data);
            }
        });

        Intent intent = getIntent();
        Log.d("Xennio", "Source:" + intent.getStringExtra("source"));
        Log.d("Xennio", "Realty List:" + intent.getStringExtra("realty_list"));
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // XennioAPI.pageView("300", "homePage", new HashMap<String, Object>());

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        Xennio.plugins().get(FcmKitPlugin.class).savePushToken(task.getResult().getToken());
                    }
                });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Xennio.logout();
                Xennio.eventing().actionResult("click");
                Snackbar.make(view, "Replace with your own action 2", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
