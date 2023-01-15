import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.imageio.*;

public class MainForm extends JFrame{
    private JPanel root;
    private JPanel slidePanel;
    private JPanel contentPanel;
    private JButton queryButton;
    private JButton insertButton;
    private JButton deleteButton;
    private JPanel queryPanel;
    private JPanel objType;
    private JPanel deletePanel;
    private JButton updateButton;
    private JButton setButton;
    private JPanel updatePanel;
    private JPanel insertPanel;
    private JTextField subField;
    private JComboBox typeBox;
    private JTextField chapterField;
    private JButton addBtn_iP;
    private JButton clearBtn;
    private JPanel contentAP;
    private JPanel ansAP;
    private JTextField textField1;
    private JTextField textField3;
    private JButton update_add_Button;
    private JComboBox cname_qP;
    private JComboBox type_qP;
    private JButton queryBtn_qP;
    private JComboBox chapter_qP;
    private JPanel contentPanel_qP;
    private JButton helpBtn_qP;

    private JTextField contentField;

    private Connection conn = null;

    private PreparedStatement pstmt = null;

    private  ResultSet rs = null;

    public MainForm(Connection conn){
        add(root);
        //连接数据库
        this.conn = conn;

        //侧边栏消除按钮边框
        queryButton.setFocusPainted(false);
        queryButton.setBorderPainted(false);
        insertButton.setBorderPainted(false);
        deleteButton.setBorderPainted(false);
        updateButton.setBorderPainted(false);
        setButton.setBorderPainted(false);

        //标题栏图标和框架设置
        setTitle("试题管理");
        ImageIcon logo = new ImageIcon("img/logo.png");
        setIconImage(logo.getImage());
        setSize(555,480);
        setLocationRelativeTo(null);
        //setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //内容部分用卡片布局实现不同面板切换
        CardLayout cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);
        Image  markImg = Toolkit.getDefaultToolkit().getImage("img/mark.png");
        Image  bgImg = Toolkit.getDefaultToolkit().getImage("img/bg.png");
        drawPanel setPanel = new drawPanel(bgImg);
        drawPanel maskPanel = new drawPanel(markImg);

