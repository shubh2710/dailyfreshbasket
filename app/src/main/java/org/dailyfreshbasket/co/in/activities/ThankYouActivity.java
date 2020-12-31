package org.dailyfreshbasket.co.in.activities;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.dailyfreshbasket.co.in.R;
public class ThankYouActivity extends AppCompatActivity {

    private TextView id,detail;
    private Button home;
    private String oid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);
        id=(TextView) findViewById(R.id.tv_confirmation_order_Id);
        detail=(TextView) findViewById(R.id.tv_confirmation_order_detail);
        home=(Button) findViewById(R.id.b_confirmation_home);
        Bundle details=getIntent().getBundleExtra("extra");
        try{

            oid=details.getString("oid");
        }catch (Exception e){

        }
        id.setText("Order ID: "+oid);
        detail.setText("Order placed Successfully");
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home=new Intent(ThankYouActivity.this,Home.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);
                finish();
            }
        });
    }
}