package pl.com.bottega.functional.basics.scrable;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

class ScrableHelperApp {

    private final Dictionary dictionary;
    private JTextField lettersTextField;
    private JTextArea matchingWordsArea;
    private JPanel outputPanel;

    ScrableHelperApp() throws Exception {
        var dictionaryLoader = new DictionaryLoader(getClass().getResource("/dictionary.txt").toURI());
        this.dictionary = new Dictionary(dictionaryLoader.load());
    }

    public static void main(String[] args) throws Exception {
        new ScrableHelperApp().showUI();
    }

    private void showUI() {
        JFrame jFrame = new JFrame("Scrable Helper");
        jFrame.setSize(800, 600);
        var layout = new BorderLayout();
        jFrame.setLayout(layout);
        addLettersInputPanel(jFrame);
        addWordsOutputPanel(jFrame);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addWordsOutputPanel(JFrame jFrame) {
        outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        matchingWordsArea = new JTextArea();
        matchingWordsArea.setEditable(false);
        var scrollPane = new JScrollPane(matchingWordsArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);
        outputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        jFrame.add(outputPanel, BorderLayout.CENTER);
    }

    private void addLettersInputPanel(JFrame jFrame) {
        var lettersInputPanel = new JPanel();
        lettersInputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        jFrame.add(lettersInputPanel, BorderLayout.NORTH);
        var layout = new BorderLayout();
        layout.setHgap(10);
        lettersInputPanel.setLayout(layout);
        lettersTextField = new JTextField();
        waitForLettersInput();
        lettersInputPanel.add(lettersTextField, BorderLayout.CENTER);
        lettersInputPanel.add(new JLabel("Your letters:"), BorderLayout.WEST);
    }

    private void waitForLettersInput() {
        lettersTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                showMatchingWords();
            }
        });
    }

    private void showMatchingWords() {
        var text = lettersTextField.getText().trim();
        SwingUtilities.invokeLater(() -> {
            var letters = new LinkedList<Character>();
            for(var c : text.toCharArray()) {
                letters.add(c);
            }
            var possibleWords = dictionary.findPossibleWords(letters);
            var sb = new StringBuilder();
            for(String word : possibleWords) {
                sb.append(word + "\n");
            }
            if(text.length() == 0) {
                matchingWordsArea.setText("");
            }
            else if(sb.length() == 0) {
                matchingWordsArea.setText("No matches found");
            } else {
                matchingWordsArea.setText(sb.toString());
            }
            matchingWordsArea.setCaretPosition(0);
            outputPanel.revalidate();
        });
    }
}
