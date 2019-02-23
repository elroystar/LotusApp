package com.lotus.lotusapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lotus.lotusapp.db.SQLiteDbHelper;
import com.lotus.lotusapp.dto.PasswordRule;
import com.lotus.lotusapp.utils.PasswordRuleUtil;

public class M09Activity extends AppCompatActivity {

    // 数据库连接工具
    private SQLiteDbHelper sqLiteDbHelper;

    // 声明soundPool
    private SoundPool soundPool;
    // 定义一个整型用load(),来设置soundID
    private int music;

    // 密码规则
    private PasswordRule cRule = new PasswordRule();
    private PasswordRule dRule = new PasswordRule();

    // 数字输入框字符串
    private String stringTx = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m09);
        // 加载声音
        initSound();
        // 获取C界面密码规则
        getPasswordRule("C", cRule);
        // 获取D界面密码规则
        getPasswordRule("D", dRule);
        // 数字键盘区逻辑
        functionNumButton();

    }

    /**
     * 数字键盘区逻辑
     */
    private void functionNumButton() {
        // OK键
        findViewById(R.id.bt_num_ok).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 查询数据库
                        sqLiteDbHelper = new SQLiteDbHelper(getApplicationContext());
                        SQLiteDatabase dbRead = sqLiteDbHelper.getReadableDatabase();
                        SQLiteDatabase dbWrit = sqLiteDbHelper.getWritableDatabase();
                        try {
                            // 验证密码有限期
                            // select * from password_bank where password = 'stringTx'
                            Cursor cursor = dbRead.query(SQLiteDbHelper.TABLE_PASSWORD_BANK,
                                    null,
                                    "password = ?",
                                    new String[]{stringTx},
                                    null,
                                    null,
                                    null);
//                            if (cursor.getCount() > 0) {
//                                alertMsg("Tips", "密码" + stringTx + "已存在，请重新输入！");
//                            } else {
//                            }
                            // 校验密码，优先校验D界面
                            if (PasswordRuleUtil.checkPassword(dRule.getRule(), stringTx)) {
                                // 已经使用的密码入库
                                dbWrit.execSQL("insert into password_bank(password) values ('" + stringTx + "')");
                                // 跳转C界面
                                Intent i = new Intent(M09Activity.this, D09Activity.class);
                                startActivity(i);
                            } else {
                                // 校验C界面，先判断密码是否启用
                                if ("1".equals(cRule.getState())) {
                                    if (PasswordRuleUtil.checkPassword(cRule.getRule(), stringTx)) {
                                        // 已经使用的密码入库
                                        dbWrit.execSQL("insert into password_bank(password) values ('" + stringTx + "')");
                                        // 跳转C界面
                                        Intent i = new Intent(M09Activity.this, C09Activity.class);
                                        startActivity(i);
                                    } else {
                                        alertMsg("Tips", "密码输入有误，请重新输入！");
                                    }
                                } else {
                                    if ("333333".equals(stringTx)) {
                                        // 跳转C界面
                                        Intent i = new Intent(M09Activity.this, C09Activity.class);
                                        startActivity(i);
                                    } else {
                                        alertMsg("Tips", "密码输入有误，请重新输入！");
                                    }
                                }
                            }
                        } finally {
                            // 关闭数据库连接
                            dbRead.close();
                            dbWrit.close();
                        }
                        break;
                }
                return false;
            }
        });
        // 退出
        findViewById(R.id.bt_exit).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        Intent i = new Intent(M09Activity.this, A09Activity.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
        // Esc键
        findViewById(R.id.bt_num_esc).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        TextView tv = findViewById(R.id.tv_password);
                        tv.setText("");
                        stringTx = "";
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "0";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "1";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "2";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "3";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_4).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "4";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_5).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "5";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_6).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "6";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_7).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "7";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_8).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "8";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
        findViewById(R.id.bt_num_9).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        stringTx = stringTx + "9";
                        TextView tv = findViewById(R.id.tv_password);
                        if (stringTx.length() < 20) {
                            tv.setText(stringTx);
                        } else {
                            String txShow = stringTx.substring(stringTx.length() - 20);
                            tv.setText(txShow);
                        }
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 获取密码规则
     *
     * @param act
     * @param rule
     */
    private void getPasswordRule(String act, PasswordRule rule) {
        sqLiteDbHelper = new SQLiteDbHelper(getApplicationContext());
        SQLiteDatabase dbRead = sqLiteDbHelper.getReadableDatabase();
        try {
            // select * from password_rule where type = act
            Cursor cursor = dbRead.query(SQLiteDbHelper.TABLE_PASSWORD_RULE,
                    null,
                    "type = ?",
                    new String[]{act},
                    null,
                    null,
                    null);
            if (cursor.getCount() > 0) {
                // passwordRule赋值
                while (cursor.moveToNext()) {
                    rule.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    rule.setRule(cursor.getString(cursor.getColumnIndex("rule")));
                    rule.setState(cursor.getString(cursor.getColumnIndex("state")));
                    rule.setType(cursor.getString(cursor.getColumnIndex("type")));
                }
            } else {
                alertMsg("Tips", "未查询到" + act + "界面密码规则");
            }
        } finally {
            // 关闭数据库连接
            dbRead.close();
        }
    }

    /**
     * 弹框提示
     *
     * @param tips
     * @param msg
     */
    private void alertMsg(String tips, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(M09Activity.this);
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
