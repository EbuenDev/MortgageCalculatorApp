package com.devian.homepayplanner;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devian.homepayplanner.databinding.ActivityMainBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        binding.CalculateButton.setOnClickListener(v -> {
            // get and converting user input to integers
            double principal = Integer.parseInt(binding.principalEditText.getText().toString());
            double annualInterest = Integer.parseInt(binding.annualInterestEditText.getText().toString());
            int loanTerm = Integer.parseInt(binding.loanTermInYearsEditText.getText().toString());
            double monthlyPayment;

            //where going to convert your monthly interest rate to decimal
            double monthlyInterestRate = annualInterest / 12 / 100;
            int numberOfPayments = loanTerm * 12;

            double powerOfNumber = Math.pow(1 + monthlyInterestRate, numberOfPayments); // output is a double
            double roundedPowerOfNumber = Math.round(powerOfNumber*1000)/1000.0; // rounding to 3 decimal places

            //calculating numerator and round to 5 decimal places
            double numerator = monthlyInterestRate * roundedPowerOfNumber;
            double roundedNumerator = Math.round(numerator*100000)/100000.0; // rounding to 5 decimal places

            //calculating denominator and round to 5 decimal places
            double denominator = roundedPowerOfNumber - 1;

            //calculate ng fraction to 6 decimal places
            double Fraction = roundedNumerator / denominator;
            double roundedFraction = Math.round(Fraction*1000000)/1000000.0; // rounding to 6 decimal places

            monthlyPayment = principal * roundedFraction;


            Locale philippines = new Locale("en", "PH");
            NumberFormat currency = NumberFormat.getCurrencyInstance(philippines);
            String monthlyPaymentString = currency.format(monthlyPayment);
            binding.resultEditText.setText(String.valueOf(monthlyPaymentString));
            String viewInterest = String.valueOf(annualInterest);
            String viewLoanTerm = String.valueOf(loanTerm);

            DecimalFormat decimalFormat = new DecimalFormat("#,###");
            String formattedValue = decimalFormat.format(principal); //"₱ "
            binding.principalTextView.setText("\u20B1 " +formattedValue);
            binding.annuaInterestTextView.setText(viewInterest + " %");
            binding.loanTermTextView.setText(viewLoanTerm + " Years");
        });


    }
}
