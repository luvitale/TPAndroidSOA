package com.luvitale.tpandroidsoa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import io.paperdb.Paper;

public class PatternActivity extends AppCompatActivity {

    String save_pattern_key = "pattern_code";
    String final_pattern = "";
    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        defineExit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        Paper.init(this);
        final String save_pattern = Paper.book().read(save_pattern_key);

        if (savePatternExists(save_pattern)) {
            checkPattern(save_pattern);
        }

        else {
            changePattern();
        }
    }

    private void changePattern() {
        setContentView(R.layout.activity_pattern);

        // Listen pattern
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
            }

            @Override
            public void onCleared() {}
        });

        // Save pattern
        Button btnSaveUnlockPattern = (Button) findViewById(R.id.btnSaveUnlockPattern);
        btnSaveUnlockPattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().write(save_pattern_key, final_pattern);
                Toast.makeText(PatternActivity.this, "Patrón guardado con éxito", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PatternActivity.this, UnlockActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean savePatternExists(final String save_pattern) {
        return save_pattern != null && !save_pattern.equals("null");
    }

    private void checkPattern(final String save_pattern) {
        setContentView(R.layout.activity_unlock);
        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {}

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {}

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                final_pattern = PatternLockUtils.patternToString(mPatternLockView, pattern);
                if (final_pattern.equals(save_pattern)) {
                    Toast.makeText(PatternActivity.this, "Patrón ingresado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PatternActivity.this, SessionActivity.class);
                    startActivity(intent);
                }

                else {
                    Toast.makeText(PatternActivity.this, "Patrón incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCleared() {}
        });
    }

    private void defineExit() {
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
            return;
        }
    }

}
