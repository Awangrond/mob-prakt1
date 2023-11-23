package karpushinkirill.gr303.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

public class GraphView extends SurfaceView {
    Graph graph = new Graph();
    Paint paint;

    int selected1 = -1;
    int selected2 = -1;
    int lasthit = -1;


    float rad = 10.0f;
    float halfside = 5.0f;

    float last_x;
    float last_y;

    public GraphView(Context context, AttributeSet attrs){
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        setWillNotDraw(false);
    }


    public int get_node_at_xy(float x, float y) {
        for (int i = graph.node.size() - 1; i >= 0; i--) {
            Node n = graph.node.get(i);
            float dx = x- n.x;
            float dy = y - n.y;
            if (dx * dx + dy * dy <= rad * rad) return i;
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                int i = get_node_at_xy(x, y);
                lasthit = i;
                if (i < 0) {
                    selected1 = -1;
                    selected2 = -1;
                } else {
                    if (selected1 >= 0) selected2 = i;
                    else selected1 = i;
                }
                last_x = x;
                last_y = y;
                invalidate();
                return true;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE: {
                if (lasthit >= 0) {
                    Node n = graph.node.get(lasthit);
                    n.x += x - last_x;
                    n.y += y - last_y;
                    invalidate();
                }
                last_x = x;
                last_y = y;
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.rgb(255,255,255));

        for (int i = 0; i < graph.link.size(); i++){
            Link l = graph.link.get(i);
            Node na = graph.node.get(l.a);
            Node nb = graph.node.get(l.b);
            paint.setColor(Color.argb(127,0,0,0));
            canvas.drawLine(na.x, na.y, nb.x, nb.y, paint);
            float bx = (na.x + nb.x) * 0.5f;
            float by = (na.y + nb.y) * 0.5f;
            float x0 = bx - halfside;
            float x1 = bx + halfside;
            float yo = by - halfside;
            float y1 = by + halfside;
            canvas.drawRect(x0, yo, x1, y1, paint);
        }

        for (int i = 0; i < graph.node.size(); i++) {
            Node n = graph.node.get(i);
            paint.setStyle(Paint.Style.FILL);

            if (i == selected1) paint.setColor(Color.argb( 50,127,0,255));
            else if (i == selected2) paint.setColor(Color.argb(50,255,0,50));
            else paint.setColor(Color.argb( 50,0,127,255));

            canvas.drawCircle(n.x, n.y, rad, paint);

            paint.setStyle(Paint.Style.STROKE);

            if (i == selected1) paint.setColor(Color.rgb(127,0,255));
            else if (i == selected2) paint.setColor(Color.rgb( 255,0,50));
            else paint.setColor(Color.rgb( 0,127,255));

            canvas.drawCircle(n.x, n.y, rad, paint);
        }
    }

}