        contentPanel.add(maskPanel,"mask");
        contentPanel.add(queryPanel,"query");
        contentPanel.add(insertPanel,"insert");
        contentPanel.add(deletePanel,"delete");
        contentPanel.add(updatePanel,"update");
        contentPanel.add(setPanel,"set");
        ActionListener cardMainEvent = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource()==queryButton){
                    cardLayout.show(contentPanel,"query");
                } else if (e.getSource()==insertButton) {
                    cardLayout.show(contentPanel,"insert");
                } else if (e.getSource()==deleteButton) {
                    cardLayout.show(contentPanel,"delete");
                } else if (e.getSource()==updateButton) {
                    cardLayout.show(contentPanel,"update");
                } else if (e.getSource()==setButton) {
                    cardLayout.show(contentPanel,"set");
                }

            }
        };
        queryButton.addActionListener(cardMainEvent);
        insertButton.addActionListener(cardMainEvent);
        deleteButton.addActionListener(cardMainEvent);
        updateButton.addActionListener(cardMainEvent);
        setButton.addActionListener(cardMainEvent);

        //insertPanel
        JTextArea contentArea = new JTextArea(10,30);
        JTextArea ansArea = new JTextArea(5,30);
        ansArea.setLineWrap(true);
        contentArea.setLineWrap(true);
        JScrollPane sp = new JScrollPane(contentArea);
        JScrollPane ap = new JScrollPane(ansArea);
        ansAP.add(ap);
        contentAP.add(sp);
        typeBox.addItem("简答");
        typeBox.addItem("选择");
        typeBox.addItem("判断");
        typeBox.addItem("填空");
        typeBox.setSelectedIndex(-1);

        //queryPanel
        helpBtn_qP.setBorderPainted(false);
        try {
            //cname_qP.removeAllItems();
            String sql_Cname = "select distinct Cname from course";
            pstmt = conn.prepareStatement(sql_Cname);
            rs = pstmt.executeQuery();
            while (rs.next()){
                String cname = rs.getString("Cname");
                cname_qP.addItem(cname);
            }
            cname_qP.setSelectedIndex(-1);
        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }

        JTextArea contentArea_qP = new JTextArea(15,35);
        contentArea_qP.setFont( new Font("宋体",Font.BOLD,18));
        contentArea_qP.setLineWrap(true);
        contentArea_qP.setEditable(false);
        JScrollPane contentArea_sP = new JScrollPane(contentArea_qP);

        //事件监听
        contentPanel_qP.add(contentArea_sP);
        queryBtn_qP.addActionListener(new ActionListener() {
            //添加按钮
            @Override
            public void actionPerformed(ActionEvent e) {
                contentArea_qP.setText("");
                String cname = (String) cname_qP.getSelectedItem();
                int chapter = 0;
                String type = null;
                String sql = "select data,qtype " +
                        "from question,course " +
                        "where Cname = ? and question.Cno=course.Cno ";
                String sql_ch = " and chapter=? ";
                String sql_ty = " and qtype=? ";
                int state = 1;

                try {
                    if((String)chapter_qP.getSelectedItem()!="全部"){
                        sql = sql + sql_ch;
                        chapter = Integer.parseInt((String)chapter_qP.getSelectedItem());
                        state = 2;
                    }

                    if((String)type_qP.getSelectedItem()!="全部"){
                        sql = sql + sql_ty;
                        type = (String)type_qP.getSelectedItem();
                        state = 3;
                    }

                    if ((String)chapter_qP.getSelectedItem()!="全部"&&(String)type_qP.getSelectedItem()!="全部"){
                        state = 4;
                    }


                    pstmt = conn.prepareStatement(sql);
                    System.out.println(sql);
                    System.out.println(cname);
                    System.out.println(chapter);
                    System.out.println(type);
                    System.out.println(state);
                    pstmt.setString(1,cname);
                    switch (state){
                        case 1:
                            pstmt.setString(1,cname);
                            break;
                        case 2:
                            pstmt.setInt(2, chapter);
                            pstmt.setString(1,cname);
                            break;
                        case 3:
                            pstmt.setString(2, type);
                            pstmt.setString(1,cname);
                            break;
                        case 4:
                            pstmt.setString(2, type);
                            pstmt.setInt(3,chapter);
                            pstmt.setString(1,cname);
                            break;
                    }

                    //显示查询结果
                    int rowNum = 0;
                    ArrayList<String> questionData = new ArrayList<String>();
                    ArrayList<String> questionType = new ArrayList<String>();
                    rs = pstmt.executeQuery();
                    while (rs.next()){
                        questionData.add(rs.getString("data"));
                        questionType.add(rs.getString("qtype"));
                        rowNum++;
                    }
                    contentArea_qP.append("查到题目数："+Integer.toString(rowNum)+"\n\n");
                    for (int i=0; i<questionData.size();i++){
                        contentArea_qP.append("("+Integer.toString(i+1)+") ");
                        contentArea_qP.append("["+questionType.get(i)+"]"+"\n");
                        contentArea_qP.append("     "+questionData.get(i)+"\n\n");

                    }

                }
                catch (Exception err){
                    System.out.println(err.getMessage());
                }
            }
        });
        cname_qP.addItemListener(new ItemListener() {
            //选择课程
            @Override
            public void itemStateChanged(ItemEvent e) {
                String cname = (String) cname_qP.getSelectedItem();
                try {

                    type_qP.removeAllItems();
                    chapter_qP.removeAllItems();
                    type_qP.addItem("全部");
                    chapter_qP.addItem("全部");
                    //设置题型
                    String sql = "select distinct qtype " +
                            "from course,question " +
                            "where course.Cname=? and course.Cno = question.Cno ";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1,cname);
                    rs = pstmt.executeQuery();
                    while (rs.next()){
                        String type = rs.getString(1);
                        type_qP.addItem(type);
                    }
                    //设置章节
                    sql = "select distinct chapter " +
                            "from course,question " +
                            "where course.Cname=? and course.Cno = question.Cno " +
                            "order by chapter";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1,cname);
                    rs = pstmt.executeQuery();
                    while (rs.next()){
                        String type = rs.getString(1);
                        chapter_qP.addItem(type);
                    }

                    type_qP.setSelectedIndex(-1);
                    chapter_qP.setSelectedIndex(-1);
                }
                catch (Exception err){
                    System.out.println(err.getMessage());
                }
            }
        });
        addBtn_iP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sql = "test";
                try {
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                }
                catch (Exception err){
                    System.out.println("添加错误");
                }
            }
        });
    }

}
