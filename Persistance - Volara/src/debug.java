import java.awt.*;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * Created by Kevin on 3/23/2017.
 */
public class debug {
    public static JTextPane debugPanel;
    public static Boolean running = false;
    public static void initDebug(){
        //Set-Up Text Area.
        JTextPane debugOutput = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(debugOutput, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        debugPanel = debugOutput;
        running = true;
        debugOutput.setBackground(Color.black);
        debugOutput.setSelectedTextColor(Color.WHITE);
        debugOutput.setCaretColor(Color.WHITE);
        debug.addLine("Persistance Volara -> Debug Window <- Logs will display here.\n", Color.WHITE, 16, true);
        debug.addLine("--------------------------------------------------------------------------------------------------------\n", Color.YELLOW, 12, true);

        //Set-Up Window
        JFrame debugWindow = new JFrame("debug - Persistance");
        debugWindow.setResizable(false);
        debugWindow.setLocationRelativeTo(null);
        debugWindow.setSize(720, 360);
        debugWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Add Text Area
        debugWindow.add(scrollPane,BorderLayout.CENTER);
        //Show Window.
        debugWindow.setVisible(true);
    }
    public static void addLine(String msg, Color c, int s, Boolean bold)
    {
        if (running == true) {
            debugPanel.setEditable(true);
            //Prepare Styles
            StyleContext sc = StyleContext.getDefaultStyleContext();
            AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

            //Set Styles
            aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Arial");
            aset = sc.addAttribute(aset, StyleConstants.FontSize, s);
            aset = sc.addAttribute(aset, StyleConstants.Bold, bold);
            aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

            //Add Line
            int len = debugPanel.getDocument().getLength();
            debugPanel.setCaretPosition(len);
            debugPanel.setCharacterAttributes(aset, false);
            debugPanel.replaceSelection(msg);
            debugPanel.setEditable(false);
        }
    }
}
