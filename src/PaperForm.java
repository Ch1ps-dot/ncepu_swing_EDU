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
    PaperForm(){
        init();
    }
    PaperForm(Vector<String> subject){
        init();
        this.subject = subject;
        for(int i = 0; i < subject.size(); i++){
            subject_Combo.addItem(subject.get(i));
        }
        subject_Combo.setSelectedIndex(-1);
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obj = (String) subject_Combo.getSelectedItem();
            }
        });
    }
    private void init(){
        add(root);
        setTitle("选择科目");
        setSize(200,100);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public String getSubject(){
        return obj;
    }

//    public static void main(String[] args) {
//        FlatDarkLaf.setup();
//        PaperForm pf = new PaperForm();
//        pf.setLocationRelativeTo(null);
//        pf.setVisible(true);
//        pf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//    }
}
