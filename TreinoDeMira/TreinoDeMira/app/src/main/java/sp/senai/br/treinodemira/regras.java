package sp.senai.br.treinodemira;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class regras extends AppCompatActivity {
    TextView tvRegras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regras);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvRegras = findViewById(R.id.tvRegras);
        //Texto de como jogar
        tvRegras.setText(" O treinamento de mira conhecido também como AIM TRAINING é um jogo que testa seu reflexo;" +
                "\n\n O objetivo é acertar todos os alvos fazendo eles sumirem durante 3 rounds;" +
                "\n\n Cada Round o jogador terá 3 balas, uma para cada alvo. Caso gaste todas as balas sem acertar todos os alvos, o jogador perde;" +
                "\n\n Caso acerte os 9 alvos jogador ganha!!" +
                "\n\n (O jogo reseta ao voltar para tela do game)");
    }

    //Método para voltar para tela de jogo
    public void voltar (View v){
        Intent it = new Intent(regras.this, MainActivity.class);
        startActivity(it);
    }

}