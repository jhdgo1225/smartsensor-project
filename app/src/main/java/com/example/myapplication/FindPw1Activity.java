package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.network.EmailAuthRequestTask;
import com.example.myapplication.utils.InputValidator;
import com.example.myapplication.utils.SharedPreferencesUtil;
import com.google.android.material.textfield.TextInputEditText;

public class FindPw1Activity extends AppCompatActivity {

    private TextInputEditText editTextUsernameFindPw;
    private Button buttonFindPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findpw1);

        editTextUsernameFindPw = findViewById(R.id.editTextUseremailFindPassword);
        buttonFindPassword = findViewById(R.id.buttonEmailRequest);

        if (buttonFindPassword != null) {
            buttonFindPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = editTextUsernameFindPw.getText().toString();
                    String mode = "reset";
                    if (email == null || email.isEmpty()) {
                        Toast.makeText(FindPw1Activity.this, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!InputValidator.isValidEmail(email)) {
                        Toast.makeText(FindPw1Activity.this, "올바른 형식의 이메일을 입력하세요", Toast.LENGTH_LONG).show();
                        return;
                    }
                    EmailAuthRequestTask.sendRequest(email, mode, new EmailAuthRequestTask.Callback() {
                        @Override
                        public void onSuccess(int httpCode, String responseBody) {
                            SharedPreferencesUtil.savePendingEmail(FindPw1Activity.this, email);
                            Intent intent = new Intent(FindPw1Activity.this, FindPw2Activity.class);
                            startActivity(intent);
                            Toast.makeText(FindPw1Activity.this, "이메일: " + email + " 로 비밀번호 찾기 시도", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(String errorMsg) {
                            Toast.makeText(FindPw1Activity.this,
                                    "인증 요청 실패: " + errorMsg,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}