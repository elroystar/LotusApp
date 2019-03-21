package com.lotus.lotusapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lotus.lotusapp.db.SQLiteDbHelper;
import com.lotus.lotusapp.dto.User;
import com.lotus.lotusapp.dto.WashingMachine;
import com.lotus.lotusapp.utils.LongClickUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class A09Activity extends Activity {

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
    // 图片按钮定义
    private ImageButton imageButton;
    // En.选择
    private String En = "Thai";
    // 定义RequestOptions
    private RequestOptions requestOptions = new RequestOptions()
            .error(R.drawable.ic_launcher_background);
    // 数字输入框字符串
    private String stringTx = "";
    // User实体类定义
    private User user = new User();
    // 图片存放路径
    private static String thaiFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "data" + File.separator + "lotus" + File.separator + "a09";
    private static String enFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "data" + File.separator + "lotus" + File.separator + "a09";

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
        // 加载数字键盘图案
        loadNumBtn();
        // 长按logo进入M界面
        LongClickUtils.setLongClick(new Handler(), findViewById(R.id.logo), 1000, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 进入M界面
                Intent i = new Intent(A09Activity.this, M09Activity.class);
                i.putExtra("En", En);
                startActivity(i);
                return false;
            }
        });
        // 数字键盘区按钮逻辑
        functionNumButton();
        // 洗衣机按钮区逻辑
        functionMachine();
    }

    private void functionMachine() {
        if (findViewById(R.id.machine_1) != null) {
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
                            i.putExtra("washingMachine", washingMachines.get(0));
                            i.putExtra("en", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_2) != null) {
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
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_3) != null) {
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
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_4) != null) {
            findViewById(R.id.machine_4).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(3));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_5) != null) {
            findViewById(R.id.machine_5).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(4));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_6) != null) {
            findViewById(R.id.machine_6).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(5));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_7) != null) {
            findViewById(R.id.machine_7).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(6));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_8) != null) {
            findViewById(R.id.machine_8).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(7));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_9) != null) {
            findViewById(R.id.machine_9).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(8));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_10) != null) {
            findViewById(R.id.machine_10).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(9));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_11) != null) {
            findViewById(R.id.machine_11).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(10));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_12) != null) {
            findViewById(R.id.machine_12).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(11));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_13) != null) {
            findViewById(R.id.machine_13).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(12));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_14) != null) {
            findViewById(R.id.machine_14).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(13));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_15) != null) {
            findViewById(R.id.machine_15).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(14));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_16) != null) {
            findViewById(R.id.machine_16).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(15));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }

        if (findViewById(R.id.machine_17) != null) {
            findViewById(R.id.machine_17).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //按下
                            // 播放按键声音
                            playSound();
                            // 进入B界面
                            Intent i = new Intent(A09Activity.this, B09Activity.class);
                            i.putExtra("WashingMachine", washingMachines.get(16));
                            i.putExtra("En", En);
                            i.putExtra("user", user);
                            startActivity(i);
                            break;
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 数字区键盘逻辑
     */
    private void functionNumButton() {
        // 数字键
        findViewById(R.id.ib_num_0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_0, "0", "ib_num_0.png");
            }
        });
        findViewById(R.id.ib_num_1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_1, "1", "ib_num_1.png");
            }
        });
        findViewById(R.id.ib_num_2).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_2, "2", "ib_num_2.png");
            }
        });
        findViewById(R.id.ib_num_3).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_3, "3", "ib_num_3.png");
            }
        });
        findViewById(R.id.ib_num_4).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_4, "4", "ib_num_4.png");
            }
        });
        findViewById(R.id.ib_num_5).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_5, "5", "ib_num_5.png");
            }
        });
        findViewById(R.id.ib_num_6).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_6, "6", "ib_num_6.png");
            }
        });
        findViewById(R.id.ib_num_7).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_7, "7", "ib_num_7.png");
            }
        });
        findViewById(R.id.ib_num_8).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_8, "8", "ib_num_8.png");
            }
        });
        findViewById(R.id.ib_num_9).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return onTouchNumFunction(v, event, R.id.ib_num_9, "9", "ib_num_9.png");
            }
        });
        // Esc键
        findViewById(R.id.ib_num_esc).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        // 重新设置按下时的背景图片
