package dialogMessage;

import javax.swing.*;

public interface DialogMessageHelper {
    static void dialogMessgae(JFrame mainFrame, String msg, String classe){
        if(classe.equals("erro")){
            JOptionPane.showMessageDialog(mainFrame,msg,"Erro", JOptionPane.ERROR_MESSAGE);
        } else if(classe.equals("info")){
            JOptionPane.showMessageDialog(mainFrame,msg,"Informação", JOptionPane.WARNING_MESSAGE);
        } else if(classe.equals("ok")){
            JOptionPane.showMessageDialog(mainFrame,msg,"Tudo certo", JOptionPane.PLAIN_MESSAGE);
        }
    }
}
