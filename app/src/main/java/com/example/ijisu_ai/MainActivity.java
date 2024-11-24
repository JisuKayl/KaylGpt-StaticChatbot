package com.example.ijisu_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText message_text_text;
    ImageView send_btn;
    TextView welcome;
    List<Message> messageList = new ArrayList<>();
    MessageAdapter messageAdapter;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        message_text_text = findViewById(R.id.message_text_text);
        send_btn = findViewById(R.id.send_btn);
        recyclerView = findViewById(R.id.recyclerView);
        welcome = findViewById(R.id.welcomeTv);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);

        send_btn.setOnClickListener(view -> {
            String question = message_text_text.getText().toString().trim();
            addToChat(question, Message.SEND_BY_ME);
            message_text_text.setText(null);
            welcome.setVisibility(View.GONE);
            callAPI(question);
        });

    }

    void addToChat(String message, String sendBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sendBy));
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SEND_BY_BOT);
    }

    int staticResponseIndex = 0;

    void callAPI(String question) {
        // okhttp
        addToChat("Typing...", Message.SEND_BY_BOT);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String[] staticResponses = {
                        "Hi! I'm Kayl-Gpt, a simple chatbot here to chat with you! 😊",
                        "I may not be the smartest AI, but I'll try my best to keep the conversation going. 😅",
                        "Just so you know, I’m a static bot, so I can only give you pre-programmed responses. 😬",
                        "Need help with anything? I'm here to chat! 😎",
                        "Okay, let's talk! But remember, I'm just a static chatbot. 😄",
                        "I can tell you jokes, though! Want to hear one? 🤔",
                        "What do you call a big surprise? 🤨",
                        "It’s a SHOCKolate! 😆 🍫",
                        "Why don’t skeletons fight each other? 💀",
                        "They don’t have the guts! 😜",
                        "Why do cows have hooves instead of feet? 🐄",
                        "Because they lactose! 😂🐄",
                        "What’s orange and sounds like a parrot? 🦜",
                        "A carrot! 🥕😂",
                        "What did one ocean say to the other? 🌊",
                        "Nothing, they just waved! 🌊👋",
                        "What did the left eye say to the right eye? 👀",
                        "Between you and me, something smells! 😆",
                        "Why don't scientists trust atoms? 🤔",
                        "Because they make up everything! 😆",
                        "What's a skeleton's least favorite room? 💀",
                        "The living room! 😂"
                };

                if (staticResponseIndex < staticResponses.length) {
                    addResponse(staticResponses[staticResponseIndex]);
                    staticResponseIndex++;
                } else {
                    addResponse("Sorry, I’ve run out of jokes for now. 😅 Maybe we can chat again later?");
                }
            }
        }, 1500);
    }
}

/*
        // Note: This commented section is for dynamic api response from chagpt api (Currently have issue. I think this requires to setup billing first before it work well....xD)

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "text-davinci-003");
            jsonBody.put("prompt", question);
            jsonBody.put("max_tokens", 4000);
            jsonBody.put("temperature", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody requestBody = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url(API.API_URL)
                .header("Authorization", "Bearer " + API.API)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Kayl-Gpt Responded. Remember that this is only a static response. Payment is required to unlock real gpt :)");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getString("text");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    addResponse("Kayl-Gpt Responded \uD83E\uDD73 Remember that this is only a static response. Payment is required to unlock real gpt \uD83E\uDD79 Sorry, no budget hehe \uD83D\uDE01");
                }

            }
        });

    }

}
*/