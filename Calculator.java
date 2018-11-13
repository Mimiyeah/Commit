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
		//如果需要更新一个组件但事件监听器中的代码并没有执行的时使用SwingUtilitiesinvokeLater方法
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				f.setTitle(f.getClass().getSimpleName());//获取低层类的简称
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//隐藏窗体，程序后台运行
				f.setSize(width,length);//设置大小
				f.setVisible(true);//使控件显示出来
			} 	
	     });
    }//主控制台
 }
 
public class Calculator extends JFrame{
	private JTextField textField;   			//输入文本框
	private String input;						//结果
	private String operator;					//操作符	
	public Calculator() {
		input = "";
		operator = "";
		Container container = this.getContentPane();
		JPanel panel = new JPanel();
		textField = new JTextField(30);
		textField.setEditable(false);						//文本框禁止编辑
		textField.setHorizontalAlignment(JTextField.LEFT);
		//textField.setBounds(100, 100, 20, 20);			//在容器布局为空情况下生效
		textField.setPreferredSize(new Dimension(200,30));
		container.add(textField, BorderLayout.NORTH);		//将文本框放置于容器的NORTH区域
		String[] name= {"7","8","9","+","4","5","6","-","1","2","3","*","0","C","=","/"};
		panel.setLayout(new GridLayout(4,4,1,1));			//s设置4*4网格，水平垂直间距为1
		for(int i=0;i<name.length;i++) {
			JButton button = new JButton(name[i]);
			button.addActionListener(new MyActionListener());
			panel.add(button);								//添加按钮
		}
		container.add(panel,BorderLayout.CENTER);
	}
	
	class MyActionListener implements ActionListener{			//内部类实现按钮响应
		public void actionPerformed(ActionEvent e) {
			int cnt=0;
			String actionCommand = e.getActionCommand();			//获取按钮上的字符串
			if(actionCommand.equals("+") || actionCommand.equals("-") || actionCommand.equals("*")
					|| actionCommand.equals("/")) {
				input += " " + actionCommand + " ";                  //将按钮上对应的运算符号记录
			}
			else if(actionCommand.equals("C")) {					//清除输入将input置空
				input = "";
			} 
			else if(actionCommand.equals("=")) {					//按下等号
				try {												//try...catch捕获异常
					input+= "="+calculate(input);                   //调用计算方法，记录下运算出的值
				} catch (MyException e1) {							//处理捕获到的异常
					if(e1.getMessage().equals("Infinity"))          //判断是否进行除0操作
						input+= "=" + e1.getMessage();				//进行除0则返回Inifinity进行报错
					else
						input = e1.getMessage();                    
				}
				textField.setText(input);                       //将组件中的文字设置成input
				input="";
				cnt = 1;
			}
			else
				input += actionCommand;							//按下数字
			if(cnt == 0)                                        //未按下运算符则不进行计算 直接将input展示在面板上
				textField.setText(input);
		}
	}
	
	private String calculate(String input) throws MyException{				//计算方法
		String[] comput = input.split(" ");			//将字符串input分割成字符串组		
		Stack<Double> stack = new Stack<>();        //建立一个空栈
		Double m = Double.parseDouble(comput[0]);
		stack.push(m);										//第一个操作数入栈
		
		for(int i = 1; i < comput.length; i++) {
			if(i%2==1) {				
				if(comput[i].equals("+"))                    //将前一个数出栈做加法再入栈
					stack.push(Double.parseDouble(comput[i+1]));
				if(comput[i].equals("-"))                     //将前一个数出栈做减法再入栈
					stack.push(-Double.parseDouble(comput[i+1]));
				if(comput[i].equals("*")) {					//将前一个数出栈做乘法再入栈
					Double d = stack.peek();				//查看栈顶元素但不出战
					stack.pop();                             //抛出栈顶元素
					stack.push(d*Double.parseDouble(comput[i+1]));
				}
				if(comput[i].equals("/")) {					//将前一个数出栈做除再入栈
					 double help = Double.parseDouble(comput[i+1]);  
					 if(help == 0)
						 throw new MyException("Infinity");			//出现异常，不会继续执行该函数
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

