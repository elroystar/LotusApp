package com.lotus.lotusapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.lotus.lotusapp.db.SQLiteDbHelper;
import com.lotus.lotusapp.dto.WashingMachine;
import com.lotus.lotusapp.utils.LongClickUtils;

import java.util.ArrayList;
import java.util.List;

public class A09Activity extends AppCompatActivity {

    // 数据库连接工具
    private SQLiteDbHelper sqLiteDbHelper;
    // 声明soundPool
    private SoundPool soundPool;
    // 定义一个整型用load(),来设置soundID
    private int music;
    // 有效洗衣机集合
    private List<WashingMachine> washingMachines = new ArrayList<>();
    // 选择洗衣机
    private WashingMachine machine;
    // FrameLayout变量
    private FrameLayout frameLayout;
    // 图片按钮定义
    private ImageView imageView;
    // En.选择
    private String En = "Thai";
    // 定义RequestOptions
    private RequestOptions requestOptions = new RequestOptions()
            .error(R.drawable.ic_launcher_background);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a09);
        // 加载声音
        initSound();
        // 加载游戏洗衣机
        initEffectiveWash();
        // 显示洗衣机按钮
        displayMachineBtn();
        // 长按logo进入M界面
        LongClickUtils.setLongClick(new Handler(), findViewById(R.id.logo), 5000, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 进入M界面
                Intent i = new Intent(A09Activity.this, M09Activity.class);
                i.putExtra("En", En);
                startActivity(i);
                return false;
            }
        });

        findViewById(R.id.machine_1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        // 进入B界面
                        Intent i = new Intent(A09Activity.this, B09Activity.class);
                        i.putExtra("WashingMachine", washingMachines.get(0));
                        i.putExtra("En", En);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.machine_2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        // 进入B界面
                        Intent i = new Intent(A09Activity.this, B09Activity.class);
                        i.putExtra("WashingMachine", washingMachines.get(1));
                        i.putExtra("En", En);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.machine_3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //按下
                        // 播放按键声音
                        playSound();
                        // 进入B界面
                        Intent i = new Intent(A09Activity.this, B09Activity.class);
                        i.putExtra("WashingMachine", washingMachines.get(2));
                        i.putExtra("En", En);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

    }

    /**
     * 显示洗衣机按钮
     */
    private void displayMachineBtn() {
        if (washingMachines.size() > 1) {
            switch (washingMachines.size()) {
                case 2:
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    imageView = (ImageView) frameLayout.getChildAt(0);
                    imageView.setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    imageView = (ImageView) frameLayout.getChildAt(0);
                    imageView.setId(R.id.machine_2);
                    break;
                case 3:
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_e);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    break;
                case 4:
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    break;
                case 5:
                    frameLayout = findViewById(R.id.frame_machine_b);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_e);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_h);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    break;
                case 6:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    break;
                case 7:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_e);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    break;
                case 8:
                    frameLayout = findViewById(R.id.frame_machine_b);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_m);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_n);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_h);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    break;
                case 9:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_j);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_m);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_e);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_n);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    break;
            }
        }
    }

    /**
     * 加载有效洗衣机
     */
    private void initEffectiveWash() {
        // 查询有效洗衣机
        sqLiteDbHelper = new SQLiteDbHelper(getApplicationContext());
        SQLiteDatabase dbRead = sqLiteDbHelper.getReadableDatabase();
        try {
            // select * from washer where state = '1'
            Cursor cursor = dbRead.query(SQLiteDbHelper.TABLE_WASHING_MACHINE,
                    null,
                    "state = ?",
                    new String[]{"1"},
                    null,
                    null,
                    null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    WashingMachine washingMachine = new WashingMachine();
                    washingMachine.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    washingMachine.setNum(cursor.getInt(cursor.getColumnIndex("num")));
                    washingMachine.setCowboyPriceCoin(cursor.getString(cursor.getColumnIndex("cowboy_price_coin")));
                    washingMachine.setCowboyPriceMobile(cursor.getString(cursor.getColumnIndex("cowboy_price_mobile")));
                    washingMachine.setDisinfectionBeforePriceCoin(cursor.getString(cursor.getColumnIndex("disinfection_before_price_coin")));
                    washingMachine.setDisinfectionBeforePriceMobile(cursor.getString(cursor.getColumnIndex("disinfection_before_price_mobile")));
                    washingMachine.setDisinfectionIngPriceCoin(cursor.getString(cursor.getColumnIndex("disinfection_ing_price_coin")));
                    washingMachine.setDisinfectionIngPriceMobile(cursor.getString(cursor.getColumnIndex("disinfection_ing_price_mobile")));
                    washingMachine.setDryingPriceCoin(cursor.getString(cursor.getColumnIndex("drying_price_coin")));
                    washingMachine.setDryingPriceMobile(cursor.getString(cursor.getColumnIndex("drying_price_mobile")));
                    washingMachine.setRinsePriceCoin(cursor.getString(cursor.getColumnIndex("rinse_price_coin")));
                    washingMachine.setRinsePriceMobile(cursor.getString(cursor.getColumnIndex("rinse_price_mobile")));
                    washingMachine.setSheetsPriceCoin(cursor.getString(cursor.getColumnIndex("sheets_price_coin")));
                    washingMachine.setSheetsPriceMobile(cursor.getString(cursor.getColumnIndex("sheets_price_mobile")));
                    washingMachine.setSofteningPriceCoin(cursor.getString(cursor.getColumnIndex("softening_price_coin")));
                    washingMachine.setSofteningPriceMobile(cursor.getString(cursor.getColumnIndex("softening_price_mobile")));
                    washingMachine.setStandardPriceCoin(cursor.getString(cursor.getColumnIndex("standard_price_coin")));
                    washingMachine.setStandardPriceMobile(cursor.getString(cursor.getColumnIndex("standard_price_mobile")));
                    washingMachine.setWashingLiquidPriceCoin(cursor.getString(cursor.getColumnIndex("washing_liquid_price_coin")));
                    washingMachine.setWashingLiquidPriceMobile(cursor.getString(cursor.getColumnIndex("washing_liquid_price_mobile")));
                    washingMachine.setDisinfectionState(cursor.getString(cursor.getColumnIndex("disinfection_state")));
                    washingMachine.setRinseState(cursor.getString(cursor.getColumnIndex("rinse_state")));
                    washingMachine.setWashingLiquidState(cursor.getString(cursor.getColumnIndex("washing_liquid_state")));
                    washingMachine.setState(cursor.getString(cursor.getColumnIndex("state")));
                    washingMachines.add(washingMachine);
                }
            } else {
                alertMsg("Error", "没有找到可以用的洗衣机！");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(A09Activity.this);
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
