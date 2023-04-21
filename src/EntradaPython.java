import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;


public class EntradaPython {
    private JButton encontrarArquivoParaLeituraButton;
    private JPanel Painel;
    private JTextArea textArea1;
    private JButton SalvarTxt;
    private JScrollPane scrollPane;
    private JTextArea entradatextual;

    public EntradaPython() {
        encontrarArquivoParaLeituraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] output2 = new String[2];
                output2 = PythonLexer.main();
                entradatextual.setText(output2[1]);
                textArea1.setText(output2[0]);
            }
        });
        SalvarTxt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exibe uma caixa de diálogo para selecionar o local e o nome do arquivo a ser salvo
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showSaveDialog(Painel);
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Obtém o arquivo selecionado pelo usuário
                    File arquivo = fileChooser.getSelectedFile();
                    try {
                        // Cria um FileWriter para escrever o conteúdo do JTextArea no arquivo
                        FileWriter writer = new FileWriter(arquivo);
                        writer.write(textArea1.getText());
                        writer.close();
                        JOptionPane.showMessageDialog(Painel, "Arquivo salvo com sucesso!");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Painel, "Erro ao salvar o arquivo!");
                    }
                }
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

        // Centralize o JFrame na tela
        frame.setLocationRelativeTo(null);


        frame.setVisible(true);
    }
}
