package algonquin.cst2335.kala0049.UI;

import static algonquin.cst2335.kala0049.UI.ChatRoom.chatModel;
import static algonquin.cst2335.kala0049.UI.ChatRoom.mDAO;
import static algonquin.cst2335.kala0049.UI.ChatRoom.messages;
import static algonquin.cst2335.kala0049.UI.ChatRoom.myAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.kala0049.Data.ChatMessage;
import algonquin.cst2335.kala0049.Data.ChatMessageDAO;
import algonquin.cst2335.kala0049.Data.ChatRoomViewModel;
import algonquin.cst2335.kala0049.Data.MessageDatabase;
import algonquin.cst2335.kala0049.Data.MessageDetailsFragment;
import algonquin.cst2335.kala0049.R;
import algonquin.cst2335.kala0049.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.kala0049.databinding.ReceiveMessageBinding;
import algonquin.cst2335.kala0049.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    static ChatRoomViewModel chatModel;
    static ArrayList<ChatMessage> messages;
    ActivityChatRoomBinding binding;
    static  RecyclerView.Adapter<MyRowHolder> myAdapter;
    static  ChatMessageDAO mDAO;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch( item.getItemId() ) {
            case R.id.item_1:
                ChatMessage selectedMessage = chatModel.selectedMessage.getValue();
                if (selectedMessage != null) {
                    int position = messages.indexOf(selectedMessage);
                    if (position != -1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Do you want to delete the message: " + selectedMessage.getMessage())
                                .setTitle("Question:")
                                .setPositiveButton("Yes", (dialogI, cl) -> {
                                    ChatMessage m = messages.get(position);
                                    messages.remove(position);
                                    myAdapter.notifyItemRemoved(position);

                                    Executor thread = Executors.newSingleThreadExecutor();
                                    thread.execute(() -> {
                                        mDAO.deleteMessage(m);
                                    });

                                    Snackbar.make(binding.getRoot(), "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                            .setAction("Undo", snackbar -> {
                                                messages.add(position, m);
                                                myAdapter.notifyItemInserted(position);
                                                Executor thread1 = Executors.newSingleThreadExecutor();
                                                thread1.execute(() -> {
                                                    mDAO.insertMessage(m);
                                                });
                                            })
                                            .show();
                                })
                                .setNegativeButton("No", (dialogI, cl) -> {})
                                .create()
                                .show();
                    }
                }
                break;

            case R.id.item_2:
                Toast.makeText(this, "Version 1.0, created by Sujal Kalathiya", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setSupportActionBar(binding.myToolbar);


        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "MessageDatabase").build();
        mDAO = db.cmDAO();

        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            messages = new ArrayList<>(); // just update the list, do not reassign
            chatModel.messages.postValue(messages);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database
                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

        chatModel.messages.observe(this, new Observer<ArrayList<ChatMessage>>() {
            @Override
            public void onChanged(ArrayList<ChatMessage> chatMessages) {
                myAdapter.notifyDataSetChanged();
            }
        });

        setContentView(binding.getRoot());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");

        binding.sendButton.setOnClickListener(click -> {
            String currentDateandTime = sdf.format(new Date());
            ChatMessage message = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, true);
            messages.add(message);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                message.id=(int) mDAO.insertMessage(message);
            });

        });

        binding.receiveButton.setOnClickListener(click -> {
            String currentDateandTime = sdf.format(new Date());
            ChatMessage message = new ChatMessage(binding.textInput.getText().toString(), currentDateandTime, false);
            messages.add(message);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                message.id=(int) mDAO.insertMessage(message);
            });
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


        chatModel.selectedMessage.observe(this, (newMessageValue) -> {
            MessageDetailsFragment chatFragment = new MessageDetailsFragment( newMessageValue );
            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();
            tx.replace(R.id.fragmentLocation, chatFragment);
            tx.addToBackStack("");
            tx.commit();

        });

    }
}

class MyRowHolder extends RecyclerView.ViewHolder {
    TextView messageText;
    TextView timeText;
    ImageView senderImage;

    public MyRowHolder(@NonNull View itemView) {
        super(itemView);

        itemView.setOnClickListener(clk -> {
            int position = getAbsoluteAdapterPosition();
            ChatMessage selected = messages.get(position);

            chatModel.selectedMessage.postValue(selected);


        });

        messageText = itemView.findViewById(R.id.messageText);
        timeText = itemView.findViewById(R.id.timeText);
        senderImage = itemView.findViewById(R.id.senderImage);
    }
}


