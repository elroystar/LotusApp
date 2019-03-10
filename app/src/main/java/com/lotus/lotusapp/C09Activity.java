package com.lotus.lotusapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

public class C09Activity extends Activity {

    // 串口工具类
    private SerialPortUtil serialPortUtil;
    // 数据库连接工具
    private SQLiteDbHelper sqLiteDbHelper;
    // 声明soundPool
    private SoundPool soundPool;
    // 定义一个整型用load(),来设置soundID
    private int music;
    // 有效洗衣机集合
    private List<String> washCommand = new ArrayList<>();
    // 有效洗衣机集合
    private List<WashingMachine> washingMachines = new ArrayList<>();
    // 定义handler对象
    private Handler handler = new Handler();
    // 循环发送控制
    private int asked = 0;
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
        // 打开串口
        serialPortUtil = new SerialPortUtil();
        serialPortUtil.openSerialPort();
        // 置灰按钮
        ashNoChoiceButton();
        // 加载游戏洗衣机
        initEffectiveWash();
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
                        ashButton(R.id.bt_test, R.drawable.bt_c_navajo_white_shape, true);
                        ashButton(R.id.bt_set, R.drawable.bt_selected_shape, true);
                        // 置灰测试模式键
                        ashTestButton(0, false);
                        // 置灰所有洗衣机
                        ashWashingMachineButton(null, false);
                        // 点亮洗衣机
                        ashWashingMachineButton(null, true);
                        // 加载游戏洗衣机
                        initEffectiveWash();
                        if (washingMachines.size() > 0) {
                            // 设置已注册洗衣机按钮颜色
                            for (WashingMachine washingMachine : washingMachines) {
                                // 获取textView id
                                int bt_washing_machine = getResources().getIdentifier("bt_machine" + washingMachine.getNum(), "id", getPackageName());
                                ashButton(bt_washing_machine, R.drawable.bt_registered_shape, true);
                            }
                        }
                        // 点亮投币箱
                        ashCoinBoxButton(null, true);
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
                        ashButton(R.id.bt_set, R.drawable.bt_c_royal_blue_shape, true);
                        ashButton(R.id.bt_test, R.drawable.bt_selected_shape, true);
                        // 点亮测试模式键
                        ashTestButton(1, true);
                        // 置灰所有洗衣机
                        ashWashingMachineButton(null, false);
                        // 置灰投币箱
                        ashCoinBoxButton(null, false);
                        // 加载游戏洗衣机
                        initEffectiveWash();
                        // 点亮有效洗衣机
                        ashWashingMachineButton(washingMachines, true);
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
                            if (!washCommand.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testWaterIn)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testWaterIn = "close";
                                    for (String cmdWashId : washCommand) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WATER_IN_CLOSE);
                                    }
                                } else if ("close".equals(testWaterIn)) {
                                    v.setBackgroundResource(R.drawable.bt_selected_shape);
                                    testWaterIn = "open";
                                    for (String cmdWashId : washCommand) {
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
                            if (!washCommand.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testWaterOut)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testWaterOut = "close";
                                    for (String cmdWashId : washCommand) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WATER_OUT_CLOSE);
                                    }
                                } else if ("close".equals(testWaterOut)) {
                                    v.setBackgroundResource(R.drawable.bt_selected_shape);
                                    testWaterOut = "open";
                                    for (String cmdWashId : washCommand) {
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
                            if (!washCommand.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testDisinfection)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testDisinfection = "close";
                                    for (String cmdWashId : washCommand) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_DISINFECTION_CLOSE);
                                    }
                                } else if ("close".equals(testDisinfection)) {
                                    v.setBackgroundResource(R.drawable.bt_selected_shape);
                                    testDisinfection = "open";
                                    for (String cmdWashId : washCommand) {
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
                            if (!washCommand.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testSoftening)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testSoftening = "close";
                                    for (String cmdWashId : washCommand) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_SOFTENING_CLOSE);
                                    }
                                } else if ("close".equals(testSoftening)) {
                                    v.setBackgroundResource(R.drawable.bt_selected_shape);
                                    testSoftening = "open";
                                    for (String cmdWashId : washCommand) {
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
                            if (!washCommand.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testWashingLiquid)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testWashingLiquid = "close";
                                    for (String cmdWashId : washCommand) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_WASHING_LIQUID_CLOSE);
                                    }
                                } else if ("close".equals(testWashingLiquid)) {
                                    v.setBackgroundResource(R.drawable.bt_selected_shape);
                                    testWashingLiquid = "open";
                                    for (String cmdWashId : washCommand) {
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
                            if (!washCommand.isEmpty()) {
                                // 播放按键声音
                                playSound();
                                // 发送串口命令
                                if ("open".equals(testShortProgram)) {
                                    v.setBackgroundResource(R.drawable.bt_c_navajo_white_shape);
                                    testShortProgram = "close";
                                    for (String cmdWashId : washCommand) {
                                        serialPortUtil.sendSerialPort(cmdWashId + CmdConstance.TEST_SHORT_PROGRAM_CLOSE);
                                    }
                                } else if ("close".equals(testShortProgram)) {
                                    v.setBackgroundResource(R.drawable.bt_selected_shape);
                                    testShortProgram = "open";
                                    for (String cmdWashId : washCommand) {
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
                selectWashMachine(event, "1", R.id.bt_machine1);
                return false;
            }
        });
        findViewById(R.id.bt_machine2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "2", R.id.bt_machine2);
                return false;
            }
        });
        findViewById(R.id.bt_machine3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "3", R.id.bt_machine3);
                return false;
            }
        });
        findViewById(R.id.bt_machine4).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "4", R.id.bt_machine4);
                return false;
            }
        });
        findViewById(R.id.bt_machine5).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "5", R.id.bt_machine5);
                return false;
            }
        });
        findViewById(R.id.bt_machine6).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "6", R.id.bt_machine6);
                return false;
            }
        });
        findViewById(R.id.bt_machine7).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "7", R.id.bt_machine7);
                return false;
            }
        });
        findViewById(R.id.bt_machine8).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "8", R.id.bt_machine8);
                return false;
            }
        });
        findViewById(R.id.bt_machine9).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "9", R.id.bt_machine9);
                return false;
            }
        });
        findViewById(R.id.bt_machine10).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "10", R.id.bt_machine10);
                return false;
            }
        });
        findViewById(R.id.bt_machine11).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "11", R.id.bt_machine11);
                return false;
            }
        });
        findViewById(R.id.bt_machine12).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "12", R.id.bt_machine12);
                return false;
            }
        });
        findViewById(R.id.bt_machine13).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "13", R.id.bt_machine13);
                return false;
            }
        });
        findViewById(R.id.bt_machine14).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "14", R.id.bt_machine14);
                return false;
            }
        });
        findViewById(R.id.bt_machine15).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "15", R.id.bt_machine15);
                return false;
            }
        });
        findViewById(R.id.bt_machine16).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "16", R.id.bt_machine16);
                return false;
            }
        });
        findViewById(R.id.bt_machine17).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                selectWashMachine(event, "17", R.id.bt_machine17);
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        serialPortUtil.closeSerialPort();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 选择洗衣机
     *
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
                if ("set".equals(model)) {
                    // 定义是否重置洗衣机
                    WashingMachine flagM = null;
                    // 获取textView id
                    int bt_washing_machine = getResources().getIdentifier("bt_machine" + machineId, "id", getPackageName());
                    // 加载游戏洗衣机
                    initEffectiveWash();
                    // 判断该洗衣机是否在集合中
                    for (WashingMachine machine : washingMachines) {
                        if (machineId.equals(Integer.toString(machine.getNum()))) {
                            flagM = machine;
                        }
                    }
                    if (flagM != null) {
                        alertMsg(bt_washing_machine, flagM);
                    } else {
                        washCommand = new ArrayList<>();
                        washCommand.add(machineId);
                        // 置灰所有洗衣机
                        ashWashingMachineButton(null, false);
                        // 置绿选择的洗衣机
                        ashButton(bt_washing_machine, R.drawable.bt_selected_shape, true);
                        asked = 0;
                        handler.postDelayed(runnable, 0);
                    }
                } else if ("test".equals(model)) {
                    WashingMachine washingMachine = getWashingMachine(machineId);
                    if (washCommand.contains(washingMachine.getCommand())) {
                        Iterator<String> it = washCommand.iterator();
                        while (it.hasNext()) {
                            String x = it.next();
                            if (x.equals(washingMachine.getCommand())) {
                                it.remove();
                            }
                        }
                        // 还原洗衣机按钮
                        ashButton(btId, R.drawable.bt_c_royal_blue_shape, true);
                    } else {
                        washCommand.add(washingMachine.getCommand());
                        // 选择洗衣机按钮
                        ashButton(btId, R.drawable.bt_selected_shape, true);
                    }
                } else {
                    alertMsg("tips", "请先选择模式！");
                }
                break;
        }
    }

    /**
     * 获取洗衣机
     *
     * @param num
     * @return
     */
    @NonNull
    private WashingMachine getWashingMachine(String num) {
        // 获取洗衣机
        sqLiteDbHelper = new SQLiteDbHelper(getApplicationContext());
        SQLiteDatabase dbRead = sqLiteDbHelper.getReadableDatabase();
        WashingMachine washingMachine = new WashingMachine();
        try {
            // select * from washer where state = '1' and num = 'num'
            Cursor cursor = dbRead.query(SQLiteDbHelper.TABLE_WASHING_MACHINE,
                    null,
                    "state = ? and num = ?",
                    new String[]{"1", num},
                    null,
                    null,
                    null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    washingMachine.setId(cursor.getInt(cursor.getColumnIndex("id")));
                    washingMachine.setNum(cursor.getInt(cursor.getColumnIndex("num")));
                    washingMachine.setCommand(cursor.getString(cursor.getColumnIndex("command")));
                }
            } else {
                alertMsg("tips", "没有查询到洗衣机！");
            }
        } finally {
            // 关闭数据库连接
            dbRead.close();
        }
        return washingMachine;
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
     * 加载有效洗衣机
     */
    private void initEffectiveWash() {
        washingMachines = new ArrayList<>();
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
                    washingMachine.setCommand(cursor.getString(cursor.getColumnIndex("command")));
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
        AlertDialog.Builder builder = new AlertDialog.Builder(C09Activity.this);
        builder.setTitle(tips);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    /**
     * 确认弹框提示
     *
     * @param bt_coin_box
     * @param washingMachine
     */
    private void alertMsg(final int bt_coin_box, final WashingMachine washingMachine) {
        AlertDialog.Builder builder = new AlertDialog.Builder(C09Activity.this);
        builder.setTitle("温馨提示");
        builder.setMessage("确定要重置吗");
        builder.setCancelable(true);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                washCommand = new ArrayList<>();
                // 还原洗衣机按钮
                ashButton(bt_coin_box, R.drawable.bt_c_royal_blue_shape, true);
                // 删除已注册的洗衣机
                sqLiteDbHelper = new SQLiteDbHelper(getApplicationContext());
                SQLiteDatabase dbWrit = sqLiteDbHelper.getWritableDatabase();
                try {
                    dbWrit.execSQL("delete from washing_machine where id = '" + washingMachine.getId() + "'");
                } finally {
                    dbWrit.close();
                }
                // 发送复位指令
                serialPortUtil.sendSerialPort(washingMachine.getCommand() + CmdConstance.RESET);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                washCommand = new ArrayList<>();
                return;
            }
        });
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
     * 循环发送询问指令
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            asked++;
            if (asked > 3) {
                handler.removeCallbacks(this);
                // 置灰所有洗衣机
                ashWashingMachineButton(null, false);
                // 点亮洗衣机
                ashWashingMachineButton(null, true);
                // 加载游戏洗衣机
                initEffectiveWash();
                if (washingMachines.size() > 0) {
                    // 设置已注册洗衣机按钮颜色
                    for (WashingMachine washingMachine : washingMachines) {
                        // 获取textView id
                        int bt_washing_machine = getResources().getIdentifier("bt_machine" + washingMachine.getNum(), "id", getPackageName());
                        ashButton(bt_washing_machine, R.drawable.bt_registered_shape, true);
                    }
                }
            } else {
                serialPortUtil.sendSerialPort(CmdConstance.REGISTER_ASK);
                handler.postDelayed(this, 5000);
            }
        }
    };

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
        try {
            // 加载游戏洗衣机
            initEffectiveWash();
            if (washingMachines.size() > 0) {
                // 设置已注册洗衣机按钮颜色
                for (WashingMachine washingMachine : washingMachines) {
                    if (string.equals(washingMachine.getCommand())) {
                        return;
                    }
                }
            }
            if (washCommand.size() == 1) {
                dbWrit.execSQL("insert into washing_machine(num,command) values('" + washCommand.get(0) + "','" + string + "');");
                // 置灰所有洗衣机
                ashWashingMachineButton(null, false);
                // 点亮洗衣机
                ashWashingMachineButton(null, true);
                // 加载游戏洗衣机
                initEffectiveWash();
                if (washingMachines.size() > 0) {
                    // 设置已注册洗衣机按钮颜色
                    for (WashingMachine washingMachine : washingMachines) {
                        // 获取textView id
                        int bt_washing_machine = getResources().getIdentifier("bt_machine" + washingMachine.getNum(), "id", getPackageName());
                        ashButton(bt_washing_machine, R.drawable.bt_registered_shape, true);
                    }
                }
                handler.removeCallbacks(runnable);
                // 发送已注册指令
                serialPortUtil.sendSerialPort(string + CmdConstance.REGISTERED);
            }
        } finally {
            dbWrit.close();
        }
    }

}
