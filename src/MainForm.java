import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

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
    private JButton generateButton;
    private JButton setButton;
    private JPanel generatePanel;
    private JPanel insertPanel;
    private JTextField subField;
    private JComboBox typeBox;
    private JTextField chapterField;
    private JButton addBtn_iP;
    private JButton clearBtn;
    private JPanel contentAP;
    private JPanel ansAP;
    private JComboBox cname_qP;
    private JComboBox type_qP;
    private JButton queryBtn_qP;
    private JComboBox chapter_qP;
    private JPanel contentPanel_qP;
    private JButton helpBtn_qP;
    private JTextArea gP_contentArea;
    private JComboBox uP_subjectCombo;
    private JButton gP_generateBtn;
    private JButton gP_outputBtn;
    private JButton gP_questionBtn;
    private JButton gP_addBtn;
    private JButton gP_subBtn;

    private JTextField contentField;

    private Connection conn = null;

    private PreparedStatement pstmt = null;

    private  ResultSet rs = null;

    JTextArea contentArea_qP;

    public Vector<String> subject = new Vector<>();

    private Vector<String> gP_questions = new Vector();

    private Map<String,String> question_number = new HashMap<>();

    String selected_subject = null;

    public MainForm(Connection conn){
        this.conn = conn;
        init();
        query_control();
        insert_control();
        generate_control();
    }

    private void init(){
        add(root);

        //侧边栏消除按钮边框
        queryButton.setFocusPainted(false);
        queryButton.setBorderPainted(false);
        insertButton.setBorderPainted(false);
        deleteButton.setBorderPainted(false);
        generateButton.setBorderPainted(false);
        setButton.setBorderPainted(false);

        //标题栏图标和框架设置
        setTitle("试题管理");
        ImageIcon logo = new ImageIcon("img/logo.png");
        setIconImage(logo.getImage());
        setSize(545,480);
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
        contentPanel.add(generatePanel,"update");
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
                } else if (e.getSource()== generateButton) {
                    cardLayout.show(contentPanel,"update");
                } else if (e.getSource()==setButton) {
                    cardLayout.show(contentPanel,"set");
                }

            }
        };
        queryButton.addActionListener(cardMainEvent);
        insertButton.addActionListener(cardMainEvent);
        deleteButton.addActionListener(cardMainEvent);
        generateButton.addActionListener(cardMainEvent);
        setButton.addActionListener(cardMainEvent);
        try {
            //cname_qP.removeAllItems();
            String sql_Cname = "select distinct Cname from course";
            pstmt = conn.prepareStatement(sql_Cname);
            rs = pstmt.executeQuery();
            while (rs.next()){
                String cname = rs.getString("Cname");
                subject.add(cname);
            }

        }
        catch (Exception err){
            System.out.println(err.getMessage());
        }
    }
    private void query_control(){
        //queryPanel
        helpBtn_qP.setBorderPainted(false);
        this.contentArea_qP = new JTextArea(15,35);
        contentArea_qP.setFont( new Font("宋体",Font.BOLD,18));
        contentArea_qP.setLineWrap(true);
        contentArea_qP.setEditable(false);
        JScrollPane contentArea_sP = new JScrollPane(contentArea_qP);
        gP_questionBtn.setBorderPainted(false);
        for(String s:subject){
            cname_qP.addItem(s);
        }
        cname_qP.setSelectedIndex(-1);

        contentPanel_qP.add(contentArea_sP);
        queryBtn_qP.addActionListener(new ActionListener() {
            //查询按钮
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
                        System.out.println(chapter);
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
                    pstmt.setString(1,cname);
                    switch (state) {
                        case 1 -> pstmt.setString(1, cname);
                        case 2 -> {
                            pstmt.setInt(2, chapter);
                            pstmt.setString(1, cname);
                        }
                        case 3 -> {
                            pstmt.setString(2, type);
                            pstmt.setString(1, cname);
                        }
                        case 4 -> {
                            pstmt.setString(3, type);
                            pstmt.setInt(2, chapter);
                            pstmt.setString(1, cname);
                        }
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
    }
    private void insert_control(){
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
    private void generate_control(){
        //设置文本区
        gP_contentArea = new JTextArea();
        gP_contentArea.setFont( new Font("宋体",Font.BOLD,10));
        gP_contentArea.setLineWrap(true);
        gP_contentArea.setEditable(false);
        JScrollPane gP_contentArea_sP = new JScrollPane(gP_contentArea);
        generatePanel.add(BorderLayout.CENTER,gP_contentArea_sP);

        //generateAction
        gP_subBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PaperForm pf = new PaperForm(subject,getFrame());
            }
        });
        gP_generateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Random random = new Random();

                random.setSeed(10000L);
                for (String s:question_number.keySet()){
                    try{
                        String content = "[" + s+ "]\n";
                        gP_contentArea.append(content);
                        String sql = "select count(*) " +
                                "from question,course " +
                                "where question.Cno = course.Cno and course.Cname = ? and question.qtype = ?";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1,selected_subject);
                        pstmt.setString(2,s);
                        rs = pstmt.executeQuery();
                        rs.next();
                        int total=rs.getInt(1);
                        //System.out.println(total);


                        String sql_2 = "select data " +
                                "from question,course " +
                                "where question.Cno = course.Cno and course.Cname = ? and question.qtype = ?";
                        pstmt = conn.prepareStatement(sql_2);
                        pstmt.setString(1,selected_subject);
                        pstmt.setString(2,s);
                        rs = pstmt.executeQuery();

                        Vector<Integer> numbers = new Vector<Integer>();
                        for(int i = 0; i < Integer.parseInt(question_number.get(s))&&i < total+1;){
                            int num = random.nextInt(total+1);
                            if(!numbers.contains(num)&&num>0){
                                numbers.add(num);
                                //System.out.println("random:"+num);
                                i++;
                            }
                        }
                        int i = 0;
                        Collections.sort(numbers);
                        while (rs.next()&&i<numbers.size()) {
                            if(rs.getRow()==numbers.get(i)){
                                content = "(" +(i+1)+") " + rs.getString(1)+"\n";
                                gP_contentArea.append(content);
                                i++;
                            }
                        }

                    }
                    catch (Exception err){
                        System.out.println(err.getMessage());
                    }
                }
            }
        });
        gP_outputBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        gP_addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String sql = "select distinct 题型 " +
                            "from course_qtype_use " +
                            "where 课程名 = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1,selected_subject);
                    rs = pstmt.executeQuery();
                    gP_questions.clear();
                    while (rs.next()){
                        gP_questions.add(rs.getString(1));
                        System.out.println(rs.getString(1));
                    }
                }
                catch (Exception err){
                    System.out.println(err.getMessage());
                }
                System.out.println(selected_subject);
                new add_question_Dialoge(question_number,selected_subject,gP_questions);
            }
        });
    }

    private MainForm getFrame(){
        return this;
    }
}
