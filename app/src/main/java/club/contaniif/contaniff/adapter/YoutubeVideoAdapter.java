package club.contaniif.contaniff.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.VideoVo;
import club.contaniif.contaniff.videos.Constants;
import club.contaniif.contaniff.videos.YoutubeViewHolder;

/**
 * Created by sonu on 10/11/17.
 */

public class YoutubeVideoAdapter extends RecyclerView.Adapter<YoutubeViewHolder> {
    private static final String TAG = YoutubeVideoAdapter.class.getSimpleName();
    private final ArrayList<VideoVo> listaVideo;


    public YoutubeVideoAdapter(ArrayList<VideoVo> youtubeVideoModelArrayList) {
        this.listaVideo = youtubeVideoModelArrayList;
    }

    @NonNull
    @Override
    public YoutubeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.youtube_video_custom_layout, parent, false);
        return new YoutubeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeViewHolder holder, final int position) {

        //if selected position is equal to that mean view is selected so change the cardview color

        /*  initialize the thumbnail image view , we need to pass Developer Key */
        holder.tituloVideo.setText(listaVideo.get(position).getTitulo());
        holder.descripcionVideo.setText(listaVideo.get(position).getDescripcion());
        holder.videoThumbnailImageView.initialize(Constants.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                //when initialization is sucess, set the video id to thumbnail to load
                youTubeThumbnailLoader.setVideo(listaVideo.get(position).getEnlace());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        //when thumbnail loaded successfully release the thumbnail loader as we are showing thumbnail in adapter
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {
                        //print or show error when thumbnail load failed
                        Log.e(TAG, "Youtube Thumbnail Error");
                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //print or show error when initialization failed
                Log.e(TAG, "Youtube Initialization Failure");

            }
        });

    }

    @Override
    public int getItemCount() {
        return listaVideo != null ? listaVideo.size() : 0;
    }

    /**
     * method the change the selected position when item clicked
     */
    public void setSelectedPosition() {
        //position to check which position is selected
        //when item selected notify the adapter
        notifyDataSetChanged();
    }
}
