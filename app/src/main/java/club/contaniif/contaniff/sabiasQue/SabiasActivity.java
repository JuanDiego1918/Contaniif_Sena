package club.contaniif.contaniff.sabiasQue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import club.contaniif.contaniff.R;

public class SabiasActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sabias);

        recyclerView=findViewById(R.id.recyclerSabias);


    }
}
