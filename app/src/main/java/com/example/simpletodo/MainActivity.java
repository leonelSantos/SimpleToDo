package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    Button addBttn;
    EditText etItem;
    RecyclerView rvItems;
    // Created as a field to be accessible to all methods in the class
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBttn = findViewById(R.id.addBttn);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        // Variable that is passed as the second argument in our itemsAdapter
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            // At this point we have entered into the position where the user has long pressed
            public void onItemLongClicked(int position) {
                // 1. Delete the item from model
                items.remove(position);

                // 2. Notify the Adapter
                itemsAdapter.notifyItemRemoved(position );

                // 3. Notify user that item was removed
                Toast.makeText(getApplicationContext(), "Item Removed!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            // Method executed when single tapping on item
            public void onItemClicked(int position) {
                Log.d("MainActivity", "Single Click on Position" + position);
                // 1. Create new activity, using intent
                // Intent is a request to the android system
                 Intent i = new Intent(MainActivity.this, EditActivity.class);
                // 2. Pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                 i.putExtra(KEY_ITEM_POSITION, position);
                // 3. Display the activity
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };

        // Construct adapter
        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        // Adding Click Listener onto button
        // Notifies every time user clicks on button, and takes corresponding action
        addBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toDoItem = etItem.getText().toString();

                // 1. Add Item to the model
                items.add(toDoItem);

                // 2. Notify the adapter that an item is inserted
                // Position where item is inserted is in the last position in the model
                itemsAdapter.notifyItemInserted(items.size() - 1);

                // 3. Clear edit text once submitted
                etItem.setText("");

                // 4. Show user that item was successfully uploaded by using toast
                // Toast is a pop up dialogue that briefly appears and then disappears.
                Toast.makeText(getApplicationContext(), "Item Added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // 1. Retrieve updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            // 2. Extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // 3. Update the model at the right position, with the new item text
            items.set(position, itemText);

            // 4. Notify the adapter
            itemsAdapter.notifyItemChanged(position);

            // 5. Persist the changes
            saveItems();

            // Show user toast, indicating they updated the item
            Toast.makeText(getApplicationContext(), "Updated Item Successfully", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    // Returns the file in which we'll store our list of to do items.
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    // 1. This function will load items by reading every line of the data file
    private void loadItems() {
        // Reads all the lines from our data file and populates them into our array list 'items'
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // 2. This function saves items by writing them into the data file
    // Save items should be called whenever we make changes to the list of to do items (Add or Remove)
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}