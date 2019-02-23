package com.lotus.lotusapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.lotus.lotusapp.constance.CmdConstance;
import com.lotus.lotusapp.db.SQLiteDbHelper;
import com.lotus.lotusapp.dto.WashingMachine;
import com.lotus.lotusapp.utils.SerialPortUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class B09Activity extends AppCompatActivity {

    public static final String DRYING = "drying";
    public static final String RINSE = "rinse";
    public static final String COWBOY = "cowboy";
    public static final String SHEETS = "sheets";
    public static final String STANDARD = "standard";
    public static final String WATER = "water";
    public static final String WASHING_LIQUID = "washingLiquid";
    public static final String SOFTENING = "softening";
    public static final String DISINFECTION_BEFORE = "disinfectionBefore";
    public static final String DISINFECTION_ING = "disinfectionIng";
    // 串口工具类
    private SerialPortUtil serialPortUtil;
    // 数据库连接工具
    private SQLiteDbHelper sqLiteDbHelper;
    // 声明soundPool
    private SoundPool soundPool;
    // 定义一个整型用load(),来设置soundID
    private int music;
    // 选择洗衣机
    private WashingMachine machine;
    // FrameLayout变量
    private FrameLayout frameLayout;
    // 图片按钮定义
    private ImageButton imageButton;
    // 显示框按钮定义
    private TextView textView;
    // En.选择
    private String En = "Thai";
    // 移动支付价格
    private BigDecimal mobilePrice;
    // 硬币支付价格
    private BigDecimal coinPrice;
    // 价格继续数组
    private Set<String> priceSet = new HashSet<>();

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        serialPortUtil.closeSerialPort();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b09);

        //注册EventBus
        EventBus.getDefault().register(this);

        // 加载声音
        initSound();
        // 置灰辅料
        ashAccessories();

        Intent i = getIntent();
        machine = i.getParcelableExtra("WashingMachine");
        En = i.getStringExtra("En");

        // 返回键
        TextView numView = findViewById(R.id.b09_machine_num);
        numView.setText(Integer.toString(machine.getNum()));
        numView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 进入B界面
                        Intent i = new Intent(B09Activity.this, A09Activity.class);
                        startActivity(i);
                        break;
                }
                return false;
            }
        });

        // 确定键
        findViewById(R.id.b09_sure).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 置灰所有按键
                        ashAllButton();
                        // 随机位置显示付款二维码

                        // 打开串口，启动投币箱
                        serialPortUtil = new SerialPortUtil();
                        serialPortUtil.openSerialPort();
                        serialPortUtil.sendSerialPort(CmdConstance.START_COIN);
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_drying).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 置灰辅料
                        ashAccessories();
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(DRYING);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_drying, DRYING);
                        // 计算价格
                        calculatedPrice();
                        // 激活辅料按钮
                        ashButton(R.id.bt_disinfection_before, getResources().getColor(R.color.green), true);
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_rinse).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 置灰辅料
                        ashAccessories();
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(RINSE);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_rinse, RINSE);
                        // 计算价格
                        calculatedPrice();
                        // 激活辅料按钮
                        ashButton(R.id.bt_disinfection_before, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_disinfection_ing, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_softening, getResources().getColor(R.color.green), true);
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_cowboy).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 置灰辅料
                        ashAccessories();
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(COWBOY);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_cowboy, COWBOY);
                        // 计算价格
                        calculatedPrice();
                        // 激活辅料按钮
                        ashButton(R.id.bt_disinfection_before, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_disinfection_ing, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_softening, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_washing_liquid, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_water, getResources().getColor(R.color.green), true);
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_sheets).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 置灰辅料
                        ashAccessories();
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(SHEETS);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_sheets, SHEETS);
                        // 计算价格
                        calculatedPrice();
                        // 激活辅料按钮
                        ashButton(R.id.bt_disinfection_before, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_disinfection_ing, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_softening, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_washing_liquid, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_water, getResources().getColor(R.color.green), true);
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_standard).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 置灰辅料
                        ashAccessories();
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(STANDARD);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_standard, STANDARD);
                        // 计算价格
                        calculatedPrice();
                        // 激活辅料按钮
                        ashButton(R.id.bt_disinfection_before, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_disinfection_ing, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_softening, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_washing_liquid, getResources().getColor(R.color.green), true);
                        ashButton(R.id.bt_water, getResources().getColor(R.color.green), true);
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_water).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        String isPress = "on";
                        if (priceSet.size() > 0) {
                            Iterator<String> it = priceSet.iterator();
                            while (it.hasNext()) {
                                if (WATER.equals(it.next())) {
                                    isPress = "up";
                                    it.remove();
                                }
                            }
                        }
                        if ("on".equals(isPress)) {
                            // 选择
                            priceSet.add(WATER);
                            v.setBackgroundColor(getResources().getColor(R.color.red));
                        } else {
                            // 取消选择
                            v.setBackgroundColor(getResources().getColor(R.color.green));
                        }
                        // 计算价格
                        calculatedPrice();
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_washing_liquid).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        String isPress = "on";
                        if (priceSet.size() > 0) {
                            Iterator<String> it = priceSet.iterator();
                            while (it.hasNext()) {
                                if (WASHING_LIQUID.equals(it.next())) {
                                    isPress = "up";
                                    it.remove();
                                }
                            }
                        }
                        if ("on".equals(isPress)) {
                            // 选择
                            priceSet.add(WASHING_LIQUID);
                            v.setBackgroundColor(getResources().getColor(R.color.red));
                        } else {
                            // 取消选择
                            v.setBackgroundColor(getResources().getColor(R.color.green));
                        }
                        // 计算价格
                        calculatedPrice();
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_softening).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        String isPress = "on";
                        if (priceSet.size() > 0) {
                            Iterator<String> it = priceSet.iterator();
                            while (it.hasNext()) {
                                if (SOFTENING.equals(it.next())) {
                                    isPress = "up";
                                    it.remove();
                                }
                            }
                        }
                        if ("on".equals(isPress)) {
                            // 选择
                            priceSet.add(SOFTENING);
                            v.setBackgroundColor(getResources().getColor(R.color.red));
                        } else {
                            // 取消选择
                            v.setBackgroundColor(getResources().getColor(R.color.green));
                        }
                        // 计算价格
                        calculatedPrice();
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_disinfection_before).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        String isPress = "on";
                        if (priceSet.size() > 0) {
                            Iterator<String> it = priceSet.iterator();
                            while (it.hasNext()) {
                                if (DISINFECTION_BEFORE.equals(it.next())) {
                                    isPress = "up";
                                    it.remove();
                                }
                            }
                        }
                        if ("on".equals(isPress)) {
                            // 选择
                            priceSet.add(DISINFECTION_BEFORE);
                            v.setBackgroundColor(getResources().getColor(R.color.red));
                        } else {
                            // 取消选择
                            v.setBackgroundColor(getResources().getColor(R.color.green));
                        }
                        // 计算价格
                        calculatedPrice();
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_disinfection_ing).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        String isPress = "on";
                        if (priceSet.size() > 0) {
                            Iterator<String> it = priceSet.iterator();
                            while (it.hasNext()) {
                                if (DISINFECTION_ING.equals(it.next())) {
                                    isPress = "up";
                                    it.remove();
                                }
                            }
                        }
                        if ("on".equals(isPress)) {
                            // 选择
                            priceSet.add(DISINFECTION_ING);
                            v.setBackgroundColor(getResources().getColor(R.color.red));
                        } else {
                            // 取消选择
                            v.setBackgroundColor(getResources().getColor(R.color.green));
                        }
                        // 计算价格
                        calculatedPrice();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 置灰所有按钮
     */
    private void ashAllButton() {
        // 置灰辅料键
        ashAccessories();
        // 置灰洗衣模式
        ashButton(R.id.bt_standard, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.bt_sheets, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.bt_cowboy, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.bt_rinse, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.bt_drying, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.b09_machine_num, getResources().getColor(R.color.accessories), false);
    }

    /**
     * 置灰辅料键
     */
    private void ashAccessories() {
        // 置灰辅料键
        ashButton(R.id.bt_water, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.bt_washing_liquid, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.bt_disinfection_before, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.bt_disinfection_ing, getResources().getColor(R.color.accessories), false);
        ashButton(R.id.bt_softening, getResources().getColor(R.color.accessories), false);
    }

    /**
     * 洗衣模式选择及计算价格
     *
     * @param btId
     * @param model
     */
    private void cancelPriceButton(int btId, String model) {
        int[] ints = {R.id.bt_drying,
                R.id.bt_rinse,
                R.id.bt_cowboy,
                R.id.bt_sheets,
                R.id.bt_standard};
        for (int i : ints) {
            if (i != btId) {
                findViewById(i).setBackgroundColor(getResources().getColor(R.color.green));
            }
        }
        String[] models = {DRYING, RINSE, COWBOY, SHEETS, STANDARD};
        for (String s : models) {
            if (!model.equals(s)) {
                Iterator<String> it = priceSet.iterator();
                while (it.hasNext()) {
                    if (s.equals(it.next())) {
                        it.remove();
                    }
                }
            }
        }
    }

    /**
     * 清空辅料选择
     */
    private void cancelAccessories() {
        String[] models = {WATER, WASHING_LIQUID, SOFTENING, DISINFECTION_BEFORE, DISINFECTION_ING};
        for (String s : models) {
            Iterator<String> it = priceSet.iterator();
            while (it.hasNext()) {
                if (s.equals(it.next())) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 计算价格
     */
    private void calculatedPrice() {
        mobilePrice = new BigDecimal("0");
        coinPrice = new BigDecimal("0");
        if (priceSet.size() > 0) {
            for (String s : priceSet) {
                switch (s) {
                    case DRYING:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getDryingPriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getDryingPriceCoin()));
                        break;
                    case RINSE:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getRinsePriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getRinsePriceCoin()));
                        break;
                    case COWBOY:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getCowboyPriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getCowboyPriceCoin()));
                        break;
                    case SHEETS:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getSheetsPriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getSheetsPriceCoin()));
                        break;
                    case STANDARD:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getStandardPriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getStandardPriceCoin()));
                        break;
                    case WATER:
                        mobilePrice = mobilePrice.add(new BigDecimal("9.5"));
                        coinPrice = coinPrice.add(new BigDecimal("10.0"));
                        break;
                    case WASHING_LIQUID:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getWashingLiquidPriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getWashingLiquidPriceCoin()));
                        break;
                    case SOFTENING:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getSofteningPriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getSofteningPriceCoin()));
                        break;
                    case DISINFECTION_BEFORE:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getDisinfectionBeforePriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getDisinfectionBeforePriceCoin()));
                        break;
                    case DISINFECTION_ING:
                        mobilePrice = mobilePrice.add(new BigDecimal(machine.getDisinfectionIngPriceMobile()));
                        coinPrice = coinPrice.add(new BigDecimal(machine.getDisinfectionIngPriceCoin()));
                        break;
                }
            }
        }
        textView = findViewById(R.id.mobile_price);
        textView.setText(mobilePrice.toString());
        textView = findViewById(R.id.coin_price);
        textView.setText(coinPrice.toString());
    }

    /**
     * 按钮置灰加不可点击
     *
     * @param bt_id
     * @param color
     * @param enable
     */
    private void ashButton(int bt_id, int color, boolean enable) {
        findViewById(bt_id).setBackgroundColor(color);
        findViewById(bt_id).setEnabled(enable);
    }

    /**
     * 弹框提示
     *
     * @param tips
     * @param msg
     */
    private void alertMsg(String tips, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(B09Activity.this);
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
        Log.d("B09Activity", "获取到了从传感器发送到Android主板的串口数据：" + string);
        BigDecimal price = new BigDecimal("0");
        switch (string) {
            case CmdConstance.FIVE_THAI_BAHT:
                calculatedPrice(price, "5");
                break;
            case CmdConstance.TEN_THAI_BAHT:
                calculatedPrice(price, "10");
                break;
            case CmdConstance.ONE_EURO:
                calculatedPrice(price, "30");
                break;
            case CmdConstance.ONE_DOLLAR:
                calculatedPrice(price, "30");
                break;
        }
    }

    /**
     * 接收投币箱币值命令，计算显示价格
     *
     * @param price
     * @param coinPrice
     */
    private void calculatedPrice(BigDecimal price, String coinPrice) {
        String showPrice;
        price = price.add(new BigDecimal(coinPrice));
        this.coinPrice = this.coinPrice.subtract(price);
        if (this.coinPrice.compareTo(new BigDecimal("0")) == 1) {
            showPrice = this.coinPrice.toString();
        } else {
            showPrice = "0";
        }
        textView = findViewById(R.id.coin_price);
        textView.setText(showPrice);
        if (price.compareTo(this.coinPrice) == -1) {
            serialPortUtil.sendSerialPort(CmdConstance.CONTINUE_COIN);
        } else {
            serialPortUtil.sendSerialPort(CmdConstance.STOP_COIN);
        }
    }
}
