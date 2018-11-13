import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import javax.swing.*;
 
class MyException extends Exception{
	public MyException() {
		super();
	}
	public MyException(String message) {
		super(message);
	}
}
 
 class SwingConsole{
	public static void run(final JFrame f,final int width,final int length){
		//�����Ҫ����һ��������¼��������еĴ��벢û��ִ�е�ʱʹ��SwingUtilitiesinvokeLater����
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				f.setTitle(f.getClass().getSimpleName());//��ȡ�Ͳ���ļ��
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//���ش��壬�����̨����
				f.setSize(width,length);//���ô�С
				f.setVisible(true);//ʹ�ؼ���ʾ����
			} 	
	     });
    }//������̨
 }
 
public class Calculator extends JFrame{
	private JTextField textField;   			//�����ı���
	private String input;						//���
	private String operator;					//������	
	public Calculator() {
		input = "";
		operator = "";
		Container container = this.getContentPane();
		JPanel panel = new JPanel();
		textField = new JTextField(30);
		textField.setEditable(false);						//�ı����ֹ�༭
		textField.setHorizontalAlignment(JTextField.LEFT);
		//textField.setBounds(100, 100, 20, 20);			//����������Ϊ���������Ч
		textField.setPreferredSize(new Dimension(200,30));
		container.add(textField, BorderLayout.NORTH);		//���ı��������������NORTH����
		String[] name= {"7","8","9","+","4","5","6","-","1","2","3","*","0","C","=","/"};
		panel.setLayout(new GridLayout(4,4,1,1));			//s����4*4����ˮƽ��ֱ���Ϊ1
		for(int i=0;i<name.length;i++) {
			JButton button = new JButton(name[i]);
			button.addActionListener(new MyActionListener());
			panel.add(button);								//��Ӱ�ť
		}
		container.add(panel,BorderLayout.CENTER);
	}
	
	class MyActionListener implements ActionListener{			//�ڲ���ʵ�ְ�ť��Ӧ
		public void actionPerformed(ActionEvent e) {
			int cnt=0;
			String actionCommand = e.getActionCommand();			//��ȡ��ť�ϵ��ַ���
			if(actionCommand.equals("+") || actionCommand.equals("-") || actionCommand.equals("*")
					|| actionCommand.equals("/")) {
				input += " " + actionCommand + " ";                  //����ť�϶�Ӧ��������ż�¼
			}
			else if(actionCommand.equals("C")) {					//������뽫input�ÿ�
				input = "";
			} 
			else if(actionCommand.equals("=")) {					//���µȺ�
				try {												//try...catch�����쳣
					input+= "="+calculate(input);                   //���ü��㷽������¼���������ֵ
				} catch (MyException e1) {							//�����񵽵��쳣
					if(e1.getMessage().equals("Infinity"))          //�ж��Ƿ���г�0����
						input+= "=" + e1.getMessage();				//���г�0�򷵻�Inifinity���б���
					else
						input = e1.getMessage();                    
				}
				textField.setText(input);                       //������е��������ó�input
				input="";
				cnt = 1;
			}
			else
				input += actionCommand;							//��������
			if(cnt == 0)                                        //δ����������򲻽��м��� ֱ�ӽ�inputչʾ�������
				textField.setText(input);
		}
	}
	
	private String calculate(String input) throws MyException{				//���㷽��
		String[] comput = input.split(" ");			//���ַ���input�ָ���ַ�����		
		Stack<Double> stack = new Stack<>();        //����һ����ջ
		Double m = Double.parseDouble(comput[0]);
		stack.push(m);										//��һ����������ջ
		
		for(int i = 1; i < comput.length; i++) {
			if(i%2==1) {				
				if(comput[i].equals("+"))                    //��ǰһ������ջ���ӷ�����ջ
					stack.push(Double.parseDouble(comput[i+1]));
				if(comput[i].equals("-"))                     //��ǰһ������ջ����������ջ
					stack.push(-Double.parseDouble(comput[i+1]));
				if(comput[i].equals("*")) {					//��ǰһ������ջ���˷�����ջ
					Double d = stack.peek();				//�鿴ջ��Ԫ�ص�����ս
					stack.pop();                             //�׳�ջ��Ԫ��
					stack.push(d*Double.parseDouble(comput[i+1]));
				}
				if(comput[i].equals("/")) {					//��ǰһ������ջ��������ջ
					 double help = Double.parseDouble(comput[i+1]);  
					 if(help == 0)
						 throw new MyException("Infinity");			//�����쳣���������ִ�иú���
					 double d = stack.peek();                        
					 stack.pop(); 
					 stack.push(d/help);  
				}
			}
		}
		double d = 0d;
		while(!stack.isEmpty()) {			
			d += stack.peek();
			stack.pop();
		}
		String result = String.valueOf(d);
		return result;
	}
	public static void main(String[] args) {
		SwingConsole.run(new Calculator(), 250, 300);
	}
}

