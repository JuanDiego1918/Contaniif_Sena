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

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.adapter.YoutubeVideoAdapter;
import club.contaniif.contaniff.entidades.VideoVo;

public class VideosActivity extends AppCompatActivity implements Response.Listener<JSONObject>,Response.ErrorListener{

    private static final String TAG = VideosActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    //youtube player fragment
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private ArrayList<VideoVo> youtubeVideoArrayList;

    Dialog dialogoCargando;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    //youtube player to play video when new video selected
    private YouTubePlayer youTubePlayer;
    Bundle miBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        dialogoCargando = new Dialog(this);
        miBundle=this.getIntent().getExtras();
        request = Volley.newRequestQueue(getApplicationContext());
        cargarWebService();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
///////////////////////
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ppal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.action_recargar) {
            finish();
        }
            return super.onOptionsItemSelected(item);
        }
*/

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
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //Horizontal direction recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * populate the recycler view and implement the click event here
     */
    private void populateRecyclerView() {
        final YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(this, youtubeVideoArrayList);
        recyclerView.setAdapter(adapter);

        //set click event
        recyclerView.addOnItemTouchListener(new RecyclerViewOnClickListener(this, new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (youTubePlayerFragment != null && youTubePlayer != null) {
                    //update selected position
                    adapter.setSelectedPosition(position);

                    //load selected video
                    youTubePlayer.cueVideo(youtubeVideoArrayList.get(position).getEnlace());
                    Bundle miBundle=new Bundle();
                    miBundle.putString("link",youtubeVideoArrayList.get(position).getEnlace());

                    Intent miIntent=new Intent(getApplicationContext(), Videos.class);
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
        String url="https://"+getApplicationContext().getString(R.string.ip)+"videos.php?categoria="+miBundle.getString("id");
        jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    private void dialogoCargando() {
        try {
            dialogoCargando.setContentView(R.layout.popup_cargando);
            dialogoCargando.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogoCargando.show();
        }catch (Exception e){
            Log.i("Error " , e.toString());
        }

    }

    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(),"NO se pudo Consultar:"+error.toString(), Toast.LENGTH_LONG).show();
        Log.i("Error",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        youtubeVideoArrayList=new ArrayList<>();
        JSONArray json=response.optJSONArray("videos");
        try {
            JSONObject jsonObject=null;
            for (int i=0;i<json.length();i++){
                VideoVo videoVo=new VideoVo();
                jsonObject=json.getJSONObject(i);
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
                    " "+response, Toast.LENGTH_LONG).show();
        }

        dialogoCargando.dismiss();
    }
}
