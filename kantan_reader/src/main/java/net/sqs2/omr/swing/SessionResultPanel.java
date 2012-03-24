package net.sqs2.omr.swing;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.sqs2.omr.master.FormMaster;

public class SessionResultPanel extends JPanel{
	private static final long serialVersionUID = 0L;

	FormMaster master;
	JButton masterButton;
	JButton excelButton;
	JButton csvButton;
	JButton textareaButton;
	JButton chartButton;
	
	public SessionResultPanel(FormMaster master){
		this();
		this.master = master;
	}
	
	public SessionResultPanel(){
		masterButton = new JButton(Messages.RESULT_MASTER_LABEL);
		excelButton = new JButton("Excel");
		csvButton = new JButton("CSV");
		textareaButton = new JButton(Messages.RESULT_TEXTAREA_LABEL);
		chartButton = new JButton(Messages.RESULT_CHART_LABEL);
		setLayout(new BorderLayout());
		add(new CenterPanel(), BorderLayout.CENTER);
	}
	
	public JButton getMasterButton() {
		return masterButton;
	}

	public JButton getExcelButton() {
		return excelButton;
	}

	public JButton getCsvButton() {
		return csvButton;
	}


	public JButton getTextareaButton() {
		return textareaButton;
	}


	public JButton getChartButton() {
		return chartButton;
	}

	class CenterPanel extends JPanel{
		//private static final long serialVersionUID = 0L;
		CenterPanel(){			
			setBackground(Color.WHITE);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(Box.createVerticalGlue());
			if(master != null){
				JPanel u = new UpperPanel();
				add(u);
			}
			JPanel l = new LowerPanel();
			add(l);
			add(Box.createVerticalGlue());
		}
		
		class UpperPanel extends JPanel{
			public UpperPanel(){
				setBackground(Color.WHITE);
				JTextField pathField = new JTextField(master.getFileResourceID().getRelativePath());
				pathField.setEditable(false);
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				add(Box.createHorizontalGlue());
				add(new ItemPanel(Messages.RESULT_MASTER_LABEL, new JComponent[]{masterButton}));
				add(Box.createVerticalStrut(5));
				add(pathField);
				add(Box.createHorizontalGlue());
			}
		}

		class LowerPanel extends JPanel{
			public LowerPanel(){
				setBackground(Color.WHITE);
				setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
				add(Box.createHorizontalGlue());
				add(new ItemPanel(Messages.RESULT_TABLE_LABEL, new JComponent[]{excelButton, csvButton}));
				add(Box.createHorizontalStrut(16));
				add(new ItemPanel(Messages.RESULT_TEXTAREA_LABEL, new JComponent[]{textareaButton}));
				add(Box.createHorizontalStrut(16));
				add(new ItemPanel(Messages.RESULT_CHART_LABEL, new JComponent[]{chartButton}));
				add(Box.createHorizontalGlue());
			}
		}
	}
	

    static class ItemPanel extends JPanel{
		private static final long serialVersionUID = 0L;
		String title;
		ItemPanel(String title, JComponent[] componentArray){
			this.title = title;
			setBackground(Color.WHITE);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			setBorder(new TitledBorder(title));
			boolean isFirst = true;
			for(JComponent coponent: componentArray){
				if(isFirst){
					isFirst = false;
				}else{
					add(Box.createVerticalStrut(16));
				}
				add(coponent);
			}
		}
	}
}
