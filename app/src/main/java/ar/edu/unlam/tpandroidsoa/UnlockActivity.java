package ar.edu.unlam.tpandroidsoa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import ar.edu.unlam.tpandroidsoa.R;

import java.util.List;

import io.paperdb.Paper;

public class UnlockActivity extends AppCompatActivity {

    String save_pattern_key = "pattern_code";
    String final_pattern = "";
    PatternLockView mPatternLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);

        Paper.init(this);
        final String save_pattern = Paper.book().read(save_pattern_key);

        if (savePatternExists(save_pattern)) {
            checkPattern(save_pattern);
        }

        else {
            setContentView(R.layout.activity_pattern);
        }
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
                    Toast.makeText(UnlockActivity.this, "Patrón ingresado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UnlockActivity.this, SessionActivity.class);
                    startActivity(intent);
                }

                else {
                    Toast.makeText(UnlockActivity.this, "Patrón incorrecto", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCleared() {}
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            ActivityCompat.finishAffinity(this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
