package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adapter is responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    // Interface in ItemsAdapter that MainActivity will implement
    public interface OnLongClickListener{
        // We use position because MainActivity needs to know the
        // position of where we did the long press, so it can notify the adapter,
        // that's the position we must delete.
        void onItemLongClicked(int position);
    }

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    // Variable to used to reference in all methods
    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    // Responsible for creating each view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Use layout inflater to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        // Wrap it inside a View Holder and return it
        return new ViewHolder(todoView) ;
    }

    @Override
    // Taking data from a certain position and placing it into view holder
    // Responsible for binding data to a particular view holder
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Grab the item at the position
        String item = items.get(position);

        // Bind the item into the specified view holder
        holder.bind(item);

    }

    @Override
    // Number of items available in the data
    // Tell the RV how many items are in the list
    public int getItemCount() {
        return items.size();
    }

    // Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // A view that we are passing inside of the view holder
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        // Update the view inside of the view holder with this data
        public void bind(String item) {
            // Set the text on the text view, to be the contents of the item we are passing
             tvItem.setText(item);

             // Attach Click Listener to our Text View
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

             // Long Click Listener for removing item from Recycler View
            // Need to pass information from MainActivity into ItemsAdapter
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // Notifies the listener of which position was long pressed
                    // getAdapterPosition  gets the position of where the ViewHolder is
                    longClickListener.onItemLongClicked(getAdapterPosition());

                    // True instead of false, so the callback consumes the long click
                    return true;
                }
            });
        }
    }
}
