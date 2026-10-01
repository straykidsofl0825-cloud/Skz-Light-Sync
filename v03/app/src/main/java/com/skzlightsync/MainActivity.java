package com.skzlightsync;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.content.*;
import java.util.*;

public class MainActivity extends Activity {
    @Override public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(new LightSyncView(this));
    }
}

class LightSyncView extends View {

    final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    final Handler handler = new Handler();

    int screen = 0;
    int selectedSector = 0;

    int manualColor = Color.rgb(35,155,255);
    int effect = 0;
    boolean running = false;

    long localStartElapsed = 0L;
    long effectStart = 0L;

    final int BG = Color.rgb(5,7,13);
    final int CARD = Color.rgb(12,20,34);
    final int WHITE = Color.WHITE;
    final int MUTED = Color.rgb(155,168,192);
    final int BLUE = Color.rgb(38,155,255);
    final int PINK = Color.rgb(235,45,209);

    final int RED = Color.rgb(239,23,64);
    final int YELLOW = Color.rgb(255,210,28);
    final int GREEN = Color.rgb(50,217,107);
    final int PURPLE = Color.rgb(155,57,255);

    final int[] colors = {
        BLUE, RED, WHITE, YELLOW, GREEN, PURPLE
    };

    final String[] colorNames = {
        "AZUL", "VERMELHO", "BRANCO",
        "AMARELO", "VERDE", "ROXO"
    };

    LightSyncView(Context c) {
        super(c);
        stroke.setStyle(Paint.Style.STROKE);
        setFocusable(true);
    }

    static class Sector {
        String name;
        int color;

        Sector(String n, int c) {
            name = n;
            color = c;
        }
    }

    ArrayList<Sector> sectors = new ArrayList<>();

    {
        sectors.add(new Sector("Pista", RED));
        sectors.add(new Sector("Arquibancada A", YELLOW));
        sectors.add(new Sector("Arquibancada B", GREEN));
        sectors.add(new Sector("Cadeira", BLUE));
    }

    void txt(Canvas c, String s, float x, float y,
             float size, int color, boolean bold) {

        p.setTextSize(size);
        p.setColor(color);
        p.setTypeface(Typeface.create(
                "sans",
                bold ? Typeface.BOLD : Typeface.NORMAL
        ));

        c.drawText(s, x, y, p);
    }

    void card(Canvas c, float l, float t, float r, float b) {
        p.setColor(CARD);
        p.setStyle(Paint.Style.FILL);
        c.drawRoundRect(l,t,r,b,18,18,p);
    }

    void button(Canvas c, String s,
                float l, float t, float r, float b) {

        LinearGradient g = new LinearGradient(
                l,t,r,b,
                BLUE,
                PINK,
                Shader.TileMode.CLAMP
        );

        p.setShader(g);
        c.drawRoundRect(l,t,r,b,28,28,p);
        p.setShader(null);

        p.setTextAlign(Paint.Align.CENTER);

        txt(
            c,
            s,
            (l+r)/2,
            t+(b-t)/2+5,
            15,
            WHITE,
            true
        );

        p.setTextAlign(Paint.Align.LEFT);
    }

    void header(Canvas c, String title, boolean back) {

        if(back)
            txt(c,"‹",18,43,42,WHITE,false);

        txt(
            c,
            title,
            back ? 58 : 22,
            37,
            22,
            WHITE,
            true
        );

        txt(
            c,
            "SKZ LightSync • V0.3",
            22,
            getHeight()-16,
            11,
            MUTED,
            false
        );
    }

    @Override
    protected void onDraw(Canvas c) {

        c.drawColor(BG);

        if(screen == 0)
            home(c);

        else if(screen == 1)
            event(c);

        else if(screen == 2)
            sector(c);

        else if(screen == 3)
            control(c);

        else
            execute(c);
    }

