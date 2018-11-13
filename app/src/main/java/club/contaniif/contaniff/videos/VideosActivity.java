package club.contaniif.contaniff.videos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.YoutubeVideoAdapter;
import club.contaniif.contaniff.entidades.VideoVo;

public class VideosActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    private static final String TAG = VideosActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    //youtube player fragment
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private ArrayList<VideoVo> youtubeVideoArrayList;

    private Dialog dialogoCargando;
    private RequestQueue request;
    //youtube player to play video when new video selected
    private YouTubePlayer youTubePlayer;
    private Bundle miBundle;
    private ImageView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        dialogoCargando = new Dialog(this);
        miBundle = this.getIntent().getExtras();
        request = Volley.newRequestQueue(getApplicationContext());
        recyclerView = findViewById(R.id.recycler_view);
        error = findViewById(R.id.errorVideos);
        cargarWebService();
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);
///////////////////////
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return false;
    }

    /**
     * initialize youtube player via Fragment and get instance of YoutubePlayer
     */
    private void initializeYoutubePlayer() {

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager()
                .findFragmentById(R.id.youtube_player_fragment);

        if (youTubePlayerFragment == null)
            return;

        youTubePlayerFragment.initialize(Constants.DEVELOPER_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                                boolean wasRestored) {
                if (!wasRestored) {
                    youTubePlayer = player;

                    //set the player style default
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);

                    //cue the 1st video by default
                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(0).getEnlace());
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                //print or show error if initialization failed
                Log.e(TAG, "Youtube Player View initialization failed");
            }
        });
    }

    /**
     * setup the recycler view here
     */
    private void setUpRecyclerView() {

        recyclerView.setHasFixedSize(true);

        //Horizontal direction recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recycler view and implement the click event here
     */
    private void populateRecyclerView() {
        final YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(youtubeVideoArrayList);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                if (youTubePlayerFragment != null && youTubePlayer != null) {
                    //update selected position
                    adapter.setSelectedPosition();

                    //load selected video
                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(position).getEnlace());
                    Bundle miBundle = new Bundle();
                    miBundle.putString("link", youtubeVideoArrayList.get(position).getEnlace());

                    Intent miIntent = new Intent(getApplicationContext(), Videos.class);
                    miIntent.putExtras(miBundle);
                    startActivity(miIntent);
                    //getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,miFragment).commit();
                }

            }
        }));
    }


    /**
     * method to generate dummy array list of videos
     */
    private void cargarWebService() {
        dialogoCargando();
        youtubeVideoArrayList = new ArrayList<>();
        String url = "https://" + getApplicationContext().getString(R.string.ip) + "videos.php?categoria=" + miBundle.getString("id");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        request.add(jsonObjectRequest);
    }

    private void dialogoCargando() {
        try {
            dialogoCargando.setContentView(R.layout.popup_cargando);
            Objects.requireNonNull(dialogoCargando.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogoCargando.show();
        } catch (Exception e) {
            Log.i("Error ", e.toString());
        }

    }

    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplication(), "Estamos realizando ajustes, por favor verifique mas tarde", Toast.LENGTH_LONG).show();
        recyclerView.setVisibility(View.INVISIBLE);
        this.error.setVisibility(View.VISIBLE);
        dialogoCargando.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        youtubeVideoArrayList = new ArrayList<>();
        JSONArray json = response.optJSONArray("videos");
        try {
            JSONObject jsonObject;
            for (int i = 0; i < json.length(); i++) {
                VideoVo videoVo = new VideoVo();
                jsonObject = json.getJSONObject(i);
                videoVo.setTitulo(jsonObject.optString("titulo"));
                videoVo.setDescripcion(jsonObject.optString("descripcion"));
                videoVo.setEnlace(jsonObject.getString("video"));
                youtubeVideoArrayList.add(videoVo);
            }
            initializeYoutubePlayer();
            setUpRecyclerView();
            populateRecyclerView();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No se ha podido establecer conexión con el servidor" +
                    " " + response, Toast.LENGTH_LONG).show();
        }

        dialogoCargando.dismiss();
    }
}
