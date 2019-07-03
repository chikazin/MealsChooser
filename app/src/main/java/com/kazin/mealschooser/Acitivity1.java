package com.kazin.mealschooser;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Acitivity1 extends AppCompatActivity {
    String[] menu = {"酸辣土豆丝","煎蛋","鱼香茄子","啤酒鸭","麻婆豆腐","宫保鸡丁","手撕包菜","剁椒鱼头","粉蒸肉","锅包肉","麻辣香锅","红烧牛肉","辣子鸡","牛肉炖土豆","糖醋鲤鱼","干煸豆角","烧茄子","炖排骨","木须肉","香辣虾","红烧狮子头","小鸡炖蘑菇","糖醋里脊","土豆炖牛肉","板栗烧鸡","糖醋鱼肉丸","梅菜扣肉","京酱肉丝","红烧带鱼","大盘鸡","红烧鸡翅","醋溜白菜","香辣蟹","地三鲜","东坡肉"};
    Random re = new Random();

    List<String> pracList = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acitivity1);


        // TODO(kazin) 下面一句可以禁止截屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        final TextView  result =(TextView)findViewById(R.id.result);
        final ListView list1 = (ListView) findViewById(R.id.list1);
        final ImageView image12 =(ImageView)findViewById(R.id.image1);

        //        makedir();
        List tempList = read();
        try{
            if(tempList.size() == 0){
                tempList = new ArrayList();
                tempList = Arrays.asList(menu);
                // TODO(kazin)写成 tempList = Arrays.aList(menu) 会导致list.add("新数据") 时抛出不支持运算异常(java.lang.UnsupportedOperationException)

            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("line58",""+e);
            if(tempList == null){
                tempList = new ArrayList();
                tempList = Arrays.asList(menu);
            }
        }

        pracList = Collections.emptyList();
        pracList = new ArrayList<String>(tempList);
        // TODO(kazin) 对Arrays.asList()返回的抽象类进行转型ArrayList<String>的转型, 使之具有add和remove方法

        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
                Acitivity1.this, android.R.layout.simple_list_item_1, pracList);
        list1.setAdapter(adapter1);

        list1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder dialog1 = new AlertDialog.Builder(Acitivity1.this);
                final String item1 = parent.getItemAtPosition(position).toString();
                dialog1.setTitle("Warning! ");
                dialog1.setMessage("Are you sure you want to delete this item（"+item1+"）? " );
                dialog1.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO 删除操作

                        pracList.remove(position);
                        save(pracList);
                        Log.e("line84", ""+pracList);
                        adapter1.notifyDataSetChanged();
                        Toast b = Toast.makeText(Acitivity1.this, item1+"删除成功", Toast.LENGTH_LONG);
                        showMyToast(b, 800);

                    }
                });
                dialog1.setNegativeButton("CANCEL", null);
                dialog1.create().show();
                return false;
            }
        });

        Button button1 =(Button)findViewById(R.id.button);


        button1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder dialong2 = new AlertDialog.Builder(Acitivity1.this);
                final EditText nextOption = new EditText(Acitivity1.this);
                nextOption.setFocusable(true);
                nextOption.setFocusableInTouchMode(true);
                nextOption.requestFocus();
                dialong2.setTitle("Enter a new option you want add to the option list: ")
                        .setView(nextOption)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String option2 = nextOption.getText().toString();
                        pracList.add(option2);
                        save(pracList);
                        adapter1.notifyDataSetChanged();
                        Toast c = Toast.makeText(Acitivity1.this, option2+"添加成功", Toast.LENGTH_LONG);
                        showMyToast(c, 800);
                    }
                })
                        .setNegativeButton("Cancel", null);

                dialong2.create().show();

//              TODO(kazin)创建一个线程来同时弹出输入法
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        SystemClock.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // TODO(kazin) 下面一行防止界面被输入法挤压
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        });
                    }
                }).start();
                return false;
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pracList.size() == 0) {
                    AlertDialog.Builder dialog3 = new AlertDialog.Builder(Acitivity1.this);
                    dialog3.setTitle("Warning! ");
                    dialog3.setMessage("There is no option in the list. \nDo want to restore the initial options? ");
                    dialog3.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pracList.clear();
                            pracList.addAll(Arrays.asList(menu));
                            adapter1.notifyDataSetChanged();
                            int index = re.nextInt(pracList.size());
                            String result1 = (String) pracList.get(index);
                            result.setText(result1);
                            Toast d = Toast.makeText(Acitivity1.this, "学弟推荐(^_−)☆: " + result1, Toast.LENGTH_LONG);
                            showMyToast(d, 800);
                        }
                    });
                    dialog3.setNegativeButton("NO", null);
                    dialog3.create().show();
                }else{
                    int index = re.nextInt(pracList.size());
                    String result1 = (String) pracList.get(index);
                    result.setText(result1);
                    Toast d = Toast.makeText(Acitivity1.this, "学弟推荐(^_−)☆: " + result1, Toast.LENGTH_LONG);
                    showMyToast(d, 800);
                }
            }
        });
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        save(pracList);
    }

    public void save(List list){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try{
            String innerStorage = getApplicationContext().getFilesDir().getAbsolutePath()+"/data";
            fos = new FileOutputStream(innerStorage);
            oos =new ObjectOutputStream(fos);
            oos.writeObject(list);
            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("line173", "写入失败"+e);
        }
    }

    public List<String> read() {
        List<String> list = new ArrayList<String>();
        try {
            String innerStorage = getApplicationContext().getFilesDir().getAbsolutePath()+"/data";
            FileInputStream fis = new FileInputStream(innerStorage);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (List<String>) ois.readObject();
            fis.close();
            ois.close();
            return list;
        }catch (IOException e){
            e.printStackTrace();
            Log.e("line188", "读取失败");
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void makedir() {
        File file = null;
        try {
            file = new File("/storage/sdcard0/MealsChooser");
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e+"");
        }
    }

    public void showMyToast(final Toast toast, final int cnt) {
        final Timer timer =new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toast.show();
            }
        },0,3000);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                toast.cancel();
                timer.cancel();
            }
        }, cnt );
    }
}
