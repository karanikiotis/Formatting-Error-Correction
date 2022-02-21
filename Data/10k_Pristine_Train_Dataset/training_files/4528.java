package me.drakeet.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.normal_text);
        final TextView labelView = (TextView) findViewById(R.id.lableview);
        textView.setText(getString(R.string.hint, labelView.getText()));

        EditText editText = (EditText) findViewById(R.id.edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                labelView.setText(s);
                textView.setText(getString(R.string.hint, labelView.getText()));
            }


            @Override public void afterTextChanged(Editable s) {

            }
        });
    }
}
