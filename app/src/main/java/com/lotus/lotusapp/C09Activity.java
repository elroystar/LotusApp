package com.lotus.lotusapp;

import android.app.AlertDialog;
import android.content.Intent;
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
import java.util.Iterator;
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
    private List<String> washList = new ArrayList<>();
    // 模式选择
    private String model = "";
    // 测试模式选择
    private String testWaterIn = "close";
    private String testWaterOut = "close";
    private String testDisinfection = "close";
    private String testSoftening = "close";
    private String testWashingLiquid = "close";
    private String testShortProgram = "close";

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
        // 退出
        findViewById(R.id.bt_exit_c09).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        Intent i = new Intent(C09Activity.this, A09Activity.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });
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
        // 测试键
        findViewById(R.id.bt_test).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        model = "test";
                        // 置灰设置键
                        ashButton(R.id.bt_set, R.drawable.bt_ash_shape, false);
                        // 点亮测试模式键
                        ashTestButton(1, true);
                        break;
                }
                return false;
            }
        });
        // 进水键
        findViewById(R.id.bt_water_in).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        if ("test".equals(model)) {
                            if (!washList.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testWaterIn)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testWaterIn = "close";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WATER_IN_CLOSE);
                                    }
                                } else if ("close".equals(testWaterIn)) {
                                    v.setBackgroundResource(R.drawable.bt_dark_orange_shape);
                                    testWaterIn = "open";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WATER_IN_OPEN);
                                    }
                                }
                            } else {
                                alertMsg("tips", "请先选择洗衣机！");
                            }
                        } else {
                            alertMsg("tips", "请先选择模式！");
                        }
                        break;
                }
                return false;
            }
        });
        // 排水键
        findViewById(R.id.bt_water_out).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        if ("test".equals(model)) {
                            if (!washList.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testWaterOut)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testWaterOut = "close";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WATER_OUT_CLOSE);
                                    }
                                } else if ("close".equals(testWaterOut)) {
                                    v.setBackgroundResource(R.drawable.bt_dark_orange_shape);
                                    testWaterOut = "open";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WATER_OUT_OPEN);
                                    }
                                }
                            } else {
                                alertMsg("tips", "请先选择洗衣机！");
                            }
                        } else {
                            alertMsg("tips", "请先选择模式！");
                        }
                        break;
                }
                return false;
            }
        });
        // 消毒
        findViewById(R.id.bt_disinfection).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        if ("test".equals(model)) {
                            if (!washList.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testDisinfection)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testDisinfection = "close";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_DISINFECTION_CLOSE);
                                    }
                                } else if ("close".equals(testDisinfection)) {
                                    v.setBackgroundResource(R.drawable.bt_dark_orange_shape);
                                    testDisinfection = "open";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_DISINFECTION_OPEN);
                                    }
                                }
                            } else {
                                alertMsg("tips", "请先选择洗衣机！");
                            }
                        } else {
                            alertMsg("tips", "请先选择模式！");
                        }
                        break;
                }
                return false;
            }
        });
        // 柔顺
        findViewById(R.id.bt_softening).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        if ("test".equals(model)) {
                            if (!washList.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testSoftening)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testSoftening = "close";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_SOFTENING_CLOSE);
                                    }
                                } else if ("close".equals(testSoftening)) {
                                    v.setBackgroundResource(R.drawable.bt_dark_orange_shape);
                                    testSoftening = "open";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_SOFTENING_OPEN);
                                    }
                                }
                            } else {
                                alertMsg("tips", "请先选择洗衣机！");
                            }
                        } else {
                            alertMsg("tips", "请先选择模式！");
                        }
                        break;
                }
                return false;
            }
        });
        // 洗液
        findViewById(R.id.bt_washing_liquid).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        if ("test".equals(model)) {
                            if (!washList.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testWashingLiquid)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testWashingLiquid = "close";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WASHING_LIQUID_CLOSE);
                                    }
                                } else if ("close".equals(testWashingLiquid)) {
                                    v.setBackgroundResource(R.drawable.bt_dark_orange_shape);
                                    testWashingLiquid = "open";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WASHING_LIQUID_OPEN);
                                    }
                                }
                            } else {
                                alertMsg("tips", "请先选择洗衣机！");
                            }
                        } else {
                            alertMsg("tips", "请先选择模式！");
                        }
                        break;
                }
                return false;
            }
        });
        // 短程序
        findViewById(R.id.bt_short_program).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        if ("test".equals(model)) {
                            if (!washList.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testShortProgram)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testShortProgram = "close";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_SHORT_PROGRAM_CLOSE);
                                    }
                                } else if ("close".equals(testShortProgram)) {
                                    v.setBackgroundResource(R.drawable.bt_dark_orange_shape);
                                    testShortProgram = "open";
                                    for (String cmdWashId : washList) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_SHORT_PROGRAM_OPEN);
                                    }
                                }
                            } else {
                                alertMsg("tips", "请先选择洗衣机！");
                            }
                        } else {
                            alertMsg("tips", "请先选择模式！");
                        }
                        break;
                }
                return false;
            }
        });
        // 洗衣机选择
        findViewById(R.id.bt_machine1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_1, R.id.bt_machine1);
                return false;
            }
        });
        findViewById(R.id.bt_machine2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_2, R.id.bt_machine2);
                return false;
            }
        });
        findViewById(R.id.bt_machine3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_3, R.id.bt_machine3);
                return false;
            }
        });
        findViewById(R.id.bt_machine4).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_4, R.id.bt_machine4);
                return false;
            }
        });
        findViewById(R.id.bt_machine5).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_5, R.id.bt_machine5);
                return false;
            }
        });
        findViewById(R.id.bt_machine6).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_6, R.id.bt_machine6);
                return false;
            }
        });
        findViewById(R.id.bt_machine7).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_7, R.id.bt_machine7);
                return false;
            }
        });
        findViewById(R.id.bt_machine8).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_8, R.id.bt_machine8);
                return false;
            }
        });
        findViewById(R.id.bt_machine9).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_9, R.id.bt_machine9);
                return false;
            }
        });
        findViewById(R.id.bt_machine10).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_10, R.id.bt_machine10);
                return false;
            }
        });
        findViewById(R.id.bt_machine11).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_11, R.id.bt_machine11);
                return false;
            }
        });
        findViewById(R.id.bt_machine12).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_12, R.id.bt_machine12);
                return false;
            }
        });
        findViewById(R.id.bt_machine13).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_13, R.id.bt_machine13);
                return false;
            }
        });
        findViewById(R.id.bt_machine14).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_14, R.id.bt_machine14);
                return false;
            }
        });
        findViewById(R.id.bt_machine15).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_15, R.id.bt_machine15);
                return false;
            }
        });
        findViewById(R.id.bt_machine16).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_16, R.id.bt_machine16);
                return false;
            }
        });
        findViewById(R.id.bt_machine17).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, CmdConstance.MACHINE_17, R.id.bt_machine17);
                return false;
            }
        });
    }

    /**
     * 选择洗衣机
     * @param event
     * @param machineId
     * @param btId
     */
    private void selectWashMachine(MotionEvent event, String machineId, int btId) {
        switch (event.getAction()) {
            //按下
            case MotionEvent.ACTION_DOWN:
                // 播放按键声音
                playSound();
                if (washList.contains(machineId)) {
                    Iterator<String> it = washList.iterator();
                    while (it.hasNext()) {
                        String x = it.next();
                        if (x.equals(machineId)) {
                            it.remove();
                        }
                    }
                    // 还原洗衣机按钮
                    ashButton(btId, R.drawable.bt_c_royal_blue_shape, true);
                } else {
                    washList.add(CmdConstance.MACHINE_1);
                    // 选择洗衣机按钮
                    ashButton(btId, R.drawable.bt_c_select_shape, true);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        serialPortUtil.closeSerialPort();
    }

    /**
     * 置灰按钮
     */
    private void ashNoChoiceButton() {
        // 置灰洗衣机
        ashWashingMachineButton(null, false);
        // 置灰投币箱
        ashCoinBoxButton(null, false);
        // 置灰测试模式键
        ashTestButton(0, false);
    }

    /**
     * 置灰测试模式键
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
            for (int i = 1; i <= 3; i++) {
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
            for (int i = 1; i <= 17; i++) {
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
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_1, "insert into washing_machine(num) values(1);", R.id.bt_machine1);
                    break;
                case CmdConstance.MACHINE_2:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_2, "insert into washing_machine(num) values(2);", R.id.bt_machine2);
                    break;
                case CmdConstance.MACHINE_3:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_3, "insert into washing_machine(num) values(3);", R.id.bt_machine3);
                    break;
                case CmdConstance.MACHINE_4:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_4, "insert into washing_machine(num) values(4);", R.id.bt_machine4);
                    break;
                case CmdConstance.MACHINE_5:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_5, "insert into washing_machine(num) values(5);", R.id.bt_machine5);
                    break;
                case CmdConstance.MACHINE_6:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_6, "insert into washing_machine(num) values(6);", R.id.bt_machine6);
                    break;
                case CmdConstance.MACHINE_7:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_7, "insert into washing_machine(num) values(7);", R.id.bt_machine7);
                    break;
                case CmdConstance.MACHINE_8:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_8, "insert into washing_machine(num) values(8);", R.id.bt_machine8);
                    break;
                case CmdConstance.MACHINE_9:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_9, "insert into washing_machine(num) values(9);", R.id.bt_machine9);
                    break;
                case CmdConstance.MACHINE_10:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_10, "insert into washing_machine(num) values(10);", R.id.bt_machine10);
                    break;
                case CmdConstance.MACHINE_11:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_11, "insert into washing_machine(num) values(11);", R.id.bt_machine11);
                    break;
                case CmdConstance.MACHINE_12:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_12, "insert into washing_machine(num) values(12);", R.id.bt_machine12);
                    break;
                case CmdConstance.MACHINE_13:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_13, "insert into washing_machine(num) values(13);", R.id.bt_machine13);
                    break;
                case CmdConstance.MACHINE_14:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_14, "insert into washing_machine(num) values(14);", R.id.bt_machine14);
                    break;
                case CmdConstance.MACHINE_15:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_15, "insert into washing_machine(num) values(15);", R.id.bt_machine15);
                    break;
                case CmdConstance.MACHINE_16:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_16, "insert into washing_machine(num) values(16);", R.id.bt_machine16);
                    break;
                case CmdConstance.MACHINE_17:
                    setWashingMachines(dbWrit, CmdConstance.MACHINE_17, "insert into washing_machine(num) values(17);", R.id.bt_machine17);
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
     *
     * @param dbWrit
     * @param cmd
     * @param sql
     * @param num
     */
    private void setWashingMachines(SQLiteDatabase dbWrit, String cmd, String sql, int num) {
        serialPortUtil.sendSerialPort(cmd);
        dbWrit.execSQL(sql);
        ashButton(num, R.drawable.bt_c_royal_blue_shape, true);
    }

}
