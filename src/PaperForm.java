import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class PaperForm extends JFrame{

    private JPanel root;
    private JButton okBtn;
    private JComboBox subject_Combo;

    private Vector<String> subject;

    public String obj;

    private MainForm mf;
    PaperForm(){
        init();
    }
    PaperForm(Vector<String> subject, MainForm mf){
        init();
        this.subject = subject;
        this.mf = mf;
        for(int i = 0; i < subject.size(); i++){
            subject_Combo.addItem(subject.get(i));
        }
        subject_Combo.setSelectedIndex(-1);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obj = (String) subject_Combo.getSelectedItem();
                mf.selected_subject = obj;
                onOut();

            }
        });
    }
    private void init(){
        add(root);
        setTitle("选择科目");
        setSize(200,100);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public String getSubject(){
        return obj;
    }

    private void onOut(){
        dispose();
    }


}
