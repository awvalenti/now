package nexo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

public class Main {

	public static void main(String[] args) throws IOException {

		JTextPane outputArea = new JTextPane();
		JTextPane inputArea = new JTextPane(/*""
				+ "#include <stdio.h>\n"
				+ "\n"
				+ "int main(void)\n"
				+ "{\n"
				+ "\tprintf(\"Hello, world!\");\n"
				+ "\treturn 0;\n"
				+ "}\n"
				+ ""*/);
//		inputArea.setTabSize(4);


		Font codeFont = new Font(Font.MONOSPACED, Font.PLAIN, 18);

		Dimension size = new Dimension(470, 600);

		inputArea.setPreferredSize(size);
		inputArea.setFont(codeFont);
		outputArea.setPreferredSize(size);
		outputArea.setFont(codeFont);


		JPanel inputPanel = new JPanel(new BorderLayout());
		JPanel outputPanel = new JPanel(new BorderLayout());
		inputPanel.setBorder(makeBorder("Source code"));
		outputPanel.setBorder(makeBorder("Output"));

		Component inputPane =
				inputArea;
//				new JScrollPane (inputArea,
//				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		Component outputPane =
				outputArea;
//				new JScrollPane (outputArea,
//				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


		inputPanel.add(inputPane, BorderLayout.CENTER);
		outputPanel.add(outputPane, BorderLayout.CENTER);

		JFrame frame = new JFrame("Nexo");
		frame.setSize(1027, 768);
		frame.add(inputPanel,BorderLayout.WEST);
		frame.add(outputPanel,BorderLayout.EAST);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		Runnable codeInterpreter = () -> {
			try {
				Process proc = Runtime.getRuntime().exec("C:\\desenvolvimento\\ferramentas\\c\\tcc\\win32\\tcc -run -", null, null);
				OutputStream outputStream = proc.getOutputStream();
				outputStream.write(inputArea.getText().getBytes());
				outputStream.close();

				byte[] buffer = new byte[32768];

				InputStream input;
				int readCount;

				input = proc.getInputStream();
				readCount = input.read(buffer);
				input.close();
				if (readCount > 0) {
					outputArea.setForeground(Color.BLACK);
					outputArea.setText(new String(buffer, 0, readCount));
				} else {
					input = proc.getErrorStream();
					readCount = input.read(buffer);
					input.close();
					if (readCount > 0) {
						outputArea.setForeground(Color.RED);
						outputArea.setText(new String(buffer, 0, readCount));
					} else {
						outputArea.setText(null);
					}
				}

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		};

		codeInterpreter.run();

		inputArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				SwingUtilities.invokeLater(codeInterpreter);
			}
		});
	}

	private static TitledBorder makeBorder(String text) {
		return new TitledBorder(null, text, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION,
				new Font(Font.SANS_SERIF, Font.BOLD, 16));
	}

}
