package com.lotus.lotusapp;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.lotus.lotusapp.constance.CmdConstance;
import com.lotus.lotusapp.db.SQLiteDbHelper;
import com.lotus.lotusapp.dto.CoinBox;
import com.lotus.lotusapp.dto.WashingMachine;
import com.lotus.lotusapp.utils.SerialPortUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class C09Activity extends AppCompatActivity {

    // 串口工具类
    private SerialPortUtil serialPortUtil;
    // 数据库连接工具
    private SQLiteDbHelper sqLiteDbHelper;
    // 声明soundPool
    private SoundPool soundPool;
    // 定义一个整型用load(),来设置soundID
    private int music;
    // 有效洗衣机集合
    private List<WashingMachine> washingMachines = new ArrayList<>();
    // 模式选择
    private String model = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c09);
        //注册EventBus
        EventBus.getDefault().register(this);
        // 加载声音
        initSound();
        // 置灰按钮
        ashNoChoiceButton();
        // 设置键
        findViewById(R.id.bt_set).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        model = "set";
                        // 置灰测试键
                        ashButton(R.id.bt_test, R.drawable.bt_ash_shape, false);
                        // 打开串口，发送设置指令
                        serialPortUtil = new SerialPortUtil();
                        serialPortUtil.openSerialPort();
                        // 新厂安装发送询问指令
                        serialPortUtil.sendSerialPort(CmdConstance.REGISTER_ASK);
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPortUtil.closeSerialPort();
    }

    /**
     * 置灰按钮
     */
    private void ashNoChoiceButton() {
        // 置灰洗衣机
        ashWashingMachineButton(null, false);
        // 置灰投币箱
        ashCoinBoxButton(null, false);
        // 置灰测试键
        ashTestButton(0, false);
    }

    /**
     * 置灰测试键
     *
     * @param type
     * @param buttonLight
     */
    private void ashTestButton(int type, Boolean buttonLight) {
        if (0 == type) {
            ashButton(R.id.bt_water_in, R.drawable.bt_ash_shape, buttonLight);
            ashButton(R.id.bt_water_out, R.drawable.bt_ash_shape, buttonLight);
            ashButton(R.id.bt_disinfection, R.drawable.bt_ash_shape, buttonLight);
            ashButton(R.id.bt_softening, R.drawable.bt_ash_shape, buttonLight);
            ashButton(R.id.bt_washing_liquid, R.drawable.bt_ash_shape, buttonLight);
            ashButton(R.id.bt_short_program, R.drawable.bt_ash_shape, buttonLight);
        } else if (1 == type) {
            ashButton(R.id.bt_water_in, R.drawable.bt_c_navajo_white_shape, buttonLight);
            ashButton(R.id.bt_water_out, R.drawable.bt_c_navajo_white_shape, buttonLight);
            ashButton(R.id.bt_disinfection, R.drawable.bt_c_navajo_white_shape, buttonLight);
            ashButton(R.id.bt_softening, R.drawable.bt_c_navajo_white_shape, buttonLight);
            ashButton(R.id.bt_washing_liquid, R.drawable.bt_c_navajo_white_shape, buttonLight);
            ashButton(R.id.bt_short_program, R.drawable.bt_c_navajo_white_shape, buttonLight);
        } else {
            if (buttonLight) {
                ashButton(type, R.drawable.bt_c_navajo_white_shape, buttonLight);
            } else {
                ashButton(type, R.drawable.bt_ash_shape, buttonLight);
            }
        }
    }

    /**
     * 还原定价区价格状态
     *
     * @param tvId
     */
    private void restoreTvPriceButton(int tvId) {
        int[] ints = {R.id.tv_drying_price_mobile,
                R.id.tv_drying_price_coin,
                R.id.tv_rinse_price_mobile,
                R.id.tv_rinse_price_coin,
                R.id.tv_cowboy_price_mobile,
                R.id.tv_cowboy_price_coin,
                R.id.tv_sheets_price_mobile,
                R.id.tv_sheets_price_coin,
                R.id.tv_standard_price_mobile,
                R.id.tv_standard_price_coin,
                R.id.tv_washing_liquid_price_mobile,
                R.id.tv_washing_liquid_price_coin,
                R.id.tv_softening_price_mobile,
                R.id.tv_softening_price_coin,
                R.id.tv_disinfection_ing_price_mobile,
                R.id.tv_disinfection_ing_price_coin,
                R.id.tv_disinfection_before_price_mobile,
                R.id.tv_disinfection_before_price_coin};
        for (int i : ints) {
            if (i != tvId) {
                findViewById(i).setBackgroundResource(R.drawable.c09_tv_price_shape);
            }
        }
    }

    /**
     * 置灰投币箱
     *
     * @param coinBox
     * @param buttonLight
     */
    private void ashCoinBoxButton(CoinBox coinBox, Boolean buttonLight) {
        if (null != coinBox) {
            int bt_coin_box = getResources().getIdentifier("bt_coin_" + coinBox.getNum(), "id", getPackageName());
            if (buttonLight) {
                ashButton(bt_coin_box, R.drawable.bt_c_royal_blue_shape, true);
            } else {
                ashButton(bt_coin_box, R.drawable.bt_ash_shape, false);
            }
        } else {
            for (int i = 1; i <= 4; i++) {
                // 获取textView id
                int bt_coin_box = getResources().getIdentifier("bt_coin_" + i, "id", getPackageName());
                if (buttonLight) {
                    ashButton(bt_coin_box, R.drawable.bt_c_royal_blue_shape, true);
                } else {
                    ashButton(bt_coin_box, R.drawable.bt_ash_shape, false);
                }
            }
        }
    }

    /**
     * 置灰洗衣机
     *
     * @param washingMachines
     * @param buttonLight
     */
    private void ashWashingMachineButton(List<WashingMachine> washingMachines, boolean buttonLight) {
        if (washingMachines == null) {
            // 置灰所有
            for (int i = 1; i <= 16; i++) {
                // 获取textView id
                int bt_washing_machine = getResources().getIdentifier("bt_machine" + i, "id", getPackageName());
                if (buttonLight) {
                    ashButton(bt_washing_machine, R.drawable.bt_c_royal_blue_shape, true);
                } else {
                    ashButton(bt_washing_machine, R.drawable.bt_ash_shape, false);
                }
            }
        } else {
            // 置灰选择的
            for (WashingMachine washingMachine : washingMachines) {
                // 获取textView id
                int bt_washing_machine = getResources().getIdentifier("bt_machine" + washingMachine.getNum(), "id", getPackageName());
                if (buttonLight) {
                    ashButton(bt_washing_machine, R.drawable.bt_c_royal_blue_shape, true);
                } else {
                    ashButton(bt_washing_machine, R.drawable.bt_ash_shape, false);
                }
            }
        }
    }

    /**
     * 按钮置灰加不可点击
     *
     * @param bt_coin_box
     * @param drawable
     * @param enable
     */
    private void ashButton(int bt_coin_box, int drawable, boolean enable) {
        findViewById(bt_coin_box).setBackgroundResource(drawable);
        findViewById(bt_coin_box).setEnabled(enable);
    }

    /**
     * 弹框提示
     *
     * @param tips
     * @param msg
     */
    private void alertMsg(String tips, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(C09Activity.this);
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

    /**
     * 用EventBus进行线程间通信，也可以使用Handler
     *
     * @param string
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String string) {
        Log.d("C09Activity", "获取到了从传感器发送到Android主板的串口数据：" + string);
        sqLiteDbHelper = new SQLiteDbHelper(getApplicationContext());
        SQLiteDatabase dbWrit = sqLiteDbHelper.getWritableDatabase();
        WashingMachine machine = new WashingMachine();
        try {
            switch (string) {
                case CmdConstance.MACHINE_1:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_1, "insert into washing_machine(num) values(1);", 1);
                    break;
                case CmdConstance.MACHINE_2:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_2, "insert into washing_machine(num) values(2);", 2);
                    break;
                case CmdConstance.MACHINE_3:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_3, "insert into washing_machine(num) values(3);", 3);
                    break;
                case CmdConstance.MACHINE_4:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_4, "insert into washing_machine(num) values(4);", 4);
                    break;
                case CmdConstance.MACHINE_5:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_5, "insert into washing_machine(num) values(5);", 5);
                    break;
                case CmdConstance.MACHINE_6:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_6, "insert into washing_machine(num) values(6);", 6);
                    break;
                case CmdConstance.MACHINE_7:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_7, "insert into washing_machine(num) values(7);", 7);
                    break;
                case CmdConstance.MACHINE_8:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_8, "insert into washing_machine(num) values(8);", 8);
                    break;
                case CmdConstance.MACHINE_9:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_9, "insert into washing_machine(num) values(9);", 9);
                    break;
                case CmdConstance.MACHINE_10:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_10, "insert into washing_machine(num) values(10);", 10);
                    break;
                case CmdConstance.MACHINE_11:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_11, "insert into washing_machine(num) values(11);", 11);
                    break;
                case CmdConstance.MACHINE_12:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_12, "insert into washing_machine(num) values(12);", 12);
                    break;
                case CmdConstance.MACHINE_13:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_13, "insert into washing_machine(num) values(13);", 13);
                    break;
                case CmdConstance.MACHINE_14:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_14, "insert into washing_machine(num) values(14);", 14);
                    break;
                case CmdConstance.MACHINE_15:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_15, "insert into washing_machine(num) values(15);", 15);
                    break;
                case CmdConstance.MACHINE_16:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_16, "insert into washing_machine(num) values(16);", 16);
                    break;
                case CmdConstance.MACHINE_17:
                    setWashingMachines(dbWrit, machine, CmdConstance.MACHINE_17, "insert into washing_machine(num) values(17);", 17);
                    break;
                default:
                    serialPortUtil.sendSerialPort(CmdConstance.REGISTER_ASK);
                    break;
            }
        } finally {
            dbWrit.close();
        }
    }

    /**
     * 设置洗衣机
     * @param dbWrit
     * @param machine
     * @param cmd
     * @param sql
     * @param num
     */
    private void setWashingMachines(SQLiteDatabase dbWrit, WashingMachine machine, String cmd, String sql, int num) {
        serialPortUtil.sendSerialPort(cmd);
        dbWrit.execSQL(sql);
        washingMachines = new ArrayList<>();
        machine.setNum(num);
        washingMachines.add(machine);
        ashWashingMachineButton(washingMachines, true);
    }

}