//                        loadImage(v.getId(), this, thaiFilePath + File.separator + "cg", "ib_num_OK.png");
                        for (int i = 0; i < 10; i++) {
                            // 获取textView id
                            int tx_num_id = getResources().getIdentifier("tx_num_" + i, "id", getPackageName());
                            TextView tv = findViewById(tx_num_id);
                            tv.setText("");
                        }
                        stringTx = "";
                        break;
                    case MotionEvent.ACTION_UP:

                        break;
                }
                return false;
            }
        });
        // OK键
        findViewById(R.id.ib_num_ok).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:
                        // 播放按键声音
                        playSound();
                        if (stringTx.length() == 10) {
                            // 查询数据库
                            sqLiteDbHelper = new SQLiteDbHelper(getApplicationContext());
                            SQLiteDatabase dbRead = sqLiteDbHelper.getReadableDatabase();
                            SQLiteDatabase dbWrit = sqLiteDbHelper.getWritableDatabase();
                            try {
                                // select * from user where phone = 'stringTx'
                                Cursor cursor = dbRead.query(SQLiteDbHelper.TABLE_USER,
                                        null,
                                        "phone = ?",
                                        new String[]{stringTx},
                                        null,
                                        null,
                                        null);
                                if (cursor.getCount() > 0) {
                                    // user实体赋值
                                    while (cursor.moveToNext()) {
                                        user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                                        user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                                        user.setRewardNum(cursor.getInt(cursor.getColumnIndex("reward_num")));
                                        user.setRewardTotal(cursor.getInt(cursor.getColumnIndex("reward_total")));
                                        user.setWashingNum(cursor.getInt(cursor.getColumnIndex("washing_num")));
                                        user.setWashingTotal(cursor.getInt(cursor.getColumnIndex("washing_total")));
                                    }
                                    // 显示洗衣次数及奖励
                                    TextView tv = findViewById(R.id.tx_washing);
                                    tv.setText(Integer.toString(user.getWashingNum()));
                                } else {
                                    // 自动注册
                                    user.setPhone(stringTx);
                                    user.setRewardNum(0);
                                    user.setRewardTotal(0);
                                    user.setWashingNum(0);
                                    user.setWashingTotal(0);
                                    dbWrit.execSQL("insert into user(phone) values ('" + stringTx + "')");
                                    // 显示洗衣次数及奖励
                                    TextView tv = findViewById(R.id.tx_washing);
                                    tv.setText("0");
                                }
                            } finally {
                                dbRead.close();
                                dbWrit.close();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:

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
                case 10:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_j);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_q);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_10);
                    break;
                case 11:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_m);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_e);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_n);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    frameLayout = findViewById(R.id.frame_machine_q);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_10);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_11);
                    break;
                case 12:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_m);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_n);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_10);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_11);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_12);
                    break;
                case 13:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_m);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_e);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_n);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_10);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_11);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_12);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_13);
                    break;
                case 14:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_j);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_b);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_10);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_11);
                    frameLayout = findViewById(R.id.frame_machine_h);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_12);
                    frameLayout = findViewById(R.id.frame_machine_q);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_13);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_14);
                    break;
                case 15:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_j);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_b);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_e);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_10);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_11);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_12);
                    frameLayout = findViewById(R.id.frame_machine_h);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_13);
                    frameLayout = findViewById(R.id.frame_machine_q);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_14);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_15);
                    break;
                case 16:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_j);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_b);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_m);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_n);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_10);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_11);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_12);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_13);
                    frameLayout = findViewById(R.id.frame_machine_h);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_14);
                    frameLayout = findViewById(R.id.frame_machine_q);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_15);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_16);
                    break;
                case 17:
                    frameLayout = findViewById(R.id.frame_machine_a);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_1);
                    frameLayout = findViewById(R.id.frame_machine_j);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_2);
                    frameLayout = findViewById(R.id.frame_machine_b);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_3);
                    frameLayout = findViewById(R.id.frame_machine_k);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_4);
                    frameLayout = findViewById(R.id.frame_machine_c);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_5);
                    frameLayout = findViewById(R.id.frame_machine_l);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_6);
                    frameLayout = findViewById(R.id.frame_machine_d);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_7);
                    frameLayout = findViewById(R.id.frame_machine_m);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_8);
                    frameLayout = findViewById(R.id.frame_machine_e);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_9);
                    frameLayout = findViewById(R.id.frame_machine_n);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_10);
                    frameLayout = findViewById(R.id.frame_machine_f);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_11);
                    frameLayout = findViewById(R.id.frame_machine_o);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_12);
                    frameLayout = findViewById(R.id.frame_machine_g);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_13);
                    frameLayout = findViewById(R.id.frame_machine_p);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_14);
                    frameLayout = findViewById(R.id.frame_machine_h);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_15);
                    frameLayout = findViewById(R.id.frame_machine_q);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_16);
                    frameLayout = findViewById(R.id.frame_machine_i);
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayout.getChildAt(0).setId(R.id.machine_17);
                    break;
            }
        }
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
            } else {
//                alertMsg("Error", "没有找到可以用的洗衣机！");
            }
        } finally {
            // 关闭数据库连接
            dbRead.close();
        }
    }

    /**
     * 加载数字键
     */
    private void loadNumBtn() {
        // 数字按钮区图片
        for (int i = 0; i < 10; i++) {
            // 获取按钮id
            int ib_id = getResources().getIdentifier("ib_num_" + i, "id", getPackageName());
            imageButton = findViewById(ib_id);
            //加载图片
            Glide.with(this)
                    .load(new File(thaiFilePath + File.separator + "cg", "ib_num_" + i + ".png"))
                    .apply(requestOptions)
                    .into(imageButton);
        }
//        loadImage(R.id.ib_num_en, this, thaiFilePath + File.separator + "cg", "ib_num_En.png");
        loadImage(R.id.ib_num_esc, this, thaiFilePath + File.separator + "cg", "ib_num_Esc.png");
        loadImage(R.id.ib_num_ok, this, thaiFilePath + File.separator + "cg", "ib_num_OK.png");
    }

    /**
     * 加载图片
     *
     * @param ib_washing
     * @param activity
     * @param filePath
     * @param imgStr
     */
    private void loadImage(int ib_washing, Activity activity, String filePath, String imgStr) {
        imageButton = findViewById(ib_washing);
        Glide.with(activity)
                .load(new File(filePath, imgStr))
                .apply(requestOptions)
                .into(imageButton);
    }

    /**
     * 数字键触摸逻辑控制
     *
     * @param v
     * @param event
     * @param ib_num
     * @param txStr
     * @param imgStr
     * @return
     */
    private boolean onTouchNumFunction(View v, MotionEvent event, int ib_num, String txStr, String imgStr) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 播放按键声音
            playSound();
            // 重新设置按下时的背景图片
            loadImage(ib_num, this, thaiFilePath + File.separator + "cg", "ib_num_OK.png");
            if (stringTx.length() == 0) {
                TextView tv = findViewById(R.id.tx_num_0);
                stringTx = "0";
                tv.setText("0");
            } else if (stringTx.length() >= 1 && stringTx.length() < 10) {
                // 获取textView id
                int tx_num_id = getResources().getIdentifier("tx_num_" + stringTx.length(), "id", getPackageName());
                TextView tv = findViewById(tx_num_id);
                tv.setText(txStr);
                stringTx = stringTx + txStr;
            } else {
                for (int i = 1; i < 10; i++) {
                    // 获取textView id
                    int tx_num_id = getResources().getIdentifier("tx_num_" + i, "id", getPackageName());
                    TextView tv = findViewById(tx_num_id);
                    tv.setText("");
                }
                stringTx = "0";
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            // 再修改为抬起时的正常图片
            loadImage(ib_num, this, thaiFilePath + File.separator + "cg", imgStr);
        }
        return false;
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
