package karpushinkirill.gr303.myapplication.model;

import static java.lang.Math.abs;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class NodeItem {

    static float node_radius = 50.0f;
    public int id;
    public float x;
    public float y;
    public String name;

    public NodeItem(int id, float x, float y, String name) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    //нажали ли внутри точки
    public boolean point_inside(float x, float y) {
        float dx = abs(this.x - x);
        float dy = abs(this.y - y);
        if (dx < node_radius / 2 && dy < node_radius / 2) return true;
        return false;
    }

    //перемещение
    public void translate(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    public  void drawNode(Canvas c, Paint p) {
        c.drawCircle(x, y, node_radius, p);
        Paint text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextSize(40);
        c.drawText(this.name, x - node_radius, y + node_radius * 2, text);
    }

    @Override
    public String toString() {
        return "NodeItem{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                '}';
    }
}


