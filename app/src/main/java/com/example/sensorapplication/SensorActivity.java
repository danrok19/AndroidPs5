package com.example.sensorapplication;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    SensorAdapter adapter;
    private boolean sensorVisible;
    private static final String KEY_EXTRA_VISIBLE = "KEY_EXTRA_VISIBLE";

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_EXTRA_VISIBLE, sensorVisible);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);
        RecyclerView recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if(adapter == null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.notifyDataSetChanged();
        }

        for (Sensor sensor: sensorList ) {
            Log.v("MainActivity",sensor.getName() + sensor.getVendor() + sensor.getMaximumRange());
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {
        private final List<Sensor> sensorList;
        public SensorAdapter(List<Sensor> sensorList){
            this.sensorList = sensorList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }

    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView sensorNameTextView;
        private ImageView sensorIconImageView;
        Sensor sensor;

        private final LinearLayout SensorView;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnClickListener(this);

            sensorNameTextView = itemView.findViewById(R.id.sensor_item_name);
            sensorIconImageView = itemView.findViewById(R.id.sensor_item_image);
            SensorView = itemView.findViewById(R.id.sensor_view);

        }

        @SuppressLint("ResourceAsColor")
        public void bind(Sensor sensor) {
            this.sensor = sensor;
            sensorNameTextView.setText(sensor.getName());
            sensorIconImageView.setImageResource(R.drawable.ic_sensor);

            if(sensorNameTextView.getText() == "Goldfish Ambient Temperature sensor" || sensorNameTextView.getText() == "Goldfish Pressure sensor")
            {
                SensorView.setBackgroundColor(R.color.purple_200);
            }

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(),SensorDetailsActivity.class);
            intent.putExtra(SensorDetailsActivity.KEY_EXTRA_SENSOR, sensorList.indexOf(sensor));
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sensor_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.sensor);

        if (sensorVisible) {
            menuItem.setTitle(R.string.hide_subtitle);
        } else {
            menuItem.setTitle(R.string.show_subtitle);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sensor) {
            sensorVisible = !sensorVisible;
            this.invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateSubtitle() {
        String subtitle = getString(R.string.sensor_count, sensorList.size());

        if (!sensorVisible) {
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) this;
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);

    }
    public void ChangeToLocation(View view) {
        Log.d(TAG,"to Location!");
        Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
        startActivity(intent);
    }
}