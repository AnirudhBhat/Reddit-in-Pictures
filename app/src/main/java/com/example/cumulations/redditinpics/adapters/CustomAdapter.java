package com.example.cumulations.redditinpics.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cumulations.redditinpics.R;
import com.example.cumulations.redditinpics.model.Children;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Random;

/**
 * Created by abhat on 17/8/16.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements View.OnClickListener {

    static List<Children> posts;

    Context context;

    private File outputFile;
    private boolean result;

    private final static String MESSAGE = "File saved successfully!";
    private AnimationSet animation;
    private ImageView imageView;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView text;
        Button save;
        Button share;

        public MyViewHolder(View itemView) {

            super(itemView);
            this.image = (ImageView)itemView.findViewById(R.id.image);
            this.text = (TextView)itemView.findViewById(R.id.text);
            this.save = (Button)itemView.findViewById(R.id.save);
            this.share = (Button)itemView.findViewById(R.id.share);
        }
    }

    public CustomAdapter(List<Children> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cards_layout, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.text;
        imageView = holder.image;
        Button save = holder.save;
        Button share = holder.share;

        save.setTag(listPosition);
        share.setTag(listPosition);

        Picasso.with(context)
                .load(posts.get(listPosition).getChildData().getPreview().getImages().get(0).getSource().getUrl())
                //.placeholder(R.mipmap.ic_launcher)
                //.fit()
                .resize(Integer.parseInt(posts.get(listPosition).getChildData().getPreview().getImages().get(0).getSource().getWidth()), Integer.parseInt(posts.get(listPosition).getChildData().getPreview().getImages().get(0).getSource().getHeight()))
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {

                    }
                });
        

        save.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.save:
                new save().execute((Integer) v.getTag());
                break;
            case R.id.share:
                new share().execute((Integer)v.getTag());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class save extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            //progressBar.setVisibility(View.VISIBLE);
            //progressBar.setIndeterminate(true);
        }


            @Override
        protected Void doInBackground(Integer... params) {
            saveImage(MESSAGE, String.valueOf(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //progressBar.setVisibility(View.GONE);
            if (result) {
                Toast.makeText(context, "Image saved successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class share extends AsyncTask<Integer, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressBar.setVisibility(View.VISIBLE);
            //progressBar.setIndeterminate(true);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            shareImage(String.valueOf(params[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressBar.setVisibility(View.GONE);
            try {
                //outputFile.delete();
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void shareImage(String position) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        saveImage("dummy", position);
        Uri uri = Uri.fromFile(outputFile);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.startActivity(Intent.createChooser(share, "share image"));
    }


    private void saveImage(String... message) {
        String path = Environment.getExternalStorageDirectory() + "/redditpics/";
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        try {
            URL url = new URL(posts.get(Integer.valueOf(message[1])).getChildData().getPreview().getImages().get(0).getSource().getUrl());
            InputStream in = new BufferedInputStream(url.openStream());
            outputFile = new File(path, getRandomNumber().toString() + ".jpeg");
            OutputStream output = new FileOutputStream(outputFile);
            byte[] data = new byte[5000];
            long total = 0;
            int count = 0;
            while ((count = in.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            in.close();
            result = message.length > 0 ? true : false;
            Log.d("PICTURE", "INSIDE FOLDER " + path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public Integer getRandomNumber() {
        Random generator = new Random(System.currentTimeMillis());
        return generator.nextInt();
    }

}