    void home(Canvas c) {

        header(c,"SKZ LightSync",false);

        card(c,18,70,getWidth()-18,310);

        txt(
            c,
            "STRAY KIDS FAN PROJECT • BRASIL 🇧🇷",
            34,105,11,BLUE,true
        );

        txt(c,"Uma só luz,",34,150,29,WHITE,true);
        txt(c,"o mesmo sonho.",34,184,29,WHITE,true);

        txt(
            c,
            "V0.3 • controle de luzes",
            34,215,13,MUTED,false
        );

        button(
            c,
            "⚡ Abrir projeto",
            34,245,
            getWidth()-34,
            295
        );

        card(c,18,330,getWidth()-18,455);

        txt(
            c,
            "STRAY KIDS — BRASIL 🇧🇷",
            34,365,16,WHITE,true
        );

        txt(
            c,
            "Projeto disponível offline",
            34,392,12,
            Color.rgb(80,220,140),
            true
        );

        txt(
            c,
            "Nova função: painel de controle",
            34,420,12,MUTED,false
        );
    }

    void event(Canvas c) {

        header(c,"Evento",true);

        card(c,18,70,getWidth()-18,270);

        txt(
            c,
            "FAN PROJECT — BRASIL 🇧🇷",
            34,105,11,BLUE,true
        );

        txt(
            c,
            "STRAY KIDS — BRASIL",
            34,150,25,WHITE,true
        );

        txt(
            c,
            "Estádio Nacional — Brasília",
            34,185,13,MUTED,false
        );

        txt(
            c,
            "Projeto offline",
            34,208,13,MUTED,false
        );

        button(
            c,
            "Escolher meu setor",
            34,300,
            getWidth()-34,
            350
        );
    }

    void sector(Canvas c) {

        header(c,"Meu setor",true);

        for(int i=0;i<sectors.size();i++) {

            float y = 75 + i*82;

            card(
                c,
                18,y,
                getWidth()-18,
                y+66
            );

            Sector s = sectors.get(i);

            p.setColor(s.color);
            c.drawCircle(43,y+33,10,p);

            txt(
                c,
                s.name,
                66,y+29,
                16,
                WHITE,
                true
            );

            txt(
                c,
                "Cor base do setor",
                66,y+50,
                12,
                MUTED,
                false
            );

            if(selectedSector == i) {

                stroke.setColor(WHITE);
                stroke.setStrokeWidth(2);

                c.drawRoundRect(
                    18,y,
                    getWidth()-18,
                    y+66,
                    18,18,
                    stroke
                );
            }
        }

        button(
            c,
            "Abrir painel de luz",
            34,425,
            getWidth()-34,
            475
        );
    }

    void control(Canvas c) {

        header(c,"Controle de luz",true);

        txt(
            c,
            "ESCOLHA UMA COR",
            22,78,12,BLUE,true
        );

        int startY = 100;

        for(int i=0;i<colors.length;i++) {

            int col = i % 2;
            int row = i / 2;

            float l = 22 + col * 155;
            float t = startY + row * 65;
            float r = l + 135;
            float b = t + 50;

            p.setColor(colors[i]);
            c.drawRoundRect(
                l,t,r,b,
                18,18,p
            );

            p.setTextAlign(Paint.Align.CENTER);

            int textColor =
                colors[i] == WHITE ? Color.BLACK : WHITE;

            txt(
                c,
                colorNames[i],
                (l+r)/2,
                t+31,
                13,
                textColor,
                true
            );

            p.setTextAlign(Paint.Align.LEFT);

            if(manualColor == colors[i]) {

                stroke.setColor(WHITE);
                stroke.setStrokeWidth(3);

                c.drawRoundRect(
                    l,t,r,b,
                    18,18,
                    stroke
                );
            }
        }

        txt(
            c,
            "EFEITO",
            22,305,12,BLUE,true
        );

        card(c,18,320,getWidth()-18,465);

        txt(
            c,
            effect == 0 ? "Luz fixa" :
            effect == 1 ? "Pulsar" :
            "Alternar cores",
            34,355,18,WHITE,true
        );

        txt(
            c,
            effect == 0 ?
            "A luz permanece na cor escolhida." :
            effect == 1 ?
            "A intensidade da luz pulsa." :
            "As cores mudam automaticamente.",
            34,382,12,MUTED,false
        );

        button(
            c,
            running ? "PARAR" : "INICIAR",
            34,410,
            getWidth()-34,
            455
        );
    }

