package karpushinkirill.gr303.myapplication;

import java.util.List;

import karpushinkirill.gr303.myapplication.model.LinkItem;
import karpushinkirill.gr303.myapplication.model.NodeItem;

public interface OnPointXYChangedListener {

    void onXYChanged(NodeItem newNode, List<LinkItem> newLinks);
}