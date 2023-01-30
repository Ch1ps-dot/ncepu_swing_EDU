import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Map;
import java.util.Vector;

public class add_question_Dialoge extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox questionBox;
    private JTextField questionNumber;
    private JPanel tablePanel;

    private DefaultTableModel m;

    private JTable table;

    private Vector dataV = new Vector<>();
    private Vector columnName = new Vector<>();
    private Map qn;
    public add_question_Dialoge(Map question_number, String type,Vector<String> question ) {

        qn = question_number;
        columnName.add("题型");
        columnName.add("数量");
        m = new DefaultTableModel(dataV,columnName);
        table = new JTable(m);
        JScrollPane p = new JScrollPane(table);
        tablePanel.add(p);
        setVisible(true);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(500,200);
        setLocationRelativeTo(null);
        for(String s:question){
            questionBox.addItem(s);
        }
        questionBox.setSelectedIndex(-1);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        if(questionBox.getSelectedItem()!=null&& questionNumber.getText()!=null){
            Vector<String> data = new Vector<>();
            data.add((String) questionBox.getSelectedItem());
            data.add(questionNumber.getText());
            qn.put((String)questionBox.getSelectedItem(),questionNumber.getText());
            m.addRow(data);
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
