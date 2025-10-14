package sp.senai.br.activityeparametros;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SegundaActivity extends AppCompatActivity {

    TextView tvAlgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //Remove a bara de título
        //getSupportActionBar().hide();
        //Remove a barra de ferramentas
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_segunda);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvAlgo = findViewById(R.id.tvAlgo);
        //Pega o parâmetro que está vindo pela Intent
        Intent valores = getIntent();
        int iNum = valores.getIntExtra("número", 8);
        String sAlgo = valores.getStringExtra("Algo");

        tvAlgo.setText(sAlgo + " - Número: "+iNum);
    }
}