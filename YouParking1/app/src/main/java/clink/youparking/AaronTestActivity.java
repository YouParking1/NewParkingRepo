package clink.youparking;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class AaronTestActivity extends AppCompatActivity {

    private EditText mInputMessageView;

    private String room = null;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://108.167.99.14:88");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... arg) {
            final Object args = arg[0];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //JSONObject data = (JSONObject) args;
                    String lat;
                    String mLong;
//                    try {
//                        lat = data.getString("Latitude");
//                        mLong = data.getString("Longitude");
//                    } catch (JSONException e) {
//                        return;
//                    }


                    //String combined = lat + " " + mLong;
                    JSONObject data = (JSONObject) args;
                    String message = "";
                    try {
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    TextView myTextView = (TextView) findViewById(R.id.from_server);
                    myTextView.setText(message);
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aaon_test);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            room = extras.getString("ROOM");
        }
        System.out.println(room);

        mSocket.connect();
        mSocket.on("message", onNewMessage);


        loginServer();
        joinRoom();
        attemptSend();
    }

    private void attemptSend() {
        //String message = mInputMessageView.getText().toString().trim();
//        if (TextUtils.isEmpty(message)) {
//            return;
//        }
        //mInputMessageView.setText("Bob is cool");
        String message = "Hello Frank";
        mSocket.emit("message", message);
    }

    private void loginServer() {
        JSONObject loginInfo = new JSONObject();
        try {
            loginInfo.put("email", User.email);
        } catch (JSONException e) {

        }
        mSocket.emit("login", User.email);
    }

    private void joinRoom() {
        if (room != null) {
            mSocket.emit("joinRoom", room);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
    }

}
