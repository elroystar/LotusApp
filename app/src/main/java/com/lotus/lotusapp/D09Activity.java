package com.lotus.lotusapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

public class D09Activity extends AppCompatActivity {

    // 声明soundPool
    private SoundPool soundPool;
    // 定义一个整型用load(),来设置soundID
    private int music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_d09);
        // 密码变更
        findViewById(R.id.bt_update_password).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        // 跳转MCD界面
                        Intent i = new Intent(D09Activity.this, MCD09Activity.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 弹框提示
     *
     * @param tips
     * @param msg
     */
    private void alertMsg(String tips, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(D09Activity.this);
        builder.setTitle(tips);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    /**
     * 加载声音
     */
    private void initSound() {
        soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 0);
        music = soundPool.load(this, R.raw.music1, 1);
    }

    /**
     * 播放声音
     */
    private void playSound() {
        soundPool.play(music, 1, 1, 1, 0, 1);
    }
}
