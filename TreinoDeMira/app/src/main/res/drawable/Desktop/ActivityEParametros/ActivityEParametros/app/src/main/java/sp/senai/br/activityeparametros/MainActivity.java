package sp.senai.br.activityeparametros;

import android.app.ComponentCaller;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText etAlgo, etAno;
    TextView tvResposta;

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
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        etAlgo = findViewById(R.id.etAlgo);
        etAno = findViewById(R.id.etAno);
        tvResposta = findViewById(R.id.tvResposta);
    }

    public void act1(View a) {
        Intent it = new Intent(MainActivity.this, PrimeiraAcitivity.class);
        startActivity(it);
    }

    public void act2(View a) {
        Intent it = new Intent(MainActivity.this, SegundaActivity.class);
        it.putExtra("Algo", etAlgo.getText().toString());
        it.putExtra("número", 58);
        startActivity(it);
        etAlgo.setText(null);
    }

    public void act3(View a) {
        Intent it = new Intent(MainActivity.this, TerceiraActivity.class);
        it.putExtra("Ano", etAno.getText().toString());
        //Como será necessário receber uma resposta, utilizamos
        //o StartAticivityForResult passando como parâmetro a
        //intenção e um código numérico que será utilizado para
        //identificar na outra activity.
        startActivityForResult(it, 1);
    }
    //Para receber o parâmetro de voltam, precisamos do método
    //onActivityResult

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data, @NonNull ComponentCaller caller) {
        if (requestCode==1){
            String sIdade = data.getExtras().getString("idade");
            tvResposta.setText(sIdade + " anos");
            etAno.setText(null);
        }
    }
}