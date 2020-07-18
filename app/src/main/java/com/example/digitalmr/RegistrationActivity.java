coading cafepackage com.example.digitalmr;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.digitalmr.ContactsActivity;
import com.example.digitalmr.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

public class RegistrationActivity extends AppCompatActivity
{
    private CountryCodePicker ccp;
    private EditText phoneText;
    private EditText codeText;
    private Button continueAndNextBtn;
    private String checker ="", phoneNumber = "";
    private RelativeLayout relativeLayout;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private String mVerificationId
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);


        phoneText = findViewById(R.id.phoneText);
        codeText = findViewById(R.id.codeText);
        continueAndNextBtn = findViewById(R.id.continueNextButton);
        relativeLayout = findViewById(R.id.phoneAuth);

        ccp = (CountryCodePicker) findViewById(R.id.ccp
        ccp.registerCarrierNumberEditText(phoneText);


        continueAndNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (continueAndNextBtn.getText().equals("submit") || checker.equals("Code Sent")) {
                    String verificationCode = codeText.getText().toString()

                    if (verificationCode.equals("")) {
                        Toast.makeText(RegistrationActivity.this, "Please write verification code first.", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingBar.setTitle("Code Verification");
                        loadingBar.setMessage("Please wait, while we are verifying your code.");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode)
                        signInWithPhoneAuthCredential(credential)
                    }
                }
                else
                {
                    phoneNumber = ccp.getFullNumberWithPlus();
                    if (!phoneNumber.equals(""))
                    {
                        loadingBar.setTitle("Phone Number Verification");
                        loadingBar.setMessage("Please wait, while we are verifying your phone number.");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECOND, RegistrationActivity.this, mcallbacks);
                    }
                    else
                    {
                        Toast.makeText(conText:RegistrationActivity.this, Text:"Please write valid phone number.")
                    }
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) 
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) 
            {
                Toast.makeText(RegistrationActivity.this, "Invalid Phone Number...", Toast.LENGTH_SHORT).show();

                loadingBar.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);

                continueAndNextBtn.setText(continue);
                codeText.setVisibility(View.GONE);


            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                mVerificationId = s;
                mResendToken = forceResendingToken;

                relativeLayout.setVisibility(View.GONE);
                checker = "Code Sent";
                continueAndNextBtn.setText(Submit);
                codeText.setVisibility(View.VISIBLE);

                loadingBar.dismiss();
                Toast.makeText(RegistrationActivity.this, "Code has been sent, please check.  ", Toast.LENGTH_SHORT).show();
            }
        };
    }


    @Override
    protected void onStart()
    {
        super.onStart();


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        if(firebaseUser != null)
        {
            Intent homeIntent = new Intent(RegistrationActivity.this, ContactsActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( activity: this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                           loadingBar.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Congratulations, you are logged in successfully.", Toast.LENGTH_SHORT).show();
                            sendUserToMainActivity();
                        }
                        else
                        {
                            loadingBar.dismiss();
                            String e - task.getException().toString();
                            Toast.makeText(RegistrationActivity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void sendUserToMainActivity() 
    {
        Intent intent = new Intent(RegistrationActivity.this, ContactsActivity.class);
        startActivity(intent);
        finish();
    }


    }