package com.lotus.lotusapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lotus.lotusapp.db.SQLiteDbHelper;
import com.lotus.lotusapp.dto.PasswordRule;

public class MCD09Activity extends Activity {

    // 数据库连接工具
    private SQLiteDbHelper sqLiteDbHelper;

    // 声明soundPool
    private SoundPool soundPool;
    // 定义一个整型用load(),来设置soundID
    private int music;

    // 密码规则
    private PasswordRule cRule = new PasswordRule();
    private PasswordRule dRule = new PasswordRule();

    private String selectModel = "";
    private String pass1 = "";
    private String pass2 = "";
    private String getTvSelectModel = "";

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcd09);
        // 加载声音
        initSound();
        // 返回安卓
        findViewById(R.id.bt_android).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 将应用退到桌面上，保留自身
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
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
                        Intent i = new Intent(MCD09Activity.this, D09Activity.class);
                        startActivity(i);
                        finish();
                        break;
                }
                return false;
            }
        });
        // 修改C界面密码
        findViewById(R.id.tv_c).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        selectModel = "C";
                        getTvSelectModel = "1";
                        v.setBackgroundResource(R.drawable.bt_c_select_shape);
                        findViewById(R.id.tv_d).setBackgroundResource(R.drawable.bt_d_dark_voilet_shape);
                        break;
                }
                return false;
            }
        });
        // 修改D界面密码
        findViewById(R.id.tv_d).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        selectModel = "D";
                        getTvSelectModel = "1";
                        v.setBackgroundResource(R.drawable.bt_d_select_shape);
                        findViewById(R.id.tv_c).setBackgroundResource(R.drawable.bt_c_royal_blue_shape);
                        break;
                }
                return false;
            }
        });
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
                        SQLiteDatabase dbWrit = sqLiteDbHelper.getWritableDatabase();
                        try {
                            if ("C".equals(selectModel)) {
                                if (pass1.length() == 6 && "1".equals(getTvSelectModel)) {
                                    getTvSelectModel = "2";
                                } else {
                                    alertMsg("Tips", "请输入有效密码");
                                }
                                if (pass2.length() == 6 && "2".equals(getTvSelectModel)) {
                                    if (pass2.equals(pass1)) {
                                        getPasswordRule("C", cRule);
                                        // 修改密码规则入库
                                        dbWrit.execSQL("update password_rule set state = '1', rule = " + pass2 + " where id = " + cRule.getId() + "");
                                        // 重置
                                        resetModel();
                                    } else {
                                        alertMsg("Tips", "两次输入密码不一致");
                                        // 重置
                                        resetModel();
                                    }
                                } else {
                                    alertMsg("Tips", "请输入有效密码");
                                }
                            } else if ("D".equals(selectModel)) {
                                if (pass1.length() == 6 && "1".equals(getTvSelectModel)) {
                                    getTvSelectModel = "2";
                                } else {
                                    alertMsg("Tips", "请输入有效密码");
                                }
                                if (pass2.length() == 6 && "2".equals(getTvSelectModel)) {
                                    if (pass2.equals(pass1)) {
                                        getPasswordRule("D", dRule);
                                        // 修改密码规则入库
                                        dbWrit.execSQL("update password_rule set state = '1', rule = " + pass2 + " where id = " + dRule.getId() + "");
                                        // 重置
                                        resetModel();
                                    } else {
                                        alertMsg("Tips", "两次输入密码不一致");
                                        // 重置
                                        resetModel();
                                    }
                                } else {
                                    alertMsg("Tips", "请输入有效密码");
                                }
                            } else {
                                alertMsg("Tips", "请选择要修改密码的界面");
                            }
                        } finally {
                            dbWrit.close();
                        }
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
                        if ("C".equals(selectModel)) {
                            if ("1".equals(getTvSelectModel)) {
                                pass1 = "";
                                textView = findViewById(R.id.tv_c_password_one);
                                textView.setText(pass1);
                            }
                            if ("2".equals(getTvSelectModel)) {
                                pass2 = "";
                                textView = findViewById(R.id.tv_c_password_two);
                                textView.setText(pass2);
                            }
                        } else if ("D".equals(selectModel)) {
                            if ("1".equals(getTvSelectModel)) {
                                pass1 = "";
                                textView = findViewById(R.id.tv_d_password_one);
                                textView.setText(pass1);
                            }
                            if ("2".equals(getTvSelectModel)) {
                                pass2 = "";
                                textView = findViewById(R.id.tv_d_password_two);
                                textView.setText(pass2);
                            }
                        } else {
                            alertMsg("Tips", "请选择要修改密码的界面");
                        }
                        break;
                }
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "0");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "1");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "2");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "3");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_4).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "4");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_5).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "5");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_6).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "6");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_7).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "7");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_8).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "8");
                return false;
            }
        });
        // 数字键盘区逻辑
        findViewById(R.id.bt_num_9).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                updatePass(event, "9");
                return false;
            }
        });
    }

    /**
     * 重置
     */
    private void resetModel() {
        selectModel = "";
        getTvSelectModel = "";
        pass1 = "";
        pass2 = "";
        findViewById(R.id.tv_c).setBackgroundResource(R.drawable.bt_c_royal_blue_shape);
        findViewById(R.id.tv_d).setBackgroundResource(R.drawable.bt_d_dark_voilet_shape);
    }

    /**
     * 输入密码
     *
     * @param event
     * @param num
     */
    private void updatePass(MotionEvent event, String num) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 播放按键声音
                playSound();
                if ("C".equals(selectModel)) {
                    if (pass1.length() <= 5 && "1".equals(getTvSelectModel)) {
                        pass1 = pass1 + num;
                        textView = findViewById(R.id.tv_c_password_one);
                        textView.setText(pass1);
                    } else if (pass1.length() > 5 && "1".equals(getTvSelectModel)) {
                        pass1 = num;
                        textView = findViewById(R.id.tv_c_password_one);
                        textView.setText(pass1);
                    }
                    if (pass2.length() <= 5 && "2".equals(getTvSelectModel)) {
                        pass2 = pass2 + num;
                        textView = findViewById(R.id.tv_c_password_two);
                        textView.setText(pass2);
                    } else if (pass2.length() > 5 && "2".equals(getTvSelectModel)) {
                        pass2 = num;
                        textView = findViewById(R.id.tv_c_password_two);
                        textView.setText(pass2);
                    }
                } else if ("D".equals(selectModel)) {
                    if (pass1.length() <= 5 && "1".equals(getTvSelectModel)) {
                        pass1 = pass1 + num;
                        textView = findViewById(R.id.tv_d_password_one);
                        textView.setText(pass1);
                    } else if (pass1.length() > 5 && "1".equals(getTvSelectModel)) {
                        pass1 = num;
                        textView = findViewById(R.id.tv_d_password_one);
                        textView.setText(pass1);
                    }
                    if (pass2.length() <= 5 && "2".equals(getTvSelectModel)) {
                        pass2 = pass2 + num;
                        textView = findViewById(R.id.tv_d_password_two);
                        textView.setText(pass2);
                    } else if (pass2.length() > 5 && "2".equals(getTvSelectModel)) {
                        pass2 = num;
                        textView = findViewById(R.id.tv_d_password_two);
                        textView.setText(pass2);
                    }
                } else {
                    alertMsg("Tips", "请选择要修改密码的界面");
                }
                break;
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(MCD09Activity.this);
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
