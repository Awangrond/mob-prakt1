package karpushinkirill.gr303.myapplication;

import static karpushinkirill.gr303.myapplication.MainActivity.database;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import karpushinkirill.gr303.myapplication.model.LinkItem;
import karpushinkirill.gr303.myapplication.model.NodeItem;
import karpushinkirill.gr303.myapplication.databinding.ActivitySecondBinding;
import karpushinkirill.gr303.myapplication.databinding.AddNodeDialogBinding;

public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding binding = null; //инициализируем объект привязки C Sharp в файле build.gradleModule
    private Integer graphId;
    private boolean isConnection = false;
    private boolean isDelete = false;
    private boolean isDisconnection = false;
    private NodeItem firstForConnect = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySecondBinding.inflate(getLayoutInflater());//создание объекта привязки
        setContentView(binding.getRoot()); //стало
        graphId = getIntent().getIntExtra("GRAPH_ID", 0);//получаем id графа с 1активити
        binding.ws.addListeners(
                (newNode, newLinks) -> {
                    database.updateNodePosition(newNode.id, newNode.x, newNode.y);
                    for (LinkItem link: newLinks) {
                        database.updateLink(link);
                    }
                },
                node -> {
                    if (isConnection) {
                        if (firstForConnect == null) {
                            firstForConnect = node;
                        } else {
                            if(!binding.ws.isLinkExists(firstForConnect, node)) {
                                openAddLinkDialog(firstForConnect, node);
                            }
                            isConnection = false;
                            firstForConnect = null;
                        }
                    }

                    if (isDelete) {
                        database.deleteNode(node.id);
                        ArrayList<LinkItem> links = database.getLinks(graphId);
                        for (LinkItem link : links) {
                            if(link.firstNodeId == node.id || link.secondNodeId == node.id) {
                                database.deleteLink(link.id);
                            }
                        }
                        refreshNodes();

                        isDelete = false;
                    }

                    if(isDisconnection){
                        ArrayList<LinkItem> links = database.getLinks(graphId);
                        for (LinkItem link : links) {
                            if (link.firstNodeId == node.id || link.secondNodeId == node.id) {
                                database.deleteLink(link.id);
                            }
                        }
                        refreshNodes();
                        isDisconnection = false;
                    }
                });

        refreshNodes();

        binding.btnExit.setOnClickListener( v -> {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        });

        binding.btnPlus.setOnClickListener(v -> {
            openAddNodeDialog();
        });
        binding.btnDelete.setOnClickListener(v -> {
            isDelete = true;
            isConnection = false;
        });
        binding.btnMulti.setOnClickListener( v -> {
            isConnection = true;
            isDelete = false;
        });
        binding.btnDismulti.setOnClickListener(v -> {
            isDelete = false;
            isConnection = false;
            isDisconnection = true;
        });

    }

    private void refreshNodes() {
        binding.ws.refreshPoints(database.getNodesInGraph(graphId), database.getLinks(graphId));
        binding.ws.invalidate();
    }

    private void openAddNodeDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();

        View dialogView = getLayoutInflater().inflate(R.layout.add_node_dialog, null);
        AddNodeDialogBinding dialogBinding = AddNodeDialogBinding.bind(dialogView);

        dialogBinding.tvTitle.setText(R.string.add_node);

        dialogBinding.btnAdd.setOnClickListener(view -> {
            if (!dialogBinding.etName.getText().toString().isEmpty()) {
                String name = dialogBinding.etName.getText().toString();
                database.addNode(graphId, name, 300f, 300F);
                refreshNodes();
                dialog.dismiss();//закрыть диалог
            }
        });
        dialog.setView(dialogView);
        dialog.show();
    }

    private void openAddLinkDialog(NodeItem firstNode, NodeItem secondNode) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();

        View dialogView = getLayoutInflater().inflate(R.layout.add_node_dialog, null);
        AddNodeDialogBinding dialogBinding = AddNodeDialogBinding.bind(dialogView);

        dialogBinding.tvTitle.setText(R.string.add_link);

        dialogBinding.btnAdd.setOnClickListener(view -> {
            if (!dialogBinding.etName.getText().toString().isEmpty()) {
                String name = dialogBinding.etName.getText().toString();
                database.addLink(graphId, name, firstNode, secondNode);
                refreshNodes();
                dialog.dismiss();//закрыть диалог
            }
        });

        dialog.setView(dialogView);
        dialog.show();
    }
}