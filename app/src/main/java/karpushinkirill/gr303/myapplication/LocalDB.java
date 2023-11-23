package karpushinkirill.gr303.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import karpushinkirill.gr303.myapplication.model.GraphItem;
import karpushinkirill.gr303.myapplication.model.LinkItem;
import karpushinkirill.gr303.myapplication.model.NodeItem;

public class LocalDB extends SQLiteOpenHelper {

    public LocalDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "PRAGMA foreign_keys = ON;";
        sqLiteDatabase.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS graphs (";
        sql += " id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += " name TEXT NOT NULL";
        sql += ");";
        sqLiteDatabase.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS nodes (";
        sql += " id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += " graph INTEGER, ";
        sql += " x REAL NOT NULL,";
        sql += " y REAL NOT NULL,";
        sql += " name TEXT NOT NULL,";
        sql += " CONSTRAINT fk_graph FOREIGN KEY (graph) REFERENCES graphs (id) ON DELETE CASCADE";
        sql += " );";
        sqLiteDatabase.execSQL(sql);

        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS links (";
        sql += " id INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += " graph INTEGER, ";
        sql += " text TEXT NOT NULL, ";
        sql += " firstNodeId INTEGER NOT NULL, ";
        sql += " firstNodeX REAL NOT NULL, ";
        sql += " firstNodeY REAL NOT NULL, ";
        sql += " secondNodeId INTEGER NOT NULL, ";
        sql += " secondNodeX REAL NOT NULL, ";
        sql += " secondNodeY REAL NOT NULL, ";
        sql += " CONSTRAINT fk_graphID FOREIGN KEY (graph) REFERENCES graphs (id) ON DELETE CASCADE, ";
        sql += " CONSTRAINT fk_firstID FOREIGN KEY (firstNodeId) REFERENCES nodes (id) ON DELETE CASCADE,";
        sql += " CONSTRAINT fk_secondID FOREIGN KEY (secondNodeId) REFERENCES nodes (id) ON DELETE CASCADE";
        sql += " );";
        sqLiteDatabase.execSQL(sql);

    }

    public ArrayList<GraphItem> getGraphs() {
        ArrayList<GraphItem> graph = new ArrayList<>();
        String sql = "SELECT * FROM graphs;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(sql, null); //запустите запрос и получите результат

        if (cur.moveToFirst()) { //перейти к первой (и единственной) соответствующей записи, если это возможно
            do {//вывод всех полей
                graph.add(
                        new GraphItem(
                                cur.getInt(0),
                                cur.getString(1)
                        )
                );
            } while (cur.moveToNext());
            return graph; //возвращаемое значение из первого столбца (my_value)
        }
        return new ArrayList<>(); //return special text when no result
    }

    public void addGraph(String graphName) {
        String sql = "INSERT INTO graphs (name) VALUES ('" + graphName + "');";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void updateGraphName(Integer graphId, String newName){
        String sql = "";
        sql += "UPDATE graphs";
        sql += " SET name = '" + newName + "'";
        sql += " WHERE id = " + graphId + " ;";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void deleteGraph(Integer graphId){
        String sql = "";
        sql += "DELETE FROM graphs";
        sql += " WHERE id = " + graphId + " ;";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public ArrayList<NodeItem> getNodesInGraph(Integer graphId) {
        ArrayList<NodeItem> nodes = new ArrayList<>();
        String sql = "SELECT * FROM nodes WHERE graph = " + graphId + " ;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(sql, null); //запустите запрос и получите результат

        if (cur.moveToFirst()) { //перейти к первой (и единственной) соответствующей записи, если это возможно
             do { //вывод всех полей
                nodes.add(
                        new NodeItem(
                                cur.getInt(0),
                                cur.getFloat(2),
                                cur.getFloat(3),
                                cur.getString(4)
                        )
                );
            } while (cur.moveToNext());
            return nodes; //возвращаемое значение из первого столбца (my_value)
        }
        return new ArrayList<>();
    }

    public void addNode(Integer graphId, String nodeName, Float x, Float y) {
        ContentValues cv = new ContentValues();
        cv.put("graph", graphId);
        cv.put("name", nodeName);
        cv.put("x", x);
        cv.put("y", y);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("nodes",null,cv);
    }

    public void updateNodePosition(Integer nodeId, float x, float y) {
        String sql = "";
        sql += "UPDATE nodes";
        sql += " SET x = " + x + ", y = " + y;
        sql += " WHERE id = " + nodeId + " ;";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void deleteNode(Integer nodeId) {
        String sql = "";
        sql += "DELETE FROM nodes";
        sql += " WHERE id = " + nodeId + " ;";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public ArrayList<LinkItem> getLinks(Integer graphId) {
        ArrayList<LinkItem> links = new ArrayList<>();
        String sql = "SELECT * FROM links WHERE graph = " + graphId + " ;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cur = db.rawQuery(sql, null); //запустите запрос и получите результат

        if (cur.moveToFirst()) { //перейти к первой (и единственной) соответствующей записи, если это возможно
            do { //вывод всех полей
                links.add(
                        new LinkItem(
                                cur.getInt(0),
                                cur.getString(2),
                                cur.getInt(3),
                                cur.getFloat(4),
                                cur.getFloat(5),
                                cur.getInt(6),
                                cur.getFloat(7),
                                cur.getFloat(8)
                        )
                );

            } while (cur.moveToNext());
            return links;
        }
        return new ArrayList<>();
    }

    public void addLink(Integer graphId, String text, NodeItem firstNode, NodeItem secondNode) {
        ContentValues cv = new ContentValues();
        cv.put("graph", graphId);
        cv.put("text", text);
        cv.put("firstNodeId", firstNode.id);
        cv.put("firstNodeX", firstNode.x);
        cv.put("firstNodeY", firstNode.y);
        cv.put("secondNodeId", secondNode.id);
        cv.put("secondNodeX", secondNode.x);
        cv.put("secondNodeY", secondNode.y);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("links",null, cv);
    }

    public void updateLink(LinkItem newLink) {
        String sql = "";
        sql += " UPDATE links";
        sql += " SET firstNodeX = " + newLink.firstX + ", firstNodeY = " + newLink.firstY + ", secondNodeX = " + newLink.secondX + ", secondNodeY = " + newLink.secondY;
        sql += " WHERE id = " + newLink.id + " ;";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    public void deleteLink(Integer linkId) {
        String sql = "";
        sql += "DELETE FROM links";
        sql += " WHERE id = " + linkId + " ;";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
