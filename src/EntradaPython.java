import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntradaPython {
    private JButton encontrarArquivoParaLeituraButton;
    private JPanel Painel;
    private JTextArea saídaDoProgramaTextArea;

    public EntradaPython() {
        encontrarArquivoParaLeituraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saídaDoProgramaTextArea.setText(PythonLexer.main());
            }
        });
    }

    public static void main(String[] args) {
        // Crie uma nova instância da classe EntradaPython
        EntradaPython entradaPython = new EntradaPython();

        // Crie um novo JFrame e configure-o para exibir o painel da classe EntradaPython
        JFrame frame = new JFrame("DataLang Compiler");
        frame.setContentPane(entradaPython.Painel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
