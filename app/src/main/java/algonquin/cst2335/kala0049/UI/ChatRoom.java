package algonquin.cst2335.kala0049.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.kala0049.Data.ChatMessage;
import algonquin.cst2335.kala0049.Data.ChatRoomViewModel;
import algonquin.cst2335.kala0049.R;
import algonquin.cst2335.kala0049.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.kala0049.databinding.ReceiveMessageBinding;
import algonquin.cst2335.kala0049.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ChatRoomViewModel chatModel ;
    ArrayList<ChatMessage> messages;
    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            messages = new ArrayList<ChatMessage>(); // just update the list, do not reassign
            chatModel.messages.postValue(messages);
        }

        chatModel.messages.observe(this, new Observer<ArrayList<ChatMessage>>() {
            @Override
            public void onChanged(ArrayList<ChatMessage> chatMessages) {
                myAdapter.notifyDataSetChanged();
            }
        });

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");

        binding.sendButton.setOnClickListener(click -> {
            String currentDateandTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, true));
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            String currentDateandTime = sdf.format(new Date());
            messages.add(new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, false));
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0){
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder( binding.getRoot());
                }else{
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder( binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {
                return messages.get(position).isSentButton() ? 0 : 1;
            }
        });

    }
}

class MyRowHolder extends RecyclerView.ViewHolder {
    TextView messageText;
    TextView timeText;
    ImageView senderImage; // Update this with your image view ID

    public MyRowHolder(@NonNull View itemView) {
        super(itemView);
        messageText = itemView.findViewById(R.id.messageText);
        timeText = itemView.findViewById(R.id.timeText);
        senderImage = itemView.findViewById(R.id.senderImage); // Update this with your image view ID
    }
}

