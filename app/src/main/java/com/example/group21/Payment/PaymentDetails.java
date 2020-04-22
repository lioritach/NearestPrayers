package com.example.group21.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.group21.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentDetails extends AppCompatActivity {

    private TextView txtID, txtAmount, txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        txtID = (TextView) findViewById(R.id.textID);
        txtAmount = (TextView) findViewById(R.id.textAmountID);
        txtStatus = (TextView) findViewById(R.id.textStatusID);

        //Get Intent
        Intent intent = getIntent();

        try{
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            showDetails(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void showDetails(JSONObject response, String paymentAmount){
        try {
            txtID.setText(response.getString("id"));
            txtAmount.setText(response.getString(String.format("$%s", paymentAmount)));
            txtStatus.setText(response.getString("status"));
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
