package com.devian.homepayplanner;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devian.homepayplanner.databinding.ActivityMainBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

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

        CardView calculateButton = findViewById(R.id.calculate_button);


        calculateButton.setOnClickListener(v -> {
            Toast.makeText(this, "Calculating...", Toast.LENGTH_SHORT).show();
            try{
                String principalString = binding.principalEditText.getEditText().getText().toString().trim(); //getting the string value from textInputLayout
                double principal =Double.parseDouble(principalString); //converting the string to double

                String annualInterestString = binding.annualInterestEditText.getEditText().getText().toString().trim();
                double annualInterest = Double.parseDouble(annualInterestString);

                String loanTermString = binding.loanTermInYearsEditText.getEditText().getText().toString().trim();
                int loanTerm = Integer.parseInt(loanTermString);

                if(principalString.isEmpty() || annualInterestString.isEmpty() || loanTermString.isEmpty()){
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                    double monthlyPayment;
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

                    //getting the default locale for Philippines
                    Locale philippines = new Locale("en", "PH");
                    NumberFormat currency = NumberFormat.getCurrencyInstance(philippines);
                    NumberFormat currencyMonthly = NumberFormat.getCurrencyInstance(philippines);
                    String monthlyPaymentString = currencyMonthly.format(monthlyPayment); //passing the monthlyPayment int to string for display
                    currency.setMaximumFractionDigits(0);
                    String formattedValue = currency.format(principal); //"₱ "
                    String viewInterest = String.valueOf(annualInterest);
                    String viewLoanTerm = String.valueOf(loanTerm);

                    //Fomatting the Principal in every 3 digits to have commas

                    DecimalFormat decimalFormat = new DecimalFormat("#,###");

                    //Displaying the Calculated Result passing all the value in textview
                    Log.d("DEBUG", "Formatted Principal: " + formattedValue);
                    binding.principalTextView.setText(formattedValue);
                    binding.interestRateTextView.setText(viewInterest + " %");
                    binding.periodOfYearsTextView.setText(viewLoanTerm + " Years");
                    Log.d("DEBUG", "Monthly Payment: " + monthlyPaymentString);
                    binding.monthlyPaymentTextView.setText(String.valueOf(monthlyPaymentString));


            } catch (NumberFormatException e) {
                Toast.makeText(this, "Input only numberical values", Toast.LENGTH_SHORT).show();
            }

        });

    }

}
