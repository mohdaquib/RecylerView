package aquib.com.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.CustomViewHolder> {

    private List<FeedItem> feedItems;
    private Context context;

    public MyRecyclerAdapter(Context context, List<FeedItem> items){
        this.context = context;
        this.feedItems = items;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        FeedItem feedItem = feedItems.get(position);

        holder.titleTextView.setText(feedItem.getTitle());
        holder.dateTextView.setText("Date " + feedItem.getDate());


        holder.titleTextView.setOnClickListener(clickListener);
        holder.titleTextView.setTag(holder);
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CustomViewHolder holder = (CustomViewHolder) v.getTag();
            int position = holder.getAdapterPosition();

            FeedItem item = feedItems.get(position);

            // Instantiate Intent to open browser with given URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
            context.startActivity(intent);
        }
    };


    @Override
    public int getItemCount() {
        return (null != feedItems ? feedItems.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        protected TextView titleTextView;
        protected TextView dateTextView;

        public CustomViewHolder(View view){
            super(view);
            this.titleTextView = (TextView) view.findViewById(R.id.title);
            this.dateTextView = (TextView) view.findViewById(R.id.date);
        }
    }
}
