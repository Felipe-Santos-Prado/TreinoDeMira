package sp.senai.br.activityeparametros;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class TerceiraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_terceira);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void calcular (View c) {
        Intent anoNasc = getIntent();
        Calendar cal = Calendar.getInstance();
        int ianoAtual = cal.get(Calendar.YEAR);
        int ianoNasc = Integer.parseInt(anoNasc.getStringExtra("Ano"));
        int iIdade = ianoAtual - ianoNasc;

        Intent it = new Intent(TerceiraActivity. this, MainActivity.class);

        it.putExtra("idade", String.valueOf(iIdade));
        setResult(1, it);
        finish();
    }
}