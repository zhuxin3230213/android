package cn.gmuni.demo.myapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static cn.gmuni.demo.myapplication.MainActivity.EXTRA_MESSAGE;

public class DisplayMessageActivity extends AppCompatActivity implements View.OnClickListener {


    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        String message = intent.getStringExtra(EXTRA_MESSAGE);

        TextView textView1= findViewById(R.id.textView2);
        textView1.append(message);
        button = findViewById(R.id.button3);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