    void execute(Canvas c) {

        if(effectStart == 0)
            effectStart = SystemClock.elapsedRealtime();

        long elapsed =
            SystemClock.elapsedRealtime()
            - effectStart;

        int color = manualColor;

        if(effect == 1) {

            float wave =
                (float)Math.sin(elapsed / 300.0);

            float brightness =
                0.15f + 0.85f * ((wave + 1f)/2f);

            int r = (int)
                (Color.red(manualColor)*brightness);

            int g = (int)
                (Color.green(manualColor)*brightness);

            int b = (int)
                (Color.blue(manualColor)*brightness);

            color = Color.rgb(r,g,b);
        }

        else if(effect == 2) {

            int index =
                (int)((elapsed / 1200) % colors.length);

            color = colors[index];
        }

        p.setColor(color);

        c.drawRect(
            0,60,
            getWidth(),
            getHeight()-55,
            p
        );

        p.setTextAlign(Paint.Align.CENTER);

        int textColor =
            color == Color.WHITE ?
            Color.BLACK :
            WHITE;

        txt(
            c,
            "SKZ LIGHTSYNC",
            getWidth()/2,
            190,
            25,
            textColor,
            true
        );

        txt(
            c,
            effect == 0 ?
            "LUZ FIXA" :
            effect == 1 ?
            "PULSANDO" :
            "ALTERNANDO",
            getWidth()/2,
            235,
            18,
            textColor,
            true
        );

        txt(
            c,
            "SETOR: " +
            sectors.get(selectedSector).name,
            getWidth()/2,
            280,
            14,
            textColor,
            false
        );

        txt(
            c,
            "SINCRONIZADO • OFFLINE",
            getWidth()/2,
            315,
            12,
            textColor,
            true
        );

        p.setTextAlign(Paint.Align.LEFT);

        if(running)
            handler.postDelayed(
                () -> invalidate(),
                50
            );
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if(e.getAction() != MotionEvent.ACTION_UP)
            return true;

        float x = e.getX();
        float y = e.getY();

        if(screen == 0 &&
           y > 230 && y < 320) {

            screen = 1;
        }

        else if(screen == 1 &&
                y > 285 && y < 380) {

            screen = 2;
        }

        else if(screen == 2) {

            if(y >= 70 && y < 405) {

                int i =
                    (int)((y-75)/82);

                if(i >= 0 &&
                   i < sectors.size()) {

                    selectedSector = i;
                }
            }

            else if(y > 405) {

                screen = 3;
            }
        }

        else if(screen == 3) {

            // Seleção de cores
            if(y >= 100 && y < 290) {

                int col =
                    x < getWidth()/2 ? 0 : 1;

                int row =
                    (int)((y-100)/65);

                int index =
                    row*2 + col;

                if(index >= 0 &&
                   index < colors.length) {

                    manualColor =
                        colors[index];

                    effect = 0;
                }
            }

            // Seleção do efeito
            else if(y >= 320 && y < 405) {

                effect++;

                if(effect > 2)
                    effect = 0;
            }

            // Iniciar/parar
            else if(y >= 405) {

                running = !running;

                if(running) {
                    effectStart =
                        SystemClock.elapsedRealtime();

                    screen = 4;
                }
            }
        }

        else if(screen == 4) {

            if(y < 60) {

                running = false;
                screen = 3;
            }
        }

        invalidate();

        return true;
    }
}
