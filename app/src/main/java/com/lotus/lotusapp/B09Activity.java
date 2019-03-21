package com.lotus.lotusapp;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lotus.lotusapp.constance.CmdConstance;
import com.lotus.lotusapp.db.SQLiteDbHelper;
import com.lotus.lotusapp.dto.User;
import com.lotus.lotusapp.dto.WashingMachine;
import com.lotus.lotusapp.utils.SerialPortUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class B09Activity extends Activity {

    public static final String DRYING = "drying";
    public static final String RINSE = "rinse";
    public static final String COWBOY = "cowboy";
    public static final String SHEETS = "sheets";
    public static final String STANDARD = "standard";
    public static final String FREE = "free";
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
    private BigDecimal price = new BigDecimal("0");
    // 价格继续数组
    private Set<String> priceSet = new HashSet<>();
    // User实体类定义
    private User user = new User();
    // 定义handler对象
    private Handler handler = new Handler();
    // 是否免费
    private Boolean isFree = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialPortUtil.closeSerialPort();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b09);
        price = new BigDecimal("0");
        //注册EventBus
        EventBus.getDefault().register(this);
        // 加载声音
        initSound();
        Intent i = getIntent();
        machine = i.getParcelableExtra("WashingMachine");
        En = i.getStringExtra("En");
        user = i.getParcelableExtra("user");
        if (null != user.getWashingNum()) {
            if (user.getWashingNum() == 10) {
                machine.setStandardPriceCoin("00.0");
                machine.setStandardPriceMobile("00.0");
                machine.setRinsePriceCoin("00.0");
                machine.setRinsePriceMobile("00.0");
                machine.setDryingPriceCoin("00.0");
                machine.setDryingPriceMobile("00.0");
                machine.setCowboyPriceCoin("00.0");
                machine.setCowboyPriceMobile("00.0");
                machine.setSheetsPriceCoin("00.0");
                machine.setSheetsPriceMobile("00.0");
                isFree = true;
            }
        }
        // 默认标准和送洗衣机默认选中
        textView = findViewById(R.id.bt_standard);
        priceSet.add(STANDARD);
        textView.setBackgroundColor(getResources().getColor(R.color.red));
        cancelPriceButton(R.id.bt_standard, STANDARD);
        textView = findViewById(R.id.bt_free);
        priceSet.add(FREE);
        textView.setBackgroundColor(getResources().getColor(R.color.red));
        // 计算价格
        calculatedPrice();
        // 根据价格显示辅料按钮
        displayAccessories();
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
                        finish();
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
                        // 判断价格是否为0
                        if (isFree) {
                            sendWashingCmd();
                        } else {
                            // TODO: 2019/3/19 随机位置显示付款二维码

                            // 打开串口，启动投币箱
                            serialPortUtil = new SerialPortUtil();
                            serialPortUtil.openSerialPort();
                            serialPortUtil.sendSerialPort(CmdConstance.START_COIN);
                            handler.postDelayed(returnA09, 10000);
                        }
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
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(DRYING);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_drying, DRYING);
                        // 计算价格
                        calculatedPrice();
                        // 显示全部辅料
                        displayAccessoriesAll();
                        if (priceSet.size() > 0) {
                            Iterator<String> it = priceSet.iterator();
                            while (it.hasNext()) {
                                if (FREE.equals(it.next())) {
                                    it.remove();
                                }
                            }
                        }
                        frameLayout = findViewById(R.id.fl_free);
                        frameLayout.setVisibility(View.INVISIBLE);
                        frameLayout = findViewById(R.id.fl_washing_liquid);
                        frameLayout.setVisibility(View.INVISIBLE);
                        frameLayout = findViewById(R.id.fl_softening);
                        frameLayout.setVisibility(View.INVISIBLE);
                        frameLayout = findViewById(R.id.fl_disinfection_ing);
                        frameLayout.setVisibility(View.INVISIBLE);
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
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(RINSE);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_rinse, RINSE);
                        // 计算价格
                        calculatedPrice();
                        // 显示全部辅料
                        displayAccessoriesAll();
                        if (priceSet.size() > 0) {
                            Iterator<String> it = priceSet.iterator();
                            while (it.hasNext()) {
                                if (FREE.equals(it.next())) {
                                    it.remove();
                                }
                            }
                        }
                        frameLayout = findViewById(R.id.fl_free);
                        frameLayout.setVisibility(View.INVISIBLE);
                        frameLayout = findViewById(R.id.fl_washing_liquid);
                        frameLayout.setVisibility(View.INVISIBLE);
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
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(COWBOY);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_cowboy, COWBOY);
                        // 计算价格
                        calculatedPrice();
                        // 显示全部辅料
                        displayAccessoriesAll();
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
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(SHEETS);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_sheets, SHEETS);
                        // 计算价格
                        calculatedPrice();
                        // 显示全部辅料
                        displayAccessoriesAll();
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
                        // 清空辅料选择
                        cancelAccessories();
                        // 洗衣模式按钮设定
                        priceSet.add(STANDARD);
                        v.setBackgroundColor(getResources().getColor(R.color.red));
                        cancelPriceButton(R.id.bt_standard, STANDARD);
                        // 计算价格
                        calculatedPrice();
                        // 显示全部辅料
                        displayAccessoriesAll();
                        break;
                }
                return false;
            }
        });

        findViewById(R.id.bt_free).setOnTouchListener(new View.OnTouchListener() {
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
                                if (FREE.equals(it.next())) {
                                    isPress = "up";
                                    it.remove();
                                }
                            }
                        }
                        if ("on".equals(isPress)) {
                            // 选择
                            priceSet.add(FREE);
                            v.setBackgroundColor(getResources().getColor(R.color.red));
                        } else {
                            // 取消选择
                            v.setBackgroundColor(getResources().getColor(R.color.green));
                        }
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
     * 显示全部辅料
     */
    private void displayAccessoriesAll() {
        if (!"00.0".equals(machine.getDisinfectionBeforePriceCoin()) && !"00.0".equals(machine.getDisinfectionBeforePriceMobile())) {
            frameLayout = findViewById(R.id.fl_disinfection_before);
            frameLayout.setVisibility(View.VISIBLE);
        }
        if (!"00.0".equals(machine.getDisinfectionIngPriceCoin()) && !"00.0".equals(machine.getDisinfectionIngPriceMobile())) {
            frameLayout = findViewById(R.id.fl_disinfection_ing);
            frameLayout.setVisibility(View.VISIBLE);
        }
        if (!"00.0".equals(machine.getSofteningPriceCoin()) && !"00.0".equals(machine.getSofteningPriceMobile())) {
            frameLayout = findViewById(R.id.fl_softening);
            frameLayout.setVisibility(View.VISIBLE);
        }
        if (!"00.0".equals(machine.getWashingLiquidPriceCoin()) && !"00.0".equals(machine.getWashingLiquidPriceMobile())) {
            frameLayout = findViewById(R.id.fl_washing_liquid);
            frameLayout.setVisibility(View.VISIBLE);
            frameLayout = findViewById(R.id.fl_free);
            frameLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据价格显示辅料按钮
     */
    private void displayAccessories() {
        if ("00.0".equals(machine.getDisinfectionBeforePriceCoin()) && "00.0".equals(machine.getDisinfectionBeforePriceMobile())) {
            frameLayout = findViewById(R.id.fl_disinfection_before);
            frameLayout.setVisibility(View.INVISIBLE);
        }
        if ("00.0".equals(machine.getDisinfectionIngPriceCoin()) && "00.0".equals(machine.getDisinfectionIngPriceMobile())) {
            frameLayout = findViewById(R.id.fl_disinfection_ing);
            frameLayout.setVisibility(View.INVISIBLE);
        }
        if ("00.0".equals(machine.getSofteningPriceCoin()) && "00.0".equals(machine.getSofteningPriceMobile())) {
            frameLayout = findViewById(R.id.fl_softening);
            frameLayout.setVisibility(View.INVISIBLE);
        }
        if ("00.0".equals(machine.getWashingLiquidPriceCoin()) && "00.0".equals(machine.getWashingLiquidPriceMobile())) {
            frameLayout = findViewById(R.id.fl_washing_liquid);
            frameLayout.setVisibility(View.INVISIBLE);
            frameLayout = findViewById(R.id.fl_free);
            frameLayout.setVisibility(View.INVISIBLE);
        }
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
        ashButton(R.id.bt_free, getResources().getColor(R.color.accessories), false);
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
        String[] models = {WASHING_LIQUID, SOFTENING, DISINFECTION_BEFORE, DISINFECTION_ING};
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
     * 显示toast，自己定义显示长短。
     * param1:activity  传入context
     * param2:word   我们需要显示的toast的内容
     * param3:time length  long类型，我们传入的时间长度（如500）
     */
    public static void showToast(final Activity activity, final String word, final long time) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
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
        if (string.equals(CmdConstance.RESET)) {
            return;
        }
        switch (string) {
            case CmdConstance.FIVE_THAI_BAHT:
                calculatedPrice("5");
                break;
            case CmdConstance.TEN_THAI_BAHT:
                calculatedPrice("10");
                break;
            case CmdConstance.ONE_EURO:
                calculatedPrice("30");
                break;
            case CmdConstance.ONE_DOLLAR:
                calculatedPrice("30");
                break;
        }
    }

    /**
     * 接收投币箱币值命令，计算显示价格
     *
     * @param coinPrice
     */
    private void calculatedPrice(String coinPrice) {
        handler.removeCallbacks(returnA09);
        // TODO: 2019/3/19 关闭二维码支付

        String showPrice;
        price = price.add(new BigDecimal(coinPrice));
        BigDecimal p = this.coinPrice.subtract(price);
        if (p.compareTo(new BigDecimal("0")) == 1) {
            showPrice = p.toString();
        } else {
            showPrice = "0";
        }
        textView = findViewById(R.id.coin_price);
        textView.setText(showPrice);
        Log.d("B09Activity", "price：" + price.toString());
        Log.d("B09Activity", "coinPrice：" + coinPrice.toString());
        if (price.compareTo(this.coinPrice) == -1) {
            serialPortUtil.sendSerialPort(CmdConstance.CONTINUE_COIN);
        } else  {
            serialPortUtil.sendSerialPort(CmdConstance.STOP_COIN);
            // 发送洗衣指令
            sendWashingCmd();
        }
    }

    /**
     * 发送洗衣指令
     */
    private void sendWashingCmd() {
        StringBuilder modelSb = new StringBuilder("00000000");
        StringBuilder materielSb = new StringBuilder("00000000");
        // 转换洗衣指令
        for (String s : priceSet) {
            if (DISINFECTION_BEFORE.equals(s)) {
                modelSb.replace(0, 1, "1");
            }
            if (STANDARD.equals(s)) {
                modelSb.replace(1, 2, "1");
            }
            if (SHEETS.equals(s)) {
                modelSb.replace(2, 3, "1");
            }
            if (COWBOY.equals(s)) {
                modelSb.replace(3, 4, "1");
            }
            if (RINSE.equals(s)) {
                modelSb.replace(4, 5, "1");
            }
            if (DISINFECTION_ING.equals(s)) {
                materielSb.replace(2, 3, "1");
            }
            if (FREE.equals(s)) {
                materielSb.replace(3, 4, "1");
            }
            if (WASHING_LIQUID.equals(s)) {
                materielSb.replace(4, 5, "1");
            }
            if (SOFTENING.equals(s)) {
                materielSb.replace(5, 6, "1");
            }
        }
        String model = Integer.toHexString(Integer.parseInt(modelSb.toString(), 2));
        String materiel = Integer.toHexString(Integer.parseInt(materielSb.toString(), 2));
        serialPortUtil.sendSerialPort(machine.getCommand() + model + materiel);
        if (!"".equals(user.getPhone()) && null != user.getPhone()) {
            // 查询数据库
            sqLiteDbHelper = new SQLiteDbHelper(getApplicationContext());
            SQLiteDatabase dbWrit = sqLiteDbHelper.getWritableDatabase();
            try {
                int washingNum = user.getWashingNum();
                int rewardNum = user.getRewardNum();
                int rewardTotal = user.getRewardTotal();
                if (rewardNum == 1) {
                    rewardNum = 0;
                    rewardTotal++;
                }
                if (washingNum == 10) {
                    washingNum = 0;
                    rewardNum = 1;
                } else {
                    washingNum++;
                }
                dbWrit.execSQL("update user set " +
                        "washing_total = washing_total + 1, " +
                        "washing_num = " + washingNum + ", " +
                        "reward_num=" + rewardNum + ", " +
                        "reward_total = " + rewardTotal + " where phone = '" + user.getPhone() + "'");
            } finally {
                dbWrit.close();
            }
            // 显示倒计时提示框
            showToast(B09Activity.this, "请前往" + machine.getNum() + "号洗衣机洗衣！", 5000);
            /** 倒计时6秒，一次1秒 */
            countDownTimer.start();
        } else {
            // 显示倒计时提示框
            showToast(B09Activity.this, "请前往" + machine.getNum() + "号洗衣机洗衣！", 5000);
            /** 倒计时6秒，一次1秒 */
            countDownTimer.start();
        }
    }

    /**
     * CountDownTimer 实现倒计时
     */
    private CountDownTimer countDownTimer = new CountDownTimer(5 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            // TODO: 2019/3/19 关闭二维码支付

            // 返回A界面
            Intent i = new Intent(B09Activity.this, A09Activity.class);
            startActivity(i);
            finish();
        }
    };

    /**
     * 循环发送询问指令
     */
    Runnable returnA09 = new Runnable() {
        @Override
        public void run() {
            // TODO: 2019/3/19 关闭二维码支付

            // 关闭投币箱
            serialPortUtil.sendSerialPort(CmdConstance.STOP_COIN);
            // 返回A界面
            Intent i = new Intent(B09Activity.this, A09Activity.class);
            startActivity(i);
            finish();
        }
    };

}
