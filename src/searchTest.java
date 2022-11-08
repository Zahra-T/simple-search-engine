import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray; 
import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 
import org.json.simple.parser.JSONParser;

public class searchTest implements ActionListener{
	static ArrayList<String> summarys;
	static HashMap<String, ArrayList<String>>  hash_word;
	static article[] articles;
	static ArrayList<String> stopwordsArrayList;
	public static void main(String[] args) throws Exception 
  	{ 
		readSummarys();
		set_article_info();
		readStopwords();
		creat_hashmap();
		new searchTest();
  	}
  	public static void readSummarys() throws Exception
  	{
  		JSONParser parser=new JSONParser();
  		JSONArray a = (JSONArray) parser.parse(new FileReader("articles.json"));
  		summarys=new ArrayList<String>();
  		  for (Object o : a)
  		  {
  		    JSONObject person = (JSONObject) o;
  		    
  		    summarys.add((String) person.get("summary"));
  		  }
  	}
  	public static void readStopwords() 
  	{
  		 String  line=null;
		 stopwordsArrayList=new ArrayList<String>();
	      try {
             BufferedReader  br = new  BufferedReader(new FileReader(
                        "stopWords.txt"));
            while((line=br.readLine())!=null)
             {
           	 String[] stopString=line.split(",");
                 for (int i = 0; i < stopString.length; i++) 
                 {
               	  stopwordsArrayList.add(stopString[i]);
                 }
             }
            br.close();
        } catch (IOException e) {
             e.printStackTrace();
        }
	}
  	public static void set_article_info()
  	{
  		articles=new article[summarys.size()]; 
		for(int i=0;i<articles.length;i++)
		{
			articles[i]=new article();
			articles[i].summary=summarys.get(i);
			articles[i].id=String.valueOf(i);
			articles[i].words=articles[i].summary.split(" ");
		}
	}
  	public static void creat_hashmap() 
  	{
  	   hash_word=new HashMap<String, ArrayList<String>>();
  			for(int i=0;i<articles.length;i++)
  			{
  				for(int j=0;j<articles[i].words.length;j++)
  				{
  					if(stopwordsArrayList.contains(articles[i].words[j])==false)
  					{
  						if( hash_word.containsKey(articles[i].words[j])==true)
  						{
  							if(hash_word.get(articles[i].words[j]).contains(articles[i].id)==false)
  							 hash_word.get(articles[i].words[j]).add(articles[i].id);
  						}
  						else 
  						{
  							 hash_word.put(articles[i].words[j], new ArrayList<String>());
  							 hash_word.get(articles[i].words[j]).add(articles[i].id);
  						}
  					}
  				}
  			}
	}
	JFrame frm;
	Container container;
	JButton searchButton;
	static JTextField textField;
	JList<String> List;
	JTextArea textArea;
	JPanel subPanel1;
	JPanel subPanel2;
	JPanel subPanel3;
	JPanel cardPanel;
	CardLayout layout;
	public searchTest()
	{
		frm=new JFrame("Java Search Engine");
		container=new Container();
	    container=frm.getContentPane();
	    container.setLayout(new BorderLayout());
	    textField=new JTextField(30);
		textField.setText("Type here...");
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		searchButton=new JButton("Search");
		searchButton.setMaximumSize(new Dimension(150,50));
		searchButton.setFont(new Font("Trebuchet MS", Font.BOLD, 11));
		searchButton.setBackground(Color.PINK);
		searchButton.addActionListener(this);
		textArea=new JTextArea();
		textArea.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
		layout = new CardLayout();
		cardPanel = new JPanel();
	    cardPanel.setLayout(layout);
		subPanel1=new JPanel();
		subPanel2=new JPanel();
		subPanel3=new JPanel();
		subPanel3.setLayout(new BoxLayout(subPanel3,BoxLayout.Y_AXIS));
		subPanel1.add(textField);
		subPanel1.add(searchButton);
		List=new JList();
		List.setLayoutOrientation(List.VERTICAL_WRAP);
		List.setVisibleRowCount(2);
		List.setFixedCellWidth(100);
		Color color=null;
		List.setSelectionBackground(color.YELLOW);
		List.setSelectionForeground(color.RED);
		Color c = subPanel3.getBackground();
		textArea.setBorder(BorderFactory.createCompoundBorder(
		        BorderFactory.createMatteBorder(20,20,20,20,c.lightGray),textArea.getBorder()));
		List.setFont(new Font("Tahoma", Font.PLAIN, 20));
		subPanel3.add(List);
		subPanel3.add(textArea);
		subPanel1.setBackground(new Color(135, 206, 250));
		subPanel2.setBackground(new Color(135, 206, 250));
		subPanel3.setBackground(new Color(135, 206, 250));
		cardPanel.add(subPanel2,"2");
		cardPanel.add(subPanel3,"3");
		container.add(subPanel1,BorderLayout.PAGE_START);
		container.add(cardPanel,BorderLayout.CENTER);
		frm.setSize(600,600);
        frm.setLocation(400,100);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frm.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(searchButton==e.getSource())
		{
			if(hash_word.containsKey(textField.getText())==false)
				JOptionPane.showMessageDialog(null,"No result!");
			else 
			{
				DefaultListModel<String> l = new DefaultListModel<>();
				if(hash_word.get(textField.getText()).size()>6)
				{
					sort_articles();
					 for(int i=0;i<6;i++)
					 {
						 l.addElement(hash_word.get(textField.getText()).get(i));
					 }
				}
				else
				{
				 for(int i=0;i<hash_word.get(textField.getText()).size();i++)
				 {
					 l.addElement(hash_word.get(textField.getText()).get(i));
				 }
				}
				 List.setModel(l);
				layout.show(cardPanel,"3");
 List.addMouseListener(new MouseListener() {
					@Override
					public void mouseReleased(MouseEvent e) {
					}
					@Override
					public void mousePressed(MouseEvent e) {
					}
					@Override
					public void mouseExited(MouseEvent e) {
					}
					@Override
					public void mouseEntered(MouseEvent e) {
					}
					@Override
					public void mouseClicked(MouseEvent e) {
						if (List.getSelectedIndex() != -1) {
			                textArea.setText(articles[Integer.
			                parseInt(List.getSelectedValue())].summary);
						}
					}
				});
	           }
			}
	}
	//sorting by position of word in article...
	public static void sort_articles()
	{
		int[] position=new int[hash_word.get(textField.getText()).size()];
		int k=0;
		for(int i=0;i<hash_word.get(textField.getText()).size();i++)
		{
			for(int j=0;j<articles[Integer.parseInt(hash_word.get(textField.getText()).get(i))].
					words.length;j++)
			{
				if(textField.getText().
						equals(articles[Integer.parseInt(hash_word.get(textField.getText()).get(i))].words[j]))
				{
					position[k]=j;
					k++;
					break;
				} 
			}	
		}
		ArrayList<String> new_List=new ArrayList<String>();
		for(int i=0;i<500;i++)
		{
			for(int j=0;j<position.length;j++)
			{
				if(position[j]==i)
					new_List.add(hash_word.get(textField.getText()).get(j));
			}
		}
		for(int i=0;i<hash_word.get(textField.getText()).size();i++)
		{
			hash_word.get(textField.getText()).set(i,new_List.get(i));
		}
	/*	int tempInteget=0;
		String tempString=null;
		for(int i=position.length-1;0<i;i--)
		{
			for(int j=0;j<i;j++)
			{
				if(position[j]>=position[j+1])
				{
					tempInteget=position[j];
					position[j]=position[j+1];
					position[j+1]=tempInteget;
					tempString=hash_word.get(textField.getText()).get(j);
					hash_word.get(textField.getText()).
					set(j,hash_word.get(textField.getText()).get(j+1));
					hash_word.get(textField.getText()).
					set(j+1, tempString);
				}
			}
		}*/
	
	}
}
