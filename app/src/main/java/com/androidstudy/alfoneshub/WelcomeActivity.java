//package com.androidstudy.alfoneshub;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.os.Build;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.androidstudy.alfoneshub.utils.Config;
//
//import su.levenetc.android.textsurface.Text;
//import su.levenetc.android.textsurface.TextBuilder;
//import su.levenetc.android.textsurface.TextSurface;
//import su.levenetc.android.textsurface.animations.Alpha;
//import su.levenetc.android.textsurface.animations.Delay;
//import su.levenetc.android.textsurface.animations.Parallel;
//import su.levenetc.android.textsurface.animations.Sequential;
//import su.levenetc.android.textsurface.animations.ShapeReveal;
//import su.levenetc.android.textsurface.animations.SideCut;
//import su.levenetc.android.textsurface.animations.Slide;
//import su.levenetc.android.textsurface.animations.TransSurface;
//import su.levenetc.android.textsurface.contants.Align;
//import su.levenetc.android.textsurface.contants.Pivot;
//import su.levenetc.android.textsurface.contants.Side;
//
//public class WelcomeActivity extends AppCompatActivity {
//    private TextSurface textSurface;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_welcome);
//
//        SharedPreferences preferences = WelcomeActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        Boolean isFirstTimeLaunch = preferences.getBoolean(Config.IS_FIRST_TIME_LAUNCH_SHARED_PREF, false);
//
//        changeStatusBarColor();
//        if(getSupportActionBar() != null)
//        {
//            getSupportActionBar().setElevation(0);
//        }
//
//        if (isFirstTimeLaunch) {
//            startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
//            finish();
//        }else{
//            Thread timer = new Thread(){
//                public void run(){
//                    try{
//                        sleep(7500);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }finally {
//                        //Save data to shared preferences
//                        SharedPreferences sharedPreferences = WelcomeActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//                        //Creating editor to store values to shared preferences
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean(Config.IS_FIRST_TIME_LAUNCH_SHARED_PREF, true);
//                        editor.commit();
//
//                        startActivity(new Intent(WelcomeActivity.this,LoginActivity.class));
//                        finish();
//                    }
//                }
//            };
//            timer.start();
//        }
//
//
//
//
//        textSurface = (TextSurface)findViewById(R.id.text_surface);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//
//        Text textDaai = TextBuilder
//                .create("Hi")
//                .setPaint(paint)
//                .setSize(64)
//                .setAlpha(0)
//                .setColor(Color.WHITE)
//                .setPosition(Align.SURFACE_CENTER).build();
//
//        Text textBraAnies = TextBuilder
//                .create("I'm Alfones Admin")
//                .setPaint(paint)
//                .setSize(30)
//                .setAlpha(0)
//                .setColor(Color.RED)
//                .setPosition(Align.BOTTOM_OF, textDaai).build();
//
//
//        Text textDaaiAnies = TextBuilder
//                .create("How about you?")
//                .setPaint(paint)
//                .setSize(40)
//                .setAlpha(0)
//                .setColor(Color.WHITE)
//                .setPosition(Align.BOTTOM_OF, textBraAnies).build();
//
//
//        textSurface.play(
//                new Sequential(
//                        ShapeReveal.create(textDaai, 750, SideCut.show(Side.LEFT), false),
//                        new Parallel(ShapeReveal.create(textDaai, 600, SideCut.hide(Side.LEFT), false), new Sequential(Delay.duration(300), ShapeReveal.create(textDaai, 600, SideCut.show(Side.LEFT), false))),
//                        new Parallel(new TransSurface(900, textBraAnies, Pivot.CENTER), ShapeReveal.create(textBraAnies, 1300, SideCut.show(Side.LEFT), false)),
//                        Alpha.hide(textDaai, 500),
//                        Delay.duration(900),
//                        new Parallel(TransSurface.toCenter(textDaaiAnies, 900), Slide.showFrom(Side.BOTTOM, textDaaiAnies, 900)),
//                        Alpha.hide(textBraAnies, 700),
//                        Alpha.hide(textDaaiAnies, 1500)
//
//                )
//        );
//
//
//    }
//
//    /**
//     * Making notification bar transparent
//     */
//    private void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.BLACK);
//        }
//    }
//}
