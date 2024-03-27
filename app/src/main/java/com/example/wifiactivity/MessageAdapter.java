package com.example.wifiactivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> messages;

    public MessageAdapter(Context context, List<String> messages) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.message_item, parent, false);
        }
        TextView messageTextView = view.findViewById(R.id.messageTextView);
        messageTextView.setText(messages.get(position));
        return view;
    }
}
