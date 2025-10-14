package sp.senai.br.treinodemira;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageView ivAlvo1, ivAlvo2, ivAlvo3, ivVida1, ivVida2, ivVida3, ivArma;
    TextView tvComecar, tvAcertos, tvCliques, tvGanhou;
    float fLayX, fLayY, fInicio, fFinal;
    int iAcertos, iCliques, round, iVida = 3;
    AnimationDrawable adAtirando;
    MediaPlayer mpTiro;
    ConstraintLayout clTela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //---------------Imagens---------------//
        ivAlvo1 = findViewById(R.id.ivAlvo1);
        ivAlvo2 = findViewById(R.id.ivAlvo2);
        ivAlvo3 = findViewById(R.id.ivAlvo3);
        ivVida1 = findViewById(R.id.ivVida1);
        ivVida2 = findViewById(R.id.ivVida2);
        ivVida3 = findViewById(R.id.ivVida3);
        ivArma = findViewById(R.id.ivArma);

        //---------------Botão---------------//
        tvComecar = findViewById(R.id.tvComecar);

        //---------------Caixas de Texto---------------//
        tvAcertos = findViewById(R.id.tvAcertos);
        tvCliques = findViewById(R.id.tvCliques);
        tvGanhou = findViewById(R.id.tvGanhou);

        //---------------Reprodução de som---------------//
        mpTiro = MediaPlayer.create(this, R.raw.tiro);
        mpTiro.setVolume(0.5f, 0.5f); // Define o volume para metade do total

        //---------------Tela de Jogo---------------//
        clTela = findViewById(R.id.clTela);

        //---------------Fazer Alvos e tvGanhou sumirem ao Criar---------------//
        tvGanhou.setVisibility(View.INVISIBLE);
        ivAlvo1.setVisibility(View.INVISIBLE);
        ivAlvo2.setVisibility(View.INVISIBLE);
        ivAlvo3.setVisibility(View.INVISIBLE);

        ViewTreeObserver obs = clTela.getViewTreeObserver();//Armazena qual é o tamanho da tela de jogo
        obs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fLayX = clTela.getWidth();//
                fLayY = clTela.getHeight();
            }
        });

        //Contabiliza todos os cliques dentro da área de jogo clTela;
        clTela.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mpTiro.start();
                    iCliques += 1;

                    ivArma.setImageResource(0);  // Limpa imagem da arma parada
                    ivArma.setBackgroundResource(R.drawable.atirando); // Define animação no clique
                    adAtirando = (AnimationDrawable) ivArma.getBackground();
                    adAtirando.stop();//Termina a animação de atirando
                    adAtirando.start();//Inicia a animação de atirando

                    iVida -= 1;//Subtrai 1 vida
                    atualizarvida();//Chama o método para atualizar vida

                    tvCliques.setText("CLIQUES: " + iCliques);//atualiza o tvCliques
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    ivArma.setBackgroundResource(0); // Remove o backround da imagem
                    ivArma.setImageResource(R.drawable.arma1);// Volta para a imagem da arma parada
                }
                return true;
            }
        });
    }

    //Caso clique no botão "COMO JOGAR" move o usuario para acitivity regras.
    public void regras(View r) {
        Intent it = new Intent(MainActivity.this, regras.class);
        startActivity(it);
        reiniciar(r);
    }

    //Caso clique no botão "REINICIAR JOGO" zera todas as variáveis;
    public void reiniciar(View r) {
        tvComecar.setText("COMEÇAR ROUND 1");
        tvComecar.setClickable(true);
        tvComecar.setAlpha(1f);

        iAcertos = 0;
        tvAcertos.setText("ACERTOS: 0");
        tvCliques.setText("CLIQUES: 0");
        tvGanhou.setVisibility(View.INVISIBLE);
        iVida = 3;
        iCliques = 0;

        ivVida1.setVisibility(View.VISIBLE);
        ivVida2.setVisibility(View.VISIBLE);
        ivVida3.setVisibility(View.VISIBLE);
        ivAlvo1.setVisibility(View.INVISIBLE);
        ivAlvo2.setVisibility(View.INVISIBLE);
        ivAlvo3.setVisibility(View.INVISIBLE);
    }

    //Método do botão começar
    public void comecar(View c) {
        //Verifica se é o round 1
        if (tvComecar.getText().toString().equals("COMEÇAR ROUND 1")) {
            tvComecar.setClickable(false);
            tvComecar.setAlpha(0.5f);  // 50% de opacidade
            round = 1;
            iniciar();
            round();
        //Verifica se é o round 2
        } else if (tvComecar.getText().toString().equals("COMEÇAR ROUND 2")) {
            iVida = 3;
            aparacerbalas();
            tvComecar.setClickable(false);
            tvComecar.setAlpha(0.5f);  // 50% de opacidade
            round = 2;
            iniciar();
            round();
        //Se não é o round 1 nem o 2 só pode ser o round 3
        } else {
            iVida = 3;
            aparacerbalas();
            tvComecar.setClickable(false);

            tvComecar.setAlpha(0.5f);  // 50% de opacidade
            round = 3;
            iniciar();
            round();
        }
    }
    //Método que inicia o posicionamento dos alvos no campo de jogo
    public void iniciar() {
        //Torna os alvos visíveis
        ivAlvo1.setVisibility(View.VISIBLE);
        ivAlvo2.setVisibility(View.VISIBLE);
        ivAlvo3.setVisibility(View.VISIBLE);

        //Aleatorizar onde os alvos iram nascer. Usando math random subtraindo o tamanho do layout pela tamanho do alvo para que não nasça fora da tela
        float posicao1X = (float) (Math.random() * (fLayX - ivAlvo1.getWidth()));
        float posicao1Y = (float) (Math.random() * (fLayY - ivAlvo1.getHeight()));

        float posicao2X = (float) (Math.random() * (fLayX - ivAlvo2.getWidth()));
        float posicao2Y = (float) (Math.random() * (fLayY - ivAlvo2.getHeight()));

        float posicao3X = (float) (Math.random() * (fLayX - ivAlvo3.getWidth()));
        float posicao3Y = (float) (Math.random() * (fLayY - ivAlvo3.getHeight()));

        // Move a imagem para a posição aleatória
        ivAlvo1.setX(posicao1X);
        ivAlvo1.setY(posicao1Y);

        ivAlvo2.setX(posicao2X);
        ivAlvo2.setY(posicao2Y);

        ivAlvo3.setX(posicao3X);
        ivAlvo3.setY(posicao3Y);
        girar();
        //Verifica em qual round está pois existem animações diferente para cada round
        if (round == 1) {
            animarCrescimentoAlvo1();
        } else if (round == 2) {
            animarCrescimentoAlvo2();
        } else {
            animarCrescimentoAlvo3();
        }
    }

    //Método de jogo
    public void round() {
        //Verifica se o alvo1 foi clicado
        ivAlvo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAlvo1.setVisibility(View.INVISIBLE);//torna o alvo invisível
                //soma e subtrai 1
                iAcertos += 1;
                iCliques += 1;
                iVida -= 1;
                animarArma();
                atualizarvida();
                verificarAcertos();

                tvCliques.setText("CLIQUES: " + iCliques);//Atualiza o tvCliques
                tvAcertos.setText("ACERTOS: " + iAcertos);//Atualiza  o tvAcertos
            }
        });

        //Verifica se o alvo2 foi clicado
        ivAlvo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAlvo2.setVisibility(View.INVISIBLE);//torna o alvo invisível
                //soma e subtrai 1
                iAcertos += 1;
                iCliques += 1;
                iVida -= 1;
                animarArma();
                atualizarvida();
                verificarAcertos();

                tvCliques.setText("CLIQUES: " + iCliques);//Atualiza o tvCliques
                tvAcertos.setText("ACERTOS: " + iAcertos);//Atualiza  o tvAcertos
            }
        });

        //Verifica se o alvo3 foi clicado
        ivAlvo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivAlvo3.setVisibility(View.INVISIBLE);//torna o alvo invisível
                //soma e subtrai 1
                iAcertos += 1;
                iCliques += 1;
                iVida -= 1;
                animarArma();
                atualizarvida();
                verificarAcertos();

                tvCliques.setText("CLIQUES: " + iCliques);//Atualiza o tvCliques
                tvAcertos.setText("ACERTOS: " + iAcertos);//Atualiza  o tvAcertos
            }
        });
    }

    //Método para verificar os acertos
    public void verificarAcertos() {
        //Se todos os alvos ficaram invisíveis, acertos é igual a 3 e a ivida é igual a zero, então
        if (ivAlvo1.getVisibility() == View.INVISIBLE && ivAlvo2.getVisibility() == View.INVISIBLE && ivAlvo3.getVisibility() == View.INVISIBLE  && iAcertos == 3 && iVida == 0){
            tvComecar.setText("COMEÇAR ROUND 2");//Muda o texto do botão começar
            tvComecar.setClickable(true);//Volta a ser possível clicar no botão
            tvComecar.setAlpha(1f);//voltar opacidade normal do botão

        //Se todos os alvos ficaram invisíveis, acertos é igual a 6 e a ivida é igual a zero, então
        } else if (ivAlvo1.getVisibility() == View.INVISIBLE && ivAlvo2.getVisibility() == View.INVISIBLE && ivAlvo3.getVisibility() == View.INVISIBLE && iAcertos == 6 && iVida == 0) {
            tvComecar.setText("COMEÇAR ROUND 3");//Muda o texto do botão começar
            tvComecar.setClickable(true);//Volta a ser possível clicar no botão
            tvComecar.setAlpha(1f);//voltar opacidade normal do botão
        }
        // Verifica vitória após o final do jogo
        if (iAcertos >= 9 && iVida == 0) {
            tvGanhou.setVisibility(View.VISIBLE);//Exibe a tvGanhou de vencedor
            tvGanhou.setText("VOCÊ GANHOU!!");//Atualiza o texto da tvGanhou
        }
    }

    //Método para atualizar a vida
    public void atualizarvida() {
        if (iVida == 2) {//Verifica se iVida é igual a 2 e Torna a vida 1 invisível
            ivVida1.setVisibility(View.INVISIBLE);
        } else if (iVida == 1) {//Verifica se iVida é igual a 1 Torna a vida 2 invisível
            ivVida2.setVisibility(View.INVISIBLE);
        } else if (iVida == 0 ) {//Verifica se iVida é igual a 0 Torna a vida 3 invisível
            ivVida3.setVisibility(View.INVISIBLE);
        }else if (iVida < 0){//Verifica se iVida é menor que 0 e não possibilita o botão começar ser clicado
            tvComecar.setClickable(false);
            tvComecar.setAlpha(0.5f);

            tvGanhou.setVisibility(View.VISIBLE);
            tvGanhou.setText("VOCÊ PERDEU!!");//Atualiza o texto da tvGanhou
        }
    }

    //Método apenas para fazer a balas voltarem a ser visíveis
    public void aparacerbalas (){
        ivVida1.setVisibility(View.VISIBLE);
        ivVida2.setVisibility(View.VISIBLE);
        ivVida3.setVisibility(View.VISIBLE);
    }

    //--------------Animações--------------//
    //Método para girar os alvos
    public void girar() {
        fFinal = 3600;
        fInicio = 0;

        int iDuracao = 10000;  // 10 segundos

        ObjectAnimator oaAlvo1;
        oaAlvo1 = ObjectAnimator.ofFloat(ivAlvo1, View.ROTATION, fInicio, fFinal);
        oaAlvo1.setDuration(iDuracao);
        oaAlvo1.setRepeatCount(ObjectAnimator.INFINITE);
        oaAlvo1.start();

        ObjectAnimator oaAlvo2;
        oaAlvo2 = ObjectAnimator.ofFloat(ivAlvo2, View.ROTATION, fInicio, fFinal);
        oaAlvo2.setDuration(iDuracao);
        oaAlvo2.setRepeatCount(ObjectAnimator.INFINITE);
        oaAlvo2.start();

        ObjectAnimator oaAlvo3;
        oaAlvo3 = ObjectAnimator.ofFloat(ivAlvo3, View.ROTATION, fInicio, fFinal);
        oaAlvo3.setDuration(iDuracao);
        oaAlvo3.setRepeatCount(ObjectAnimator.INFINITE);
        oaAlvo3.start();
    }

    //--------------O que muda de um Crescimento para o outro é a velocidade da animação e como ela escala--------------//
    public void animarCrescimentoAlvo1() {
        ObjectAnimator oaAumentarX1 = ObjectAnimator.ofFloat(ivAlvo1, View.SCALE_X, 1.0f, 0.2f, 0.1f);
        ObjectAnimator oaAumentarY1 = ObjectAnimator.ofFloat(ivAlvo1, View.SCALE_Y, 1.0f, 0.2f, 0.1f);

        oaAumentarX1.setDuration(3000);
        oaAumentarY1.setDuration(3000);

        oaAumentarX1.start();
        oaAumentarY1.start();

        ObjectAnimator oaAumentarX2 = ObjectAnimator.ofFloat(ivAlvo2, View.SCALE_X, 0.5f, 0.2f, 0.1f);
        ObjectAnimator oaAumentarY2 = ObjectAnimator.ofFloat(ivAlvo2, View.SCALE_Y, 0.5f, 0.2f, 0.1f);

        oaAumentarX2.setDuration(5000);
        oaAumentarY2.setDuration(5000);

        oaAumentarX2.start();
        oaAumentarY2.start();

        ObjectAnimator oaAumentarX3 = ObjectAnimator.ofFloat(ivAlvo3, View.SCALE_X, 0.2f, 1.0f, 0.1f);
        ObjectAnimator oaAumentarY3 = ObjectAnimator.ofFloat(ivAlvo3, View.SCALE_Y, 0.2f, 1.0f, 0.1f);

        oaAumentarX3.setDuration(6000);
        oaAumentarY3.setDuration(6000);

        oaAumentarX3.start();
        oaAumentarY3.start();
    }

    public void animarCrescimentoAlvo2() {
        ObjectAnimator oaAumentarX1 = ObjectAnimator.ofFloat(ivAlvo1, View.SCALE_X, 0.2f, 1.0f, 0.1f);
        ObjectAnimator oaAumentarY1 = ObjectAnimator.ofFloat(ivAlvo1, View.SCALE_Y, 0.2f, 1.0f, 0.1f);

        oaAumentarX1.setDuration(6000);
        oaAumentarY1.setDuration(6000);

        oaAumentarX1.start();
        oaAumentarY1.start();

        ObjectAnimator oaAumentarX2 = ObjectAnimator.ofFloat(ivAlvo2, View.SCALE_X, 0.5f, 0.2f, 0.1f);
        ObjectAnimator oaAumentarY2 = ObjectAnimator.ofFloat(ivAlvo2, View.SCALE_Y, 0.5f, 0.2f, 0.1f);

        oaAumentarX2.setDuration(5000);
        oaAumentarY2.setDuration(5000);

        oaAumentarX2.start();
        oaAumentarY2.start();

        ObjectAnimator oaAumentarX3 = ObjectAnimator.ofFloat(ivAlvo3, View.SCALE_X, 1.0f, 0.2f, 0.1f);
        ObjectAnimator oaAumentarY3 = ObjectAnimator.ofFloat(ivAlvo3, View.SCALE_Y, 1.0f, 0.2f, 0.1f);

        oaAumentarX3.setDuration(4000);
        oaAumentarY3.setDuration(4000);

        oaAumentarX3.start();
        oaAumentarY3.start();
    }

    public void animarCrescimentoAlvo3() {
        ObjectAnimator oaAumentarX1 = ObjectAnimator.ofFloat(ivAlvo1, View.SCALE_X, 0.2f, 1.0f, 0.1f);
        ObjectAnimator oaAumentarY1 = ObjectAnimator.ofFloat(ivAlvo1, View.SCALE_Y, 0.2f, 1.0f, 0.1f);

        oaAumentarX1.setDuration(3000);
        oaAumentarY1.setDuration(3000);

        oaAumentarX1.start();
        oaAumentarY1.start();

        ObjectAnimator oaAumentarX2 = ObjectAnimator.ofFloat(ivAlvo2, View.SCALE_X, 0.2f, 1.0f, 0.1f);
        ObjectAnimator oaAumentarY2 = ObjectAnimator.ofFloat(ivAlvo2, View.SCALE_Y, 0.2f, 1.0f, 0.1f);

        oaAumentarX2.setDuration(2000);
        oaAumentarY2.setDuration(2000);

        oaAumentarX2.start();
        oaAumentarY2.start();

        ObjectAnimator oaAumentarX3 = ObjectAnimator.ofFloat(ivAlvo3, View.SCALE_X, 1.0f, 0.2f, 0.1f);
        ObjectAnimator oaAumentarY3 = ObjectAnimator.ofFloat(ivAlvo3, View.SCALE_Y, 1.0f, 0.2f, 0.1f);

        oaAumentarX3.setDuration(2500);
        oaAumentarY3.setDuration(2500);

        oaAumentarX3.start();
        oaAumentarY3.start();
    }
    public void animarArma() {
        mpTiro.start(); // Inicia o som do tiro

        ivArma.setImageResource(0);  // Remove imagem parada
        ivArma.setBackgroundResource(R.drawable.atirando); // Define animação
        adAtirando = (AnimationDrawable) ivArma.getBackground();
        adAtirando.stop();
        adAtirando.start();

        new android.os.Handler().postDelayed(() -> {
            ivArma.setBackgroundResource(0);
            ivArma.setImageResource(R.drawable.arma1);
        }, 200);
    }
}