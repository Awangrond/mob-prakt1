package karpushinkirill.gr303.myapplication.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.Objects;

public class LinkItem {

    public Integer id;
    public String text;

    public Integer firstNodeId;
    public Float firstX;
    public Float firstY;

    public Integer secondNodeId;
    public Float secondX;
    public Float secondY;

    //конструктор
    public LinkItem(Integer id, String text, Integer firstNodeId, Float firstX, Float firstY, Integer secondNodeId, Float secondX, Float secondY) {
        this.id = id;
        this.text = text;
        this.firstNodeId = firstNodeId;
        this.firstX = firstX;
        this.firstY = firstY;
        this.secondNodeId = secondNodeId;
        this.secondX = secondX;
        this.secondY = secondY;
    }

    public void drawLink(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(3);
        canvas.drawLine(firstX, firstY, secondX, secondY, paint);
        fillArrow(paint, canvas, firstX, firstY, secondX, secondY);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(35);
        canvas.drawText(text, (secondX + ((secondX + firstX) / 2)) / 2, (secondY + ((secondY + firstY) / 2)) / 2, textPaint);
    }

    //перемещение
    public void translate(Integer nodeId, Float newX, Float newY) {
        if (Objects.equals(nodeId, firstNodeId)) {
            this.firstX += newX;
            this.firstY += newY;
        } else {
            this.secondX += newX;
            this.secondY += newY;
        }
    }

    private void fillArrow(Paint paint, Canvas canvas, float x0, float y0, float x1, float y1) {
        paint.setStyle(Paint.Style.FILL);

        float deltaX = x1 - x0;
        float deltaY = y1 - y0;
        double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
        float frac = (float) (1 / (distance / 20));

        float point_x_1 = x0 + (float) ((1 - frac) * deltaX + frac * deltaY);
        float point_y_1 = y0 + (float) ((1 - frac) * deltaY - frac * deltaX);

        float point_x_2 = x1;
        float point_y_2 = y1;

        float point_x_3 = x0 + (float) ((1 - frac) * deltaX - frac * deltaY);
        float point_y_3 = y0 + (float) ((1 - frac) * deltaY + frac * deltaX);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo(point_x_1, point_y_1);
        path.lineTo(point_x_2, point_y_2);
        path.lineTo(point_x_3, point_y_3);
        path.lineTo(point_x_1, point_y_1);
        path.lineTo(point_x_1, point_y_1);
        path.close();

        canvas.drawPath(path, paint);
    }
}
