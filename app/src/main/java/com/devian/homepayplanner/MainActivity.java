package com.devian.homepayplanner;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.devian.homepayplanner.databinding.ActivityMainBinding;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("principal", binding.principalEditText.getEditText().getText().toString());
        outState.putString("annualInterest", binding.annualInterestEditText.getEditText().getText().toString());
        outState.putString("loanTerm", binding.loanTermInYearsEditText.getEditText().getText().toString());
        outState.putString("monthlyPayment", binding.monthlyPaymentTextView.getText().toString());
        outState.putString("Loan", binding.principalTextView.getText().toString());
    }

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

            if(savedInstanceState != null){
                binding.principalEditText.getEditText().setText(savedInstanceState.getString("principal"));
                binding.annualInterestEditText.getEditText().setText(savedInstanceState.getString("annualInterest"));
                binding.loanTermInYearsEditText.getEditText().setText(savedInstanceState.getString("loanTerm"));
                binding.monthlyPaymentTextView.setText(savedInstanceState.getString("monthlyPayment"));
                binding.principalTextView.setText(savedInstanceState.getString("Loan"));
            }

            CardView calculateButton = findViewById(R.id.calculate_button);
            calculateButton.setOnClickListener(v -> {

            String principalString = binding.principalEditText.getEditText().getText().toString().trim(); //getting the string value from textInputLayout
            String annualInterestString = binding.annualInterestEditText.getEditText().getText().toString().trim();
            String loanTermString = binding.loanTermInYearsEditText.getEditText().getText().toString().trim();

            boolean isValid = true;

                if (principalString.isEmpty()) {
                    binding.principalEditText.setError("Principal is required");
                    binding.principalEditText.requestFocus();
                    isValid = false;
                    binding.principalTextView.setText("₱ 0.00");

                    binding.principalEditText.getEditText().setOnFocusChangeListener((view, hasFocus) -> {
                        if (hasFocus) {
                            binding.principalEditText.setError(null); // Clear error
                        }
                    });
                }

                if (annualInterestString.isEmpty()) {
                    binding.annualInterestEditText.setError("Annual interest is required");
                    binding.annualInterestEditText.requestFocus();
                    isValid = false;

                    binding.annualInterestEditText.getEditText().setOnFocusChangeListener((view, hasFocus) -> {
                        if (hasFocus) {
                            binding.annualInterestEditText.setError(null); // Clear error
                        }
                    });
                }

                if (loanTermString.isEmpty()) {
                    binding.loanTermInYearsEditText.setError("Loan term is required");
                    binding.loanTermInYearsEditText.requestFocus();
                    isValid = false;

                    binding.loanTermInYearsEditText.getEditText().setOnFocusChangeListener((view, hasFocus) -> {
                        if (hasFocus) {
                            binding.loanTermInYearsEditText.setError(null); // Clear error
                        }
                    });
                }

                if (isValid) {
                    try{
                        double principal =Double.parseDouble(principalString); //converting the string to double
                        double annualInterest = Double.parseDouble(annualInterestString);
                        int loanTerm = Integer.parseInt(loanTermString);

                        Toast.makeText(this, "Calculation\nSuccessful!", Toast.LENGTH_SHORT).show();
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
                        NumberFormat currencyMonthly = NumberFormat.getCurrencyInstance(philippines);
                        String monthlyPaymentString = currencyMonthly.format(monthlyPayment); //passing the monthlyPayment int to string for display

                        NumberFormat currency = NumberFormat.getCurrencyInstance(philippines);
                        currency.setMaximumFractionDigits(0);
                        String formattedValue = currency.format(principal);

                        NumberFormat percentage = NumberFormat.getPercentInstance();
                        percentage.setMinimumFractionDigits(1);
                        String formattedInterest = percentage.format(annualInterest/100);

                        String viewLoanTerm = String.valueOf(loanTerm);

                        DecimalFormat decimalFormat = new DecimalFormat("#,###");

                        //Displaying the Calculated Result passing all the value in textview
                        Log.d("DEBUG", "Formatted Principal: " + formattedValue);
                        binding.principalTextView.setText(formattedValue);
//                        binding.interestRateTextView.setText(formattedInterest);
//                        binding.periodOfYearsTextView.setText(viewLoanTerm);
                        Log.d("DEBUG", "Monthly Payment: " + monthlyPaymentString);
                        binding.monthlyPaymentTextView.setText(String.valueOf(monthlyPaymentString));

                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Input only numberical values", Toast.LENGTH_SHORT).show();
                    }
                }

        });

    }
    private void setInputTextLayoutViewStyle() {
        TextInputLayout textInputLayout = findViewById(R.id.principal_editText);
        textInputLayout.setHintEnabled(false); // Disable the default floating hint

        TextView label = new TextView(this);
        label.setText("Loan Amount");

        Typeface customFont = ResourcesCompat.getFont(this,R.font.helvetica_medium); // NOTE: this code is not working but i will work on these
        label.setTypeface(customFont);


        label.setTextAppearance(this, androidx.appcompat.R.style.TextAppearance_AppCompat_Caption);
        label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // Set text size to 16sp (adjust as needed)
        label.setTypeface(Typeface.DEFAULT_BOLD);

        // Add the new label to the TextInputLayout
        textInputLayout.addView(label,0);// Add at the beginning
    }

}
